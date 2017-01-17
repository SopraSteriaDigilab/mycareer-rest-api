package services;

import static org.junit.Assert.assertFalse;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import services.validate.Validate;


public class ValidateTest {
	
	private String validString = "valid";
	private String emptyString = "";
	private String nullString = null;
	
	//	Testing areStringsEmpytOrNull Method
    @Test
    public void testAreStringsEmptyorNullWithValid() throws InvalidAttributeValueException {
    	assertFalse("should return 'false'", Validate.areStringsEmptyorNull(validString)); // one valid
    	assertFalse("should return 'false'", Validate.areStringsEmptyorNull(validString, validString)); // multiple valid
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithEmpty() throws InvalidAttributeValueException {
	     Validate.areStringsEmptyorNull(emptyString, emptyString); // both empty
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithNull() throws InvalidAttributeValueException {
    	 Validate.areStringsEmptyorNull(nullString, nullString); // both null
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithValidAndEmpty() throws InvalidAttributeValueException {
        Validate.areStringsEmptyorNull(validString, emptyString); // one valid, one empty
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithValidAndNull() throws InvalidAttributeValueException {
        Validate.areStringsEmptyorNull(validString, nullString); // one valid, one null
    }
    
    @Test(expected=InvalidAttributeValueException.class)
    public void testAreStringsEmptyorNullWithNoInput() throws InvalidAttributeValueException {
    	Validate.areStringsEmptyorNull(); // no input
    }
    
   



    

}
