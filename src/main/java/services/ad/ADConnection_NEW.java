package services.ad;

import static javax.naming.directory.SearchControls.*;

import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.AppController;

public class ADConnection_NEW implements AutoCloseable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ADConnection_NEW.class);
	
	private DirContext connection;
	private final Hashtable<String, String> ldapEnvironmentSettings;
	
	public ADConnection_NEW(final Hashtable<String, String> ldapEnvironmentSettings) {
		this.ldapEnvironmentSettings = ldapEnvironmentSettings;
	}

	public Attributes searchAD(final String[] returningAttributes, final String searchTree, final String searchFilter) throws NamingException {
		Attributes attributes = null;
		final NamingEnumeration<SearchResult> answer;
		final SearchControls searchCtls = new SearchControls();
		
		searchCtls.setReturningAttributes(returningAttributes);
		searchCtls.setSearchScope(SUBTREE_SCOPE);
		establishConnection();
		answer = connection.search(searchTree, searchFilter, searchCtls);
		attributes = answer.next().getAttributes();
		
		if (answer.hasMoreElements()) {
			// handle situation where more than one match was found in the AD
		}
		
		return attributes;
	}
	
	private void establishConnection() throws NamingException {
		if (connection != null) { return; }
		
		try {
			connection = new InitialDirContext(ldapEnvironmentSettings);
		} catch (final RuntimeException re) {
			LOGGER.error("Unable to connect to AD", re);
		}
	}

	@Override
	public void close() throws NamingException {
		try {
			connection.close();
		} catch (NamingException | NullPointerException e) {
			LOGGER.info(e.getStackTrace().toString());
		}
	}
}
