package services.ews;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.Constants;
import dataStructure.FeedbackRequest;
import dataStructure.GroupFeedbackRequest;
import services.EmployeeDAO;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import application.Application;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 21st October 2016
 *
 * Source: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSenderImpl.html
 * Source: https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html
 * Source: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/MimeMessageHelper.html
 * 
 * This class contains the SMTP service
 * 
 */

public final class SMTPService {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	//Global Variables
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;

	static{
		emailService=null;
		credentials=null;
	}

	private SMTPService(){}
	
	public static synchronized boolean tryToSendFeedbackRequest (int counter, long employeeID, String notes, String... mailTo) throws Exception{
		try{
			logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Sending Feedback Request/s. Attempt "+ counter +"/2");
			return createFeedbackRequest(employeeID, notes, mailTo);
		}
		catch (InvalidAttributeValueException e) {
			throw e;
		}
		catch(ServiceRequestException reqFailed){
			if(counter<2){
				logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - The Request Failed by the Exchange Server, The system is trying to recovering from this error. Attempt "+ counter++ +"/2");
				tryToSendFeedbackRequest(counter, employeeID, notes, mailTo);
			}
			else{
				logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Maximum number of attempts reached.");
				//Fill the email with the error details and contact the administrator
				String subject="Email Service Error";
				String body="There has been a problem with the email service.\n\n"+reqFailed.toString()+"\n\nRegards,\nMyCareer Team\n\n";
				contactAdministrator(subject, body);
				throw reqFailed;
			}
		}
		return false;
	}

	private static synchronized boolean createFeedbackRequest(long employeeID, String notes, String... mailTo) throws InvalidAttributeValueException, ServiceRequestException{
		if(notes.length()>1000)
			throw new InvalidAttributeValueException(Constants.INVALID_SMTPSERVICE_NOTES);
		if(mailTo.length>20)
			throw new InvalidAttributeValueException(Constants.INVALID_SMTPSERVICE_TOOMANYADDRESSES);
		try{


			//PART 1


			//Open a connection with the Email Server
			logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Establishing a connection with the Mail Server");
			initiateSMTPConnection();


			//PART2


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


			//PART 3


			//Get email address of the user requesting feedback which also validates the emaployeeID
			String emailAddresEmployeeRequester=EmployeeDAO.getUserEmailAddressFromID(employeeID);
			//Get the full name of the employee from his/hers ID
			String fullNameEmployeeRequester=EmployeeDAO.getUserFullNameFromUserID(employeeID);


			//PART 4


			//Create a GroupFeedbackRequest object with an unique ID to the employee
			logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Creating a Feedback Request");
			GroupFeedbackRequest groupRequest=new GroupFeedbackRequest(employeeID);

			//PART 5


			logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Sending the Request/s");
			//Send a feedback requests, now that the incorrect email addresses have been removed
			List<String> tempEmailAddressSendingList=new ArrayList<>();
			String tempEmailAddressSending="";
			try{
				for(String s: validEmailAddressesList){
					//Create a Feedback request object containing the current valid email address
					FeedbackRequest tempFeedbackReq=new FeedbackRequest(employeeID);
					tempFeedbackReq.setRecipient(s);
					
					tempEmailAddressSending=s;
					
					//Create and send the email
					EmailMessage msg= new EmailMessage(emailService);
					msg.setSubject("Feedback Request - "+tempFeedbackReq.getID());
					//Fill the email with the template
					//Generate a Template email, add the Request ID an any further information
					msg.setBody(fillTemplate(fullNameEmployeeRequester, tempFeedbackReq.getID(), notes));
					msg.getToRecipients().add(s);
					//msg.setFrom(new EmailAddress(Constants.MAILBOX_ADDRESS));
					msg.sendAndSaveCopy();
					//Add the request to the list of requests
					groupRequest.addFeedbackRequest(tempFeedbackReq);
				}
			}
			catch (InvalidAttributeValueException invalidE){
				//Fill the email with the error details and contact the administrators
				String subject="Loading Template Error";
				String body="There has been a problem while loading the template for a feedback request.\n\n"+invalidE+"\n\nRegards,\nMyCareer Team\n\n";
				contactAdministrator(subject, body);
				
				logger.error("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Error Loading the Template, Admin has been contacted");
				throw new InvalidAttributeValueException("Error while loading the template, Operation Interrupted.\nThe Administrator has been contacted");
			}
			//Catch all the error regarding the email service and send an email to the system administrator when such error happens
			catch(ServiceRequestException serviceE){
				logger.error("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Email Service Error, Admin has been contacted");
				throw new ServiceRequestException("Email Service Error: "+serviceE.getMessage());
			}
			catch(NullPointerException ne){
				throw new InvalidAttributeValueException("Error while reading the Template");
			}
			catch(Exception e){
				//If an exception happens, the email address is incorrect, move it to the invalid list
				tempEmailAddressSendingList.add(tempEmailAddressSending);
				logger.error("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Error: "+e.getMessage());
			}

			//PART 6


			//Update the valid and invalid lists before sending a confirmation to the user requesting feedback
			for(String s:tempEmailAddressSendingList){
				//Add the invalid element to the invalid list
				invalidEmailAddressesList.add(s);
				//Remove this same element from the valid list
				validEmailAddressesList.remove(s);
			}

			//PART 7


			//Link the group feedback request to the requester
			if(!EmployeeDAO.insertNewGroupFeedbackRequest(employeeID, groupRequest))
				logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - A Group Feedback request has been sent, however it could not be saved onto the permanent storage!"+groupRequest.toString());

			
			//PART 8


//			logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Sending a confirmation email");
//			//Send confirmation email to feedback requester
//			EmailMessage msg= new EmailMessage(emailService);
//			msg.setSubject("Feedback Request Sent");
//			//Fill the email with the error details
//			String bodyMsg="Hi, "+fullNameEmployeeRequester+"\n\nThank you for using MyCareer to request feedback.\n\n";
//			if(validEmailAddressesList.size()>0){
//				bodyMsg+="A feedback request has been proccess to the following addresses:\n";
//				bodyMsg+=getStringFromListEmails(validEmailAddressesList)+"\n";
//			}
//			if(invalidEmailAddressesList.size()>0){
//				bodyMsg+="The following email addresses are invalid, therefore no feedback request has been sent to:\n"+getStringFromListEmails(invalidEmailAddressesList);
//			}
//			bodyMsg+="\nKind Regards,\nMyCareer Team\n\n";
//			MessageBody mexB=new MessageBody();
//			mexB.setText(bodyMsg);
//			//msg.setFrom(new EmailAddress(Constants.MAILBOX_ADDRESS));
//			mexB.setBodyType(BodyType.Text);
//			msg.setBody(mexB);
//			msg.getToRecipients().add(emailAddresEmployeeRequester);
//			msg.sendAndSaveCopy();


			//PART 9


			logger.info("\t"+LocalTime.now(ZoneId.of(UK_TIMEZONE))+" - Task completed");
			return true;
		}
		catch(ServiceRequestException se){
			//closeIMAPConnection();
			throw new ServiceRequestException(""+se.getMessage());
		}catch (URISyntaxException e1) {
			throw new InvalidAttributeValueException(e1.getMessage());
		}catch(Exception e){
			throw new InvalidAttributeValueException(e.getMessage());
		}
	}

