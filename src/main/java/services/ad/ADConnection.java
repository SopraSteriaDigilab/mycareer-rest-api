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

public class ADConnection implements AutoCloseable
{

  private static final Logger LOGGER = LoggerFactory.getLogger(ADConnection.class);
  private static final String TOO_MANY_RESULTS = "More than one match was found in the Active Directory";
  private static final String SEARCH_EXCEPTION_MSG = "Exception occurred while trying to search the active directory: ";
  private static final String CONNECTION_ERROR = "Unable to connect to the Active Directory: ";

  private DirContext connection;
  private final Hashtable<String, String> ldapEnvironmentSettings;

  public ADConnection(final Hashtable<String, String> ldapEnvironmentSettings)
  {
    this.ldapEnvironmentSettings = ldapEnvironmentSettings;
  }

  /**
   * Constructor to be used for testing purposes only.
   * 
   * @param ldapEnvironmentSettings
   * @param connection
   */
  public ADConnection(final Hashtable<String, String> ldapEnvironmentSettings, final DirContext connection)
  {
    this(ldapEnvironmentSettings);
    this.connection = connection;
  }

  public Attributes searchAD(final SearchControls searchCtls, final String[] returningAttributes,
      final String searchTree, final String searchFilter) throws NamingException
  {
    Attributes attributes = null;
    final NamingEnumeration<SearchResult> answer;

    searchCtls.setReturningAttributes(returningAttributes);
    searchCtls.setSearchScope(SUBTREE_SCOPE);
    establishConnection();
    answer = search(searchTree, searchFilter, searchCtls);
    attributes = answer.next().getAttributes();

    if (answer.hasMoreElements())
    {
      LOGGER.warn(TOO_MANY_RESULTS);
    }

    return attributes;
  }

  private void establishConnection() throws NamingException
  {
    if (connection != null)
    {
      return;
    }

    try
    {
      connection = new InitialDirContext(ldapEnvironmentSettings);
    }
    catch (final RuntimeException re)
    {
      LOGGER.error(CONNECTION_ERROR.concat(ldapEnvironmentSettings.toString()), re);
    }
  }
  
  private NamingEnumeration<SearchResult> search(final String searchTree, final String searchFilter,
      final SearchControls searchCtls) throws NamingException
  {
    try
    {
      return connection.search(searchTree, searchFilter, searchCtls);
    }
    catch (final NamingException | RuntimeException ex)
    {
      LOGGER.error(SEARCH_EXCEPTION_MSG.concat(ldapEnvironmentSettings.toString()), ex);
      throw ex;
    }
  }

  @Override
  public void close()
  {
    try
    {
      connection.close();
    }
    catch (final NamingException | NullPointerException e)
    {
      LOGGER.info(e.getMessage());
    }
  }
}
