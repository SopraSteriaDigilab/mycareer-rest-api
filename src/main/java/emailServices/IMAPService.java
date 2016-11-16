package emailServices;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.management.InvalidAttributeValueException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.mongodb.MongoException;

import dataStructure.Constants;
import dataStructure.Feedback;
import functionalities.EmployeeDAO;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 21st October 2016
 * 
 * This class contains the definition of the IMAPService
 *
 */
public final class IMAPService {

	//Global Variables
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;
	private static Timer timer;

	static{
		emailService=null;
		credentials=null;
		timer=null;
	}

	private IMAPService(){}

	/**
	 * 
	 * This method initiate the email service and checks for new emails every minute
	 * 
	 * @throws URISyntaxException
	 */
	public static void initiateIMAPService() throws URISyntaxException{
		//Schedule a task to run every minute
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					//Create a connection with the server;
					initiateIMAPConnection();
					System.out.println(LocalTime.now()+" - Checking for new Emails");
					//Check for new emails and then close the connection with the email server
					retrieveNewEmails();
					closeIMAPConnection();
					System.out.println("\t"+LocalTime.now()+" - Task Completed\n");
				} catch (Exception e) {
					System.out.println(LocalTime.now()+" - Email Service Error: "+e.getMessage());
				}
			}
		}, 0, Constants.MAIL_REFRESH_TIME);
	}

	/**
	 * 
	 * This method establishes a connection with the email server, gets the unread emails, loop through them to extract the data
	 * and moves the emails into specific folders depending on the result of the action taken
	 * 
	 * 
	 * @throws Exception
	 */
	private static void retrieveNewEmails() throws Exception{
		//Open the Inbox folder for the given mailbox
		Folder inbox = Folder.bind(emailService, WellKnownFolderName.Inbox);
		//Verify if there are unread mails
		if(inbox.getUnreadCount()>0){
			System.out.println("\t"+LocalTime.now()+" - Processing the unread emails ...");
			//Retrieve list of unread mails
			//Create a filter to use while retrieving the data
			SearchFilter unreadEmailsFilter = new SearchFilter.SearchFilterCollection(LogicalOperator.And, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			//Find any unread mails within the server
			FindItemsResults<Item> findResults = emailService.findItems(WellKnownFolderName.Inbox, unreadEmailsFilter, new ItemView(inbox.getUnreadCount()));
			//Loop through the items and add the data to the database (if any are found)
			for(Item tempMail:findResults){
				// As a best practice, limit the properties returned to only those that are required.
				PropertySet psPropset = new PropertySet();
				//Limit the number of fields the server needs to return
				psPropset.setBasePropertySet(BasePropertySet.FirstClassProperties);
				//This defines that the body of the email will be returned as TEXT and not as XML/HTML
				psPropset.setRequestedBodyType(BodyType.Text);
				//Retrieve only the properties previously set for the message with the given ID
				EmailMessage temp = EmailMessage.bind(emailService, tempMail.getId(),psPropset);
				//Load the message
				temp.load();

				//Verify the email

				//Check for a RequestID inside the body of the email
				String reqIDSet=retrieveRequestID(temp);
				//If such ID exists it's a reply from a feedback request
				if(!reqIDSet.equals("")){
					//Retrieve the TO and FROM fields
					//Get the CC field
					List<EmailAddress> ccElements=temp.getCcRecipients().getItems();
					if(ccElements.size()<1)
						return;
					EmailAddress ccEmployee=ccElements.get(0);
			
					//The email is internal the company if the email address contains @soprasteria.com
					EmailAddress fromField=temp.getFrom();
					String type="";
					if(fromField.toString().contains("@soprasteria.com"))
						type="Internal";
					else
						type="External";
					//Get the body of the email extracting only the necessary parts
					String body=extractReplyToFeedbackRequest(temp);
					//Create an Feedback Object					
					Feedback feedbackObj=new Feedback(1,fromField.getAddress(),body,type,"Email");
					//Add the request id to the feedback object
					feedbackObj.setRequestID(reqIDSet);
					
					//Now that we have all the details, pass this data to the EmployeeDAO which will try to link the feedback to the user
					boolean res=EmployeeDAO.linkFeedbackReqReplyToUser(ccEmployee.getAddress(), feedbackObj);
					//If the task has been completed succesfully, set the email as read and move it to the Draft Folder
					if(res){
						temp.setIsRead(true);
						temp.move(WellKnownFolderName.Drafts);
					}
					else{
						///DECIDE WHAT TO DO IN THIS BLOCK
					}
					
				}
				//If it doesn't exist, it's a unrequested feedback
				else{
					
				}
				/*
				//Get the Sender of the email
				EmailAddress fromField=temp.getFrom();
				//The email is internal the company if the email address contains @soprasteria.com
				String type="";
				if(fromField.toString().contains("@soprasteria.com"))
					type="Internal";
				else
					type="External";
				//Get the To field
				List<EmailAddress> toElements=temp.getToRecipients().getItems();
				if(toElements.size()>1){
					if(toElements.contains(new EmailAddress(Constants.MAILBOX_ADDRESS)))
						toElements.remove(new EmailAddress(Constants.MAILBOX_ADDRESS));
				}
				//Retrieve the employee whose feedback is going to be added to
				EmailAddress toEmployee=toElements.get(0);
				//Validate the ToEmployee with the data in the database to find the user to attach the feedback to
				long userID=findEmployeeOfTOField(toEmployee.getAddress());
				//Verify if the ID retrieved is valid or not
				if(userID<1){
					//Reply with an email saying the given employee email address is not in the system
					EmailMessage msg= new EmailMessage(emailService);
					msg.setSubject("Feedback Error"); 
					msg.setBody(MessageBody.getMessageBodyFromText("The employee email address "+toEmployee.toString()+" linked to your feedback has not been found in the system"));
					msg.getToRecipients().add(fromField);
					msg.sendAndSaveCopy();
					//Set the message to unread
					temp.setIsRead(false);
					//Move this email to the Drafts Folder and go to the next item
					temp.move(WellKnownFolderName.Drafts);
					continue;
				}
				//Extract the body of the message and add it as a new feedback for that specific user
				MessageBody emailBody=temp.getBody();
				//Create a new Feedback and add it to the user with the ID previously found
				Feedback feedObj=new Feedback(0,fromField.getAddress(),cleanEmailBody(emailBody.toString()),type, "Email");
				//Attach the feedback to the User on the Database
				try{
					if(EmployeeDAO.insertNewGeneralFeedback(userID, feedObj)){
						System.out.println("\t"+LocalTime.now()+" - Feedback added correctly to user "+userID);
						//Reply with an email thanking the sender of the feedback
						EmailMessage msg= new EmailMessage(emailService);
						msg.setSubject("Thank you"); 
						msg.setBody(MessageBody.getMessageBodyFromText("Thank you for your feedback!"));
						msg.getToRecipients().add(fromField);
						msg.sendAndSaveCopy();
					}
				}
				catch(InvalidAttributeValueException inE){
					//Move the message to the Draft folder, the admin of the system will deal with it
					temp.setIsRead(false);
					//Move this email to the Drafts Folder and go to the next item
					temp.move(WellKnownFolderName.Drafts);
					continue;
				}
				catch(MongoException moE){
					System.out.println("\t"+LocalTime.now()+" -DB Connection error");
					break;
				}
				temp.setIsRead(true);
				temp.update(ConflictResolutionMode.AutoResolve);
				 */
			}

		}
		else{
			System.out.println("\t"+LocalTime.now()+" - No new Emails Found");
		}
	}

	/**
	 * 
	 * This method cleans the body of the email from html tags
	 * 
	 * @param body the HTML body of the email to clean
	 * @return The message contained within the body of the html email
	 */
	private static String cleanEmailBody(String body) {
		String s="";
		Document docHtml=Jsoup.parse(body);
		for(Element el: docHtml.select("p")){
			//Remove this line from the body of the email to save space
			if(!el.text().contains("Before printing, think about the")){
				s+=el.text();
			}
		}
		return s;
	}

	/**
	 * 
	 * This method looks up for an employee ID from a given email address
	 * 
	 * @param emailAddress email address of the employee
	 * @return the ID which identifies a Sopra Steria employee
	 */
	private static long findEmployeeOfTOField(String emailAddress){
		try {
			//Call to the EmployeeDAO to retrieve the employee ID
			return EmployeeDAO.getUserIDFromEmailAddress(emailAddress);
		} catch (InvalidAttributeValueException e) {
			return -1;
		}
	}

	/**
	 * 
	 * This method opens the body of the email and searches for a RequestID
	 * 
	 * @param body The body of the email 
	 * @return A String containing the Request ID
	 */
	private static String retrieveRequestID(EmailMessage mail){
		try{
			//Search for ID in the Subject
			String idInSubject=mail.getSubject();
			int locationIndex=idInSubject.indexOf("-");
			//Retrieve ID
			if(locationIndex>0){
				//System.out.println(idInSubject.substring(locationIndex+1));
				return idInSubject.substring(locationIndex+1);
			}
			//System.out.println(idInSubject);
			return "";
		}catch(Exception e){
			return "";
		}
	}
	
	private static String extractReplyToFeedbackRequest(EmailMessage mail){
		try{
			MessageBody bodyMessage=mail.getBody();
			String body=bodyMessage.toString();
			//Find where the previous email starts at
			int index=body.indexOf("-----Original Message-----");
			if(index>1)
				//Remove the request text and keep only the reply
				body=body.substring(0, index).trim();
			return body;
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * 
	 * This method initiates the authentication with the email server
	 * 
	 * @throws Exception
	 */
	private static void initiateIMAPConnection() throws Exception{
		emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		emailService.setMaximumPoolingConnections(1);
		credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
		emailService.setCredentials(credentials);
		emailService.setUrl(new URI(Constants.MAIL_EXCHANGE_URI));
		//This allows the trace listener to listen to requests and responses
		//THIS IS IF YOU WANT TO ADD A LISTENER FOR PUSHNOTIFICATIONS
		//emailService.setTraceEnabled(true);
		//emailService.setTraceFlags(EnumSet.allOf(TraceFlags.class));
		//		emailService.setTraceListener(new ITraceListener() {
		//            public void trace(String traceType, String traceMessage) {
		//                // do some logging-mechanism here
		//                log("Type:" + traceType + " Message:" + traceMessage);
		//            }
		//        });
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
	public void closeIMAPService(){
		if(timer!=null)
			timer.cancel();
	}

}