	private static boolean isAddressValid(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	private static String getStringFromListEmails(List<String> data){
		String result="";
		int counter=1;
		for(String s:data){
			result+="\t"+counter++ +") " +s+"\n";
		}
		return result;
	}

	private static MessageBody fillTemplate(String requester, String reqID, String notes) throws InvalidAttributeValueException{
		MessageBody mb=new MessageBody();
		String body="";
		//Read in the content from the template file stored in the externalData package
		try (final BufferedReader inputFile =
				new BufferedReader(
				new FileReader("D:/Home/redhwan/Dev/Workspaces/Workspace-Eclipse-MyCareer/mycareer-web/mycareer-rest-api/src/main/java/externalServices/ews/FeedbackRequestBody_Template.txt"));) {			
			
			String line="";
			
			while ((line=inputFile.readLine()) != null) {
			
				if(line.contains("[FeedbackRequester_name]")){
					line=line.replace("[FeedbackRequester_name]", requester);
				}
				
				if (line.contains("[FeedbackRequest_Comments]")) {
					if (!notes.trim().equals("")) {
						line=line.replace("[FeedbackRequest_Comments]", notes);
					} else {
						line="No Comment Added";
					}
				}
				
				body += line + "\n";
			}
			//Add the request ID at last
			body+=reqID;
			mb.setText(body);
			//Set tje body type to plain text
			mb.setBodyType(BodyType.Text);
			//Return the filled message
			return mb;
		}catch(FileNotFoundException e){
			throw new InvalidAttributeValueException(e.getMessage()+"\n"+e.getCause());
		}catch (IOException e) {
			throw new InvalidAttributeValueException(e.getMessage()+"\n"+e.getCause());
		}
	}
	
	private static void contactAdministrator(String subject, String body) throws Exception{
		EmailMessage msg= new EmailMessage(emailService);
		msg.setSubject(subject);
		MessageBody mexB=new MessageBody();
		mexB.setText(body);
		mexB.setBodyType(BodyType.Text);
		msg.setBody(mexB);
		msg.getToRecipients().add(Constants.MAILBOX_ADDRESS);
		msg.sendAndSaveCopy();
	}
	

	
	
	

	/**
	 * 
	 * This method initiates the authentication with the email server
	 * @throws URISyntaxException 
	 * 
	 * @throws Exception
	 */
	private static void initiateSMTPConnection() throws URISyntaxException{
		if(emailService==null){
			emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			emailService.setMaximumPoolingConnections(1);
			credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
			emailService.setCredentials(credentials);
			emailService.setUrl(new URI(Constants.MAIL_EXCHANGE_URI));
			emailService.setTimeout(20000);
		}
	}

}
