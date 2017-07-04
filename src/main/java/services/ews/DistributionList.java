package services.ews;

import java.util.Set;
import java.util.function.Consumer;

import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;

public interface DistributionList
{
  String CUSTOM_LIST = "Custom list";
  
  Set<String> getEmailAddresses();
  void sendEmail(String subject, String body) throws Exception;
  int size();
  boolean hasMember(long employeeID);
  EmployeeProfile getMember(long employeeID) throws EmployeeNotFoundException;
  DistributionList combine(DistributionList steriaDL);
  String getName();
  void forEach(Consumer<? super EmployeeProfile> action);
  Set<EmployeeProfile> getList();
}
