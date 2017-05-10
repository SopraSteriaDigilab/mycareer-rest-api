package services.ews;

import static services.ad.ADOperations.*;
import static services.ad.query.LDAPQueries.*;
import static utils.Conversions.*;
import static services.ews.DistributionList.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  public boolean isSopraDistributionList(final String distributionListName)
  {
    return isDistributionList(distributionListName, sopraADSearchSettings, SOPRA_DL_TREE);
  }

  public boolean isSteriaDistributionList(final String distributionListName)
  {
    return isDistributionList(distributionListName, steriaADSearchSettings, STERIA_DL_TREE);
  }

  public DistributionList getCachedDistributionList(String distributionListName)
  {
    if (!distributionListCache.contains(distributionListName))
    {
      throw new IllegalArgumentException("The given distribution list is not cached: ".concat(distributionListName));
    }

    return distributionListCache.get(distributionListName);
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
    final List<String> allMemberDNs = getAllMembersDNs(distributionListResult, adSearchSettings, dlTree);
    final List<SearchResult> allMemberResults = getAllMemberResults(allMemberDNs, adSearchSettings, userTree,
        employeeIDField);
    final Set<EmployeeProfile> employeeProfiles = getEmployeeProfiles(allMemberResults, employeeIDField);
    final DistributionList distributionList = new MyCareerMailingList(distributionListName, employeeProfiles);

    return distributionList;
  }

  private List<SearchResult> getAllMemberResults(List<String> allMembers, ADSearchSettings adSearchSettings,
      String userTree, String employeeIDField) throws ADConnectionException, DistributionListException
  {
    List<SearchResult> returnValue = null;
    List<LDAPQuery> clauses = new ArrayList<>();
    LDAPQuery query;

    for (final String memberDN : allMembers)
    {
      clauses.add(basicQuery(DISTINGUISHED_NAME, memberDN));
    }

    query = and(or(clauses), hasField(employeeIDField));
    
    try
    {
      returnValue = searchADAsList(adSearchSettings, userTree, query.get());
    }
    catch (NamingException | NullPointerException e)
    {
      LOGGER.warn(ERROR_FETCH_MEMBERS);
    }

    return returnValue;
  }

  @SuppressWarnings("unchecked")
  private void addNestedMemberDNs(List<String> memberDNs, ADSearchSettings adSearchSettings, String dlTree)
      throws DistributionListException, ADConnectionException
  {
    if (memberDNs.isEmpty())
    {
      return;
    }

    List<LDAPQuery> clauses = new ArrayList<>();
    List<SearchResult> results = null;
    LDAPQuery query = null;

    for (final String memberDN : memberDNs)
    {
      clauses.add(basicQuery(DISTINGUISHED_NAME, memberDN));
    }

    query = and(or(clauses), hasField(MEMBER));

    try
    {
      results = searchADAsList(adSearchSettings, dlTree, query.get());

      if (results.isEmpty())
      {
        return;
      }
    }
    catch (NullPointerException | NamingException e)
    {
      LOGGER.warn(ERROR_FETCH_MEMBERS);
    }

    for (final SearchResult result : results)
    {
      addMemberDNs(result, memberDNs);
    }

    addNestedMemberDNs(memberDNs, adSearchSettings, dlTree);
  }

  @SuppressWarnings("unchecked")
  private List<String> getAllMembersDNs(final SearchResult distributionList, ADSearchSettings adSearchSettings,
      String dlTree) throws DistributionListException, ADConnectionException
  {
    final List<String> memberDNs = new ArrayList<String>();

    addMemberDNs(distributionList, memberDNs);
    addNestedMemberDNs(memberDNs, adSearchSettings, dlTree);

    return memberDNs;
  }

  @SuppressWarnings("unchecked")
  private void addMemberDNs(final SearchResult result, final List<String> memberDNs) throws DistributionListException
  {
    final Attributes attributes = result.getAttributes();
    final Attribute members = attributes.get(MEMBER);

    try
    {
      memberDNs.addAll((List<String>) namingEnumToList(members.getAll()));
    }
    catch (NullPointerException | NamingException e)
    {
      LOGGER.warn(ERROR_FETCH_MEMBERS);
    }
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
        employeeProfiles.add(profile);
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

  public DistributionList cache(String name, DistributionList distributionList)
  {
    return distributionListCache.put(name, distributionList);
  }

  public DistributionList combine(DistributionList sopraDL, DistributionList steriaDL)
  {
    return sopraDL != null ? sopraDL.combine(steriaDL) : (steriaDL != null ? steriaDL : null);
  }
}
