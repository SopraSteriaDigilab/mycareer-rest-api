package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Utils;

public class SearchResultsMappingService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsMappingService.class);
  
  private SearchResultsMappingService() {}
  
  public static Map<SearchResult, Optional<SearchResult>> getResultsPairs(final List<SearchResult> sopraResults, final List<SearchResult> steriaResults) throws NamingException
  {
    final Map<String, SearchResult> steriaMap = steriaResults.parallelStream()
                                                .filter(s -> s != null)
                                                .filter(s -> s.getAttributes().get("extensionAttribute2") != null)
                                                .filter(s -> Utils.getAttribute(s, "extensionAttribute2") != null)
                                                .collect(Collectors.toConcurrentMap(
                                                    s -> Utils.getAttribute(s, "extensionAttribute2"),
                                                    s -> s
                                                    ));
    
    final Map<SearchResult, Optional<SearchResult>> pairedResults = sopraResults.parallelStream()
                                                .filter(s -> s != null)
                                                .filter(s -> s.getAttributes().get("extensionAttribute7") != null)
                                                .filter(s -> Utils.getAttribute(s, "extensionAttribute7") != null)
                                                .collect(Collectors.toConcurrentMap(
                                                    s -> s,
                                                    s -> Optional.ofNullable(steriaMap.get(Utils.getAttribute(s, "extensionAttribute7")))
                                                    ));
    
    return pairedResults;
  }
}
