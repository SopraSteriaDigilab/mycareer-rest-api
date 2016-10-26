package emailServices;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import dataStructure.Constants;
import functionalities.EmployeeDAO;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
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

	public static boolean createFeedbackRequest(String notes, String... mailTo){
		try{
			//Open a connection with the Email Server
			openIMAPConnection();
			//Validate the email addresses
			List<String> emailAddressesList=new ArrayList<String>();
			for(String s: mailTo){
				//Add the email address to the list of addresses only if there is an employeeID associated to it
				if(findEmployeeOfTOField(s)!=-1)
					emailAddressesList.add(s);
			}
			//Send a feedback requests, now that the incorrect email addresses have been removed
			for(String s: emailAddressesList){
				
			}
			//Close the connection with the Email Server
			closeIMAPConnection();
			return true;
		}
		catch(Exception e){

		}
		return false;
	}
	
	private static boolean sendRequest(String emailAddress) throws Exception{
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
