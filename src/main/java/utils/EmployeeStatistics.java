package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import dataStructure.Employee;

/**
 * Class to hold create statistic mappings.
 *
 */
public class EmployeeStatistics
{

  /** String[] Constant - Indicates fields to be used in the employee statistics */
  public final static String[] EMPLOYEE_FIELDS = { "employeeID", "forename", "surname", "company", "superSector",
      "steriaDepartment" };

  public final static String[] FEEDBACK_FIELDS = {  "feedback" };


  /**
   * Statistics for employees given a list of employees.
   *
   * @param employees
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getEmployeeStats(List<Employee> employees)
  {
    List<Map> statistics = new ArrayList<>();
    employees.forEach(e -> {
      Map<String, Object> map = getBasicMap(e);
      statistics.add(map);
    });
    return statistics;
  }

  /**
   * Statistics for feedback given a list of employees.
   *
   * @param employees
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getFeedbackStats(List<Employee> employees)
  {
    List<Map> statistics = new ArrayList<>();
    employees.forEach(e -> {
      Map<String, Object> map = getBasicMap(e);
      map.put("totalFeedback", e.getFeedback().size());
      statistics.add(map);
    });
    return statistics;
  }

  private Map<String, Object> getBasicMap(Employee employee)
  {
    Map<String, Object> map = new HashMap<>();
    map.put("employeeID", employee.getEmployeeID());
    map.put("fullName", employee.getFullName());
    map.put("company", employee.getCompany());
    map.put("superSector", employee.getSuperSector());
    map.put("department", employee.getSteriaDepartment());
    return map;
  }


}
