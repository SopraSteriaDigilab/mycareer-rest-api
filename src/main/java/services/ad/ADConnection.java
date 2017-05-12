package services.ad;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

public interface ADConnection extends AutoCloseable
{
  NamingEnumeration<SearchResult> searchAD(final String searchTree, final String searchFilter) throws ADConnectionException, NamingException;
  
  @Override
  public void close();
}
