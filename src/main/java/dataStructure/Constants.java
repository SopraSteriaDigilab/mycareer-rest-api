package dataStructure;

import java.time.format.DateTimeFormatter;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains all the constants that are going to be used within the API
 *
 */
public final class Constants {
	
	//Constants for the DataStructure package
	public static final  int INVALID_INT=-1;
	public static final String INVALID_STRING="Invalid Value";
	public static final String INVALID_EMAIL="Invalid Email Address";
	public static final DateTimeFormatter DATE_FORMAT=DateTimeFormatter.ISO_LOCAL_DATE;
	public static final DateTimeFormatter DATE_TIME_FORMAT=DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	public static final DateTimeFormatter YEAR_MONTH_FORMAT=DateTimeFormatter.ofPattern("yyyy-MM");
	public static final String SIMPLE_DATE_FORMAT="yyyy-mm-dd";
	public static final String COMPLETE_DATE_TIME_FORMAT="yyyy-mm-dd hh:mm:ss";
	public static final String COMPLETE_DATE_NOT_SET="Ongoing";
	public static final String PENDING_FEEDBACK="Pending";
	public static final String RECEIVED_ALL_FEEDBACK="Received";
	
	//Constants for the Functionalities package
	public static final String MONGODB_USERNAME="michael";
	public static final String MONGODB_PASSWORD="leahcim";
	public static final String MONGODB_HOST="127.0.0.1";
	public static final int MONGODB_PORT=27017;
	public static final String MONGODB_DB_NAME="Development";
	
	//Constants for the emailServices package
	//Common Constants
	public static final String MAILBOX_ADDRESS="mycareer.feedback@soprasteria.com";
	public static final String MAIL_USERNAME="michael.piccoli@soprasteria.com";
	public static final String MAIL_PASSWORD="MikeSopra16$";
	public static final String MAIL_EXCHANGE_URI="https://outlook.office365.com/ews/exchange.asmx";
	public static final String MAIL_ENCODING_CHARSET="UTF-8";
	public static final long MAIL_REFRESH_TIME=(1*60*1000); //1 minute
	//SMTP Constants
	//public static final String SMTP_HOST="smtp.office365.com";
	//public static final String SMTP_HOST_PORT="587";
		//SSL Port: 465 TSL:587 Plain:25
	//IMAP Constants
	//public static final String IMAP_HOST="outlook.office365.com";
	//public static final String IMAP_HOST_PORT="993";

	
	
	

}
