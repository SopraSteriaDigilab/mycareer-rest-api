package services.ews;

import java.net.URI;

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
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import static microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName.Inbox;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

@Component
@PropertySource("${ENVIRONMENT}.properties")
public class EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	private static Environment env;
	private static ExchangeService emailService;
	
	@Autowired
	public EmailService(Environment env){
		EmailService.env = env;
	}
	
	public synchronized static void sendEmail(String from, String recipient, String subject, String body) throws Exception{
			initiateEWSConnection(20000);
			
			EmailMessage message = new EmailMessage(emailService);
			message.setSubject(subject);
			message.setBody(new MessageBody(BodyType.Text, body));
			message.getToRecipients().add(recipient);
			message.sendAndSaveCopy();
	}
	
	@Scheduled(fixedRate = 15000)
	public static void findEmails() throws Exception {
		initiateEWSConnection(120000);
		
		logger.info("Finding unread emails...");
		
		int unreadEmails = Folder.bind(emailService, Inbox).getUnreadCount();		
		
		
		if(unreadEmails < 1) {
			logger.info("No unread emails...", unreadEmails);
			return;
		}
		
		ItemView view = new ItemView(unreadEmails);
	    
	    FindItemsResults<Item> findResults = emailService.findItems(Inbox, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false), view);

	    PropertySet propertySet = new PropertySet(BasePropertySet.FirstClassProperties);
	    propertySet.setRequestedBodyType(BodyType.Text);
	    
	    emailService.loadPropertiesForItems(findResults, propertySet);
	    
	    logger.info("Total number of items found {} ", findResults.getTotalCount());
	    
	    for(Item item : findResults){
	    	EmailMessage message = (EmailMessage)item;
	    	logger.info("From: {}", message.getReceivedBy());
	    	logger.info("To: {}", message.getToRecipients().getItems());
	    	logger.info("CC: {}", message.getCcRecipients().getItems());
	    	logger.info("Subject: {}", message.getSubject());
	    	logger.info("body {}", message.getBody().toString().trim());
	    }
	    
	    
	    logger.info("Find emails task complete ");
	}
	
	private static void initiateEWSConnection(int timeout) throws Exception{
		if(emailService==null){
			emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			emailService.setMaximumPoolingConnections(1);
			emailService.setCredentials(new WebCredentials(env.getProperty("mail.username"), env.getProperty("mail.password")));
			emailService.setUrl(new URI(env.getProperty("mail.uri")));
			emailService.setTimeout(timeout);
		}
	}

}
