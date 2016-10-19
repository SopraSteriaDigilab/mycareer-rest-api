package emailServices;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import dataStructure.Constants;

@Configuration
public final class IMAPConfig {
	
	//Global Variables
		@Autowired
		//private static MailReceiver mailSender;
		private static Session session;
		private static Properties properties;
		
		static{
			//mailSender=null;
			session=null;
			properties=null;
		}
		
		private IMAPConfig(){}
		
		public static void initiateIMAPService(){
			Timer timer = new Timer();
			  long interval = (10*60*1000) ; // 10 minutes

			  timer.schedule( new TimerTask() {
			       public void run() {
			    	   retrieveNewEmails();
			       }
			  }, 0, interval);
		}
		
		public static void retrieveNewEmails(){
			
		}
		
		public static void initiateIMAPConnection() throws MessagingException{
			//Create a new instance only if the element has not been initiated before
			//if(mailSender==null)
				//mailSender = new JavaMailSenderImpl();
			//Create a new instance only if the element has not been initiated before
			if(properties==null){
				properties = new Properties();
				//Set the Host
				properties.setProperty("mail.imaps.host", Constants.IMAP_HOST);
				//Set the port
				properties.setProperty("mail.imaps.port", Constants.IMAP_HOST_PORT);
				//Set the protocol
				properties.setProperty("mail.store.protocol", "imaps");
				//Set the authentication to true since the host needs a username and password to login into the mailbox
				properties.setProperty("mail.imaps.auth", "true");
				//Enable TLS 
				//properties.setProperty("mail.imaps.starttls.enable", "true");
				properties.setProperty("mail.imaps.socketFactory.fallback", "false");
				properties.setProperty("mail.imaps.auth.plain.disable", "true");
				properties.setProperty("mail.imaps.auth.ntlm.disable", "true");
				//properties.put("mail.imaps.sasl.enable", "true");
				//properties.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
				//Set the Charset
				//properties.setProperty("mail.mime.charset", Constants.MAIL_ENCODING_CHARSET);
				//properties.setProperty("mail.imaps.allow8bitmime", "true");
				//Set debug to true during development
				properties.setProperty("mail.debug", "true");
				
				//properties.put("mail.imaps.auth.plain.disable", "false");
				//properties.put("mail.imaps.auth.ntlm.disable", "true");
				//properties.put("mail.imaps.auth.gssapi.disable", "false");
			}
			//Create a new instance only if the element has not been initiated before
			if(session==null){
				session = Session.getInstance(properties,
						new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
					}
				});
				//mailSender.setSession(session);
			}
			Store store=session.getStore("imaps");
			store.connect();
			
			Folder inboxFolder=store.getFolder("INBOX");
			inboxFolder.open(Folder.READ_ONLY);
			
			Message[] messages=inboxFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
                String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
                String sentDate = msg.getSentDate().toString();
 
                String contentType = msg.getContentType();
                String messageContent = "";
 
                if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    try {
                        Object content = msg.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        ex.printStackTrace();
                    }
                }
 
                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t To: " + toList);
                System.out.println("\t CC: " + ccList);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);
            }
 
		}
		
		private static String parseAddresses(Address[] address) {
	        String listAddress = "";
	 
	        if (address != null) {
	            for (int i = 0; i < address.length; i++) {
	                listAddress += address[i].toString() + ", ";
	            }
	        }
	        if (listAddress.length() > 1) {
	            listAddress = listAddress.substring(0, listAddress.length() - 2);
	        }
	 
	        return listAddress;
	    }
		

}
