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
import static dataStructure.Constants.INVALID_OBJECTIVE;
import static dataStructure.Constants.INVALID_OBJECTIVEID;
import static dataStructure.Constants.INVALID_OBJECTIVE_OR_EMPLOYEEID;
import static dataStructure.Constants.NULL_OBJECTIVE;
import static dataStructure.Constants.NULL_USER_DATA;
import static dataStructure.Constants.OBJECTIVE_NOTADDED_ERROR;

import java.time.LocalDateTime;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.mongodb.MongoException;

import dataStructure.Competency;
import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import services.ad.ADConnectionException;
import services.ews.EmailService;
import services.validate.Validate;
import utils.Template;
import utils.Utils;

/**
 * This class contains the definition of the EmployeeDAO object
 *
 */
@Component
@PropertySource("${ENVIRONMENT}.properties")
public class EmployeeService
{

  /** TYPE Property|Constant - Represents|Indicates... */
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

  private static final String ERROR_USER_NOT_FOUND = "The given user ID does not exist.";

  /** String Constant - Represents Feedback Request */
  public static final String FEEDBACK_REQUEST = "Feedback Request";

  // There is only 1 instance of the Datastore in the whole system
  private static Datastore dbConnection;

  /** Environment Property - Reference to environment to get property details. */
  private static Environment env;

  /* Accesses the Active Directories */
  private static EmployeeProfileService employeeProfileService;

  /**
   * EmployeeDAO Constructor - Needed for morphia?.
   *
   */
  public EmployeeService()
  {
  }

  /**
   * EmployeeDAO Constructor - Responsible for initialising dbConnection.
   *
   * @param dbConnection
   */
  public EmployeeService(Datastore dbConnection, EmployeeProfileService employeeProfileService, Environment env)
  {
    EmployeeService.dbConnection = dbConnection;
    EmployeeService.env = env;
    EmployeeService.employeeProfileService = employeeProfileService;
  }

  /**
   * Gets Employee from database with the specified id
   *
   * @param employeeID ID of the employee
   * @return the employee if exists
   * @throws InvalidAttributeValueException if employee is not found or is null.
   */
  public Employee getEmployee(long employeeID) throws InvalidAttributeValueException
  {
    Employee employee = getEmployeeQuery(employeeID).get();
    if (employee == null)
    {
      throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
    }
    return employee;
  }

  /**
   * Get Employee query with the specified id
   *
   * @param employeeID
   * @return
   */
  private Query<Employee> getEmployeeQuery(long employeeID)
  {
    return dbConnection.createQuery(Employee.class).filter("profile.employeeID =", employeeID);
  }

