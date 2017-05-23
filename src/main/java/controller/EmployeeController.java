package controller;

import static application.GlobalExceptionHandler.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static utils.Validate.*;
import static utils.Utils.*;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Set;

import javax.management.InvalidAttributeValueException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dataStructure.Competency.CompetencyTitle;
import dataStructure.DevelopmentNeed;
import dataStructure.DocumentConversionException;
import dataStructure.EmployeeProfile;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Rating;
import services.DuplicateEmailAddressException;
import services.EmployeeNotFoundException;
import services.EmployeeProfileService;
import services.EmployeeService;

/**
 * REST controller for end points providing user access and control.
 * 
 * @see EmployeeService
 * @see EmployeeProfileService
 */
@CrossOrigin
@RestController
@PropertySource("${ENVIRONMENT}.properties")
@Validated
public class EmployeeController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";
  private static final String ERROR_OBJECTIVE_ID = "The given Objective ID is invalid";
  private static final String ERROR_DEVELOPMENT_NEED_ID = "The given Development Need ID is invalid";
  private static final String ERROR_NOTE_ID = "The given Note ID is invalid";
  private static final String ERROR_FEEDBACK_ID = "The given Feedback ID is invalid";
  private static final String ERROR_LIMIT_TITLE = "Max Title length is 150 characters";
  private static final String ERROR_EMPTY_TITLE = "Title can not be empty";
  private static final String ERROR_LIMIT_PROVIDER_NAME = "Max Provider Name length is 150 characters.";
  private static final String ERROR_EMPTY_EMAIL_RECIPIENTS = "The emailsTo field can not be empty";
  private static final String ERROR_DATE_FORMAT = "The date format is incorrect";
  private static final String ERROR_EMPTY_NOTE_PROVIDER_NAME = "Provider name can not be empty.";
  private static final String ERROR_EMPTY_NOTE_DESCRIPTION = "Note description can not be empty.";
  private static final String ERROR_LIMIT_NOTE_DESCRIPTION = "Max Description length is 1000 characters.";
  private static final String ERROR_EMPTY_OBJECTIVE_DESCRIPTION = "Objective description can not be empty.";
  private static final String ERROR_LIMIT_OBJECTIVE_DESCRIPTION = "Max Description length is 2,000 characters.";
  private static final String ERROR_LIMIT_COMMENT = "Max Comment length is 1000 characters.";
  private static final String ERROR_EMPTY_FEEDBACK = "The feedback cannot be empty.";
  private static final String ERROR_LIMIT_FEEDBACK = "Max feedback length is 5000";
  private static final String ERROR_CATEGORY = "Category must be from 0 to 4";
  private static final String ERROR_COMPETENCY_TITLE = "Invalid Competency, please enter one of the following: 'Accountability', 'Effective Communication', 'Leadership', 'Service Excellence', 'Business Awareness', 'Future Orientation', 'Innovation and Change', 'Teamwork'";
  private static final String ERROR_EMAILS_EMPTY = "Emails field can not be empty";
  private static final String REGEX_YEAR_MONTH = "^\\d{4}[-](0[1-9]|1[012])$";
  private static final String REGEX_COMPETENCY_TITLE = "^(Accountability)|(Effective Communication)|(Leadership)|(Service Excellence)|(Business Awareness)|(Future Orientation)|(Innovation and Change)|(Teamwork)$";
  private static final String ERROR_LIMIT_DESCRIPTION = "Max Description is 2000 characters";
  private static final String ERROR_EMPTY_DESCRIPTION = "Description cannot be empty";
  private static final String ERROR_PROGRESS = "Progress must be a value from 0-2.";
  private static final String ERROR_LIMIT_EVALUATION = "Max Evaluation length is 10,000 characters";
  private static final String ERROR_LIMIT_USERNAME_EMAIL = "The username or email address provided is invalid";

  private static final String[] CATEGORY_LIST = { "JOB_TRAINING", "CLASSROOM_TRAINING", "ONLINE", "SELF_STUDY", "OTHER" };
  private static final String[] PROGRESS_LIST = { "PROPOSED", "IN_PROGRESS", "COMPLETE" };

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeProfileService employeeProfileService;

  //////////////////////////////////////////////////////////////////////////////
  //////////////////// INTRO/LOGON END POINT METHODS FOLLOW ////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request for a simple welcome message.
   * 
   * @return a {@code ResponseEntity<String>} containing a simple welcome message.
   */
  @RequestMapping(value = "/", method = GET)
  public ResponseEntity<String> welcomePage()
  {
    return ok("Welcome to the MyCareer Project");
  }

  /**
   * HTTP GET request for portal users.
   *
   * @param request
   * @param response
   */
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

  /**
   * HTTP GET request to fetch the employee profile of the user making the request.
   *
   * @param request
   * @return {@code ResponseEntity<EmployeeProfile>} with OK response and body containing the employee profile of the
   *         user making the request. Bad Request response with error message if the identity of the user could not be
   *         determined or if the employee could not found.
   */
  @RequestMapping(value = "/logMeIn", method = GET)
  public ResponseEntity<?> index(HttpServletRequest request)
  {
    String username = request.getRemoteUser();
    ResponseEntity<?> response = authenticateUserProfile(username.toLowerCase());
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
   * HTTP GET request to fetch an employee profile from the database based on the given employee username or email
   * address.
   *
   * @param userNameEmail An employee's username or email address. Must be non-null and contain between 1 and 300
   *          characters.
   * @return {@code ResponseEntity<EmployeeProfile>} with OK response and body containing the employee profile of the
   *         employee with the given username or email address. Bad Request response with error message if employee not
   *         found.
   */
  @RequestMapping(value = "/authenticateUserProfile", method = GET)
  public ResponseEntity<?> authenticateUserProfile(
      @RequestParam(value = "userName_Email") @NotBlank @Size(max = 300, message = ERROR_LIMIT_USERNAME_EMAIL) String userNameEmail)
  {
    try
    {
      return ok(employeeProfileService.fetchEmployeeProfile(userNameEmail.toLowerCase()));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  //////////////////// OBJECTIVES END POINT METHODS FOLLOW /////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request to fetch the current objectives of the employee corresponding to the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose objectives are to be returned. Must be an integer greater
   *          than 0.
   * @return {@code ResponseEntity<List<Objective>> with OK response and body containing the current objectives of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getObjectives/{employeeId}", method = GET)
  public ResponseEntity<?> getObjectives(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      return ok(employeeService.getObjectives(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to add an objective to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee to whom an objective is to be added. Must be an integer greater
   *          than 0.
   * @param title POST request parameter - the title of the objective to be added. Must be non-null and contain between
   *          1 and 150 characters.
   * @param description POST request parameter - the description of the objective to be added. Must be non-null and
   *          contain between 1 and 2,000 characters.
   * @param dueDate POST request parameter - the date by which the objective should be achieved. Must be of the form
   *          yyyy-MM.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the
   *         objective was successfully added. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/addObjective/{employeeId}", method = POST)
  public ResponseEntity<?> addObjective(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_DESCRIPTION) @Size(max = 2_000, message = ERROR_LIMIT_DESCRIPTION) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate)
  {
    try
    {
      employeeService.addObjective(employeeId,
          new Objective(title, description, presentOrFutureYearMonthToLocalDate(YearMonth.parse(dueDate))));
      return ok("Objective inserted correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
    catch (DocumentConversionException e)
    {
      LOGGER.error("Error adding objective {}", e);
      return badRequest().body(error("Sorry there was an error adding your objective. Please try again later."));
    }
  }

  /**
   * HTTP POST request to edit an existing objective of the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose objective is to be edited. Must be an integer greater than
   *          0.
   * @param objectiveId POST request parameter - the ID number of the objective to be edited. Must be an integer greater
   *          than 0.
   * @param title POST request parameter - the new title of the objective to be edited. Must be non-null and contain
   *          between 1 and 150 characters.
   * @param description POST request parameter - the new description of the objective to be edited. Must be non-null and
   *          contain between 1 and 2,000 characters.
   * @param dueDate POST request parameter - the new date by which the objective to be edited should be achieved. Must
   *          be of the form yyyy-MM.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and objective were
   *         found and the objective was successfully edited. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/editObjective/{employeeId}", method = POST)
  public ResponseEntity<?> editObjective(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_OBJECTIVE_DESCRIPTION) @Size(max = 2_000, message = ERROR_LIMIT_OBJECTIVE_DESCRIPTION) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate)
  {
    try
    {
      employeeService.editObjective(employeeId, new Objective(objectiveId, title, description,
          presentOrFutureYearMonthToLocalDate(YearMonth.parse(dueDate))));
      return ok("Objective updated correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to permanently delete an objective from the employee with the given employee ID. Also adds an
   * auto-generated note to the employee if the objective was successfully deleted.
   *
   * @param employeeId The employee ID of the employee whose objective is to be deleted. Must be an integer greater than
   *          0.
   * @param objectiveId POST request parameter - the ID number of the objective to be deleted. Must be an integer
   *          greater than 0.
   * @param comment POST request parameter - a comment to include in the auto-generated note. Must contain between 0 and
   *          1,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and objective were
   *         found, the objective was successfully deleted, and the auto-generated note was successfully added. Bad
   *         Request response with error message otherwise.
   */
  @RequestMapping(value = "/deleteObjective/{employeeId}", method = POST)
  public ResponseEntity<?> deleteObjective(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId,
      @RequestParam @Size(max = 1_000, message = ERROR_LIMIT_COMMENT) String comment)
  {
    try
    {
      employeeService.deleteObjective(employeeId, objectiveId, comment);
      return ok("Objective deleted");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to update the progress of an existing objective of the employee with the given employee ID. Also
   * adds an auto-generated note to the employee if the objective progress was successfully updated.
   *
   * @param employeeId The employee ID of the employee whose objective is to be updated. Must be an integer greater than
   *          0.
   * @param objectiveId POST request parameter - the ID number of the objective whose progress is to be updated. Must be
   *          an integer greater than 0.
   * @param progress POST request parameter - the progress level that the objective is to be updated to. Progress levels
   *          are 0 - Proposed, 1 - In-Progress, 2 - Complete. Must be an integer between 0 and 2.
   * @param comment POST request parameter - a comment to include in the auto-generated note. Must contain between 0 and
   *          1,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and objective were
   *         found, the objective progress was successfully updated, and the auto-generated note was successfully added.
   *         Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/updateObjectiveProgress/{employeeId}", method = POST)
  public ResponseEntity<?> updateObjectiveProgress(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId,
      @RequestParam @Min(value = 0, message = ERROR_PROGRESS) @Max(value = 2, message = ERROR_PROGRESS) int progress,
      @RequestParam @Size(max = 1_000, message = ERROR_LIMIT_COMMENT) String comment)
  {
    try
    {
      employeeService.updateObjectiveProgress(employeeId, objectiveId,
          Objective.Progress.valueOf(PROGRESS_LIST[progress]), comment);
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

  /**
   * HTTP POST request to archive an active objective or restore/re-activate an archived objective from the employee
   * with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose objective is to be archived or restored. Must be an integer
   *          greater than 0.
   * @param objectiveId POST request parameter - the ID number of the objective to archive or restore. Must be an
   *          integer greater than 0.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and objective were
   *         found, and the objective was successfully archived or restored. Bad Request response with error message
   *         otherwise.
   */
  @RequestMapping(value = "/toggleObjectiveArchive/{employeeId}", method = POST)
  public ResponseEntity<?> toggleObjectiveArchive(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_OBJECTIVE_ID) int objectiveId)
  {
    try
    {
      employeeService.toggleObjectiveArchive(employeeId, objectiveId);
      return ok("Objective updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  ///////////////// DEVELOPMENT NEEDS END POINT METHODS FOLLOW /////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request to fetch the current development needs of the employee corresponding to the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose development needs are to be returned. Must be an integer
   *          greater than 0.
   * @return {@code ResponseEntity<List<DevelopmentNeed>> with OK response and body containing the current development needs of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getDevelopmentNeeds/{employeeId}", method = GET)
  public ResponseEntity<?> getDevelopmentNeeds(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      return ok(employeeService.getDevelopmentNeeds(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to add a development need to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee to whom a development need is to be added. Must be an integer
   *          greater than 0.
   * @param title POST request parameter - the title of the development need to be added. Must be non-null and contain
   *          between 1 and 150 characters.
   * @param description POST request parameter - the description of the development need to be added. Must be non-null
   *          and contain between 1 and 2,000 characters.
   * @param dueDate POST request parameter - the date by which the development need should be achieved. Must be of the
   *          form yyyy-MM.
   * @param category POST request parameter - the category of the development need to be added. Categories are 0 - Job
   *          Training, 1 - Classroom Training, 2 - Online, 3 - Self Study, 4 - Other. Must be an integer between 0 and
   *          4.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the
   *         development need was successfully added. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/addDevelopmentNeed/{employeeId}", method = POST)
  public ResponseEntity<?> addDevelopmentNeeds(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_DESCRIPTION) @Size(max = 2_000, message = ERROR_LIMIT_DESCRIPTION) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @Min(value = 0, message = ERROR_CATEGORY) @Max(value = 4, message = ERROR_CATEGORY) int category)
  {
    try
    {
      employeeService.addDevelopmentNeed(employeeId,
          new DevelopmentNeed(title, description, presentOrFutureYearMonthToLocalDate(YearMonth.parse(dueDate)),
              DevelopmentNeed.Category.valueOf(CATEGORY_LIST[category])));
      return ok("Development Need inserted correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
    catch (DocumentConversionException e)
    {
      LOGGER.error("Error adding development need {}", e);
      return badRequest().body(error("Sorry there was an error adding your development need. Please try again later."));
    }
  }

  /**
   * HTTP POST request to edit an existing development need of the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose development need is to be edited. Must be an integer
   *          greater than 0.
   * @param developmentNeedId POST request parameter - the ID number of the development need to be edited. Must be an
   *          integer greater than 0.
   * @param title POST request parameter - the new title of the development need to be edited. Must be non-null and
   *          contain between 1 and 150 characters.
   * @param descriptionn POST request parameter - the new description of the development need to be edited. Must be
   *          non-null and contain between 1 and 2,000 characters.
   * @param dueDate POST request parameter - the new date by which the development need should be achieved. Must be of
   *          the form yyyy-MM.
   * @param category POST request parameter - the new category of the development need to be edited. Categories are 0 -
   *          Job Training, 1 - Classroom Training, 2 - Online, 3 - Self Study, 4 - Other. Must be an integer between 0
   *          and 4.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and development need
   *         were found and the development need was successfully edited. Bad Request response with error message
   *         otherwise.
   */
  @RequestMapping(value = "/editDevelopmentNeed/{employeeId}", method = POST)
  public ResponseEntity<?> editDevelopmentNeed(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_DESCRIPTION) @Size(max = 2_000, message = ERROR_LIMIT_DESCRIPTION) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @Min(value = 0, message = ERROR_CATEGORY) @Max(value = 4, message = ERROR_CATEGORY) int category)
  {
    try
    {
      employeeService.editDevelopmentNeed(employeeId,
          new DevelopmentNeed(developmentNeedId, title, description,
              presentOrFutureYearMonthToLocalDate(YearMonth.parse(dueDate)),
              DevelopmentNeed.Category.valueOf(CATEGORY_LIST[category])));
      return ok("Development Need updated correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to permanently delete a development need from the employee with the given employee ID. Also adds
   * an auto-generated note to the employee if the development need was successfully deleted.
   *
   * @param employeeId The employee ID of the employee whose development need is to be deleted. Must be an integer
   *          greater than 0.
   * @param developmentNeedId POST request parameter - the ID number of the development need to be deleted. Must be an
   *          integer greater than 0.
   * @param comment POST request parameter - a comment to include in the auto-generated note. Must contain between 0 and
   *          1,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and development need
   *         were found, the development need was successfully deleted, and the auto-generated note was successfully
   *         added. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/deleteDevelopmentNeed/{employeeId}", method = POST)
  public ResponseEntity<?> deleteDevelopmentNeed(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId,
      @RequestParam @Size(max = 1_000, message = ERROR_LIMIT_COMMENT) String comment)
  {
    try
    {
      employeeService.deleteDevelopmentNeed(employeeId, developmentNeedId, comment);
      return ok("Development Need deleted");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to update the progress of an existing development need of the employee with the given employee
   * ID. Also adds an auto-generated note to the employee if the development need progress was successfully updated.
   *
   * @param employeeId The employee ID of the employee whose development need is to be updated. Must be an integer
   *          greater than 0.
   * @param developmentNeedId POST request parameter - the ID number of the development need whose progress is to be
   *          updated. Must be an integer greater than 0.
   * @param progress POST request parameter - the progress level that the development need is to be updated to. Progress
   *          levels are 0 - Proposed, 1 - In-Progress, 2 - Complete. Must be an integer between 0 and 2.
   * @param comment POST request parameter - a comment to include in the auto-generated note. Must contain between 0 and
   *          1,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and development need
   *         were found, the development need progress was successfully updated, and the auto-generated note was
   *         successfully added. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/updateDevelopmentNeedProgress/{employeeId}", method = POST)
  public ResponseEntity<?> updateDevelopmentNeedProgress(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId,
      @RequestParam @Min(value = 0, message = ERROR_PROGRESS) @Max(value = 2, message = ERROR_PROGRESS) int progress,
      @RequestParam @Size(max = 1_000, message = ERROR_LIMIT_COMMENT) String comment)
  {
    try
    {
      employeeService.updateDevelopmentNeedProgress(employeeId, developmentNeedId,
          Objective.Progress.valueOf(PROGRESS_LIST[progress]), comment);
      return ok("Development Need progress updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to archive an active development need or restore/re-activate an archived development need from
   * the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose development need is to be archived or restored. Must be an
   *          integer greater than 0.
   * @param developmentNeedId POST request parameter - the ID number of the development need to archive or restore. Must
   *          be an integer greater than 0.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee and development need
   *         were found, and the development need was successfully archived or restored. Bad Request response with error
   *         message otherwise.
   */
  @RequestMapping(value = "/toggleDevelopmentNeedArchive/{employeeId}", method = POST)
  public ResponseEntity<?> toggleDevelopmentNeedArchive(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_DEVELOPMENT_NEED_ID) int developmentNeedId)
  {
    try
    {
      employeeService.toggleDevelopmentNeedArchive(employeeId, developmentNeedId);
      return ok("Development Need updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  /////////////////// COMPETENCIES END POINT METHODS FOLLOW ////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request to fetch the competencies of the employee corresponding to the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose competencies are to be returned. Must be an integer greater
   *          than 0.
   * @return {@code ResponseEntity<List<Competency>> with OK response and body containing the competencies of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getCompetencies/{employeeId}", method = GET)
  public ResponseEntity<?> getCompetencies(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      return ok(employeeService.getCompetencies(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to toggle whether or not a competency is being focused on by the employee with the given employee
   * ID.
   *
   * @param employeeId The employee ID of the employee whose competency is to be toggled. Must be an integer greater
   *          than 0.
   * @param competencyTitle POST request parameter - the title of the competency to toggle. Must match one of the
   *          following exactly (case-sensitive): "Accountability", "Effective Communication", "Leadership", "Service
   *          Excellence", "Business Awareness", "Future Orientation", "Innovation and Change", "Teamwork".
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the
   *         competency was successfully toggled from {@code true} to {@code false} or vice versa. Bad Request response
   *         with error message otherwise.
   */
  @RequestMapping(value = "/toggleCompetency/{employeeId}", method = POST)
  public ResponseEntity<?> toggleCompetency(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Pattern(regexp = REGEX_COMPETENCY_TITLE, message = ERROR_COMPETENCY_TITLE) String competencyTitle)
  {
    try
    {
      employeeService.toggleCompetency(employeeId, CompetencyTitle.getCompetencyTitleFromString(competencyTitle));
      return ok("Competency updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  /////////////////////// NOTES END POINT METHODS FOLLOW ///////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request to fetch the current notes of the employee corresponding to the given employee ID.
   * 
   * @param employeeID The employee ID of the employee whose notes are to be returned. Must be an integer greater than
   *          0.
   * @return {@code ResponseEntity<List<Note>> with OK response and body containing the current notes of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
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
   * HTTP POST request to add a note to the employee with the given employee ID.
   *
   * @param employeeID The employee ID of the employee to whom an objective is to be added. Must be an integer greater
   *          than 0.
   * @param providerName POST request parameter - the name of the person or entity who is adding the note. Must be
   *          non-null and contain between 1 and 150 characters.
   * @param noteDescription POST request parameter - the description of the note to be added. Must be non-null and
   *          contain between 1 and 1,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the note
   *         was successfully added. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/addNote/{employeeID}", method = POST)
  public ResponseEntity<String> addNote(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam @NotBlank(message = ERROR_EMPTY_NOTE_PROVIDER_NAME) @Size(max = 150, message = ERROR_LIMIT_PROVIDER_NAME) String providerName,
      @RequestParam @NotBlank(message = ERROR_EMPTY_NOTE_DESCRIPTION) @Size(max = 1_000, message = ERROR_LIMIT_NOTE_DESCRIPTION) String noteDescription)
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

  //////////////////////////////////////////////////////////////////////////////
  ///////////////////// FEEDBACK END POINT METHODS FOLLOW //////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request to fetch the current feedback of the employee corresponding to the given employee ID.
   *
   * @param employeeID The employee ID of the employee whose feedback is to be returned. Must be an integer greater than
   *          0.
   * @return {@code ResponseEntity<List<Feedback>> with OK response and body containing the current feedback of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getFeedback/{employeeID}", method = GET)
  public ResponseEntity<?> getFeedback(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID)
  {
    try
    {
      return ok(employeeService.getFeedback(employeeID));
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * HTTP POST request for the employee with the given employee ID to add feedback to one or more employees with the
   * given {@code emails}.
   *
   * @param employeeId The employee ID of the employee who is providing feedback. Must be an integer greater than 0.
   * @param emails POST request parameter - a comma separated list of email addresses of employees to whom feedback is
   *          to be added. Must be non-null and contain at least 1 character.
   * @param feedback POST request parameter - the feedback being provided. Must be non-null and contain between 1 and
   *          5,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found, all of the
   *         email addresses were matched to employees, and the feedback was successfully added to them. Bad Request
   *         response with error message otherwise.
   */
  @RequestMapping(value = "/addFeedback/{employeeId}", method = POST)
  public ResponseEntity<?> addFeedback(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMAILS_EMPTY) String emails,
      @RequestParam @NotBlank(message = ERROR_EMPTY_FEEDBACK) @Size(max = 5_000, message = ERROR_LIMIT_FEEDBACK) String feedback)
  {
    try
    {
      Set<String> emailSet = stringEmailsToHashSet(emails);
      employeeService.addFeedback(employeeId, emailSet, feedback, false);
      return ok("Feedback added");
    }
    catch (InvalidAttributeValueException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
    catch (Exception e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  ///////////////// FEEDBACK REQUEST END POINT METHODS FOLLOW //////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP POST request for an employee with the given employee ID to request feedback by email.
   *
   * @param employeeID The employee ID of the employee who is requesting feedback. Must be an integer greater than 0.
   * @param emailsTo POST request parameter - a comma separated list of email addresses to which a feedback request
   *          email is to be sent. Must be non-null, contain at least 1 character, and contain between 1 and 19 comma
   *          separated strings.
   * @param notes POST request parameter - personalised notes to be added to the feedback request email. Must contain
   *          between 0 and 1,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and a
   *         feedback request email was successfully sent to each of the comma separated email addresses. Bad Request
   *         response with error message otherwise.
   */
  @RequestMapping(value = "/generateFeedbackRequest/{employeeID}", method = POST)
  public ResponseEntity<String> createFeedbackRequest(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam @NotBlank(message = ERROR_EMPTY_EMAIL_RECIPIENTS) String emailsTo,
      @RequestParam @Size(max = 1_000, message = ERROR_LIMIT_NOTE_DESCRIPTION) String notes)
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

  //////////////////////////////////////////////////////////////////////////////
  /////////////////////// TAGS END POINT METHODS FOLLOW ////////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request to fetch the tags of the employee corresponding to the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose tags are to be returned. Must be an integer greater than 0.
   * @return {@code ResponseEntity<List<Map<String, Map<Integer, String>>>> with OK response and body containing the
   *         tags of the employee with {@code employeeId}. Bad request response with error message if the employee ID
   *         could not be found.
   */
  @RequestMapping(value = "/getTags/{employeeId}", method = GET)
  public ResponseEntity<?> getTags(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      return ok(employeeService.getTags(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to update the feedback tags of the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose feedback tags are to be updated. Must be an integer greater
   *          than 0.
   * @param feedbackId POST request parameter - the ID number of the feedback whose tags are to be updated. Must be an
   *          integer greater than 0.
   * @param objectiveIds POST request parameter - a {@code Set} of ID numbers of the objectives which are to be tagged
   *          in the feedback whose tags are to be updated.
   * @param developmentNeedIds POST request parameter - a {@code Set} of ID numbers of the development needs which are
   *          to be tagged in the feedback whose tags are to be updated.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee, feedback ID,
   *         objectives IDs and development need IDs were all found and the feedback tags were successfully updated. Bad
   *         Request response with error message otherwise.
   */
  @RequestMapping(value = "/updateFeedbackTags/{employeeId}", method = POST)
  public ResponseEntity<?> updateFeedbackTags(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_FEEDBACK_ID) int feedbackId,
      @RequestParam Set<Integer> objectiveIds, @RequestParam Set<Integer> developmentNeedIds)
  {
    try
    {
      employeeService.updateFeedbackTags(employeeId, feedbackId, objectiveIds, developmentNeedIds);
      return ok("Tags Updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to update the notes tags of the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose notes tags are to be updated. Must be an integer greater
   *          than 0.
   * @param noteId POST request parameter - the ID number of the note whose tags are to be updated. Must be an integer
   *          greater than 0.
   * @param objectiveIds POST request parameter - a {@code Set} of ID numbers of the objectives which are to be tagged
   *          in the note whose tags are to be updated.
   * @param developmentNeedIds POST request parameter - a {@code Set} of ID numbers of the development needs which are
   *          to be tagged in the note whose tags are to be updated.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee, note ID, objectives
   *         IDs and development need IDs were all found and the note tags were successfully updated. Bad Request
   *         response with error message otherwise.
   */
  @RequestMapping(value = "/updateNotesTags/{employeeId}", method = POST)
  public ResponseEntity<?> updateNotesTags(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 0, message = ERROR_NOTE_ID) int noteId, @RequestParam Set<Integer> objectiveIds,
      @RequestParam Set<Integer> developmentNeedIds)
  {
    try
    {
      employeeService.updateNotesTags(employeeId, noteId, objectiveIds, developmentNeedIds);
      return ok("Tags Updated");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  ////////////////////// RATINGS END POINT METHODS FOLLOW //////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP GET request to get the current rating of the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose current rating is to be returned. Must be an integer
   *          greater than 0.
   * @return {@code ResponseEntity<Rating> with OK response and body containing the current rating of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getCurrentRating/{employeeId}", method = GET)
  public ResponseEntity<?> getCurrentRating(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      int year = Rating.getRatingYear();
      return ok(employeeService.getRating(employeeId, year));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to save a self-evaluation to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee to whom a self-evaluation is to be saved. Must be an integer
   *          greater than 0.
   * @param selfEvaluation POST request parameter - the self-evaluation of the employee with the {@code employeeId}.
   *          Must contain between 0 and 10,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the
   *         self-evaluation was successfully added. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/addSelfEvaluation/{employeeId}", method = POST)
  public ResponseEntity<?> addSelfEvaluation(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Size(max = 10_000, message = ERROR_LIMIT_EVALUATION) String selfEvaluation)
  {
    // TODO Uncomment this code once UAT is complete
//    if (!Rating.isRatingPeriod())
//    {
//      return badRequest().body("Self-evaluations can only be added during the ratings submission window.");
//    }

    try
    {
      int year = Rating.getRatingYear();
      employeeService.addSelfEvaluation(employeeId, year, selfEvaluation);
      return ok("Evaluation Added");
    }
    catch (EmployeeNotFoundException | InvalidAttributeValueException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to submit a previously saved self-evaluation to the employee with the given employee ID. Also
   * sends an email to the employee's line manager to inform them of the submission.
   *
   * @param employeeId The employee ID of the employee whose self-evaluation is to be submitted. Must be an integer
   *          greater than 0.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the
   *         self-evaluation was successfully submitted. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/submitSelfEvaluation/{employeeId}", method = POST)
  public ResponseEntity<?> submitSelfEvaluation(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    // TODO Uncomment this code once UAT is complete
//    if (!Rating.isRatingPeriod())
//    {
//      return badRequest().body("Self-evaluations can only be submitted during the ratings submission window.");
//    }

    try
    {
      int year = Rating.getRatingYear();
      employeeService.submitSelfEvaluation(employeeId, year);
      return ok("Evaluation Submitted");
    }
    catch (EmployeeNotFoundException | InvalidAttributeValueException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
    catch (IOException e)
    {
      LOGGER.error(e.getMessage());
      return badRequest().body("Sorry, there was an issue with your request. Please try again later.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  //////////////// JOINT VENTURE EMAIL END POINT METHODS FOLLOW ////////////////
  //////////////////////////////////////////////////////////////////////////////

  /**
   * HTTP POST request to add an additional, user email address of the employee with the given employee ID. If the
   * employee already has a user email address, it will be overwritten.
   *
   * @param employeeId The employee ID of the employee whose self-evaluation is to be submitted. Must be an integer
   *          greater than 0.
   * @param emailAddress The email address to be added to the employee with the given employee ID. Must be a well-formed
   *          email address.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the user
   *         email address was successfully added or updated. Bad Request response with error message otherwise.
   * @see Email
   */
  @RequestMapping(value = "/editUserEmailAddress/{employeeId}", method = POST)
  public ResponseEntity<?> editUserEmailAddress(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Email String emailAddress)
  {
    String retVal = null;
    boolean updated = false;

    try
    {
      updated = employeeProfileService.editUserEmailAddress(employeeId, emailAddress);
      retVal = updated ? "Email address updated" : "Email address not updated";
    }
    catch (final DuplicateEmailAddressException e)
    {
      return badRequest().body(error(e.getMessage()));
    }

    return ok(retVal);
  }
}
