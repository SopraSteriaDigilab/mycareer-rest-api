package ewsServices;

import java.net.URI;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import dataStructure.Constants;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.ContactsFolder;
import microsoft.exchange.webservices.data.core.service.item.Contact;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 22nd December 2016
 * 
 * This class contains the definition of the ContactListService which retrieves periodically the list of contacts from the server
 *
 */
public class ContactListService {

	//Global Variables
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;
	private static Timer timer;

	static{
		emailService=null;
		credentials=null;
		timer=null;
	}

	private ContactListService(){}

	public static void initiateJobService(){
		//Schedule a task to run every day
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					//Create a connection with the server;
					initiateEWSConnection();
					System.out.println(LocalTime.now()+" - Updating the Contact List");
					//Check for new emails and then close the connection with the email server
					getContacts();
					closeEWSConnection();
				} catch (Exception e) {
					System.err.println("\t"+LocalTime.now()+" - EWS Connection Error:"
							+ "\n\tContactListService.Java --> "+e.toString());
				}
				finally{
					System.out.println("\t"+LocalTime.now()+" - Job Completed\n");
				}
			}
		}, 0, (1000*60*1000));
	}
	
	private static void getContacts(){
		try {
			ContactsFolder contactFolder=ContactsFolder.bind(emailService, WellKnownFolderName.Contacts);
			FindItemsResults<Item> contacts = emailService.findItems(WellKnownFolderName.Contacts, new ItemView(contactFolder.getTotalCount()));
			
			for(Item tempContact:contacts){
				Contact c=(Contact) tempContact;
				System.out.println("\t"+tempContact.getChangeXmlElementName());
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void closeEWSConnection(){
		if(emailService!=null)
			emailService.close();
	}

	private static void initiateEWSConnection() throws Exception{
		emailService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		emailService.setMaximumPoolingConnections(1);
		credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
		emailService.setCredentials(credentials);
		emailService.setUrl(new URI(Constants.MAIL_EXCHANGE_URI));
		emailService.setTimeout(20000); //20 sec
	}
}
