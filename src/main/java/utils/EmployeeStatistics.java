package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataStructure.Employee;

/**
 * Class to hold create statistic mappings.
 *
 */
public class EmployeeStatistics
{
  
  /** String[] Constant - Indicates fields to be used in the feedback statistics */
  public final static String[] FEEDBACK_FIELDS = { "forename", "surname", "employeeID", "company", "superSector",
      "steriaDepartment", "feedback" };

  /**
   * Statistics for feedback given a list of employees.
   *
   * @param employees
   * @return 
   */
  public List<Map<String, Object>> getFeedbackStats(List<Employee> employees)
  {
    List<Map<String, Object>> statistics = new ArrayList<>();
    employees.forEach(e -> {
      Map<String, Object> map = new HashMap<>();
      map.put("employeeID", e.getEmployeeID());
      map.put("fullName", e.getFullName());
      map.put("totalFeedback", e.getFeedback().size());
      map.put("company", e.getCompany());
      map.put("superSector", e.getSuperSector());
      map.put("department", e.getSteriaDepartment());
      statistics.add(map);
    });
    return statistics;
  }

}
