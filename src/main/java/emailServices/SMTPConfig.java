package emailServices;

import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import dataStructure.Constants;

/**
 * 
 * @author Michael Piccoli
 *
 *
 * Source: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSenderImpl.html
 * Source: https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html
 * Source: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/MimeMessageHelper.html
 */
@Configuration
public final class SMTPConfig {

	//Global Variables
	@Autowired
	private static JavaMailSenderImpl mailSender;
	private static Session session;
	private static Properties properties;
	//SSL Port: 465 TSL:587 Plain:25

	static{
		mailSender=null;
		session=null;
		properties=null;
	}
	
	private SMTPConfig(){}
	

	private static void initiateSMTPConnection(){
		//Create a new instance only if the element has not been initiated before
		if(mailSender==null)
			mailSender = new JavaMailSenderImpl();
		//Create a new instance only if the element has not been initiated before
		if(properties==null){
			properties = new Properties();
			//Set the Host
			properties.setProperty("mail.smtp.host", Constants.SMTP_HOST);
			//Set the port
			properties.setProperty("mail.smtp.port", Constants.SMTP_HOST_PORT);
			//Set the protocol
			properties.setProperty("mail.transport.protocol", "smtp");
			//Set the authentication to true since the host needs a username and password to login into the mailbox
			properties.setProperty("mail.smtp.auth", "true");
			//Enable TLS 
			properties.setProperty("mail.smtp.starttls.enable", "true");
			properties.setProperty("mail.smtp.socketFactory.fallback", "false");
			//Set the Charset
			properties.setProperty("mail.mime.charset", Constants.MAIL_ENCODING_CHARSET);
			properties.setProperty("mail.smtp.allow8bitmime", "true");
			properties.setProperty("mail.smtps.allow8bitmime", "true");
			//Set debug to true during development
			properties.setProperty("mail.debug", "true");
		}
		//Create a new instance only if the element has not been initiated before
		if(session==null){
			session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
				}
			});
			mailSender.setSession(session);
		}
	}

	public static void sendTemplateFeedbackEmail(String... mailTo){
		initiateSMTPConnection();
		try {
			MimeMessage msg1 = mailSender.createMimeMessage();
			//TRUE means that it supports alternative texts, in-line elements and attachments)
			MimeMessageHelper msg = new MimeMessageHelper(msg1, true, Constants.MAIL_ENCODING_CHARSET);
			msg.setTo(mailTo);
			//msg.setTo(String[] to) ;
			//msg.validateAddresses(InternetAddress[] addresses);
			//msg.setSentDate(Date sentDate);
			msg.setReplyTo(Constants.MAIL_USERNAME);
			msg.setFrom(Constants.MAIL_USERNAME);
			msg.setSubject("Test23425");
			msg.setText("TestttMEeeee");
			//Send the Message
			mailSender.send(msg1);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
