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

  /** String[] Constant - Indicates fields to be used in the employee statistics */
  public final static String[] EMPLOYEE_FIELDS = { "employeeID", "forename", "surname", "company", "superSector",
      "steriaDepartment" };

  /** String[] Constant - Represents fields to be used in the feedback statistics */
  public final static String[] FEEDBACK_FIELDS = { "feedback" };

  /** String[] Constant - Represents fields to be used in the objectives statistics */
  public final static String[] OBJECTIVES_FIELDS = { "objectives" };

  /** String[] Constant - Represents fields to be used in the developmentNeeds statistics */
  public final static String[] DEVELOPMENT_NEEDS_FIELDS = { "developmentNeeds" };

  /**
   * Statistics from my career.
   *
   * @param users
   * @param objectives
   * @param devNeeds
   * @param notes
   * @param competencies
   * @param feedbackRequests
   * @param feedbacks
   * @return
   */
  public Map<String, Object> getMyCareerStats(long users, long objectives, long devNeeds, long notes, long competencies,
      long feedbackRequests, long feedbacks)
  {
    Map<String, Object> map = new HashMap<>();
    map.put("totalAccounts", users);
    map.put("usersWithObjectives", objectives);
    map.put("usersWithDevNeeds", devNeeds);
    map.put("usersWithNotes", notes);
    map.put("usersWithCompetencies", competencies);
    map.put("usersWithFeedbackRequests", feedbackRequests);
    map.put("usersWithFeedback", feedbacks);
    return map;
  }

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

  /**
   * Gets the standard employee details.
   *
   * @param employee
   * @return
   */
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
