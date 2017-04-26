package services;

import static dataStructure.EmployeeProfile.*;
import static dataStructure.EmailAddresses.*;
import static services.ad.ADOperations.*;
import static services.mappers.EmployeeProfileMapper.*;
import static com.mongodb.client.model.Filters.*;
import static utils.Validate.*;

import javax.naming.directory.SearchResult;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

import dataStructure.EmployeeProfile;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;
import services.db.MongoOperations;
import services.db.MorphiaOperations;

/**
 * Service which fetches EmployeeProfile objects.
 *
 * @see EmployeeProfile
 */
public class EmployeeProfileService
{
  private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeProfileService.class);

  // Exception messages
  private static final String TOO_MANY_RESULTS = "More than one match was found in the database";
  private static final String INVALID_EMPLOYEE_ID = "Employee ID cannot be a negative number";
  private static final String INVALID_USERNAME_OR_EMAIL_ADDRESS = "Not a valid username or email address";
  private static final String INVALID_EMAIL_ADDRESS = "Not a valid email address";
  private static final String INVALID_USERNAME = "Not a valid username";
  private static final String DUPLICATE_EMAIL_ADDRESS = "Employee with ID {} attempted to add an email addresss which already exists: {}";
  private static final String EMAIL_ADDRESS_NOT_FOUND_LOG = "Employee not found based on the email address: {}";
  private static final String EMPLOYEE_NOT_FOUND_LOG = "Employee not found based on the criteria: {} {}";
  private static final String EMPLOYEE_NOT_FOUND = "Employee not found based on the criteria: ";
  private static final String ADD_EMAIL_EXCEPTION = "Exception caught while updating user email address.";
  private static final String HR_PERMISSION_EXCEPTION = "Exception caught while trying to find HR Dashboard Permission for employee with ID {}";

  // Sopra AD Details
  private static final String AD_SOPRA_TREE = "ou=usersemea,DC=emea,DC=msad,DC=sopra";
  private static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";

  private final ADSearchSettings sopraADSearchSettings;
  private final MorphiaOperations morphiaOperations;
  private final MongoOperations employeeOperations;

  public EmployeeProfileService(final MorphiaOperations morphiaOperations, final MongoOperations employeeOperations,
      final ADSearchSettings sopraADSearchSettings)
  {
    this.morphiaOperations = morphiaOperations;
    this.employeeOperations = employeeOperations;
    this.sopraADSearchSettings = sopraADSearchSettings;
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee ID.
   *
   * @param employeeID
   * @return the {@code EmployeeProfile} for the given employee ID
   * @throws IllegalArgumentException if the employee ID is in an invalid format
   * @throws EmployeeNotFoundException if the given employee ID could not be found in the database
   */
  public EmployeeProfile fetchEmployeeProfile(final long employeeID) throws EmployeeNotFoundException
  {
    final EmployeeProfile profile = fetchEmployeeProfile(EMPLOYEE_ID, employeeID);
    setHasHRDash(profile);

    return profile;
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee username or email address.
   *
   * This is a convenience method to allow an employee profile to be fetched when it is unknown whether a username or
   * email address has been provided.
   * 
   * @param usernameEmail The given username or email address
   * @return the {@code EmployeeProfile} relating to the given username or email address
   * @throws EmployeeNotFoundException if the given username or email address could not be found in the database
   */
  // TODO See if this method can be removed. We should know whether an email address or username is being
  // used for authentication.
  public EmployeeProfile fetchEmployeeProfile(final String usernameEmail)
      throws EmployeeNotFoundException, IllegalArgumentException
  {
    EmployeeProfile profile;

    if (usernameEmail.contains("@"))
    {
      profile = fetchEmployeeProfileFromEmailAddress(usernameEmail);
    }
    else
    {
      profile = fetchEmployeeProfileFromUsername(usernameEmail);
    }

    return profile;
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee username.
   *
   * @param username
   * @return the {@code EmployeeProfile} relating to the given username
   * @throws EmployeeNotFoundException if the given username could not be found in the database
   */
  public EmployeeProfile fetchEmployeeProfileFromUsername(final String username)
      throws EmployeeNotFoundException, IllegalArgumentException
  {
    if (username == null || username.isEmpty())
    {
      throw new IllegalArgumentException(INVALID_USERNAME);
    }

    final EmployeeProfile profile = fetchEmployeeProfile(USERNAME, username);
    setHasHRDash(profile);

    return profile;
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee email address.
   *
   * @param emailAddress
   * @return the {@code EmployeeProfile} relating to the given email address
   * @throws EmployeeNotFoundException if the given email address could not be found in the database
   * @throws ADConnectionException if the HR dashboard permission could not be
   */
  public EmployeeProfile fetchEmployeeProfileFromEmailAddress(final String emailAddress)
      throws EmployeeNotFoundException, IllegalArgumentException
  {
    if (emailAddress == null || emailAddress.isEmpty() || !emailAddress.contains("@"))
    {
      throw new IllegalArgumentException(INVALID_EMAIL_ADDRESS);
    }

    EmployeeProfile profile = null;

    try
    {
      profile = morphiaOperations.getEmployeeFromEmailAddress(emailAddress).getProfile();
    }
    catch (final NullPointerException e)
    {
      LOGGER.error(EMAIL_ADDRESS_NOT_FOUND_LOG, emailAddress);
      throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.concat(emailAddress));
    }

    setHasHRDash(profile);

    return profile;
  }

  /**
   * Adds a user provided email address to the given user profile.
   * 
   * The email address is added to the "profile.emailAddresses.userAddress" field and overwrites any existing value in
   * that field.
   *
   * @param employeeId The employee ID of the user profile to amend
   * @param emailAddress The email address to add, provided by the user
   * @throws DuplicateEmailAddressException 
   */
  public boolean editUserEmailAddress(final Long employeeId, final String emailAddress) throws DuplicateEmailAddressException
  {
    if (!isUniqueEmailAddress(emailAddress))
    {
      LOGGER.info(DUPLICATE_EMAIL_ADDRESS, employeeId, emailAddress);
      throw new DuplicateEmailAddressException();
    }
    
    boolean updated = false;

    try
    {
      updated = stringNotEmptyNotNull(emailAddress) ? setUserEmailAddress(employeeId, emailAddress)
          : unsetUserEmailAddress(employeeId);
    }
    catch (final MongoException me)
    {
      LOGGER.error(ADD_EMAIL_EXCEPTION, me);
    }

    return updated;
  }

  private boolean isUniqueEmailAddress(String emailAddress)
  {
    final String[] emailFields = { MAIL, TARGET_ADDRESS, USER_ADDRESS };
    boolean unique = false;
    
    for (final String field : emailFields)
    {
      unique = !employeeOperations.valueExists(field, emailAddress);
      
      if (!unique)
      {
        break;
      }
    }
    
    return unique;
  }

  private boolean setUserEmailAddress(final long employeeId, final String emailAddress)
  {
    return employeeOperations.setFields(eq(EMPLOYEE_ID, employeeId), new Document(USER_ADDRESS, emailAddress));
  }

  private boolean unsetUserEmailAddress(final long employeeId)
  {
    return employeeOperations.unsetFields(eq(EMPLOYEE_ID, employeeId), new Document(USER_ADDRESS, ""));
  }

  private <T> EmployeeProfile fetchEmployeeProfile(final String field, final T value) throws EmployeeNotFoundException
  {
    EmployeeProfile profile = null;

    try
    {
      profile = morphiaOperations.getEmployeeProfile(field, value);
    }
    catch (final NullPointerException e)
    {
      LOGGER.error(EMPLOYEE_NOT_FOUND_LOG, field, value);
      throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND + value);
    }

    return profile;
  }

  private void setHasHRDash(final EmployeeProfile profile)
  {
    try
    {
      profile.setHasHRDash(hasHRDash(profile.getEmployeeID()));
    }
    catch (final ADConnectionException e)
    {
      LOGGER.warn(HR_PERMISSION_EXCEPTION, profile.getEmployeeID());
      profile.setHasHRDash(false);
    }
  }

  private boolean hasHRDash(final long employeeID) throws ADConnectionException
  {
    final String filter = "(extensionAttribute7=s" + employeeID + ")";
    final SearchResult result = searchADSingleResult(sopraADSearchSettings, AD_SOPRA_TREE, filter);

    return mapHRPermission(result.getAttributes());
  }
}
