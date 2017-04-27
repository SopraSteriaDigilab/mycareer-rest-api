package services.ews;

import java.util.List;
import java.util.stream.Collectors;

import dataStructure.EmployeeProfile;

public class DistributionList
{
  private final List<EmployeeProfile> emailsInDL;
  
  /* package-private */ DistributionList(List<EmployeeProfile> emailsInDL)
  {
    this.emailsInDL = emailsInDL;
  }
  
  public EmployeeProfile getEmail(final int index)
  {
    return new EmployeeProfile(emailsInDL.get(index));
  }
  
  public int size()
  {
    return emailsInDL.size();
  }
  
  public void sendEmail(String subject, String body) throws Exception
  {
    final List<String> emailAddresses = emailsInDL.stream()
        .map(p -> p.getEmailAddresses().getPreferred())
        .collect(Collectors.toList());
    
    EmailService.sendEmail(emailAddresses, subject, body);
  }
}
