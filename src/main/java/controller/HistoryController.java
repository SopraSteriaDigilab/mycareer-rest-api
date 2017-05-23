package controller;

import static application.GlobalExceptionHandler.*;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dataStructure.Employee;
import services.EmployeeNotFoundException;
import services.HRService;
import services.HistoryService;

/**
 * REST controller for end points providing access to historical employee data.
 * 
 * @see HistoryService
 * @see HRService
 */
@CrossOrigin
@RestController
@Validated
@RequestMapping("/history")
public class HistoryController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(HistoryController.class);

  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";

  @Autowired
  private HistoryService historyService;

  @Autowired
  private HRService hrService;

  /**
   * HTTP GET request to fetch all data corresponding to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose data is to be returned. Must be an integer greater than 0.
   * @return {@code ResponseEntity<Employee> with OK response and body containing the employee with {@code employeeId}.
   *         Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getMyCareer/{employeeId}", method = GET)
  public ResponseEntity<?> getMyCareer(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    LOGGER.debug("/history/getMyCareer called");

    try
    {
      final Employee employee = hrService.getMyCareer(employeeId);

      return ok(employee);
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP GET request to fetch all objectives corresponding to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose objectives are to be returned. Must be an integer greater
   *          than 0.
   * @return {@code ResponseEntity<List<Objective>> with OK response and body containing all objectives of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getObjectives/{employeeId}", method = GET)
  public ResponseEntity<?> getObjectives(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    LOGGER.debug("/history/getObjectives called");

    try
    {
      return ok(historyService.getObjectives(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP GET request to fetch all development needs corresponding to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose development needs are to be returned. Must be an integer
   *          greater than 0.
   * @return {@code ResponseEntity<List<DevelopmentNeed>> with OK response and body containing all development needs of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getDevelopmentNeeds/{employeeId}", method = GET)
  public ResponseEntity<?> getDevelopmentNeeds(
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    LOGGER.debug("/history/getDevelopmentNeeds called");

    try
    {
      return ok(historyService.getDevelopmentNeeds(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP GET request to fetch all notes corresponding to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose notes are to be returned. Must be an integer greater than
   *          0.
   * @return {@code ResponseEntity<List<Note>> with OK response and body containing all notes of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getNotes/{employeeID}", method = GET)
  public ResponseEntity<?> getNotes(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID)
  {
    LOGGER.debug("/history/getNotes called");

    try
    {
      return ok(historyService.getNotes(employeeID));
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * HTTP GET request to fetch all feedback corresponding to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose feedback is to be returned. Must be an integer greater than
   *          0.
   * @return {@code ResponseEntity<List<Feedback>> with OK response and body containing all feedback of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getFeedback/{employeeID}", method = GET)
  public ResponseEntity<?> getFeedback(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeID)
  {
    LOGGER.debug("/history/getFeedback called");

    try
    {
      return ok(historyService.getFeedback(employeeID));
    }
    catch (final EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }

  /**
   * HTTP GET request to fetch all ratings corresponding to the employee with the given employee ID.
   *
   * @param employeeId The employee ID of the employee whose ratings are to be returned. Must be an integer greater than
   *          0.
   * @return {@code ResponseEntity<List<Rating>> with OK response and body containing all ratings of the employee with
   *         {@code employeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/getRatings/{employeeId}", method = GET)
  public ResponseEntity<?> getRatings(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    LOGGER.debug("/history/getRatings called");

    try
    {
      return ok(historyService.getRatings(employeeId));
    }
    catch (EmployeeNotFoundException e)
    {
      return badRequest().body(e.getMessage());
    }
  }
}
