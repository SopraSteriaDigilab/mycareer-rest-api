package services;

import static services.ad.ADOperations.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import dataStructure.EmployeeProfile;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;
import services.mappers.EmployeeProfileMapper;
import services.mappers.InvalidEmployeeProfileException;
import utils.sequence.Sequence;
import utils.sequence.SequenceException;

public class BulkUpdateService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateService.class);
  
  // Steria AD Details
  private static final String AD_STERIA_UK_TREE = "ou=UK,ou=Internal,ou=People,DC=one,DC=steria,DC=dom";
  
  private final EmployeeService employeeService;
  private final ADSearchSettings steriaADSearchSettings;
  private final Sequence<String> steriaFilterSequence;
  
  public BulkUpdateService(final EmployeeService employeeService, final ADSearchSettings steriaADSearchSettings, final Sequence<String> steriaFilterSequence)
  {
    this.employeeService = employeeService;
    this.steriaADSearchSettings = steriaADSearchSettings;
    this.steriaFilterSequence = steriaFilterSequence;
  }
  
  @Scheduled(cron = "0 30 23 * * ?")
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
         * besides, we are concerned with the hundreds, not the one 
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
    // There are approximately 6,100 employees, hence initial capacity of 10,000 
    final List<EmployeeProfile> allEmployeeProfiles = new ArrayList<>(10_000);
    final List<SearchResult> steriaList = searchAD(steriaADSearchSettings, AD_STERIA_UK_TREE, steriaFilterSequence);
    
    for (final SearchResult result : steriaList)
    {
      try
      {
        EmployeeProfile profile = new EmployeeProfileMapper().apply(Optional.empty(), Optional.ofNullable(result));
        allEmployeeProfiles.add(profile);
      }
      catch (InvalidEmployeeProfileException e)
      {
        LOGGER.warn(e.getMessage());
      }
      catch (NoSuchElementException | NullPointerException e)
      {
        LOGGER.error("Exception occurred: ", e);
      }
    }
    
    logMetadata(steriaList, allEmployeeProfiles);
    
    return allEmployeeProfiles;
  }

  private void logMetadata(final List<SearchResult> resultsList, final List<EmployeeProfile> allEmployeeProfiles)
  {
    // Some metadata.  
    // TODO don't think this belongs here
    
    LOGGER.info("Steria list size: " + resultsList.size());
    LOGGER.info("EmployeeProfiles generated: " + allEmployeeProfiles.size());
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
  }
}
