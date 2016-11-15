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

public class ADProfileDAO {

	public static DirContext ldapContext;

	@SuppressWarnings("unchecked")
	public static ADProfile_Basic authenticateUserProfile(String username) throws NamingException, InvalidAttributeValueException {

		//Verify the given string
		if(username==null || username.equals("") || username.length()<1)
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
		String searchFilter = "(sAMAccountName=" + username + ")";
		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = ldapContext.search(Constants.AD_SERVERS, searchFilter, searchCtls);

		//Check the results retrieved
		if(answer.hasMoreElements()){
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();
			ADProfile_Advanced adObj=new ADProfile_Advanced();
			
			adObj.setEmployeeID(Long.parseLong((String)attrs.get("employeeID").get()));
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
			//System.out.println(adObj.toString());
			//Close the connection with the AD
			ldapContext.close();
			ldapContext=null;
			//return new ADProfile_Basic(employeeID, displayName, company, sAMAccountName, team);
			//return null;
			return EmployeeDAO.matchADWithMongoData(adObj);
		//There is no a matching user in the Active Directory
		}else{
			//Close the connection with the AD
			ldapContext.close();
			ldapContext=null;
			throw new InvalidAttributeValueException("No match in the AD for user "+username);
		}
	}

	private static DirContext getADConnection() throws NamingException{

		Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
		ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ldapEnv.put(Context.PROVIDER_URL,  Constants.AD_HOST+":"+Constants.AD_PORT);
		ldapEnv.put(Context.SECURITY_AUTHENTICATION, Constants.AD_AUTHENTICATION);
		//This is essential in order to retrieve the GUID later on in the process
		ldapEnv.put("java.naming.ldap.attributes.binary", "objectGUID"); 
		ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn="+Constants.AD_USERNAME+","+Constants.AD_SERVERS);
		ldapEnv.put(Context.SECURITY_CREDENTIALS, Constants.AD_PASSWORD);
		return new InitialDirContext(ldapEnv);
	}
}
