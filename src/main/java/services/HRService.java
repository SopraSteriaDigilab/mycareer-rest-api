package services;

import static org.apache.commons.lang3.ArrayUtils.addAll;
import static utils.EmployeeStatistics.DEVELOPMENT_NEEDS_FIELDS;
import static utils.EmployeeStatistics.EMPLOYEE_FIELDS;
import static utils.EmployeeStatistics.FEEDBACK_FIELDS;
import static utils.EmployeeStatistics.OBJECTIVES_FIELDS;

import java.util.List;
import java.util.Map;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import dataStructure.Employee;
import utils.EmployeeStatistics;

/**
 * HR Service class.
 * 
 * @version 1.0
 * @since 23rd January 2017
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
   * @return Statistics for MyCareer
   */
  public Map<String, Object> getMyCareerStats()
  {
    return employeeStats.getMyCareerStats(employeeQuery().countAll(), countAll("objectives"),
        countAll("developmentNeeds"), countAll("notes"), countAll("competencies"), countAll("feedbackRequests"),
        countAll("feedback"));
  }

  /**
   * Statistics for employees from the database.
   *
   * @return List of employees that have logged in.
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
   * @return Statistics for feedback
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
   * @return Statistics for objectives
   */
  public Object getObjectiveStats()
  {
    List<Employee> employees = employeeQuery().field("objectives").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, OBJECTIVES_FIELDS)).asList();
    return employeeStats.getObjectiveStats(employees);
  }

  /**
   * Statistics for development needs from the employees from the database.
   *
   * @return Statistics for development needs
   */
  public Object getDevelopmentNeedStats()
  {
    List<Employee> employees = employeeQuery().field("developmentNeeds").exists()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, DEVELOPMENT_NEEDS_FIELDS)).asList();
    return employeeStats.getDevelopmentNeedStats(employees);
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
   * @param field The field to check
   * @return The number of employees with the field.
   */
  private long countAll(String field)
  {
    return employeeQuery().field(field).exists().countAll();
  }
}
