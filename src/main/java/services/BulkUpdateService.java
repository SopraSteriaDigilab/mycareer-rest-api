package services;

import static services.ad.ADOperations.searchAD;
import static services.ad.ADOperations.searchADAsList;

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
import utils.sequence.StringSequence;

public class BulkUpdateService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateService.class);

  private static final String AD_TREE = "ou=UK,ou=Internal,ou=People,DC=one,DC=steria,DC=dom";
  private static final String AD_UNUSED_OBJECT_TREE = "OU=UK,OU=People,OU=Unused Objects,DC=one,DC=steria,DC=dom";

  private final EmployeeService employeeService;
  private final ADSearchSettings steriaADSearchSettings;

  public BulkUpdateService(final EmployeeService employeeService, final ADSearchSettings steriaADSearchSettings)
  {
    this.employeeService = employeeService;
    this.steriaADSearchSettings = steriaADSearchSettings;
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
      catch (final EmployeeNotFoundException | InvalidAttributeValueException e)
      {
        /*
         * swallow this exception as matchADWithMongoData already logs it besides, we are concerned with the hundreds,
         * not the one
         */
        notUpdatedCount++;
      }
      catch (Exception e)
      {
        LOGGER.warn("Bulk update error: {}", e.getMessage());
        notUpdatedCount++;
      }
    }

    final Instant endDBOps = Instant.now();

    logMetadata(startADOps, endADOps, startDBOps, endDBOps, updatedCount, notUpdatedCount);

    return updatedCount;
  }

  /**
   * Fetches a list of all Sopra Steria UK employees.
   *
   * @return The list of all Sopra Steria employees.
   * @throws ADConnectionException if there was a problem connecting to the active directory or performing the search.
   * @throws NamingException
   * @throws SequenceException
   */
  public List<EmployeeProfile> fetchAllEmployeeProfiles()
      throws ADConnectionException, NamingException, SequenceException
  {
    // There are approximately 6,200 employees, hence initial capacity of 10,000
    final List<EmployeeProfile> allEmployeeProfiles = new ArrayList<>(10_000);
    final List<SearchResult> steriaList = searchAD(steriaADSearchSettings, AD_TREE, steriaFilterSequence());
    steriaList.addAll(searchADAsList(steriaADSearchSettings, AD_UNUSED_OBJECT_TREE, "CN=*"));

    for (final SearchResult result : steriaList)
    {
      try
      {
        EmployeeProfile profile = new EmployeeProfileMapper().map(Optional.ofNullable(result), Optional.empty());
        allEmployeeProfiles.add(profile);
      }
      catch (InvalidEmployeeProfileException e)
      {
        LOGGER.warn(e.getMessage());
      }
      catch (NoSuchElementException | NullPointerException e)
      {
        LOGGER.error("Exception caught: ", e);
      }
    }

    logMetadata(steriaList, allEmployeeProfiles);

    return allEmployeeProfiles;
  }

  // TODO this doesn't belong here
  private Sequence<String> steriaFilterSequence() throws SequenceException
  {
    return new StringSequence.StringSequenceBuilder().initial("CN=A*") // first call to next() will return this
        .characterToChange(3) // 'A'
        .increment(1) // increment by one character
        .size(26) // 26 Strings in the sequence
        .build();
  }

  private void logMetadata(Instant startADOps, Instant endADOps, Instant startDBOps, Instant endDBOps, int updatedCount,
      int notUpdatedCount)
  {
    final Duration adOpsTime = Duration.between(startADOps, endADOps);
    final Duration dbOpsTime = Duration.between(startDBOps, endDBOps);
    final Duration totalOpsTime = adOpsTime.plus(dbOpsTime);

    LOGGER.info("DB entries inserted/updated: {}", updatedCount);
    LOGGER.info("Failed attempts to insert/update DB: {}", notUpdatedCount);
    LOGGER.info("AD Operations time: {}", adOpsTime);
    LOGGER.info("DB Operations time: {}", dbOpsTime);
    LOGGER.info("Total time to sync: {}", totalOpsTime);
  }

  private void logMetadata(final List<SearchResult> resultsList, final List<EmployeeProfile> allEmployeeProfiles)
  {
    // Some metadata.
    // TODO don't think this belongs here

    LOGGER.info("Steria list size: {}", resultsList.size());
    LOGGER.info("EmployeeProfiles generated: {}", allEmployeeProfiles.size());
    LOGGER.info("With employee ID: {}", allEmployeeProfiles.stream().filter(e -> e.getEmployeeID() > 0).count());
    LOGGER.info("With username: {}",
        allEmployeeProfiles.stream().filter(e -> e.getUsername() != null && !e.getUsername().isEmpty()).count());
    LOGGER.info("With forename: {}",
        allEmployeeProfiles.stream().filter(e -> e.getForename() != null && !e.getForename().isEmpty()).count());
    LOGGER.info("With surname: {}",
        allEmployeeProfiles.stream().filter(e -> e.getSurname() != null && !e.getSurname().isEmpty()).count());
    LOGGER.info("With email address: {}", allEmployeeProfiles.stream()
        .filter(e -> e.getEmailAddress() != null && !e.getEmailAddress().isEmpty()).count());
    LOGGER.info("With reportees: {}",
        allEmployeeProfiles.stream().filter(e -> e.getReporteeCNs() != null && !e.getReporteeCNs().isEmpty()).count());
    LOGGER.info("With department: {}", allEmployeeProfiles.stream()
        .filter(e -> e.getSteriaDepartment() != null && !e.getSteriaDepartment().isEmpty()).count());
    LOGGER.info("With sector: {}",
        allEmployeeProfiles.stream().filter(e -> e.getSector() != null && !e.getSector().isEmpty()).count());
    LOGGER.info("With super sector: {}",
        allEmployeeProfiles.stream().filter(e -> e.getSuperSector() != null && !e.getSuperSector().isEmpty()).count());
    LOGGER.info("With company: {}",
        allEmployeeProfiles.stream().filter(e -> e.getCompany() != null && !e.getCompany().isEmpty()).count());
    LOGGER.info("With leaving date: {}",
        allEmployeeProfiles.stream().filter(e -> e.getAccountExpires() != null).count());
  }
}
