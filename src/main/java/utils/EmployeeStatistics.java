package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dataStructure.DevelopmentNeed_OLD;
import dataStructure.Employee;
import dataStructure.Objective_OLD;

/**
 * Class to hold create statistic mappings.
 *
 */
public class EmployeeStatistics
{

  /** String[] Constant - Indicates fields to be used in the employee statistics */
  public final static String[] EMPLOYEE_FIELDS = { "profile.employeeID", "profile.forename", "profile.surname",
      "profile.company", "profile.superSector", "profile.steriaDepartment", "profile.accountExpires", "lastLogon" };

  /** String[] Constant - Represents fields to be used in the feedback statistics */
  public final static String[] FEEDBACK_FIELDS = { "feedback" };

  /** String[] Constant - Represents fields to be used in the objectives statistics */
  public final static String[] OBJECTIVES_FIELDS = { "objectives" };

  /** String[] Constant - Represents fields to be used in the developmentNeeds statistics */
  public final static String[] DEVELOPMENT_NEEDS_FIELDS = { "developmentNeeds" };

  /** String[] Constant - Represents fields to be used in the sector statistics */
  public final static String[] SECTOR_FIELDS = { "profile.employeeID", "profile.superSector", "objectives", "developmentNeeds" };

  /** String[] Constant - Represents fields to be used in the developmentNeeds statistics */
  public final static String[] DEVELOPMENT_NEED_CATEGORIES = { "On Job Training", "Classroom Training",
      "Online or E-Learning", "Self-Study", "Other", "INVALID" };

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
      map.put("currentEmployee", e.getProfile().getAccountExpires() == null);
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
      addObjectivesCounts(map, e.getLatestVersionObjectives());
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
      addDevNeedsCounts(map, e.getLatestVersionDevelopmentNeeds());
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
      e.getLatestVersionDevelopmentNeeds().forEach(d -> {
        if (d.getProgress() == 2) return;
        Map<String, Object> map = getBasicMap(e);
        map.put("title", d.getTitle());
        map.put("category", DEVELOPMENT_NEED_CATEGORIES[d.getCategory()]);
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
    return map;
  }

  /**
   * Gets counts for the stages of development needs
   *
   * @param map
   * @param devNeeds
   */
  private void addDevNeedsCounts(Map<String, Object> map, List<DevelopmentNeed_OLD> devNeeds)
  {
    int proposed = 0, inProgress = 0, complete = 0;
    for (DevelopmentNeed_OLD devNeed : devNeeds)
    {
      switch (devNeed.getProgress())
      {
        case 0:
          proposed++;
          break;
        case 1:
          inProgress++;
          break;
        case 2:
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
   */
  private void addObjectivesCounts(Map<String, Object> map, List<Objective_OLD> objectives)
  {
    int proposed = 0, inProgress = 0, complete = 0, total = 0;
    if (!objectives.isEmpty())
    {
      for (Objective_OLD objective : objectives)
      {
        if (objective.getIsArchived()) continue;
        switch (objective.getProgress())
        {
          case 0:
            proposed++;
            break;
          case 1:
            inProgress++;
            break;
          case 2:
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
    
    Optional<Map<String, Object>> optionalMap = statistics.stream().filter(m -> m.get("sector").equals(sector)).findFirst();
    if (optionalMap.isPresent())
    {
      optionalMap.get().put("employees", (Integer) optionalMap.get().get("employees") + 1);
      if (!employee.getLatestVersionObjectives().isEmpty())
      {
        optionalMap.get().put("noWithObjs", (Integer) optionalMap.get().get("noWithObjs") + 1);
      }
      if (!employee.getLatestVersionDevelopmentNeeds().isEmpty())
      {
        optionalMap.get().put("noWithDevNeeds", (Integer) optionalMap.get().get("noWithDevNeeds") + 1);
      }
    }
    else
    {
      
      map.put("sector", sector);
      map.put("employees", 1);
      map.put("noWithObjs", (employee.getLatestVersionObjectives().isEmpty()) ? 0 : 1 );
      map.put("noWithDevNeeds", (employee.getLatestVersionDevelopmentNeeds().isEmpty()) ? 0 : 1 );
      statistics.add(map);
    }
  }

}
