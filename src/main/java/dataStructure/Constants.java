package dataStructure;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * This class contains all the constants that are going to be used within the API
 *
 */
public final class Constants
{

  public static final ZoneId UK_TIMEZONE = ZoneId.of("Europe/London");

  // Constants for the DataStructure package
  public static final int INVALID_INT = -1;
  public static final String INVALID_STRING = "Invalid Value";
  public static final String INVALID_EMAIL = "Invalid Email Address";
  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  public static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
  public static final String SIMPLE_DATE_FORMAT = "yyyy-mm-dd";
  public static final String COMPLETE_DATE_TIME_FORMAT = "yyyy-mm-dd hh:mm:ss";
  public static final String COMPLETE_DATE_NOT_SET = "Ongoing";
  public static final String PENDING_FEEDBACK = "Pending";
  public static final String RECEIVED_ALL_FEEDBACK = "Received";
  public static final int MAX_TITLE_LENGTH = 151;

  // Invalid Messages
  public static final String INVALID_CONTEXT_MAIL = "The given 'Email Address' is not valid in this context";
  public static final String INVALID_CONTEXT_GUID = "The given 'GUID' is not valid in this context";
  public static final String INVALID_CONTEXT_COMPANY = "The given 'company' is not valid in this context";
  public static final String INVALID_CONTEXT_TEAM = "The given 'team' is not valid in this context";
  public static final String INVALID_CONTEXT_USERID = "The given 'ID' is not valid in this context";
  public static final String INVALID_CONTEXT_USERNAME = "The given 'username' is not valid in this context";
  public static final String INVALID_CONTEXT_SURNAME = "The given 'surname' is not valid in this context";
  public static final String INVALID_CONTEXT_FORENAME = "The given 'forename' is not valid in this context";
  public static final String INVALID_CONTEXT_TITLE = "The given 'title' is not valid in this context";
  public static final String INVALID_CONTEXT_DESCRIPTION = "The given 'description' is not valid in this context";
  public static final String INVALID_CONTEXT_PROGRESS = "The given 'progress' value is not valid in this context";
  public static final String INVALID_CONTEXT_CATEGORY = "The given 'category' value is not valid in this context";
  public static final String INVALID_CONTEXT_FROMWHO = "The given 'fromWho' value is not valid in this context";
  public static final String INVALID_CONTEXT_FULLNAME = "The given 'fullName' value is not valid in this context";
  public static final String INVALID_CONTEXT_FEEDBACKTYPE = "The given 'Feedback type' value is not valid in this context";
  public static final String INVALID_CONTEXT_FEEDBACKSOURCE = "The given 'Feedback source' value is not valid in this context";

  public static final String INVALID_PASTDATE = "The given date is not valid because it is in the past";
  public static final String INVALID_DATEFORMAT = "The given date format is not valid";
  public static final String INVALID_FEEDBACKLIST = "Not all feedback were added due to their incorrect format/status";
  public static final String INVALID_FEEDBACK = "The given feedback is not valid";
  public static final String INVALID_FEEDBACKID = "The given Feedback ID is not valid";
  public static final String INVALID_OBJECTIVELIST = "Not all Objectives were added due to their incorrect format/status";
  public static final String INVALID_OBJECTIVEID = "The given Objective ID is not valid";
  public static final String INVALID_OBJECTIVE = "The given Objective is not valid";
  public static final String INVALID_NOTELIST = "Not all Notes were added due to their incorrect format/status";
  public static final String INVALID_NOTEID = "The given Note ID is not valid";
  public static final String INVALID_NOTE = "The given Note is not valid";

  public static final String NULL_FEEDBACKLIST = "The given list of feedback is null";
  public static final String NULL_FEEDBACK = "The given feedback is null";
  public static final String NULL_OBJECTIVE = "The given Objective is null";
  public static final String NULL_OBJECTIVELIST = "The given list of Objectives is null";
  public static final String NULL_NOTE = "The given Note is null";
  public static final String NULL_NOTELIST = "The given list of Notes is null";

