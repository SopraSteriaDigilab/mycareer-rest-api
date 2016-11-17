package emailServices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import dataStructure.Constants;
import dataStructure.FeedbackRequest;
import functionalities.EmployeeDAO;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

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

	//Global Variables
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;

	static{
		emailService=null;
		credentials=null;
	}

	private SMTPService(){}

	public static synchronized boolean createFeedbackRequest(long employeeID, String notes, String... mailTo) throws InvalidAttributeValueException{
		if(notes.length()>1000)
			throw new InvalidAttributeValueException("The notes cannot exceed the 1000 characters");
		if(mailTo.length>20)
			throw new InvalidAttributeValueException("Too many email addresses, The maximum number allowed is 20");
		try{


			//PART 1


			//Open a connection with the Email Server
			System.out.println("\t"+LocalTime.now()+" - Establishing a connection with the Mail Server");
			openIMAPConnection();


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
			String fullNameEmployeeRequester=EmployeeDAO.getUserFullNmeFromUserID(employeeID);


			//PART 4


			//Create a FeedbackRequest object with an unique ID to the employee
			//Keep generating request IDs until a valid one is found, this eliminates duplicated values and issues that such matter can cause
			FeedbackRequest request;
			System.out.println("\t"+LocalTime.now()+" - Creating a Feedback Request");
			do{
				request=new FeedbackRequest(employeeID);
			}
			while(!EmployeeDAO.validateFeedbackRequestID(employeeID, request.getID()));
			System.out.println(request.getID());

			//PART 5


			System.out.println("\t"+LocalTime.now()+" - Sending the Request/s");
			//Send a feedback requests, now that the incorrect email addresses have been removed
			List<String> tempEmailAddressSendingList=new ArrayList<>();
			String tempEmailAddressSending="";
			try{
				for(String s: validEmailAddressesList){
					tempEmailAddressSending=s;
					EmailMessage msg= new EmailMessage(emailService);
					msg.setSubject("Feedback Request - "+request.getID());
					//Fill the email with the template
					//Generate a Template email, add the Request ID an any further information
					msg.setBody(fillTemplate(fullNameEmployeeRequester, request.getID(), notes));
					msg.getToRecipients().add(s);
					//msg.getCcRecipients().add(Constants.MAILBOX_ADDRESS);
					//msg.setFrom(new EmailAddress(Constants.MAILBOX_ADDRESS));
					msg.sendAndSaveCopy();
				}
			}
			catch (InvalidAttributeValueException invalidE){
				//Fill the email with the error details and contact the administrators
				String subject="Loading Template Error";
				String body="There has been a problem while loading the template for a feedback request.\n\n"+invalidE+"\n\nRegards,\nMyCareer Team\n\n";
				contactAdministrator(subject, body);
				
				System.out.println("\t"+LocalTime.now()+" - Error Loading the Template, Admin has been contacted");
				throw new InvalidAttributeValueException("Error while loading the template, Operation Interrupted!\nThe Administrator has been contacted");
			}
			//Catch all the error regarding the email service and send an email to the system administrator when such error happens
			catch(ServiceRequestException serviceE){
				//Fill the email with the error details and contact the administrator
				String subject="Email Service Error";
				String body="There has been a problem with the email service.\n\n"+serviceE.toString()+"\n\nRegards,\nMyCareer Team\n\n";
				contactAdministrator(subject, body);
				
				System.out.println("\t"+LocalTime.now()+" - Email Service Error, Admin has been contacted");
				throw new InvalidAttributeValueException("Email Service Error");
			}
			catch(Exception e){
				//If an exception happens, the email address is incorrect, move it to the invalid list
				tempEmailAddressSendingList.add(tempEmailAddressSending);
				System.out.println("\t"+LocalTime.now()+" - Error: "+e.getMessage());
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


			//Add list of recipients to the feedback request object
			request.setRecipients(validEmailAddressesList);
			//Link the feedback request to the requester
			if(!EmployeeDAO.insertNewFeedbackRequest(employeeID, request))
				throw new InvalidAttributeValueException("A Feedback request has been sent, however it could not be saved on the peramanent storage!");


			//PART 8


			System.out.println("\t"+LocalTime.now()+" - Sending a confirmation email");
			//Send confirmation email to feedback requester
			EmailMessage msg= new EmailMessage(emailService);
			msg.setSubject("Feedback Request Sent");
			//Fill the email with the error details
			String bodyMsg="Hi, "+fullNameEmployeeRequester+"\n\nThank you for using MyCareer to request feedback!\n\n";
			if(validEmailAddressesList.size()>0){
				bodyMsg+="A feedback request has been successfully sent to the following addresses:\n";
				bodyMsg+=getStringFromListEmails(validEmailAddressesList)+"\n";
			}
			if(invalidEmailAddressesList.size()>0){
				bodyMsg+="The following email addresses are invalid, therefore no feedback request has been sent to:\n"+getStringFromListEmails(invalidEmailAddressesList);
			}
			bodyMsg+="\nKind Regards,\nMyCareer Team\n\n";
			MessageBody mexB=new MessageBody();
			mexB.setText(bodyMsg);
			//msg.setFrom(new EmailAddress(Constants.MAILBOX_ADDRESS));
			mexB.setBodyType(BodyType.Text);
			msg.setBody(mexB);
			msg.getToRecipients().add(emailAddresEmployeeRequester);
			msg.sendAndSaveCopy();


			//PART 9


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
		try{
			@SuppressWarnings("resource")
			BufferedReader inputFile=new BufferedReader(new FileReader("src/main/java/emailServices/FeedbackRequestBody_Template.txt"));
			String line="";
			while((line=inputFile.readLine())!=null){
				if(line.contains("[FeedbackRequester_name]")){
					line=line.replace("[FeedbackRequester_name]", requester);
				}
				if(line.contains("[FeedbackRequest_Comments]")){
					if(!notes.trim().equals(""))
						line=line.replace("[FeedbackRequest_Comments]", notes);
					else
						line="No Comment Added";
				}
				if(line.contains("(External link required)")){
					line=line.replace("(External link required)", "\nhttp://portal.corp.sopra/hr/HR_UK_SG/mycareerpath/Performancemanagement/Pages/default.aspx");
				}
				body+=line+"\n";
			}
			//Add the request ID at last
			body+=reqID;
			mb.setText(body);
			//Set tje body type to plain text
			mb.setBodyType(BodyType.Text);;
			//Return the filled message
			return mb;
		}catch(Exception e){
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
