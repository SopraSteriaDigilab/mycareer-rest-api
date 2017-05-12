
//  TODO Commented code to be reviewed
  
//package services.ad;
//
//import static org.mockito.MockitoAnnotations.*;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//import java.util.Hashtable;
//import java.util.NoSuchElementException;
//
//import javax.naming.NamingEnumeration;
//import javax.naming.NamingException;
//import javax.naming.directory.Attributes;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.SearchControls;
//import javax.naming.directory.SearchResult;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ADConnectionTest
//{
//  /**
//   * Logger Property - Represents an implementation of the Logger interface that may be used here.
//   */
//  private static final Logger LOGGER = LoggerFactory.getLogger(ADConnectionTest.class);
//
//  /** Environment settings dependency, mocked by Mockito */
//  @Mock
//  private Hashtable<String, String> ldapEnvironmentSettings;
//
//  /** DirContext property used to connect to an AD, mocked by Mockito */
//  @Mock
//  private DirContext connection;
//
//  /** Search Controls dependency, mocked by Mockito */
//  @Mock
//  private SearchControls searchCtls;
//
//  /** Attributes dependency, mocked by Mockito */
//  @Mock
//  private Attributes attributes;
//
//  /** Search result dependency, mocked by Mockito */
//  @Mock
//  private SearchResult results;
//
//  /** NamingEnumeration dependency, holds the results from the AD search, mocked by Mockito */
//  @Mock
//  private NamingEnumeration<SearchResult> answer;
//
//  /** ADConnection Property - Represents the unit under test. */
//  @InjectMocks
//  private ADConnectionImpl unitUnderTest;
//
//  private static final String AD_SOPRA_TREE = "ou=usersemea,DC=emea,DC=msad,DC=sopra";
//  private static final String[] AD_SOPRA_ATTRIBUTES = { "sn", "givenName", "company", "sAMAccountName",
//      "extensionAttribute7", "objectGUID", "mail", "department", "targetAddress", "memberOf" };
//  private final String searchFilter = "cn=mmehmet";
//
//  @Before
//  public void setup() throws NamingException
//  {
//    initMocks(this);
//    unitUnderTest = new ADConnectionImpl(ldapEnvironmentSettings, connection);
//  }
//
//  @Test
//  public void searchADSuccessTest() throws ADConnectionException, NamingException
//  {
//    when(connection.search(anyString(), anyString(), eq(searchCtls))).thenReturn(answer);
//    when(answer.next()).thenReturn(results);
//    when(results.getAttributes()).thenReturn(attributes);
//    when(answer.hasMoreElements()).thenReturn(false);
//
//    final Attributes testAttributes = unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE,
//        searchFilter);
//
//    verify(connection).search(anyString(), anyString(), eq(searchCtls));
//    verify(answer).next();
//    verify(answer).hasMoreElements();
//    verify(results).getAttributes();
//    assertEquals(testAttributes, attributes);
//  }
//
//  @Test(expected = ADConnectionException.class)
//  public void searchADADConnectionException() throws ADConnectionException, NamingException
//  {
//    when(connection.search(anyString(), anyString(), eq(searchCtls))).thenThrow(new NamingException());
//    when(answer.next()).thenReturn(results);
//    when(results.getAttributes()).thenReturn(attributes);
//    when(answer.hasMoreElements()).thenReturn(false);
//
//    final Attributes testAttributes = unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE,
//        searchFilter);
//
//    verify(connection).search(anyString(), anyString(), eq(searchCtls));
//    verify(answer, never()).next();
//    verify(answer, never()).hasMoreElements();
//    verify(results, never()).getAttributes();
//    assertNull(testAttributes);
//  }
//
//  @Test(expected = NoSuchElementException.class)
//  public void searchADNoMatches() throws ADConnectionException, NamingException
//  {
//    when(connection.search(anyString(), anyString(), eq(searchCtls))).thenReturn(answer);
//    when(answer.next()).thenThrow(new NoSuchElementException());
//    when(results.getAttributes()).thenReturn(attributes);
//    when(answer.hasMoreElements()).thenReturn(false);
//
//    final Attributes testAttributes = unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE,
//        searchFilter);
//    
//    verify(connection).search(anyString(), anyString(), eq(searchCtls));
//    verify(answer).next();
//    verify(answer, never()).hasMoreElements();
//    verify(results, never()).getAttributes();
//    assertNull(testAttributes);
//  }
//
//  @Test(expected = NamingException.class)
//  public void searchADCannotRetrieveResults() throws ADConnectionException, NamingException
//  {
//    when(connection.search(anyString(), anyString(), eq(searchCtls))).thenReturn(answer);
//    when(answer.next()).thenThrow(new NamingException());
//    when(results.getAttributes()).thenReturn(attributes);
//    when(answer.hasMoreElements()).thenReturn(false);
//
//    final Attributes testAttributes = unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE,
//        searchFilter);
//    
//    verify(connection).search(anyString(), anyString(), eq(searchCtls));
//    verify(answer).next();
//    verify(answer, never()).hasMoreElements();
//    verify(results, never()).getAttributes();
//    assertNull(testAttributes);
//  }
//
//  @Test()
//  public void searchADTooManyMatches() throws ADConnectionException, NamingException
//  {
//    when(connection.search(anyString(), anyString(), eq(searchCtls))).thenReturn(answer);
//    when(answer.next()).thenReturn(results);
//    when(results.getAttributes()).thenReturn(attributes);
//    when(answer.hasMoreElements()).thenReturn(true);
//
//    final Attributes testAttributes = unitUnderTest.searchAD(searchCtls, AD_SOPRA_ATTRIBUTES, AD_SOPRA_TREE,
//        searchFilter);
//
//    verify(connection).search(anyString(), anyString(), eq(searchCtls));
//    verify(answer).next();
//    verify(answer).hasMoreElements();
//    verify(results).getAttributes();
//    // check that a log message was written?
//    assertEquals(testAttributes, attributes);
//  }
//
//  @Test
//  public void closeConnectionSuccess() throws NamingException
//  {
//    unitUnderTest.close();
//
//    verify(connection).close();
//  }
//
//  @Test
//  public void closeConnectionNamingException() throws NamingException
//  {
//    doThrow(NamingException.class).when(connection).close();
//    unitUnderTest.close();
//
//    verify(connection).close();
//    // check that a log message was written?
//  }
//
//  @Test
//  public void closeConnectionNullPointerException() throws NamingException
//  {
//    doThrow(NullPointerException.class).when(connection).close();
//    unitUnderTest.close();
//
//    verify(connection).close();
//    // check that a log message was written?
//  }
//}
