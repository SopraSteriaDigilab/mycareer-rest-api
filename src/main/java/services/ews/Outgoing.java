package services.ews;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.Constants;
import dataStructure.Employee;
import dataStructure.FeedbackRequest;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import services.EmployeeDAO;
import services.validate.Validate;

public class Outgoing {
	
	private static final Logger logger = LoggerFactory.getLogger(Outgoing.class);
	
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;
	
	public static synchronized void processFeedbackRequest(long employeeID, String emailsString, String notes) throws InvalidAttributeValueException {
		Employee requester = EmployeeDAO.getEmployee(employeeID);
		Set<String> recipientList = Validate.stringEmailsToHashSet(emailsString);
		
		for(String recipient : recipientList){
			sendEmail(requester.getEmailAddress(), recipient, "Subject", notes);
			
			EmployeeDAO.addFeedbackRequest(requester, new FeedbackRequest(employeeID, recipient));
			
		}
	}
	
	
	
	private static void sendEmail(String from, String to, String subject, String body) {
		
	
		logger.info("Email sent from: " + from + " to: " + to + ". ");
		
	}

	public static void sendEmail(){
		try {
			initiateEWSConnection(20000);
			EmailMessage message = new EmailMessage(emailService);
			message.setSubject("Test Subject");
			MessageBody body = new MessageBody(BodyType.Text, "Test Body");
			message.setBody(body);
			message.getToRecipients().add("chris.mcintyre@soprasteria.com");
			logger.info("Sending message");
			message.sendAndSaveCopy();
			logger.info("Message Sent");
			
		} catch (URISyntaxException e1) {
			logger.error("Issue with initiateSMTPConnection");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	
	private static void initiateEWSConnection(int timeout) throws URISyntaxException{
		if(emailService==null){
			emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			emailService.setMaximumPoolingConnections(1);
			credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
			emailService.setCredentials(credentials);
			emailService.setUrl(new URI(Constants.MAIL_EXCHANGE_URI));
			emailService.setTimeout(timeout);
		}
	}



}
