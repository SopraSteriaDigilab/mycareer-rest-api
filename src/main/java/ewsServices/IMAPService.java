package ewsServices;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.management.InvalidAttributeValueException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import dataStructure.ADProfile_Basic;
import dataStructure.Constants;
import dataStructure.Feedback;
import functionalities.ADProfileDAO;
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
	 * This method initiate the email service and checks for new emails at regular intervals
	 * 
	 * @throws URISyntaxException
	 */
	public static void initiateIMAPService(){
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
				} catch (Exception e) {
					System.err.println("\t"+LocalTime.now()+" - Email Service Error:"
							+ "\n\tIMAPSErvice.Java --> "+e.toString());
				}
				finally{
					System.out.println("\t"+LocalTime.now()+" - Task Completed\n");
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
		Folder inboxFolder = Folder.bind(emailService, WellKnownFolderName.Inbox);
		int counter=1;
		int unreadCounter=inboxFolder.getUnreadCount();

		//Verify if there are unread mails
		if(unreadCounter>0){
			System.out.println("\t"+LocalTime.now()+" - Processing unread emails...");
			//Retrieve list of unread mails
			//Create a filter to use while retrieving the data
			SearchFilter unreadEmailsFilter = new SearchFilter.SearchFilterCollection(LogicalOperator.And, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			//Find any unread mails within the server
			FindItemsResults<Item> notReadEmails = emailService.findItems(WellKnownFolderName.Inbox, unreadEmailsFilter, new ItemView(inboxFolder.getUnreadCount()));
			//Loop through the items and add the data to the database (if any are found)
			for(Item tempMail:notReadEmails){
				// As a best practice, limit the properties returned to only those that are required.
				PropertySet psPropset = new PropertySet();
				//Limit the number of fields the server needs to return
				psPropset.setBasePropertySet(BasePropertySet.FirstClassProperties);
				//This defines that the body of the email will be returned as TEXT and not as XML/HTML
				psPropset.setRequestedBodyType(BodyType.Text);
				//Retrieve only the properties previously set for the message with the given ID
				EmailMessage openNotReadEmail = EmailMessage.bind(emailService, tempMail.getId(),psPropset);
				System.out.println("\t"+LocalTime.now()+" - Loading Element ("+counter++ +"/"+unreadCounter+")...");
				//Load the message
				openNotReadEmail.load();

				//STEP 1 CHECK FOR INVALID EMAILS


				//Verify if the email is auto-generated
				//Check if the subject contains the word "undelivered" which means that an address was invalid,
				//therefore it must be removed from the feedback request object of the user]
				String subjectEmailCheck=openNotReadEmail.getSubject();
				if(subjectEmailCheck==null){
					//If the system enters in this stage, the subject of the email is empty
					//Treat it is a generic feedback
					genericFeedbackFound(openNotReadEmail);
					continue;
				}

				if(openNotReadEmail.getInternetMessageHeaders().find("Auto-Submitted")!=null
						//|| openNotReadEmail.getInternetMessageHeaders().find("Return-Path").equals("<>")
						|| subjectEmailCheck.toLowerCase().contains("undeliverable") 
						|| subjectEmailCheck.toLowerCase().contains("undelivered")
						|| subjectEmailCheck.contains("New Feedback Received")
						|| subjectEmailCheck.contains("A BIG Thank You")
						|| subjectEmailCheck.toLowerCase().contains("auto")
						|| subjectEmailCheck.contains("Email Service Error")){
					//Extract the Undeliverable emails
					if(subjectEmailCheck.toLowerCase().contains("undeliver") && subjectEmailCheck.toLowerCase().contains("request")){
						//Treat the undeliverable email
						undeliverableEmailFound(openNotReadEmail);
						continue;
					}
					System.out.println("\t"+LocalTime.now()+" - Irrelevant Email,  Moved to DRAFTS");
					openNotReadEmail.move(WellKnownFolderName.Drafts);
					continue;
				}				
				//STEP 2 CHECK FOR A REQUEST ID INSIDE THE SUBJECT

				//Check for a RequestID inside the body of the email
				String requestIDSetInSubject=retrieveRequestID(openNotReadEmail);
				//If such ID exists it's a reply from a feedback request
				if(!requestIDSetInSubject.equals("")){
					//Process the link to a feedback request
					linkToAFeedbackRequestFound(openNotReadEmail, requestIDSetInSubject);
					continue;
				}
				//STEP 3 WHEN REQUEST ID NOT FOUND, TRY TO RETRIEVE A UNREQUESTED FEEDBACK

				//If it doesn't exist, it's a unrequested feedback
				else{
					genericFeedbackFound(openNotReadEmail);
					continue;
				}
			}
		}
		else{
			System.out.println("\t"+LocalTime.now()+" - No new Emails Found");
		}
	}

	private static void genericFeedbackFound(EmailMessage message) throws Exception{
		//Get the TO field && remove the mycarer.feedback email address if it's there by mistake
		List<EmailAddress> toElements=message.getToRecipients().getItems();
		for(int i=0; i<toElements.size(); i++){
			if(toElements.get(i).toString().contains(Constants.MAILBOX_ADDRESS))
				toElements.remove(i);
		}
		//Get the Sender of the email
		EmailAddress fromFieldEmail=message.getFrom();

		if(toElements.size()<1){
			//No user in the TO field, impossible to find employee/s
			System.out.println("\t"+LocalTime.now()+" - No user can be linked to this feedback");						
			System.out.println("\t"+LocalTime.now()+" - Irrelevant Email found, Moved to DRAFTS");
			//It is an irrelevant email to the system, move it to another folder so that
			//the system admin can manage them separately
			message.move(WellKnownFolderName.Drafts);

			//Interrupt the flow and move on to the next email
			return;
		}


		else{
			System.out.println("\t"+LocalTime.now()+" - General Feedback found...");
			//If the email enters this portion of code, this is a unrequested feedback

			//The email is internal the company if the email address contains @soprasteria.com
			String type="";
			String fullNameFeedbackProvider="";
			if(fromFieldEmail.toString().contains("@soprasteria.com")){
				type="Internal";
				//Find the full name of the employee providing the feedback from the AD
				try{
					fullNameFeedbackProvider=ADProfileDAO.findEmployeeFullNameFromEmailAddress(fromFieldEmail.getAddress().toString());
				}
				catch(Exception e){
					System.out.println("\t"+LocalTime.now()+" IMAPSErvice.Java --> Error while finding the full name of the feedback provider\n"+e.getMessage());
				}
			}
			else
				type="External";

			String cleanBodyEmail=cleanEmailBody(message.getBody().toString()).trim();
			if(cleanBodyEmail.length()<3){
				System.out.println("\t"+LocalTime.now()+" - Irrelevant Email found, empty body,  Moved to DRAFTS");
				message.move(WellKnownFolderName.Drafts);
				return;
			}

			//Search for employee/s to link this feedback to

			//For each employee found in the TO field,
			//Create the feedback object and add it to the user data
			List<String> successfullyAdded=new ArrayList<>();
			List<String> unsuccessfullyAdded=new ArrayList<>();
			boolean errorFlag=false;
			for(EmailAddress toElem: toElements){
				try{
					ADProfile_Basic userFound=ADProfileDAO.authenticateUserProfile(toElem.getAddress());
					//Remove unnecessary part of the email body

					Feedback feedbackObj=new Feedback(fromFieldEmail.getAddress(),type,"Email",false,fullNameFeedbackProvider,message.getBody().toString());
					//Attach the feedback to the User on the Database
					if(EmployeeDAO.insertNewGeneralFeedback(userFound.getEmployeeID(), feedbackObj)){
						System.out.println("\t"+LocalTime.now()+" - General Feedback added correctly to user "+userFound.getEmployeeID());
						successfullyAdded.add(toElem.getAddress());

						//Notice employee/s regarding the new feedback received
						{
							String bodyEmailMsg="Hi,\nGood news for you.\nThe user "+fromFieldEmail.toString()+" has given you a new feedback.\n"
									+ "Login into MyCareer website to find out how you did.\n\nRegards,\nTeam MyCareer";
							contactUserViaEmail(toElem.getAddress(), "New Feedback Received", bodyEmailMsg);
						}
					}
					else{
						System.out.println("\t"+LocalTime.now()+" - The General Feedback couldn't be added to user "+userFound.getEmployeeID());
						unsuccessfullyAdded.add(toElem.getAddress());
					}
				}
				catch(Exception e){
					System.out.println("\t"+LocalTime.now()+" - "+e.getMessage());
					//User not found in the AD, add it to the list of invalid addresses
					if (e.getMessage().contains(Constants.NOTFOUND_EMAILORUSERNAME_AD))
						unsuccessfullyAdded.add(toElem.getAddress());
					else{
						message.setIsRead(false);
						message.move(WellKnownFolderName.Drafts);
						System.out.println("\t"+LocalTime.now()+" - General Error: "+e.getMessage());
						errorFlag=true;
						break;
					}
				}
			}
			if(errorFlag)
				return;
			//Mark the Email as read and update it into the mail server
			message.setIsRead(true);
			message.update(ConflictResolutionMode.AutoResolve);

			//Praise feedback provider
			if(unsuccessfullyAdded.size()>0)
				praiseFeedbackProvider(fromFieldEmail, successfullyAdded, unsuccessfullyAdded);
		}
	}

	private static void undeliverableEmailFound(EmailMessage message) throws Exception{
		System.out.println("\t"+LocalTime.now()+" - Undeliverable Email found...");
		//Find the Request ID, the employeeID and remove this data from the DB as well as
		//sending an error message to the feedback requester
		String reqID_Undelivered=retrieveRequestID(message);
		if(!reqID_Undelivered.equals("")){
			Long reqID_EmpID;
			try {
				String employeeIDFromSubject = message.getSubject();
				int indexEmp=employeeIDFromSubject.indexOf("_");
				employeeIDFromSubject=employeeIDFromSubject.substring(indexEmp-6, indexEmp);
				reqID_EmpID=Long.parseLong(employeeIDFromSubject);
				//Now that we have the user ID, let's try to remove the full request from the system
				try{
					String incorrectEmailAddress=EmployeeDAO.removeFeedbackReqFromUser(reqID_Undelivered, reqID_EmpID);
					if(!incorrectEmailAddress.equals("")){
						System.out.println("\t"+LocalTime.now()+" Undeliverable Feedback Request Removed Successfully from employee: "+reqID_EmpID);
						//Update email
						message.setIsRead(true);
						message.update(ConflictResolutionMode.AutoResolve);
						//Send email to feedback requester regarding the invalid email address
						String emailSender=EmployeeDAO.getUserEmailAddressFromID(reqID_EmpID);
						if(!emailSender.equals("")){
							contactUserViaEmail(emailSender, "Incorrect Email Address", "Hi,\nThe Email address provided: "
									+ ""+incorrectEmailAddress+" is incorrect.\nFor this reason, a feedback request could not be sent to the above recipient.\n"
									+ "\nRegards,\nTeam MyCareer");
							return;
						}
						message.move(WellKnownFolderName.Drafts);
						System.out.println("\t"+LocalTime.now()+" Invalid Email address found! Email moved to DRAFTS");
					}
				}
				catch(InvalidAttributeValueException er){
					System.out.println("\t"+LocalTime.now()+" Error - "+er.getMessage());
					message.move(WellKnownFolderName.Drafts);
				}
			} catch (Exception e) {
				//If an error occurs while retriving the employeeID, move the mail to the Draft folder
				System.out.println("\t"+LocalTime.now()+" - The system is unable to retrieve a valid employeeID from the email subject, Email moved to DRAFTS");
				message.move(WellKnownFolderName.Drafts);
			}
			//Process the next email
			return;
		}
	}

	private static void linkToAFeedbackRequestFound(EmailMessage message, String requestID) throws Exception{
		System.out.println("\t"+LocalTime.now()+" - Response to a Feedback Request found...");
		//Retrieve the FROM field
		EmailAddress fromFieldEmail=message.getFrom();
		//The email is internal the company if the email address contains @soprasteria.com
		String type="";
		String fullNameFeedbackProvider="";

		if(fromFieldEmail.toString().contains("@soprasteria.com")){
			type="Internal";
			//Find the full name of the employee providing the feedback from the AD
			try{
				fullNameFeedbackProvider=ADProfileDAO.findEmployeeFullNameFromEmailAddress(fromFieldEmail.getAddress().toString());
			}
			catch(Exception e){
				System.out.println("\t"+LocalTime.now()+" IMAPSErvice.Java --> Error while finding the full name of the feedback provider\n"+e.getMessage());
			}
		}
		else
			type="External";

		//Get the body of the email extracting only the necessary parts
		String bodyEmail=extractReplyToFeedbackRequest(message).trim();
		bodyEmail=cleanEmailBody(bodyEmail).trim();
		if(bodyEmail.length()<3){
			System.out.println("\t"+LocalTime.now()+" - Irrelevant Email found, empty body,  Moved to DRAFTS");
			message.move(WellKnownFolderName.Drafts);
			return;
		}

		//Find email address of employee to link feedback to
		String emailEmployee=findEmployeeEmailFromSubject(message);
		if(emailEmployee.equals("")){
			System.out.println("\t"+LocalTime.now()+" - Invalid Employee, A feedback cannot be created (Mail moved to the DRAFTS folder)");
			//Interrupt the program, ID Invalid/not found, the feedback provider has change the ID
			//Move the current email to a folder the system admin will deal with it
			message.setIsRead(false);
			message.move(WellKnownFolderName.Drafts);

			//Notify the feedback provider regarding the USER NOT FOUND error
			{
				String bodyError="Hi,\nThank you for your feedback, unfortunately the system could not identify a valid employee to associate your feedback to.\n"
						+ "Please do NOT change the subject of the feedback request when replying to a feedback request.\n\nRegards,\nThe MyCareer Team";
				contactUserViaEmail(fromFieldEmail.getAddress(), "Feedback Error", bodyError);
			}
			//Interrupt the flow for this email and move on to the next one
			return;
		}

		//Create an Feedback Object					
		Feedback feedbackObj=new Feedback(fromFieldEmail.getAddress(),type,"Email",true, fullNameFeedbackProvider, message.getBody().toString());

		//Now that we have all the details, pass this data to the EmployeeDAO which will try to link the feedback to the user
		boolean res=EmployeeDAO.linkFeedbackReqReplyToUserGroupFBReq(emailEmployee, requestID, feedbackObj);
		//If the task has been completed successfully, mark the Email as read and update it into the mail server
		if(res){
			message.setIsRead(true);
			message.update(ConflictResolutionMode.AutoResolve);
			System.out.println("\t"+LocalTime.now()+" - Reply to a Feedback Request linked correctly");

			//Praise Feedback provider
			//Commented out because Pav wants to reduce the amount of emails sent to a feedback provider
			//List<String> sentTo=new ArrayList<>();
			//sentTo.add(emailEmployee);
			//praiseFeedbackProvider(fromFieldEmail, sentTo, null);

			//Notify user about the new feedback added
			String bodyErrorEmail="Hi,\nGood news for you.\nThe user "+fromFieldEmail.toString()+" had sent you a feedback.\n"
					+ "Login into MyCareer website to find out how you did.\n\nRegards,\nTeam MyCareer";
			contactUserViaEmail(emailEmployee, "New Feedback Received", bodyErrorEmail);
		}
		else{
			System.out.println("\t"+LocalTime.now()+" - Error while linking the feedback request response");
			message.move(WellKnownFolderName.Drafts);
			contactUserViaEmail(Constants.MAILBOX_ADDRESS,"Adding feedback error", "Error while linking the feedback request response: \n\n"+feedbackObj.toString());
		}
	}

	/**
	 * 
	 * This method cleans the body of the email from html tags and unnecessary strings 
	 * 
	 * @param body the HTML body of the email to clean
	 * @return The message contained within the body of the html email
	 */
	private static String cleanEmailBody(String body) {
		String s="";
		Document docHtml=Jsoup.parse(body);

		//This is for when a reply is sent via an Iphone
		Element elem=docHtml.select("div").first();
		if(elem!=null && elem.hasText()){
			s+=elem.text();
		}

		//Do this for the HTML body 
		for(Element el: docHtml.select("p")){
			//Remove this line from the body of the email to save space
			if(!el.text().contains("Before printing, think about the")){
				s+=el.text();
			}
		}

		//Do this if the body is of type TEXT
		if(s.equals("")){
			int index=body.indexOf("Before printing, think about the");
			if(index>0)
				body=body.substring(0, index);
			return body;
		}
		return s;
	}

	/**
	 * 
	 * This method finds an employee's email address given the subject of the email
	 * 
	 * @param mail the email received in the mail box
	 * @return the String containing the email address of the employee
	 */
	private static String findEmployeeEmailFromSubject(EmailMessage mail){
		String employeeIDFromSubject;
		long empID=0;
		try {
			employeeIDFromSubject = mail.getSubject();
			int indexEmp=employeeIDFromSubject.indexOf("_");
			employeeIDFromSubject=employeeIDFromSubject.substring(indexEmp-6, indexEmp);
			empID=Long.parseLong(employeeIDFromSubject);
			return EmployeeDAO.getUserEmailAddressFromID(empID);			
		} catch (InvalidAttributeValueException e) {
			//If the code end in here, no user has been found in the our DB.
			//We then need to check the AD, if the user exists, create it in our system DB
			try{
				ADProfile_Basic adData=ADProfileDAO.verifyIfUserExists(empID);
				//Try again to retrieve the email, this cannot fail this time
				return EmployeeDAO.getUserEmailAddressFromID(adData.getEmployeeID());
			}catch(Exception er){
				return "";
			}
		}
		catch(Exception err){
			return "";
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
			//If the subject doesn't contain feedback request, it is a general feedback or an 
			//unrecognised email, not relevant to our system
			if(!idInSubject.contains("Feedback Request"))
				return "";
			int locationIndex=idInSubject.indexOf("_");
			//Retrieve ID
			if(locationIndex>0)
				return idInSubject.substring(locationIndex-6);
			return "";
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * 
	 * This method extracts the reply from the body of the received email
	 * 
	 * @param mail The EmailMessage 
	 * @return a String containg the body of the mail
	 */
	private static String extractReplyToFeedbackRequest(EmailMessage mail){
		try{
			MessageBody bodyMessage=mail.getBody();
			//Verify if the body of the email is HTML or TEXT
			if(bodyMessage.getBodyType().equals(BodyType.HTML)){
				bodyMessage.setBodyType(BodyType.Text);
			}
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
	 * This method contacts the system administrator when an error occurs, providing the error message
	 * 
	 * @param subject 
	 * @param body body of the email
	 * @throws Exception
	 */
	private static void contactUserViaEmail(String to,String subject, String body) throws Exception{
		EmailMessage msg= new EmailMessage(emailService);
		msg.setSubject(subject);
		MessageBody mexB=new MessageBody();
		mexB.setBodyType(BodyType.Text);
		mexB.setText(body);
		msg.setBody(mexB);
		msg.getToRecipients().add(to);
		msg.sendAndSaveCopy();
	}

	/**
	 * 
	 * This method thanks the feedback provider
	 * 
	 * @param to the feedback provider email address
	 * @param delivered the list of people the email has been sent to
	 * @param undelivered the list of invalid SopraSteria employees
	 * @throws Exception
	 */
	private static void praiseFeedbackProvider(EmailAddress to, List<String> delivered, List<String> undelivered) throws Exception{
		EmailMessage msg= new EmailMessage(emailService);
		msg.setSubject("A BIG Thank You");
		MessageBody mexB=new MessageBody();
		mexB.setBodyType(BodyType.Text);
		String body="Hi,\n\nWe have processed your feedback.\nThis is what happened:\n\n";
		if(delivered!=null && delivered.size()>0){
			body+="We have linked your feedback to:\n";
			body+=getStringFromListEmails(delivered)+"\n";
		}
		if(undelivered!=null && undelivered.size()>0){
			body+="Unfortunately, We could not find the following employee/s in our system:\n";
			body+=getStringFromListEmails(undelivered)+"\n";
		}
		body+="\nKind Regards,\nMyCareer Team\n\n";
		mexB.setText(body);

		msg.setBody(mexB);
		msg.getToRecipients().add(to);
		msg.sendAndSaveCopy();
	}

	/**
	 * 
	 * This method returns a formatted string given a list of strings
	 * 
	 * @param data the List<String>
	 * @return A simplified String
	 */
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
	private static void initiateIMAPConnection() throws Exception{
		emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		emailService.setMaximumPoolingConnections(1);
		credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
		emailService.setCredentials(credentials);
		emailService.setUrl(new URI(Constants.MAIL_EXCHANGE_URI));
		emailService.setTimeout(120000);
		//This allows the trace listener to listen to requests and responses
		//THIS IS IF YOU WANT TO ADD A LISTENER FOR PUSH NOTIFICATIONS
		//emailService.setTraceEnabled(true);
		//emailService.setTraceFlags(EnumSet.allOf(TraceFlags.class));
		//		emailService.setTraceListener(new ITraceListener() {
		//            public void trace(String traceType, String traceMessage) {
		//                // do some logging-mechanism here
		//                log("Type:" + traceType + " Message:" + traceMessage);
		//            }
		//        });

		//https://mailbox.corp.sopra/ews/exchange.asmx
		// Setup binding with username and password of the delegate account
		//		credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD, "corp.sopra");
		//		emailService.setCredentials(credentials);
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

	//	private static TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager(){
	//	public java.security.cert.X509Certificate[] getAcceptedIssuers(){
	//			return null;
	//	  	}
	//	public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String    authType) {
	//	}
	//
	//	public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) 
	//	{
	//	}
	//	}
	//
	//	// Java Secure Socket Connection 
	//	private static HostnameVerifier hv = new HostnameVerifier() 
	//	{
	//	@Override
	//		public boolean verify(String urlHostName, SSLSession session) 
	//	{
	//			return true;
	//		}
	//	};
	//
	//	//Sets the ssl config.
	//	private static void setSSLConfig() throws Exception 
	//	{
	//	   SSLContext context = SSLContext.getInstance("SSL");
	//	   context.init(null, trustAllCerts, new java.security.SecureRandom());
	//	   HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	//	   HttpsURLConnection.setDefaultHostnameVerifier(hv);
	//	}

}
