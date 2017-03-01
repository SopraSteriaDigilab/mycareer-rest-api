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
  private final String VALID_NAME = "Alexandre Brard";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_EMAIL_ADDRESS = "alexandre.brard@soprasteria.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_EMAIL_ADDRESS = "alexandre.brard$soprasteria.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_EMPLOYEE_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_EMPLOYEE_ID = -675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_DESCRIPTION = "a valid description";
  
  @InjectMocks
  private Feedback fbk, fbk2;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   fbk = new Feedback(VALID_EMPLOYEE_ID, VALID_EMAIL_ADDRESS, VALID_DESCRIPTION);
   fbk2 = new Feedback(VALID_EMPLOYEE_ID, VALID_EMAIL_ADDRESS, VALID_NAME, VALID_DESCRIPTION);
  }
  
  
  /**
   * Unit test for the getID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetID() throws InvalidAttributeValueException
  {  
    fbk.setId(VALID_EMPLOYEE_ID);
    assertEquals(fbk.getId(), VALID_EMPLOYEE_ID);
  }
  
  /**
   * Unit test for the getProviderEmail method.
   * @throws InvalidAttributeValueException 
   * 
   */
  @Test
  public void testGetProviderEmail() throws InvalidAttributeValueException
  {  
    fbk.setProviderEmail(VALID_EMAIL_ADDRESS);
    assertEquals(fbk.getProviderEmail(),VALID_EMAIL_ADDRESS);
  }
  
  /**
   * Unit test for the getProviderName method.
   * @throws InvalidAttributeValueException 
   * 
   */
  @Test
  public void testGetProviderName() throws InvalidAttributeValueException
  {  
    fbk.setProviderName(VALID_NAME);
    assertEquals(fbk.getProviderName(),VALID_NAME);
  }
  
  /**
   * Unit test for the setProviderEmail method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProviderEmailWithInvalidEmailAddress() throws InvalidAttributeValueException
  {
    fbk.setProviderEmail(INVALID_EMAIL_ADDRESS);
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
    assertEquals(fbk.getProviderEmail(),VALID_EMAIL_ADDRESS);
  }
  
  /**
   * Unit test for the getProviderName method.
   * @throws InvalidAttributeValueException 
   * 
   */
  @Test
  public void testGetFeedbackDescription() throws InvalidAttributeValueException
  {  
    fbk.setFeedbackDescription(VALID_DESCRIPTION);
    assertEquals(fbk.getFeedbackDescription(),VALID_DESCRIPTION);
  }  
}

