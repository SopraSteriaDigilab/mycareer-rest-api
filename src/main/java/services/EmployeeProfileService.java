package services;

import static services.ad.ADOperations.*;

import static dataStructure.Constants.INVALID_ID_NOT_FOUND;
import static dataStructure.Constants.INVALID_IDMATCHUSERNAME;
import static dataStructure.Constants.INVALID_CONTEXT_MAIL;
import static dataStructure.Constants.INVALID_CONTEXT_USERNAME;

import java.util.NoSuchElementException;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;
import services.ad.ADConnection;
import services.ad.ADConnectionException;
import services.ad.ADConnectionImpl;
import services.ad.ADSearchSettings;

import java.util.UUID;

/**
 * This class contains the definition of the ADProfileDAO
 *
 */
// TODO user EmployeeProfileMapper for all employeeProfile creations.
public class EmployeeProfileService
{
  private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeProfileService.class);

  private static final String TOO_MANY_RESULTS = "More than one match was found in the Active Directory";

  // Sopra AD Details
  private static final String AD_SOPRA_TREE = "ou=usersemea,DC=emea,DC=msad,DC=sopra";
  private static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";

  // Steria AD Details
  private static final String AD_STERIA_TREE = "DC=one,DC=steria,DC=dom";

  // AD errors
  private static final String NOTFOUND_EMAILORUSERNAME_AD = "The given 'username/email address' didn't match any valid employee: ";
  private static final String INVALID_EMAILORUSERNAME_AD = "The given 'username/email address' is not valid in this context";
  private static final String INVALID_EMAIL_AD = "The given email address didn't match any employee: ";
  private static final String NOTFOUND_USERNAME_AD = "The given 'username' didn't match any valid employee: ";

  private final ADSearchSettings sopraADSearchSettings;
  private final ADSearchSettings steriaADSearchSettings;

  public EmployeeProfileService(final ADSearchSettings sopraADSearchSettings,
      final ADSearchSettings steriaADSearchSettings)
  {
    this.sopraADSearchSettings = sopraADSearchSettings;
    this.steriaADSearchSettings = steriaADSearchSettings;
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee ID.
   *
   * @param employeeID
   * @return the {@code EmployeeProfile} for the given employee ID
   * @throws InvalidAttributeValueException if the employee ID is not valid
   * @throws NamingException
   * @throws ADConnectionException
   */
  // TODO handle NamingException and ADConnectionException instead of throw?
  public EmployeeProfile fetchEmployeeProfile(final long employeeID)
      throws InvalidAttributeValueException, ADConnectionException, NamingException
  {
    if (employeeID < 1)
    {
      throw new InvalidAttributeValueException(INVALID_ID_NOT_FOUND);
    }

    final String searchFilter = "(extensionAttribute2=s" + employeeID + ")";

    try
    {
      final NamingEnumeration<SearchResult> result = searchAD(steriaADSearchSettings, AD_STERIA_TREE, searchFilter);
      final Attributes attrs = result.next().getAttributes();

      if (result.hasMoreElements())
      {
        LOGGER.warn(TOO_MANY_RESULTS);
      }

      final String userName = (String) attrs.get("sAMAccountName").get();

      return fetchEmployeeProfileFromUsername(userName);
    }
    catch (final NoSuchElementException nsee)
    {
      throw new InvalidAttributeValueException(INVALID_IDMATCHUSERNAME + employeeID);
    }
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee username or email address.
   *
   * This is a convenience method to allow an employee profile to be fetched when it is unknown whether a username or
   * email address has been provided.
   * 
   * @param usernameEmail The given username or email address
   * @return the {@code EmployeeProfile} relating to the given username or email address
   * @throws InvalidAttributeValueException if the given username or email address could not be matched to an employee
   * @throws NamingException
   * @throws ADConnectionException
   */
  // TODO See if this method can be removed. We should know whether an email address or username is being
  // used for authentication. If not, handle ADConnectionException and NamingException instead of throw?
  public EmployeeProfile fetchEmployeeProfile(String usernameEmail)
      throws InvalidAttributeValueException, NamingException, ADConnectionException
  {
    if (usernameEmail == null || usernameEmail.equals("") || usernameEmail.length() < 1)
    {
      throw new InvalidAttributeValueException(INVALID_EMAILORUSERNAME_AD);
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
   * @throws InvalidAttributeValueException if the given username could not be matched to an employee
   * @throws NamingException
   */
  // TODO handle NamingException instead of throw?
  public EmployeeProfile fetchEmployeeProfileFromUsername(final String username)
      throws InvalidAttributeValueException, NamingException
  {
    // Verify the given string
    if (username == null || username.equals("") || username.length() < 1)
    {
      throw new InvalidAttributeValueException(INVALID_EMAILORUSERNAME_AD);
    }

    return authenticateSSEmailUserName(username);
  }

  /**
   * Fetches an {@code EmployeeProfile} object using the given employee email address.
   *
   * @param emailAddress
   * @return the {@code EmployeeProfile} relating to the given email address
   * @throws InvalidAttributeValueException if the given email address could not be matched to an employee
   * @throws NamingException
   * @throws ADConnectionException
   */
  // TODO Handle ADConnectionException and NamingException instead of throw?
  public EmployeeProfile fetchEmployeeProfileFromEmailAddress(final String emailAddress)
      throws InvalidAttributeValueException, NamingException, ADConnectionException
  {
    if (emailAddress == null || emailAddress.equals("") || emailAddress.length() < 1)
    {
      throw new InvalidAttributeValueException(INVALID_EMAILORUSERNAME_AD);
    }

    EmployeeProfile adObj = new EmployeeProfile();

    if (emailAddress.toString().contains("@soprasteria.com"))
    {
      adObj = authenticateSSEmailUserName(emailAddress);
    }
    else
    {
      adObj = authenticateJVEmail(emailAddress);
    }

    return adObj;
  }

  /**
   * Builds an employee's full name using the given email address.
   *
   * @param emailAddress
   * @return the employee's full name
   * @throws InvalidAttributeValueException If the given email address could not be matched to an employee
   * @throws NamingException
   * @throws ADConnectionException
   */
  // TODO: this method doesn't belong in this class
  public String fetchEmployeeFullNameFromEmailAddress(final String emailAddress)
      throws InvalidAttributeValueException, ADConnectionException, NamingException
  {
    if (emailAddress == null || emailAddress.length() < 1)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_MAIL);
    }

    final String searchFilter = "(mail=" + emailAddress + ")";

    try
    {
      final NamingEnumeration<SearchResult> result = searchAD(steriaADSearchSettings, AD_STERIA_TREE, searchFilter);
      final Attributes attrs = result.next().getAttributes();

      if (result.hasMoreElements())
      {
        LOGGER.warn(TOO_MANY_RESULTS);
      }

      // Find and return the full name
      String surname = (String) attrs.get("sn").get();
      String forename = (String) attrs.get("givenName").get();

      final String fullName = new StringBuilder(forename).append(" ").append(surname.substring(0, 1).toUpperCase())
          .append(surname.substring(1)).toString();

      return fullName;
    }
    catch (final NoSuchElementException nsee)
    {
      throw new InvalidAttributeValueException(INVALID_EMAIL_AD.concat(emailAddress));
    }
  }

  private EmployeeProfile authenticateSSEmailUserName(String usernameEmail)
      throws NamingException, InvalidAttributeValueException
  {
    final EmployeeProfile adObj = new EmployeeProfile();
    getProfileFromSopraAD(usernameEmail, adObj);

    try
    {
      getProfileFromSteriaAD(adObj.getUsername(), adObj);
    }
    catch (Exception e)
    {
      // TODO handle!
      System.err.println(e.getMessage());
    }

    return adObj;
  }

  private EmployeeProfile authenticateJVEmail(String email)
      throws ADConnectionException, NamingException, InvalidAttributeValueException
  {
    EmployeeProfile adObj = new EmployeeProfile();

    final String searchFilter = "(targetAddress=" + email + ")";

    // Check the results retrieved
    try (final ADConnection connection = new ADConnectionImpl(steriaADSearchSettings))
    {
      final NamingEnumeration<SearchResult> result = connection.searchAD(AD_STERIA_TREE, searchFilter);

      final Attributes attrs = result.next().getAttributes();

      if (result.hasMoreElements())
      {
        LOGGER.warn(TOO_MANY_RESULTS);
      }

      getProfileFromSopraAD((String) attrs.get("sAMAccountName").get(), adObj);
      getProfileFromSteriaAD(adObj.getUsername(), adObj);

    }
    catch (NoSuchElementException | NullPointerException | NamingException e)
    {
      throw new InvalidAttributeValueException(NOTFOUND_EMAILORUSERNAME_AD.concat(email));
    }

    return adObj;
  }

  // TODO Use the ADOperations and EmployeeProfileMapper classes
  private EmployeeProfile getProfileFromSteriaAD(String username, EmployeeProfile userData)
      throws ADConnectionException, NamingException, InvalidAttributeValueException
  {
    if (username == null || username.length() < 2 || userData == null)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_USERNAME);
    }

    final String searchFilter = "(sAMAccountName=" + username + ")";
    Attribute directReports = null;
    String superSector = null;
    String sector = null;
    String steriaDepartment = null;
    boolean isManager = false;

    try (final ADConnection connection = new ADConnectionImpl(steriaADSearchSettings))
    {
      final NamingEnumeration<SearchResult> result = connection.searchAD(AD_STERIA_TREE, searchFilter);
      final Attributes attrs = result.next().getAttributes();

      if (result.hasMoreElements())
      {
        LOGGER.warn(TOO_MANY_RESULTS);
      }

      directReports = attrs.get("directReports");
      superSector = (String) attrs.get("ou").get();
      sector = ((String) attrs.get("SteriaSectorUnit").get()).substring(3);
      steriaDepartment = (String) attrs.get("department").get();
    }
    catch (final NoSuchElementException nsee)
    {
      throw new InvalidAttributeValueException(NOTFOUND_USERNAME_AD.concat(username));
    }

    isManager = directReports != null;

    if (isManager)
    {
      @SuppressWarnings("unchecked")
      final NamingEnumeration<String> reportees = (NamingEnumeration<String>) directReports.getAll();

      while (reportees.hasMoreElements())
      {
        final String s = reportees.next().toString();
        final String[] t = s.split(",");

        // We need to extract only the 1st element of the array, removing the first 3 chars (cn=)
        userData.addReportee(t[0].substring(3));
      }
    }

    userData.setIsManager(isManager);
    userData.setSuperSector(superSector);
    userData.setSector(sector);
    userData.setSteriaDepartment(steriaDepartment);

    return userData;
  }

  // TODO Use the ADOperations and EmployeeProfileMapper classes
  private EmployeeProfile getProfileFromSopraAD(String email, EmployeeProfile adObj)
      throws NamingException, InvalidAttributeValueException
  {
    // specify the LDAP search filter
    String searchFilter = "";

    if (email.contains("@"))
    {
      searchFilter = "(mail=" + email + ")";
    }
    else
    {
      searchFilter = "(sAMAccountName=" + email + ")";
    }

    // Check the results retrieved
    try (final ADConnection connection = new ADConnectionImpl(sopraADSearchSettings))
    {
      final NamingEnumeration<SearchResult> result = connection.searchAD(AD_SOPRA_TREE, searchFilter);
      final Attributes attrs = result.next().getAttributes();

      if (result.hasMoreElements())
      {
        LOGGER.warn(TOO_MANY_RESULTS);
      }
      // Get the employee ID from extensionAttribute7.
      // If this field doesn't exist then this email address cannot be handled
      Attribute extensionAttribute7 = attrs.get("extensionAttribute7");
      Long employeeId = Long.parseLong(extensionAttribute7.get().toString().substring(1));
      adObj.setEmployeeID(employeeId);

      // Extract the GUID which is a hexadecimal number and must be converted before using it
      UUID uid = UUID.nameUUIDFromBytes((byte[]) attrs.get("objectGUID").get());

      // Check for HR Dashboard permission
      @SuppressWarnings("unchecked")
      final NamingEnumeration<String> groups = (NamingEnumeration<String>) attrs.get("memberOf").getAll();
      String group = null;
      boolean hasHRDash = false;

      while (groups.hasMoreElements())
      {
        group = groups.next().toUpperCase();
        if (group.contains(AD_SOPRA_HR_DASH.toUpperCase()))
        {
          hasHRDash = true;
          break;
        }
      }

      adObj.setGuid(uid.toString());
      adObj.setHasHRDash(hasHRDash);
      adObj.setSurname((String) attrs.get("sn").get());
      adObj.setForename((String) attrs.get("givenName").get());
      adObj.setEmailAddress((String) attrs.get("mail").get());
      adObj.setUsername((String) attrs.get("sAMAccountName").get());
      adObj.setCompany((String) attrs.get("company").get());
      adObj.setSopraDepartment((String) attrs.get("department").get());

    }
    catch (NoSuchElementException | NullPointerException e)
    { // There is no matching user in the Active Directory.
      e.printStackTrace();
      throw new InvalidAttributeValueException(NOTFOUND_EMAILORUSERNAME_AD.concat(email));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return adObj;
  }
}
