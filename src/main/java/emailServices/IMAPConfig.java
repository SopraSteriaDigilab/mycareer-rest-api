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

public final class IMAPConfig {

	//Global Variables
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;
	private static Timer timer;

	static{
		emailService=null;
		credentials=null;
		timer=null;
	}

	private IMAPConfig(){}

	public static void initiateIMAPService() throws URISyntaxException{
		//Schedule a task to run every 10 minutes
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					//Create a connection with the server;
					initiateIMAPConnection();
					System.out.println(LocalTime.now()+" - Checking for new Emails");
					retrieveNewEmails();
					closeIMAPConnection();
					System.out.println("\t"+LocalTime.now()+" - Task Completed\n");
				} catch (Exception e) {
					System.out.println(LocalTime.now()+" - Email Service Error: "+e.getMessage());
				}
			}
		}, 0, Constants.MAIL_REFRESH_TIME);
	}

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
				ItemId itemId = tempMail.getId();
				PropertySet psPropset = new PropertySet();
				//Limit the number of fields the server needs to return
				psPropset.setBasePropertySet(BasePropertySet.FirstClassProperties);
				//This defines that the body of the email will be returned as TEXT and not XML/HTML
				psPropset.setRequestedBodyType(BodyType.Text);
				//Retrieve only the properties previously set for the message with the given ID
				EmailMessage temp = EmailMessage.bind(emailService, itemId,psPropset);
				//Load the message
				temp.load();
				//Verify the email
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
				int userID=findEmployeeOfTOField(toEmployee.getAddress());
				//Verify if the ID retrieved is valid or not
				if(userID<1){
					//Reply with an email saying the given employee email address is not in the system
					//EmailMessage msg= new EmailMessage(emailService);
					//msg.setSubject("Feedback Error"); 
					//msg.setBody(MessageBody.getMessageBodyFromText("The employee email address "+toEmployee.toString()+" for your Feedback has not been found in the system"));
					//msg.getToRecipients().add(fromField);
					//msg.sendAndSaveCopy();
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
					EmployeeDAO.insertNewGeneralFeedback(userID, feedObj);
					System.out.println("\t"+LocalTime.now()+" - Feedback added correctly to user "+userID);
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
			}
		}
		else{
			System.out.println("\t"+LocalTime.now()+" - No new Emails Found");
		}
	}

	private static String cleanEmailBody(String body) {
		String s="";
		Document docHtml=Jsoup.parse(body);
		for(Element el: docHtml.select("p")){
			//Remove this line from the body of the email to save space
			if(!el.text().contains("Before printing, think about the")){
				//System.out.println("___________"+el.text().length()+":::: "+el.text());
				s+=el.text();
			}
		}
		return s;
	}

	private static int findEmployeeOfTOField(String emailAddress){
		int employeeID;
		try {
			employeeID = EmployeeDAO.getUserIDFromEmailAddress(emailAddress);
		} catch (InvalidAttributeValueException e) {
			return -1;
		}
		return employeeID;
	}

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

	private static void closeIMAPConnection(){
		if(emailService!=null)
			emailService.close();
	}
	public void closeIMAPService(){
		if(timer!=null)
			timer.cancel();
	}

}
