package services.ad;

import static dataStructure.Constants.*;

import java.util.Hashtable;
import java.util.NoSuchElementException;

import javax.management.InvalidAttributeValueException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import dataStructure.ADProfile_Advanced;
import dataStructure.ADProfile_Basic;
import services.EmployeeDAO;

import java.util.UUID;

/**
 * 
 * @author Michael Piccoli
 * @author Ridhwan Nacef
 * @version 1.0
 * @since 14th November 2016
 * 
 * This class contains the definition of the ADProfileDAO 
 *
 */
public class ADProfileDAO {

	private static DirContext ldapContext;
	private static DirContext ldapSteriaContext;

	public static ADProfile_Basic authenticateUserProfile(String usernameEmail)
			throws NamingException, InvalidAttributeValueException {
		// Verify the given string
		if (usernameEmail == null || usernameEmail.equals("") || usernameEmail.length() < 1)
			throw new InvalidAttributeValueException(INVALID_EMAILORUSERNAME_AD);
		ADProfile_Advanced adObj = new ADProfile_Advanced();
		// specify the LDAP search filter
		if (usernameEmail.contains("@")) {
			if (usernameEmail.toString().contains("@soprasteria.com")) {
				adObj = authenticateSSEmailUserName(usernameEmail);
			} else {
				adObj = authenticateJVEmail(usernameEmail);
			}
		} else {
			adObj = authenticateSSEmailUserName(usernameEmail);
		}

		return EmployeeDAO.matchADWithMongoData(adObj);
	}
	
	private static ADProfile_Advanced getProfileFromSopraAD(String email)throws NamingException, InvalidAttributeValueException {
		
		if(ldapContext==null)
			ldapContext = getADConnection();
		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter="";
		if(email.contains("@"))
			searchFilter = "(mail=" + email + ")";
		else
			searchFilter = "(sAMAccountName=" + email + ")";
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(AD_SOPRA_TREE, searchFilter, searchCtls);
		ADProfile_Advanced adObj=new ADProfile_Advanced();

		//Check the results retrieved
		try {
			SearchResult sr = (SearchResult) answer.next();
			Attributes attrs = sr.getAttributes();
			
			// Get the employee ID from extensionAttribute7.
			// If this field doesn't exist then this email address cannot be handled
			Attribute extensionAttribute7 = attrs.get("extensionAttribute7");
			Long employeeId = Long.parseLong(extensionAttribute7.get().toString().substring(1));
			adObj.setEmployeeID(employeeId);
			
			//Extract the GUID which is a hexadecimal number and must be converted before using it
			UUID uid=UUID.nameUUIDFromBytes((byte[])attrs.get("objectGUID").get());
			adObj.setGUID(uid.toString());
			
			adObj.setSurname((String)attrs.get("sn").get());
			adObj.setForename((String)attrs.get("givenName").get());
			adObj.setEmailAddress((String)attrs.get("mail").get());
			adObj.setUsername((String)attrs.get("sAMAccountName").get());
			adObj.setCompany((String) attrs.get("company").get());
			adObj.setTeam((String) attrs.get("department").get());
		} catch (NoSuchElementException | NullPointerException e) { // There is no  matching user in the Active Directory.
			throw new InvalidAttributeValueException(NOTFOUND_EMAILORUSERNAME_AD + email);
		} finally {
			// Close the connection with the AD
			ldapContext.close();
			ldapContext = null;			
		}

		return adObj;
	}

