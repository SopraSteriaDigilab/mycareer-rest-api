package services.ews;

import static services.ad.ADOperations.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;
import services.EmployeeProfileService;
import services.ad.ADConnectionException;
import services.ad.ADSearchSettings;

public class DistributionListService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DistributionListService.class);

  private static final String EMEAAD_TREE = "OU=users,OU=Sopragroup,OU=UsersEmea,DC=emea,DC=msad,DC=sopra";

  private final EmployeeProfileService employeeProfileService;
  private final ADSearchSettings sopraADSearchSettings;

  public DistributionListService(final EmployeeProfileService employeeProfileService,
      final ADSearchSettings sopraADSearchSettings)
  {
    this.employeeProfileService = employeeProfileService;
    this.sopraADSearchSettings = sopraADSearchSettings;
  }

  public DistributionList getDistributionList(final String distributionListName) throws DistributionListException
  {
    final String filter = new StringBuilder("(&(memberOf=").append(distributionListName)
        .append(")(extensionAttribute7=s*))").toString();
    final List<SearchResult> distributionListResult = searchAD(sopraADSearchSettings, EMEAAD_TREE, filter);
    final List<EmployeeProfile> employeeProfiles = getEmployeeProfiles(distributionListResult);

    return new MyCareerMailingList(employeeProfiles);
  }

  public DistributionList getDistributionList(final Set<String> emailAddresses, final Set<String> invalidEmailAddresses) throws DistributionListException
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

  public boolean listExists(final String distributionListName)
  {
    return false;
  }

  private List<SearchResult> searchAD(final ADSearchSettings settings, final String tree, final String filter)
  {
    List<SearchResult> result = null;

    try
    {
      result = searchADAsList(settings, tree, filter);
    }
    catch (final ADConnectionException | NamingException e)
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      catch (final EmployeeNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return employeeProfiles;
  }
}
