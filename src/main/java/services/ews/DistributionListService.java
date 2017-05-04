package services.ews;

import static services.ad.ADOperations.*;
import static services.ad.query.LDAPQueries.*;
import static utils.Conversions.*;

import java.util.ArrayList;
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

  private static final String ERROR_FETCH_MEMBERS = "Error encountered while fetching members of this distribution list";

  private static final String EMEAAD_DL_TREE = "OU=Distribution lists,OU=usersemea,DC=emea,DC=msad,DC=sopra";//
  private static final String EMEAAD_USER_TREE = "OU=usersemea,DC=emea,DC=msad,DC=sopra";//OU=UK,OU=users,OU=SopraGroup,

  private final EmployeeProfileService employeeProfileService;
  private final ADSearchSettings sopraADSearchSettings;

  public DistributionListService(final EmployeeProfileService employeeProfileService,
      final ADSearchSettings sopraADSearchSettings)
  {
    this.employeeProfileService = employeeProfileService;
    this.sopraADSearchSettings = sopraADSearchSettings;
  }

  public DistributionList getDistributionList(final String distributionListName)
      throws DistributionListException, ADConnectionException
  {
    final LDAPQuery filter = basicQuery("cn", distributionListName);
    final SearchResult distributionListResult = searchAD(sopraADSearchSettings, EMEAAD_DL_TREE, filter.get());
    final List<String> allMemberDNs = getAllMembersDNs(distributionListResult);
    final List<SearchResult> allMemberResults = getAllMemberResults(allMemberDNs);
    final List<EmployeeProfile> employeeProfiles = getEmployeeProfiles(allMemberResults);

    return new MyCareerMailingList(employeeProfiles);
  }

  public DistributionList getDistributionList(final Set<String> emailAddresses, final Set<String> invalidEmailAddresses)
      throws DistributionListException
  {
    if (!invalidEmailAddresses.isEmpty())
    {
      throw new IllegalArgumentException("Invalid email addresses set must be empty");
    }

    final List<EmployeeProfile> employeeProfiles = new ArrayList<>();

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

    return new MyCareerMailingList(employeeProfiles);
  }

  private List<SearchResult> getAllMemberResults(List<String> allMembers)
      throws ADConnectionException, DistributionListException
  {
    List<SearchResult> returnValue = null;
    List<LDAPQuery> clauses = new ArrayList<>();
    LDAPQuery query;

    try
    {
      for (final String memberDN : allMembers)
      {
        clauses.add(and(basicQuery("distinguishedName", memberDN), hasField("extensionAttribute7")));
      }

      query = or(clauses);
      returnValue = searchADAsList(sopraADSearchSettings, EMEAAD_USER_TREE, query.get());
    }
    catch (NamingException e)
    {
      LOGGER.error(ERROR_FETCH_MEMBERS);
      throw new DistributionListException(ERROR_FETCH_MEMBERS, e);
    }

    return returnValue;
  }

  @SuppressWarnings("unchecked")
  private void addNestedMemberDNs(List<String> memberDNs) throws DistributionListException, ADConnectionException
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
      clauses.add(and(basicQuery("distinguishedName", memberDN), hasField("member")));
    }

    query = or(clauses);

    try
    {
      results = searchADAsList(sopraADSearchSettings, EMEAAD_DL_TREE, query.get());

      if (results.isEmpty())
      {
        return;
      }
    }
    catch (NamingException e)
    {
      LOGGER.error(ERROR_FETCH_MEMBERS);
      throw new DistributionListException(ERROR_FETCH_MEMBERS, e);
    }
    
    for (final SearchResult result : results)
    {
      addMemberDNs(result, memberDNs);
    }
    
    addNestedMemberDNs(memberDNs);
  }

  @SuppressWarnings({ "unchecked" })
  private List<String> getAllMembersDNs(final SearchResult distributionList)
      throws DistributionListException, ADConnectionException
  {
    final List<String> memberDNs = new ArrayList<String>();
    
    addMemberDNs(distributionList, memberDNs);
    addNestedMemberDNs(memberDNs);
    
    return memberDNs;
  }
  
  @SuppressWarnings("unchecked")
  private void addMemberDNs(final SearchResult result, final List<String> memberDNs) throws DistributionListException
  {
    final Attributes attributes = result.getAttributes();
    final Attribute members = attributes.get("member");

    try
    {
      memberDNs.addAll((List<String>) namingEnumToList(members.getAll()));
    }
    catch (NamingException e)
    {
      LOGGER.error(ERROR_FETCH_MEMBERS);
      throw new DistributionListException(ERROR_FETCH_MEMBERS, e);
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
      // TODO handle
    }

    return result;
  }

  private List<EmployeeProfile> getEmployeeProfiles(final List<SearchResult> results)
  {
    final List<EmployeeProfile> employeeProfiles = new ArrayList<>();

    for (final SearchResult result : results)
    {
      String extensionAttribute7;
      Long employeeID;
      EmployeeProfile profile;

      try
      {
        extensionAttribute7 = result.getAttributes().get("extensionAttribute7").get().toString();
        employeeID = Long.parseLong(extensionAttribute7.substring(1));
        profile = employeeProfileService.fetchEmployeeProfile(employeeID);
        employeeProfiles.add(profile);
      }
      catch (final NamingException | NullPointerException e)
      {
        LOGGER.error(e.getMessage(), e);
      }
      catch (final EmployeeNotFoundException e)
      {
        /* Swallow this exception */
      }
    }

    return employeeProfiles;
  }
}