  public static final String INVALID_NULLDEVNEEDSLIST_CONTEXT = "The given list of Development Needs is null";
  public static final String INVALID_DEVNEEDSLIST_CONTEXT = "Not all Development Needs were added due to their incorrect format/status";
  public static final String INVALID_NULLDEVNEED_CONTEXT = "The given Development Need is null";
  public static final String INVALID_DEVNEEDID_CONTEXT = "The given Development Need ID is not valid";
  public static final String INVALID_DEVNEED_CONTEXT = "The given Development Need is not valid";
  public static final String INVALID_GROUPFEEDBACKREQUESTLIST_CONTEXT = "Not all Feedback Requests were added due to their incorrect format/status";
  public static final String INVALID_NULLGROUPFEEDBACKREQUESTLIST_CONTEXT = "The given list of Feedback Requests is null";
  public static final String INVALID_GROUPFEEDBACKREQUESTID_CONTEXT = "The given ID for the Group Feedback Request is not valid";
  public static final String INVALID_NULLGROUPFEEDBACKREQUEST_CONTEXT = "The given Group Feedback Request object is null";
  public static final String INVALID_NULLCOMPETENECYLIST_CONTEXT = "The given list of Competencies is null";
  public static final String INVALID_COMPETENCYLIST_CONTEXT = "Not all Competencies were added due to their incorrect format/status";
  public static final String INVALID_COMPETENCYTID_CONTEXT = "The given Competency ID is not valid";
  public static final String INVALID_COMPETENCY_CONTEXT = "The given Competency is not valid";
  public static final String INVALID_NULLCOMPETENCY_CONTEXT = "The given Competency is null";
  public static final String INVALID_NULLFEEDBACK_OBJECTIVEID_CONTEXT = "The given feedback is null or the provided objective ID is not valid";
  public static final String INVALID_FEEDBACKREQ_RECIPIENT_CONTEXT = "The given 'recipient' value is not valid in this context";
  public static final String INVALID_NULLFEEDBACKREQ_REPLIES_CONTEXT = "The given 'list of repies' is null";
  public static final String INVALID_FEEDBACKNOTFOUND_CONTEXT = "The given 'feedback' does not exist in the user data, and therefore it cannot be removed";
  public static final String INVALID_FEEDBACKREQ_NOTFOUND_CONTEXT = "The given 'feedback request' does not exist in the user data, and therefore it cannot be removed";
  public static final String INVALID_NULLFEEDBACKREQLIST_CONTEXT = "The given list of feedback requests is null";
  public static final String INVALID_FEEDBACKREQLIST_CONTEXT = "The given list of feedback requests is not valid";
  public static final String INVALID_FEEDBACKREQ_CONTEXT = "The given feedback request is not valid";
  public static final String INVALID_FEEDBACKDUPLICATE_CONTEXT = "The Feedback request is a duplicate and cannot be added to this GroupFeedback";
  public static final String INVALID_FEEDBACKREQ_ID_CONTEXT = "The given 'feedback request ID' value is not valid in this context";

  public static final String INVALID_NOTE_TYPE = "A note type must be between 0 and 6";
  public static final String INVALID_NOTE_LINKTYTLE = "The given 'link title' value is not valid in this context";
  public static final String INVALID_NOTE_BODY = "The given 'note body' value is not valid in this context";
  public static final String INVALID_NOTE_FROMWHO = "The name of the note writer is not valid in this context";
  public static final String INVALID_OBJECTIVE_PROPOSEDBY = "The given 'proposed by' valud is not valid in this context";
  public static final String INVALID_OBJECTIVE_PERFORMANCE = "The given 'performance' value is not valid in this context";
  public static final String INVALID_SMTPSERVICE_NOTES = "The notes cannot exceed 1000 characters";
  public static final String INVALID_SMTPSERVICE_TOOMANYADDRESSES = "Too many email addresses, The maximum number allowed is 20";
  public static final String INVALID_NULLREPORTEESLIST = "The given list of reportees is null";
  public static final String INVALID_NULLREPORTEE = "The given 'reportee' is null";
  public static final String INVALID_ID_NOT_FOUND = "No employee matches the given ID";
  public static final String INVALID_OBJECTIVEIDNOTFOND = "No objective ID matches the user data";
  public static final String INVALID_USEREMAIL = "The given email address does not match any valid employee";
  public static final String OBJECTIVE_NOTADDED_ERROR = "The given objective couldn't be added Please try again later";
  public static final String FEEDBACK_NOTADDED_ERROR = "The given feedback couldn't be added Please try again later";
  public static final String NOTE_NOTADDED_ERROR = "The given note couldn't be added Please try again later";
  public static final String DEVELOPMENTNEED_NOTADDED_ERROR = "The given development need couldn't be added Please try again later";
  public static final String GROUPFBREQ_NOTADDED_ERROR = "The given feedback request couldn't be added Please try again later";
  public static final String DUPLICATE_FEEDBACK = "The given feedback couldn't be added because it is a duplicate";
  public static final String INVALID_OBJECTIVE_OR_EMPLOYEEID = "The given Employee ID or Objective ID are invalid";
  public static final String INVALID_NOTE_OR_EMPLOYEEID = "The given Employee ID or Note ID are invalid";
  public static final String INVALID_DEVNEED_OR_EMPLOYEEID = "The given Employee ID or Development Need ID are invalid";
  public static final String INVALID_FBREQ_OR_EMPLOYEEID = "The given Employee ID or Feedback Request ID are invalid";
  public static final String INVALID_COMPETENCY_OR_EMPLOYEEID = "The given Employee ID or Compentecy Title are invalid";
  public static final String INVALID_USERGUID_NOTFOUND = "The given user GUID didn't match any result in the Active Directory";
  public static final String NULL_USER_DATA = "The given user data is invalid";
  public static final String ERROR_LINKING_FBTOUSER = "Something went wrong while adding the feedback to user ";
  public static final String INVALID_EMAILORUSERNAME_AD = "The given 'username/email address' is not valid in this context";
  public static final String NOTFOUND_EMAILORUSERNAME_AD = "The given 'username/email address' didn't match any valid employee: ";
  public static final String INVALID_IDMATCHUSERNAME = "The given ID didn't match any employee: ";
  public static final String INVALID_EMAIL_AD = "The given email address didn't match any employee: ";
  public static final String NOTFOUND_USERNAME_AD = "The given 'username' didn't match any valid employee: ";
  public static final String NOTDELETED_FBREQ = "The given Feedback Request could not be removed from the system";

