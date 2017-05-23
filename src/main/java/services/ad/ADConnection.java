package services.ad;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import services.ad.ADSearchSettingsImpl.LdapPort;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public interface ADConnection extends AutoCloseable
{
  /**
   * 
   * TODO: Describe this method.
   *
   * @param searchTree
   * @param searchFilter
   * @return
   * @throws ADConnectionException
   * @throws NamingException
   */
  NamingEnumeration<SearchResult> searchAD(final String searchTree, final String searchFilter, final LdapPort ldapPort)
      throws ADConnectionException, NamingException;

  /**
   * 
   * Override of NAME method.
   *
   * TODO: Describe this method.
   *
   * @see java.lang.AutoCloseable#close()
   *
   */
  @Override
  public void close();
}
