package services;

import static utils.EmployeeStatistics.EMPLOYEE_FIELDS;
import static utils.EmployeeStatistics.FEEDBACK_FIELDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.Datastore;

import dataStructure.Employee;
import domain.HRData;
import domain.HRDevNeedsData;
import domain.HRObjectiveData;
import domain.HRTotals;
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

  private EmployeeStatistics stats = new EmployeeStatistics();

  public HRDataDAO()
  {
  }

  public HRDataDAO(Datastore dbConnection)
  {
    HRDataDAO.dbConnection = dbConnection;
  }

  public long getTotalNumberOfUsers()
  {
    long totalAccounts = dbConnection.find(Employee.class).countAll();
    return totalAccounts;
  }

  public long getTotalUsersWithObjectives()
  {
    long totalUsersWithObjectives = dbConnection.find(Employee.class).field("objectives").exists().countAll();
    return totalUsersWithObjectives;
  }

  public long getTotalUsersWithDevelopmentNeeds()
  {
    long totalUsersWithDevelopmentNeeds = dbConnection.find(Employee.class).field("developmentNeeds").exists()
        .countAll();
    return totalUsersWithDevelopmentNeeds;
  }

  public long getTotalUsersWithNotes()
  {
    long totalUsersWithNotes = dbConnection.find(Employee.class).field("notes").exists().countAll();
    return totalUsersWithNotes;
  }

  public long getTotalUsersWithCompetencies()
  {
    long totalUsersWithCompetencies = dbConnection.find(Employee.class).field("competencies").exists().countAll();
    return totalUsersWithCompetencies;
  }

  public long getTotalUsersWithSubmittedFeedback()
  {
    long totalUsersWithSubmittedFeedback = dbConnection.find(Employee.class).field("feedbackRequests").exists()
        .countAll();
    return totalUsersWithSubmittedFeedback;
  }

  public long getTotalUsersWithFeedback()
  {
    long totalUsersWithFeedback = dbConnection.find(Employee.class).field("feedback").exists().countAll();
    return totalUsersWithFeedback;
  }

  public HRData getHRData()
  {
    HRTotals hrTotals = getHRTotals();

    List<HRObjectiveData> hrObjectiveData = getHRObjectiveData();
    List<HRDevNeedsData> hrDevNeedsData = getHRDevNeedsData();

    HRData hrData = new HRData(hrTotals, hrObjectiveData, hrDevNeedsData);
    return hrData;
  }

  public HRTotals getHRTotals()
  {
    long totalAccounts = getTotalNumberOfUsers();
    long totalUsersWithObjectives = getTotalUsersWithObjectives();
    long totalUsersWithDevelopmentNeeds = getTotalUsersWithDevelopmentNeeds();
    long totalUsersWithNotes = getTotalUsersWithNotes();
    long totalUsersWithCompetencies = getTotalUsersWithCompetencies();
    long totalUsersWithSubmittedFeedback = getTotalUsersWithSubmittedFeedback();
    long totalUsersWithFeedback = getTotalUsersWithFeedback();

    return new HRTotals(totalAccounts, totalUsersWithObjectives, totalUsersWithDevelopmentNeeds, totalUsersWithNotes,
        totalUsersWithCompetencies, totalUsersWithSubmittedFeedback, totalUsersWithFeedback);
  }

  public List<HRObjectiveData> getHRObjectiveData()
  {
    List<HRObjectiveData> hrObjectiveList = new ArrayList<>();
    List<Employee> query = dbConnection.createQuery(Employee.class).field("objectives").exists()
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
    List<Employee> query = dbConnection.createQuery(Employee.class).field("developmentNeeds").exists()
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

  /**
   * Statistics for feedback from the employees from the database.
   *
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getFeedbackStats()
  {
    List<Employee> employees = dbConnection.createQuery(Employee.class).field("feedback").exists()
        .retrievedFields(true, FEEDBACK_FIELDS).asList();
    return stats.getFeedbackStats(employees);
  }

  /**
   * Statistics for employees from the database.
   *
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getEmployeeStats()
  {
    List<Employee> employees = dbConnection.createQuery(Employee.class).retrievedFields(true, EMPLOYEE_FIELDS).asList();
    return stats.getEmployeeStats(employees);
  }

}
