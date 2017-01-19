package services.ews;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.Constants;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;
	
	public synchronized static void sendEmail(String from, String recipient, String subject, String body) throws Exception{
			initiateEWSConnection(20000);
			
			EmailMessage message = new EmailMessage(emailService);
			MessageBody messageBody = new MessageBody(BodyType.Text, body);
			message.setSubject(subject);
			message.setBody(messageBody);
			message.getToRecipients().add(recipient);
			message.sendAndSaveCopy();
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