  // Constants for the Functionalities package

  // AD Details

  // Common
  public static final int AD_PORT = 389;
  public static final String AD_AUTHENTICATION = "simple";

  // Sopra AD Details
  public static final String AD_SOPRA_HOST = "ldap://duns.ldap-ad.dmsi.corp.sopra";
  public static final String AD_SOPRA_USERNAME = "svc_mycareer@emea.msad.sopra";
  public static final String AD_SOPRA_PASSWORD = "N9T$SiPSZ";
  public static final String AD_SOPRA_TREE = "ou=usersemea,DC=emea,DC=msad,DC=sopra";
  public static final String[] AD_SOPRA_ATTRIBUTES = { "sn", "givenName", "company", "sAMAccountName",
      "extensionAttribute7", "objectGUID", "mail", "department", "targetAddress", "memberOf" };
  public static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";
  // Steria AD Details
  public static final String AD_STERIA_HOST = "ldap://one.steria.dom";
  public static final String AD_STERIA_USERNAME = "UK-SVC-CAREER";
  public static final String AD_STERIA_PASSWORD = "3I=AkSiGRr";
  public static final String AD_STERIA_SEARCH_TREE = "DC=one,DC=steria,DC=dom";
  public static final String AD_STERIA_LOGIN_TREE = "OU=Service Accounts,OU=UKCentral,OU=UK,OU=Resources,DC=one,DC=steria,DC=dom";
  public static final String[] AD_STERIA_ATTRIBUTES = { "directReports", "sn", "givenName", "mail", "targetAddress",
      "company", "sAMAccountName", "department", "ou", "SteriaSectorUnit" };

  // Constants for the emailServices package
  // public static final String MAILBOX_ADDRESS="feedback.uk@soprasteria.com";
  // public static final String MAIL_USERNAME="FbackUK";
  // public static final String MAIL_PASSWORD="Auto_Map_$459$";
  // public static final String MAIL_EXCHANGE_URI="https://mailbox.corp.sopra/ews/exchange.asmx";
  // public static final String MAILBOX_ADDRESS = "feedback.dev.uk@soprasteria.com";
  // public static final String MAIL_USERNAME = "feedback.dev.uk";
  // public static final String MAIL_PASSWORD = "=9UzlDg^3N";
  // public static final String MAIL_EXCHANGE_URI = "https://mailbox.corp.sopra/ews/exchange.asmx";
  public static final String MAILBOX_ADDRESS = "ridhwan.nacef@soprasteria.com";
  public static final String MAIL_USERNAME = "ridhwan.nacef@soprasteria.com";
  public static final String MAIL_PASSWORD = "Steria123";
  public static final String MAIL_EXCHANGE_URI = "https://outlook.office365.com/ews/exchange.asmx";

