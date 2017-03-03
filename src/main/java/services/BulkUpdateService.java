package services;

import static services.ad.ADOperations.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;
import services.mappers.EmployeeProfileMapper;
import utils.sequence.Sequence;
import utils.sequence.SequenceException;

public class BulkUpdateService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateService.class);
  
  // Sopra AD Details
  private static final String AD_SOPRA_UK_TREE = "ou=uk,ou=users,ou=sopragroup,ou=usersemea,DC=emea,DC=msad,DC=sopra";
  
  // Steria AD Details
  private static final String AD_STERIA_UK_TREE = "ou=UK,ou=Internal,ou=People,DC=one,DC=steria,DC=dom";
  
  private final EmployeeService employeeService;
  private final ADSearchSettings sopraADSearchSettings;
  private final ADSearchSettings steriaADSearchSettings;
  private final Sequence<String> steriaFilterSequence;
  
  public BulkUpdateService(final EmployeeService employeeService, final ADSearchSettings sopraADSearchSettings, final ADSearchSettings steriaADSearchSettings, final Sequence<String> steriaFilterSequence)
  {
    this.employeeService = employeeService;
    this.sopraADSearchSettings = sopraADSearchSettings;
    this.steriaADSearchSettings = steriaADSearchSettings;
    this.steriaFilterSequence = steriaFilterSequence;
  }
  
  public int syncDBWithADs() throws ADConnectionException, NamingException, SequenceException
  {
    final Instant startADOps = Instant.now();
    final List<EmployeeProfile> allEmployeeProfiles = fetchAllEmployeeProfiles();
    final Instant endADOps = Instant.now();
    
    final Instant startDBOps = Instant.now();
    int updatedCount = 0;
    int notUpdatedCount = 0;
    
    for (EmployeeProfile profile : allEmployeeProfiles)
    {
      try
      {
        employeeService.matchADWithMongoData(profile);
        updatedCount++;
      }
      catch (InvalidAttributeValueException e)
      {
        /* swallow this exception as matchADWithMongoData already logs it 
         * we are concerned with the hundreds, not the one 
         */
        notUpdatedCount++;
      }
      catch (Exception e)
      {
        LOGGER.warn("Bulk update error: " + e.getMessage());
        notUpdatedCount++;
      }
    }
    
    final Instant endDBOps = Instant.now();
    
    final Duration adOpsTime = Duration.between(startADOps, endADOps);
    final Duration dbOpsTime = Duration.between(startDBOps, endDBOps);
    final Duration totalOpsTime = adOpsTime.plus(dbOpsTime);
    
    LOGGER.info("Updated: " + updatedCount);
    LOGGER.info("Not updated: " + notUpdatedCount);
    LOGGER.info("AD Operations time: " + adOpsTime);
    LOGGER.info("DB Operations time: " + dbOpsTime);
    LOGGER.info("Total time to sync: " + totalOpsTime);
    
    return updatedCount;
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
        .filter(e -> !e.getCompany().isEmpty())
        .count());
    LOGGER.info("With email address: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getEmailAddress().isEmpty())
        .count());
    LOGGER.info("With employee ID: " + allEmployeeProfiles.stream()
        .filter(e -> e.getEmployeeID() > 0)
        .count());
    LOGGER.info("With forename: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getForename().isEmpty())
        .count());
    LOGGER.info("With GUID: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getGUID().isEmpty())
        .count());
    LOGGER.info("With Sopra department: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getSopraDepartment().isEmpty())
        .count());
    LOGGER.info("With surname: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getSurname().isEmpty())
        .count());
    LOGGER.info("With username: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getUsername().isEmpty())
        .count());
    LOGGER.info("With Steria department: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getSteriaDepartment().isEmpty())
        .count());
    LOGGER.info("With sector: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getSector().isEmpty())
        .count());
    LOGGER.info("With super sector: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getSuperSector().isEmpty())
        .count());
    LOGGER.info("With reportees: " + allEmployeeProfiles.stream()
        .filter(e -> !e.getReporteeCNs().isEmpty())
        .count());
    
    
    return allEmployeeProfiles;
  }
}
