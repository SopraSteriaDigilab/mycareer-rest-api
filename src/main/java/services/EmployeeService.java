package services;

import static dataStructure.Constants.DEVELOPMENTNEED_NOTADDED_ERROR;
import static dataStructure.Constants.INVALID_COMPETENCY_CONTEXT;
import static dataStructure.Constants.INVALID_COMPETENCY_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_CONTEXT_PROGRESS;
import static dataStructure.Constants.INVALID_CONTEXT_USERID;
import static dataStructure.Constants.INVALID_DEVNEEDID_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_ID_NOT_FOUND;
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
import dataStructure.DevelopmentNeed_NEW;
import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Objective_NEW;
import dataStructure.Objective_NEW.Progress;
import services.ad.ADConnectionException;
import services.ews.EmailService;
import utils.Template;
import utils.Utils;
import utils.Validate;

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
  private static final String INVALID_DEVELOPMENT_NEED_ID = "The given development Need ID does not exist.";

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
   * @param employeeId ID of the employee
   * @return the employee if exists
   * @throws InvalidAttributeValueException if employee is not found or is null.
   */
  public Employee getEmployee(long employeeId) throws InvalidAttributeValueException
  {
    Employee employee = getEmployeeQuery(employeeId).get();
    if (employee == null)
    {
      throw new InvalidAttributeValueException(INVALID_ID_NOT_FOUND);
    }
    return employee;
  }

  /**
   * Gets full name os a user from the database
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   */
  public String getFullNameUser(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getProfile().getFullName();
  }

  /**
   * Get a list of the current objectives for a user.
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Objective> getObjectivesForUser(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getLatestVersionObjectives();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @param objectiveID
   * @return
   * @throws InvalidAttributeValueException
   */
  public Objective getSpecificObjectiveForUser(long employeeId, int objectiveID) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getLatestVersionOfSpecificObjective(objectiveID);
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Feedback> getFeedbackForUser(long employeeId) throws InvalidAttributeValueException
  {
    List<Feedback> feedbackList = getEmployee(employeeId).getFeedback();
    Collections.reverse(feedbackList);
    return feedbackList;
  }

  /**
   * Get all notes for user.
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Note> getNotes(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getNotes();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<DevelopmentNeed> getDevelopmentNeedsForUser(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getLatestVersionDevelopmentNeeds();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   */
  public String getAllUserDataFromID(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).toString();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<Competency> getCompetenciesForUser(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getLatestVersionCompetencies();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   * @throws NamingException
   */
  public List<EmployeeProfile> getReporteesForUser(long employeeId)
      throws InvalidAttributeValueException, NamingException
  {
    Employee employee = getEmployee(employeeId);

    List<EmployeeProfile> reporteeList = new ArrayList<>();

    for (String str : employee.getProfile().getReporteeCNs())
    {
      long temp = Long.parseLong(str.substring(str.indexOf('-') + 1).trim());
      try
      {
        reporteeList.add(matchADWithMongoData(employeeProfileService.fetchEmployeeProfile(temp)));
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
   * @param employeeId
   * @return This method inserts a new objective for a specific employee given their ID
   * @throws InvalidAttributeValueException
   */
  public boolean insertNewObjective(long employeeId, Object data) throws InvalidAttributeValueException, MongoException
  {
    // Check the employeeId
    if (employeeId > 0)
    {
      if (data != null && data instanceof Objective)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeId);
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
        else throw new InvalidAttributeValueException(INVALID_ID_NOT_FOUND);
      }
      else throw new InvalidAttributeValueException(NULL_OBJECTIVE);
    }
    else throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @param objectiveID
   * @param progress
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean updateProgressObjective(long employeeId, int objectiveID, int progress)
      throws InvalidAttributeValueException
  {
    if (employeeId < 1 || objectiveID < 1)
    {
      throw new InvalidAttributeValueException(INVALID_OBJECTIVE_OR_EMPLOYEEID);
    }
    else if (progress < -1 || progress > 2)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
    }

    boolean updated = false;
    final Query<Employee> querySearch = getEmployeeQuery(employeeId);
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
   * @param employeeId
   * @param objectiveID
   * @param data
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNewVersionObjective(long employeeId, int objectiveID, Object data)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and ObjectiveID
    if (employeeId > 0 && objectiveID > 0)
    {
      if (data != null && data instanceof Objective)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeId);
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
        else throw new InvalidAttributeValueException(INVALID_ID_NOT_FOUND);
      }
      else throw new InvalidAttributeValueException(INVALID_OBJECTIVE);
    }
    else throw new InvalidAttributeValueException(INVALID_OBJECTIVE_OR_EMPLOYEEID);

    return false;
  }

  /**
   * Adds new note to an employee
   *
   * @param employeeId
   * @param note
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNote(long employeeId, Note note) throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeId);
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
   * @param employeeId
   * @param data
   * @return
   * @throws InvalidAttributeValueException
   * @throws MongoException
   */
  public boolean insertNewDevelopmentNeed(long employeeId, Object data)
      throws InvalidAttributeValueException, MongoException
  {
    // Check the employeeId
    if (employeeId > 0)
    {
      if (data != null && data instanceof DevelopmentNeed)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeId);

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
        else throw new InvalidAttributeValueException(INVALID_ID_NOT_FOUND);
      }
      else throw new InvalidAttributeValueException(INVALID_DEVNEED_CONTEXT);
    }
    else throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @param devNeedID
   * @param progress
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean updateProgressDevelopmentNeed(long employeeId, int devNeedID, int progress)
      throws InvalidAttributeValueException
  {
    if (employeeId < 1 || devNeedID < 1)
    {
      throw new InvalidAttributeValueException(INVALID_DEVNEED_OR_EMPLOYEEID);
    }
    else if (progress < -1 || progress > 2)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
    }

    boolean updated = false;
    final Query<Employee> querySearch = getEmployeeQuery(employeeId);
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
   * @param employeeId
   * @param devNeedID
   * @param data
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNewVersionDevelopmentNeed(long employeeId, int devNeedID, Object data)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and noteID
    if (employeeId > 0 && devNeedID > 0)
    {
      if (data != null && data instanceof DevelopmentNeed)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeId);
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
        else throw new InvalidAttributeValueException(INVALID_ID_NOT_FOUND);
      }
      else throw new InvalidAttributeValueException(INVALID_DEVNEED_CONTEXT);
    }
    else throw new InvalidAttributeValueException(INVALID_DEVNEED_OR_EMPLOYEEID);
    return false;
  }

  public void toggleDevNeedArchive(long employeeId, int developmentNeedID) throws InvalidAttributeValueException
  {
    Query<Employee> querySearch = getEmployeeQuery(employeeId);
    Employee employee = querySearch.get();
    DevelopmentNeed curDevNeed = employee.getLatestVersionOfSpecificDevelopmentNeed(developmentNeedID);

    if (curDevNeed == null) throw new InvalidAttributeValueException(INVALID_DEVELOPMENT_NEED_ID);

    DevelopmentNeed developmentNeed = new DevelopmentNeed(curDevNeed);
    developmentNeed.setIsArchived(!developmentNeed.getIsArchived());

    if (employee.editDevelopmentNeed(developmentNeed))
    {
      UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("developmentNeeds",
          employee.getDevelopmentNeedsList());
      dbConnection.update(querySearch, ops);
    }
  }

  /**
   * Sends Emails to the recipients and updates the database.
   * 
   * @param employeeId
   * @param emailsString
   * @param notes
   * @throws Exception
   */
  public void processFeedbackRequest(long employeeId, String emailsString, String notes) throws Exception
  {
    Employee requester = getEmployee(employeeId);
    Set<String> recipientList = Utils.stringEmailsToHashSet(emailsString);

    if (recipientList.size() > 20)
      throw new InvalidAttributeValueException("There must be less than 20 email addresses.");

    List<String> errorRecipientList = new ArrayList<String>();
    String requesterName = requester.getProfile().getFullName();

    for (String recipient : recipientList)
    {
      String tempID = Utils.generateFeedbackRequestID(employeeId);
      String subject = String.format("Feedback Request from %s - %s", requester.getProfile().getFullName(), employeeId);
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

    long employeeId = matchADWithMongoData(employeeProfileService.fetchEmployeeProfileFromEmailAddress(recipientEmail))
        .getEmployeeID();
    Employee employee = getEmployee(employeeId);

    Feedback feedback = new Feedback(employee.nextFeedbackID(), providerEmail, feedbackDescription);

    try
    {
      feedback.setProviderName(employeeProfileService.fetchEmployeeFullNameFromEmailAddress(providerEmail));
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

    long employeeId = Utils.getEmployeeIDFromRequestID(feedbackRequestID);
    Employee employee = getEmployee(employeeId);

    employee.getFeedbackRequest(feedbackRequestID).setReplyReceived(true);

    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests",
        employee.getFeedbackRequestsList());
    dbConnection.update(employee, ops);

    addFeedback(providerEmail, employee.getProfile().getEmailAddress(), feedbackDescription, true);
  }

  /**
   * 
   * @param employeeId the employee ID
   * @param data the Competency to update
   * @param title the title of the competency (max 200 characters)
   * @return true or false to establish whether the task has been completed successfully or not This method inserts a
   *         new version of competencies list
   * @throws InvalidAttributeValueException
   */
  public boolean addNewVersionCompetency(long employeeId, Object data, String title)
      throws InvalidAttributeValueException
  {
    // Check EmployeeID and noteID
    if (employeeId > 0 && title != null && title.length() > 0)
    {
      if (data != null && data instanceof Competency && title != null && title.length() > 0)
      {
        // Retrieve Employee with the given ID
        Query<Employee> querySearch = getEmployeeQuery(employeeId);
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
        else throw new InvalidAttributeValueException(INVALID_ID_NOT_FOUND);
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
    return employeeProfileService.fetchEmployeeProfile(usernameEmail);
  }

  public void updateLastLoginDate(EmployeeProfile profile) throws InvalidAttributeValueException
  {
    Employee employee = getEmployee(profile.getEmployeeID());
    employee.setLastLogon(Utils.localDateTimetoDate(LocalDateTime.now()));

    UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("lastLogon",
        employee.getLastLogon());
    dbConnection.update(employee, ops);
  }

  /**
   * Get Employee query with the specified id
   *
   * @param employeeId
   * @return
   */
  private Query<Employee> getEmployeeQuery(long employeeID)
  {
    return dbConnection.createQuery(Employee.class).filter("profile.employeeID =", employeeID);
  }

  //////////////////// START NEW OBJECTIVES

  public List<Objective_NEW> getObjectivesNEW(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getObjectivesNEW();
  }

  public void addObjectiveNEW(long employeeId, Objective_NEW objective) throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeId);
    Employee employee = employeeQuery.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.addObjectiveNEW(objective);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newObjectives", employee.getObjectivesNEW());
    dbConnection.update(employeeQuery, updateOperation);
  }

  public void editObjectiveNEW(long employeeId, Objective_NEW objective) throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeId);
    Employee employee = employeeQuery.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.editObjectiveNEW(objective);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newObjectives", employee.getObjectivesNEW());
    dbConnection.update(employeeQuery, updateOperation);
  }

  public void deleteObjectiveNEW(long employeeId, int objectiveId) throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeId);
    Employee employee = employeeQuery.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.deleteObjectiveNEW(objectiveId);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newObjectives", employee.getObjectivesNEW());
    dbConnection.update(employeeQuery, updateOperation);
  }

  public void updateObjectiveNEWProgress(long employeeId, int objectiveId, Progress progress)
      throws InvalidAttributeValueException
  {
    Query<Employee> querySearch = getEmployeeQuery(employeeId);
    Employee employee = querySearch.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.updateObjectiveNEWProgress(objectiveId, progress);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newObjectives", employee.getObjectivesNEW());
    dbConnection.update(querySearch, updateOperation);
  }

  public void toggleObjectiveNEWArchive(long employeeId, int objectiveId) throws InvalidAttributeValueException
  {
    Query<Employee> querySearch = getEmployeeQuery(employeeId);
    Employee employee = querySearch.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.toggleObjectiveNEWArchive(objectiveId);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newObjectives", employee.getObjectivesNEW());
    dbConnection.update(querySearch, updateOperation);
  }

  //////////////////// END NEW OBJECTIVES

  //////////////////// START NEW DEVELOPMENT NEEDS

  public List<DevelopmentNeed_NEW> getDevelopmentNeedsNEW(long employeeId) throws InvalidAttributeValueException
  {
    return getEmployee(employeeId).getDevelopmentNeedsNEW();
  }

  public void addDevelopmentNeedNEW(long employeeId, DevelopmentNeed_NEW developmentNeed)
      throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeId);
    Employee employee = employeeQuery.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.addDevelopmentNeedNEW(developmentNeed);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newDevelopmentNeeds", employee.getDevelopmentNeedsNEW());
    dbConnection.update(employeeQuery, updateOperation);
  }

  public void editDevelopmentNeedNEW(long employeeId, DevelopmentNeed_NEW developmentNeed)
      throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeId);
    Employee employee = employeeQuery.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.editDevelopmentNeedNEW(developmentNeed);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newDevelopmentNeeds", employee.getDevelopmentNeedsNEW());
    dbConnection.update(employeeQuery, updateOperation);
  }

  public void deleteDevelopmentNeedNEW(long employeeId, int developmentNeedId) throws InvalidAttributeValueException
  {
    Query<Employee> employeeQuery = getEmployeeQuery(employeeId);
    Employee employee = employeeQuery.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.deleteDevelopmentNeedNEW(developmentNeedId);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newDevelopmentNeeds", employee.getDevelopmentNeedsNEW());
    dbConnection.update(employeeQuery, updateOperation);
  }

  public void updateDevelopmentNeedNEWProgress(long employeeId, int developmentNeedId, Progress progress)
      throws InvalidAttributeValueException
  {
    Query<Employee> querySearch = getEmployeeQuery(employeeId);
    Employee employee = querySearch.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.updateDevelopmentNeedNEWProgress(developmentNeedId, progress);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newDevelopmentNeeds", employee.getDevelopmentNeedsNEW());
    dbConnection.update(querySearch, updateOperation);
  }

  public void toggleDevelopmentNeedNEWArchive(long employeeId, int developmentNeedId)
      throws InvalidAttributeValueException
  {
    Query<Employee> querySearch = getEmployeeQuery(employeeId);
    Employee employee = querySearch.get();

    if (employee == null) throw new InvalidAttributeValueException(ERROR_USER_NOT_FOUND);

    employee.toggleDevelopmentNeedNEWArchive(developmentNeedId);

    UpdateOperations<Employee> updateOperation = dbConnection.createUpdateOperations(Employee.class)
        .set("newDevelopmentNeeds", employee.getDevelopmentNeedsNEW());
    dbConnection.update(querySearch, updateOperation);
  }

  //////////////////// END NEW DEVELOPMENT NEEDS

}
