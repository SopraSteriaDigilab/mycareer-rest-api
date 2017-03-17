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

/**
 * Represents a connection to an active directory.
 * 
 * ADConnection instances are created with specific environment settings which may not
 * be changed once instantiated.  The connection is made in a lazy way, i.e. is only
 * instantiated after the first call to {@code searchAD()}.
 *
 */
public class ADConnectionImpl implements ADConnection
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ADConnectionImpl.class);

  private static final String CONNECTION_EXCEPTION_MSG = "Unable to connect to the Active Directory: ";
  private static final String SEARCH_EXCEPTION_MSG = "Exception occurred while trying to search the active directory: ";
  private static final String CLOSE_EXCEPTION_MSG = "Exception encountered while closing a connection to the active directory: ";

  private DirContext connection;
  private final ADSearchSettings adSearchSettings;
  
  /**
   * Constructs a new {@code ADConnection} using the given environment settings.
   *
   * @param ldapEnvironmentSettings The environment settings to be used with this
   * {@code ADConnection} instance.
   */
  public ADConnectionImpl(final ADSearchSettings adSearchSettings)
  {
    this.adSearchSettings = adSearchSettings;
  }

  /**
   * Constructor to be used for testing purposes only.
   * 
   * TODO: Move the test to this package in order to make this constructor package private?
   * 
   * @param ldapEnvironmentSettings
   * @param connection
   */
  public ADConnectionImpl(final ADSearchSettings adSearchSettings, final DirContext connection)
  {
    this(adSearchSettings);
    this.connection = connection;
  }
  
  /**
   * Performs a search of the active directory.
   *
   * @param searchCtls The search controls to use for the search (note that if returning
   * attributes and/or search scope are already set, they will be overridden by this method). 
   * @param returningAttributes The attributes to return from the search.
   * @param searchTree The directory/directories on the target AD to search.
   * @param searchFilter A search filter.
   * @return An {@code Attributes} instance containing the results of the search.
   * @throws ADConnectionException If an exception occurred while trying to connect to or
   * search the AD.
   * @throws NamingException If an exception occurred while attempting to access the first
   * result of the search.
   * @throws NoSuchElementException If the search produced no results.
   */
  @Override
  public NamingEnumeration<SearchResult> searchAD(final String searchTree, final String searchFilter) throws ADConnectionException, NamingException
  {
    final NamingEnumeration<SearchResult> result;
    
    establishConnection();
    result = search(searchTree, searchFilter, adSearchSettings.getSearchControls());

    return result;
  }

  private void establishConnection() throws ADConnectionException
  {
    if (connection != null)
    {
      return;
    }

    try
    {
      connection = new InitialDirContext(adSearchSettings.getEnvironmentSettings());
    }
    catch (final NamingException | RuntimeException ex)
    {
      LOGGER.error(CONNECTION_EXCEPTION_MSG.concat(adSearchSettings.getEnvironmentSettings().toString()), ex);
      throw new ADConnectionException(CONNECTION_EXCEPTION_MSG, ex);
    }
  }

  private NamingEnumeration<SearchResult> search(final String searchTree, final String searchFilter,
      final SearchControls searchCtls) throws ADConnectionException
  {
    try
    {
      return connection.search(searchTree, searchFilter, searchCtls);
    }
    catch (final NamingException | RuntimeException ex)
    {
      LOGGER.error(SEARCH_EXCEPTION_MSG.concat(adSearchSettings.getEnvironmentSettings().toString()), ex);
      throw new ADConnectionException(SEARCH_EXCEPTION_MSG, ex);
    }
  }

  @Override
  public void close()
  {
    try
    {
      connection.close();
    }
    catch (final NamingException | RuntimeException ex)
    {
      final String msg = ex.getMessage();
      LOGGER.warn(CLOSE_EXCEPTION_MSG + msg);
    }
  }
}
