package functionalities;

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
import dataStructure.ADProfile_Advanced;
import dataStructure.ADProfile_Basic;
import dataStructure.Constants;
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

	public static ADProfile_Basic authenticateUserProfile(String usernameEmail) throws NamingException, InvalidAttributeValueException {
		//Verify the given string
		if(usernameEmail==null || usernameEmail.equals("") || usernameEmail.length()<1)
			throw new InvalidAttributeValueException(Constants.INVALID_EMAILORUSERNAME_AD);
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(Constants.AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter="";
		if(usernameEmail.contains("@"))
			searchFilter = "(mail=" + usernameEmail + ")";
		else
			searchFilter = "(sAMAccountName=" + usernameEmail + ")";
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_SOPRA_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			ADProfile_Advanced adObj=new ADProfile_Advanced();
			//Extract the information from the AD and add it to our object
			adObj.setEmployeeID(Long.parseLong(attrs.get("extensionAttribute7").get().toString().substring(1)));
			//Extract the GUID which is a hexadecimal number and must be converted before using it
			UUID uid=UUID.nameUUIDFromBytes((byte[])attrs.get("objectGUID").get());
			adObj.setGUID(uid.toString());
			adObj.setSurname((String)attrs.get("sn").get());
			adObj.setForename((String)attrs.get("givenName").get());
			adObj.setEmailAddress((String)attrs.get("mail").get());
			adObj.setUsername((String)attrs.get("sAMAccountName").get());
			adObj.setCompany((String) attrs.get("company").get());
			adObj.setTeam((String) attrs.get("department").get());

			//Try to extract the reportees of a user by calling a static method inside the ADReporteedDAO which deals with the connection with the STERIA AD
			try{
				adObj=ADReporteesDAO.findManagerReportees(adObj.getUsername(), adObj);
			}catch(Exception e){
				System.err.println(e.getMessage());
				//Nothing to do if this operation fails
			}
			//Close the connection with the AD
			ldapContext.close();
			ldapContext=null;
			return EmployeeDAO.matchADWithMongoData(adObj);
			//There is no a matching user in the Active Directory
		}else{
			//Close the connection with the AD
			ldapContext.close();
			ldapContext=null;
			throw new InvalidAttributeValueException(Constants.NOTFOUND_EMAILORUSERNAME_AD + usernameEmail);
		}
	}

	public static ADProfile_Basic verifyIfUserExists(long employeeID) throws NamingException, InvalidAttributeValueException{
		//Verify the long
		if(employeeID<1)
			throw new InvalidAttributeValueException(Constants.INVALID_IDNOTFOND);
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		//Check the employeeID in the AD
		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(Constants.AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter="(extensionAttribute7=s" + employeeID + ")";
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_SOPRA_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			//Extract the username from the AD
			String userName=(String) attrs.get("sAMAccountName").get();
			if(!userName.equals(""))
				return ADProfileDAO.authenticateUserProfile(userName);
		}
		throw new InvalidAttributeValueException(Constants.INVALID_IDMATCHUSERNAME+employeeID);
	}

	public static String findEmployeeFullNameFromEmailAddress(String email) throws NamingException, InvalidAttributeValueException{
		//Verify the given email address
		if(email==null || email.length()<1)
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_MAIL);
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(Constants.AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		
		String searchFilter = "(mail=" + email + ")";
		
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_SOPRA_TREE, searchFilter, searchCtls);

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
		throw new InvalidAttributeValueException(Constants.INVALID_EMAIL_AD + email);
	}
	
	public static String findEmployeeFullNameFromID(String id)throws InvalidAttributeValueException, NamingException{
		//Verify the given ID
		if(id==null || id.length()<1)
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERID);
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(Constants.AD_SOPRA_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter = "(extensionAttribute7=s" + id + ")";
		
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_SOPRA_TREE, searchFilter, searchCtls);

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
		throw new InvalidAttributeValueException(Constants.INVALID_IDMATCHUSERNAME + id);
	}

	private static DirContext getADConnection() throws NamingException{

		Hashtable<String, String> ldapEnvironmentSettings = new Hashtable<String, String>();
		ldapEnvironmentSettings.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ldapEnvironmentSettings.put(Context.PROVIDER_URL,  Constants.AD_SOPRA_HOST+":"+Constants.AD_PORT);
		ldapEnvironmentSettings.put(Context.SECURITY_AUTHENTICATION, Constants.AD_AUTHENTICATION);

		//This is essential in order to retrieve the user GUID later on in the process
		ldapEnvironmentSettings.put("java.naming.ldap.attributes.binary", "objectGUID");

		ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL, Constants.AD_SOPRA_USERNAME);
		//ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL, "cn="+Constants.AD_USERNAME+","+Constants.AD_TREE);
		ldapEnvironmentSettings.put(Context.SECURITY_CREDENTIALS, Constants.AD_SOPRA_PASSWORD);
		return new InitialDirContext(ldapEnvironmentSettings);
	}
}
