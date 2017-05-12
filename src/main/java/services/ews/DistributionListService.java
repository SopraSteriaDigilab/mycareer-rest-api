package services.ews;

import static services.ad.ADOperations.*;
import static services.ad.query.LDAPQueries.*;
import static services.ews.DistributionList.*;
import static utils.Conversions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;
import services.EmployeeProfileService;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;
import services.ad.query.LDAPQuery;

public class DistributionListService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DistributionListService.class);

  private static final String ERROR_FETCH_MEMBERS = "Error encountered while fetching members of this distribution list.  It may not have any members, or it may not be a list.";
  private static final String UNKNOWN_ERROR = "ADConnectionException encountered while searching for a distribution list: {}";

  private static final String SOPRA_DL_TREE = "DC=emea,DC=msad,DC=sopra";
  private static final String SOPRA_USER_TREE = "DC=emea,DC=msad,DC=sopra";
  private static final String SOPRA_EMPLOYEE_ID_FIELD = EXTENSION_ATTRIBUTE_7;

  private static final String STERIA_DL_TREE = "DC=one,DC=steria,DC=dom";
  private static final String STERIA_USER_TREE = "DC=one,DC=steria,DC=dom";
  private static final String STERIA_EMPLOYEE_ID_FIELD = EXTENSION_ATTRIBUTE_2;

  private final Cache<String, DistributionList> distributionListCache;
  private final EmployeeProfileService employeeProfileService;
  private final ADSearchSettings sopraADSearchSettings;
  private final ADSearchSettings steriaADSearchSettings;

  public DistributionListService(Cache<String, DistributionList> distributionListCache,
      final EmployeeProfileService employeeProfileService, final ADSearchSettings sopraADSearchSettings,
      final ADSearchSettings steriaADSearchSettings)
  {
    this.distributionListCache = distributionListCache;
    this.employeeProfileService = employeeProfileService;
    this.sopraADSearchSettings = sopraADSearchSettings;
    this.steriaADSearchSettings = steriaADSearchSettings;
  }

  /**
   * Checks the Sopra AD (EMEAAD) for the existence of the given distributionListName.
   *
   * @param distributionListName the name to search for
   * @return {@code true} if the name exists and is found, {@code false} otherwise.
   */
  public boolean isSopraDistributionList(final String distributionListName)
  {
    return isDistributionList(distributionListName, sopraADSearchSettings, SOPRA_DL_TREE);
  }

  /**
   * Checks the Steria AD (ADOne) for the existence of the given distributionListName.
   *
   * @param distributionListName the name to search for
   * @return {@code true} if the name exists and is found, {@code false} otherwise.
   */
  public boolean isSteriaDistributionList(final String distributionListName)
  {
    return isDistributionList(distributionListName, steriaADSearchSettings, STERIA_DL_TREE);
  }

  /**
   * Generates a {@code DistributionList} based on the given name.
   * 
   * The distribution list is taken from EMEAAD.
   *
   * @param distributionListName the name of the list to find
   * @return a DistributionList taken from an active directory with non-MyCareer members removed
   * @throws DistributionListException if a list by the given name does not exist on EMEAAD, or if no MyCareer members
   *           are in the list.
   * @throws ADConnectionException if there was a problem connecting to an ctive directory
   */
  public DistributionList sopraDistributionList(final String distributionListName)
      throws DistributionListException, ADConnectionException
  {
    return generateDistributionList(distributionListName, sopraADSearchSettings, SOPRA_DL_TREE, SOPRA_USER_TREE,
        SOPRA_EMPLOYEE_ID_FIELD);
  }

  /**
   * Generates a {@code DistributionList} based on the given name.
   * 
   * The distribution list is taken from ADOne.
   *
   * @param distributionListName the name of the list to find
   * @return a DistributionList taken from an active directory with non-MyCareer members removed
   * @throws DistributionListException if a list by the given name does not exist on ADOne, or if no MyCareer members
   *           are in the list.
   * @throws ADConnectionException if there was a problem connecting to an ctive directory
   */
  public DistributionList steriaDistributionList(final String distributionListName)
      throws DistributionListException, ADConnectionException
  {
    return generateDistributionList(distributionListName, steriaADSearchSettings, STERIA_DL_TREE, STERIA_USER_TREE,
        STERIA_EMPLOYEE_ID_FIELD);
  }

  /**
   * Generates a distribution list using the given email addresses.
   * 
   * Any email addresses for which a valid MyCareer member cannot be found are removed from the given emailAddresses set
   * and placed into the given invalidEmailAddresses set. The invalidEmailAddresses parameter should be an empty set.
   *
   * @param emailAddresses the list of email addresses to be added to the distribution list.
   * @param invalidEmailAddresses an empty placeholder set for email addresses for which there is no valid MyCareer
   *          employee
   * @return the distribution list
   * @throws DistributionListException if {@code emailAddresses} is empty or if it contains no valid MyCareer email
   *           addresses
   * @throws IllegalArgumentException if {@code invalidEmailAddress} is not empty
   */
  public DistributionList customDistributionList(final Set<String> emailAddresses,
      final Set<String> invalidEmailAddresses) throws DistributionListException
  {
    if (!invalidEmailAddresses.isEmpty())
    {
      throw new IllegalArgumentException("Invalid email addresses set must be empty");
    }

    final Set<EmployeeProfile> employeeProfiles = new HashSet<>();

    for (final String emailAddress : emailAddresses)
    {
      EmployeeProfile profile;

      try
      {
        profile = employeeProfileService.fetchEmployeeProfileFromEmailAddress(emailAddress);
        employeeProfiles.add(profile);
      }
      catch (final EmployeeNotFoundException e)
      {
        invalidEmailAddresses.add(emailAddress);
      }
    }

    emailAddresses.removeAll(invalidEmailAddresses);

    if (emailAddresses.isEmpty())
    {
      return null;
    }

    return new MyCareerMailingList(CUSTOM_LIST, employeeProfiles);
  }

  /**
   * Returns a DistributionList with the given name from the cache.
   *
   * @param distributionListName the name of the DistributionList to return
   * @return the DistributionList with the given name
   * @throws IllegalArgumentException if no DistributionList with the given name exists in the cache
   */
  public DistributionList getCachedDistributionList(String distributionListName) throws IllegalArgumentException
  {
    if (!distributionListCache.contains(distributionListName))
    {
      throw new IllegalArgumentException("The given distribution list is not cached: ".concat(distributionListName));
    }

    return distributionListCache.get(distributionListName);
  }

  /**
   * Adds the given DistributionList to the cache, with the given name as a reference
   *
   * @param name the name of the DistributionList
   * @param distributionList the DistributionList to cache
   * @return the DistributionList previously associated with this name, or {@code null} if the name was previously
   *         unassociated
   */
  public DistributionList cache(String name, DistributionList distributionList)
  {
    return distributionListCache.put(name, distributionList);
  }

  /**
   * Combines the two given DistributionLists into a single list.
   *
   * @param dlOne a DistributionList
   * @param dlTwo another DistributionList
   * @return the union of the two given DistributionLists
   */
  public DistributionList combine(DistributionList dlOne, DistributionList dlTwo)
  {
    return dlOne != null ? dlOne.combine(dlTwo) : (dlTwo != null ? dlTwo : null);
  }

  private LDAPQuery createDistributionListQuery(final String distributionListName)
  {
    return or(basicQuery(MAIL, distributionListName), basicQuery(DISPLAY_NAME, distributionListName),
        basicQuery(CN, distributionListName), basicQuery(MAIL_NICKNAME, distributionListName),
        basicQuery(NAME, distributionListName), basicQuery(SAM_ACCOUNT_NAME, distributionListName),
        basicQuery(TARGET_ADDRESS, distributionListName));
  }

  private boolean isDistributionList(final String distributionListName, final ADSearchSettings adSearchSettings,
      final String dlTree)
  {
    final LDAPQuery filter = createDistributionListQuery(distributionListName);
    final SearchResult distributionListResult = searchAD(adSearchSettings, dlTree, filter.get());

    return distributionListResult != null;
  }

  private DistributionList generateDistributionList(final String distributionListName,
      final ADSearchSettings adSearchSettings, final String dlTree, final String userTree, final String employeeIDField)
      throws DistributionListException, ADConnectionException
  {
    if (distributionListCache.contains(distributionListName))
    {
      return distributionListCache.get(distributionListName);
    }

    final LDAPQuery filter = createDistributionListQuery(distributionListName);
    final SearchResult distributionListResult = searchAD(adSearchSettings, dlTree, filter.get());
    final List<SearchResult> allMemberResults = getAllMemberResults(distributionListResult, adSearchSettings, userTree,
        employeeIDField);
    final Set<EmployeeProfile> employeeProfiles = getEmployeeProfiles(allMemberResults, employeeIDField);
    DistributionList distributionList;

    try
    {
      distributionList = new MyCareerMailingList(distributionListName, employeeProfiles);
    }
    catch (final DistributionListException e)
    {
      distributionList = null;
      LOGGER.info(e.getMessage() + ": " + distributionListName);
    }

    return distributionList;
  }

  @SuppressWarnings("unchecked")
  private List<SearchResult> getAllMemberResults(final SearchResult dlResult, final ADSearchSettings adSearchSettings,
      final String userTree, final String employeeIDField) throws ADConnectionException, DistributionListException
  {
    final List<SearchResult> allPersonResults = new ArrayList<>();
    final List<SearchResult> dLResults = new ArrayList<>();
    dLResults.add(dlResult);

    addDLMembersToSet(dLResults, allPersonResults, adSearchSettings, userTree, employeeIDField);

    return allPersonResults;
  }

  private void addDLMembersToSet(final List<SearchResult> dlResults, final List<SearchResult> allPersonResults,
      final ADSearchSettings adSearchSettings, final String userTree, final String employeeIDField)
      throws ADConnectionException
  {
    final Set<String> memberDNs = addMembersToSet(dlResults);

    if (memberDNs.isEmpty())
    {
      return;
    }

    List<SearchResult> allResults = null;
    final Set<LDAPQuery> clauses = new HashSet<>();
    final LDAPQuery query;

    for (final String memberDN : memberDNs)
    {
      clauses.add(basicQuery(DISTINGUISHED_NAME, memberDN));
    }

    query = or(clauses);

    try
    {
      allResults = searchADAsList(adSearchSettings, userTree, query.get());
    }
    catch (NamingException | NullPointerException e)
    {
      LOGGER.warn(ERROR_FETCH_MEMBERS);
    }

    dlResults.clear();

    for (final SearchResult result : allResults)
    {
      if (isPerson(result, employeeIDField))
      {
        allPersonResults.add(result);
      }
      else if (hasMembers(result))
      {
        dlResults.add(result);
      }
      else
      {
        // TODO this shouldn't happen but if it does...?
      }
    }

    if (!dlResults.isEmpty())
    {
      addDLMembersToSet(dlResults, allPersonResults, adSearchSettings, userTree, employeeIDField);
    }
  }

  private boolean isPerson(final SearchResult result, final String employeeIDField)
  {
    final Attribute employeeIDAttribute = getAttribute(result, employeeIDField);

    return employeeIDAttribute != null;
  }

  private boolean hasMembers(final SearchResult result)
  {
    final Attribute memberAttribute = getAttribute(result, MEMBER);

    return memberAttribute != null;
  }

  private Set<String> addMembersToSet(final List<SearchResult> dlResults)
  {
    final Set<String> allMembers = new HashSet<>();

    for (final SearchResult dlResult : dlResults)
    {
      allMembers.addAll(addMembersToSet(dlResult));
    }

    return allMembers;
  }

  @SuppressWarnings("unchecked")
  private Set<String> addMembersToSet(final SearchResult dlResult)
  {
    final Set<String> members = new HashSet<>();
    final Attribute membersAttribute = getAttribute(dlResult, MEMBER);

    try
    {
      members.addAll((List<String>) namingEnumToList(membersAttribute.getAll()));
    }
    catch (final NamingException e)
    {
      // TODO handle
    }

    return members;
  }

  private Attribute getAttribute(final SearchResult result, final String fieldName)
  {
    final Attributes attributes = result.getAttributes();

    NamingEnumeration<String> ids = attributes.getIDs();

    try
    {
      while (ids.hasMore())
      {
        LOGGER.info(ids.next().toString());
      }
    }
    catch (Exception e)
    {
      LOGGER.info("error while printing IDs");
    }

    return attributes.get(fieldName);
  }

  private SearchResult searchAD(final ADSearchSettings settings, final String tree, final String filter)
  {
    SearchResult result = null;

    try
    {
      result = searchADSingleResult(settings, tree, filter);
    }
    catch (final ADConnectionException e)
    {
      LOGGER.warn(UNKNOWN_ERROR, e.getMessage());
    }

    return result;
  }

  private Set<EmployeeProfile> getEmployeeProfiles(final List<SearchResult> results, final String employeeIDField)
  {
    final Set<EmployeeProfile> employeeProfiles = new HashSet<>();

    for (final SearchResult result : results)
    {
      String employeeIDString;
      Long employeeID;
      EmployeeProfile profile;

      try
      {
        employeeIDString = result.getAttributes().get(employeeIDField).get().toString();
        employeeID = Long.parseLong(employeeIDString.substring(1));
        profile = employeeProfileService.fetchEmployeeProfile(employeeID);
        
        if (profile.getAccountExpires() == null)
        {
          employeeProfiles.add(profile);
        }
      }
      catch (final NamingException | NullPointerException e)
      {
        LOGGER.error(e.getMessage(), e);
      }
      catch (final EmployeeNotFoundException e)
      {
        /*
         * Swallow this exception, as it expected that DLs will occasionally have members who do not have MyCareer
         * access. These can just be skipped over.
         */
      }
    }

    return employeeProfiles;
  }
}
