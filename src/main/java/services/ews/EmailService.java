package services.ews;

import static microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet.FirstClassProperties;
import static microsoft.exchange.webservices.data.core.enumeration.property.BodyType.Text;
import static microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName.Inbox;
import static microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema.IsRead;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
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
import services.EmployeeDAO;
import utils.Template;
import utils.Utils;

/**
 * 
 * Class with static methods using the EWS Java API to handle Email functionality within the MyCareer project.
 * 
 * @author Ridhwan Nacef
 * @version 1.0
 * @since January 2017
 * @see <a href="https://github.com/OfficeDev/ews-java-api">EWS Java API</a>
 */
@Component
@PropertySource("${ENVIRONMENT}.properties")
public class EmailService
{
  /** Logger Property - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

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
  private EmailService(Environment env)
  {
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
    initiateEWSConnection(20000);
    EmailMessage message = new EmailMessage(emailService);
    message.setSubject(subject);
    message.setBody(new MessageBody(Text, body));
    message.getToRecipients().add(recipient);
    message.sendAndSaveCopy();
  }

  /**
   * Scheduled method that finds all unread Emails every minute and passes to the
   * {@linkplain #analyseAndSortEmail(EmailMessage)} method.
   * 
   * @throws Exception
   */
//  @Scheduled(fixedRate = 30000)
  private void findEmails()
  {
    try
    {
      initiateEWSConnection(120000);

      logger.info("Finding unread Emails...");
      int unreadEmails = Folder.bind(emailService, Inbox).getUnreadCount();

      if (unreadEmails < 1)
      {
        logger.info("No unread Emails...", unreadEmails);
        return;
      }

      ItemView view = new ItemView(unreadEmails);
      FindItemsResults<Item> findResults = emailService.findItems(Inbox, new SearchFilter.IsEqualTo(IsRead, false),
          view);
      PropertySet propertySet = new PropertySet(FirstClassProperties);
      propertySet.setRequestedBodyType(Text);

      emailService.loadPropertiesForItems(findResults, propertySet);

      logger.info("Total number of items found {} ", findResults.getTotalCount());
      for (Item item : findResults)
      {
        analyseAndSortEmail((EmailMessage) item);
      }
      logger.info("Find Emails task complete ");
    }
    catch (Exception e)
    {
      logger.error(e.getMessage());
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
      // Set<EmailAddress> ccRecipients = new HashSet<>(email.getCcRecipients().getItems());
      String subject = email.getSubject().toLowerCase();
      String body = email.getBody().toString().trim();

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
      logger.error(e.getMessage());
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
    logger.info("Requested Feedback found");

    String requestID = Utils.getFeedbackRequestIDFromEmailBody(body);
    if (requestID.isEmpty())
    {
      genericFeedbackFound(from, recipients, body);
      return;
    }
    try
    {
      EmployeeDAO.addRequestedFeedback(from, requestID, body);
    }
    catch (InvalidAttributeValueException | NamingException e)
    {
      String errorRecipient = from;
      String errorSubject = "Error Processing Feedback";
      // String errorBody = "There was an issue processing your feedback, please try reply to the feedback request "
      // + "email and do make sure not to changed any of the details on the email.";
      String errorBody = Template.populateTemplate(env.getProperty("templates.error.invalidfeedback"));

      sendEmail(errorRecipient, errorSubject, errorBody);

      logger.info("Requested Feedback, email sent. Error: {}", e.getMessage());
    }
    logger.info("Requested Feedback processed");
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
    logger.info("Generic Feedback found");
    Set<EmailAddress> errorRecipients = new HashSet<>();

    if (recipients.size() == 1
        && recipients.stream().anyMatch(s -> s.getAddress().equals(env.getProperty("mail.address"))))
    {
      String errorRecipient = from;
      String errorSubject = "Error Processing Feedback";
      String errorBody = Template.populateTemplate(env.getProperty("templates.error.invalidfeedback"));

      sendEmail(errorRecipient, errorSubject, errorBody);

      logger.info("Incorrect Use of Feedback.uk found, email sent.");
      return;
    }

    for (EmailAddress recipient : recipients)
    {
      if (recipient.getAddress().equals(env.getProperty("mail.address"))) continue;

      try
      {
        EmployeeDAO.addFeedback(from, recipient.getAddress(), body, false);
      }
      catch (InvalidAttributeValueException | NamingException e)
      {
        errorRecipients.add(recipient);

        logger.error("Error processing feedback from {} to {}, Error:{}", from, recipient, e.getMessage());
      }
    }
    if (!errorRecipients.isEmpty())
    {
      String errorRecipient = from;
      String errorSubject = "Feedback Issue";
      String errorBody = String.format(
          "There was an issue proccessing your feedback to %s, please make sure the email address is correct and try again",
          errorRecipients.toString());

      sendEmail(errorRecipient, errorSubject, errorBody);

      logger.error("Email sent to {} regarding error feedback to {}", from, errorRecipients.toString());
    }

    logger.info("Generic Feedback processed");
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
    logger.info("Undelivered Email found.");

    long employeeID = Utils.getEmployeeIDFromFeedbackRequestSubject(subject);
    Employee employee = EmployeeDAO.getEmployee(employeeID);
    String intendedRecipient = Utils.getRecipientFromUndeliverableEmail(body);

    String errorRecipient = employee.getEmailAddress();
    String errorSubject = "Feedback Request Issue";
    // String errorBody = String.format("There was an issue proccessing your feedback to %s, please make sure the email
    // address is correct and try again",intendedRecipient);
    String errorBody = Template.populateTemplate(env.getProperty("templates.error.invalidemail"), intendedRecipient);

    sendEmail(errorRecipient, errorSubject, errorBody);

    logger.info("Undelivered Email processed, error email sent.");
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
