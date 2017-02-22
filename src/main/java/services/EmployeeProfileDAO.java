package services;

import static javax.naming.Context.*;
import static dataStructure.Constants.INVALID_IDNOTFOND;
import static dataStructure.Constants.INVALID_IDMATCHUSERNAME;
import static dataStructure.Constants.INVALID_CONTEXT_MAIL;
import static dataStructure.Constants.INVALID_CONTEXT_USERNAME;

import java.util.Hashtable;
import java.util.NoSuchElementException;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import dataStructure.ADProfile_Advanced;
import dataStructure.ADProfile_Basic;
import dataStructure.EmployeeProfile_NEW;
import services.ad.ADConnection;
import services.ad.ADConnectionException;

import java.util.UUID;

/**
 * This class contains the definition of the ADProfileDAO
 *
 */
public final class EmployeeProfileDAO
{
  // Sopra AD Details
  private static final String AD_SOPRA_HOST = "ldap://duns.ldap-ad.dmsi.corp.sopra";
  private static final String AD_SOPRA_URL = AD_SOPRA_HOST.concat(":389");
  private static final String AD_SOPRA_USERNAME = "svc_mycareer@emea.msad.sopra";
  private static final String AD_SOPRA_PASSWORD = "N9T$SiPSZ";
  private static final String AD_SOPRA_TREE = "ou=usersemea,DC=emea,DC=msad,DC=sopra";
  private static final String AD_SOPRA_BINARY_ATTRIBUTES = "objectGUID";
  private static final String AD_SOPRA_PRINCIPAL = AD_SOPRA_USERNAME;
  private static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";
  private static final String[] AD_SOPRA_ATTRIBUTES = { "sn", "givenName", "company", "sAMAccountName",
      "extensionAttribute7", "objectGUID", "mail", "department", "targetAddress", "memberOf" };

  // Steria AD Details
  private static final String AD_STERIA_HOST = "ldap://one.steria.dom";
  private static final String AD_STERIA_URL = AD_STERIA_HOST.concat(":389");
  private static final String AD_STERIA_USERNAME = "UK-SVC-CAREER";
  private static final String AD_STERIA_PASSWORD = "3I=AkSiGRr";
  private static final String AD_STERIA_SEARCH_TREE = "DC=one,DC=steria,DC=dom";
  private static final String AD_STERIA_LOGIN_TREE = "OU=Service Accounts,OU=UKCentral,OU=UK,OU=Resources,DC=one,DC=steria,DC=dom";
  private static final String AD_STERIA_PRINCIPAL = new StringBuilder("cn=").append(AD_STERIA_USERNAME).append(",")
      .append(AD_STERIA_LOGIN_TREE).toString();
  private static final String[] AD_STERIA_ATTRIBUTES = { "directReports", "sn", "givenName", "mail", "targetAddress",
      "company", "sAMAccountName", "department", "ou", "SteriaSectorUnit" };

  // Other AD settings
  private static final String AUTHENTICATION = "simple";
  private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
  private static final String BINARY_ATTRIBUTES_KEY = "java.naming.ldap.attributes.binary";
  private static final String TIMEOUT_ATTRIBUTE_KEY = "com.sun.jndi.ldap.read.timeout";
  private static final String TIMEOUT_ATTRIBUTE = "10000";

  // AD errors
  private static final String NOTFOUND_EMAILORUSERNAME_AD = "The given 'username/email address' didn't match any valid employee: ";
  private static final String INVALID_EMAILORUSERNAME_AD = "The given 'username/email address' is not valid in this context";
  private static final String INVALID_EMAIL_AD = "The given email address didn't match any employee: ";
  private static final String NOTFOUND_USERNAME_AD = "The given 'username' didn't match any valid employee: ";

  private static final Hashtable<String, String> SOPRA_ENVIRONMENT_SETTINGS = new Hashtable<>();
  private static final Hashtable<String, String> STERIA_ENVIRONMENT_SETTINGS = new Hashtable<>();;

  static
  {
    SOPRA_ENVIRONMENT_SETTINGS.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    SOPRA_ENVIRONMENT_SETTINGS.put(PROVIDER_URL, AD_SOPRA_URL);
    SOPRA_ENVIRONMENT_SETTINGS.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    SOPRA_ENVIRONMENT_SETTINGS.put(BINARY_ATTRIBUTES_KEY, AD_SOPRA_BINARY_ATTRIBUTES);
    SOPRA_ENVIRONMENT_SETTINGS.put(SECURITY_PRINCIPAL, AD_SOPRA_PRINCIPAL);
    SOPRA_ENVIRONMENT_SETTINGS.put(SECURITY_CREDENTIALS, AD_SOPRA_PASSWORD);
    SOPRA_ENVIRONMENT_SETTINGS.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);

