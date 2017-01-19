package services.validate;


import static services.validate.Validate.*;

import javax.management.InvalidAttributeValueException;

import services.Helper;



public class ValidateAppController {
	
	public static boolean isValidCreateFeedbackRequest(long employeeID, String toFields, String notes) throws InvalidAttributeValueException{
		
		if(employeeID < 1)
			throw new InvalidAttributeValueException("The given employeeID is invalid, please try again later.");
		
		areStringsEmptyorNull(toFields);
		
		if(notes.length() > 1000)
			throw new InvalidAttributeValueException("Notes must be under 1000 characters.");
		
		if(Helper.stringEmailsToHashSet(toFields).size() > 20)
			throw new InvalidAttributeValueException("There must be less than 20 email addresses.");
		
		return true;
	}


}
