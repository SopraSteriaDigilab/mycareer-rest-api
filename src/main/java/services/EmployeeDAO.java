package services;

import static dataStructure.Constants.DEVELOPMENTNEED_NOTADDED_ERROR;
import static dataStructure.Constants.INVALID_COMPETENCY_CONTEXT;
import static dataStructure.Constants.INVALID_COMPETENCY_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_CONTEXT_PROGRESS;
import static dataStructure.Constants.INVALID_CONTEXT_USERID;
import static dataStructure.Constants.INVALID_DEVNEEDID_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_IDNOTFOND;
import static dataStructure.Constants.INVALID_NOTE;
import static dataStructure.Constants.INVALID_NOTEID;
import static dataStructure.Constants.INVALID_NOTE_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_OBJECTIVE;
import static dataStructure.Constants.INVALID_OBJECTIVEID;
import static dataStructure.Constants.INVALID_OBJECTIVE_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_USERGUID_NOTFOUND;
import static dataStructure.Constants.NOTE_NOTADDED_ERROR;
import static dataStructure.Constants.NULL_OBJECTIVE;
import static dataStructure.Constants.NULL_USER_DATA;
import static dataStructure.Constants.OBJECTIVE_NOTADDED_ERROR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

import dataStructure.ADProfile_Advanced;
import dataStructure.ADProfile_Basic;
import dataStructure.Competency;
import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import services.ad.ADProfileDAO;
import services.ews.EmailService;
import services.validate.Validate;
import utils.Utils;

/**
 * This class contains the definition of the EmployeeDAO object
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
 * @author Ridhwan Nacef
 * @author Mehmet Mehmet
 * @version 1.0
 * @since 10th October 2016
 *
 */
public class EmployeeDAO
{

