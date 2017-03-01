package services;

import static services.ad.ADOperations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;
import services.mappers.EmployeeProfileMapper;
import utils.Sequence;
import utils.SequenceException;

public class BulkUpdateService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateService.class);
  
  // Sopra AD Details
  private static final String AD_SOPRA_UK_TREE = "ou=uk,ou=users,ou=sopragroup,ou=usersemea,DC=emea,DC=msad,DC=sopra";
  
  // Steria AD Details
  private static final String AD_STERIA_UK_TREE = "ou=UK,ou=Internal,ou=People,DC=one,DC=steria,DC=dom";
  
  private final Datastore dbConnection; 
  private final ADSearchSettings sopraADSearchSettings;
  private final ADSearchSettings steriaADSearchSettings;
  private final Sequence<String> steriaFilterSequence;
  
  public BulkUpdateService(final Datastore dbConnection, final ADSearchSettings sopraADSearchSettings, final ADSearchSettings steriaADSearchSettings, final Sequence<String> steriaFilterSequence)
  {
    this.dbConnection = dbConnection;
    this.sopraADSearchSettings = sopraADSearchSettings;
    this.steriaADSearchSettings = steriaADSearchSettings;
    this.steriaFilterSequence = steriaFilterSequence;
  }
  
  public void syncDBWithADs() throws ADConnectionException, NamingException, SequenceException
  {
    List<EmployeeProfile> allEmployeeProfiles = fetchAllEmployeeProfiles();
    
  }
  
  /**
   * Fetches a list of all Sopra Steria UK employees.
   *
   * @return The list of all Sopra Steria employees.
   * @throws ADConnectionException if there was a problem connecting to the active
   * directory or performing the search.
   * @throws NamingException 
   * @throws SequenceException 
   */
  public List<EmployeeProfile> fetchAllEmployeeProfiles() throws ADConnectionException, NamingException, SequenceException
  {
    // There are approximately 7800 employees, hence initial capacity of 10,000 
    final List<EmployeeProfile> allEmployeeProfiles = new ArrayList<>(10_000);
    final String sopraFilter = "extensionAttribute7=*";
    final List<SearchResult> sopraList = searchADAsList(sopraADSearchSettings, AD_SOPRA_UK_TREE, sopraFilter);
    final List<SearchResult> steriaList = searchAD(steriaADSearchSettings, AD_STERIA_UK_TREE, steriaFilterSequence);
    final Map<SearchResult, Optional<SearchResult>> pairedResults = SearchResultsMappingService.getResultsPairs(sopraList, steriaList);
    
    for (Map.Entry<SearchResult, Optional<SearchResult>> entry : pairedResults.entrySet())
    {
      try
      {
        EmployeeProfile profile = new EmployeeProfileMapper().apply(Optional.of(entry.getKey()), entry.getValue());
        allEmployeeProfiles.add(profile);
      }
      catch (NoSuchElementException | NullPointerException e)
      {
        e.printStackTrace();
      }
      
    }
    
    LOGGER.info("Sopra list size: " + sopraList.size());
    LOGGER.info("Steria list size: " + steriaList.size());
    LOGGER.info("Paired results size: " + pairedResults.size());
    LOGGER.info("EmployeeProfiles generated: " + allEmployeeProfiles.size());
    
    // Some metadata.  TODO don't think this belongs here
    LOGGER.info("With company: " + allEmployeeProfiles.stream()
        .filter(e -> e.getCompany() != null)
        .count());
    LOGGER.info("With email address: " + allEmployeeProfiles.stream()
        .filter(e -> e.getEmailAddress() != null)
        .count());
    LOGGER.info("With employee ID: " + allEmployeeProfiles.stream()
        .filter(e -> e.getEmployeeID() > 0)
        .count());
    LOGGER.info("With forename: " + allEmployeeProfiles.stream()
        .filter(e -> e.getForename() != null)
        .count());
    LOGGER.info("With GUID: " + allEmployeeProfiles.stream()
        .filter(e -> e.getGUID() != null)
        .count());
    LOGGER.info("With Sopra department: " + allEmployeeProfiles.stream()
        .filter(e -> e.getSopraDepartment() != null)
        .count());
    LOGGER.info("With surname: " + allEmployeeProfiles.stream()
        .filter(e -> e.getSurname() != null)
        .count());
    LOGGER.info("With username: " + allEmployeeProfiles.stream()
        .filter(e -> e.getUsername() != null)
        .count());
    LOGGER.info("With Steria department: " + allEmployeeProfiles.stream()
        .filter(e -> e.getSteriaDepartment() != null)
        .count());
    LOGGER.info("With sector: " + allEmployeeProfiles.stream()
        .filter(e -> e.getSector() != null)
        .count());
    LOGGER.info("With super sector: " + allEmployeeProfiles.stream()
        .filter(e -> e.getSuperSector() != null)
        .count());
    LOGGER.info("With reportees: " + allEmployeeProfiles.stream()
        .filter(e -> e.getReporteeCNs() != null)
        .count());
    
    
    return allEmployeeProfiles;
  }
}
