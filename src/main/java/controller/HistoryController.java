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
 * 
 * TODO: Describe this TYPE.
 *
 */
@CrossOrigin
@RestController
@Validated
@RequestMapping("/history")
public class HistoryController
{
  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(HistoryController.class);

  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";

  @Autowired
  private HistoryService historyService;

  @Autowired
  private HRService hrService;

  /**
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
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
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
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
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
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
   * 
   * GET end point - gets all notes for a user
   * 
   * @param employeeID the ID of the employee
   * @return list of notes
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
   * 
   * This method allows the front-end to retrieve the latest version of each feedback related to a specific user
   * 
   * @param employeeID the employee ID (>0)
   * @return list of feedback (only the latest version of them)
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
   * 
   * TODO: Describe this method.
   *
   * @param employeeId
   * @return
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
