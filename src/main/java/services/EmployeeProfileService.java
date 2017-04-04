package services;

import static services.ad.ADOperations.*;
import static services.mappers.EmployeeProfileMapper.*;

import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;
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
  private static final String EMPLOYEE_NOT_FOUND_LOG = "Employee not found based on the criteria: {} {} ";
  private static final String EMPLOYEE_NOT_FOUND = "Employee not found based on the criteria: ";
  private static final String HR_PERMISSION_EXCEPTION = "Exception caught while trying to find HR Dashboard Permission for employee with ID {}";

  // Sopra AD Details
  private static final String AD_SOPRA_TREE = "ou=usersemea,DC=emea,DC=msad,DC=sopra";
  private static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";

  // DB fields
  private static final String EMPLOYEE_ID = "profile.employeeID";
  private static final String USERNAME = "profile.username";
  private static final String EMAIL_ADDRESS = "profile.emailAddress";


  private final ADSearchSettings sopraADSearchSettings;
  private final MorphiaOperations morphiaOperations;

  public EmployeeProfileService(final MorphiaOperations morphiaOperations, final ADSearchSettings sopraADSearchSettings)
  {
    this.morphiaOperations = morphiaOperations;
    this.sopraADSearchSettings = sopraADSearchSettings;
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee ID.
   *
   * @param employeeID
   * @return the {@code EmployeeProfile} for the given employee ID
   * @throws EmployeeNotFoundException if the given employee ID could not be found in the database
   * @throws ADConnectionException if the employee could not be
   */
  public EmployeeProfile fetchEmployeeProfile(final long employeeID)
      throws EmployeeNotFoundException
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
    if (usernameEmail == null || usernameEmail.isEmpty())
    {
      throw new IllegalArgumentException(INVALID_USERNAME_OR_EMAIL_ADDRESS);
    }
    
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

    final EmployeeProfile profile = fetchEmployeeProfile(EMAIL_ADDRESS, emailAddress);
    setHasHRDash(profile);

    return profile;
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
