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

import dataStructure.ADProfile;

public class ADProfileDAO {
	
	public static DirContext ldapContext;
	
	public static ADProfile authenticateUserProfile(String userName) throws NamingException, InvalidAttributeValueException {
		
		ldapContext = getADConnection();
		
	    // Create the search controls         
		SearchControls searchCtls = new SearchControls();
	    
	    //Specify the attributes to return
		String returnedAtts[]={"displayName","company", "sAMAccountName", "employeeID"};
	    searchCtls.setReturningAttributes(returnedAtts);
	
	    //Specify the search scope
	    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

	    //specify the LDAP search filter
	    String searchFilter = "(sAMAccountName=" + userName + ")";
	
	    //Specify the Base for the search
	    String searchBase = "OU=UK,OU=Internal,OU=People,DC=one,DC=steria,DC=dom";
	
	    // Search for objects using the filter
	    NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);
	    
	    if(answer.hasMoreElements()){
	        SearchResult sr = (SearchResult)answer.next();
//	        System.out.println(sr.getName());
	        
	        Attributes attrs = sr.getAttributes();
	        String displayName = (String) attrs.get("displayName").get();
	        String company = (String) attrs.get("company").get();
	        String sAMAccountName = (String) attrs.get("sAMAccountName").get();
	        long employeeID = Long.parseLong((String)attrs.get("employeeID").get());
	        String team = "603 GOVERNMENT UK";
	        
	        ldapContext.close();
	        return new ADProfile(employeeID, displayName, company, sAMAccountName,  team);
		  
	    }else{
	    	ldapContext.close();
	    	throw new InvalidAttributeValueException("No user in AD");
	    }
		
	}
	
	
	
	
	
	
	
	
	  private static DirContext getADConnection() throws NamingException{
		  
	      Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
	      ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	      ldapEnv.put(Context.PROVIDER_URL,  "ldap://one.steria.dom:389");
	      ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
	      ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn=NACEF Ridhwan - 675590,OU=UK,OU=Internal,OU=People,DC=one,DC=steria,DC=dom");
	      ldapEnv.put(Context.SECURITY_CREDENTIALS, "");

	      return new InitialDirContext(ldapEnv);
		  
	  }

}
