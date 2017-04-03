package services;

import static dataStructure.Constants.DEVELOPMENTNEED_NOTADDED_ERROR;
import static dataStructure.Constants.INVALID_CONTEXT_PROGRESS;
import static dataStructure.Constants.INVALID_DEVNEEDID_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_OBJECTIVEID;
import static dataStructure.Constants.NULL_USER_DATA;
import static dataStructure.Constants.OBJECTIVE_NOTADDED_ERROR;
import static dataStructure.Constants.UK_TIMEZONE;
import static services.db.MongoOperations.developmentNeedHistoryIdFilter;
import static services.db.MongoOperations.objectiveHistoryIdFilter;
import static utils.Utils.generateFeedbackRequestID;
import static utils.Utils.getEmployeeIDFromRequestID;
import static utils.Utils.localDateTimetoDate;
import static utils.Utils.stringEmailsToHashSet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dataStructure.Competency;
import dataStructure.Competency.CompetencyTitle;
import dataStructure.Competency_OLD;
import dataStructure.DevelopmentNeed;
import dataStructure.DevelopmentNeed_OLD;
import dataStructure.DocumentConversionException;
import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Objective.Progress;
import dataStructure.Objective_OLD;
import services.db.MongoOperations;
import services.db.MorphiaOperations;
import services.ews.EmailService;
import utils.Template;
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
  private static final String EMPLOYEE_NOT_FOUND = "Employee not found based on the criteria: {} {} ";

  /** String Constant - Represents Feedback Request */
  public static final String FEEDBACK_REQUEST = "Feedback Request";

  private static final String EMPLOYEE_ID = "profile.employeeID";
  private static final String NOTES = "notes";
  private static final String OBJECTIVES = "objectives";
  private static final String DEVELOPMENT_NEEDS = "developmentNeeds";
  private static final String FEEDBACK_REQUESTS = "feedbackRequests";
  private static final String FEEDBACK = "feedback";
  private static final String COMPETENCIES = "competencies";
  private static final String LAST_LOGON = "lastLogon";

  private static final String NEW_OBJECTIVES = "objectives";
  private static final String NEW_DEVELOPMENT_NEEDS = "developmentNeeds";
  private static final String NEW_COMPETENCIES = "competencies";

  private static final String AUTO_GENERATED = "Auto Generated";
  private static final String COMMENT_DELTED_OBJECTIVE = "%s has deleted Objective '%s'. %s";
  private static final String COMMENT_DELETED_DEVELOPMENT_NEED = "%s has deleted Development Need '%s'. %s";
  private static final String COMMENT_COMPLETED_OBJECTIVE = "%s has completed Objective '%s'. %s";
  private static final String COMMENT_COMPLETED_DEVELOPMENT_NEED = "%s has completed Development Need '%s'. %s";
  private static final String COMMENT_ADDED = "A comment was added: '%s'";

  private static final String EMPTY_STRING = "";

  /** MorphiaOperations Property - Represents a reference to the database using morphia. */
  private MorphiaOperations morphiaOperations;

  /** MongoOperations Property - Represents a reference to the database using mongo java driver */
  private MongoOperations objectivesHistoriesOperations;

  /** MongoOperations Property - Represents a reference to the database using mongo java driver */
  private MongoOperations developmentNeedsHistoriesOperations;

  /** MongoOperations Property - Represents a reference to the database using mongo java driver */
  private MongoOperations competenciesHistoriesOperations;

  /** EmployeeProfileService Property - Represents a reference to the employee profile service. */
  private EmployeeProfileService employeeProfileService;

  /** Environment Property - Reference to environment to get property details. */
  private Environment env;

  /**
   * EmployeeService Constructor - Responsible for initialising dbConnection.
   *
   * @param dbConnection
   */
  public EmployeeService(MorphiaOperations morphiaOperations,
      MongoOperations objectivesHistoriesOperations, MongoOperations developmentNeedsHistoriesOperations,
      MongoOperations competenciesHistoriesOperations, EmployeeProfileService employeeProfileService, Environment env)
  {
    this.morphiaOperations = morphiaOperations;
    this.objectivesHistoriesOperations = objectivesHistoriesOperations;
    this.developmentNeedsHistoriesOperations = developmentNeedsHistoriesOperations;
    this.competenciesHistoriesOperations = competenciesHistoriesOperations;
    this.employeeProfileService = employeeProfileService;
    this.env = env;
  }

  /**
   * Gets Employee from database with the specified id
   *
   * @param employeeId ID of the employee
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
   * Gets Employee from database with the specified email
   *
   * @param email of the employee
   * @return the employee if exists
   * @throws EmployeeNotFoundException if employee is not found or is null.
   */
  public Employee getEmployee(final String email) throws EmployeeNotFoundException
  {
    Employee employee = morphiaOperations.getEmployeeFromEmailAddress(email);
    if (employee == null)
    {
      throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND + email);
    }
    return employee;
  }

  /**
   * Gets full name of a user from the database
   *
   * @param employeeId
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
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Objective_OLD> getObjectivesForUser(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionObjectives();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @param objectiveID
   * @return
   * @throws EmployeeNotFoundException
   */
  public Objective_OLD getSpecificObjectiveForUser(long employeeID, int objectiveID)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    return getEmployee(employeeID).getLatestVersionOfSpecificObjective(objectiveID);
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
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
   * @param employeeId
   * @return
   * @throws InvalidAttributeValueException
   * @throws services.EmployeeNotFoundException
   */
  public List<Competency_OLD> getCompetenciesForUser(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionCompetencies();
  }

  /**
   * Get all notes for user.
   *
   * @param employeeId
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
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<DevelopmentNeed_OLD> getDevelopmentNeedsForUser(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionDevelopmentNeeds();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
   * @throws EmployeeNotFoundException
   */
  public List<Competency_OLD> EmployeeNotFoundException(long employeeID) throws EmployeeNotFoundException
  {
    return getEmployee(employeeID).getLatestVersionCompetencies();
  }

  /**
   * TODO: Describe this method.
   *
   * @param employeeId
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
      EmployeeProfile profile = null;
      try
      {
        profile = employeeProfileService.fetchEmployeeProfile(temp);
        reporteeList.add(profile);
      }
      catch (EmployeeNotFoundException e)
      {
        /*
         * This employee is not perm/internal staff
         */
      }
    }
    return reporteeList;
  }

  /**
   * 
   * @param employeeId
   * @return This method inserts a new objective for a specific employee given their ID
   * @throws EmployeeNotFoundException
   */
  public boolean insertNewObjective(long employeeID, Objective_OLD data)
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
   * @param employeeId
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
    final Objective_OLD objective = employee.getLatestVersionOfSpecificObjective(objectiveID);

    if (objective.getProgress() == progress)
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
   * @param employeeId
   * @param objectiveID
   * @param data
   * @return
   * @throws EmployeeNotFoundException
   */
  public boolean addNewVersionObjective(long employeeID, int objectiveID, Objective_OLD data)
      throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    // Retrieve Employee with the given ID
    Employee e = getEmployee(employeeID);
    // Extract its List of Objectives
    List<List<Objective_OLD>> dataFromDB = e.getObjectiveList();
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
      if (e.editObjective((Objective_OLD) data))
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
   * @param employeeId
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

    Set<String> reporteeEmail = getEmployee(reporteeEmployeeID).getProfile().getEmailAddresses();
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
   * @throws EmployeeNotFoundException
   */
  public boolean insertNewDevelopmentNeed(long employeeID, DevelopmentNeed_OLD data)
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
   * @param employeeId
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
    final DevelopmentNeed_OLD devNeed = employee.getLatestVersionOfSpecificDevelopmentNeed(devNeedID);

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
   * @param employeeId
   * @param devNeedID
   * @param data
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addNewVersionDevelopmentNeed(long employeeID, int devNeedID, DevelopmentNeed_OLD data)
      throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    // Retrieve Employee with the given ID
    Employee e = getEmployee(employeeID);
    // Extract its List of notes
    List<List<DevelopmentNeed_OLD>> dataFromDB = e.getDevelopmentNeedsList();
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
      if (e.editDevelopmentNeed((DevelopmentNeed_OLD) data))
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
    DevelopmentNeed_OLD curDevNeed = employee.getLatestVersionOfSpecificDevelopmentNeed(developmentNeedID);

    if (curDevNeed == null)
    {
      throw new InvalidAttributeValueException(INVALID_DEVELOPMENT_NEED_ID);
    }

    DevelopmentNeed_OLD developmentNeed = new DevelopmentNeed_OLD(curDevNeed);
    developmentNeed.setIsArchived(!developmentNeed.getIsArchived());

    if (employee.editDevelopmentNeed(developmentNeed))
    {
      morphiaOperations.updateEmployee(employeeID, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsList());
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
  public void processFeedbackRequest(long employeeID, String emailsString, String notes) throws Exception
  {
    Employee requester = getEmployee(employeeID);
    Set<String> recipientList = stringEmailsToHashSet(emailsString);

    if (recipientList.size() > 20)
      throw new InvalidAttributeValueException("There must be less than 20 email addresses.");

    List<String> errorRecipientList = new ArrayList<String>();
    String requesterName = requester.getProfile().getFullName();

    for (String recipient : recipientList)
    {
      String tempID = generateFeedbackRequestID(employeeID);
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

  public void addFeedback(long employeeId, Set<String> emailSet, String feedback, boolean isFeedbackRequest)
      throws Exception
  {
    Employee employee = getEmployee(employeeId);
    addFeedback(employee.getProfile().getEmailAddresses().stream().findFirst().get(), emailSet, feedback,
        isFeedbackRequest);

    String subject = String.format("Feedback from %s", employee.getProfile().getFullName());
    String body = Template.populateTemplate(env.getProperty("templates.feedback.generic"),
        employee.getProfile().getFullName());

    EmailService.sendEmail(emailSet, subject, body);
  }

  public void addFeedback(String providerEmail, String recipientEmail, String feedbackDescription,
      boolean isFeedbackRequest) throws Exception
  {
    final Set<String> recipientEmails = Stream.of(recipientEmail).collect(Collectors.toSet());
    addFeedback(providerEmail, recipientEmails, feedbackDescription, isFeedbackRequest);
  }

  /**
   * Add a feedback to an employee
   *
   * @param providerEmail
   * @param recipientEmail
   * @param feedbackDescription
   * @throws Exception
   */
  public void addFeedback(String providerEmail, Set<String> recipientEmail, String feedbackDescription,
      boolean isFeedbackRequest) throws Exception
  {
    Validate.areStringsEmptyorNull(providerEmail, feedbackDescription);
    Validate.areStringsEmptyorNull(recipientEmail.toArray(new String[0]));
    Employee employee = morphiaOperations.getEmployeeFromEmailAddress(recipientEmail);
    Feedback feedback = new Feedback(employee.nextFeedbackID(), providerEmail, feedbackDescription);
    String providerName = null;

    try
    {
      providerName = employeeProfileService.fetchEmployeeProfileFromEmailAddress(providerEmail).getFullName();
    }
    catch (final EmployeeNotFoundException e)
    {
      /*
       * If this happens the provider email address is external (not SopraSteria). This is a normal operation so just
       * swallow the exception and set provider name to provider email address
       */
      providerName = providerEmail;
    }

    feedback.setProviderName(providerName);
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
    long employeeID = getEmployeeIDFromRequestID(feedbackRequestID);
    Employee employee = getEmployee(employeeID);

    employee.getFeedbackRequest(feedbackRequestID).setReplyReceived(true);
    morphiaOperations.updateEmployee(employeeID, FEEDBACK_REQUESTS, employee.getFeedbackRequestsList());
    addFeedback(providerEmail, employee.getProfile().getEmailAddresses(), feedbackDescription, true);
  }

  /**
   * 
   * @param employeeId the employee IDsdfsd
   * @param data the Competency to update
   * @param title the title of the competency (max 200 characters)
   * @return true or false to establish whether the task has been completed successfully or not This method inserts a
   *         new version of competencies list
   * @throws InvalidAttributeValueException
   * @throws EmployeeNotFoundException
   */
  public boolean addNewVersionCompetency(long employeeID, Competency_OLD data, String title)
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

  public EmployeeProfile authenticateUserProfile(String usernameEmail) throws EmployeeNotFoundException
  {
    return employeeProfileService.fetchEmployeeProfile(usernameEmail);
  }

  public void updateLastLoginDate(EmployeeProfile profile) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(profile.getEmployeeID());
    employee.setLastLogon(localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE)));
    morphiaOperations.updateEmployee(profile.getEmployeeID(), LAST_LOGON, employee.getLastLogon());
  }

  //////////////////// START NEW OBJECTIVES

  public List<Objective> getObjectivesNEW(long employeeId) throws EmployeeNotFoundException
  {
    return getEmployee(employeeId).getObjectivesNEW();
  }

  public void addObjectiveNEW(long employeeId, Objective objective)
      throws EmployeeNotFoundException, DocumentConversionException
  {
    Employee employee = getEmployee(employeeId);

    objective.setProposedBy(employee.getProfile().getFullName());
    employee.addObjectiveNEW(objective);

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objective.getId(), objective.getCreatedOn()), objective.toDocument());

    morphiaOperations.updateEmployee(employeeId, NEW_OBJECTIVES, employee.getObjectivesNEW());
  }

  public void editObjectiveNEW(long employeeId, Objective objective)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    Document update = employee.getObjectiveNEW(objective.getId()).differences(objective);

    if (update.isEmpty()) return;

    employee.editObjectiveNEW(objective);

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objective.getId(),
            employee.getObjectiveNEW(objective.getId()).getCreatedOn()),
        update.append("timestamp", localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_OBJECTIVES, employee.getObjectivesNEW());
  }

  public void deleteObjectiveNEW(long employeeId, int objectiveId, String comment)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    Document deletedId = new Document(
        objectiveHistoryIdFilter(employeeId, objectiveId, employee.getObjectiveNEW(objectiveId).getCreatedOn()));

    String title = employee.getObjectiveNEW(objectiveId).getTitle();

    employee.deleteObjectiveNEW(objectiveId);

    objectivesHistoriesOperations.addToObjDevHistory(deletedId,
        new Document("deletedOn", localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_OBJECTIVES, employee.getObjectivesNEW());

    String commentAdded = (!comment.isEmpty()) ? String.format(COMMENT_ADDED, comment) : EMPTY_STRING;

    addNote(employeeId, new Note(AUTO_GENERATED,
        String.format(COMMENT_DELTED_OBJECTIVE, employee.getProfile().getFullName(), title, commentAdded)));
  }

  public void updateObjectiveNEWProgress(long employeeId, int objectiveId, Progress progress, String comment)
      throws EmployeeNotFoundException, InvalidAttributeValueException, JsonParseException, JsonMappingException,
      IOException
  {
    Employee employee = getEmployee(employeeId);

    employee.updateObjectiveNEWProgress(objectiveId, progress);

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objectiveId, employee.getObjectiveNEW(objectiveId).getCreatedOn()),
        new Document("progress", progress.getProgressStr()).append("timestamp",
            localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_OBJECTIVES, employee.getObjectivesNEW());

    if (progress.equals(Progress.COMPLETE))
    {
      String commentAdded = (!comment.isEmpty()) ? String.format(COMMENT_ADDED, comment) : EMPTY_STRING;
      
      addNote(employeeId, new Note(AUTO_GENERATED, String.format(COMMENT_COMPLETED_OBJECTIVE,
          employee.getProfile().getFullName(), employee.getObjectiveNEW(objectiveId).getTitle(), commentAdded)));
    }
  }

  public void toggleObjectiveNEWArchive(long employeeId, int objectiveId)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    employee.toggleObjectiveNEWArchive(objectiveId);

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objectiveId, employee.getObjectiveNEW(objectiveId).getCreatedOn()),
        new Document("isArchived", employee.getObjectiveNEW(objectiveId).getArchived()).append("timestamp",
            localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_OBJECTIVES, employee.getObjectivesNEW());
  }

  public void proposeObjectiveNEW(long employeeId, Objective objective, Set<String> emailSet)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Set<String> successEmails = new HashSet<>();
    Set<String> errorEmails = new HashSet<>();

    Employee proposer = getEmployee(employeeId);
    objective.setProposedBy(proposer.getProfile().getFullName());

    for (String email : emailSet)
    {
      try
      {
        Employee employee = getEmployee(email);
        employee.addObjectiveNEW(objective);

        objectivesHistoriesOperations.addToObjDevHistory(
            objectiveHistoryIdFilter(employeeId, objective.getId(), objective.getCreatedOn()), objective.toDocument());

        morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), NEW_OBJECTIVES,
            employee.getObjectivesNEW());

        successEmails.add(email);

        String subject = String.format("Proposed Objective from %s", objective.getProposedBy());
        String body = Template.populateTemplate(env.getProperty("templates.objective.proposed"),
            objective.getProposedBy());
        EmailService.sendEmail(email, subject, body);
      }
      catch (EmployeeNotFoundException e)
      {
        errorEmails.add(email);
        continue;
      }
      catch (Exception e)
      {
        LOGGER.error("Email could not be sent for a proposed objective. Error: ", e);
      }
    }

    if (!errorEmails.isEmpty())
    {
      if (successEmails.isEmpty()) throw new InvalidAttributeValueException(
          "Employees not found for the following Email Addresses: " + errorEmails.toString());
      throw new InvalidAttributeValueException("Objective proposed for: " + successEmails.toString()
          + ". Employees not found for the following Email Addresses: " + errorEmails.toString());
    }

  }

  //////////////////// END NEW OBJECTIVES

  //////////////////// START NEW DEVELOPMENT NEEDS

  public List<DevelopmentNeed> getDevelopmentNeedsNEW(long employeeId) throws EmployeeNotFoundException
  {
    return getEmployee(employeeId).getDevelopmentNeedsNEW();
  }

  public void addDevelopmentNeedNEW(long employeeId, DevelopmentNeed developmentNeed)
      throws EmployeeNotFoundException, DocumentConversionException
  {
    Employee employee = getEmployee(employeeId);

    developmentNeed.setProposedBy(employee.getProfile().getFullName());
    employee.addDevelopmentNeedNEW(developmentNeed);

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeed.getId(), developmentNeed.getCreatedOn()),
        developmentNeed.toDocument());

    morphiaOperations.updateEmployee(employeeId, NEW_DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());
  }

  public void editDevelopmentNeedNEW(long employeeId, DevelopmentNeed developmentNeed)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    Document update = employee.getDevelopmentNeedNEW(developmentNeed.getId()).differences(developmentNeed);

    if (update.isEmpty()) return;

    employee.editDevelopmentNeedNEW(developmentNeed);

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeed.getId(),
            employee.getDevelopmentNeedNEW(developmentNeed.getId()).getCreatedOn()),
        update.append("timestamp", localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());

  }

  public void deleteDevelopmentNeedNEW(long employeeId, int developmentNeedId, String comment)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    Document deletedId = new Document(developmentNeedHistoryIdFilter(employeeId, developmentNeedId,
        employee.getDevelopmentNeedNEW(developmentNeedId).getCreatedOn()));

    String title = employee.getDevelopmentNeedNEW(developmentNeedId).getTitle();

    employee.deleteDevelopmentNeedNEW(developmentNeedId);

    developmentNeedsHistoriesOperations.addToObjDevHistory(deletedId,
        new Document("deletedOn", localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());

    String commentAdded = (!comment.isEmpty()) ? String.format(COMMENT_ADDED, comment) : EMPTY_STRING;

    addNote(employeeId, new Note(AUTO_GENERATED,
        String.format(COMMENT_DELETED_DEVELOPMENT_NEED, employee.getProfile().getFullName(), title, commentAdded)));
  }

  public void updateDevelopmentNeedNEWProgress(long employeeId, int developmentNeedId, Progress progress,
      String comment) throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    employee.updateDevelopmentNeedNEWProgress(developmentNeedId, progress);

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeedId,
            employee.getDevelopmentNeedNEW(developmentNeedId).getCreatedOn()),
        new Document("progress", progress.getProgressStr()).append("timestamp",
            localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());

    if (progress.equals(Progress.COMPLETE))
    {
      addNote(employeeId, new Note(AUTO_GENERATED, String.format(COMMENT_COMPLETED_DEVELOPMENT_NEED,
          employee.getProfile().getFullName(), employee.getDevelopmentNeedNEW(developmentNeedId).getTitle(), comment)));
    }
  }

  public void toggleDevelopmentNeedNEWArchive(long employeeId, int developmentNeedId)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    employee.toggleDevelopmentNeedNEWArchive(developmentNeedId);

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeedId,
            employee.getDevelopmentNeedNEW(developmentNeedId).getCreatedOn()),
        new Document("isArchived", employee.getDevelopmentNeedNEW(developmentNeedId).getArchived()).append("timestamp",
            localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE))));

    morphiaOperations.updateEmployee(employeeId, NEW_DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());
  }

  //////////////////// END NEW DEVELOPMENT NEEDS

  //////////////////// START NEW COMPETENCIES

  public List<Competency> getCompetenciesNEW(long employeeId) throws EmployeeNotFoundException
  {
    return getEmployee(employeeId).getCompetenciesNEW();
  }

  public void toggleCompetencyNEW(long employeeId, CompetencyTitle competencyTitle)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    employee.toggleCompetencyNEW(competencyTitle);

    competenciesHistoriesOperations.addToCompetenciesHistory(employeeId,
        competencyTitle.getCompetencyTitleStr(), employee.getCompetencyNEW(competencyTitle).isSelected(),
        employee.getCompetencyNEW(competencyTitle).getLastModified());

    morphiaOperations.updateEmployee(employeeId, NEW_COMPETENCIES, employee.getCompetenciesNEW());
  }

  //////////////////// END NEW COMPETENCIES

  public Map<String, Map<Integer, String>> getTags(long employeeId) throws services.EmployeeNotFoundException
  {
    Map<String, Map<Integer, String>> tags = new HashMap<>();
    Map<Integer, String> objectivesTags = new HashMap<>();
    Map<Integer, String> developmentNeedsTags = new HashMap<>();

    getObjectivesNEW(employeeId).forEach(o -> objectivesTags.put(o.getId(), o.getTitle()));
    getDevelopmentNeedsNEW(employeeId).forEach(d -> developmentNeedsTags.put(d.getId(), d.getTitle()));

    tags.put("objectivesTags", objectivesTags);
    tags.put("developmentNeedsTags", developmentNeedsTags);

    return tags;
  }

  public void updateFeedbackTags(long employeeId, int feedbackId, Set<Integer> objectiveIds,
      Set<Integer> developmentNeedIds) throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    for (int id : objectiveIds)
      employee.getObjectiveNEW(id);

    for (int id : developmentNeedIds)
      employee.getDevelopmentNeedNEW(id);

    employee.updateFeedbackTags(feedbackId, objectiveIds, developmentNeedIds);

    morphiaOperations.updateEmployee(employeeId, FEEDBACK, employee.getFeedback());
  }

  public void updateNotesTags(long employeeId, int noteId, Set<Integer> objectiveIds, Set<Integer> developmentNeedIds)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    for (int id : objectiveIds)
      employee.getObjectiveNEW(id);

    for (int id : developmentNeedIds)
      employee.getDevelopmentNeedNEW(id);

    employee.updateNotesTags(noteId, objectiveIds, developmentNeedIds);

    morphiaOperations.updateEmployee(employeeId, NOTES, employee.getNotes());

  }

}
