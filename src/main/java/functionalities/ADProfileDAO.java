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

	@SuppressWarnings("unchecked")
	public static ADProfile_Basic authenticateUserProfile(String usernameEmail) throws NamingException, InvalidAttributeValueException {
		//Verify the given string
		if(usernameEmail==null || usernameEmail.equals("") || usernameEmail.length()<1)
			throw new InvalidAttributeValueException("The given username is invalid");
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(Constants.AD_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter="";
		if(usernameEmail.contains("@"))
			searchFilter = "(mail=" + usernameEmail + ")";
		else
			searchFilter = "(sAMAccountName=" + usernameEmail + ")";
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			ADProfile_Advanced adObj=new ADProfile_Advanced();
			//Extract the information from the AD and add it to our object
			adObj.setEmployeeID(Long.parseLong((String)attrs.get("extensionAttribute7").get().toString().substring(1)));
			//Extract the GUID which is a hexadecimal number and must be converted before using it
			UUID uid=UUID.nameUUIDFromBytes((byte[])attrs.get("objectGUID").get());
			adObj.setGUID(uid.toString());
			adObj.setSurname((String)attrs.get("sn").get());
			adObj.setForename((String)attrs.get("givenName").get());
			adObj.setEmailAddress((String)attrs.get("mail").get());
			adObj.setUsername((String)attrs.get("sAMAccountName").get());
			adObj.setCompany((String) attrs.get("company").get());
			adObj.setTeam("603 GOVERNMENT UK");

			//Try to extract the reportees of a user
			try{
				NamingEnumeration<String> isUserAManager=(NamingEnumeration<String>) attrs.get("directReports").getAll();
				while(isUserAManager.hasMoreElements()){
					//Get the current element
					String s=isUserAManager.next().toString();
					//Split it by commas
					String[] t=s.split(",");
					//We need to extract only the 1st element of the array, removing the first 3 chars (cn=)
					adObj.addReportee(t[0].substring(3));
				}
				adObj.setIsManager(true);
			}catch(Exception e){
				//If if goes here, it means the user is not a manager
				adObj.setIsManager(false);
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
			throw new InvalidAttributeValueException("No match in the AD for user "+usernameEmail);
		}
	}

	public static ADProfile_Basic verifyIfUserExists(long employeeID) throws NamingException, InvalidAttributeValueException{
		//Verify the long
		if(employeeID<1)
			throw new InvalidAttributeValueException("No valid employeeID");
		//Instantiate the connection
		if(ldapContext==null)
			ldapContext = getADConnection();

		//Check the employeeID in the AD
		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		//Specify the attributes to return
		searchCtls.setReturningAttributes(Constants.AD_ATTRIBUTES);
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter="(employeeNumber=" + employeeID + ")";
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_TREE, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			//Extract the username from the AD
			String userName=(String) attrs.get("sAMAccountName").get();
			if(!userName.equals(""))
				return ADProfileDAO.authenticateUserProfile(userName);
		}
		throw new InvalidAttributeValueException("No username has been found for employee ID: "+employeeID);
	}

	private static DirContext getADConnection() throws NamingException{
		
//		Hashtable<String, String> env = new Hashtable<String, String>();
//		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//		env.put(Context.SECURITY_AUTHENTICATION, "simple");
//		env.put(Context.PROVIDER_URL, "ldap://emea.msad.sopra:389");
//		 
//		// The value of Context.SECURITY_PRINCIPAL must be the logon username with the domain name
//		env.put(Context.SECURITY_PRINCIPAL, "svc_mycareer@emea.msad.sopra");
//		// The value of the Context.SECURITY_CREDENTIALS should be the user's password
//		env.put(Context.SECURITY_CREDENTIALS, "N9T$SiPSZ");

		Hashtable<String, String> ldapEnvironmentSettings = new Hashtable<String, String>();
		ldapEnvironmentSettings.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ldapEnvironmentSettings.put(Context.PROVIDER_URL,  Constants.AD_HOST+":"+Constants.AD_PORT);
		ldapEnvironmentSettings.put(Context.SECURITY_AUTHENTICATION, Constants.AD_AUTHENTICATION);
		
		//This is essential in order to retrieve the user GUID later on in the process
		ldapEnvironmentSettings.put("java.naming.ldap.attributes.binary", "objectGUID");
		
		ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL, Constants.AD_USERNAME);
//		ldapEnvironmentSettings.put(Context.SECURITY_PRINCIPAL, "cn="+Constants.AD_USERNAME+","+Constants.AD_TREE);
		ldapEnvironmentSettings.put(Context.SECURITY_CREDENTIALS, Constants.AD_PASSWORD);
		return new InitialDirContext(ldapEnvironmentSettings);
	}
}
