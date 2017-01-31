package services;

import static dataStructure.Constants.UK_TIMEZONE;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import services.validate.Validate;

public class Helper {

	
	/**
	 * Generate a feedbackRequestID using an employeeID.
	 * Format will be dddddd_ddddddddddddddddd. 
	 * Where 'd' is a number.
	 * 
	 * @param id the employee id
	 * @return
	 */
	public static String generateFeedbackRequestID(long id) {
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
	public static Set<String> stringEmailsToHashSet(String emailsString) throws InvalidAttributeValueException {
		Validate.areStringsEmptyorNull(emailsString);
		
		Set<String> emailSet = new HashSet<>();
		String[] emailArray = emailsString.split(",");
		
		for(String email : emailArray){
			if(email.isEmpty() || !Validate.isValidEmailSyntax(email.trim())){
				throw new InvalidAttributeValueException("One or more of the emails are invalid");
			}
			emailSet.add(email.trim());
		}	
		return emailSet;
	}

	
	/**
	 * Returns a feedbackRequestID found from the given text. 
	 * Format of id should be dddddd_ddddddddddddddddd. 
	 * Where 'd' is a number.
	 * 
	 * @param text the text that contains the requestID
	 * @return The String ID if found, empty string otherwise.
	 * @throws InvalidAttributeValueException 
	 */
	public static String findFeedbackRequestIDFromString(String text) throws InvalidAttributeValueException {
		Validate.areStringsEmptyorNull(text);
		String searchStr = "feedback_request: ";
		
		int start = text.toLowerCase().indexOf(searchStr);
		
		if(start == -1) return "";
		
		int end = (start + searchStr.length());
		String feedbackRequestID = text.substring(end, end+24);
		
		return feedbackRequestID;
	}

	public static long getEmployeeIDFromRequestID(String feedbackRequestID) throws InvalidAttributeValueException {
		Validate.areStringsEmptyorNull(feedbackRequestID);
		if(!Validate.isValidFeedbackRequestID(feedbackRequestID))
			throw new InvalidAttributeValueException("ID is invalid.");
		
		String employeeID = feedbackRequestID.substring(0, 6);
		return Long.parseLong(employeeID);
	}
	
}
