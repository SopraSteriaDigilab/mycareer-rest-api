package services;

import static org.apache.commons.lang3.ArrayUtils.addAll;
import static utils.EmployeeStatistics.DEVELOPMENT_NEEDS_FIELDS;
import static utils.EmployeeStatistics.EMPLOYEE_FIELDS;
import static utils.EmployeeStatistics.FEEDBACK_FIELDS;
import static utils.EmployeeStatistics.OBJECTIVES_FIELDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import dataStructure.Employee;
import domain.HRData;
import domain.HRDevNeedsData;
import domain.HRObjectiveData;
import utils.EmployeeStatistics;

/**
 * HR Service class.
 * 
 * @version 1.0
 * @since 23rd January 2017
 */
public class HRDataDAO
{

  private static Datastore dbConnection;

  private EmployeeStatistics employeeStats = new EmployeeStatistics();

  public HRDataDAO()
  {
  }

  public HRDataDAO(Datastore dbConnection)
  {
    HRDataDAO.dbConnection = dbConnection;
  }

  public HRData getHRData()
  {
    List<HRObjectiveData> hrObjectiveData = getHRObjectiveData();
    List<HRDevNeedsData> hrDevNeedsData = getHRDevNeedsData();
    HRData hrData = new HRData(hrObjectiveData, hrDevNeedsData);
    return hrData;
  }

  public List<HRObjectiveData> getHRObjectiveData()
  {
    List<HRObjectiveData> hrObjectiveList = new ArrayList<>();
    List<Employee> query = employeeQuery().field("objectives").exists()
        .retrievedFields(true, "forename", "surname", "employeeID", "objectives").asList();

    if (!query.isEmpty())
    {
      for (Employee employee : query)
      {
        hrObjectiveList.add(new HRObjectiveData(employee.getEmployeeID(), employee.getFullName(),
            employee.getLatestVersionObjectives()));
      }
    }
    return hrObjectiveList;
  }

  public List<HRDevNeedsData> getHRDevNeedsData()
  {
    List<HRDevNeedsData> hrDevNeedsList = new ArrayList<>();
    List<Employee> query = employeeQuery().field("developmentNeeds").exists()
        .retrievedFields(true, "forename", "surname", "employeeID", "developmentNeeds").asList();

    if (!query.isEmpty())
    {
      for (Employee employee : query)
      {
        hrDevNeedsList.add(new HRDevNeedsData(employee.getEmployeeID(), employee.getFullName(),
            employee.getLatestVersionDevelopmentNeeds()));
      }
    }
    return hrDevNeedsList;
  }

  public Map<String, Object> getMyCareerStats()
  {
    return employeeStats.getMyCareerStats(employeeQuery().countAll(), countAll("objectives"),
        countAll("developmentNeeds"), countAll("notes"), countAll("competencies"), countAll("feedbackRequests"),
        countAll("feedback"));
  }

  /**
   * Statistics for employees from the database.
   *
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getEmployeeStats()
  {
    List<Employee> employees = employeeQuery().retrievedFields(true, EMPLOYEE_FIELDS).asList();
    return employeeStats.getEmployeeStats(employees);
  }

  /**
   * Statistics for feedback from the employees from the database.
   *
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getFeedbackStats()
  {
    List<Employee> employees = employeeQuery().field("feedback").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, FEEDBACK_FIELDS)).asList();
    return employeeStats.getFeedbackStats(employees);
  }

  /**
   *  Statistics for objectives from the employees from the database.
   *
   * @return
   */
  public Object getObjectiveStats()
  {
    List<Employee> employees = employeeQuery().field("objectives").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, OBJECTIVES_FIELDS)).asList();
    return employeeStats.getObjectiveStats(employees);
  }

  /**
   *  Statistics for objectives from the employees from the database.
   *
   * @return
   */
  public Object getDevelopmentNeedStats()
  {
    List<Employee> employees = employeeQuery().field("developmentNeeds").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, DEVELOPMENT_NEEDS_FIELDS)).asList();
    return employeeStats.getDevelopmentNeedStats(employees);
  }
  
  private Query<Employee> employeeQuery()
  {
    return dbConnection.find(Employee.class);
  }

  private long countAll(String field)
  {
    return employeeQuery().field(field).exists().countAll();
  }
}
