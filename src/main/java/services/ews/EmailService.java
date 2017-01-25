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
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

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
	
	@Scheduled(fixedRate = 60000)
	public static void findEmails() {
		logger.info("Checking for emails in {}", env.getProperty("mail.username"));
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
