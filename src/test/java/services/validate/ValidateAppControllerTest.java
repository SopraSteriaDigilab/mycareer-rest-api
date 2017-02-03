package services.validate;

import static org.junit.Assert.assertTrue;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import services.validate.ValidateAppController;

/**
 * Unit tests for the ValidateAppController class.
 */
public class ValidateAppControllerTest
{

  /** long Constant - Represents Valid ID */
  private final long validID = 111111;

  /** long Constant - Represents invalid ID... */
  private long invalidID = 0;

  /** String Constant - Represents valid String. */
  private String validString = "valid";

  /** String Constant - Represents valid email. */
  private String validEmail1 = "abc@def.com";

  /** String Constant - Represents valid email */
  private String validEmail2 = "def@def.com";

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testIsValidCreateFeedbackRequestWithValid() throws InvalidAttributeValueException
  {
    StringBuilder validEmailList = new StringBuilder(validEmail1 + "," + validEmail2);
    
    // all valid
    assertTrue("should return 'true'",
        ValidateAppController.isValidCreateFeedbackRequest(validID, validEmail1, validString));

    // all valid multiple emails
    assertTrue("should return 'true'",
        ValidateAppController.isValidCreateFeedbackRequest(validID, validEmailList.toString(), validString));
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testIsValidCreateFeedbackRequestWithInvalidID() throws InvalidAttributeValueException
  {
    ValidateAppController.isValidCreateFeedbackRequest(invalidID, validEmail1, validString); // invaldID
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testIsValidCreateFeedbackRequestWithTooManyEmails() throws InvalidAttributeValueException
  {
    StringBuilder invalidToFields = new StringBuilder(validEmail1);
    for (int i = 0; i < 21; i++)
      invalidToFields.append("," + i + validEmail1);
    ValidateAppController.isValidCreateFeedbackRequest(validID, invalidToFields.toString(), validString); // invalidToFields
  }

  /**
   * TODO: Describe this method.
   *
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testIsValidCreateFeedbackRequestWithInvalidNotes() throws InvalidAttributeValueException
  {
    StringBuilder invalidNotes = new StringBuilder(validString);
    for (int i = 0; i < 201; i++)
      invalidNotes.append(validString);
    ValidateAppController.isValidCreateFeedbackRequest(validID, validEmail1, invalidNotes.toString()); // invalidNotes
  }

}
