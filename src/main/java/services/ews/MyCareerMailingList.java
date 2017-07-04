package services.ews;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;

public class MyCareerMailingList implements DistributionList
{
  private final String name;
  private final Set<EmployeeProfile> employeeProfiles;

  /* package-private */ MyCareerMailingList(String name, Set<EmployeeProfile> employeeProfiles) throws DistributionListException
  {
    if (employeeProfiles.isEmpty())
    {
      throw new DistributionListException("Invalid DistributionList: no MyCareer members");
    }

    this.name = name;
    this.employeeProfiles = employeeProfiles;
  }
  
  @Override
  public String getName()
  {
    return name;
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

  @Override
  public Set<EmployeeProfile> getList()
  {
    return new HashSet<>(employeeProfiles);
  }
  
  @Override
  public void forEach(final Consumer<? super EmployeeProfile> action)
  {
    employeeProfiles.forEach(action);
  }

  @Override
  public int size()
  {
    return employeeProfiles.size();
  }

  @Override
  public Set<String> getEmailAddresses()
  {
    return employeeProfiles.stream().map(p -> p.getEmailAddresses().getPreferred()).filter(s -> s != null)
        .collect(Collectors.toSet());
  }

  @Override
  public void sendEmail(String subject, String body) throws Exception
  {
    EmailService.sendEmail(getEmailAddresses(), subject, body);
  }
  
  @Override
  public boolean equals(Object other)
  {
    if (other == null || !(other instanceof MyCareerMailingList))
    {
      return false;
    }
    
    MyCareerMailingList otherMCML = (MyCareerMailingList) other;
    
    return employeeProfiles.equals(otherMCML.employeeProfiles);
  }

  @Override
  public String toString()
  {
    return employeeProfiles.toString();
  }

  @Override
  public DistributionList combine(DistributionList other)
  {
    if (other == null || !(other instanceof MyCareerMailingList) || equals(other))
    {
      return this;
    }

    MyCareerMailingList otherMCML = (MyCareerMailingList) other;
    Set<EmployeeProfile> newMCMLSet = new HashSet<>(employeeProfiles);
    newMCMLSet.addAll(otherMCML.employeeProfiles);
    DistributionList combinedDL = null;
    
    try
    {
      combinedDL = new MyCareerMailingList(name, newMCMLSet);
    }
    catch (DistributionListException e)
    {
      /* Since these are already valid this can't happen */
    }

    return combinedDL;
  }
}
