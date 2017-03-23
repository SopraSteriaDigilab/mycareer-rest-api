package controller;

import static application.GlobalExceptionHandler.error;
import static dataStructure.Constants.UK_TIMEZONE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static utils.Validate.isYearMonthInPast;

import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoException;

import application.GlobalExceptionHandler;
import dataStructure.Competency_OLD;
import dataStructure.Competency;
import dataStructure.Constants;
import dataStructure.DevelopmentNeed_OLD;
import dataStructure.DevelopmentNeed;
import dataStructure.EmployeeProfile;
import dataStructure.Note;
import dataStructure.Objective_OLD;
import dataStructure.Objective;
import dataStructure.Competency.CompetencyTitle;
import services.EmployeeNotFoundException;
import services.EmployeeProfileService;
import services.EmployeeService;
import services.ews.EmailService;
import utils.Template;
import utils.Utils;
import utils.Validate;

/**
 * This class contains all the available roots of the web service
 */
@CrossOrigin
@RestController
@PropertySource("${ENVIRONMENT}.properties")
@Validated
public class EmployeeController
{

  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";
  private static final String ERROR_OBJECTIVE_ID = "The given Objective ID is invalid";
  private static final String ERROR_DEVELOPMENT_NEED_ID = "The given Development Need ID is invalid";
  private static final String ERROR_TITLE_LIMIT = "Max Title lenght is 150 characters";
  private static final String ERROR_TITLE_EMPTY = "Title can not be empty";
  private static final String ERROR_PROVIDER_NAME_LIMIT = "Max Provider Name length is 150 characters.";
  private static final String ERROR_EMAIL_RECIPIENTS_EMPTY = "The emailsTo field can not be empty";
  private static final String ERROR_DATE_FORMAT = "The date format is incorrect";
  private static final String ERROR_NOTE_PROVIDER_NAME_EMPTY = "Provider name can not be empty.";
  private static final String ERROR_NOTE_DESCRIPTION_EMPTY = "Note description can not be empty.";
  private static final String ERROR_NOTE_DESCRIPTION_LIMIT = "Max Description length is 1000 characters.";
  private static final String ERROR_COMPETENCY_TITLE_BLANK = "Compentency title cannot be empty";
  private static final String ERROR_CATEGORY = "Category must be from 0 to 4";
  private static final String ERROR_COMPETENCY_TITLE = "Invalida Competency, please enter one of the following: 'Accountability', 'Effective Communication', 'Leadership', 'Service Excellence', 'Business Awareness', 'Future Orientation', 'Innovation and Change', 'Teamwork'";
  private static final String ERROR_EMAILS_EMPTY = "Emails field can not be empty";
  private static final String REGEX_YEAR_MONTH = "^\\d{4}[-](0[1-9]|1[012])$";
  private static final String REGEX_COMPETENCY_TITLE = "^(Accountability)|(Effective Communication)|(Leadership)|(Service Excellence)|(Business Awareness)|(Future Orientation)|(Innovation and Change)|(Teamwork)$";
  private static final String[] CATEGORY_LIST = { "JobTraining", "ClassroomTraining", "Online", "SelfStudy", "Other" };
  private static final String[] PROGRESS_LIST = { "PROPOSED", "IN_PROGRESS", "COMPLETE" };

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeProfileService employeeProfileService;

  /** Environment Property - Reference to environment to get property details. */
  @Autowired
  private Environment env;

  @RequestMapping(value = "/", method = GET)
  public ResponseEntity<String> welcomePage()
  {
    return ok("Welcome to the MyCareer Project");
  }

  @RequestMapping(value = "/portal", method = GET)
  public void portal(HttpServletRequest request, HttpServletResponse response)
  {
    String currentURL = request.getRequestURL().toString();
    String newUrl = "";
    if (currentURL.contains(":8080/portal"))
    {
      newUrl = currentURL.replace(":8080/portal", "/myobjectives");
    }

    try
    {
      response.sendRedirect(newUrl);
    }
    catch (IOException e)
    {
      LOGGER.error(e.getMessage());
    }

  }

