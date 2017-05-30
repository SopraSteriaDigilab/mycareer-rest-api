package services.ews;

import static microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet.FirstClassProperties;
import static microsoft.exchange.webservices.data.core.enumeration.property.BodyType.Text;
import static microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName.Inbox;
import static microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema.IsRead;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dataStructure.Employee;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
import services.EmployeeService;
import utils.Template;
import utils.Utils;

/**
 * 
 * Class with static methods using the EWS Java API to handle Email functionality within the MyCareer project.
 * 
 * @see <a href="https://github.com/OfficeDev/ews-java-api">EWS Java API</a>
 */
@Component
@PropertySource("${ENVIRONMENT}.properties")
public class EmailService
{
  /** Logger Property - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  /** TYPE Property|Constant - Represents|Indicates... */
  private static EmployeeService employeeService;

  /** Environment Property - Reference to environment to get property details. */
  private static Environment env;

  /** ExchangeService Property - The service used to connect to the mailbox */
  private static ExchangeService emailService;

  /**
   * Autowired constructor to inject the environment
   * 
   * @param env The Environment of the project that is set in {@link application.Application Application }
   */
  @Autowired
  private EmailService(EmployeeService employeeService, Environment env)
  {
    EmailService.employeeService = employeeService;
    EmailService.env = env;
  }

  /**
   * Static method to send an Email.
   * 
   * @param recipient
   * @param subject
   * @param body
   * @throws Exception
   */
  public synchronized static void sendEmail(String recipient, String subject, String body) throws Exception
  {
    List<String> recipients = Arrays.asList(recipient);
    sendEmail(recipients, subject, body);
    // TODO Consider removing/redoing
  }

  /**
   * Static method to send email to multiple recipients
   *
   * @param recipientsSet
   * @param subject
   * @param body
   * @throws Exception
   */
  public static void sendEmail(Set<String> recipientsSet, String subject, String body) throws Exception
  {
    final List<String> recipientsList = new ArrayList<>();
    recipientsList.addAll(recipientsSet);
    sendEmail(recipientsList, subject, body);
  }

  /**
   * Static method to send an Email.
   * 
   * @param recipient
   * @param subject
   * @param body
   * @throws Exception
   */
  public synchronized static void sendEmail(List<String> recipients, String subject, String body) throws Exception
  {
    initiateEWSConnection(20_000);
    EmailMessage message = new EmailMessage(emailService);
    message.setSubject(subject);
    message.setBody(new MessageBody(Text, body));

    for (String recipient : recipients)
    {
      message.getToRecipients().add(recipient);
    }

    message.sendAndSaveCopy();
  }

