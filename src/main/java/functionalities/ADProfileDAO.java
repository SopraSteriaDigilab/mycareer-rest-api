package functionalities;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class ADProfileDAO {
	
	public static DirContext ldapContext;
	
	public static void authenticateUserProfile(String userName) throws NamingException {
		
		ldapContext = getADConnection();
		
	    // Create the search controls         
		SearchControls searchCtls = new SearchControls();
	    
	    //Specify the attributes to return
		String returnedAtts[]={"sn","givenName", "sAMAccountName", "mail"};
	    searchCtls.setReturningAttributes(returnedAtts);
	
	    //Specify the search scope
	    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

	    //specify the LDAP search filter
	    String searchFilter = "(sAMAccountName=rnacef)";
	
	    //Specify the Base for the search
	    String searchBase = "OU=UK,OU=Internal,OU=People,DC=one,DC=steria,DC=dom";
	
	    // Search for objects using the filter
	    NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);
	    
	    if(answer.hasMoreElements()){
	        SearchResult sr = (SearchResult)answer.next();
	        System.out.println(sr.getName());
	        Attributes attrs = sr.getAttributes();
	        System.out.println(attrs.get("sAMAccountName"));
	        System.out.println(attrs.get("mail"));
	        System.out.println();
		  
	    }else{
	    	System.out.println("Sorry no users found");  
	    }

	    ldapContext.close();
		
	}
	
	
	
	
	
	
	
	
	  public static DirContext getADConnection() throws NamingException{
		  
	      Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
	      ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	      ldapEnv.put(Context.PROVIDER_URL,  "ldap://one.steria.dom:389");
	      ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
	      ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn=NACEF Ridhwan - 675590,OU=UK,OU=Internal,OU=People,DC=one,DC=steria,DC=dom");
	      ldapEnv.put(Context.SECURITY_CREDENTIALS, "A138TeFnC1");

	      return new InitialDirContext(ldapEnv);
		  
	  }

}