  private static final Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);

  // There is only 1 instance of the Datastore in the whole system
  private static Datastore dbConnection;

  /** String Constant - Represents Feedback Request */
  public final static String FEEDBACK_REQUEST = "Feedback Request";

  /**
   * EmployeeDAO Constructor - Needed for mongoDB.
   *
   */
  public EmployeeDAO()
  {
  }

  /**
   * EmployeeDAO Constructor - Responsible for initialising dbConnection.
   *
   * @param dbConnection
   */
  public EmployeeDAO(Datastore dbConnection)
  {
    EmployeeDAO.dbConnection = dbConnection;
  }

  /**
   * Gets Employee from database with the specified id
   *
   * @param employeeID ID of the employee
   * @return the employee if exists
   * @throws InvalidAttributeValueException if employee is not found or is null.
   */
  public static Employee getEmployee(long employeeID) throws InvalidAttributeValueException
  {
    Employee employee = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
    if (employee == null)
    {
      throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
    }
    return employee;
  }

  /**
   * 
   * @deprecated Use the {@linkplain #getEmployee(long) getEmployee(long)} method instead.
   *
   * @param employeeID
   * @return
   */
  private static Query<Employee> getEmployeeQuery(long employeeID)
  {
    return dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
  }

  public static String getFullNameUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getFullName();
  }

  public static List<Objective> getObjectivesForUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionObjectives();
  }

  public static Objective getSpecificObjectiveForUser(long employeeID, int objectiveID)
      throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionOfSpecificObjective(objectiveID);
  }

  public static List<Feedback> getFeedbackForUser(long employeeID) throws InvalidAttributeValueException
  {
    List<Feedback> feedbackList = getEmployee(employeeID).getFeedback();
    Collections.reverse(feedbackList);
    return feedbackList;
  }

  public static List<Note> getNotesForUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionNotes();
  }

  public static List<DevelopmentNeed> getDevelopmentNeedsForUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionDevelopmentNeeds();
  }

  public static String getAllUserDataFromID(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).toString();
  }

  // Returns list of Competencies for a user
  public static List<Competency> getCompetenciesForUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionCompetencies();
  }

  // Returns list of reportees for a user
  public static List<ADProfile_Basic> getReporteesForUser(long employeeID)
      throws InvalidAttributeValueException, NamingException
  {
    Employee employee = getEmployee(employeeID);

    List<ADProfile_Basic> reporteeList = new ArrayList<>();

    for (String str : employee.getReporteeCNs())
    {
      long temp = Long.parseLong(str.substring(str.indexOf('-') + 1).trim());
      try
      {
        reporteeList.add(ADProfileDAO.verifyIfUserExists(temp));
      }
      catch (Exception e)
      {
        throw new InvalidAttributeValueException("Sorry there were some connectiviy errors. Please try again later.");
      }
    }
    return reporteeList;
  }

  /**
   * 
   * @param employeeID
   * @return This method inserts a new objective for a specific employee given their ID
   * @throws InvalidAttributeValueException
   */
  public static boolean insertNewObjective(long employeeID, Object data)
      throws InvalidAttributeValueException, MongoException
  {
    // Check the employeeID
    if (employeeID > 0)
    {
      if (data != null && data instanceof Objective)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          // Add the new objective to the list
          if (e.addObjective((Objective) data))
          {
            // Update the List<List<objective>> in the DB passing the new list
            UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("objectives",
                e.getObjectiveList());
            // Commit the changes to the DB
            dbConnection.update(querySearch, ops);
            return true;
          }
          else throw new InvalidAttributeValueException(OBJECTIVE_NOTADDED_ERROR);
        }
        else throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
      }
      else throw new InvalidAttributeValueException(NULL_OBJECTIVE);
    }
    else throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
  }

  public static boolean updateProgressObjective(long employeeID, int objectiveID, int progress)
      throws InvalidAttributeValueException
  {
    if (employeeID < 1 || objectiveID < 1)
    {
      throw new InvalidAttributeValueException(INVALID_OBJECTIVE_OR_EMPLOYEEID);
    }
    else if (progress < -1 || progress > 2)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
    }

    boolean updated = false;
    final Query<Employee> querySearch = getEmployeeQuery(employeeID);
    final Employee employee = querySearch.get();
    final Objective objective = employee.getLatestVersionOfSpecificObjective(objectiveID);

    if (objective.getProgress() == progress)
    {
      updated = true;
    }
    else
    {
      objective.setProgress(progress);

      if (employee.editObjective(objective))
      {
        UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("objectives",
            employee.getObjectiveList());
        dbConnection.update(querySearch, ops);
        updated = true;
      }
    }

    return updated;
  }

  public static boolean addNewVersionObjective(long employeeID, int objectiveID, Object data)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and ObjectiveID
    if (employeeID > 0 && objectiveID > 0)
    {
      if (data != null && data instanceof Objective)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          // Extract its List of Objectives
          List<List<Objective>> dataFromDB = e.getObjectiveList();
          // Search for the objective Id within the list of objectives
          int indexObjectiveList = -1;
          for (int i = 0; i < dataFromDB.size(); i++)
          {
            // Save the index of the list when the objectiveID is found
            if (dataFromDB.get(i).get(0).getID() == objectiveID)
            {
              indexObjectiveList = i;
              // Exit the for loop once the value has been found
              break;
            }
          }
          // verify that the index variable has changed its value
          if (indexObjectiveList != -1)
          {
            // Add the updated version of the objective
            if (e.editObjective((Objective) data))
            {
              // Update the List<List<objective>> in the DB passing the new list
              UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("objectives",
                  e.getObjectiveList());
              // Commit the changes to the DB
              dbConnection.update(querySearch, ops);
              return true;
            }
          }
          // if the index hasn't changed its value it means that there is no objective with such ID, therefore throw and
          // exception
          else throw new InvalidAttributeValueException(INVALID_OBJECTIVEID);
        }
        else throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
      }
      else throw new InvalidAttributeValueException(INVALID_OBJECTIVE);
    }
    else throw new InvalidAttributeValueException(INVALID_OBJECTIVE_OR_EMPLOYEEID);

    return false;
  }

  public static boolean insertNewNote(long employeeID, Object data)
      throws InvalidAttributeValueException, MongoException
  {
    // Check the employeeID
    if (employeeID > 0)
    {
      if (data != null && data instanceof Note)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          // Add the new note to the list
          if (e.addNote((Note) data))
          {
            // Update the List<List<Note>> in the DB passing the new list
            UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("notes",
                e.getNoteList());
            // Commit the changes to the DB
            dbConnection.update(querySearch, ops);
            return true;
          }
          else throw new InvalidAttributeValueException(NOTE_NOTADDED_ERROR);
        }
        else throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
      }
      else throw new InvalidAttributeValueException(INVALID_NOTE);
    }
    else throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
  }

  public static boolean addNewVersionNote(long employeeID, int noteID, Object data)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and noteID
    if (employeeID > 0 && noteID > 0)
    {
      if (data != null && data instanceof Note)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          // Extract its List of notes
          List<List<Note>> dataFromDB = e.getNoteList();
          // Search for the objective Id within the list of notes
          int indexNoteList = -1;
          for (int i = 0; i < dataFromDB.size(); i++)
          {
            // Save the index of the list when the noteID is found
            if (dataFromDB.get(i).get(0).getID() == noteID)
            {
              indexNoteList = i;
              // Exit the for loop once the value has been found
              break;
            }
          }
          // verify that the index variable has changed its value
          if (indexNoteList != -1)
          {
            // Add the updated version of the note
            if (e.editNote((Note) data))
            {
              // Update the List<List<Note>> in the DB passing the new list
              UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("notes",
                  e.getNoteList());
              // Commit the changes to the DB
              dbConnection.update(querySearch, ops);
              return true;
            }
          }
          // if the index hasn't changed its value it means that there is no note with such ID, therefore throw and
          // exception
          else throw new InvalidAttributeValueException(INVALID_NOTEID);
        }
        else throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
      }
      else throw new InvalidAttributeValueException(INVALID_NOTE);
    }
    else throw new InvalidAttributeValueException(INVALID_NOTE_OR_EMPLOYEEID);
    return false;
  }

  public static boolean insertNewDevelopmentNeed(long employeeID, Object data)
      throws InvalidAttributeValueException, MongoException
  {
    // Check the employeeID
    if (employeeID > 0)
    {
      if (data != null && data instanceof DevelopmentNeed)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);

        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          // Add the new development need to the list
          if (e.addDevelopmentNeed((DevelopmentNeed) data))
          {
            // Update the List<List<developmentNeed>> in the DB passing the new list
            UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("developmentNeeds",
                e.getDevelopmentNeedsList());
            // Commit the changes to the DB
            dbConnection.update(querySearch, ops);
            return true;
          }
          else throw new InvalidAttributeValueException(DEVELOPMENTNEED_NOTADDED_ERROR);
        }
        else throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
      }
      else throw new InvalidAttributeValueException(INVALID_DEVNEED_CONTEXT);
    }
    else throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
  }

  public static boolean updateProgressDevelopmentNeed(long employeeID, int devNeedID, int progress)
      throws InvalidAttributeValueException
  {
    if (employeeID < 1 || devNeedID < 1)
    {
      throw new InvalidAttributeValueException(INVALID_DEVNEED_OR_EMPLOYEEID);
    }
    else if (progress < -1 || progress > 2)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
    }

    boolean updated = false;
    final Query<Employee> querySearch = getEmployeeQuery(employeeID);
    final Employee employee = querySearch.get();
    final DevelopmentNeed devNeed = employee.getLatestVersionOfSpecificDevelopmentNeed(devNeedID);

    if (devNeed.getProgress() == progress)
    {
      updated = true;
    }
    else
    {
      devNeed.setProgress(progress);

      if (employee.editDevelopmentNeed(devNeed))
      {
        UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("developmentNeeds",
            employee.getDevelopmentNeedsList());
        dbConnection.update(querySearch, ops);
        updated = true;
      }
    }

    return updated;
  }

  public static boolean addNewVersionDevelopmentNeed(long employeeID, int devNeedID, Object data)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and noteID
    if (employeeID > 0 && devNeedID > 0)
    {
      if (data != null && data instanceof DevelopmentNeed)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          // Extract its List of notes
          List<List<DevelopmentNeed>> dataFromDB = e.getDevelopmentNeedsList();
          // Search for the objective Id within the list of development needs
          int indexDevNeedList = -1;
          for (int i = 0; i < dataFromDB.size(); i++)
          {
            // Save the index of the list when the devNeedID is found
            if (dataFromDB.get(i).get(0).getID() == devNeedID)
            {
              indexDevNeedList = i;
              // Exit the for loop once the value has been found
              break;
            }
          }
          // verify that the index variable has changed its value
          if (indexDevNeedList != -1)
          {
            // Add the updated version of the DevelopmentNeed
            if (e.editDevelopmentNeed((DevelopmentNeed) data))
            {
              // Update the List<List<DevelopmentNeed>> in the DB passing the new list
              UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class)
                  .set("developmentNeeds", e.getDevelopmentNeedsList());
              // Commit the changes to the DB
              dbConnection.update(querySearch, ops);
              return true;
            }
          }
          // if the index hasn't changed its value it means that there is no development need with such ID, therefore
          // throw and exception
          else throw new InvalidAttributeValueException(INVALID_DEVNEEDID_CONTEXT);
        }
        else throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
      }
      else throw new InvalidAttributeValueException(INVALID_DEVNEED_CONTEXT);
    }
    else throw new InvalidAttributeValueException(INVALID_DEVNEED_OR_EMPLOYEEID);
    return false;
  }

  /**
   * Sends Emails to the recipients and updates the database.
   * 
   * @param employeeID
   * @param emailsString
   * @param notes
   * @throws Exception
   */
  public static void processFeedbackRequest(long employeeID, String emailsString, String notes) throws Exception
  {
    Employee requester = EmployeeDAO.getEmployee(employeeID);
    Set<String> recipientList = Utils.stringEmailsToHashSet(emailsString);
    List<String> errorRecipientList = new ArrayList<String>();

    for (String recipient : recipientList)
    {
      String tempID = Utils.generateFeedbackRequestID(employeeID);
      String subject = String.format("Feedback Request from %s - %s", requester.getFullName(), employeeID);
      String body = String.format("%s \n\n Feedback_Request: %s", notes, tempID);
      // TODO Replace above with template.
      try
      {
        EmailService.sendEmail(recipient, subject, body);
      }
      catch (Exception e)
      {
        logger.error(e.getMessage());
        errorRecipientList.add(recipient);
        continue;
      }
      EmployeeDAO.addFeedbackRequest(requester, new FeedbackRequest(tempID, recipient));
    }

    if (!errorRecipientList.isEmpty())
    {
      throw new Exception(
          "There were issues sending requests to the following addresses: \n" + errorRecipientList.toString());
    }
  }

  /**
   * Add a feedbackRequest to an employee.
   *
   * @param employee
   * @param feedbackRequest
   * @throws InvalidAttributeValueException
   */
  public static void addFeedbackRequest(Employee employee, FeedbackRequest feedbackRequest)
      throws InvalidAttributeValueException
  {
    Validate.isNull(employee, feedbackRequest);

    employee.addFeedbackRequest(feedbackRequest);
    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests",
        employee.getFeedbackRequestsList());
    dbConnection.update(employee, ops);
  }

  /**
   * Add a feedback to an employee
   *
   * @param providerEmail
   * @param recipientEmail
   * @param feedbackDescription
   * @throws InvalidAttributeValueException
   * @throws NamingException
   */
  public static void addFeedback(String providerEmail, String recipientEmail, String feedbackDescription)
      throws InvalidAttributeValueException, NamingException

  {
    Validate.areStringsEmptyorNull(providerEmail, recipientEmail, feedbackDescription);

    long employeeID = ADProfileDAO.authenticateUserProfile(recipientEmail).getEmployeeID();
    Employee employee = getEmployee(employeeID);

    Feedback feedback = new Feedback(employee.nextFeedbackID(), providerEmail, feedbackDescription);

    try
    {
      feedback.setProviderName(ADProfileDAO.findEmployeeFullNameFromEmailAddress(providerEmail));
    }
    catch (InvalidAttributeValueException | NamingException e)
    {
      // TODO Handle this better?
    }

    employee.addFeedback(feedback);

    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedback",
        employee.getFeedback());
    dbConnection.update(employee, ops);
  }

  /**
   * Method that updates requested feedback to acknowledge feedback has been received then adds the feedback to the
   * employee
   *
   * @param providerEmail
   * @param feedbackRequestID
   * @param feedbackDescription
   * @throws InvalidAttributeValueException
   * @throws NamingException
   */
  public static void addRequestedFeedback(String providerEmail, String feedbackRequestID, String feedbackDescription)
      throws InvalidAttributeValueException, NamingException
  {
    Validate.areStringsEmptyorNull(providerEmail, feedbackRequestID, feedbackDescription);

    long employeeID = Utils.getEmployeeIDFromRequestID(feedbackRequestID);
    Employee employee = getEmployee(employeeID);

    employee.getFeedbackRequest(feedbackRequestID).setReplyReceived(true);

    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests",
        employee.getFeedbackRequestsList());
    dbConnection.update(employee, ops);

    addFeedback(providerEmail, employee.getEmailAddress(), feedbackDescription);
  }

  /**
   * 
   * @param employeeID the employee ID
   * @param data the Competency to update
   * @param title the title of the competency (max 200 characters)
   * @return true or false to establish whether the task has been completed successfully or not This method inserts a
   *         new version of competencies list
   * @throws InvalidAttributeValueException
   */
  public static boolean addNewVersionCompetency(long employeeID, Object data, String title)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and noteID
    if (employeeID > 0 && title != null && title.length() > 0)
    {
      if (data != null && data instanceof Competency && title != null && title.length() > 0)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          // Add the updated version of the note
          if (e.updateCompetency((Competency) data, title))
          {
            // Update the List<List<Competencies>> in the DB passing the new list
            UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("competencies",
                e.getCompetenciesList());
            // Commit the changes to the DB
            dbConnection.update(querySearch, ops);
            return true;
          }
        }
        else throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
      }
      else throw new InvalidAttributeValueException(INVALID_COMPETENCY_CONTEXT);
    }
    else throw new InvalidAttributeValueException(INVALID_COMPETENCY_OR_EMPLOYEEID);
    return false;
  }

  public static ADProfile_Basic matchADWithMongoData(ADProfile_Advanced userData) throws InvalidAttributeValueException
  {
    if (userData != null)
    {
      // Establish a connection with the DB
      // Search for a user GUID
      // Retrieve Employee with the given GUID
      if (!userData.getGUID().equals(""))
      {
        Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("GUID =", userData.getGUID());

        // If a user exists in our system, verify that his/hers data is up-to-date
        if (querySearch.get() != null)
        {
          Employee e = querySearch.get();
          boolean resUpdate = e.verifyDataIsUpToDate(userData);
          // If the method returns true, the data has been updated
          if (resUpdate)
          {
            // Reflect the changes to our system, updating the user data in the MongoDB
            // Remove incorrect document
            dbConnection.findAndDelete(querySearch);
            // Commit the changes to the DB
            dbConnection.save(e);
          }
        }
        // Else, Create the user in our system with the given data
        else
        {
          // Create the Employee Object
          Employee employeeNewData = new Employee(userData);
          // Save the new user to the DB
          dbConnection.save(employeeNewData);
        }
        // Return a smaller version of the current object to the user
        
        return new ADProfile_Basic(userData.getEmployeeID(), userData.getSurname(), userData.getForename(),
            userData.getIsManager(), userData.getUsername(), userData.getEmailAddress(), userData.getHasHRDash());
      }
      else throw new InvalidAttributeValueException(INVALID_USERGUID_NOTFOUND);
    }
    else throw new InvalidAttributeValueException(NULL_USER_DATA);
  }

}
