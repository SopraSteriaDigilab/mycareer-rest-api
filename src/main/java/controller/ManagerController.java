package controller;

import static application.GlobalExceptionHandler.error;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static utils.Validate.notPastOrThrow;

import java.time.YearMonth;
import java.util.Set;

import javax.management.InvalidAttributeValueException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoException;

import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Rating;
import services.EmployeeNotFoundException;
import services.ManagerService;
import utils.Utils;

/**
 * This class contains all the available roots of the web service
 */
@CrossOrigin
@RestController
@PropertySource("${ENVIRONMENT}.properties")
@Validated
@RequestMapping("/manager")
public class ManagerController
{
  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";
  private static final String ERROR_EMPTY_NOTE_PROVIDER_NAME = "Provider name can not be empty.";
  private static final String ERROR_LIMIT_PROVIDER_NAME = "Max Provider Name length is 150 characters.";
  private static final String ERROR_EMPTY_NOTE_DESCRIPTION = "Note description can not be empty.";
  private static final String ERROR_LIMIT_NOTE_DESCRIPTION = "Max Description length is 1,000 characters.";
  private static final String ERROR_LIMIT_TITLE = "Max Title length is 150 characters";
  private static final String ERROR_EMPTY_TITLE = "Title can not be empty";
  private static final String ERROR_DATE_FORMAT = "The date format is incorrect";
  private static final String ERROR_EMAILS_EMPTY = "Emails field can not be empty";
  private static final String REGEX_YEAR_MONTH = "^\\d{4}[-](0[1-9]|1[012])$";
  private static final String ERROR_LIMIT_EVALUATION = "Max Evaluation length is 10,000 characters";
  private static final String ERROR_SCORE = "Score must be a number from 0 to 5.";
  private static final String ERROR_EMPTY_DL = "Distribution list cannot be empty";
  private static final String ERROR_LIMIT_DL = "Distribution list cannot be more than 100 characters";
  
  @Autowired
  private ManagerService managerService;

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
      return ok(managerService.getReporteesForUser(employeeID));
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
   * POST End point - Add note to reportee.
   * 
   * @return
   *
   */
  @RequestMapping(value = "/addNoteToReportee/{employeeID}", method = POST)
  public ResponseEntity<String> addNoteToReportee(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID,
      @RequestParam @Min(value = 1, message = ERROR_EMPLOYEE_ID) long reporteeEmployeeID,
      @NotBlank(message = ERROR_EMPTY_NOTE_PROVIDER_NAME) @Size(max = 150, message = ERROR_LIMIT_PROVIDER_NAME) String providerName,
      @NotBlank(message = ERROR_EMPTY_NOTE_DESCRIPTION) @Size(max = 1_000, message = ERROR_LIMIT_NOTE_DESCRIPTION) String noteDescription)
  {
    try
    {
      managerService.addNoteToReportee(reporteeEmployeeID, new Note(providerName, noteDescription));
      return ok("Note inserted correctly");
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  @RequestMapping(value = "/proposeObjective/{employeeId}", method = POST)
  public ResponseEntity<?> proposeObjective(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 2_000, message = ERROR_LIMIT_TITLE) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @NotBlank(message = ERROR_EMAILS_EMPTY) String emails)
  {
    try
    {
      Set<String> emailSet = Utils.stringEmailsToHashSet(emails);
      managerService.proposeObjective(employeeId,
          new Objective(title, description, notPastOrThrow(YearMonth.parse(dueDate))), emailSet);
      return ok("Objective inserted correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }
  
  @RequestMapping(value = "/proposeObjective/{employeeId}", method = POST)
  public ResponseEntity<?> proposeObjectiveToDistributionList(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 2_000, message = ERROR_LIMIT_TITLE) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @NotBlank(message = ERROR_EMPTY_DL) @Size(max = 100, message = ERROR_LIMIT_DL) String distributionList)
  {
    try
    {
      managerService.proposeObjective(employeeId,
          new Objective(title, description, notPastOrThrow(YearMonth.parse(dueDate))), distributionList);
      return ok("Objective inserted correctly");
    }
    catch (InvalidAttributeValueException | EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/addManagerEvaluation/{employeeId}", method = POST)
  public ResponseEntity<?> addManagerEvaluation(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_EMPLOYEE_ID) long reporteeId,
      @RequestParam @Size(max = 10_000, message = ERROR_LIMIT_EVALUATION) String managerEvaluation,
      @RequestParam @Min(value = 0, message = ERROR_SCORE) @Max(value = 5, message = ERROR_SCORE) int score)
  {
    try
    {
      // TODO will have to add validation so this can only be edited in the right time of year.
      int year = Rating.getRatingYear();
      managerService.addManagerEvaluation(reporteeId, year, managerEvaluation, score);
      return ok("Evaluation Added");
    }
    catch (EmployeeNotFoundException | InvalidAttributeValueException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  @RequestMapping(value = "/submitManagerEvaluation/{employeeId}", method = POST)
  public ResponseEntity<?> submitManagerEvaluation(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_EMPLOYEE_ID) long reporteeId)
  {
    try
    {
      // TODO will have to add validation so this can only be edited in the right time of year.
      int year = Rating.getRatingYear();
      managerService.submitManagerEvaluation(reporteeId, year);
      return ok("Evaluation Submitted");
    }
    catch (EmployeeNotFoundException | InvalidAttributeValueException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }
  
  @RequestMapping(value = "/getActivityFeed/{employeeId}", method = GET)
  public ResponseEntity<?> getActivityFeed(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    return ok(managerService.getActivityFeed(employeeId));
  }
}
