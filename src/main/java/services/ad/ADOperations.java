package services.ad;

import static services.ad.ADOperations.searchAD;
import static utils.Conversions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Sequence;
import utils.SequenceException;

public class ADOperations
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ADOperations.class);
  
  private ADOperations() {}
  
  public static NamingEnumeration<SearchResult> searchAD(final ADSearchSettings adSearchSettings, final String tree, final String filter) throws ADConnectionException
  {
    NamingEnumeration<SearchResult> result = null;
    
    try (final ADConnection connection = new ADConnectionImpl(adSearchSettings))
    {
      result = connection.searchAD(tree, filter);
    }
    catch (NamingException e)
    {
      LOGGER.error("Exception while searching Sopra Active Directory", e);
      throw new ADConnectionException(e);
    }
    
    return result;
  }
  
  public static List<SearchResult> searchADAsList(final ADSearchSettings adSearchSettings, final String tree, final String filter) throws ADConnectionException, NamingException
  {
    return namingEnumToList(searchAD(adSearchSettings, tree, filter));
  }
  
  public static List<SearchResult> searchAD(ADSearchSettings adSearchSettings, String tree, Sequence<String> filterSequence) throws ADConnectionException, NamingException, SequenceException
  {
    final List<SearchResult> finalResult = new ArrayList<>(); 
    int letterCount = 0;
    
    while (filterSequence.hasNext())
    {
      String filter = filterSequence.next();
      NamingEnumeration<SearchResult> result = searchAD(adSearchSettings, tree, filter);
      
      while (result.hasMore())
      {
        letterCount++;
        finalResult.add(result.next());
      }
      
      LOGGER.info("{" + filter + ": " + letterCount + "}");
      
      letterCount = 0;
    }
    
    return finalResult;
  }
}