	private static ADProfile_Advanced authenticateJVEmail(String email) throws NamingException, InvalidAttributeValueException {
		ADProfile_Advanced adObj = new ADProfile_Advanced();
		/// Instantiate the connection
		//Instantiate the connection
				if(ldapSteriaContext==null)
					ldapSteriaContext = getADSteriaConnectionJV();

				// Create the search controls         
				SearchControls searchCtls = new SearchControls();
				//Specify the attributes to return
				searchCtls.setReturningAttributes(AD_STERIA_ATTRIBUTES);
				//Specify the search scope
				searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				//specify the LDAP search filter
				
				String searchFilter = "(targetAddress=" + email + ")";
				
				// Search for objects using the filter
				NamingEnumeration<SearchResult> answer = ldapSteriaContext.search(AD_STERIA_SEARCH_TREE, searchFilter, searchCtls);

		// Check the results retrieved
		try {
			if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult) answer.next();
			Attributes attrs = sr.getAttributes();			
			adObj = getProfileFromSopraAD((String) attrs.get("sAMAccountName").get());
			} else {
				throw new InvalidAttributeValueException(NOTFOUND_EMAILORUSERNAME_AD + email);
			}
			// Try to extract the reportees of a user by calling a static method
			// inside the ADReporteedDAO which deals with the connection with
			// the STERIA AD
			try {
				adObj = ADReporteesDAO.findManagerReportees(adObj.getUsername(), adObj);
			} catch (Exception e) {
				// TO BE COMPLETED!!!
				System.err.println(e.getMessage());
			}
		} catch (NoSuchElementException | NullPointerException e) { // There is no  matching user in the Active Directory.
			throw new InvalidAttributeValueException(NOTFOUND_EMAILORUSERNAME_AD + email);
		} finally {
			// Close the connection with the AD
			ldapSteriaContext.close();
			ldapSteriaContext = null;			
		}

		return adObj;
	}

	private static ADProfile_Advanced authenticateSSEmailUserName(String usernameEmail) throws NamingException, InvalidAttributeValueException {
		ADProfile_Advanced adObj = new ADProfile_Advanced();
			// Check the results retrieved
		try {			
			adObj = getProfileFromSopraAD(usernameEmail);
			// the STERIA AD
			try {
				adObj = ADReporteesDAO.findManagerReportees(adObj.getUsername(), adObj);
			} catch (Exception e) {
				// TO BE COMPLETED!!!
				System.err.println(e.getMessage());
			}
		} catch (NoSuchElementException | NullPointerException e) { // There is no  matching user in the Active Directory.
			throw new InvalidAttributeValueException(NOTFOUND_EMAILORUSERNAME_AD + usernameEmail);
		}

		return adObj;
	}

	public static ADProfile_Basic verifyIfUserExists(long employeeID) throws NamingException, InvalidAttributeValueException{
		//Verify the long
		if(employeeID<1)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		//Check the employeeID in the AD
		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter="(extensionAttribute7=s" + employeeID + ")";
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(AD_SOPRA_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			//Extract the username from the AD
			String userName=(String) attrs.get("sAMAccountName").get();
			if(!userName.equals(""))
				return ADProfileDAO.authenticateUserProfile(userName);
		}
		throw new InvalidAttributeValueException(INVALID_IDMATCHUSERNAME+employeeID);
	}

	//find full name using sopra steria email
	public static String findEmployeeFullNameFromEmailAddress(String email) throws NamingException, InvalidAttributeValueException{
		//Verify the given email address
		if(email==null || email.length()<1)
			throw new InvalidAttributeValueException(INVALID_CONTEXT_MAIL);
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		
		String searchFilter = "(mail=" + email + ")";
		
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(AD_SOPRA_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			
			//Find and return the full name
			String surname=(String)attrs.get("sn").get();
			//Convert the upper case surname into a lower case string with only the 1st char uppercase
			surname=surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
			String forename=(String)attrs.get("givenName").get();
			return forename+" "+surname;
		}
		//Close the connection with the AD
		ldapContext.close();
		ldapContext=null;
		throw new InvalidAttributeValueException(INVALID_EMAIL_AD + email);
	}
	
	//find full name using targetAddress email
	public static String findEmployeeFullNameFromEmailAddressJV(String email) throws NamingException, InvalidAttributeValueException{
		String fullName = ""; 
		//Verify the given email address
		if(email==null || email.length()<1)
			throw new InvalidAttributeValueException(INVALID_CONTEXT_MAIL);
		//Instantiate the connection
		if(ldapSteriaContext==null)
			ldapSteriaContext = getADSteriaConnectionJV();

		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(AD_STERIA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		
		String searchFilter = "(targetAddress=" + email + ")";
		
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapSteriaContext.search(AD_STERIA_SEARCH_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			
			//Find and return the full name
			String surname=(String)attrs.get("sn").get();
			//Convert the upper case surname into a lower case string with only the 1st char uppercase
			surname=surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
			String forename=(String)attrs.get("givenName").get();
			fullName = forename+" "+surname;
			return fullName;
		}
		//Close the connection with the AD
		ldapSteriaContext.close();
		ldapSteriaContext=null;
		return fullName;
	}
	
	public static String findEmployeeFullNameFromID(String id)throws InvalidAttributeValueException, NamingException{
		//Verify the given ID
		if(id==null || id.length()<1)
			throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter = "(extensionAttribute7=s" + id + ")";
		
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(AD_SOPRA_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			
			//Find and return the full name
			String surname=(String)attrs.get("sn").get();
			//Convert the upper case surname into a lower case string with only the 1st char uppercase
			surname=surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
			String forename=(String)attrs.get("givenName").get();
			return surname+" "+forename;
		}
		//Close the connection with the AD
		ldapContext.close();
		ldapContext=null;
		throw new InvalidAttributeValueException(INVALID_IDMATCHUSERNAME + id);
	}

	private static DirContext getADConnection() throws NamingException{
		// get AD Sopra connection JV from 
		Hashtable<String, String> ldapEnvironmentSettings = new Hashtable<String, String>();
		ldapEnvironmentSettings.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ldapEnvironmentSettings.put(Context.PROVIDER_URL,  AD_SOPRA_HOST+":"+AD_PORT);
		ldapEnvironmentSettings.put(Context.SECURITY_AUTHENTICATION, AD_AUTHENTICATION);

		//This is essential in order to retrieve the user GUID later on in the process
		ldapEnvironmentSettings.put("java.naming.ldap.attributes.binary", "objectGUID");

		ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL, AD_SOPRA_USERNAME);
		//ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL, "cn="+Constants.AD_USERNAME+","+Constants.AD_TREE);
		ldapEnvironmentSettings.put(Context.SECURITY_CREDENTIALS, AD_SOPRA_PASSWORD);
		return new InitialDirContext(ldapEnvironmentSettings);
	}
	
	// get AD Steria connection JV from 
	private static DirContext getADSteriaConnectionJV() throws NamingException{

		Hashtable<String, String> ldapEnvironmentSettings = new Hashtable<String, String>();
		ldapEnvironmentSettings.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ldapEnvironmentSettings.put(Context.PROVIDER_URL,  AD_STERIA_HOST+":"+AD_PORT);
		ldapEnvironmentSettings.put(Context.SECURITY_AUTHENTICATION, AD_AUTHENTICATION);

		//This is essential in order to retrieve the user GUID later on in the process
		//ldapEnvironmentSettings.put("java.naming.ldap.attributes.binary", "objectGUID");

		ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL, "cn="+AD_STERIA_USERNAME+","+AD_STERIA_LOGIN_TREE);
		ldapEnvironmentSettings.put(Context.SECURITY_CREDENTIALS, AD_STERIA_PASSWORD);
		return new InitialDirContext(ldapEnvironmentSettings);
	}
}
