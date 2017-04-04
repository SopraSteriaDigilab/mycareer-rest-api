package services.ad;

import static utils.Conversions.*;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.sequence.Sequence;
import utils.sequence.SequenceException;

/**
 * Utility class with methods to aid in accessing information from Active Directories.
 *
 */
public final class ADOperations
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ADOperations.class);

  private static final String TOO_MANY_RESULTS = "A single result was expected but multiple were found";
  private static final String UNKNOWN_ERROR = "An exception was caught: ";

  /* This is a stateless class with statics only methods, therefore should never be instantiated. */
  private ADOperations()
  {
  }

  /**
   * Searches an active directory for a single match.
   *
   * @param adSearchSettings the search environment for this search
   * @param tree the group and subgroups to search in
   * @param filter the String to search for
   * @return the result of this search
   * @throws ADConnectionException if the number of results returned by this search was not equal to one or if the
   *           result could not be returned for an unknown reason
   */
  public static SearchResult searchADSingleResult(final ADSearchSettings adSearchSettings, final String tree,
      final String filter) throws ADConnectionException
  {
    SearchResult result = null;
    NamingEnumeration<SearchResult> allResults = searchAD(adSearchSettings, tree, filter);

    try
    {
      result = allResults.next();
    }
    catch (final NamingException | NullPointerException e)
    {
      LOGGER.error(UNKNOWN_ERROR, e);
      throw new ADConnectionException(UNKNOWN_ERROR, e);
    }

    if (allResults.hasMoreElements())
    {
      throw new ADConnectionException(TOO_MANY_RESULTS);
    }

    return result;
  }

  /**
   * Searches the active directory using the given settings and criteria.
   *
   * @param adSearchSettings the search environment for this search
   * @param tree the group and subgroups to search in
   * @param filter the String to search for
   * @return the NamingEnumeration containing all results of this search
   * @throws ADConnectionException if the results could not be returned for an unknown reason
   */
  public static NamingEnumeration<SearchResult> searchAD(final ADSearchSettings adSearchSettings, final String tree,
      final String filter) throws ADConnectionException
  {
    NamingEnumeration<SearchResult> result = null;

    try (final ADConnection connection = new ADConnectionImpl(adSearchSettings))
    {
      result = connection.searchAD(tree, filter);
    }
    catch (final NamingException e)
    {
      LOGGER.error(UNKNOWN_ERROR, e);
      throw new ADConnectionException(UNKNOWN_ERROR, e);
    }

    return result;
  }

  /**
   * Searches the active directory using the given settings and criteria, returning the results in a {@code List}.
   *
   * @param adSearchSettings the search environment for this search
   * @param tree the group and subgroups to search in
   * @param filter the String to search for
   * @return the List containing all results of this search
   * @throws ADConnectionException if the results could not be returned for an unknown reason
   * @throws NamingException if there was a problem adding the results to a {@code List}
   */
  public static List<SearchResult> searchADAsList(final ADSearchSettings adSearchSettings, final String tree,
      final String filter) throws ADConnectionException, NamingException
  {
    return namingEnumToList(searchAD(adSearchSettings, tree, filter));
  }

  /**
   * Performs a series of searches of an active directory using the given settings and criteria, and sequence of search
   * filters.
   *
   * @param adSearchSettings the search environment for this search
   * @param tree the group and subgroups to search in
   * @param filterSequence the sequence of filters to apply
   * @return
   * @throws ADConnectionException if the results could not be returned
   * @throws NamingException if there was a problem adding the results to a List
   * @throws SequenceException if there was a problem with the given sequence
   */
  // TODO: The logging in this method is very messy!
  public static List<SearchResult> searchAD(ADSearchSettings adSearchSettings, String tree,
      Sequence<String> filterSequence) throws ADConnectionException, NamingException, SequenceException
  {
    final List<SearchResult> finalResult = new ArrayList<>();
    StringBuilder out = new StringBuilder("{");
    boolean stringBuilderFlag = false;
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
      
      if (stringBuilderFlag)
      {
        out.append(", ");
      }
      
      out.append(filter).append(": ").append(letterCount);

      letterCount = 0;
      stringBuilderFlag = true;
    }
    
    out.append("}");
    LOGGER.info(out.toString());

    return finalResult;
  }
}
