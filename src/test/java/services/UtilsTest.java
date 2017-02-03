package services;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import services.validate.Validate;
import utils.Utils;

/**
 * Unit tests for the Utils class.
 */
public class UtilsTest
{

  /** TYPE Property|Constant - Represents|Indicates... */
  private final long VALID_EMPLOYEE_ID = 111111;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String EMPTY_STRING = "";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String NULL_STRING = null;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String REQUEST_FEEDBACK_ID = "111111_11111111111111111";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_REQUEST_FEEDBACK_ID = "111_111";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String REQUEST_FEEDBACK_ID_TEXT = "Random text. Feedback_Request: 111111_11111111111111111";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String REQUEST_FEEDBACK_ID_TEXT_2 = "Random text.";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String EMAIL_STRING_1 = "a@test.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String EMAIL_STRING_2 = "b@test.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String EMAIL_STRING_3 = "c@test.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String EMAILS_STRING = "a@test.com, b@test.com, c@test.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_EMAILS_STRING = "a@test.com, , c@test.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_EMAILS_STRING_2 = "a@test.com, asdsadasd, c@test.com";

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGenerateFeedbackRequestIDShouldWorkAsExpected() throws InvalidAttributeValueException
  {
    assertTrue(Validate.isValidFeedbackRequestID(Utils.generateFeedbackRequestID(VALID_EMPLOYEE_ID)));
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testStringEmailsToHashSetShouldWorkAsExpected() throws InvalidAttributeValueException
  {
    Set<String> expected1 = new HashSet<>(Arrays.asList(EMAIL_STRING_1, EMAIL_STRING_2, EMAIL_STRING_3));
    Set<String> expected2 = new HashSet<>(Arrays.asList(EMAIL_STRING_1));

    assertEquals(expected1, Utils.stringEmailsToHashSet(EMAILS_STRING));
    assertEquals(expected2, Utils.stringEmailsToHashSet(EMAIL_STRING_1));
    assertFalse(expected1.equals(Utils.stringEmailsToHashSet(EMAIL_STRING_3)));
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testStringEmailsToHashSetWithEmptyEmail() throws InvalidAttributeValueException
  {
    Utils.stringEmailsToHashSet(INVALID_EMAILS_STRING);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testStringEmailsToHashSetWithInvalidEmail() throws InvalidAttributeValueException
  {
    Utils.stringEmailsToHashSet(INVALID_EMAILS_STRING_2);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testStringEmailsToHashSetWithEmpty() throws InvalidAttributeValueException
  {
    Utils.stringEmailsToHashSet(EMPTY_STRING);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testStringEmailsToHashSetWithNull() throws InvalidAttributeValueException
  {
    Utils.stringEmailsToHashSet(NULL_STRING);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testFindFeedbackRequestIDFromStringShouldWorkAsExpected() throws InvalidAttributeValueException
  {
    assertEquals(REQUEST_FEEDBACK_ID, Utils.findFeedbackRequestIDFromString(REQUEST_FEEDBACK_ID_TEXT));
    assertEquals("", Utils.findFeedbackRequestIDFromString(REQUEST_FEEDBACK_ID_TEXT_2));
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testFindFeedbackRequestIDFromStringWithEmpty() throws InvalidAttributeValueException
  {
    Utils.findFeedbackRequestIDFromString(EMPTY_STRING);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testFindFeedbackRequestIDFromStringWithNull() throws InvalidAttributeValueException
  {
    Utils.findFeedbackRequestIDFromString(NULL_STRING);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetEmployeeIDFromRequestID() throws InvalidAttributeValueException
  {
    assertEquals(VALID_EMPLOYEE_ID, Utils.getEmployeeIDFromRequestID(REQUEST_FEEDBACK_ID));
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testGetEmployeeIDFromRequestIDWithInvalidID() throws InvalidAttributeValueException
  {
    Utils.getEmployeeIDFromRequestID(INVALID_REQUEST_FEEDBACK_ID);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testGetEmployeeIDFromRequestIDWithEmpty() throws InvalidAttributeValueException
  {
    Utils.getEmployeeIDFromRequestID(EMPTY_STRING);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testGetEmployeeIDFromRequestIDWithNull() throws InvalidAttributeValueException
  {
    Utils.getEmployeeIDFromRequestID(NULL_STRING);
  }

}
