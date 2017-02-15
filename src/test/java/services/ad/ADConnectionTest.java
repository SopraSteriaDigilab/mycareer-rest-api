package services.ad;

import static org.mockito.MockitoAnnotations.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Hashtable;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.Constants;
import services.EmployeeProfileDAO;

public class ADConnectionTest {
	/**
	 * Logger Property - Represents an implementation of the Logger interface
	 * that may be used here.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ADConnectionTest.class);
	
	/** Environment settings dependency, mocked by Mockito */
	@Mock
	private Hashtable<String, String> ldapEnvironmentSettings;
	
	/** DirContext property used to connect to an AD, mocked by Mockito */
	@Mock
	private DirContext connection;
	
	/** Search Controls dependency, mocked by Mockito */
	@Mock
	private SearchControls searchCtls;
	
	/** Attributes dependency, mocked by Mockito */
	@Mock
	private Attributes attributes;
	
	/** Search result dependency, mocked by Mockito */
	@Mock
	private SearchResult results;
	
	/** NamingEnumeration dependency, holds the results from the AD search, mocked by Mockito */
	@Mock
	private NamingEnumeration<SearchResult> answer;
	
	
	/** ADConnection Property - Represents the unit under test. */
	@InjectMocks
	private ADConnection unitUnderTest;
	
	private static final String AD_SOPRA_TREE = "ou=usersemea,DC=emea,DC=msad,DC=sopra";
	private static final String[] AD_SOPRA_ATTRIBUTES={"sn","givenName","company", "sAMAccountName", "extensionAttribute7", "objectGUID", "mail", "department", "targetAddress", "memberOf"};
	private static final String TOO_MANY_RESULTS = "More than one match was found in the Active Directory";
	private static final String CONNECTION_ERROR = "Unable to connect to the Active Directory: ";
	private final String searchFilter = "cn=mmehmet";
	
	@Before
	public void setup() throws NamingException {
		initMocks(this);
		unitUnderTest = new ADConnection(ldapEnvironmentSettings, connection);
	}
	
	@Test
	public void searchADSuccessTest() throws NamingException {
		when(connection.search(anyString(), anyString(), eq(searchCtls))).thenReturn(answer);
		when(answer.next()).thenReturn(results);
		when(results.getAttributes()).thenReturn(attributes);
		when(answer.hasMoreElements()).thenReturn(false);
		
		final Attributes testAttributes = 
				unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE, searchFilter);
		
		verify(connection, times(1)).search(anyString(), anyString(), eq(searchCtls));
		verify(answer, times(1)).next();
		verify(answer, times(1)).hasMoreElements();
		verify(results, times(1)).getAttributes();
		assertEquals(testAttributes, attributes);
	}
	
	@Test(expected = NamingException.class)
	public void searchADNamingException() throws NamingException {
		when(connection.search(anyString(), anyString(), eq(searchCtls))).thenThrow(new NamingException());
		when(answer.next()).thenReturn(results);
		when(results.getAttributes()).thenReturn(attributes);
		when(answer.hasMoreElements()).thenReturn(false);
		
		final Attributes testAttributes = 
				unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE, searchFilter);
		
		verify(connection, times(1)).search(anyString(), anyString(), eq(searchCtls));
		verify(answer, never()).next();
		verify(answer, never()).hasMoreElements();
		verify(results, never()).getAttributes();
	}
	
	@Test(expected = NamingException.class)
	public void searchADNoMatches() throws NamingException {
		when(connection.search(anyString(), anyString(), eq(searchCtls))).thenThrow(new NamingException());
		when(answer.next()).thenThrow(new NamingException());
		when(results.getAttributes()).thenReturn(attributes);
		when(answer.hasMoreElements()).thenReturn(false);
		
		final Attributes testAttributes = 
				unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE, searchFilter);
		
		verify(connection, times(1)).search(anyString(), anyString(), eq(searchCtls));
		verify(answer, times(1)).next();
		verify(answer, never()).hasMoreElements();
		verify(results, never()).getAttributes();
	}
	
	@Test()
	public void searchADTooManyMatches() throws NamingException {
		when(connection.search(anyString(), anyString(), eq(searchCtls))).thenReturn(answer);
		when(answer.next()).thenReturn(results);
		when(results.getAttributes()).thenReturn(attributes);
		when(answer.hasMoreElements()).thenReturn(true);
		
		final Attributes testAttributes = 
				unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE, searchFilter);
		
		verify(connection, times(1)).search(anyString(), anyString(), eq(searchCtls));
		verify(answer, times(1)).next();
		verify(answer, times(1)).hasMoreElements();
		verify(results, times(1)).getAttributes();
//		check that a log message was written?
		assertEquals(testAttributes, attributes);
	}
	
	@Test
	public void closeConnectionSuccess() throws NamingException {
		unitUnderTest.close();
		
		verify(connection, times(1)).close();
	}
	
	@Test
	public void closeConnectionNamingException() throws NamingException {
		doThrow(NamingException.class).when(connection).close();
		unitUnderTest.close();
		
		verify(connection, times(1)).close();
//		check that a log message was written?
	}
	
	@Test
	public void closeConnectionNullPointerException() throws NamingException {
		doThrow(NullPointerException.class).when(connection).close();
		unitUnderTest.close();
		
		verify(connection, times(1)).close();
//		check that a log message was written?
	}
}
