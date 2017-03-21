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

import java.io.FileNotFoundException;
import java.io.IOException;
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
import services.db.MorphiaOperations;
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
  private static final String INVALID_DEVELOPMENT_NEED_ID = "The given development Need ID does not exist.";
  private static final String EMPLOYEE_NOT_FOUND = "Employee not found based on the criteria: {} {} ";

  /** String Constant - Represents Feedback Request */
  public static final String FEEDBACK_REQUEST = "Feedback Request";

  private static final String EMPLOYEE_ID = "profile.employeeID";
  private static final String EMAIL_ADDRESS = "profile.emailAddress";
  private static final String NOTES = "notes";
  private static final String OBJECTIVES = "objectives";
  private static final String DEVELOPMENT_NEEDS = "developmentNeeds";
  private static final String FEEDBACK_REQUESTS = "feedbackRequests";
  private static final String FEEDBACK = "feedback";
  private static final String COMPETENCIES = "competencies";
  private static final String LAST_LOGON = "lastLogon";

  // There is only 1 instance of the Datastore in the whole system
  private MorphiaOperations morphiaOperations;

  /** Environment Property - Reference to environment to get property details. */
  private Environment env;

  /* Accesses the Active Directories */
  private EmployeeProfileService employeeProfileService;

  /**
   * EmployeeService Constructor - Responsible for initialising dbConnection.
   *
   * @param dbConnection
   */
  public EmployeeService(MorphiaOperations morphiaOperations, EmployeeProfileService employeeProfileService,
      Environment env)
  {
    this.morphiaOperations = morphiaOperations;
    this.env = env;
    this.employeeProfileService = employeeProfileService;
  }

  /**
   * Gets Employee from database with the specified id
   *
   * @param employeeID ID of the employee
   * @return the employee if exists
   * @throws EmployeeNotFoundException if employee is not found or is null.
   */
  public Employee getEmployee(final long employeeID) throws EmployeeNotFoundException
  {
    Employee employee = morphiaOperations.getEmployee(EMPLOYEE_ID, employeeID);

    if (employee == null)
    {
      throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND + employeeID);
    }

    return employee;
  }

  /**
   * Gets full name of a user from the database
   *
   * @param employeeID
   * @return
   * @throws EmployeeNotFoundException
   */
  public String getFullNameUser(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getProfile().getFullName();
  }

  /**
   * Get a list of the current objectives for a user.
   *
   * @param employeeID
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Objective> getObjectivesForUser(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionObjectives();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param objectiveID
   * @return
   * @throws EmployeeNotFoundException
   */
  public Objective getSpecificObjectiveForUser(long employeeID, int objectiveID)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionOfSpecificObjective(objectiveID);
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Feedback> getFeedbackForUser(long employeeID) throws EmployeeNotFoundException
  {
    List<Feedback> feedbackList = getEmployee(employeeID).getFeedback();
    Collections.reverse(feedbackList);
    return feedbackList;
  }
  
  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   * @throws services.EmployeeNotFoundException 
   */
  public List<Competency> getCompetenciesForUser(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionCompetencies();
  }

  /**
   * Get all notes for user.
   *
   * @param employeeID
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Note> getNotes(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getNotes();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<DevelopmentNeed> getDevelopmentNeedsForUser(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionDevelopmentNeeds();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Competency> EmployeeNotFoundException(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionCompetencies();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @return
   * @throws InvalidAttributeValueException
   */
  public List<EmployeeProfile> getReporteesForUser(long employeeID)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeID);

    List<EmployeeProfile> reporteeList = new ArrayList<>();

    for (String str : employee.getProfile().getReporteeCNs())
    {
      long temp = Long.parseLong(str.substring(str.indexOf('-') + 1).trim());

      reporteeList.add(matchADWithMongoData(employeeProfileService.fetchEmployeeProfile(temp)));
    }
    return reporteeList;
  }

  /**
   * 
   * @param employeeID
   * @return This method inserts a new objective for a specific employee given their ID
   * @throws EmployeeNotFoundException
   */
  public boolean insertNewObjective(long employeeID, Objective data)
      throws InvalidAttributeValueException, services.EmployeeNotFoundException
  {
    // Retrieve Employee with the given ID
    final Employee e = morphiaOperations.getEmployee(EMPLOYEE_ID, employeeID);

    if (e == null)
    {
      throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND);
    }

    if (!e.addObjective(data))
    {
      throw new InvalidAttributeValueException(OBJECTIVE_NOTADDED_ERROR);
    }

    morphiaOperations.updateEmployee(e.getProfile().getEmployeeID(), OBJECTIVES, e.getObjectiveList());

    return true;
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param objectiveID
   * @param progress
   * @return
   * @throws EmployeeNotFoundException
   */
  public boolean updateProgressObjective(long employeeID, int objectiveID, int progress)
      throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    if (progress < -1 || progress > 2)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
    }

    boolean updated = false;
    final Employee employee = getEmployee(employeeID);
    final Objective objective = employee.getLatestVersionOfSpecificObjective(objectiveID);

    if (objective.getProgress() != progress)
    {
      updated = true;
    }
    else
    {
      objective.setProgress(progress);

      if (employee.editObjective(objective))
      {
        morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), OBJECTIVES,
            employee.getObjectiveList());
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
   * @throws EmployeeNotFoundException
   */
  public boolean addNewVersionObjective(long employeeID, int objectiveID, Objective data)
      throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    // Retrieve Employee with the given ID
    Employee e = getEmployee(employeeID);
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
        morphiaOperations.updateEmployee(e.getProfile().getEmployeeID(), OBJECTIVES, e.getObjectiveList());
        return true;
      }
    }
    else
    {
      // if the index hasn't changed its value it means that there is no objective with such ID, therefore throw an
      // exception
      throw new InvalidAttributeValueException(INVALID_OBJECTIVEID);
    }

    return false;
  }

  /**
   * Adds new note to an employee
   *
   * @param employeeID
   * @param note
   * @return
   * @throws EmployeeNotFoundException
   */
  public boolean addNote(long employeeID, Note note) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(employeeID);
    employee.addNote(note);
    morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), NOTES, employee.getNotes());

    return true;
  }

  /**
   * Add new note to reportee
   *
   * @param reporteeEmployeeID
   * @param note
   * @return
   * @throws InvalidAttributeValueException
   * @throws EmployeeNotFoundException
   */
  public boolean addNoteToReportee(long reporteeEmployeeID, Note note) throws EmployeeNotFoundException
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
   * @throws EmployeeNotFoundException
   */
  public boolean insertNewDevelopmentNeed(long employeeID, DevelopmentNeed data)
      throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    // Retrieve Employee with the given ID
    Employee e = getEmployee(employeeID);
    // Add the new development need to the list
    if (e.addDevelopmentNeed(data))
    {
      morphiaOperations.updateEmployee(employeeID, DEVELOPMENT_NEEDS, e.getDevelopmentNeedsList());
      return true;
    }
    else
    {
      throw new InvalidAttributeValueException(DEVELOPMENTNEED_NOTADDED_ERROR);
    }
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeID
   * @param devNeedID
   * @param progress
   * @return
   * @throws InvalidAttributeValueException
   * @throws EmployeeNotFoundException
   */
  public boolean updateProgressDevelopmentNeed(long employeeID, int devNeedID, int progress)
      throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    if (progress < -1 || progress > 2)
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
    }

    boolean updated = false;
    final Employee employee = getEmployee(employeeID);
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
        morphiaOperations.updateEmployee(employeeID, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsList());
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
  public boolean addNewVersionDevelopmentNeed(long employeeID, int devNeedID, DevelopmentNeed data)
      throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    // Retrieve Employee with the given ID
    Employee e = getEmployee(employeeID);
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
        morphiaOperations.updateEmployee(employeeID, DEVELOPMENT_NEEDS, e.getDevelopmentNeedsList());
        return true;
      }
    }
    else
    {
      // if the index hasn't changed its value it means that there is no development need with such ID, therefore
      // throw and exception
      throw new InvalidAttributeValueException(INVALID_DEVNEEDID_CONTEXT);
    }

    return false;
  }

  public void toggleDevNeedArchive(long employeeID, int developmentNeedID)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeID);
    DevelopmentNeed curDevNeed = employee.getLatestVersionOfSpecificDevelopmentNeed(developmentNeedID);

    if (curDevNeed == null)
    {
      throw new InvalidAttributeValueException(INVALID_DEVELOPMENT_NEED_ID);
    }

    DevelopmentNeed developmentNeed = new DevelopmentNeed(curDevNeed);
    developmentNeed.setIsArchived(!developmentNeed.getIsArchived());

    if (employee.editDevelopmentNeed(developmentNeed))
    {
      morphiaOperations.updateEmployee(employeeID, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsList());
    }
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

    if (recipientList.size() > 20)
      throw new InvalidAttributeValueException("There must be less than 20 email addresses.");

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
  private void addFeedbackRequest(Employee employee, FeedbackRequest feedbackRequest)
      throws InvalidAttributeValueException
  {
    Validate.isNull(employee, feedbackRequest);
    employee.addFeedbackRequest(feedbackRequest);
    morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), FEEDBACK_REQUESTS,
        employee.getFeedbackRequestsList());
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
    Employee employee = morphiaOperations.getEmployee(EMAIL_ADDRESS, recipientEmail);
    Feedback feedback = new Feedback(employee.nextFeedbackID(), providerEmail, feedbackDescription);

    feedback.setProviderName(employeeProfileService.fetchEmployeeProfileFromEmailAddress(providerEmail).getFullName());
    employee.addFeedback(feedback);
    morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), FEEDBACK, employee.getFeedback());

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
    morphiaOperations.updateEmployee(employeeID, FEEDBACK_REQUESTS, employee.getFeedbackRequestsList());
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
   * @throws EmployeeNotFoundException 
   */
  public boolean addNewVersionCompetency(long employeeID, Competency data, String title)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    // Retrieve Employee with the given ID
    Employee e = getEmployee(employeeID);
    // Add the updated version of the note
    if (e.updateCompetency(data, title))
    {
      morphiaOperations.updateEmployee(employeeID, COMPETENCIES, e.getCompetenciesList());
      return true;
    }
    
    return false;
  }

  /**
   * TODO: Describe this method.
   *
   * @param profileFromAD
   * @return
   * @throws InvalidAttributeValueException
   * @throws EmployeeNotFoundException 
   */
  public EmployeeProfile matchADWithMongoData(EmployeeProfile profileFromAD) throws InvalidAttributeValueException, EmployeeNotFoundException
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

    Employee e = getEmployee(profileFromAD.getEmployeeID());

    if (e != null)
    {
      final boolean needsUpdate = !e.getProfile().equals(profileFromAD);
      LOGGER.debug("Employee (" + e.getProfile().getEmployeeID() + ") needs update: " + needsUpdate);
      if (needsUpdate)
      {
        e.setProfile(profileFromAD);
        LOGGER.debug("Updating employee: " + e.getProfile().getEmployeeID());
        morphiaOperations.updateEmployee(e);
      }
    }
    else
    {
      e = new Employee(profileFromAD);
      LOGGER.debug("Inserting employee: " + e.getProfile().getEmployeeID());
      morphiaOperations.updateEmployee(e);
    }

    return e.getProfile();
  }

  public EmployeeProfile authenticateUserProfile(String usernameEmail) throws EmployeeNotFoundException
  {
    return employeeProfileService.fetchEmployeeProfile(usernameEmail);
  }

  public void updateLastLoginDate(EmployeeProfile profile) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(profile.getEmployeeID());
    employee.setLastLogon(Utils.localDateTimetoDate(LocalDateTime.now()));
    morphiaOperations.updateEmployee(profile.getEmployeeID(), LAST_LOGON, employee.getLastLogon());
  }
}
