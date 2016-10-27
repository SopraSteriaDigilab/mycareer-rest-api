package emailServices;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.management.InvalidAttributeValueException;

import org.eclipse.jetty.http.MetaData.Request;

import dataStructure.Constants;
import dataStructure.FeedbackRequest;
import functionalities.EmployeeDAO;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
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

	public static boolean createFeedbackRequest(String sender, String notes, String... mailTo){
		try{
			//Open a connection with the Email Server
			openIMAPConnection();
			//Validate the email addresses
			List<String> validEmailAddressesList=new ArrayList<String>();
			List<String> invalidEmailAddressesList=new ArrayList<String>();
			for(String s: mailTo){
				//Add the email address to the list of addresses only if the email is valid
				if(isAddressValid(s))
					validEmailAddressesList.add(s);
				//Add the email address to a separate list
				else
					invalidEmailAddressesList.add(s);
			}
			//Create a FeedbackRequest object with an unique ID to the employee
			//Retrieve employeeID from the sender emailAddress
			int tempEmployeeID=findEmployeeOfTOField(sender);
			//No ID has a value less than 0 in the system
			if(tempEmployeeID>0){
				//Keep generating request IDs until a valid one is found, this eliminates duplicated values and issues that such matter can cause
				FeedbackRequest request;
				do{
					request=new FeedbackRequest();
				}
				while(EmployeeDAO.validateFeedbackRequestID(tempEmployeeID, request.getID()));
				//Add further information to the feedback request object
				//Add list of recipients to the feedback request object
				request.setRecipients(validEmailAddressesList);
				//Add the feedback request to te user on the DB
				//EmployeeDAO.add
				//Send a feedback requests, now that the incorrect email addresses have been removed
				for(String s: validEmailAddressesList){
					//Create RequestID
					//Associate the Request to the valid receivers of this email
					
					//Associate the sender to this RequestID
					//Add any additional notes to the template of the email
					//Send the email
					//Send confirmation email to sender
				}
			}
			//Send an email to the creator of this task containing the invalid email addresses
			if(invalidEmailAddressesList.size()>0){
				EmailMessage msg= new EmailMessage(emailService);
				msg.setSubject("Incorrect Email");
				//Fill the email with the error details
				msg.setBody(MessageBody.getMessageBodyFromText("Some of the email addresses linked to the feedback request"
						+ " are invalid:\n"+invalidEmailAddressesList.toString()));
				msg.getToRecipients().add(sender);
				msg.sendAndSaveCopy();
			}
			//Close the connection with the Email Server
			closeIMAPConnection();
			return true;
		}
		catch(Exception e){

		}
		return false;
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

	private static boolean sendRequest(String toField, String fromField, String body) throws Exception{
		//Create a mail object
		EmailMessage msg= new EmailMessage(emailService);
		msg.setSubject("Feedback Request");
		//Fill the template with data
		//msg.setBody(MessageBody.getMessageBodyFromText("The employee email address "+toEmployee.toString()+" linked to your feedback has not been found in the system"));
		//msg.getToRecipients().add(fromField);
		msg.sendAndSaveCopy();
		return true;
	}

	/**
	 * 
	 * This method looks up for an employee ID from a given email address
	 * 
	 * @param emailAddress email address of the employee
	 * @return the ID which identifies a Sopra Steria employee
	 */
	private static int findEmployeeOfTOField(String emailAddress){
		try {
			//Call to the EmployeeDAO to retrieve the employee ID
			return EmployeeDAO.getUserIDFromEmailAddress(emailAddress);
		} catch (InvalidAttributeValueException e) {
			return -1;
		}
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

	/**
	 * 
	 * This method closes the communication with the email server 
	 * 
	 */
	private static void closeIMAPConnection(){
		if(emailService!=null)
			emailService.close();
	}

}