    STERIA_ENVIRONMENT_SETTINGS.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    STERIA_ENVIRONMENT_SETTINGS.put(PROVIDER_URL, AD_STERIA_URL);
    STERIA_ENVIRONMENT_SETTINGS.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    STERIA_ENVIRONMENT_SETTINGS.put(SECURITY_PRINCIPAL, AD_STERIA_PRINCIPAL);
    STERIA_ENVIRONMENT_SETTINGS.put(SECURITY_CREDENTIALS, AD_STERIA_PASSWORD);
    STERIA_ENVIRONMENT_SETTINGS.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);
  }

  public EmployeeProfile_NEW authenticateUserProfile(String usernameEmail)
      throws ADConnectionException, NamingException, InvalidAttributeValueException
  {
    // Verify the given string
    if (usernameEmail == null || usernameEmail.equals("") || usernameEmail.length() < 1)
      throw new InvalidAttributeValueException(INVALID_EMAILORUSERNAME_AD);
    
    
    EmployeeProfile_NEW adObj = new EmployeeProfile_NEW();
    // specify the LDAP search filter
    if (usernameEmail.contains("@"))
    {
      if (usernameEmail.toString().contains("@soprasteria.com"))
      {
        adObj = authenticateSSEmailUserName(usernameEmail);
      }
      else
      {
        adObj = authenticateJVEmail(usernameEmail);
      }
    }
    else
    {
      adObj = authenticateSSEmailUserName(usernameEmail);
    }

    return adObj;
  }

  public EmployeeProfile_NEW verifyIfUserExists(long employeeID)
      throws ADConnectionException, NamingException, InvalidAttributeValueException
  {
    if (employeeID < 1)
    {
      throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
    }

    final String searchFilter = "(extensionAttribute7=s" + employeeID + ")";

    try (final ADConnection connection = new ADConnection(SOPRA_ENVIRONMENT_SETTINGS))
    {
      final Attributes attrs = connection.searchAD(new SearchControls(), AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE,
          searchFilter);
      final String userName = (String) attrs.get("sAMAccountName").get();

      return authenticateUserProfile(userName);
    }
    catch (final NoSuchElementException nsee)
    {
      throw new InvalidAttributeValueException(INVALID_IDMATCHUSERNAME + employeeID);
    }
  }

  // find full name using sopra steria email
  public String findEmployeeFullNameFromEmailAddress(String email)
      throws ADConnectionException, NamingException, InvalidAttributeValueException
  {
    if (email == null || email.length() < 1)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_MAIL);
    }

    final String searchFilter = "(mail=" + email + ")";

    try (final ADConnection connection = new ADConnection(SOPRA_ENVIRONMENT_SETTINGS))
    {
      Attributes attrs = connection.searchAD(new SearchControls(), AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE, searchFilter);

      // Find and return the full name
      String surname = (String) attrs.get("sn").get();
      String forename = (String) attrs.get("givenName").get();

      final String fullName = new StringBuilder(forename).append(" ").append(surname.substring(0, 1).toUpperCase())
          .append(surname.substring(1)).toString();

      return fullName;
    }
    catch (final NoSuchElementException nsee)
    {
      throw new InvalidAttributeValueException(INVALID_EMAIL_AD.concat(email));
    }
  }

  private EmployeeProfile_NEW getProfileFromSopraAD(String email, EmployeeProfile_NEW adObj)
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
    try (final ADConnection connection = new ADConnection(SOPRA_ENVIRONMENT_SETTINGS))
    {
      final Attributes attrs = connection.searchAD(new SearchControls(), AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE,
          searchFilter);

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

      adObj.setGUID(uid.toString());
      adObj.setHasHRDash(hasHRDash);
      adObj.setSurname((String) attrs.get("sn").get());
      adObj.setForename((String) attrs.get("givenName").get());
      adObj.setEmailAddress((String) attrs.get("mail").get());
      adObj.setUsername((String) attrs.get("sAMAccountName").get());
      adObj.setCompany((String) attrs.get("company").get());
      adObj.setSopraDepartment((String) attrs.get("department").get());
      adObj.setTeam((String) attrs.get("department").get());

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

  private EmployeeProfile_NEW authenticateJVEmail(String email)
      throws ADConnectionException, NamingException, InvalidAttributeValueException
  {
    EmployeeProfile_NEW adObj = new EmployeeProfile_NEW();

    final String searchFilter = "(targetAddress=" + email + ")";

    // Check the results retrieved
    try (final ADConnection connection = new ADConnection(STERIA_ENVIRONMENT_SETTINGS))
    {
      final Attributes attrs = connection.searchAD(new SearchControls(), AD_STERIA_ATTRIBUTES, AD_STERIA_SEARCH_TREE,
          searchFilter);

      getProfileFromSopraAD((String) attrs.get("sAMAccountName").get(), adObj);
      getProfileFromSteriaAD(adObj.getUsername(), adObj);

    }
    catch (NoSuchElementException | NullPointerException | NamingException e)
    {
      throw new InvalidAttributeValueException(NOTFOUND_EMAILORUSERNAME_AD.concat(email));
    }

    return adObj;
  }

  private EmployeeProfile_NEW authenticateSSEmailUserName(String usernameEmail)
      throws NamingException, InvalidAttributeValueException
  {
    final EmployeeProfile_NEW adObj = new EmployeeProfile_NEW();
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

  private EmployeeProfile_NEW getProfileFromSteriaAD(String username, EmployeeProfile_NEW userData)
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

    try (final ADConnection connection = new ADConnection(STERIA_ENVIRONMENT_SETTINGS))
    {
      final Attributes attrs = connection.searchAD(new SearchControls(), AD_STERIA_ATTRIBUTES, AD_STERIA_SEARCH_TREE,
          searchFilter);
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
}
