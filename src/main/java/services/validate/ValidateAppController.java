//package services.validate;
//
//import static services.validate.Validate.areStringsEmptyorNull;
//
//import javax.management.InvalidAttributeValueException;
//
//import utils.Utils;
//
///**
// * Class to handle validation of AppController methods.
// * 
// * @deprecated This will be replaced with Spring Rest.
// * 
// *///TODO remove.
//public class ValidateAppController
//{
//
//  /**
//   * Method to validate input from createFeedbackRequest method.
//   *
//   * @param employeeID
//   * @param toFields
//   * @param notes
//   * @return True if valid, throws exception otherwise.
//   * @throws InvalidAttributeValueException
//   */
//  public static boolean isValidCreateFeedbackRequest(long employeeID, String toFields, String notes)
//      throws InvalidAttributeValueException
//  {
//
//    if (employeeID < 1)
//      throw new InvalidAttributeValueException("The given employeeID is invalid, please try again later.");
//
//    areStringsEmptyorNull(toFields);
//
//    if (notes.length() > 1000) throw new InvalidAttributeValueException("Notes must be under 1000 characters.");
//
//    if (Utils.stringEmailsToHashSet(toFields).size() > 20)
//      throw new InvalidAttributeValueException("There must be less than 20 email addresses.");
//
//    return true;
//  }
//
//}
