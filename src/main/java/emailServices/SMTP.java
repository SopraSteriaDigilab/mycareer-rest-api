package emailServices;

import java.net.URI;
import dataStructure.Constants;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class SMTP {
	
	public static void connect() throws Exception{
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);

		ExchangeCredentials credentials = new WebCredentials(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
		service.setCredentials(credentials);
		service.setUrl(new URI("https://outlook.office365.com/ews/exchange.asmx"));
		Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
		
		System.out.println("messages: " + inbox.getTotalCount());
		EmailMessage msg= new EmailMessage(service);
	    msg.setSubject("Hello world!"); 
	    msg.setBody(MessageBody.getMessageBodyFromText("Sent using the EWS Managed API."));
	    msg.getToRecipients().add("michael.piccoli@soprasteria.com");
	    msg.sendAndSaveCopy();
	    service.close();
	}
}
