package services;

import static org.apache.commons.lang3.ArrayUtils.addAll;
import static utils.EmployeeStatistics.DEVELOPMENT_NEEDS_FIELDS;
import static utils.EmployeeStatistics.EMPLOYEE_FIELDS;
import static utils.EmployeeStatistics.FEEDBACK_FIELDS;
import static utils.EmployeeStatistics.OBJECTIVES_FIELDS;
import static utils.EmployeeStatistics.SECTOR_FIELDS;

import java.util.List;
import java.util.Map;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import dataStructure.Employee;
import utils.EmployeeStatistics;

/**
 * HR Service class.
 * 
 */
public class HRService
{

  /** Datastore Constant - Represents connection to the database */
  private static Datastore dbConnection;

  /** EmployeeStatistics Constant - Represents employeeStats reference */
  private final EmployeeStatistics employeeStats = new EmployeeStatistics();

  /**
   * Empty Constructor - Responsible for initialising this object.
   *
   */
  public HRService()
  {
  }

  /**
   * Datastore Constructor - Responsible for injecting the database connection to this object.
   *
   * @param dbConnection
   */
  public HRService(Datastore dbConnection)
  {
    HRService.dbConnection = dbConnection;
  }

  /**
   * Statistics for MyCareer from the database.
   *
   * @return A Map<String, Object> with the Statistics for MyCareer
   */
  public Map<String, Object> getMyCareerStats()
  {
    Query<Employee> empQuery = employeeQuery();
    return employeeStats.getMyCareerStats(empQuery.countAll(), countAll(empQuery, "objectives"),
        countAll(empQuery, "developmentNeeds"), countAll(empQuery, "notes"), countAll(empQuery, "competencies"),
        countAll(empQuery, "feedbackRequests"), countAll(empQuery, "feedback"));
  }

  /**
   * Statistics for employees from the database.
   *
   * @return List<Map> of employees that have logged in.
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
   * @return List<Map> of statistics for feedback
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getFeedbackStats()
  {
    List<Employee> employees = employeeQuery().field("feedback").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, FEEDBACK_FIELDS)).asList();
    return employeeStats.getFeedbackStats(employees);
  }

  /**
   * Statistics for objectives from the employees from the database.
   *
   * @return List<Map> of statistics for objectives
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getObjectiveStats()
  {
    List<Employee> employees = employeeQuery().retrievedFields(true, addAll(EMPLOYEE_FIELDS, OBJECTIVES_FIELDS))
        .asList();
    return employeeStats.getObjectiveStats(employees);
  }

  /**
   * Statistics for development needs from the employees from the database.
   *
   * @return List<Map> of statistics for development needs
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getDevelopmentNeedStats()
  {
    List<Employee> employees = employeeQuery().field("developmentNeeds").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, DEVELOPMENT_NEEDS_FIELDS)).asList();
    return employeeStats.getDevelopmentNeedStats(employees);
  }

  /**
   * 
   * Break down of development needs with category, not including complete ones.
   *
   * @return List<Map> of development need breakdown statistics
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getDevelopmentNeedBreakDown()
  {
    List<Employee> employees = employeeQuery().field("developmentNeeds").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, DEVELOPMENT_NEEDS_FIELDS)).asList();
    return employeeStats.getDevelopmentNeedBreakDown(employees);
  }

  /**
   * Break down of number of employees, objectives and development needs per section.
   *
   * @return Map<String, Object> of sector need breakdown statistics
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getSectorBreakDown()
  {
    List<Employee> employees = employeeQuery().retrievedFields(true, SECTOR_FIELDS).asList();
    return employeeStats.getSectorBreakDown(employees);
  }

  /**
   * Employee query
   * 
   * @return An Employee query
   */
  private Query<Employee> employeeQuery()
  {
    return dbConnection.find(Employee.class);
  }

  /**
   * Counts all the employees with the existing field
   *
   * @param query The query for the check
   * @param field The field to check
   * @return The number of employees with the field.
   */
  private long countAll(Query<Employee> query, String field)
  {
    return query.field(field).exists().countAll();
  }

}
