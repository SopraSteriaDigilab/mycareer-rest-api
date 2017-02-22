package services.ad;

import java.util.Hashtable;
import javax.management.InvalidAttributeValueException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import dataStructure.EmployeeProfile;
import dataStructure.Constants;

/**
 * This class contains the definition of the ADReporteedDAO and helps the ADPRofileDAO to retrieve the list of reportees
 * for a manager
 *
 */
@Deprecated
public class ADReporteesDAO_OLD
{

  private static DirContext ldapContext;

  @SuppressWarnings("unchecked")
  public static EmployeeProfile findSteriaProfileAttributes(String username, EmployeeProfile userData)
      throws NamingException, InvalidAttributeValueException
  {
    // Verify the given string
    if (username == null || username.length() < 2 || userData == null)
      throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERNAME);
    // Instantiate the connection
    if (ldapContext == null) ldapContext = getADConnection();

    // Create the search controls
    SearchControls searchCtls = new SearchControls();
    // Specify the attributes to return
    searchCtls.setReturningAttributes(Constants.AD_STERIA_ATTRIBUTES);
    // Specify the search scope
    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    // specify the LDAP search filter
    String searchFilter = "(sAMAccountName=" + username + ")";
    // Search for objects using the filter
    NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_STERIA_SEARCH_TREE, searchFilter,
        searchCtls);

    // Check the results retrieved
    if (answer.hasMoreElements())
    {
      SearchResult sr = (SearchResult) answer.next();
      Attributes attrs = sr.getAttributes();
      // Extract the information from the AD and add it to our object
      // Try to extract the reportees of a user
      try
      {
        NamingEnumeration<String> isUserAManager = (NamingEnumeration<String>) attrs.get("directReports").getAll();
        while (isUserAManager.hasMoreElements())
        {
          // Get the current element
          String s = isUserAManager.next().toString();
          // Split it by commas
          String[] t = s.split(",");
          // We need to extract only the 1st element of the array, removing the first 3 chars (cn=)
          userData.addReportee(t[0].substring(3));
        }
        userData.setIsManager(true);
      }
      catch (Exception e)
      {
        // If if goes here, it means the user is not a manager
        userData.setIsManager(false);
      }

      userData.setSector(((String) attrs.get("SteriaSectorUnit").get()).substring(3));
      userData.setSteriaDepartment((String) attrs.get("department").get());
      userData.setSuperSector((String) attrs.get("ou").get());
      // Close the connection with the AD
      ldapContext.close();
      ldapContext = null;
      // There is no a matching user in the Active Directory
    }
    else
    {
      // Close the connection with the AD
      ldapContext.close();
      ldapContext = null;
      throw new InvalidAttributeValueException(Constants.NOTFOUND_USERNAME_AD + username);
    }
    // return the object
    return userData;
  }

  private static DirContext getADConnection() throws NamingException
  {

    Hashtable<String, String> ldapEnvironmentSettings = new Hashtable<String, String>();
    ldapEnvironmentSettings.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    ldapEnvironmentSettings.put(Context.PROVIDER_URL, Constants.AD_STERIA_HOST + ":" + Constants.AD_PORT);
    ldapEnvironmentSettings.put(Context.SECURITY_AUTHENTICATION, Constants.AD_AUTHENTICATION);

    // This is essential in order to retrieve the user GUID later on in the process
    // ldapEnvironmentSettings.put("java.naming.ldap.attributes.binary", "objectGUID");

    ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL,
        "cn=" + Constants.AD_STERIA_USERNAME + "," + Constants.AD_STERIA_LOGIN_TREE);
    ldapEnvironmentSettings.put(Context.SECURITY_CREDENTIALS, Constants.AD_STERIA_PASSWORD);
    return new InitialDirContext(ldapEnvironmentSettings);
  }
}
