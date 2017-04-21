package services;

import static dataStructure.Constants.UK_TIMEZONE;
import static dataStructure.EmployeeProfile.*;
import static dataStructure.Employee.*;
import static dataStructure.CRUD.*;
import static services.db.MongoOperations.developmentNeedHistoryIdFilter;
import static services.db.MongoOperations.objectiveHistoryIdFilter;
import static utils.Utils.generateFeedbackRequestID;
import static utils.Utils.getEmployeeIDFromRequestID;
import static utils.Utils.localDateTimetoDate;
import static utils.Utils.stringEmailsToHashSet;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

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
import com.mongodb.client.model.Updates;

import dataStructure.CRUD;
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
  private MongoOperations employeeOperations;

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
  public EmployeeService(MorphiaOperations morphiaOperations, MongoOperations employeeOperations,
      MongoOperations objectivesHistoriesOperations, MongoOperations developmentNeedsHistoriesOperations,
      MongoOperations competenciesHistoriesOperations, EmployeeProfileService employeeProfileService, Environment env)
  {
    this.morphiaOperations = morphiaOperations;
    this.employeeOperations = employeeOperations;
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
    EmployeeProfile profile = employee.getProfile();
    employee.addNote(note);

    if (profile.getFullName().equals(note.getProviderName()))
    {
      employee.addActivity(note.createActivity(ADD, profile));
      updateActivityFeed(employee);
    }

    morphiaOperations.updateEmployee(profile.getEmployeeID(), NOTES, employee.getNotes());
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
    employee.addActivity(feedbackRequest.createActivity(employee.getProfile()));
    morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), FEEDBACK_REQUESTS,
        employee.getFeedbackRequests());
    updateActivityFeed(employee);
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
    morphiaOperations.updateEmployee(employeeID, FEEDBACK_REQUESTS, employee.getFeedbackRequests());
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

  public void updateLastLoginDate(EmployeeProfile profile) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(profile.getEmployeeID());
    employee.setLastLogon(localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE)));
    morphiaOperations.updateEmployee(profile.getEmployeeID(), LAST_LOGON, employee.getLastLogon());
  }

  //////////////////// START NEW OBJECTIVES

  public List<Objective> getObjectives(long employeeId) throws EmployeeNotFoundException
  {
    return getEmployee(employeeId).getObjectives();
  }

  public void addObjective(long employeeId, Objective objective)
      throws EmployeeNotFoundException, DocumentConversionException
  {
    Employee employee = getEmployee(employeeId);
    EmployeeProfile profile = employee.getProfile();

    objective.setProposedBy(profile.getFullName());
    employee.addObjective(objective);
    employee.addActivity(objective.createActivity(ADD, profile));

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objective.getId(), objective.getCreatedOn()), objective.toDocument());
    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectives());
    updateActivityFeed(employee);
  }

  public void editObjective(long employeeId, Objective objective)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    Document update = employee.getObjective(objective.getId()).differences(objective);

    if (update.isEmpty())
    {
      return;
    }

    employee.editObjective(objective);
    employee.addActivity(objective.createActivity(EDIT, employee.getProfile()));

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objective.getId(),
            employee.getObjective(objective.getId()).getCreatedOn()),
        update.append("timestamp", employee.getObjective(objective.getId()).getLastModifiedAsDate()));
    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectives());
    updateActivityFeed(employee);
  }

  public void deleteObjective(long employeeId, int objectiveId, String comment)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    Document deletedId = new Document(
        objectiveHistoryIdFilter(employeeId, objectiveId, employee.getObjective(objectiveId).getCreatedOn()));
    String title = employee.getObjective(objectiveId).getTitle();
    String commentAdded = (!comment.isEmpty()) ? String.format(COMMENT_ADDED, comment) : EMPTY_STRING;
    Objective objective = employee.getObjective(objectiveId);

    employee.deleteObjective(objectiveId);
    employee.addActivity(objective.createActivity(DELETE, employee.getProfile()));

    objectivesHistoriesOperations.addToObjDevHistory(deletedId,
        new Document("deletedOn", objective.getLastModifiedAsDate()));
    // TODO Make these update operations a single call to the db. As it stands this has the potential to cause
    // inconsistency or undesirable results in the db as one call may succeed when the other doesn't.
    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectives());
    morphiaOperations.updateEmployee(employeeId, NOTES, employee.getNotes());
    morphiaOperations.updateEmployee(employeeId, FEEDBACK, employee.getFeedback());
    updateActivityFeed(employee);
    addNote(employeeId, new Note(AUTO_GENERATED,
        String.format(COMMENT_DELTED_OBJECTIVE, employee.getProfile().getFullName(), title, commentAdded)));
  }

  public void updateObjectiveProgress(long employeeId, int objectiveId, Progress progress, String comment)
      throws EmployeeNotFoundException, InvalidAttributeValueException, JsonParseException, JsonMappingException,
      IOException
  {
    Employee employee = getEmployee(employeeId);
    Objective objective = employee.getObjective(objectiveId);

    employee.updateObjectiveProgress(objectiveId, progress);

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objectiveId, objective.getCreatedOn()),
        new Document("progress", progress.getProgressStr()).append("timestamp", objective.getLastModifiedAsDate()));

    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectives());

    if (progress.equals(Progress.COMPLETE))
    {
      employee.addActivity(objective.createActivity(COMPLETE, employee.getProfile()));
      String commentAdded = (!comment.isEmpty()) ? String.format(COMMENT_ADDED, comment) : EMPTY_STRING;
      addNote(employeeId, new Note(AUTO_GENERATED, String.format(COMMENT_COMPLETED_OBJECTIVE,
          employee.getProfile().getFullName(), objective.getTitle(), commentAdded)));
    }
  }

  public void toggleObjectiveArchive(long employeeId, int objectiveId)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    Objective objective = employee.getObjective(objectiveId);
    CRUD crud = objective.getArchived() ? RESTORE : ARCHIVE;

    employee.toggleObjectiveArchive(objectiveId);
    employee.addActivity(objective.createActivity(crud, employee.getProfile()));

    objectivesHistoriesOperations.addToObjDevHistory(
        objectiveHistoryIdFilter(employeeId, objectiveId, objective.getCreatedOn()),
        new Document("isArchived", objective.getArchived()).append("timestamp", objective.getLastModifiedAsDate()));
    morphiaOperations.updateEmployee(employeeId, OBJECTIVES, employee.getObjectives());
    updateActivityFeed(employee);
  }

  //////////////////// END NEW OBJECTIVES

  //////////////////// START NEW DEVELOPMENT NEEDS

  public List<DevelopmentNeed> getDevelopmentNeeds(long employeeId) throws EmployeeNotFoundException
  {
    return getEmployee(employeeId).getDevelopmentNeeds();
  }

  public void addDevelopmentNeedNEW(long employeeId, DevelopmentNeed developmentNeed)
      throws EmployeeNotFoundException, DocumentConversionException
  {
    Employee employee = getEmployee(employeeId);

    developmentNeed.setProposedBy(employee.getProfile().getFullName());
    employee.addDevelopmentNeed(developmentNeed);
    employee.addActivity(developmentNeed.createActivity(ADD, employee.getProfile()));

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeed.getId(), developmentNeed.getCreatedOn()),
        developmentNeed.toDocument());
    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeeds());
    updateActivityFeed(employee);
  }

  public void editDevelopmentNeed(long employeeId, DevelopmentNeed developmentNeed)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    Document update = employee.getDevelopmentNeed(developmentNeed.getId()).differences(developmentNeed);

    if (update.isEmpty())
    {
      return;
    }

    employee.editDevelopmentNeed(developmentNeed);
    employee.addActivity(developmentNeed.createActivity(EDIT, employee.getProfile()));

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeed.getId(),
            employee.getDevelopmentNeed(developmentNeed.getId()).getCreatedOn()),
        update.append("timestamp", employee.getDevelopmentNeed(developmentNeed.getId()).getLastModifiedAsDate()));
    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeeds());
    updateActivityFeed(employee);
  }

  public void deleteDevelopmentNeed(long employeeId, int developmentNeedId, String comment)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    Document deletedId = new Document(developmentNeedHistoryIdFilter(employeeId, developmentNeedId,
        employee.getDevelopmentNeed(developmentNeedId).getCreatedOn()));
    String title = employee.getDevelopmentNeed(developmentNeedId).getTitle();
    String commentAdded = (!comment.isEmpty()) ? String.format(COMMENT_ADDED, comment) : EMPTY_STRING;
    DevelopmentNeed developmentNeed = employee.getDevelopmentNeed(developmentNeedId);

    employee.deleteDevelopmentNeed(developmentNeedId);
    employee.addActivity(developmentNeed.createActivity(DELETE, employee.getProfile()));

    developmentNeedsHistoriesOperations.addToObjDevHistory(deletedId,
        new Document("deletedOn", developmentNeed.getLastModifiedAsDate()));
    // TODO Make these update operations a single call to the db. As it stands this has the potential to cause
    // inconsistency or undesirable results in the db as one call may succeed when the other doesn't.
    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeeds());
    morphiaOperations.updateEmployee(employeeId, NOTES, employee.getNotes());
    morphiaOperations.updateEmployee(employeeId, FEEDBACK, employee.getFeedback());
    updateActivityFeed(employee);

    addNote(employeeId, new Note(AUTO_GENERATED,
        String.format(COMMENT_DELETED_DEVELOPMENT_NEED, employee.getProfile().getFullName(), title, commentAdded)));
  }

  public void updateDevelopmentNeedProgress(long employeeId, int developmentNeedId, Progress progress, String comment)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    DevelopmentNeed developmentNeed = employee.getDevelopmentNeed(developmentNeedId);

    employee.updateDevelopmentNeedProgress(developmentNeedId, progress);

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeedId, developmentNeed.getCreatedOn()),
        new Document("progress", progress.getProgressStr()).append("timestamp",
            developmentNeed.getLastModifiedAsDate()));

    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeeds());

    if (progress.equals(Progress.COMPLETE))
    {
      employee.addActivity(developmentNeed.createActivity(COMPLETE, employee.getProfile()));
      addNote(employeeId, new Note(AUTO_GENERATED, String.format(COMMENT_COMPLETED_DEVELOPMENT_NEED,
          employee.getProfile().getFullName(), developmentNeed.getTitle(), comment)));
    }
  }

  public void toggleDevelopmentNeedArchive(long employeeId, int developmentNeedId)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    DevelopmentNeed developmentNeed = employee.getDevelopmentNeed(developmentNeedId);
    CRUD crud = developmentNeed.getArchived() ? RESTORE : ARCHIVE;

    employee.toggleDevelopmentNeedArchive(developmentNeedId);
    employee.addActivity(employee.getDevelopmentNeed(developmentNeedId).createActivity(crud, employee.getProfile()));

    developmentNeedsHistoriesOperations.addToObjDevHistory(
        developmentNeedHistoryIdFilter(employeeId, developmentNeedId, developmentNeed.getCreatedOn()),
        new Document("isArchived", developmentNeed.getArchived()).append("timestamp",
            developmentNeed.getLastModifiedAsDate()));
    morphiaOperations.updateEmployee(employeeId, DEVELOPMENT_NEEDS, employee.getDevelopmentNeeds());
    updateActivityFeed(employee);
  }

  //////////////////// END NEW DEVELOPMENT NEEDS

  //////////////////// START NEW COMPETENCIES

  public List<Competency> getCompetencies(long employeeId) throws EmployeeNotFoundException
  {
    return getEmployee(employeeId).getCompetencies();
  }

  public void toggleCompetencyNEW(long employeeId, CompetencyTitle competencyTitle)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    employee.toggleCompetency(competencyTitle);

    competenciesHistoriesOperations.addToCompetenciesHistory(employeeId, competencyTitle.getCompetencyTitleStr(),
        employee.getCompetency(competencyTitle).isSelected(),
        employee.getCompetency(competencyTitle).getLastModified());

    morphiaOperations.updateEmployee(employeeId, COMPETENCIES, employee.getCompetencies());
  }

  //////////////////// END NEW COMPETENCIES

  public Map<String, Map<Integer, String>> getTags(long employeeId) throws services.EmployeeNotFoundException
  {
    Map<String, Map<Integer, String>> tags = new HashMap<>();
    Map<Integer, String> objectivesTags = new HashMap<>();
    Map<Integer, String> developmentNeedsTags = new HashMap<>();

    getObjectives(employeeId).forEach(o -> objectivesTags.put(o.getId(), o.getTitle()));
    getDevelopmentNeeds(employeeId).forEach(d -> developmentNeedsTags.put(d.getId(), d.getTitle()));

    tags.put("objectivesTags", objectivesTags);
    tags.put("developmentNeedsTags", developmentNeedsTags);

    return tags;
  }

  public void updateFeedbackTags(long employeeId, int feedbackId, Set<Integer> objectiveIds,
      Set<Integer> developmentNeedIds) throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    for (int id : objectiveIds)
      employee.getObjective(id);

    for (int id : developmentNeedIds)
      employee.getDevelopmentNeed(id);

    employee.updateFeedbackTags(feedbackId, objectiveIds, developmentNeedIds);

    morphiaOperations.updateEmployee(employeeId, FEEDBACK, employee.getFeedback());
  }

  public void updateNotesTags(long employeeId, int noteId, Set<Integer> objectiveIds, Set<Integer> developmentNeedIds)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);

    for (int id : objectiveIds)
      employee.getObjective(id);

    for (int id : developmentNeedIds)
      employee.getDevelopmentNeed(id);

    employee.updateNotesTags(noteId, objectiveIds, developmentNeedIds);

    morphiaOperations.updateEmployee(employeeId, NOTES, employee.getNotes());

  }

  public Rating getRating(long employeeId, int year) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(employeeId);
    return employee.getRating(year);
  }

  public void addSelfEvaluation(long employeeId, int year, String selfEvaluation)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = getEmployee(employeeId);
    employee.addSelfEvaluation(year, selfEvaluation);
    morphiaOperations.updateEmployee(employeeId, RATINGS, employee.getRatings());
  }

  public void submitSelfEvaluation(long employeeId, int year) throws EmployeeNotFoundException
  {
    Employee employee = getEmployee(employeeId);
    employee.submitSelfEvaluation(year);
    morphiaOperations.updateEmployee(employeeId, RATINGS, employee.getRatings());
  }

  private void updateActivityFeed(Employee employee)
  {
    employeeOperations.setFields(eq(EMPLOYEE_ID, employee.getProfile().getEmployeeID()),
        employee.getActivityFeedAsDocument());
  }
}
