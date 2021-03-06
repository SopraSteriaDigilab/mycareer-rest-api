package services;

import static dataStructure.EmployeeProfile.EMPLOYEE_ID;
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
import services.db.MorphiaOperations;
import utils.EmployeeStatistics;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class HRService
{
  /** TODO describe */
  public static final String ACCOUNT_EXPIRES = "profile.accountExpires";

  /* Datastore Constant - Represents connection to the database */
  private final Datastore datastore;

  private final MorphiaOperations morphiaOperations;

  /* EmployeeStatistics Constant - Represents employeeStats reference */
  private final EmployeeStatistics employeeStats = new EmployeeStatistics();

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param datastore
   * @param morphiaOperations
   */
  public HRService(final Datastore datastore, final MorphiaOperations morphiaOperations)
  {
    this.datastore = datastore;
    this.morphiaOperations = morphiaOperations;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public Employee getMyCareer(long employeeId) throws EmployeeNotFoundException
  {
    return morphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, employeeId);
  }

  /**
   * Statistics for MyCareer from the database.
   *
   * @return A Map<String, Object> with the Statistics for MyCareer
   */
  public Map<String, Long> getMyCareerStats()
  {
    Query<Employee> empQuery = employeeQuery();
    return employeeStats.getMyCareerStats(countAll(empQuery, "lastLogon"), countAll(empQuery, "objectives"),
        countAll(empQuery, "developmentNeeds"), countAll(empQuery, "notes"), countAll(empQuery, "competencies"),
        countAll(empQuery, "feedbackRequests"), countAll(empQuery, "feedback"));
  }

  /**
   * Statistics for employees from the database.
   *
   * @return List<Map> of employees that have logged in.
   */
  public List<Map<String, Object>> getEmployeeStats()
  {
    List<Employee> employees = datastore.find(Employee.class).retrievedFields(true, EMPLOYEE_FIELDS).asList();
    return employeeStats.getEmployeeStats(employees);
  }

  /**
   * Statistics for feedback from the employees from the database.
   *
   * @return List<Map> of statistics for feedback
   */
  public List<Map<String, Object>> getFeedbackStats()
  {
    List<Employee> employees = datastore.find(Employee.class).field(ACCOUNT_EXPIRES).doesNotExist().field("feedback")
        .exists().retrievedFields(true, addAll(EMPLOYEE_FIELDS, FEEDBACK_FIELDS)).asList();
    return employeeStats.getFeedbackStats(employees);
  }

  /**
   * Statistics for objectives from the employees from the database.
   *
   * @return List<Map> of statistics for objectives
   */
  public List<Map<String, Object>> getObjectiveStats()
  {
    List<Employee> employees = datastore.find(Employee.class).field(ACCOUNT_EXPIRES).doesNotExist()
        .retrievedFields(true, addAll(EMPLOYEE_FIELDS, OBJECTIVES_FIELDS)).asList();
    return employeeStats.getObjectiveStats(employees);
  }

  /**
   * Statistics for development needs from the employees from the database.
   *
   * @return List<Map> of statistics for development needs
   */
  public List<Map<String, Object>> getDevelopmentNeedStats()
  {
    List<Employee> employees = datastore.find(Employee.class).field(ACCOUNT_EXPIRES).doesNotExist()
        .field("developmentNeeds").exists().retrievedFields(true, addAll(EMPLOYEE_FIELDS, DEVELOPMENT_NEEDS_FIELDS))
        .asList();
    return employeeStats.getDevelopmentNeedStats(employees);
  }

  /**
   * 
   * Break down of development needs with category, not including complete ones.
   *
   * @return List<Map> of development need breakdown statistics
   */
  public List<Map<String, Object>> getDevelopmentNeedBreakDown()
  {
    List<Employee> employees = datastore.find(Employee.class).field(ACCOUNT_EXPIRES).doesNotExist()
        .field("developmentNeeds").exists().retrievedFields(true, addAll(EMPLOYEE_FIELDS, DEVELOPMENT_NEEDS_FIELDS))
        .asList();
    return employeeStats.getDevelopmentNeedBreakDown(employees);
  }

  /**
   * Break down of number of employees, objectives and development needs per section.
   *
   * @return Map<String, Object> of sector need breakdown statistics
   */
  public List<Map<String, Object>> getSectorBreakDown()
  {
    List<Employee> employees = datastore.find(Employee.class).field(ACCOUNT_EXPIRES).doesNotExist()
        .retrievedFields(true, SECTOR_FIELDS).asList();
    return employeeStats.getSectorBreakDown(employees);
  }

  private Query<Employee> employeeQuery()
  {
    return datastore.find(Employee.class);
  }

  private long countAll(Query<Employee> query, String field)
  {
    return datastore.find(Employee.class).field(ACCOUNT_EXPIRES).doesNotExist().field(field).exists().countAll();
  }
}
