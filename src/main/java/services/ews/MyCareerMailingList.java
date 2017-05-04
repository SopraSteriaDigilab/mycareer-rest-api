package services.ews;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;

public class MyCareerMailingList implements DistributionList
{
  private final List<EmployeeProfile> employeeProfiles;

  /* package-private */ MyCareerMailingList(List<EmployeeProfile> emailsInDL) throws DistributionListException
  {
    if (emailsInDL.isEmpty())
    {
      throw new DistributionListException("Invalid DistributionList: no MyCareer members");
    }

    this.employeeProfiles = emailsInDL;
  }

  @Override
  public boolean hasMember(final long employeeID)
  {
    return employeeProfiles.stream().anyMatch(p -> p.getEmployeeID() == employeeID);
  }

  @Override
  public EmployeeProfile getMember(final long employeeID) throws EmployeeNotFoundException
  {
    final EmployeeProfile profile = employeeProfiles.stream().filter(p -> p.getEmployeeID() == employeeID).findAny()
        .orElseThrow(() -> new EmployeeNotFoundException("Employee ID does not exist in this mailing list"));

    return new EmployeeProfile(profile);
  }
  
  public List<EmployeeProfile> getList()
  {
    return employeeProfiles;
  }

  @Override
  public int size()
  {
    return employeeProfiles.size();
  }

  @Override
  public Set<String> getEmailAddresses()
  {
    return employeeProfiles.stream().map(p -> p.getEmailAddresses().getPreferred()).filter(s -> s != null).collect(Collectors.toSet());
  }

  @Override
  public void sendEmail(String subject, String body) throws Exception
  {
    EmailService.sendEmail(getEmailAddresses(), subject, body);
  }
  
  @Override
  public String toString()
  {
    return employeeProfiles.toString();
  }
}