  /**
   * Gets full name os a user from the database
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public String getFullNameUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getProfile().getFullName();
  }

  /**
   * Get a list of the current objectives for a user.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Objective> getObjectivesForUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionObjectives();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param objectiveID
   * @return
   * @throws InvalidAttributeValueException
   */
  public Objective getSpecificObjectiveForUser(long employeeID, int objectiveID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionOfSpecificObjective(objectiveID);
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Feedback> getFeedbackForUser(long employeeID) throws InvalidAttributeValueException
  {
    List<Feedback> feedbackList = getEmployee(employeeID).getFeedback();
    Collections.reverse(feedbackList);
    return feedbackList;
  }

  /**
   * Get all notes for user.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Note> getNotes(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getNotes();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<DevelopmentNeed> getDevelopmentNeedsForUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionDevelopmentNeeds();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public String getAllUserDataFromID(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).toString();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Competency> getCompetenciesForUser(long employeeID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionCompetencies();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   * @throws NamingException
   */
  public List<EmployeeProfile> getReporteesForUser(long employeeID)
      throws InvalidAttributeValueException, NamingException
  {
    Employee employee = getEmployee(employeeID);

    List<EmployeeProfile> reporteeList = new ArrayList<>();

    for (String str : employee.getProfile().getReporteeCNs())
    {
      long temp = Long.parseLong(str.substring(str.indexOf('-') + 1).trim());
      try
      {
        reporteeList.add(matchADWithMongoData(employeeProfileService.verifyIfUserExists(temp)));
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
  public boolean insertNewObjective(long employeeID, Object data) throws InvalidAttributeValueException, MongoException
  {
    // Check the employeeID
    if (employeeID > 0)
    {
      if (data != null && data instanceof Objective)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeID);
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

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param objectiveID
   * @param progress
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean updateProgressObjective(long employeeID, int objectiveID, int progress)
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

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param objectiveID
   * @param data
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNewVersionObjective(long employeeID, int objectiveID, Object data)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and ObjectiveID
    if (employeeID > 0 && objectiveID > 0)
    {
      if (data != null && data instanceof Objective)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeID);
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

  /**
   * Adds new note to an employee
   *
   * @param employeeID
   * @param note
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNote(long employeeID, Note note) throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeID);
    Employee employee = employeeQuery.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.addNote(note);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class).set("notes",
        employee.getNotes());
    dbConnection.update(employeeQuery, updateOperation);
    return true;
  }

  /**
   * Add new note to reportee
   *
   * @param reporteeEmployeeID
   * @param note
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNoteToReportee(long reporteeEmployeeID, Note note) throws InvalidAttributeValueException
  {
    addNote(reporteeEmployeeID, note);

    String reporteeEmail = getEmployee(reporteeEmployeeID).getProfile().getEmailAddress();
    String subject = String.format("Note added from %s", note.getProviderName());
    try
    {
      String body = Template.populateTemplate(env.getProperty("templates.note.added"), note.getProviderName());
      EmailService.sendEmail(reporteeEmail, subject, body);
    }
    catch (Exception e)
    {
      LOGGER.error("Email could not be sent for a proposed objective. Error: {}", e);
    }

    return true;
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param data
   * @return
   * @throws InvalidAttributeValueException
   * @throws MongoException
   */
  public boolean insertNewDevelopmentNeed(long employeeID, Object data)
      throws InvalidAttributeValueException, MongoException
  {
    // Check the employeeID
    if (employeeID > 0)
    {
      if (data != null && data instanceof DevelopmentNeed)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeID);

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

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param devNeedID
   * @param progress
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean updateProgressDevelopmentNeed(long employeeID, int devNeedID, int progress)
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

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param devNeedID
   * @param data
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNewVersionDevelopmentNeed(long employeeID, int devNeedID, Object data)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and noteID
    if (employeeID > 0 && devNeedID > 0)
    {
      if (data != null && data instanceof DevelopmentNeed)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeID);
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
  public void processFeedbackRequest(long employeeID, String emailsString, String notes) throws Exception
  {
    Employee requester = getEmployee(employeeID);
    Set<String> recipientList = Utils.stringEmailsToHashSet(emailsString);
    List<String> errorRecipientList = new ArrayList<String>();
    String requesterName = requester.getProfile().getFullName();

    for (String recipient : recipientList)
    {
      String tempID = Utils.generateFeedbackRequestID(employeeID);
      String subject = String.format("Feedback Request from %s - %s", requester.getProfile().getFullName(), employeeID);
      // String body = String.format("%s \n\n Feedback_Request: %s", notes, tempID);
      String body = Template.populateTemplate(env.getProperty("templates.feedback.request"), requesterName, notes,
          tempID);
      try
      {
        EmailService.sendEmail(recipient, subject, body);
      }
      catch (Exception e)
      {
        LOGGER.error(e.getMessage());
        errorRecipientList.add(recipient);
        continue;
      }
      addFeedbackRequest(requester, new FeedbackRequest(tempID, recipient));
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
  public void addFeedbackRequest(Employee employee, FeedbackRequest feedbackRequest)
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
   * @throws Exception
   */
  public void addFeedback(String providerEmail, String recipientEmail, String feedbackDescription,
      boolean isFeedbackRequest) throws Exception

  {
    Validate.areStringsEmptyorNull(providerEmail, recipientEmail, feedbackDescription);

    long employeeID = matchADWithMongoData(employeeProfileService.authenticateUserProfile(recipientEmail))
        .getEmployeeID();
    Employee employee = getEmployee(employeeID);

    Feedback feedback = new Feedback(employee.nextFeedbackID(), providerEmail, feedbackDescription);

    try
    {
      feedback.setProviderName(employeeProfileService.findEmployeeFullNameFromEmailAddress(providerEmail));
    }
    catch (InvalidAttributeValueException | NamingException e)
    {
      // TODO Handle this better?
    }

    employee.addFeedback(feedback);

    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedback",
        employee.getFeedback());
    dbConnection.update(employee, ops);

    if (isFeedbackRequest)
    {

      String provider = (!feedback.getProviderName().isEmpty()) ? feedback.getProviderName() : providerEmail;
      String subject = String.format("Feedback Request reply from %s", provider);
      String body = Template.populateTemplate(env.getProperty("templates.feedback.reply"), provider);

      EmailService.sendEmail(recipientEmail, subject, body);
    }
  }

  /**
   * Method that updates requested feedback to acknowledge feedback has been received then adds the feedback to the
   * employee
   *
   * @param providerEmail
   * @param feedbackRequestID
   * @param feedbackDescription
   * @throws Exception
   */
  public void addRequestedFeedback(String providerEmail, String feedbackRequestID, String feedbackDescription)
      throws Exception
  {
    Validate.areStringsEmptyorNull(providerEmail, feedbackRequestID, feedbackDescription);

    long employeeID = Utils.getEmployeeIDFromRequestID(feedbackRequestID);
    Employee employee = getEmployee(employeeID);

    employee.getFeedbackRequest(feedbackRequestID).setReplyReceived(true);

    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests",
        employee.getFeedbackRequestsList());
    dbConnection.update(employee, ops);

    addFeedback(providerEmail, employee.getProfile().getEmailAddress(), feedbackDescription, true);
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
  public boolean addNewVersionCompetency(long employeeID, Object data, String title)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and noteID
    if (employeeID > 0 && title != null && title.length() > 0)
    {
      if (data != null && data instanceof Competency && title != null && title.length() > 0)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeID);
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

  /**
   * TODO: Describe this method.
   *
   * @param profileFromAD
   * @return
   * @throws InvalidAttributeValueException
   */
  public EmployeeProfile matchADWithMongoData(EmployeeProfile profileFromAD) throws InvalidAttributeValueException
  {
    if (profileFromAD == null)
    {
      LOGGER.warn(NULL_USER_DATA);
      throw new InvalidAttributeValueException(NULL_USER_DATA);
    }

    if (profileFromAD.getEmployeeID() < 0)
    {
      LOGGER.warn(INVALID_DEVNEED_OR_EMPLOYEEID);
      throw new InvalidAttributeValueException(INVALID_DEVNEED_OR_EMPLOYEEID);
    }

    Employee e = getEmployeeQuery(profileFromAD.getEmployeeID()).get();

    if (e != null)
    {
      final boolean needsUpdate = !e.getProfile().equals(profileFromAD);
      LOGGER.debug("Employee (" + e.getProfile().getEmployeeID() + ") needs update: " + needsUpdate);
      if (needsUpdate)
      {
        e.setProfile(profileFromAD);
        LOGGER.debug("Updating employee: " + e.getProfile().getEmployeeID());
        dbConnection.save(e);
      }
    }
    else
    {
      e = new Employee(profileFromAD);
      LOGGER.debug("Inserting employee: " + e.getProfile().getEmployeeID());
      dbConnection.save(e);
    }

    return e.getProfile();
  }

  public EmployeeProfile authenticateUserProfile(String usernameEmail)
      throws InvalidAttributeValueException, ADConnectionException, NamingException
  {
    return employeeProfileService.authenticateUserProfile(usernameEmail);
  }

  public void updateLastLoginDate(EmployeeProfile profile) throws InvalidAttributeValueException
  {
    Employee employee = getEmployee(profile.getEmployeeID());
    employee.setLastLogon(Utils.localDateTimetoDate(LocalDateTime.now()));

    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("lastLogon",
        employee.getLastLogon());
    dbConnection.update(employee, ops);
  }
}
