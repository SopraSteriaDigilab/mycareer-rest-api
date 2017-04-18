package utils;

import static dataStructure.Objective.Progress.*;
import static dataStructure.EmployeeProfile.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;

import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.Objective;

/**
 * Class to hold create statistic mappings.
 *
 */
public class EmployeeStatistics
{

  /** String[] Constant - Indicates fields to be used in the employee statistics */
  public final static String[] EMPLOYEE_FIELDS = { EMPLOYEE_ID, FORENAME, SURNAME,
      COMPANY, SUPER_SECTOR, DEPARTMENT, ACCOUNT_EXPIRES, "lastLogon" };

  /** String[] Constant - Represents fields to be used in the feedback statistics */
  public final static String[] FEEDBACK_FIELDS = { "feedback" };

  /** String[] Constant - Represents fields to be used in the objectives statistics */
  public final static String[] OBJECTIVES_FIELDS = { "objectives" };

  /** String[] Constant - Represents fields to be used in the developmentNeeds statistics */
  public final static String[] DEVELOPMENT_NEEDS_FIELDS = { "developmentNeeds" };

  /** String[] Constant - Represents fields to be used in the sector statistics */
  public final static String[] SECTOR_FIELDS = { EMPLOYEE_ID, SUPER_SECTOR, "objectives",
      "developmentNeeds" };

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
   * @return A Map<String, Object> with the statistics
   */
  public Map<String, Long> getMyCareerStats(long users, long objectives, long devNeeds, long notes, long competencies,
      long feedbackRequests, long feedbacks)
  {
    Map<String, Long> map = new HashMap<>();
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
   * @return A List<Map> with the statistics
   */
  public List<Map<String, Object>> getEmployeeStats(List<Employee> employees)
  {
    List<Map<String, Object>> statistics = new ArrayList<>();
    employees.forEach(e -> {
      Map<String, Object> map = getBasicMap(e);
      map.put("lastLogon",
          (e.getLastLogon() == null) ? "Never" : Utils.DateToLocalDateTime(e.getLastLogon()).toString());
      statistics.add(map);
    });
    return statistics;
  }

  /**
   * Statistics for feedback given a list of employees.
   *
   * @param employees
   * @return A List<Map> with the statistics
   */
  public List<Map<String, Object>> getFeedbackStats(List<Employee> employees)
  {
    List<Map<String, Object>> statistics = new ArrayList<>();
    employees.forEach(e -> {
      Map<String, Object> map = getBasicMap(e);
      map.put("totalFeedback", e.getFeedback().size());
      statistics.add(map);
    });
    return statistics;
  }

  /**
   * Statistics for objectives given employees
   *
   * @param employees
   * @return A List<Map> with the statistics
   */
  public List<Map<String, Object>> getObjectiveStats(List<Employee> employees)
  {
    List<Map<String, Object>> statistics = new ArrayList<>();
    employees.forEach(e -> {
      Map<String, Object> map = getBasicMap(e);
      addObjectivesCounts(map, e.getObjectivesNEW());
      statistics.add(map);
    });
    return statistics;
  }

  /**
   * Statistics for development needs given employees
   *
   * @param employees
   * @return A List<Map> with the statistics
   */
  public List<Map<String, Object>> getDevelopmentNeedStats(List<Employee> employees)
  {
    List<Map<String, Object>> statistics = new ArrayList<>();
    employees.forEach(e -> {
      Map<String, Object> map = getBasicMap(e);
      addDevNeedsCounts(map, e.getDevelopmentNeedsNEW());
      statistics.add(map);
    });
    return statistics;
  }

  /**
   * Details of development needs per employee with category and title
   *
   * @param employees
   * @return A List<Map> with the statistics
   */
  public List<Map<String, Object>> getDevelopmentNeedBreakDown(List<Employee> employees)
  {
    List<Map<String, Object>> statistics = new ArrayList<>();
    employees.forEach(e -> {
      e.getDevelopmentNeedsNEW().forEach(d -> {
        if (getProgressFromString(d.getProgress()).equals(COMPLETE)) return;
        Map<String, Object> map = getBasicMap(e);
        map.put("title", d.getTitle());
        map.put("category", d.getCategory());
        statistics.add(map);
      });
    });
    return statistics;
  }

  /**
   * Details of sector break down details
   *
   * @param employees
   * @return A List<Map> with the statistics
   */
  public List<Map<String, Object>> getSectorBreakDown(List<Employee> employees)
  {
    List<Map<String, Object>> statistics = new ArrayList<>();
    employees.forEach(e -> groupSector(statistics, e));
    statistics.forEach(m -> {
      double e = ((Integer) m.get("employees"));
      double o = ((Integer) m.get("noWithObjs"));
      double dn = ((Integer) m.get("noWithDevNeeds"));
      m.put("percentObjs", (Math.ceil((o / e) * 100)));
      m.put("percentDevNeeds", (Math.ceil((dn / e) * 100)));
    });
    return statistics;
  }

  /**
   * Gets the standard employee details.
   *
   * @param employee
   * @return A List<Map> with the statistics
   */
  private Map<String, Object> getBasicMap(Employee employee)
  {
    Map<String, Object> map = new HashMap<>();
    map.put("employeeID", employee.getProfile().getEmployeeID());
    map.put("fullName", employee.getProfile().getFullName());
    map.put("company", employee.getProfile().getCompany());
    map.put("superSector", employee.getProfile().getSuperSector());
    map.put("department", employee.getProfile().getSteriaDepartment());
    map.put("currentEmployee", employee.getProfile().getAccountExpires() == null);
    return map;
  }

  /**
   * Gets counts for the stages of development needs
   *
   * @param map
   * @param devNeeds
   * @throws InvalidAttributeValueException 
   */
  private void addDevNeedsCounts(Map<String, Object> map, List<DevelopmentNeed> devNeeds)
  {
    int proposed = 0, inProgress = 0, complete = 0;
    for (DevelopmentNeed devNeed : devNeeds)
    {
      switch (getProgressFromString(devNeed.getProgress()))
      {
        case PROPOSED:
          proposed++;
          break;
        case IN_PROGRESS:
          inProgress++;
          break;
        case COMPLETE:
          complete++;
          break;
      }
    }
    map.put("totalDevelopmentNeeds", devNeeds.size());
    map.put("proposed", proposed);
    map.put("inProgress", inProgress);
    map.put("complete", complete);
  }

  /**
   * Gets counts for the stages of objectives
   *
   * @param map
   * @param objectives
   * @throws InvalidAttributeValueException
   */
  private void addObjectivesCounts(Map<String, Object> map, List<Objective> objectives)
  {
    int proposed = 0, inProgress = 0, complete = 0, total = 0;
    if (!objectives.isEmpty())
    {
      for (Objective objective : objectives)
      {
        if (objective.getArchived()) continue;
        switch (getProgressFromString(objective.getProgress()))
        {
          case PROPOSED:
            proposed++;
            break;
          case IN_PROGRESS:
            inProgress++;
            break;
          case COMPLETE:
            complete++;
            break;
        }
        total++;
      }
    }
    map.put("totalObjectives", total);
    map.put("proposed", proposed);
    map.put("inProgress", inProgress);
    map.put("complete", complete);
  }

  /**
   * Add the employee to a list of maps grouped by super sector
   *
   * @param statistics
   * @param employee
   */
  private void groupSector(List<Map<String, Object>> statistics, Employee employee)
  {
    Map<String, Object> map = new HashMap<>();

    String profileSector = employee.getProfile().getSuperSector();
    String sector = (profileSector == null) ? "unknown" : profileSector;

    Optional<Map<String, Object>> optionalMap = statistics.stream().filter(m -> m.get("sector").equals(sector))
        .findFirst();
    if (optionalMap.isPresent())
    {
      optionalMap.get().put("employees", (Integer) optionalMap.get().get("employees") + 1);
      if (!employee.getObjectivesNEW().isEmpty())
      {
        optionalMap.get().put("noWithObjs", (Integer) optionalMap.get().get("noWithObjs") + 1);
      }
      if (!employee.getDevelopmentNeedsNEW().isEmpty())
      {
        optionalMap.get().put("noWithDevNeeds", (Integer) optionalMap.get().get("noWithDevNeeds") + 1);
      }
    }
    else
    {

      map.put("sector", sector);
      map.put("employees", 1);
      map.put("noWithObjs", (employee.getObjectivesNEW().isEmpty()) ? 0 : 1);
      map.put("noWithDevNeeds", (employee.getDevelopmentNeedsNEW().isEmpty()) ? 0 : 1);
      statistics.add(map);
    }
  }

}
