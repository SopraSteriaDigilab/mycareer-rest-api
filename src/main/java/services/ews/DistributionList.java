package services.ews;

import java.util.Set;

import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;

public interface DistributionList
{
  Set<String> getEmailAddresses();
  void sendEmail(String subject, String body) throws Exception;
  int size();
  boolean hasMember(long employeeID);
  EmployeeProfile getMember(long employeeID) throws EmployeeNotFoundException;
}
