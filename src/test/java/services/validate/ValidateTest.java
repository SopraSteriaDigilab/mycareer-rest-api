package services.validate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import services.validate.Validate;


public class ValidateTest {
	
	private static final String VALID_STRING = "valid";
	private static final String EMPTY_STRING = "";
	private static final String NULL_STRING = null;
	private static final String VALID_FEEDBACK_REQUEST_ID = "111111_11111111111111111";
	private static final String INVALID_FEEDBACK_REQUEST_ID = "1111";
	private static final String INVALID_FEEDBACK_REQUEST_ID_2 = "aaaaaa_aaaaaaaaaaaaaaaaa";
	
    @Test
    public void testAreStringsEmptyorNullWithValid() throws InvalidAttributeValueException {
    	assertFalse("should return 'false'", Validate.areStringsEmptyorNull(VALID_STRING)); // one valid
    	assertFalse("should return 'false'", Validate.areStringsEmptyorNull(VALID_STRING, VALID_STRING)); // multiple valid
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithEmpty() throws InvalidAttributeValueException {
	     Validate.areStringsEmptyorNull(EMPTY_STRING, EMPTY_STRING); // both empty
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithNull() throws InvalidAttributeValueException {
    	 Validate.areStringsEmptyorNull(NULL_STRING, NULL_STRING); // both null
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithValidAndEmpty() throws InvalidAttributeValueException {
        Validate.areStringsEmptyorNull(VALID_STRING, EMPTY_STRING); // one valid, one empty
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithValidAndNull() throws InvalidAttributeValueException {
        Validate.areStringsEmptyorNull(VALID_STRING, NULL_STRING); // one valid, one null
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithNoInput() throws InvalidAttributeValueException {
    	Validate.areStringsEmptyorNull(); // no input
    }
    
    @Test
    public void testIsNullWithValid() throws InvalidAttributeValueException {
    	assertFalse("should return 'false'", Validate.isNull(VALID_STRING, VALID_STRING)); // valid
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testIsNullWithNulls() throws InvalidAttributeValueException {
    	Validate.isNull(NULL_STRING, NULL_STRING); // nulls
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testIsNullWithNotInput() throws InvalidAttributeValueException {
    	Validate.isNull(); // no input
    }
    
    @Test
    public void testIsValidFeedbackRequestID() throws InvalidAttributeValueException {
    	assertTrue("should return 'true'", Validate.isValidFeedbackRequestID(VALID_FEEDBACK_REQUEST_ID));
    	assertFalse("should return 'true'", Validate.isValidFeedbackRequestID(INVALID_FEEDBACK_REQUEST_ID));
    	assertFalse("should return 'true'", Validate.isValidFeedbackRequestID(INVALID_FEEDBACK_REQUEST_ID_2));
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testIsValidFeedbackRequestIDWithNull() throws InvalidAttributeValueException {
    	Validate.isValidFeedbackRequestID(NULL_STRING);
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testIsValidFeedbackRequestIDWithEMPTY() throws InvalidAttributeValueException {
    	Validate.isValidFeedbackRequestID(EMPTY_STRING);
    }
    
	
    
       

}
