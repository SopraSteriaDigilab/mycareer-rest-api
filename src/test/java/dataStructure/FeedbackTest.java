//package dataStructure;
//
//import static org.mockito.MockitoAnnotations.*;
//import static dataStructure.Constants.UK_TIMEZONE;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//
//import javax.management.InvalidAttributeValueException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//
//import utils.Validate;
//
//public class FeedbackTest
//{
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_NAME = "Alexandre Brard";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_EMAIL_ADDRESS = "alexandre.brard@soprasteria.com";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_EMAIL_ADDRESS = "alexandre.brard$soprasteria.com";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int VALID_EMPLOYEE_ID = 675590;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int INVALID_EMPLOYEE_ID = -675590;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_DESCRIPTION = "a valid description";
//  
//  @InjectMocks
//  private Feedback unitUnderTest, unitUnderTest2;
//  
//  /**
//   * Setup method that runs once before each test method.
//   * 
//   */
//  @Before
//  public void setup()
//  {
//   initMocks(this);
//   unitUnderTest = new Feedback(VALID_EMPLOYEE_ID, VALID_EMAIL_ADDRESS, VALID_DESCRIPTION);
//   unitUnderTest2 = new Feedback(VALID_EMPLOYEE_ID, VALID_EMAIL_ADDRESS, VALID_NAME, VALID_DESCRIPTION);
//  }
//  
//  
//  /**
//   * Unit test for the getID method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetID() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setId(VALID_EMPLOYEE_ID);
//    assertEquals(unitUnderTest.getId(), VALID_EMPLOYEE_ID);
//  }
//  
//  /**
//   * Unit test for the getProviderEmail method.
//   * @throws InvalidAttributeValueException 
//   * 
//   */
//  @Test
//  public void testGetProviderEmail() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setProviderEmail(VALID_EMAIL_ADDRESS);
//    assertEquals(unitUnderTest.getProviderEmail(),VALID_EMAIL_ADDRESS);
//  }
//  
//  /**
//   * Unit test for the getProviderName method.
//   * @throws InvalidAttributeValueException 
//   * 
//   */
//  @Test
//  public void testGetProviderName() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setProviderName(VALID_NAME);
//    assertEquals(unitUnderTest.getProviderName(),VALID_NAME);
//  }
//  
//  /**
//   * Unit test for the setProviderEmail method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetProviderEmailWithInvalidEmailAddress() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setProviderEmail(INVALID_EMAIL_ADDRESS);
//  }
//  
//  /**
//   * Unit test for the setProviderEmail method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetProviderEmailWithValidEmailAddress() throws InvalidAttributeValueException
//  { 
//    unitUnderTest.setProviderEmail(VALID_EMAIL_ADDRESS);
//    assertEquals(unitUnderTest.getProviderEmail(),VALID_EMAIL_ADDRESS);
//  }
//  
//  /**
//   * Unit test for the getProviderName method.
//   * @throws InvalidAttributeValueException 
//   * 
//   */
//  @Test
//  public void testGetFeedbackDescription() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setFeedbackDescription(VALID_DESCRIPTION);
//    assertEquals(unitUnderTest.getFeedbackDescription(),VALID_DESCRIPTION);
//  }
//  
//  /**
//   * Unit test for the getTimestamp method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetTimestamp() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.getTimestamp(),LocalDateTime.now(UK_TIMEZONE).toString());
//  }
//}
//
