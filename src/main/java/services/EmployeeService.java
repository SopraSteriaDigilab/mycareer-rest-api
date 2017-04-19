package services;

import static dataStructure.Constants.DEVELOPMENTNEED_NOTADDED_ERROR;
import static dataStructure.Constants.INVALID_CONTEXT_PROGRESS;
import static dataStructure.Constants.INVALID_DEVNEEDID_CONTEXT;
import static dataStructure.Constants.INVALID_OBJECTIVEID;
import static dataStructure.Constants.OBJECTIVE_NOTADDED_ERROR;
import static dataStructure.Constants.UK_TIMEZONE;
import static dataStructure.EmployeeProfile.*;
import static dataStructure.Employee.*;
import static services.db.MongoOperations.developmentNeedHistoryIdFilter;
import static services.db.MongoOperations.objectiveHistoryIdFilter;
import static utils.Utils.generateFeedbackRequestID;
import static utils.Utils.getEmployeeIDFromRequestID;
import static utils.Utils.localDateTimetoDate;
import static utils.Utils.stringEmailsToHashSet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dataStructure.Competency;
import dataStructure.Competency.CompetencyTitle;
import dataStructure.DevelopmentNeed;
import dataStructure.DocumentConversionException;
import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Objective.Progress;
import dataStructure.Rating;
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
  public EmployeeService(MorphiaOperations morphiaOperations, MongoOperations objectivesHistoriesOperations,
      MongoOperations developmentNeedsHistoriesOperations, MongoOperations competenciesHistoriesOperations,
      EmployeeProfileService employeeProfileService, Environment env)
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

  /**
   * Adding feedback from the MyCareer App.
   *
   * @param employeeId
   * @param emailSet
   * @param feedback
   * @param isFeedbackRequest
   * @throws Exception
   */
  public void addFeedback(long employeeId, Set<String> emailSet, String feedback, boolean isFeedbackRequest)
      throws Exception
  {
    Employee employee = getEmployee(employeeId);
    String employeeEmail = employee.getProfile().getEmailAddresses().toSet().stream().findFirst().get();
    List<String> errorRecipientList = new ArrayList<String>();
    List<String> successfullRecipientList = new ArrayList<String>();

    for (String email : emailSet)
    {
      try
      {
        addFeedback(employeeEmail, email, feedback, isFeedbackRequest);
        successfullRecipientList.add(email);

        String subject = String.format("Feedback from %s", employee.getProfile().getFullName());
        String body = Template.populateTemplate(env.getProperty("templates.feedback.generic"),
            employee.getProfile().getFullName());

        EmailService.sendEmail(email, subject, body);
      }
      catch (EmployeeNotFoundException e)
      {
        errorRecipientList.add(email);
      }
    }

    if (!errorRecipientList.isEmpty())
    {
      if (successfullRecipientList.isEmpty()) throw new InvalidAttributeValueException(
          "Employees not found for the following Email Addresses: " + errorRecipientList.toString());
      throw new InvalidAttributeValueException("Feedback Added for: " + successfullRecipientList.toString()
          + ". Employees not found for the following Email Addresses: " + errorRecipientList.toString());
    }

  }

  /**
   * Add a feedback to an employee
   *
   * @param providerEmail
   * @param recipientEmails
   * @param feedbackDescription
   * @throws InvalidAttributeValueException
   * @throws EmployeeNotFoundException
   */
  public void addFeedback(String providerEmail, String recipientEmail, String feedbackDescription,
      boolean isFeedbackRequest) throws InvalidAttributeValueException, EmployeeNotFoundException
  {
    Validate.areStringsEmptyorNull(providerEmail, feedbackDescription, recipientEmail);
    Employee employee = morphiaOperations.getEmployeeFromEmailAddress(recipientEmail);
    if (employee == null) throw new EmployeeNotFoundException("Employee not found with email: " + recipientEmail);

    Feedback feedback = new Feedback(employee.nextFeedbackID(), providerEmail, feedbackDescription);

    String providerName = (getFullNameFromEmail(providerEmail) != null) ? getFullNameFromEmail(providerEmail)
        : providerEmail;

    feedback.setProviderName(providerName);
    employee.addFeedback(feedback);
    morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), FEEDBACK, employee.getFeedback());
  }

  /**
   * Method that adds requested feedback to an employee
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
    addFeedback(providerEmail, employee.getProfile().getEmailAddresses().toSet().stream().findFirst().get(),
        feedbackDescription, true);

    String provider = (getFullNameFromEmail(providerEmail) != null) ? getFullNameFromEmail(providerEmail)
        : providerEmail;

    String subject = String.format("Feedback Request reply from %s", provider);
    String body = Template.populateTemplate(env.getProperty("templates.feedback.reply"), provider);
    EmailService.sendEmail(employee.getProfile().getEmailAddresses().toSet(), subject, body);
  }

  private String getFullNameFromEmail(String email)
  {
    try
    {
      return employeeProfileService.fetchEmployeeProfileFromEmailAddress(email).getFullName();
    }
    catch (final EmployeeNotFoundException e)
    {
      return null;
    }
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

    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectivesNEW());
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

    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectivesNEW());
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

    // TODO Make these update operations a single call to the db. As it stands this has the potential to cause
    // inconsistency or undesirable results in the db as one call may succeed when the other doesn't.
    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectivesNEW());
    morphiaOperations.updateEmployee(employeeId, NOTES, employee.getNotes());
    morphiaOperations.updateEmployee(employeeId, FEEDBACK, employee.getFeedback());

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

    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectivesNEW());

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

    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectivesNEW());
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

    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());
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

    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());

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

    // TODO Make these update operations a single call to the db. As it stands this has the potential to cause
    // inconsistency or undesirable results in the db as one call may succeed when the other doesn't.
    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());
    morphiaOperations.updateEmployee(employeeId, NOTES, employee.getNotes());
    morphiaOperations.updateEmployee(employeeId, FEEDBACK, employee.getFeedback());

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

    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());

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

    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeedsNEW());
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

    competenciesHistoriesOperations.addToCompetenciesHistory(employeeId, competencyTitle.getCompetencyTitleStr(),
        employee.getCompetencyNEW(competencyTitle).isSelected(),
        employee.getCompetencyNEW(competencyTitle).getLastModified());

    morphiaOperations.updateEmployee(employeeId, COMPETENCIES, employee.getCompetenciesNEW());
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

  public Rating getRating(long employeeId, int year) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(employeeId);
    return employee.getRating(year);
  }

  public void addSelfEvaluation(long employeeId, int year, String selfEvaluation) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(employeeId);
    employee.addSelfEvaluation(year, selfEvaluation);
    morphiaOperations.updateEmployee(employeeId, RATINGS, employee.getRatings());
  }
}
