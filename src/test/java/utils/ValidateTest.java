package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import utils.Validate;

/**
 * Unit tests for the Validate class.
 */
public class ValidateTest
{
  /** String Constant - Represents a valid String. */
  private final String VALID_STRING = "valid";

  /** String Constant - Represents an empty String. */
  private final String EMPTY_STRING = "";

  /** String Constant - Represents a null String. */
  private final String NULL_STRING = null;

  /** String Constant - Represents a valid feedback Request feedback Request ID. */
  private final String VALID_FEEDBACK_REQUEST_ID = "111111_11111111111111111";

  /** String Constant - Represents an invalid feedback Request ID. */
  private final String INVALID_FEEDBACK_REQUEST_ID = "1111";

  /** Long Constant - Represents an invalid feedback Request ID. */
  private final String INVALID_FEEDBACK_REQUEST_ID_2 = "aaaaaa_aaaaaaaaaaaaaaaaa";

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testAreStringsEmptyorNullShoudWorkAsExpected() throws InvalidAttributeValueException
  {
    assertTrue(Validate.stringNotEmptyNotNull(VALID_STRING)); // one valid
    assertFalse(Validate.stringsNotEmptyNotNullOrThrow(VALID_STRING, VALID_STRING)); // multiple valid
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testAreStringsEmptyorNullWithEmpty() throws InvalidAttributeValueException
  {
    Validate.stringsNotEmptyNotNullOrThrow(VALID_STRING, EMPTY_STRING); // both empty
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testAreStringsEmptyorNullWithNull() throws InvalidAttributeValueException
  {
    Validate.stringsNotEmptyNotNullOrThrow(NULL_STRING, NULL_STRING); // both null
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testAreStringsEmptyorNullWithValidAndEmpty() throws InvalidAttributeValueException
  {
    Validate.stringsNotEmptyNotNullOrThrow(VALID_STRING, EMPTY_STRING); // one valid, one empty
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testAreStringsEmptyorNullWithValidAndNull() throws InvalidAttributeValueException
  {
    Validate.stringsNotEmptyNotNullOrThrow(VALID_STRING, NULL_STRING); // one valid, one null
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testAreStringsEmptyorNullWithNoInput() throws InvalidAttributeValueException
  {
    Validate.stringsNotEmptyNotNullOrThrow(); // no input
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testIsNullShouldWorkAsExpected() throws InvalidAttributeValueException
  {
    assertFalse("should return 'false'", Validate.throwIfNull(VALID_STRING, VALID_STRING)); // valid
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testIsNullWithNulls() throws InvalidAttributeValueException
  {
    Validate.throwIfNull(NULL_STRING, NULL_STRING); // nulls
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testIsNullWithNotInput() throws InvalidAttributeValueException
  {
    Validate.throwIfNull(); // no input
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testIsValidFeedbackRequestID() throws InvalidAttributeValueException
  {
    assertTrue("should return 'true'", Validate.isValidFeedbackRequestID(VALID_FEEDBACK_REQUEST_ID));
    assertFalse("should return 'true'", Validate.isValidFeedbackRequestID(INVALID_FEEDBACK_REQUEST_ID));
    assertFalse("should return 'true'", Validate.isValidFeedbackRequestID(INVALID_FEEDBACK_REQUEST_ID_2));
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testIsValidFeedbackRequestIDWithNull() throws InvalidAttributeValueException
  {
    Validate.isValidFeedbackRequestID(NULL_STRING);
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testIsValidFeedbackRequestIDWithEMPTY() throws InvalidAttributeValueException
  {
    Validate.isValidFeedbackRequestID(EMPTY_STRING);
  }

}