  /**
   * Scheduled method that finds all unread Emails every minute and passes to the
   * {@linkplain #analyseAndSortEmail(EmailMessage)} method.
   * 
   * @throws Exception
   */
  @Scheduled(fixedRate = 60_000)
  private void findEmails()
  {
    try
    {
      initiateEWSConnection(120_000);

      LOGGER.debug("Finding unread Emails...");
      int unreadEmails = Folder.bind(emailService, Inbox).getUnreadCount();

      if (unreadEmails < 1)
      {
        LOGGER.debug("No unread Emails...", unreadEmails);
        return;
      }

      ItemView view = new ItemView(unreadEmails);
      FindItemsResults<Item> findResults = emailService.findItems(Inbox, new SearchFilter.IsEqualTo(IsRead, false),
          view);
      PropertySet propertySet = new PropertySet(FirstClassProperties);
      propertySet.setRequestedBodyType(Text);

      emailService.loadPropertiesForItems(findResults, propertySet);

      LOGGER.debug("Total number of items found {} ", findResults.getTotalCount());
      for (Item item : findResults)
      {
        analyseAndSortEmail((EmailMessage) item);
      }
      LOGGER.debug("Find Emails task complete ");
    }
    catch (Exception e)
    {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Analyses an Email and sorts between generic feedback, feedback request or undelivered Email.
   * 
   * @param email
   * @throws ServiceLocalException
   * @throws Exception
   */
  private static void analyseAndSortEmail(EmailMessage email)
  {
    try
    {
      String from = email.getFrom().getAddress();
      Set<EmailAddress> recipients = new HashSet<>(email.getToRecipients().getItems());
      String subject = (email.getSubject() == null) ? "" : email.getSubject().toLowerCase();
      String body = (email.getBody() == null) ? "" : email.getBody().toString().trim();

      if (subject.contains("undeliverable"))
      {
        undeliverableFeedbackFound(subject, body);
      }
      else
      {
        if (subject.contains("feedback request"))
        {
          requestedFeedbackFound(from, recipients, body);
        }
        else
        {
          genericFeedbackFound(from, recipients, body);
        }
      }

      email.setIsRead(true);
      email.update(ConflictResolutionMode.AutoResolve);
    }
    catch (Exception e)
    {
      LOGGER.error(e.getMessage(), e);
    }
  }

  /**
   * Method to handle an email that contains Feedback Request in the subject.
   * 
   * @param from
   * @param recipients
   * @param body
   * @throws Exception
   * @throws NamingException
   */
  private static void requestedFeedbackFound(String from, Set<EmailAddress> recipients, String body) throws Exception
  {
    LOGGER.debug("Requested Feedback found");

    String requestID = Utils.getFeedbackRequestIDFromEmailBody(body);
    if (requestID.isEmpty())
    {
      genericFeedbackFound(from, recipients, body);
      return;
    }
    try
    {
      employeeService.addRequestedFeedback(from, requestID, body);
    }
    catch (InvalidAttributeValueException | NamingException e)
    {
      String errorRecipient = from;
      String errorSubject = "Error Processing Feedback";
      String errorBody = Template.populateTemplate(env.getProperty("templates.error.invalidfeedback"));

      sendEmail(errorRecipient, errorSubject, errorBody);

      LOGGER.info("Requested Feedback, email sent. Error: {}", e.getMessage());
    }
    LOGGER.debug("Requested Feedback processed");
  }

  /**
   * Method to handle email that is thought to be generic. (Does not contain feedback request or undelivered email in
   * the subject).
   * 
   * @param from
   * @param recipients
   * @param subject
   * @throws Exception
   * @throws NamingException
   * @throws InvalidAttributeValueException
   */
  private static void genericFeedbackFound(String from, Set<EmailAddress> recipients, String body) throws Exception
  {
    LOGGER.debug("Generic Feedback found");

    final Set<EmailAddress> errorRecipients = new HashSet<>();
    final boolean hasSingleRecipient = recipients.size() == 1;
    final boolean feedbackIsARecipient = recipients.stream()
        .anyMatch(s -> s.getAddress().equals(env.getProperty("mail.address")));
    final boolean feedbackIsSoleRecipient = hasSingleRecipient && feedbackIsARecipient;

    if (feedbackIsSoleRecipient)
    {
      final String errorRecipient = from;
      final String errorSubject = "Error Processing Feedback";
      final String errorBody = Template.populateTemplate(env.getProperty("templates.error.invalidfeedback"));

      sendEmail(errorRecipient, errorSubject, errorBody);
      LOGGER.info("Incorrect Use of Feedback.uk found, email sent.");

      return;
    }

    for (final EmailAddress recipient : recipients)
    {
      final boolean recipientIsFeedback = recipient.getAddress().equals(env.getProperty("mail.address"));

      if (recipientIsFeedback)
      {
        continue;
      }

      try
      {
        employeeService.addFeedback(from, recipient.getAddress(), body, false);
      }
      catch (InvalidAttributeValueException | RuntimeException e)
      {
        errorRecipients.add(recipient);
        LOGGER.error("Exception thrown while processing feedback from {} to {}, Error:{}", from, recipient, e);
      }
      catch (Exception e)
      {
        errorRecipients.add(recipient);
        LOGGER.error("Generic exception thrown while processing feedback from {} to {}, Error:{}", from, recipient,
            e.getMessage(), e);
      }
    }

    if (!errorRecipients.isEmpty())
    {
      final List<String> errorRecipient = Arrays.asList(from);
      final String errorSubject = "Feedback Issue";
      final String errorBody = String.format(
          "There was an issue proccessing your feedback to %s, please make sure the email address is correct and try again.  If this problem persists, please raise a support ticket.",
          errorRecipients.toString());

      sendEmail(errorRecipient, errorSubject, errorBody);
      LOGGER.error("Email sent to {} regarding error feedback to {}", from, errorRecipients.toString());
    }

    LOGGER.debug("Generic Feedback processed");
  }

  /**
   * Method to handle undelivered feedback.
   * 
   * @param subject
   * @param body
   * @throws Exception
   */
  private static void undeliverableFeedbackFound(String subject, String body) throws Exception
  {
    LOGGER.debug("Undelivered Email found.");

    long employeeID = Utils.getEmployeeIDFromFeedbackRequestSubject(subject);
    Employee employee = employeeService.getEmployee(employeeID);
    String intendedRecipient = Utils.getRecipientFromUndeliverableEmail(body);

    Set<String> errorRecipient = employee.getProfile().getEmailAddresses().toSet();
    String errorSubject = "Feedback Request Issue";
    String errorBody = Template.populateTemplate(env.getProperty("templates.error.invalidemail"), intendedRecipient);

    sendEmail(errorRecipient, errorSubject, errorBody);

    LOGGER.info("Undelivered Email processed, error email sent.");
  }

  /**
   * Opens connection with the mailbox with specified timeout.
   * 
   * @param timeout
   * @throws URISyntaxException
   */
  private static void initiateEWSConnection(int timeout) throws URISyntaxException
  {
    if (emailService == null)
    {
      emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
      emailService.setMaximumPoolingConnections(1);
      emailService
          .setCredentials(new WebCredentials(env.getProperty("mail.username"), env.getProperty("mail.password")));
      emailService.setUrl(new URI(env.getProperty("mail.uri")));
      emailService.setTimeout(timeout);
    }
  }
}
