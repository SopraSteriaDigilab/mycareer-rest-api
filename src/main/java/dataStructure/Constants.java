package dataStructure;

import java.time.format.DateTimeFormatter;

/**
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
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
	public static final int MAX_TITLE_LENGTH=151;
	
	//Invalid Messages
	public static final String INVALID_MAIL_CONTEXT="The given 'Email Address' is not valid in this context";
	public static final String INVALID_GUID_CONTEXT="The given 'GUID' is not valid in this context";
	public static final String INVALID_COMPANY_CONTEXT="The given 'company' is not valid in this context";
	public static final String INVALID_TEAM_CONTEXT="The given 'team' is not valid in this context";
	public static final String INVALID_USERID_CONTEXT="The given 'ID' is not valid in this context";
	public static final String INVALID_USERNAME_CONTEXT="The given 'username' is not valid in this context";
	public static final String INVALID_SURNAME_CONTEXT="The given 'surname' is not valid in this context";
	public static final String INVALID_FORENAME_CONTEXT="The given 'forename' is not valid in this context";
	public static final String INVALID_TITLE_CONTEXT="The given 'title' is not valid in this context";
	public static final String INVALID_DESCRIPTION_CONTEXT="The given 'description' is not valid in this context";
	public static final String INVALID_PROGRESS_CONTEXT="The given 'progress' value is not valid in this context";
	public static final String INVALID_CATEGORY_CONTEXT="The given 'category' value is not valid in this context";
	public static final String INVALID_FROMWHO_CONTEXT="The given 'fromWho' value is not valid in this context";
	public static final String INVALID_FULLNAME_CONTEXT="The given 'fullName' value is not valid in this context";
	public static final String INVALID_FEEDBACKTYPE_CONTEXT="The given 'feedback type' value is not valid in this context";
	public static final String INVALID_FEEDBACKSOURCE_CONTEXT="The given 'source' value is not valid in this context";
	public static final String INVALID_PASTDATE_CONTEXT="The given date is not valid because it is in the past";
	public static final String INVALID_DATEFORMAT_CONTEXT="The format of the given 'date' is not valid";
	public static final String INVALID_FEEDBACKLIST_CONTEXT="Not all feedback were added due to their incorrect format/status";
	public static final String INVALID_NULLFEEDBACKLIST_CONTEXT="The given list of feedback is null";
	public static final String INVALID_NULLFEEDBACK_CONTEXT="The given feedback is null";
	public static final String INVALID_FEEDBACK_CONTEXT="The given feedback is not valid";
	public static final String INVALID_FEEDBACKID_CONTEXT="The given Feedback ID is not valid";
	public static final String INVALID_OBJECTIVELIST_CONTEXT="Not all Objectives were added due to their incorrect format/status";
	public static final String INVALID_NULLOBJECTIVE_CONTEXT="The given Objective is null";
	public static final String INVALID_NULLOBJECTIVELIST_CONTEXT="The given list of Objectives is null";
	public static final String INVALID_OBJECTIVEID_CONTEXT="The given Objective ID is not valid";
	public static final String INVALID_OBJECTIVE_CONTEXT="The given Objective is not valid";
	public static final String INVALID_NULLNOTE_CONTEXT="The given Note is null";
	public static final String INVALID_NULLNOTELIST_CONTEXT="The given list of Notes is null";
	public static final String INVALID_NOTELIST_CONTEXT="Not all Notes were added due to their incorrect format/status";
	public static final String INVALID_NOTEID_CONTEXT="The given Note ID is not valid";
	public static final String INVALID_NOTE_CONTEXT="The given Note is not valid";
	public static final String INVALID_NULLDEVNEEDSLIST_CONTEXT="The given list of Development Needs is null";
	public static final String INVALID_DEVNEEDSLIST_CONTEXT="Not all Development Needs were added due to their incorrect format/status";
	public static final String INVALID_NULLDEVNEED_CONTEXT="The given Development Need is null";
	public static final String INVALID_DEVNEEDID_CONTEXT="The given Development Need ID is not valid";
	public static final String INVALID_DEVNEED_CONTEXT="The given Development Need is not valid";
	public static final String INVALID_GROUPFEEDBACKREQUESTLIST_CONTEXT="Not all Feedback Requests were added due to their incorrect format/status";
	public static final String INVALID_NULLGROUPFEEDBACKREQUESTLIST_CONTEXT="The given list of Feedback Requests is null";
	public static final String INVALID_GROUPFEEDBACKREQUESTID_CONTEXT="The given ID for the Group Feedback Request is not valid";
	public static final String INVALID_NULLGROUPFEEDBACKREQUEST_CONTEXT="The given Group Feedback Request object is null";
	public static final String INVALID_NULLCOMPETENECYLIST_CONTEXT="The given list of Competencies is null";
	public static final String INVALID_COMPETENCYLIST_CONTEXT="Not all Competencies were added due to their incorrect format/status";
	public static final String INVALID_COMPETENCYTID_CONTEXT="The given Competency ID is not valid";
	public static final String INVALID_COMPETENCY_CONTEXT="The given Competency is not valid";
	public static final String INVALID_NULLCOMPETENCY_CONTEXT="The given Competency is null";
	public static final String INVALID_NULLFEEDBACK_OBJECTIVEID_CONTEXT="The given feedback is null or the provided objective ID is not valid";
	public static final String INVALID_FEEDBACKREQ_RECIPIENT_CONTEXT="The given 'recipient' value is not valid in this context";
	public static final String INVALID_NULLFEEDBACKREQ_REPLIES_CONTEXT="The given 'list of repies' is null";
	public static final String INVALID_FEEDBACKNOTFOUND_CONTEXT="The given 'feedback' does not exist in the user data, and therefore it cannot be removed";
	public static final String INVALID_FEEDBACKREQ_NOTFOUND_CONTEXT="The given 'feedback request' does not exist in the user data, and therefore it cannot be removed";
	public static final String INVALID_NULLFEEDBACKREQLIST_CONTEXT="The given list of feedback requests is null";
	public static final String INVALID_FEEDBACKREQLIST_CONTEXT="The given list of feedback requests is not valid";
	public static final String INVALID_FEEDBACKREQ_CONTEXT="The given feedback request is not valid";
	public static final String INVALID_FEEDBACKDUPLICATE_CONTEXT="The Feedback request is a duplicate and cannot be added to this GroupFeedback";
	public static final String INVALID_FEEDBACKREQ_ID_CONTEXT="The given 'feedback request ID' value is not valid in this context";
	
	public static final String INVALID_NOTE_TYPE="A note type must be between 0 and 6";
	public static final String INVALID_NOTE_LINKTYTLE="The given 'link title' value is not valid in this context";
	public static final String INVALID_NOTE_BODY="The given 'note body' value is not valid in this context";
	public static final String INVALID_NOTE_FROMWHO="The name of the note writer is not valid in this context";
	public static final String INVALID_OBJECTIVE_PROPOSEDBY="The given 'proposed by' valud is not valid in this context";
	public static final String INVALID_OBJECTIVE_PERFORMANCE="The given 'performance' value is not valid in this context";
	public static final String INVALID_SMTPSERVICE_NOTES="The notes cannot exceed 1000 characters";
	public static final String INVALID_SMTPSERVICE_TOOMANYADDRESSES="Too many email addresses, The maximum number allowed is 20";
	
	
	
	
	
	

	//Constants for the Functionalities package
	//MongoDB Details
	public static final String MONGODB_USERNAME="michael";
	public static final String MONGODB_PASSWORD="leahcim";
	public static final String MONGODB_HOST="127.0.0.1";
	public static final int MONGODB_PORT=27017;
	public static final String MONGODB_DB_NAME="Development";
	//AD Details
//	public static final String AD_HOST="ldap://one.steria.dom";
//	public static final int AD_PORT=389;
//	public static final String AD_AUTHENTICATION="simple";
//	public static final String AD_USERNAME="PICCOLI Michael - 675599";
//	public static final String AD_PASSWORD="";
//	public static final String AD_TREE="OU=UK,OU=Internal,OU=People,DC=one,DC=steria,DC=dom";
//	public static final String[] AD_ATTRIBUTES={"sn","givenName","company", "sAMAccountName", "employeeID", "objectGUID", "mail", "directReports"};
	
	public static final String AD_HOST="ldap://emea.msad.sopra";
	public static final int AD_PORT=389;
	public static final String AD_AUTHENTICATION="simple";
	public static final String AD_USERNAME="svc_mycareer@emea.msad.sopra";
	public static final String AD_PASSWORD="N9T$SiPSZ";
	public static final String AD_TREE="ou=uk,ou=users,ou=sopragroup,ou=usersemea,DC=emea,DC=msad,DC=sopra";
	public static final String[] AD_ATTRIBUTES={"sn","givenName","company", "sAMAccountName", "extensionAttribute7", "objectGUID", "mail", "directReports"};
	

	//Constants for the emailServices package
	public static final String MAILBOX_ADDRESS="MYCAREER.FEEDBACK@soprasteria.com";
	public static final String MAIL_USERNAME="michael.piccoli@soprasteria.com";
	public static final String MAIL_PASSWORD="";
	public static final String MAIL_EXCHANGE_URI="https://outlook.office365.com/ews/exchange.asmx";
	
//	public static final String MAILBOX_ADDRESS="feedback.UK@soprasteria.com";
//	public static final String MAIL_USERNAME="feedback.UK@soprasteria.com";
//	public static final String MAIL_PASSWORD="Auto_Map_$459$";
//	public static final String MAIL_EXCHANGE_URI="https://mailbox.corp.sopra/ews/exchange.asmx";
		//	public static final String MAIL_EXCHANGE_URI="https://mailbox.corp.sopra/ews/Services.wsdl";
	
	public static final String MAIL_ENCODING_CHARSET="UTF-8";
	public static final long MAIL_REFRESH_TIME=(1*60*1000); //1 minute

	//Arrays Containing all Competences including both Names and Descriptions
	public static final String [] COMPETENCY_NAMES  = new String[]
			{
					"Accountability", 
					"Business Awareness",
					"Effective Communication", 
					"Future Orientation", 
					"Innovation and Change", 
					"Leadership", 
					"Service Excellence", 
					"Teamwork"
			};
	public static final String [] COMPETENCY_DESCRIPTIONS = new String[] 
			{
					"Shows drive and commitment to achieve objectives. Is willing to act decisively and strives to find ways of overcoming obstacles. Takes ownership of issues and empowers team members by giving them an appropriate level of responsibility and autonomy. Does not give up and can be counted on to deliver. Is action oriented.",
					"Is able to recognise opportunities to leverage Sopra Steria's capabilities to provide practical and profitable solutions to client's needs. Understands the key commercial issues that affect profitability and growth. Builds relationships with clients and seeks to understand their needs and priorities. Uses this knowledge to provide flexible and reliable solutions to meet and exceed clients' expectations and deliver value to Sopra Steria. Acts as a company Ambassador.",
					"Expresses ideas clearly, persuasively and with impact. Listens to others and convinces them to accept their ideas. Is open and honest with colleagues and clients. Able to write or present clearly and succinctly in a variety of communication settings and audiences. Gets messages across that have the desired effect. Is timely with provision of information.",
					"Takes account of a wide range of long-term changes and trends in technology, the market-place and in the business and plans ahead accordingly. Has a clear view of where they want to get to in the medium and longer term. Understands competition. Anticipates consequences and trends.",
					"Shows flexibility and the desire to acquire new knowledge and ideas. Accepts and supports the need for change and looks for new ways of solving problems. Demonstrates the ability to lead and foster change within the organisation. Can act differently depending upon the situation and can handle uncertainty. Is committed to continuous improvement. Comes up with practical steps to implement own or others ideas.",
					"Provides clear direction and motivates and inspires others to succeed. Seeks to develop the skills and confidence of others and to recognise and develop talent. Acts as a role model and provides appropriate induction, feedback and coaching to team members. Is willing to confront and challenge poor performance and encourages team members to contribute ideas. Brings the best out of others.",
					"Builds Sopra Steria's reputation in the market by setting high standards of service and delivery. Knows what to measure and how to measure it and looks for opportunities for synergies. Is committed to Service Excellence.",
					"Works cooperatively with colleagues and considers their needs and the impact of decisions on them. Seeks to build relationships across the organisation and to work for the overall good of the business. Able to find common ground and gain trust. Encourages collaboration."
			};
	
	public static int getCompetencyIDGivenTitle(String title){
		for(int i=0; i<COMPETENCY_NAMES.length; i++){
			if(COMPETENCY_NAMES[i].equalsIgnoreCase(title))
				return i;
		}
		return -1;
	}
	
	public static String getCompetencyNameGivenID(int id){
		if(id>-1 && id<COMPETENCY_NAMES.length)
			return COMPETENCY_NAMES[id];
		return null;
	}

	public static String getCompetencyDescriptionGivenID(int id){
		if(id>-1 && id<COMPETENCY_DESCRIPTIONS.length)
			return COMPETENCY_DESCRIPTIONS[id];
		return null;
	}



}
