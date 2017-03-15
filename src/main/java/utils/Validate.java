package utils;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.management.InvalidAttributeValueException;

/**
 * Class to handle validation or various input types.
 */
public class Validate
{

  /**
   * Checks if one or more Strings are empty or null
   * 
   * @param strings
   * @return Returns true if all strings are not null and not empty, throws exception otherwise.
   * @throws InvalidAttributeValueException
   */
  public static boolean areStringsEmptyorNull(String... strings) throws InvalidAttributeValueException
  {
    if (strings.length < 1)
      throw new InvalidAttributeValueException("No values have been given, please try again with values.");

    if (Arrays.stream(strings).anyMatch(s -> s == null || s.isEmpty()))
      throw new InvalidAttributeValueException("One or more of the values are empty. Please try again.");

    return false;
  }

  /**
   * Checks if one or more objects are null
   * 
   * @param objects
   * @return Returns true if all objects are not null, throws exception otherwise.
   * @throws InvalidAttributeValueException
   */
  public static boolean isNull(Object... objects) throws InvalidAttributeValueException
  {
    if (objects.length < 1)
      throw new InvalidAttributeValueException("No values have been given, please try again with values.");

    if (Arrays.stream(objects).anyMatch(o -> o == null))
      throw new InvalidAttributeValueException("One or more of the values are empty. Please try again.");

    return false;
  }

  /**
   * Checks the syntax of an email address
   * 
   * @param email
   * @return Returns true if the email is valid syntax, false otherwise.
   * @throws InvalidAttributeValueException
   */
  public static boolean isValidEmailSyntax(String email) throws InvalidAttributeValueException
  {
    areStringsEmptyorNull(email);

    Pattern pattern = Pattern.compile(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    return pattern.matcher(email).matches();
  }

  /**
   * Checks syntax on feedbackRequestID against dddddd_ddddddddddddddddd where d is a number.
   * 
   * @param id
   * @return Returns true if di is valid syntax, false otherwise.
   * @throws InvalidAttributeValueException
   */
  public static boolean isValidFeedbackRequestID(String id) throws InvalidAttributeValueException
  {
    areStringsEmptyorNull(id);

    Pattern pattern = Pattern.compile("^(\\d{6})_(\\d{17})$");
    return pattern.matcher(id).matches();
  }

}
