package services;

import static services.db.MongoOperations.objectiveHistoryIdFilter;
import static dataStructure.Employee.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import dataStructure.Note;
import dataStructure.Objective;
import services.db.MongoOperations;
import services.db.MorphiaOperations;
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
      MongoOperations objectivesHistoriesOperations, EmployeeProfileService employeeProfileService, Environment env)
  {
    this.employeeService = employeeService;
    this.morphiaOperations = morphiaOperations;
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

  public void proposeObjectiveNEW(long employeeId, Objective objective, Set<String> emailSet)
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
        employee.addObjectiveNEW(objective);

        objectivesHistoriesOperations.addToObjDevHistory(
            objectiveHistoryIdFilter(employeeId, objective.getId(), objective.getCreatedOn()), objective.toDocument());

        morphiaOperations.updateEmployee(employee.getProfile().getEmployeeID(), OBJECTIVES,
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

  public void addManagerEvaluation(long reporteeId, int year, String managerEvaluation, int score)
      throws EmployeeNotFoundException
  {
    Employee employee = employeeService.getEmployee(reporteeId);
    employee.addManagerEvaluation(year, managerEvaluation, score);
    morphiaOperations.updateEmployee(reporteeId, RATINGS, employee.getRatings());
  }
}
