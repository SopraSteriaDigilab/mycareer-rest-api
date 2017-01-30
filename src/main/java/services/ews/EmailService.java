package services.ews;

import static microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet.FirstClassProperties;
import static microsoft.exchange.webservices.data.core.enumeration.property.BodyType.Text;
import static microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName.Inbox;
import static microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema.IsRead;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
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

/**
 * 
 * Class with static methods using the EWS Java API
 * to handle Email functionality within the MyCareer project.
 * 
 * @author Ridhwan Nacef
 * @version 1.0
 * @since January 2017
 * @see <a href="https://github.com/OfficeDev/ews-java-api">EWS Java API</a>
 */

/**
 * @author rnacef
 *
 */
@Component
@PropertySource("${ENVIRONMENT}.properties")
public class EmailService {
	
	/** Defines the types of feedback that can be found. */
	private enum FeedbackType {
		GENERIC,
		REQUEST,
		ERROR
	}
	
	
	/**
	 * Logger for printing.
	 */
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	
	/**
	 * Reference to environment to get property details.
	 */
	private static Environment env;
	
	/**
	 * The service used to connect to the mailbox
	 */
	private static ExchangeService emailService;
	
	
	/**
	 * Autowired constructor to inject the environment
	 * @param env
	 */
	@Autowired
	private EmailService(Environment env){
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
	public synchronized static void sendEmail(String recipient, String subject, String body) throws Exception {
			initiateEWSConnection(20000);
			EmailMessage message = new EmailMessage(emailService);
			message.setSubject(subject);
			message.setBody(new MessageBody(Text, body));
			message.getToRecipients().add(recipient);
			message.sendAndSaveCopy();
	}
	
	
	/**
	 * Scheduled method that finds all unread Emails every minute and 
	 * passes to the {@linkplain #analyseAndSortEmail(EmailMessage)} method.
	 * 
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 15000)
	private void findEmails()  {
		try {
			initiateEWSConnection(120000);
			
			logger.info("Finding unread Emails...");
			int unreadEmails = Folder.bind(emailService, Inbox).getUnreadCount();		
			
			if(unreadEmails < 1) {
				logger.info("No unread Emails...", unreadEmails);
				return;
			}
			
			ItemView view = new ItemView(unreadEmails);
		    FindItemsResults<Item> findResults = emailService.findItems(Inbox, new SearchFilter.IsEqualTo(IsRead, false), view);
		    PropertySet propertySet = new PropertySet(FirstClassProperties);
		    propertySet.setRequestedBodyType(Text);
		    
		    emailService.loadPropertiesForItems(findResults, propertySet);
		    
		    logger.info("Total number of items found {} ", findResults.getTotalCount());
		    for(Item item : findResults) {
		    	analyseAndSortEmail((EmailMessage)item);
		    }
		    
		    logger.info("Find Emails task complete ");
	    
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
	
	/**
	 * Analyses an Email and sorts between generic feedback, 
	 * feedback request or invalid Email.
	 * 
	 * @param email
	 * @throws Exception
	 */
	private static void analyseAndSortEmail(EmailMessage email) throws Exception {
		String from = email.getFrom().toString();
    	Set<EmailAddress> recipients = new HashSet<>(email.getToRecipients().getItems());
    	Set<EmailAddress> ccRecipients = new HashSet<>(email.getCcRecipients().getItems());
    	String subject = email.getSubject();
    	String body = email.getBody().toString().trim();
    	
    	if(subject.toLowerCase().contains("test")) //Just for testing generic feedback
    		genericFeedbackFound(from, recipients, body);    	
    	
    	email.setIsRead(true);
    	email.update(ConflictResolutionMode.AutoResolve);
	}
	
	/**
	 * @param from
	 * @param recipients
	 * @param subject
	 * @throws NamingException 
	 * @throws InvalidAttributeValueException 
	 */
	private static void genericFeedbackFound(String from, Set<EmailAddress> recipients, String body) throws InvalidAttributeValueException, NamingException {	
		logger.info("Generic Feedback found");
		
		for(EmailAddress recipient : recipients)
			EmployeeDAO.addFeedback(from, recipient.getAddress(), body);
		
		logger.info("Generic Feedback processed");
	}
	
	/**
	 * Opens connection with the mailbox with specified timeout.
	 * 
	 * @param timeout
	 * @throws URISyntaxException
	 */
	private static void initiateEWSConnection(int timeout) throws URISyntaxException{
		if(emailService==null){
			emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			emailService.setMaximumPoolingConnections(1);
			emailService.setCredentials(new WebCredentials(env.getProperty("mail.username"), env.getProperty("mail.password")));
			emailService.setUrl(new URI(env.getProperty("mail.uri")));
			emailService.setTimeout(timeout);
		}
	}

}