  @RequestMapping(value = "/logMeIn", method = GET)
  public ResponseEntity<?> index(HttpServletRequest request)
  {
    String username = request.getRemoteUser();
    ResponseEntity<?> response = authenticateUserProfile(username);
    try
    {
      if (response.getStatusCode().equals(OK))
      {
        employeeService.updateLastLoginDate((EmployeeProfile) response.getBody());
      }
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e);
    }

    return response;
  }

  /**
   * 
   * This method allows the front-end to retrieve the latest version for each objective related to a specific user
   * 
   * @param employeeID the employee ID (>0)
   * @return the list of objectives (only the latest version of them)
   */
  @RequestMapping(value = "/getObjectives/{employeeID}", method = GET)
  public ResponseEntity<?> getObjectives(@PathVariable long employeeID)
  {
    if (employeeID > 0)
    {
      try
      {
        // Retrieve and return the objectives from the system
        return ok(employeeService.getObjectivesForUser(employeeID));
      }
      catch (MongoException me)
      {
        return badRequest().body("DataBase Connection Error");
      }
      catch (Exception e)
      {
        return badRequest().body(e.getMessage());
      }
    }
    else
    {
      return badRequest().body(Constants.INVALID_CONTEXT_USERID);
    }
  }

  /**
   * 
   * This method allows the front-end to retrieve the latest version of each feedback related to a specific user
   * 
   * @param employeeID the employee ID (>0)
   * @return list of feedback (only the latest version of them)
   */
  @RequestMapping(value = "/getFeedback/{employeeID}", method = GET)
  public ResponseEntity<?> getFeedback(@PathVariable long employeeID)
  {
    if (employeeID > 0)
    {
      try
      {
        return ok(employeeService.getFeedbackForUser(employeeID));
      }
      catch (MongoException me)
      {
        return badRequest().body("DataBase Connection Error");
      }
      catch (Exception e)
      {
        return badRequest().body(e.getMessage());
      }
    }
    else
    {
      return badRequest().body(Constants.INVALID_CONTEXT_USERID);
    }
  }

  /**
   * 
   * GET end point - gets all notes for a user
   * 
   * @param employeeID the ID of the employee
   * @return list of notes
   */
  @RequestMapping(value = "/getNotes/{employeeID}", method = GET)
  public ResponseEntity<?> getNotes(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID)
  {
    try
    {
      return ok(employeeService.getNotes(employeeID));
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to retrieve all the development needs associated to a user
   * 
   * @param employeeID the ID of an employee
   * @return list of development needs (only latest version for each one of them)
   */
  @RequestMapping(value = "/getDevelopmentNeeds/{employeeID}", method = GET)
  public ResponseEntity<?> getDevelomentNeeds(@PathVariable long employeeID)
  {
    if (employeeID > 0)
    {
      try
      {
        return ok(employeeService.getDevelopmentNeedsForUser(employeeID));
      }
      catch (MongoException me)
      {
        return badRequest().body("DataBase Connection Error");
      }
      catch (Exception e)
      {
        return badRequest().body(e.getMessage());
      }
    }
    else
    {
      return badRequest().body(Constants.INVALID_CONTEXT_USERID);
    }
  }

  /**
   * 
   * This method allows the front-end to retrieve all the competencies associated with a user
   * 
   * @param employeeID the ID of an employee
   * @return list of competencies (only latest version for each one of them)
   */
  @RequestMapping(value = "/getCompetencies/{employeeID}", method = GET)
  public ResponseEntity<?> getCompetencies(@PathVariable("employeeID") long employeeID)
  {
    try
    {
      return ok(employeeService.getCompetenciesForUser(employeeID));
    }
    catch (MongoException me)
    {
      return badRequest().body("DataBase Connection Error");
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to retrieve all the reportees associated with a user
   * 
   * @param employeeID the ID of an employee
   * @return list of ADProfileBasics
   */
  @RequestMapping(value = "/getReportees/{employeeID}", method = GET)
  public ResponseEntity<?> getReportees(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID)
  {
    try
    {
      return ok(employeeService.getReporteesForUser(employeeID));
    }
    catch (MongoException me)
    {
      return badRequest().body("DataBase Connection Error");
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to add a new objective to a user
   * 
   * @param employeeID A value >0
   * @param title a string that doesn't exceed 150 characters
   * @param description a string that doesn't exceed 1000 characters
   * @param completedBy a valid month and year in the following format: yyyy-MM
   * @param progress a value between -1 and 2 -1 => Not Relevant to my career anymore 0 => Awaiting 1 => In Flight 2 =>
   *          Done
   * @return a message explaining if the objective has been inserted or if there was an error while completing the task
   */
  @RequestMapping(value = "/addObjective/{employeeID}", method = POST)
  public ResponseEntity<?> addObjectiveToAUser(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "title") String title, @RequestParam(value = "description") String description,
      @RequestParam(value = "completedBy") String completedBy, @RequestParam(value = "proposedBy") String proposedBy)
  {
    try
    {
      Objective_OLD obj = new Objective_OLD(0, 0, title, description, completedBy);
      obj.setProposedBy(proposedBy);
      boolean inserted = employeeService.insertNewObjective(employeeID, obj);
      if (inserted)
      {
        return ok("Objective inserted correctly");
      }
      else
      {
        return badRequest().body("Error while adding the objective");
      }
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to edit a new version of an objective currently stored within the system
   * 
   * @param employeeID the employee ID
   * @param objectiveID the ID of the objective (>0)
   * @param title the title of the objective (< 150)
   * @param description the description of the objective (< 3000)
   * @param completedBy (string with format: yyyy-MM)
   * @param progress a value between -1 and 2 -1 => Deleted 0 => Awaiting 1 => In Flight 2 => Done
   * @return a message explaining if the objective has been updated or if there was an error while completing the task
   */
  @RequestMapping(value = "/editObjective/{employeeID}", method = POST)
  public ResponseEntity<?> addNewVersionObjectiveToAUser(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "objectiveID") @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveID,
      @RequestParam(value = "title") String title, @RequestParam(value = "description") String description,
      @RequestParam(value = "completedBy") String completedBy, @RequestParam(value = "progress") int progress,
      @RequestParam(value = "proposedBy") String proposedBy)
  {
    try
    {
      Objective_OLD obj = new Objective_OLD(objectiveID, progress, 0, title, description, completedBy);
      obj.setProposedBy(proposedBy);
      boolean inserted = employeeService.addNewVersionObjective(employeeID, objectiveID, obj);
      if (inserted)
      {
        return ok("Objective modified correctly");
      }
      else
      {
        return badRequest().body("Error while editing the objective");
      }
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  @RequestMapping(value = "/editObjectiveProgress/{employeeID}", method = POST)
  public ResponseEntity<?> addNewVersionObjectiveToAUser(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "objectiveID") @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveID,
      @RequestParam(value = "progress") int progress)
  {
    try
    {
      boolean inserted = employeeService.updateProgressObjective(employeeID, objectiveID, progress);
      if (inserted)
      {
        return ok("Objective modified correctly");
      }
      else
      {
        return badRequest().body("Error while editing the objective");
      }
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to update the status of a objective. This corresponds to archiving or unarchiving
   * an objective
   * 
   * @param employeeID The user ID
   * @param objectiveID The objective ID to update
   * @param isArchived boolean value (true=archive, false=unarchive)
   * @return a message explaining if the objective has been updated or if there was an error while completing the task
   */
  @RequestMapping(value = "/changeStatusObjective/{employeeID}", method = POST)
  public ResponseEntity<?> updateStatusUserObjective(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "objectiveID") @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveID,
      @RequestParam(value = "isArchived") boolean isArchived)
  {
    try
    {
      // Retrieve the object with the given ID from the DB data
      Objective_OLD obj = employeeService.getSpecificObjectiveForUser(employeeID, objectiveID);
      if (obj.getIsArchived() == isArchived)
      {
        return ok("The status of the objective has not changed");
      }
      // //Create a new object which stores the data from the retrieved element but sets a new timestamp to it
      // Objective newObjUpdated=new Objective(obj);
      // newObjUpdated.setIsArchived(isArchived);

      boolean updatedArchiveStatus = obj.updateArchiveStatus(isArchived);

      // Store the new version to the system
      boolean inserted = employeeService.addNewVersionObjective(employeeID, objectiveID, obj);

      if (inserted)
      {
        if (updatedArchiveStatus)
        {
          return ok("The objective has been archived");
        }
        else
        {
          return ok("The objective has been restored");
        }
      }
      else
      {
        return badRequest().body("Error while editing the objective");
      }
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * POST End point - Adds note to employee
   *
   */
  @RequestMapping(value = "/addNote/{employeeID}", method = POST)
  public ResponseEntity<String> addNote(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam @NotBlank(message = ERROR_NOTE_PROVIDER_NAME_EMPTY) @Size(max = 150, message = ERROR_PROVIDER_NAME_LIMIT) String providerName,
      @RequestParam @NotBlank(message = ERROR_NOTE_DESCRIPTION_EMPTY) @Size(max = 1000, message = ERROR_NOTE_DESCRIPTION_LIMIT) String noteDescription)
  {
    try
    {
      employeeService.addNote(employeeID, new Note(providerName, noteDescription));
      return ok("Note inserted");
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }

  }

  /**
   * POST End point - Add note to reportee.
   * 
   * @return
   *
   */
  @RequestMapping(value = "/addNoteToReportee/{employeeID}", method = POST)
  public ResponseEntity<String> addNoteToReportee(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam @Min(value = 1, message = ERROR_EMPLOYEE_ID) long reporteeEmployeeID,
      @NotBlank(message = ERROR_NOTE_PROVIDER_NAME_EMPTY) @Size(max = 150, message = ERROR_PROVIDER_NAME_LIMIT) String providerName,
      @NotBlank(message = ERROR_NOTE_DESCRIPTION_EMPTY) @Size(max = 1000, message = ERROR_NOTE_DESCRIPTION_LIMIT) String noteDescription)
  {
    try
    {
      employeeService.addNoteToReportee(reporteeEmployeeID, new Note(providerName, noteDescription));
      return ok("Note inserted correctly");
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to insert a new development need in the system
   * 
   * @param employeeID the employee ID (>0)
   * @param title title of the development need (<150)
   * @param description content of the development need (<1000)
   * @param timeToCompleteBy String containing a date with format yyyy-MM or empty ""
   * @return a message explaining if the development need has been added or if there was an error while completing the
   *         task
   */
  @RequestMapping(value = "/addDevelopmentNeed/{employeeID}", method = POST)
  public ResponseEntity<?> addDevelopmentNeedToAUser(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "category") int cat, @RequestParam(value = "title") String title,
      @RequestParam(value = "description") String description,
      @RequestParam(value = "timeToCompleteBy") String timeToCompleteBy)
  {
    try
    {
      DevelopmentNeed_OLD obj = new DevelopmentNeed_OLD(1, 0, cat, title, description, timeToCompleteBy);
      boolean inserted = employeeService.insertNewDevelopmentNeed(employeeID, obj);
      if (inserted)
      {
        return ok("Development need inserted correctly");
      }
      else
      {
        return badRequest().body("Error while adding the Development need");
      }
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to edit a development need previously inserted in the system
   * 
   * @param employeeID employeeID of the employee (>0)
   * @param devNeedID ID of the development need to edit (>0)
   * @param title title of the development need (<150>
   * @param description content of the development need (<1000)
   * @param timeToCompleteBy String containing a date with format yyyy-MM or empty ""
   * @return a message explaining if the development need has been added or if there was an error while completing the
   *         task
   */
  @RequestMapping(value = "/editDevelopmentNeed/{employeeID}", method = POST)
  public ResponseEntity<?> addNewVersionDevelopmentNeedToAUser(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "category") int cat,
      @RequestParam(value = "devNeedID") @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int devNeedID,
      @RequestParam(value = "title") String title, @RequestParam(value = "description") String description,
      @RequestParam(value = "timeToCompleteBy") String timeToCompleteBy, @RequestParam int progress)
  {
    try
    {
      DevelopmentNeed_OLD obj = new DevelopmentNeed_OLD(devNeedID, progress, cat, title, description, timeToCompleteBy);
      boolean inserted = employeeService.addNewVersionDevelopmentNeed(employeeID, devNeedID, obj);
      if (inserted)
      {
        return ok("Development need modified correctly");
      }
      else
      {
        return badRequest().body("Error while editing the Development need");
      }
    }
    catch (MongoException me)
    {
      return badRequest().body("DataBase Connection Error");
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  @RequestMapping(value = "/editDevelopmentNeedProgress/{employeeID}", method = POST)
  public ResponseEntity<?> addNewVersionDevelopmentNeedToAUser(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "devNeedID") @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int devNeedID,
      @RequestParam(value = "progress") int progress)
  {
    try
    {
      boolean inserted = employeeService.updateProgressDevelopmentNeed(employeeID, devNeedID, progress);
      if (inserted)
      {
        return ok("Development need modified correctly");
      }
      else
      {
        return badRequest().body("Error while editing the development need");
      }
    }
    catch (MongoException me)
    {
      return badRequest().body("DataBase Connection Error");
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  @RequestMapping(value = "/toggleDevNeedArchive/{employeeID}", method = POST)
  public ResponseEntity<?> toggleDevNeedArchive(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedID)
  {
    try
    {
      employeeService.toggleDevNeedArchive(employeeID, developmentNeedID);
      return ok("Development Need updated");
    }
    catch (final EmployeeNotFoundException | InvalidAttributeValueException e)
    {
      return badRequest().body(GlobalExceptionHandler.error(e.getMessage()));
    }

  }

  @RequestMapping(value = "/generateFeedbackRequest/{employeeID}", method = POST)
  public ResponseEntity<String> createFeedbackRequest(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam @NotBlank(message = ERROR_EMAIL_RECIPIENTS_EMPTY) String emailsTo,
      @RequestParam @Size(max = 1000, message = ERROR_NOTE_DESCRIPTION_LIMIT) String notes)
  {
    try
    {
      employeeService.processFeedbackRequest(employeeID, emailsTo, notes);
      return ok("Your feedback request has been processed.");
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to insert new competencies in the system
   * 
   * @param employeeID the employee ID (>0)
   * @param title title of the competency (<200)
   * @return a message explaining if the competency has been updated or if there was an error while completing the task
   */
  @RequestMapping(value = "/updateCompetency/{employeeID}", method = POST)
  public ResponseEntity<?> addCompetenciesToAUser(
      @PathVariable("employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "title") @NotBlank(message = ERROR_COMPETENCY_TITLE_BLANK) String title,
      @RequestParam(value = "status") boolean status)
  {
    try
    {
      if (title == null || title.length() < 1 || title.length() > 200)
      {
        return badRequest().body("The given title is invalid");
      }
      int index = Constants.getCompetencyIDGivenTitle(title);
      if (index < 0)
      {
        return badRequest().body("The given title does not match any valid competency");
      }
      Competency_OLD obj = new Competency_OLD(index, status);
      boolean inserted = employeeService.addNewVersionCompetency(employeeID, obj, title);
      if (inserted)
      {
        return ok("Competency updated correctly");
      }
      else
      {
        return badRequest().body("Error while updating the Competency");
      }
    }
    catch (MongoException me)
    {
      return badRequest().body("DataBase Connection Error");
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  @RequestMapping(value = "/authenticateUserProfile", method = GET)
  public ResponseEntity<?> authenticateUserProfile(@RequestParam(value = "userName_Email") String userName)
  {
    try
    {
      if (userName != null && !userName.equals("") && userName.length() < 300)
      {
        return ok(employeeService.authenticateUserProfile(userName));
      }
      else
      {
        return badRequest().body("The username given is invalid");
      }
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * 
   * This method allows the front-end to propose a new objective for a list of users
   * 
   * @param employeeID A value >0
   * @param title a string that doesn't exceed 150 characters
   * @param description a string that doesn't exceed 1000 characters
   * @param completedBy a valid month and year in the following format: yyyy-MM
   * @param emails, a string of email addresses -1 => Not Relevant to my career anymore 0 => Awaiting 1 => In Flight 2
   *          => Done
   * @return a message explaining if the objective has been inserted or if there was an error while completing the task
   * @throws EmployeeNotFoundException
   */
  @RequestMapping(value = "/addProposedObjective/{employeeID}", method = POST)
  public ResponseEntity<?> addProposedObjectiveToAUser(
      @PathVariable(value = "employeeID") @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam(value = "title") String title, @RequestParam(value = "description") String description,
      @RequestParam(value = "completedBy") String completedBy, @RequestParam(value = "emails") String emails)
      throws EmployeeNotFoundException
  {
    String result = "Objective Proposed for: ";
    String errorResult = "Error: ";
    boolean errorInserting = false;
    boolean insertAccepted = false;
    Set<String> emailSet = new HashSet<>();
    try
    {

      // Check that input variables are not empty
      Validate.areStringsEmptyorNull(title, description, completedBy);

      // Get email addresses and check they are not empty and limit to 20
      String[] emailAddresses = emails.split(",");
      // if(emailAddresses.length >19){
      // throw new InvalidAttributeValueException("There is a maximum of 20 allowed emails in one request.");
      // }
      for (String email : emailAddresses)
      {
        if (email.length() < 1)
        {
          throw new InvalidAttributeValueException("One or more of the emails are invalid");
        }
        emailSet.add(email.trim());
      }

      // check date is not in the past
      YearMonth temp = YearMonth.parse(completedBy, Constants.YEAR_MONTH_FORMAT);
      if (temp.isBefore(YearMonth.now(UK_TIMEZONE)))
      {
        throw new InvalidAttributeValueException("Date can not be in the past");
      }

      // get user and loop through emails and add objective
      String proposedBy = employeeService.getFullNameUser(employeeID);

      String subject = String.format("Proposed Objective from %s", proposedBy);

      for (String email : emailSet)
      {
        try
        {
          EmployeeProfile userInQuestion = employeeProfileService.fetchEmployeeProfile(email);
          Objective_OLD obj = new Objective_OLD(0, 0, title, description, completedBy);
          obj.setProposedBy(proposedBy);
          boolean inserted = employeeService.insertNewObjective(userInQuestion.getEmployeeID(), obj);
          if (inserted)
          {
            insertAccepted = true;
            result += userInQuestion.getFullName() + ", ";

            try
            {
              String body = Template.populateTemplate(env.getProperty("templates.objective.proposed"), proposedBy);
              EmailService.sendEmail(email, subject, body);
            }
            catch (Exception e)
            {
              LOGGER.error("Email could not be sent for a proposed objective. Error: {}", e);
            }

          }
          else
          {
            errorInserting = true;
            errorResult += "Could not send to " + userInQuestion.getEmployeeID() + ", ";
          }
        }
        catch (InvalidAttributeValueException | EmployeeNotFoundException | IllegalArgumentException er)
        {
          errorInserting = true;
          errorResult += er.getMessage();
        }
      }

      // If any error pop up, add to result
      if (errorInserting)
      {
        if (!insertAccepted)
        {
          result = "";
        }
        result += errorResult;
      }

      return ok(result);

    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException | IllegalArgumentException e)
    {
      if (!insertAccepted)
      {
        result = "";
      }
      return badRequest().body(result + e.getMessage() + ", ");
    }
  }

  //////////////////// START NEW OBJECTIVES

  @RequestMapping(value = "/getObjectivesNEW/{employeeId}", method = GET)
  public ResponseEntity<?> getObjectivesNEW(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      return ok(employeeService.getObjectivesNEW(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/addObjectiveNEW/{employeeId}", method = POST)
  public ResponseEntity<?> addObjectiveNEW(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 150, message = ERROR_TITLE_LIMIT) String title,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 2000, message = ERROR_TITLE_LIMIT) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate)
  {
    try
    {
      employeeService.addObjectiveNEW(employeeId,
          new Objective(title, description, isYearMonthInPast(YearMonth.parse(dueDate))));
      return ok("Objective inserted correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
    catch (IOException e)
    {
      LOGGER.error("Error adding objective {}", e);
      return badRequest().body(error("Sorry there was an error adding your objective. Please try again later."));
    }
  }

  @RequestMapping(value = "/editObjectiveNEW/{employeeId}", method = POST)
  public ResponseEntity<?> editObjectiveNEW(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 150, message = ERROR_TITLE_LIMIT) String title,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 2000, message = ERROR_TITLE_LIMIT) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate)
  {
    try
    {
      employeeService.editObjectiveNEW(employeeId,
          new Objective(objectiveId, title, description, isYearMonthInPast(YearMonth.parse(dueDate))));
      return ok("Objective updated correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/deleteObjectiveNEW/{employeeId}", method = DELETE)
  public ResponseEntity<?> deleteObjectiveNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId)
  {
    try
    {
      employeeService.deleteObjectiveNEW(employeeId, objectiveId);
      return ok("Objective deleted");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/updateObjectiveNEWProgress/{employeeId}", method = POST)
  public ResponseEntity<?> updateObjectiveNEWProgress(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId,
      @RequestParam @Min(value = 0, message = ERROR_OBJECTIVE_ID) @Max(value = 2, message = ERROR_OBJECTIVE_ID) int progress)
  {
    try
    {
      employeeService.updateObjectiveNEWProgress(employeeId, objectiveId,
          Objective.Progress.valueOf(PROGRESS_LIST[progress]));
      return ok("Objective progress updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
    catch (IOException e)
    {
      LOGGER.error("Error adding objective {}", e);
      return badRequest().body(error("Sorry there was an error updating your objective. Please try again later."));
    }
  }

  @RequestMapping(value = "/toggleObjectiveNEWArchive/{employeeId}", method = POST)
  public ResponseEntity<?> toggleObjectiveNEWArchive(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId)
  {
    try
    {
      employeeService.toggleObjectiveNEWArchive(employeeId, objectiveId);
      return ok("Objective updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/proposeObjectiveNEW/{employeeId}", method = POST)
  public ResponseEntity<?> proposeObjectiveNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 150, message = ERROR_TITLE_LIMIT) String title,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 2000, message = ERROR_TITLE_LIMIT) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @NotBlank(message = ERROR_EMAILS_EMPTY) String emails)
  {
    try
    {
      Set<String> emailSet = Utils.stringEmailsToHashSet(emails);
      employeeService.proposeObjectiveNEW(employeeId,
          new Objective(title, description, isYearMonthInPast(YearMonth.parse(dueDate))), emailSet);
      return ok("Objective inserted correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////// END NEW OBJECTIVES

  //////////////////// START NEW DEVELOPMENT NEEDS

  @RequestMapping(value = "/getDevelopmentNeedsNEW/{employeeId}", method = GET)
  public ResponseEntity<?> getDevelopmentNeedsNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      return ok(employeeService.getDevelopmentNeedsNEW(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/addDevelopmentNeedNEW/{employeeId}", method = POST)
  public ResponseEntity<?> addDevelopmentNeedsNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 150, message = ERROR_TITLE_LIMIT) String title,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 2000, message = ERROR_TITLE_LIMIT) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @Min(value = 0, message = ERROR_CATEGORY) @Max(value = 4, message = ERROR_CATEGORY) int category)
  {
    try
    {
      employeeService.addDevelopmentNeedNEW(employeeId, new DevelopmentNeed(title, description,
          isYearMonthInPast(YearMonth.parse(dueDate)), DevelopmentNeed.Category.valueOf(CATEGORY_LIST[category])));
      return ok("Development Need inserted correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/editDevelopmentNeedNEW/{employeeId}", method = POST)
  public ResponseEntity<?> editDevelopmentNeedNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 150, message = ERROR_TITLE_LIMIT) String title,
      @RequestParam @NotBlank(message = ERROR_TITLE_EMPTY) @Size(max = 2000, message = ERROR_TITLE_LIMIT) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @Min(value = 0, message = ERROR_CATEGORY) @Max(value = 4, message = ERROR_CATEGORY) int category)
  {
    try
    {
      employeeService.editDevelopmentNeedNEW(employeeId, new DevelopmentNeed(developmentNeedId, title, description,
          isYearMonthInPast(YearMonth.parse(dueDate)), DevelopmentNeed.Category.valueOf(CATEGORY_LIST[category])));
      return ok("Development Need updated correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/deleteDevelopmentNeedNEW/{employeeId}", method = DELETE)
  public ResponseEntity<?> deleteDevelopmentNeedNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId)
  {
    try
    {
      employeeService.deleteDevelopmentNeedNEW(employeeId, developmentNeedId);
      return ok("Development Need deleted");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/updateDevelopmentNeedNEWProgress/{employeeId}", method = POST)
  public ResponseEntity<?> updateDevelopmentNeedNEWProgress(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId,
      @RequestParam @Min(value = 0, message = ERROR_DEVELOPMENT_NEED_ID) @Max(value = 2, message = ERROR_DEVELOPMENT_NEED_ID) int progress)
  {
    try
    {
      employeeService.updateDevelopmentNeedNEWProgress(employeeId, developmentNeedId,
          Objective.Progress.valueOf(PROGRESS_LIST[progress]));
      return ok("Development Need progress updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/toggleDevelopmentNeedNEWArchive/{employeeId}", method = POST)
  public ResponseEntity<?> toggleDevelopmentNeedNEWArchive(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId)
  {
    try
    {
      employeeService.toggleDevelopmentNeedNEWArchive(employeeId, developmentNeedId);
      return ok("Development Need updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////// END NEW DEVELOPMENT NEEDS

  //////////////////// START NEW COMPETENCIES

  @RequestMapping(value = "/getCompetenciesNEW/{employeeId}", method = GET)
  public ResponseEntity<?> getCompetenciesNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      return ok(employeeService.getCompetenciesNEW(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/toggleCompetencyNEW/{employeeId}", method = POST)
  public ResponseEntity<?> toggleCompetencyNEW(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Pattern(regexp = REGEX_COMPETENCY_TITLE, message = ERROR_COMPETENCY_TITLE) String competencyTitle)
  {
    try
    {
      employeeService.toggleCompetencyNEW(employeeId, CompetencyTitle.getCompetencyTitleFromString(competencyTitle));
      return ok("Competency updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////// END NEW COMPETENCIES

}
