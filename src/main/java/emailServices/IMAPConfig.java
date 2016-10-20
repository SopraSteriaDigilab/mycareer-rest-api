package emailServices;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.Timer;
import java.util.TimerTask;
import dataStructure.Constants;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.misc.TraceFlags;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.ITraceListener;
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
		//Verify if there are unread emails
		if(inbox.getUnreadCount()>0){
			//Retrieve list of unread emails
			SearchFilter sf = new SearchFilter.SearchFilterCollection(LogicalOperator.And, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			//findResults = service.findItems(WellKnownFolderName.Inbox,sf, new ItemView(20));
		}
		System.out.println("Number of emails: " + inbox.getTotalCount());
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

}
