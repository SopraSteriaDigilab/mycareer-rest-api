package emailServices;

import java.net.URI;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.management.InvalidAttributeValueException;
import dataStructure.Constants;
import dataStructure.FeedbackRequest;
import functionalities.EmployeeDAO;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

/**
 * 
 * @author Michael Piccoli
 *
 *
 * Source: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSenderImpl.html
 * Source: https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html
 * Source: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/MimeMessageHelper.html
 */

public final class SMTPService {

	//Global Variables
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;

	static{
		emailService=null;
		credentials=null;
	}

	private SMTPService(){}

	public static synchronized boolean createFeedbackRequest(int employeeID, String notes, String... mailTo) throws InvalidAttributeValueException{
		if(notes.length()>1000)
			throw new InvalidAttributeValueException("The notes cannot exceed the 1000 characters");
		try{
			//Open a connection with the Email Server
			System.out.println("\t"+LocalTime.now()+" - Establishing a connection with the Mail Server");
			openIMAPConnection();
			//Validate the email addresses
			List<String> validEmailAddressesList=new ArrayList<String>();
			List<String> invalidEmailAddressesList=new ArrayList<String>();
			for(String s: mailTo){
				//Add the email address to the list of addresses only if the email is valid and not a duplicate
				if(isAddressValid(s) && !validEmailAddressesList.contains(s)){
					if(validEmailAddressesList.size()<21){
						validEmailAddressesList.add(s);
					}
				}
				//Add the email address to a separate list
				else
					invalidEmailAddressesList.add(s);
			}

			//Get email address of the user requesting feedback which also validates the emaployeeID
			String emailAddresEmployeeRequester=EmployeeDAO.getUserEmailAddressFromID(employeeID);
			//Create a FeedbackRequest object with an unique ID to the employee
			//Keep generating request IDs until a valid one is found, this eliminates duplicated values and issues that such matter can cause
			FeedbackRequest request;
			System.out.println("\t"+LocalTime.now()+" - Creating a Feedback Request");
			do{
				request=new FeedbackRequest();
			}
			while(!EmployeeDAO.validateFeedbackRequestID(employeeID, request.getID()));
			//Add list of recipients to the feedback request object
			request.setRecipients(validEmailAddressesList);

			//Generate a Template email, add the Request ID an any further information
			String templateBody="GET TEMPLATE + ADD NOTES + EMPLOYEE REQUESTING FEEDBACK";
			System.out.println("\t"+LocalTime.now()+" - Sending the Request/s");
			//Send a feedback requests, now that the incorrect email addresses have been removed
			for(String s: validEmailAddressesList){
				EmailMessage msg= new EmailMessage(emailService);
				msg.setSubject("Feedback Request");
				//Fill the email with the template
				msg.setBody(new MessageBody(templateBody));
				msg.getToRecipients().add(s);
				msg.getCcRecipients().add(Constants.MAILBOX_ADDRESS);
				//msg.setFrom(new EmailAddress(emailAddresEmployeeRequester));
				msg.sendAndSaveCopy();
			}
			System.out.println("\t"+LocalTime.now()+" - Sending a confirmation email");
			//Send confirmation email to feedback requester
			EmailMessage msg= new EmailMessage(emailService);
			msg.setSubject("Feedback Request Sent");
			//Fill the email with the error details
			String bodyMsg="A feedback request has been successfully sent to the following addresses:\n"
					+ getStringFromListEmails(validEmailAddressesList)+"\n";
			if(invalidEmailAddressesList.size()>0){
				bodyMsg+="\nHowever, some of the given email addresses are invalid:\n"+getStringFromListEmails(invalidEmailAddressesList);
			}
			bodyMsg+="\n\nRegards,\nMyCareer Team\n\n";
			msg.setBody(MessageBody.getMessageBodyFromText(bodyMsg));
			msg.getToRecipients().add(emailAddresEmployeeRequester);
			msg.sendAndSaveCopy();
			System.out.println("\t"+LocalTime.now()+" - Task completed");
			return true;
		}
		catch(ServiceRequestException se){
			//closeIMAPConnection();
			throw new InvalidAttributeValueException(se.getMessage());
		}
		catch(Exception e){
			throw new InvalidAttributeValueException(e.getMessage());
		}
	}

	private static boolean isAddressValid(String email) {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
			return true;
		} catch (AddressException ex) {
			return false;
		}
	}

	private static String getStringFromListEmails(List<String> data){
		String result="";
		int counter=1;
		for(String s:data){
			result+="\t"+counter++ +") " +s+"\n";
		}
		return result;
	}

	/**
	 * 
	 * This method initiates the authentication with the email server
	 * 
	 * @throws Exception
	 */
	private static void openIMAPConnection() throws Exception{
		if(emailService==null){
			emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			emailService.setMaximumPoolingConnections(1);
			if(credentials==null)
				credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
			emailService.setCredentials(credentials);
			emailService.setUrl(new URI(Constants.MAIL_EXCHANGE_URI));
		}
	}

}
