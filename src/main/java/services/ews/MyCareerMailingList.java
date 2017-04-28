package services.ews;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;

public class MyCareerMailingList implements DistributionList
{
  private final List<EmployeeProfile> emailsInDL;

  /* package-private */ MyCareerMailingList(List<EmployeeProfile> emailsInDL) throws DistributionListException
  {
    if (emailsInDL.isEmpty())
    {
      throw new DistributionListException("Invalid DistributionList: no MyCareer members");
    }

    this.emailsInDL = emailsInDL;
  }

  @Override
  public boolean hasMember(final long employeeID)
  {
    return emailsInDL.stream().anyMatch(p -> p.getEmployeeID() == employeeID);
  }

  @Override
  public EmployeeProfile getMember(final long employeeID) throws EmployeeNotFoundException
  {
    final EmployeeProfile profile = emailsInDL.stream().filter(p -> p.getEmployeeID() == employeeID).findAny()
        .orElseThrow(() -> new EmployeeNotFoundException("Employee ID does not exist in this mailing list"));

    return new EmployeeProfile(profile);
  }

  @Override
  public int size()
  {
    return emailsInDL.size();
  }

  @Override
  public Set<String> getEmailAddresses()
  {
    return emailsInDL.stream().map(p -> p.getEmailAddresses().getPreferred()).collect(Collectors.toSet());
  }

  @Override
  public void sendEmail(String subject, String body) throws Exception
  {
    EmailService.sendEmail(getEmailAddresses(), subject, body);
  }
}
