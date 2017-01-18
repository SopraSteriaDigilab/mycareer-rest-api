package services;

import static org.junit.Assert.assertTrue;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import services.validate.ValidateAppController;

public class ValidateAppControllerTest {
	
	private long validID = 111111;
	private long invalidID = 0;
	private String validString = "valid";
	private String validEmail1 = "abc@def.com";
	private String validEmail2 = "def@def.com";
	
	@Test
	public void testIsValidCreateFeedbackRequestWithValid() throws InvalidAttributeValueException{
		StringBuilder validEmailList = new StringBuilder(validEmail1 + "," + validEmail2);
		
		assertTrue("should return 'true'", ValidateAppController.isValidCreateFeedbackRequest(validID, validEmail1, validString)); // all valid		
		assertTrue("should return 'true'", ValidateAppController.isValidCreateFeedbackRequest(validID, validEmailList.toString(), validString)); // all valid multiple emails
	}
	
    @Test(expected=InvalidAttributeValueException.class)
    public void testIsValidCreateFeedbackRequestWithInvalidID() throws InvalidAttributeValueException {
    	ValidateAppController.isValidCreateFeedbackRequest(invalidID, validEmail1, validString); // invaldID
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testIsValidCreateFeedbackRequestWithTooManyEmails() throws InvalidAttributeValueException  {
    	StringBuilder invalidToFields = new StringBuilder(validEmail1);
    	for(int i = 0; i < 21; i++)
			invalidToFields.append(","+i+validEmail1);
		ValidateAppController.isValidCreateFeedbackRequest(validID, invalidToFields.toString(), validString); // invalidToFields
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testIsValidCreateFeedbackRequestWithInvalidNotes() throws InvalidAttributeValueException {
    	StringBuilder invalidNotes = new StringBuilder(validString);
    	for(int i = 0; i < 201; i++)
    		invalidNotes.append(validString);
		ValidateAppController.isValidCreateFeedbackRequest(validID, validEmail1, invalidNotes.toString()); // invalidNotes
    }

}
