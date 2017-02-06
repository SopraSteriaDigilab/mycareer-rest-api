package utils;

import static dataStructure.Constants.UK_TIMEZONE;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import services.validate.Validate;

/**
 * Helper class that provides various helper methods throughout the application.
 */
public class Utils
{

  /**
   * Generate a feedbackRequestID using an employeeID. Format will be dddddd_ddddddddddddddddd. Where 'd' is a number.
   * 
   * @param id the employee id
   * @return
   */
  public static String generateFeedbackRequestID(long id)
  {
    LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(UK_TIMEZONE));
    String stringDate = localDateTime.toString().replace("-", "").replace("T", "").replace(":", "").replace(".", "");
    String generatedId = id + "_" + stringDate;
    return generatedId;
  }

  /**
   * Converts a string of comma separated emails into a set.
   * 
   * @param emailsString
   * @return
   * @throws InvalidAttributeValueException
   */
  public static Set<String> stringEmailsToHashSet(String emailsString) throws InvalidAttributeValueException
  {
    Validate.areStringsEmptyorNull(emailsString);

    Set<String> emailSet = new HashSet<>();
    String[] emailArray = emailsString.split(",");

    for (String email : emailArray)
    {
      if (email.isEmpty() || !Validate.isValidEmailSyntax(email.trim()))
      {
        throw new InvalidAttributeValueException("One or more of the emails are invalid");
      }
      emailSet.add(email.trim());
    }
    return emailSet;
  }

  /**
   * Finds a feedbackRequestID found from the given email body, looks for 'feedback_request: {id}'. Format of id should be
   * dddddd_ddddddddddddddddd. Where 'd' is a number.
   * 
   * @param text the text that contains the requestID
   * @return The String ID if found, empty string otherwise.
   * @throws InvalidAttributeValueException
   */
  public static String getFeedbackRequestIDFromEmailBody(String body) throws InvalidAttributeValueException
  {
    Validate.areStringsEmptyorNull(body);
    String searchStr = "feedback_request: ";

    int start = body.toLowerCase().indexOf(searchStr);

    if (start == -1) return "";

    start += searchStr.length();
    String feedbackRequestID = body.substring(start, start + 24);
    
    if(!Validate.isValidFeedbackRequestID(feedbackRequestID))
      return "";

    return feedbackRequestID;
  }
  
  /**
   * Finds an EmployeeID from a feedback request subject.
   *
   * @param subject
   * @return The employeeID found, empty string otherwise.
   * @throws InvalidAttributeValueException 
   */
  public static long getEmployeeIDFromFeedbackRequestSubject(String subject) throws InvalidAttributeValueException
  {
    Validate.areStringsEmptyorNull(subject);
    String searchStr = "- ";
    
    int start = subject.toLowerCase().indexOf(searchStr);
    
    if (start == -1) return -1;
    
    start += searchStr.length();
    long employeeID = Long.parseLong(subject.substring(start, start+6));
    
    return employeeID;
  }


  /**
   * 
   * Gets an exployeeID from a feedbackRequestID.
   *
   * @param feedbackRequestID
   * @return The employeeID if the feedbackRequestID is in the valid format
   * @throws InvalidAttributeValueException if the feedbackRequestID is invalid
   * @see {@linkplain services.validate.Validate #isValidFeedbackRequestID(String) isValidFeedbackRequest(String)}
   */
  public static long getEmployeeIDFromRequestID(String feedbackRequestID) throws InvalidAttributeValueException
  {
    Validate.areStringsEmptyorNull(feedbackRequestID);
    if (!Validate.isValidFeedbackRequestID(feedbackRequestID))
      throw new InvalidAttributeValueException("ID is invalid.");

    String employeeID = feedbackRequestID.substring(0, 6);
    return Long.parseLong(employeeID);
  }

}
