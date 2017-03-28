package services;

import static services.ad.ADOperations.searchAD;
import static services.ad.ADOperations.searchADAsList;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;
import services.db.MongoOperations;
import services.db.MorphiaOperations;
import services.mappers.EmployeeProfileMapper;
import services.mappers.InvalidEmployeeProfileException;
import utils.sequence.Sequence;
import utils.sequence.SequenceException;
import utils.sequence.StringSequence;

public class BulkUpdateService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateService.class);

  private static final String BEGIN_UPDATE = "Beginning bulk update service";
  private static final String END_UPDATE = "Update service completed";

  private static final String AD_TREE = "ou=UK,ou=Internal,ou=People,DC=one,DC=steria,DC=dom";
  private static final String AD_UNUSED_OBJECT_TREE = "OU=UK,OU=People,OU=Unused Objects,DC=one,DC=steria,DC=dom";
  private static final String EMPLOYEE_ID = "profile.employeeID";

  private final MorphiaOperations morphiaOperations;
  private final MongoOperations employeeOperations;
  private final ADSearchSettings steriaADSearchSettings;
  private final EmployeeProfileMapper employeeProfileMapper;

  private int inserted;
  private int updated;
  private int alreadyUpToDate;
  private int notAnEmployee;
  private int exceptionsThrown;

  public BulkUpdateService(final MorphiaOperations morphiaOperations, final MongoOperations employeeOperations,
      final ADSearchSettings steriaADSearchSettings, final EmployeeProfileMapper employeeProfileMapper)
  {
    this.morphiaOperations = morphiaOperations;
    this.employeeOperations = employeeOperations;
    this.steriaADSearchSettings = steriaADSearchSettings;
    this.employeeProfileMapper = employeeProfileMapper;
  }

  @Scheduled(cron = "0 30 23 * * ?")
  public int syncDBWithADs() throws ADConnectionException, NamingException, SequenceException
  {
    LOGGER.info(BEGIN_UPDATE);
    inserted = updated = alreadyUpToDate = exceptionsThrown = notAnEmployee = 0;

    final Instant startADOps = Instant.now();
    final List<EmployeeProfile> allEmployeeProfiles = fetchAllEmployeeProfiles();
    final Instant endADOps = Instant.now();

    final Instant startDBOps = Instant.now();

    for (EmployeeProfile profile : allEmployeeProfiles)
    {
      try
      {
        upsertEmployeeProfile(profile);
      }
      catch (Exception e)
      {
        LOGGER.warn("Bulk update error: {}", e.getMessage());
        exceptionsThrown++;
      }
    }

    final Instant endDBOps = Instant.now();

    logMetadata(startADOps, endADOps, startDBOps, endDBOps, allEmployeeProfiles.size());

    LOGGER.info(END_UPDATE);
    return inserted + updated + alreadyUpToDate;
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
        EmployeeProfile profile = employeeProfileMapper.map(result);
        allEmployeeProfiles.add(profile);
      }
      catch (InvalidEmployeeProfileException e)
      {
        /*
         * There are always a lot of objects found by the bulk AD search that are not valid employee profiles. We can
         * just ignore these.
         */
        LOGGER.debug(e.getMessage());
        notAnEmployee++;
      }
      catch (NoSuchElementException | NullPointerException e)
      {
        LOGGER.error("Exception caught: ", e);
        exceptionsThrown++;
      }
    }

    logMetadata(steriaList, allEmployeeProfiles);

    return allEmployeeProfiles;
  }

  /**
   * Matches what is stored in the database with the given employee profile.
   * 
   * If the employee ID of the given employee profile does not exist in the database, the employee is inserted. If the
   * given employee profile is an exact match for an entry in the database, does nothing. Otherwise updates the employee
   * in the database to match the given employee profile.
   *
   * @param employeeProfile the employee profile to upsert
   * @return the new EmployeeProfile as stored in the MyCareer database
   */
  private EmployeeProfile upsertEmployeeProfile(EmployeeProfile employeeProfile)
  {
    Employee employee = morphiaOperations.getEmployee(EMPLOYEE_ID, employeeProfile.getEmployeeID());

    if (employee == null)
    {
      morphiaOperations.saveEmployee(new Employee(employeeProfile));
      inserted++;
      return employeeProfile;
    }
    else if (employee.getProfile().equals(employeeProfile))
    {
      alreadyUpToDate++;
      return employeeProfile;
    }

    updateEmployee(employee.getProfile(), employeeProfile);
    updated++;

    return employeeProfile;
  }

  // Performs an update of an existing employee
  private void updateEmployee(EmployeeProfile profile, EmployeeProfile employeeProfile)
  {
    Document filter = new Document(EmployeeProfile.EMPLOYEE_ID, profile.getEmployeeID());
    Document newFields = profile.differences(employeeProfile);

    employeeOperations.setFields(filter, newFields);
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

  private void logMetadata(Instant startADOps, Instant endADOps, Instant startDBOps, Instant endDBOps, int entriesFound)
  {
    final Duration adOpsTime = Duration.between(startADOps, endADOps);
    final Duration dbOpsTime = Duration.between(startDBOps, endDBOps);
    final Duration totalOpsTime = adOpsTime.plus(dbOpsTime);

    LOGGER.info("Total entries found: {}", entriesFound);
    LOGGER.info("New employees inserted: {}", inserted);
    LOGGER.info("Employees updated: {}", updated);
    LOGGER.info("Employees already up to date: {}", alreadyUpToDate);
    LOGGER.info("Exceptions thrown: {}", exceptionsThrown);
    LOGGER.info("Number of entries not corresponding to an employee: {}", notAnEmployee);
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
        .filter(e -> e.getEmailAddresses() != null && !e.getEmailAddresses().isEmpty()).count());
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
