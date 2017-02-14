
package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.Datastore;

import dataStructure.Employee;
import domain.HRData;
import domain.HRDevNeedsData;
import domain.HRObjectiveData;
import domain.HRTotals;

/**
 * HR Service class.
 * 
 * @version 1.0
 * @since 23rd January 2017
 */
public class HRDataDAO
{

  private final String[] FEEDBACK_FIELDS = { "forename", "surname", "employeeID", "objectives" };

  private static Datastore dbConnection;

  public HRDataDAO(Datastore dbConnection)
  {
    HRDataDAO.dbConnection = dbConnection;
  }

  public static long getTotalNumberOfUsers()
  {
    long totalAccounts = dbConnection.find(Employee.class).countAll();
    return totalAccounts;
  }

  public static long getTotalUsersWithObjectives()
  {
    long totalUsersWithObjectives = dbConnection.find(Employee.class).field("objectives").exists().countAll();
    return totalUsersWithObjectives;
  }

  public static long getTotalUsersWithDevelopmentNeeds()
  {
    long totalUsersWithDevelopmentNeeds = dbConnection.find(Employee.class).field("developmentNeeds").exists()
        .countAll();
    return totalUsersWithDevelopmentNeeds;
  }

  public static long getTotalUsersWithNotes()
  {
    long totalUsersWithNotes = dbConnection.find(Employee.class).field("notes").exists().countAll();
    return totalUsersWithNotes;
  }

  public static long getTotalUsersWithCompetencies()
  {
    long totalUsersWithCompetencies = dbConnection.find(Employee.class).field("competencies").exists().countAll();
    return totalUsersWithCompetencies;
  }

  public static long getTotalUsersWithSubmittedFeedback()
  {
    long totalUsersWithSubmittedFeedback = dbConnection.find(Employee.class).field("feedbackRequests").exists()
        .countAll();
    return totalUsersWithSubmittedFeedback;
  }

  public static long getTotalUsersWithFeedback()
  {
    long totalUsersWithFeedback = dbConnection.find(Employee.class).field("feedback").exists().countAll();
    return totalUsersWithFeedback;
  }

  public static HRData getHRData()
  {
    HRTotals hrTotals = getHRTotals();

    List<HRObjectiveData> hrObjectiveData = getHRObjectiveData();
    List<HRDevNeedsData> hrDevNeedsData = getHRDevNeedsData();

    HRData hrData = new HRData(hrTotals, hrObjectiveData, hrDevNeedsData);
    return hrData;
  }

  public static HRTotals getHRTotals()
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

  public static List<HRObjectiveData> getHRObjectiveData()
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

  public static List<HRDevNeedsData> getHRDevNeedsData()
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

//  public static List<Map> getFeedbackStats()
//  {
//    List<Map> a = new ArrayList<>();
//    List<Employee> query = dbConnection.createQuery(Employee.class).field("feedback").exists()
//        .retrievedFields(true, "employeeID", "forename", "surname", "company", "superSector", "steriaDepartment").asList();
//    // TODO
//    for(Employee emp : query){
//      Map<String, Object> m = new HashMap<>(); 
//      m.put("employeeID", emp.getEmployeeID());
//      m.put("fullName", emp.getFullName());
//      m.put("totalFeedback", emp.getFullName());
//      m.put("company", emp.getCompany());
//      m.put("superSector", emp.s;
//      m.put("department", emp.getDe());
//      a.add(m);
//      
//    }
//    return a;
//  }

}
