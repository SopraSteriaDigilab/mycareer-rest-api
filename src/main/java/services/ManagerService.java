package services;

import static services.db.MongoOperations.objectiveHistoryIdFilter;
import static services.db.MongoUtils.*;
import static dataStructure.Employee.*;
import static dataStructure.EmployeeProfile.*;
import static dataStructure.Activity.*;
import static services.ews.DistributionList.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import dataStructure.Activity;
import dataStructure.DocumentConversionException;
import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import dataStructure.Note;
import dataStructure.Objective;
import services.db.MongoOperations;
import services.db.MorphiaOperations;
import services.ews.DistributionList;
import services.ews.EmailService;
import utils.Template;

public class ManagerService
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final Logger LOGGER = LoggerFactory.getLogger(ManagerService.class);

  /** MorphiaOperations Property - Represents a reference to the database using morphia. */
  private EmployeeService employeeService;

  /** MorphiaOperations Property - Represents a reference to the database using morphia. */
  private MorphiaOperations morphiaOperations;

  /** MorphiaOperations Property - Represents a reference to the database using mongo java driver. */
  private MongoOperations employeeOperations;

  /** MongoOperations Property - Represents a reference to the database using mongo java driver */
  private MongoOperations objectivesHistoriesOperations;

  /** EmployeeProfileService Property - Represents a reference to the employee profile service. */
  private EmployeeProfileService employeeProfileService;

  /** Environment Property - Reference to environment to get property details. */
  private Environment env;

  /**
   * EmployeeService Constructor - Responsible for initialising dbConnection.
   *
   * @param dbConnection
   */
  public ManagerService(EmployeeService employeeService, MorphiaOperations morphiaOperations,
      MongoOperations employeeOperations, MongoOperations objectivesHistoriesOperations,
      EmployeeProfileService employeeProfileService, Environment env)
  {
    this.employeeService = employeeService;
    this.morphiaOperations = morphiaOperations;
    this.employeeOperations = employeeOperations;
    this.objectivesHistoriesOperations = objectivesHistoriesOperations;
    this.employeeProfileService = employeeProfileService;
    this.env = env;
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
    Employee employee = employeeService.getEmployee(employeeID);

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
    employeeService.addNote(reporteeEmployeeID, note);

    Set<String> reporteeEmail = employeeService.getEmployee(reporteeEmployeeID).getProfile().getEmailAddresses()
        .toSet();
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

  @Deprecated
  public void proposeObjective(long employeeId, Objective objective, Set<String> emailSet)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Set<String> successEmails = new HashSet<>();
    Set<String> errorEmails = new HashSet<>();

    Employee proposer = employeeService.getEmployee(employeeId);
    objective.setProposedBy(proposer.getProfile().getFullName());

    for (String email : emailSet)
    {
      try
      {
        Employee employee = employeeService.getEmployee(email);
        String preferredEmail = employee.getProfile().getEmailAddresses().getPreferred(email);
        employee.addObjective(objective);

        objectivesHistoriesOperations.addToObjDevHistory(
            objectiveHistoryIdFilter(employeeId, objective.getId(), objective.getCreatedOn()), objective.toDocument());

        morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), OBJECTIVES, employee.getObjectives());

        successEmails.add(preferredEmail);

        String subject = String.format("Proposed Objective from %s", objective.getProposedBy());
        String body = Template.populateTemplate(env.getProperty("templates.objective.proposed"),
            objective.getProposedBy());
        EmailService.sendEmail(preferredEmail, subject, body);
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
      if (successEmails.isEmpty())
      {
        throw new InvalidAttributeValueException(
            "Employees not found for the following Email Addresses: " + errorEmails.toString());
      }

      throw new InvalidAttributeValueException("Objective proposed for: " + successEmails.toString()
          + ". Employees not found for the following Email Addresses: " + errorEmails.toString());
    }
  }

  public void proposeObjective(long employeeId, Objective objective, DistributionList distributionList)
      throws EmployeeNotFoundException, DocumentConversionException
  {
    final Employee proposer = employeeService.getEmployee(employeeId);
    final Set<EmployeeProfile> profileList = distributionList.getList();

    objective.setProposedBy(proposer.getProfile().getFullName());

    for (final EmployeeProfile employeeProfile : profileList)
    {
      final Employee employee = employeeService.getEmployee(employeeProfile.getEmployeeID());

      employee.addObjective(objective);
      objectivesHistoriesOperations.addToObjDevHistory(
          objectiveHistoryIdFilter(employeeId, objective.getId(), objective.getCreatedOn()), objective.toDocument());
      morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), OBJECTIVES, employee.getObjectives());
    }

    sendObjectiveEmail(distributionList, objective);
  }

  private void sendObjectiveEmail(final DistributionList distributionList, final Objective objective)
  {
    try
    {
      final String subject = String.format("Proposed Objective from %s", objective.getProposedBy());
      final String body = Template.populateTemplate(env.getProperty("templates.objective.proposed"),
          objective.getProposedBy());

      distributionList.sendEmail(subject, body);
    }
    catch (Exception e)
    {
      LOGGER.error("Email could not be sent for a proposed objective. Error: ", e);
    }
  }

  public void addManagerEvaluation(long reporteeId, int year, String managerEvaluation, int score)
      throws EmployeeNotFoundException, InvalidAttributeValueException
  {
    Employee employee = employeeService.getEmployee(reporteeId);
    employee.addManagerEvaluation(year, managerEvaluation, score);
    morphiaOperations.updateEmployee(reporteeId, RATINGS, employee.getRatings());
  }

  public void submitManagerEvaluation(long employeeId, int year)
      throws InvalidAttributeValueException, EmployeeNotFoundException, FileNotFoundException, IOException
  {
    Employee employee = employeeService.getEmployee(employeeId);
    employee.submitManagerEvaluation(year);
    morphiaOperations.updateEmployee(employeeId, RATINGS, employee.getRatings());

    String reporteeEmail = employee.getProfile().getEmailAddresses().getPreferred();
    String subject = "Manager Rating Submitted";
    String body = Template.populateTemplate(env.getProperty("template.manager.evaluation.submitted"));

    try
    {
      EmailService.sendEmail(reporteeEmail, subject, body);
    }
    catch (Exception e)
    {
      LOGGER.error("Failed sending manager rating email notification to {}.", reporteeEmail);
    }
  }

  public List<Activity> getActivityFeed(final long employeeID)
  {
    final List<Long> reporteeIDs = getReportees(employeeID);
    final Document matchEmployeeIDs = matchField(EMPLOYEE_ID, in(reporteeIDs));
    final Document projectActivityFeeds = projectField(ACTIVITY_FEED);
    final Document unwindActivityFeeds = unwind(ACTIVITY_FEED);
    final Document sortMostRecent = sortDescending(TIMESTAMP);
    final List<Document> resultsDocuments = employeeOperations.aggregateAsList(matchEmployeeIDs, projectActivityFeeds,
        unwindActivityFeeds, sortMostRecent);
    final List<Activity> activityFeed = new ArrayList<>();

    resultsDocuments.forEach(d -> activityFeed.add(Activity.ofDocument(d)));

    return activityFeed;
  }

  @SuppressWarnings("unchecked")
  private List<Long> getReportees(final long employeeID)
  {
    final List<Long> reporteeIDs = new ArrayList<>();
    final String reportees = "reportees";
    final Document matchEmployeeID = matchField(EMPLOYEE_ID, employeeID);
    final Document projectReporteeCNs = projectRenamedField(reportees, REPORTEE_CNS);
    final Document unwindReporteeCNs = unwind(reportees);
    final Document groupReporteeStrings = addSubstringToSet(reportees, "6", 6);
    final Document projectExcludeId = projectExcludeId();
    List<String> reporteeIDStrings = null;

    try
    {
      reporteeIDStrings = (List<String>) employeeOperations.aggregateSingleResult(reportees, List.class,
          matchEmployeeID, projectReporteeCNs, unwindReporteeCNs, groupReporteeStrings, projectExcludeId);
    }
    catch (NullPointerException e)
    {
      return reporteeIDs;
    }

    reporteeIDStrings.forEach(s -> reporteeIDs.add(Long.parseLong(s)));

    return reporteeIDs;
  }
}
