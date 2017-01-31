package services;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import services.validate.Validate;



public class HelperTest {
	
	private static final long EMPLOYEE_ID = 111111;
	private static final String EMPTY_STRING = "";
	private static final String NULL_STRING = null;
	private static final String REQUEST_FEEDBACK_ID = "111111_11111111111111111";
	private static final String INVALID_REQUEST_FEEDBACK_ID = "111_111";
	private static final String REQUEST_FEEDBACK_ID_TEXT = "Random text. Feedback_Request: 111111_11111111111111111";
	private static final String REQUEST_FEEDBACK_ID_TEXT_2 = "Random text.";
	private static final String EMAIL_STRING_1 = "a@test.com";
	private static final String EMAIL_STRING_2 = "b@test.com";
	private static final String EMAIL_STRING_3 = "c@test.com";
	private static final String EMAILS_STRING = "a@test.com, b@test.com, c@test.com";
	private static final String INVALID_EMAILS_STRING = "a@test.com, , c@test.com";
	private static final String INVALID_EMAILS_STRING_2 = "a@test.com, asdsadasd, c@test.com";
	
    @Test
    public void testGenerateFeedbackRequestID() throws InvalidAttributeValueException {
    	assertTrue(Validate.isValidFeedbackRequestID(Helper.generateFeedbackRequestID(EMPLOYEE_ID)));
    }
    
    @Test
    public void testStringEmailsToHashSet() throws InvalidAttributeValueException{
    	Set<String> expected1 = new HashSet<>(Arrays.asList(EMAIL_STRING_1, EMAIL_STRING_2, EMAIL_STRING_3));
    	Set<String> expected2 = new HashSet<>(Arrays.asList(EMAIL_STRING_1));
    	
    	assertEquals(expected1, Helper.stringEmailsToHashSet(EMAILS_STRING));
    	assertEquals(expected2, Helper.stringEmailsToHashSet(EMAIL_STRING_1));
    	assertFalse(expected1.equals(Helper.stringEmailsToHashSet(EMAIL_STRING_3)));
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testStringEmailsToHashSetWithEmptyEmail() throws InvalidAttributeValueException{
    	Helper.stringEmailsToHashSet(INVALID_EMAILS_STRING);
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testStringEmailsToHashSetWithInvalidEmail() throws InvalidAttributeValueException{
    	Helper.stringEmailsToHashSet(INVALID_EMAILS_STRING_2);
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testStringEmailsToHashSetWithEmpty() throws InvalidAttributeValueException{
    	Helper.stringEmailsToHashSet(EMPTY_STRING);
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testStringEmailsToHashSetWithNull() throws InvalidAttributeValueException{
    	Helper.stringEmailsToHashSet(NULL_STRING);
    }
	
    @Test
    public void testFindFeedbackRequestIDFromString() throws InvalidAttributeValueException {
    	assertEquals(REQUEST_FEEDBACK_ID, Helper.findFeedbackRequestIDFromString(REQUEST_FEEDBACK_ID_TEXT));
    	assertEquals("", Helper.findFeedbackRequestIDFromString(REQUEST_FEEDBACK_ID_TEXT_2));
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testFindFeedbackRequestIDFromStringWithEmpty() throws InvalidAttributeValueException {
    	Helper.findFeedbackRequestIDFromString(EMPTY_STRING);
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testFindFeedbackRequestIDFromStringWithNull() throws InvalidAttributeValueException {
    	Helper.findFeedbackRequestIDFromString(NULL_STRING);
    }
    
    @Test
    public void testGetEmployeeIDFromRequestID() throws InvalidAttributeValueException {
    	assertEquals(EMPLOYEE_ID, Helper.getEmployeeIDFromRequestID(REQUEST_FEEDBACK_ID));	
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testGetEmployeeIDFromRequestIDWithInvalidID() throws InvalidAttributeValueException {
    	Helper.getEmployeeIDFromRequestID(INVALID_REQUEST_FEEDBACK_ID);
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testGetEmployeeIDFromRequestIDWithEmpty() throws InvalidAttributeValueException {
    	Helper.getEmployeeIDFromRequestID(EMPTY_STRING);
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testGetEmployeeIDFromRequestIDWithNull() throws InvalidAttributeValueException {
    	Helper.getEmployeeIDFromRequestID(NULL_STRING);
    }

}
