package services;

import static services.ad.ADSearchSettingsImpl.LdapPort.*;

import static services.ad.ADOperations.searchAD;
import static services.ad.ADOperations.searchADAsList;
import static dataStructure.EmployeeProfile.*;
import static services.ad.query.LDAPQueries.*;

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
import services.ews.Cache;
import services.ews.DistributionList;
import services.mappers.EmployeeProfileMapper;
import services.mappers.InvalidEmployeeProfileException;
import utils.sequence.Sequence;
import utils.sequence.SequenceException;
import utils.sequence.StringSequence;
import services.ad.query.LDAPQueries;
import services.ad.query.LDAPQuery;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class BulkUpdateService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateService.class);

  private static final String BEGIN_UPDATE = "Beginning bulk update service";
  private static final String END_UPDATE = "Update service completed";

  private static final String AD_TREE = "ou=UK,ou=Internal,ou=People,DC=one,DC=steria,DC=dom";
  private static final String AD_UNUSED_OBJECT_TREE = "OU=UK,OU=People,OU=Unused Objects,DC=one,DC=steria,DC=dom";

  private final MorphiaOperations morphiaOperations;
  private final MongoOperations employeeOperations;
  private final ADSearchSettings steriaADSearchSettings;
  private final EmployeeProfileMapper employeeProfileMapper;
  private final Cache<String, DistributionList> distributionListCache;

  private int inserted;
  private int updated;
  private int alreadyUpToDate;
  private int notAnEmployee;
  private int exceptionsThrown;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param morphiaOperations
   * @param employeeOperations
   * @param steriaADSearchSettings
   * @param employeeProfileMapper
   * @param distributionListCache
   */
  public BulkUpdateService(final MorphiaOperations morphiaOperations, final MongoOperations employeeOperations,
      final ADSearchSettings steriaADSearchSettings, final EmployeeProfileMapper employeeProfileMapper,
      Cache<String, DistributionList> distributionListCache)
  {
    this.morphiaOperations = morphiaOperations;
    this.employeeOperations = employeeOperations;
    this.steriaADSearchSettings = steriaADSearchSettings;
    this.employeeProfileMapper = employeeProfileMapper;
    this.distributionListCache = distributionListCache;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   * @throws ADConnectionException
   * @throws NamingException
   * @throws SequenceException
   */
  // @Scheduled(cron = "0 30 23 * * ?")
  @Scheduled(fixedDelay = 999999999)
  public int syncDBWithADs() throws ADConnectionException, NamingException, SequenceException
  {
    LOGGER.info(BEGIN_UPDATE);
    inserted = updated = alreadyUpToDate = exceptionsThrown = notAnEmployee = 0;

    final Instant startADOps = Instant.now();
    final List<EmployeeProfile> allAdProfiles = fetchAllAdProfiles();
    final Instant endADOps = Instant.now();

    final Instant startDBOps = Instant.now();

    for (EmployeeProfile adProfile : allAdProfiles)
    {
      try
      {
        upsertEmployeeProfile(adProfile);
      }
      catch (Exception e)
      {
        LOGGER.warn("Bulk update error: {}", e.getMessage());
        exceptionsThrown++;
      }
    }

    distributionListCache.clear();

    final Instant endDBOps = Instant.now();

    logMetadata(startADOps, endADOps, startDBOps, endDBOps, allAdProfiles.size());

    LOGGER.info(END_UPDATE);

    return inserted + updated + alreadyUpToDate;
  }

  private List<EmployeeProfile> fetchAllAdProfiles() throws ADConnectionException, NamingException, SequenceException
  {
    // There are approximately 6,200 employees, hence initial capacity of 10,000
    final List<EmployeeProfile> allEmployeeProfiles = new ArrayList<>(10_000);
    final List<SearchResult> steriaList = searchAD(steriaADSearchSettings, AD_TREE, steriaFilterSequence(), LOCAL);
    final LDAPQuery query = and(hasField(CN), hasField(EXTENSION_ATTRIBUTE_2),
        basicQuery(LDAPQueries.EMPLOYEE_TYPE, EMPLOYEE));
    steriaList.addAll(searchADAsList(steriaADSearchSettings, AD_UNUSED_OBJECT_TREE, query.get(), LOCAL));

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

  private EmployeeProfile upsertEmployeeProfile(EmployeeProfile newProfile)
  {
    Employee dbEmployee = morphiaOperations.getEmployee(EMPLOYEE_ID, newProfile.getEmployeeID());

    if (dbEmployee == null)
    {
      morphiaOperations.saveEmployee(new Employee(newProfile));
      ++inserted;
    }
    else if (dbEmployee.getProfile().equals(newProfile))
    {
      ++alreadyUpToDate;
    }
    else
    {
      updateEmployee(dbEmployee.getProfile(), newProfile);
      ++updated;
    }

    return newProfile;
  }

  // Performs an update of an existing employee
  private void updateEmployee(EmployeeProfile currentProfile, EmployeeProfile newProfile)
  {
    Document filter = new Document(EMPLOYEE_ID, currentProfile.getEmployeeID());
    Document newFields = currentProfile.differences(newProfile);

    employeeOperations.setFields(filter, newFields);
  }

  // TODO this doesn't belong here
  private Sequence<String> steriaFilterSequence() throws SequenceException
  {
    final LDAPQuery initialQuery = and(fieldBeginsWith(CN, "A"), hasField(EXTENSION_ATTRIBUTE_2),
        basicQuery(LDAPQueries.EMPLOYEE_TYPE, EMPLOYEE));
    final String initialQueryString = initialQuery.get();
    final int firstLetter = initialQueryString.indexOf("cn=A") + 3;
    return new StringSequence.StringSequenceBuilder().initial(initialQuery.get()) // first call to next() will return
                                                                                  // this
        .characterToChange(firstLetter) // 'A'
        .increment(1) // increment by one character
        .size(26) // 26 Strings in the sequence
        .build();
  }

  private void logMetadata(final List<SearchResult> resultsList, final List<EmployeeProfile> allEmployeeProfiles)
  {
    // Some metadata.
    // TODO don't think this belongs here
    LOGGER.info("Steria list size: {}", resultsList.size());
    LOGGER.info("EmployeeProfiles generated: {}", allEmployeeProfiles.size());
    LOGGER.info("With employee ID: {}", allEmployeeProfiles.stream().filter(e -> e.getEmployeeID() > 0).count());
    LOGGER.info("With employee type: {}", allEmployeeProfiles.stream()
        .filter(e -> e.getEmployeeType() != null && !e.getEmployeeType().isEmpty()).count());
    LOGGER.info("With username: {}",
        allEmployeeProfiles.stream().filter(e -> e.getUsername() != null && !e.getUsername().isEmpty()).count());
    LOGGER.info("With forename: {}",
        allEmployeeProfiles.stream().filter(e -> e.getForename() != null && !e.getForename().isEmpty()).count());
    LOGGER.info("With surname: {}",
        allEmployeeProfiles.stream().filter(e -> e.getSurname() != null && !e.getSurname().isEmpty()).count());
    LOGGER.info("With email address \"mail\": {}",
        allEmployeeProfiles.stream().filter(e -> e.getEmailAddresses() != null
            && e.getEmailAddresses().getMail() != null && !e.getEmailAddresses().getMail().isEmpty()).count());
    LOGGER.info("With email address \"targetAddress\": {}",
        allEmployeeProfiles.stream().filter(e -> e.getEmailAddresses() != null
            && e.getEmailAddresses().getTargetAddress() != null && !e.getEmailAddresses().getTargetAddress().isEmpty())
            .count());
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
}
