package utils;

import static dataStructure.Constants.UK_TIMEZONE;

import java.time.LocalDate;
import java.time.YearMonth;
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
  public static boolean stringsNotEmptyNotNullOrThrow(String... strings) throws InvalidAttributeValueException
  {
    if (strings.length < 1)
    {
      throw new InvalidAttributeValueException("No values have been given, please try again with values.");
    }

    if (Arrays.stream(strings).anyMatch(s -> !stringNotEmptyNotNull(s)))
    {
      throw new InvalidAttributeValueException("One or more of the values are empty. Please try again.");
    }

    return false;
  }

  /**
   * Checks if a String is empty or null
   * 
   * @param strings
   * @return Returns true if the String is not null and not empty, false otherwise.
   */
  public static boolean stringNotEmptyNotNull(final String string)
  {
    return !(string == null || string.isEmpty());
  }

  /**
   * Checks if one or more objects are null
   * 
   * @param objects
   * @return Returns false if all objects are not null, throws exception otherwise.
   * @throws InvalidAttributeValueException
   */
  public static boolean throwIfNull(Object... objects) throws InvalidAttributeValueException
  {
    if (objects.length < 1)
    {
      throw new InvalidAttributeValueException("No values have been given, please try again with values.");
    }

    if (Arrays.stream(objects).anyMatch(o -> o == null))
    {
      throw new InvalidAttributeValueException("One or more of the values are empty. Please try again.");
    }

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
    stringNotEmptyNotNull(email);

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
    stringNotEmptyNotNull(id);
    
    Pattern pattern = Pattern.compile("^(\\d{6})_(\\d{17})$");
    
    return pattern.matcher(id).matches();
  }

  /**
   * Checks if a year month is in the past
   *
   * @param yearMonth
   * @return A local date of the year month with the using the {@link java.time.YearMonth#atEndOfMonth() atEndOfMonth}
   *         method.
   * @throws InvalidAttributeValueException if date is in the past
   */
  public static LocalDate notPastOrThrow(YearMonth yearMonth) throws InvalidAttributeValueException
  {
    if (yearMonth.isBefore(YearMonth.now(UK_TIMEZONE)))
    {
      throw new InvalidAttributeValueException("Date is in past.");
    }
    
    return yearMonth.atEndOfMonth();
  }
}
