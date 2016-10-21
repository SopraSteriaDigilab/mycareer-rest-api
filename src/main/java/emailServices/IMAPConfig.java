package emailServices;

import java.awt.MenuItem;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.InvalidAttributeValueException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import dataStructure.Constants;
import dataStructure.Feedback;
import functionalities.EmployeeDAO;
import microsoft.exchange.webservices.data.core.EwsServiceXmlReader;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.misc.TraceFlags;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.ITraceListener;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.MimeContent;
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
					System.out.println("Establishing a connection with the Exchange Server");
					initiateIMAPConnection();
					System.out.println("Checking for new Emails");
					retrieveNewEmails();
					System.out.println("Close connection with the server");
					closeIMAPConnection();
					System.out.println("Task Completed");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, Constants.MAIL_REFRESH_TIME);
	}

	private static void retrieveNewEmails() throws Exception{
		//Open the Inbox folder for the given mailbox
		Folder inbox = Folder.bind(emailService, WellKnownFolderName.Inbox);
		//Verify if there are unread mails
		if(inbox.getUnreadCount()>0){
			//Retrieve list of unread mails
			//Create a filter to use while retrieving the data
			SearchFilter unreadEmailsFilter = new SearchFilter.SearchFilterCollection(LogicalOperator.And, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			//Find any unread mails within the server
			FindItemsResults<Item> findResults = emailService.findItems(WellKnownFolderName.Inbox, unreadEmailsFilter, new ItemView(inbox.getUnreadCount()));
			//Loop through the items and add the data to the database (if any are found)
			try{
				for(Item tempMail:findResults){
					// As a best practice, limit the properties returned to only those that are required.
					//PropertySet propSet = new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.Body, ItemSchema.DisplayCc, ItemSchema.DisplayTo);
					//tempMail = Item.Bind(emailService, itemId, propSet);
					ItemId itemId = tempMail.getId();					
					PropertySet psPropset = new PropertySet();
				    //Limit the number of fields the server needs to return
				    psPropset.setBasePropertySet(BasePropertySet.FirstClassProperties);
				    //This defines that the body of the email will be returned as TEXT and not XML/HTML
				    psPropset.setRequestedBodyType(BodyType.Text);
				    //Retrieve only the properties previously set for the message with the given ID
				    EmailMessage temp = EmailMessage.bind(emailService, itemId,psPropset);
				   
					//System.out.println(temp.getBody());
					
					//EmailMessage temp=(EmailMessage) tempMail;
					//Load the message
					temp.load();
					//MimeContent c=temp.;
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
					System.out.println("--------------------------------------------------------"+toEmployee.getAddress());
					//Validate the ToEmployee with the data in the database to find the user to attach the feedback to
					int userID=findEmployeeOfTOField(toEmployee.getAddress());
					System.out.println("--------------------------------------------------------"+userID);
					if(userID<1){
						//Reply with an email saying the given employee email address is not in the system
//						EmailMessage msg= new EmailMessage(emailService);
//					    msg.setSubject("Feedback Error"); 
//					    msg.setBody(MessageBody.getMessageBodyFromText("The employee email address "+toEmployee.toString()+" for your Feedback has not been found in the system"));
//					    msg.getToRecipients().add(fromField);
//					    msg.sendAndSaveCopy();
//					    //Set the message to unread
//						temp.setIsRead(false);
						//Move this email to te Drafts Folder and go to the next item
						temp.move(WellKnownFolderName.Drafts);
						continue;
					}
					
					//Get the CC field which must match a user in the Database
					//EmailAddressCollection ccElements=temp.getCcRecipients();
					//Extract the body of the message and add it as a new feedback for that specific user
					//temp.getBody().setBodyType(BodyType.Text);
					MessageBody emailBody=temp.getBody();
					//Body b=emailBody.getMessageBodyFromText(temp.loadFromXml(reader, clearPropertyBag);)
					
					
					//cleanEmailBody(emailBody.toString());
					System.out.println("----------_______________--------------");
					cleanEmailBody(emailBody.toString());
					//String s=temp);
					//System.out.println("Body Length:------------------------------ "+s);
					//Clear the body of the email
					//System.out.println("--------------------------------------------------------"+emailBody.getStringFromMessageBody(emailBody));
					//Create a new Feedback and add it to the user with the ID previously found
					Feedback feedObj=new Feedback(0,fromField.getAddress(),emailBody.toString(),type, "Email");
					//Attach the feedback to the User on the Database
					if(EmployeeDAO.insertNewGeneralFeedback(userID, feedObj)){
						System.out.println("Feedback added correctly to user "+userID);
					}
					else{
						System.out.println("Error while adding a feedback to user "+userID);
						temp.setIsRead(false);
						//Move this email to te Drafts Folder and go to the next item
						temp.move(WellKnownFolderName.Drafts);
						continue;
					}
					temp.setIsRead(true);
					temp.update(ConflictResolutionMode.AutoResolve);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			//findResults = service.findItems(WellKnownFolderName.Inbox,sf, new ItemView(20));
		}
		System.out.println("Number of emails: " + inbox.getUnreadCount());
	}
	
	private static String cleanEmailBody(String body) {
		String s=body;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
//			dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(s);
//			doc.getDocumentElement().normalize();
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//			NodeList nList = doc.getElementsByTagName("body");
//			System.out.println("----------------------------");
			Pattern p = Pattern.compile("<body></body>");
		    Matcher m = p.matcher(body);
		    if (m.find()) {

		        // get the matching group
		        String codeGroup = m.group(1);
		        
		        // print the group
		        System.out.format("'%s'\n", codeGroup);

		      }
		} catch (Exception e) {
			e.printStackTrace();
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

	private static void initiateIMAPConnection() throws URISyntaxException{
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
