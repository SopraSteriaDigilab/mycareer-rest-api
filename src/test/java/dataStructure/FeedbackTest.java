package dataStructure;

import static org.mockito.MockitoAnnotations.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import services.validate.Validate;

public class FeedbackTest
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_EMAIL_ADDRESS = "alexandre.brard@soprasteria.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_EMPLOYEE_ID = 675590;
  
  @InjectMocks
  private Feedback fbk;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   fbk = new Feedback();
  }
  
  /**
   * Unit test for the setProviderEmail method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProviderEmailWithInvalidEmailAddress() throws InvalidAttributeValueException
  {
    when(Validate.isValidEmailSyntax(anyString())).thenReturn(false);
  }
  
  /**
   * Unit test for the setProviderEmail method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetProviderEmailWithValidEmailAddress() throws InvalidAttributeValueException
  { 
    fbk.setProviderEmail(VALID_EMAIL_ADDRESS);
    when(Validate.isValidEmailSyntax(anyString())).thenReturn(true);
    assertEquals(fbk.getProviderEmail(),VALID_EMAIL_ADDRESS);
  }
}