  public static final String MAIL_ENCODING_CHARSET = "UTF-8";
  public static final long MAIL_REFRESH_TIME = (1 * 60 * 1000); // 1 minute

  // Constants for the CORS header

  // Domain Dev Server
  // public static final String CORS_IP_DOMAIN_DEV="172.25.112.189";
  public static final String CORS_DOMAIN_DEV = "http://ldunsmycareerdev01.duns.uk.sopra";
  // Domain UAT Server
  // public static final String CORS_IP_DOMAIN_UAT="172.25.112.148";
  public static final String CORS_DOMAIN_UAT = "http://mycareer-uat.duns.uk.sopra";

  // Domain Live Server
  // public static final String CORS_IP_DOMAIN_LIVE="172.25.113.42";
  public static final String CORS_DOMAIN_LIVE = "http://mycareer.uk.corp.sopra";

  // Server names
  public static final String DEV_SERVER_NAME = "ldunsmycareerdev01";
  public static final String UAT_SERVER_NAME = "ldunsmycareeruat01";
  public static final String LIVE_SERVER_NAME = "ldunsmycareer01";

  // Constants for the SPNEGO-KERBEROS setup

  // Arrays Containing all Competences including both Names and Descriptions
  public static final String[] COMPETENCY_NAMES = new String[] { "Accountability", "Business Awareness",
      "Effective Communication", "Future Orientation", "Innovation and Change", "Leadership", "Service Excellence",
      "Teamwork" };
  public static final String[] COMPETENCY_DESCRIPTIONS = new String[] {
      "Shows drive and commitment to achieve objectives. Is willing to act decisively and strives to find ways of overcoming obstacles. Takes ownership of issues and empowers team members by giving them an appropriate level of responsibility and autonomy. Does not give up and can be counted on to deliver. Is action oriented.",
      "Is able to recognise opportunities to leverage Sopra Steria's capabilities to provide practical and profitable solutions to client's needs. Understands the key commercial issues that affect profitability and growth. Builds relationships with clients and seeks to understand their needs and priorities. Uses this knowledge to provide flexible and reliable solutions to meet and exceed clients' expectations and deliver value to Sopra Steria. Acts as a company Ambassador.",
      "Expresses ideas clearly, persuasively and with impact. Listens to others and convinces them to accept their ideas. Is open and honest with colleagues and clients. Able to write or present clearly and succinctly in a variety of communication settings and audiences. Gets messages across that have the desired effect. Is timely with provision of information.",
      "Takes account of a wide range of long-term changes and trends in technology, the market-place and in the business and plans ahead accordingly. Has a clear view of where they want to get to in the medium and longer term. Understands competition. Anticipates consequences and trends.",
      "Shows flexibility and the desire to acquire new knowledge and ideas. Accepts and supports the need for change and looks for new ways of solving problems. Demonstrates the ability to lead and foster change within the organisation. Can act differently depending upon the situation and can handle uncertainty. Is committed to continuous improvement. Comes up with practical steps to implement own or others ideas.",
      "Provides clear direction and motivates and inspires others to succeed. Seeks to develop the skills and confidence of others and to recognise and develop talent. Acts as a role model and provides appropriate induction, feedback and coaching to team members. Is willing to confront and challenge poor performance and encourages team members to contribute ideas. Brings the best out of others.",
      "Builds Sopra Steria's reputation in the market by setting high standards of service and delivery. Knows what to measure and how to measure it and looks for opportunities for synergies. Is committed to Service Excellence.",
      "Works cooperatively with colleagues and considers their needs and the impact of decisions on them. Seeks to build relationships across the organisation and to work for the overall good of the business. Able to find common ground and gain trust. Encourages collaboration." };

  public static int getCompetencyIDGivenTitle(String title)
  {
    for (int i = 0; i < COMPETENCY_NAMES.length; i++)
    {
      if (COMPETENCY_NAMES[i].equalsIgnoreCase(title)) return i;
    }
    return -1;
  }

  public static String getCompetencyNameGivenID(int id)
  {
    if (id > -1 && id < COMPETENCY_NAMES.length) return COMPETENCY_NAMES[id];
    return null;
  }

  public static String getCompetencyDescriptionGivenID(int id)
  {
    if (id > -1 && id < COMPETENCY_DESCRIPTIONS.length) return COMPETENCY_DESCRIPTIONS[id];
    return null;
  }
}
