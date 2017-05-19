package controller;

import static application.GlobalExceptionHandler.error;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static utils.Validate.presentOrFutureYearMonthToLocalDate;

import java.io.IOException;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.management.InvalidAttributeValueException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

import dataStructure.DocumentConversionException;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Rating;
import services.EmployeeNotFoundException;
import services.ManagerService;
import services.ews.DistributionList;
import services.ews.DistributionListException;
import services.ews.DistributionListService;
import utils.Utils;

/**
 * REST controller for end points providing manager access and control.
 * 
 * @see ManagerService
 * @see DistributionListService
 */
@CrossOrigin
@RestController
@PropertySource("${ENVIRONMENT}.properties")
@Validated
@RequestMapping("/manager")
public class ManagerController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ManagerController.class);

  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";
  private static final String ERROR_EMPTY_NOTE_PROVIDER_NAME = "Provider name can not be empty.";
  private static final String ERROR_LIMIT_PROVIDER_NAME = "Max Provider Name length is 150 characters.";
  private static final String ERROR_EMPTY_NOTE_DESCRIPTION = "Note description can not be empty.";
  private static final String ERROR_LIMIT_NOTE_DESCRIPTION = "Max Description length is 1,000 characters.";
  private static final String ERROR_EMPTY_OBJECTIVE_DESCRIPTION = "Objective description can not be empty.";
  private static final String ERROR_LIMIT_OBJECTIVE_DESCRIPTION = "Max Description length is 2,000 characters.";
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

  @Autowired
  private DistributionListService distributionListService;

  /**
   * HTTP GET request to fetch the profiles of the reportees of the employee with the given employee ID.
   * 
   * @param employeeID The employee ID of the employee whose reportees' profiles are to be returned. Must be an integer
   *          greater than 0.
   * @return {@code ResponseEntity<List<EmployeeProfile>> with OK response and body containing the profiles of the reportees of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getReportees/{employeeID}", method = GET)
  public ResponseEntity<?> getReportees(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID)
  {
    try
    {
      return ok(managerService.getReporteesForUser(employeeID));
    }
    catch (Exception e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * HTTP POST request to add a note to a reportee of the employee with the given employee ID.
   *
   * @param employeeID The employee ID of the employee who is adding a note to a reportee. Must be an integer greater
   *          than 0.
   * @param reporteeEmployeeID POST request parameter - the employee ID of the employee to whom a note is to be added.
   *          Must be an integer greater than 0.
   * @param providerName POST request parameter - the name of the person or entity who is adding the note. Must be
   *          non-null and contain between 1 and 150 characters.
   * @param noteDescription POST request parameter - the description of the note to be added. Must be non-null and
   *          contain between 1 and 1,000 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the reportee was found and the note
   *         was successfully added. Bad Request response with error message otherwise.
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

  /**
   * HTTP POST request to add an objective to one or more employees. Also sends an email to each of the employees to
   * whom an objective was added.
   *
   * @param employeeId The employee ID of the employee who is proposing an objective. Must be an integer greater than 0.
   * @param title POST request parameter - the title of the objective to be added. Must be non-null and contain between
   *          1 and 150 characters.
   * @param description POST request parameter - the description of the objective to be added. Must be non-null and
   *          contain between 1 and 2,000 characters.
   * @param dueDate POST request parameter - the date by which the objective should be achieved. Must be of the form
   *          yyyy-MM.
   * @param emails POST request parameter - a comma separated list of email addresses of employees to whom an objective
   *          is to be added. Must be non-null and contain at least 1 character.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee proposing the objective
   *         was found, all of the email addresses were matched to employees, and the objective was successfully added
   *         to them. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/proposeObjective/{employeeId}", method = POST)
  public ResponseEntity<?> proposeObjective(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_OBJECTIVE_DESCRIPTION) @Size(max = 2_000, message = ERROR_LIMIT_OBJECTIVE_DESCRIPTION) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @NotBlank(message = ERROR_EMAILS_EMPTY) String emails)
  {
    try
    {
      final Set<String> emailSet = Utils.stringEmailsToHashSet(emails);
      final Set<String> invalidEmailAddresses = new HashSet<>();
      final DistributionList customDistributionList = distributionListService.customDistributionList(emailSet,
          invalidEmailAddresses);

      if (invalidEmailAddresses.isEmpty())
      {
        managerService.proposeObjective(employeeId,
            new Objective(title, description, presentOrFutureYearMonthToLocalDate(YearMonth.parse(dueDate))),
            customDistributionList);

        return ok("Objective inserted correctly");
      }
      else if (emailSet.isEmpty())
      {
        throw new IllegalArgumentException(
            "Employees not found for the following Email Addresses: " + invalidEmailAddresses.toString());
      }
      else
      {
        throw new IllegalArgumentException("Objective proposed for: " + emailSet.toString()
            + ". Employees not found for the following Email Addresses: " + invalidEmailAddresses.toString());
      }
    }
    catch (IllegalArgumentException | EmployeeNotFoundException | DocumentConversionException
        | InvalidAttributeValueException | DistributionListException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to check whether a distribution list has been cached. If not, generates a distribution list and
   * adds it to the cache.
   *
   * @param employeeId The employee ID of the employee who is generating the distribution list. Must be an integer
   *          greater than 0.
   * @param distributionListName POST request parameter - the name of the distribution list to add. Must be non-null and
   *          contain between 1 and 100 characters.
   * @return {@code ResponseEntity<DistributionList>} with OK response and details of the members of the distribution
   *         list if it was successfully generated or found in the cache. Bad Request response with error message
   *         otherwise.
   */
  @RequestMapping(value = "/generateDistributionList/{employeeId}", method = POST)
  public ResponseEntity<?> generateDistributionList(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_DL) @Size(max = 100, message = ERROR_LIMIT_DL) String distributionListName)
  {
    final DistributionList distributionList;
    final Callable<DistributionList> sopraCallable = () -> distributionListService
        .sopraDistributionList(distributionListName);
    final Callable<DistributionList> steriaCallable = () -> distributionListService
        .steriaDistributionList(distributionListName);
    final ExecutorService executor = Executors.newFixedThreadPool(2);
    final Future<DistributionList> sopraFuture = executor.submit(sopraCallable);
    final Future<DistributionList> steriaFuture = executor.submit(steriaCallable);
    DistributionList sopraDL = null;
    DistributionList steriaDL = null;

    try
    {
      sopraDL = sopraFuture.get();
    }
    catch (InterruptedException | ExecutionException e)
    {
      LOGGER.info(e.getCause().getMessage());
    }

    try
    {
      steriaDL = steriaFuture.get();
    }
    catch (InterruptedException | ExecutionException e)
    {
      LOGGER.info(e.getCause().getMessage());
    }

    executor.shutdown();
    distributionList = distributionListService.combine(sopraDL, steriaDL);
    distributionListService.cache(distributionListName, distributionList);

    if (distributionList == null)
    {
      return badRequest()
          .body(error("The distribution list could not be generated, it may not exist, or it may be inaccessible: "
              .concat(distributionListName)));
    }

    LOGGER.debug(distributionList.toString());
    LOGGER.debug("List size: " + distributionList.size());

    return ok(distributionList);
  }

  /**
   * HTTP POST request to add an objective to employees who are members of a previously cached distribution list.
   * Distribution lists are generated and cached using {@code generateDistributionList}. Also sends an email to each of
   * the employees to whom an objective was added.
   *
   * @param employeeId The employee ID of the employee who is proposing an objective. Must be an integer greater than 0.
   * @param title POST request parameter - the title of the objective to be added. Must be non-null and contain between
   *          1 and 150 characters.
   * @param description POST request parameter - the description of the objective to be added. Must be non-null and
   *          contain between 1 and 2,000 characters.
   * @param dueDate POST request parameter - the date by which the objective should be achieved. Must be of the form
   *          yyyy-MM.
   * @param distributionListName POST request parameter - the name of the distribution list whose members are to be
   *          proposed an objective. Must be non-null and contain between 1 and 100 characters.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee proposing the objective
   *         was found, the distribution list was found in the cache, and the objective was successfully added to all
   *         members of it. Bad Request response with error message otherwise.
   * @see generateDistributionList
   */
  @RequestMapping(value = "/proposeObjectiveToDistributionList/{employeeId}", method = POST)
  public ResponseEntity<?> proposeObjectiveToDistributionList(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @NotBlank(message = ERROR_EMPTY_TITLE) @Size(max = 150, message = ERROR_LIMIT_TITLE) String title,
      @RequestParam @NotBlank(message = ERROR_EMPTY_OBJECTIVE_DESCRIPTION) @Size(max = 2_000, message = ERROR_LIMIT_OBJECTIVE_DESCRIPTION) String description,
      @RequestParam @Pattern(regexp = REGEX_YEAR_MONTH, message = ERROR_DATE_FORMAT) String dueDate,
      @RequestParam @NotBlank(message = ERROR_EMPTY_DL) @Size(max = 100, message = ERROR_LIMIT_DL) String distributionListName)
  {
    try
    {
      final Objective objective = new Objective(title, description,
          presentOrFutureYearMonthToLocalDate(YearMonth.parse(dueDate)));
      final DistributionList distributionList = distributionListService.getCachedDistributionList(distributionListName);

      managerService.proposeObjective(employeeId, objective, distributionList);

      return ok("Objective inserted correctly");
    }
    catch (IllegalArgumentException | DocumentConversionException | EmployeeNotFoundException
        | InvalidAttributeValueException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to save a manager evaluation to a reportee.
   *
   * @param employeeId The employee ID of the manager saving the manager evaluation. Must be an integer greater than 0.
   * @param reporteeId POST request parameter - The employee ID of the reportee to whom a manager evaluation is to be
   *          saved. Must be an integer greater than 0.
   * @param managerEvaluation POST request parameter - the manager evaluation of the employee with the
   *          {@code reporteeId}. Must contain between 0 and 10,000 characters.
   * @param score POST request parameter - the score given to the employee as part of the manager evaluation. Must be an
   *          integer between 0 and 5.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the
   *         manager evaluation was successfully added. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/addManagerEvaluation/{employeeId}", method = POST)
  public ResponseEntity<?> addManagerEvaluation(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_EMPLOYEE_ID) long reporteeId,
      @RequestParam @Size(max = 10_000, message = ERROR_LIMIT_EVALUATION) String managerEvaluation,
      @RequestParam @Min(value = 0, message = ERROR_SCORE) @Max(value = 5, message = ERROR_SCORE) int score)
  {
    if (!Rating.isRatingPeriod())
    {
      return badRequest().body("Manager evaluations can only be added during the ratings submission window.");
    }

    try
    {
      int year = Rating.getRatingYear();
      managerService.addManagerEvaluation(reporteeId, year, managerEvaluation, score);
      return ok("Evaluation Added");
    }
    catch (EmployeeNotFoundException | InvalidAttributeValueException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP POST request to submit a previously saved manager evaluation to a reportee. Also sends an email to the
   * reportee to inform them of the submission.
   *
   * @param employeeId The employee ID of the manager saving the manager evaluation. Must be an integer greater than 0.
   * @param reporteeId POST request parameter - The employee ID of the reportee to whom a manager evaluation is to be
   *          saved. Must be an integer greater than 0.
   * @return {@code ResponseEntity<String>} with OK response and success message if the employee was found and the
   *         manager evaluation was successfully submitted. Bad Request response with error message otherwise.
   */
  @RequestMapping(value = "/submitManagerEvaluation/{employeeId}", method = POST)
  public ResponseEntity<?> submitManagerEvaluation(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @RequestParam @Min(value = 1, message = ERROR_EMPLOYEE_ID) long reporteeId)
  {
    if (!Rating.isRatingPeriod())
    {
      return badRequest().body("Manager evaluations can only be added during the ratings submission window.");
    }

    try
    {
      int year = Rating.getRatingYear();
      managerService.submitManagerEvaluation(reporteeId, year);
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

  /**
   * HTTP GET request to fetch the activity feeds of reportees for a line manager.
   *
   * @param employeeId The employee ID of the manager whose reportees' activity feeds are to be returned. Must be an
   *          integer greater than 0.
   * @return {@code ResponseEntity<List<ActivityFeed>>} with OK response and the activity feeds of the reportees of the
   *         line manager with the given employee ID.
   */
  @RequestMapping(value = "/getActivityFeed/{employeeId}", method = GET)
  public ResponseEntity<?> getActivityFeed(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    return ok(managerService.getActivityFeed(employeeId));
  }
}
