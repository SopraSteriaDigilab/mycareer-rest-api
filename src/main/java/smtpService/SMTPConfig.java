package smtpService;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configurable
public class SMTPConfig {

	//Global Variables
	private static JavaMailSenderImpl mailSender;
	private static Session session;
	private static Properties pro;
	private static final String username="michael.piccoli@soprasteria.com";
	private static final String password="MikeSopra16$";
	private static final String emailFromUser="michael.piccoli.mp@gmail.com";
	//SSL Port: 465 TSL:587 Plain:25


	public void sendTemplateFeedbackEmail(String mailTo){
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.office365.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        //properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(username);
        msg.setReplyTo(username);
        msg.setFrom(username);
        msg.setSubject("Test1");
        msg.setText("Emailssss");

        try {
        	javaMailSender.send(msg);
        } catch (MailException e) {
        	e.printStackTrace();
        }
        
        /*SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(username);
        mailMessage.setReplyTo(username);
        mailMessage.setFrom(username);
        mailMessage.setSubject("Lorem ipsum");
        mailMessage.setText("Lorem ipsum dolor sit amet [...]");*/
        //javaMailSender.send(mailMessage);
        
        /*
        Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailTo));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailTo));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler," +"\n\n No spam to my email, please!");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.office365.com", 587, username, password);
            transport.send(message);

            System.out.println("Done");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		*/
/*
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailFromUser));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailTo));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
					+ "\n\n No spam to my email, please!");

			Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        } */
	}

	public void processEmails(){
		//Connect to the server

	}



}
