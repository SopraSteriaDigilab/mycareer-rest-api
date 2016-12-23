package ewsServices;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import dataStructure.Constants;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class SendRandomEmails {

	//Global Variables
	private static ExchangeService emailService;
	private static ExchangeCredentials credentials;

	static{
		emailService=null;
		credentials=null;
	}

	private SendRandomEmails(){}

	public static void sendRandomEmail(String email,int num){
		String bo="Test large number of emails ";
		try {
			initiateIMAPConnection();
		} catch (URISyntaxException e1) {
			System.out.print(e1.toString());
		}
		for(int i=0; i<num; i++){
			try {
				Random random=new Random();
				produceEmail("Counter "+(i+1), bo+random.nextFloat()+1, email);
				System.out.println("Email sent: ("+(i+1)+"/"+num+")");
			} catch (Exception e) {
				System.out.print(e.toString());
			}
		}
	}

	private static void produceEmail(String subject, String body, String email) throws Exception{
		EmailMessage msg= new EmailMessage(emailService);
		msg.setSubject(subject);
		MessageBody mexB=new MessageBody();
		mexB.setText(body);
		mexB.setBodyType(BodyType.Text);
		msg.setBody(mexB);
		msg.getToRecipients().add(email);
		msg.sendAndSaveCopy();
	}

	/**
	 * 
	 * This method initiates the authentication with the email server
	 * @throws URISyntaxException 
	 * 
	 * @throws Exception
	 */
	private static void initiateIMAPConnection() throws URISyntaxException{
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
