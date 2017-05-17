package controller;

import static application.GlobalExceptionHandler.*;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import services.EmployeeNotFoundException;
import services.EmployeeProfileService;
import services.HRService;

/**
 * REST controller for end points providing HR with statistical data.
 * 
 * @see HRService
 * @see EmployeeProfileService
 */
@CrossOrigin
@RestController
@RequestMapping("/hr")
public class HRController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(HRController.class);

  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";

  @Autowired
  private HRService hrService;

  @Autowired
  private EmployeeProfileService employeeProfileService;

  /**
   * HTTP GET request to fetch the data for an employee corresponding to {@code searchEmployeeId}.
   *
   * @param employeeId The employee ID of the employee performing the search. Must be an integer greater than 0.
   * @param searchEmployeeId The employee ID of the employee whose data is to be returned. Must be an integer greater
   *          than 0.
   * @return {@code ResponseEntity<Employee> with OK response and body containing the employee with
   *         {@code searchEmployeeId}. Bad request response with error message if the employee ID could not be found.
   */
  @RequestMapping(value = "/{employeeId}/getCareer/{searchEmployeeId}", method = GET)
  public ResponseEntity<?> getMyCareer(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId,
      @PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long searchEmployeeId)
  {
    LOGGER.debug("/hr/getCareer called");

    try
    {
      final Employee employee = hrService.getMyCareer(searchEmployeeId);
      final EmployeeProfile profile = employee.getProfile();
      employeeProfileService.setHasHRDash(profile);

      if (employeeId != searchEmployeeId && profile.getHasHRDash())
      {
        throw new EmployeeDataRestrictionException();
      }

      return ok(employee);
    }
    catch (EmployeeNotFoundException | EmployeeDataRestrictionException e)
    {
      return badRequest().body(error(e.getMessage()));
    }
  }

  /**
   * HTTP GET request - Gets an overview of MyCareer statistics.
   *
   * @return
   */
  @RequestMapping(value = "/getMyCareerStats", method = GET)
  public ResponseEntity<?> getMyCareerStats()
  {
    LOGGER.debug("/hr/getMyCareerStats called");

    return ResponseEntity.ok(hrService.getMyCareerStats());
  }

  /**
   * HTTP GET request - Gets lists of employees with department data
   *
   * @return
   */
  @RequestMapping(value = "/getEmployeeStats", method = GET)
  public ResponseEntity<?> getEmployeeStats()
  {
    LOGGER.debug("/hr/getEmployeeStats called");

    return ResponseEntity.ok(hrService.getEmployeeStats());
  }

  /**
   * HTTP GET request - Gets lists of employees and the number of feedback they have received
   *
   * @return
   */
  @RequestMapping(value = "/getFeedbackStats", method = GET)
  public ResponseEntity<?> getFeedbackStats()
  {
    LOGGER.debug("/hr/getFeedbackStats called");

    return ResponseEntity.ok(hrService.getFeedbackStats());
  }

  /**
   * HTTP GET request - Gets lists of employees and their objective statistics
   *
   * @return
   */
  @RequestMapping(value = "/getObjectiveStats", method = GET)
  public ResponseEntity<?> getObjectiveStats()
  {
    LOGGER.debug("/hr/getObjectiveStats called");

    return ResponseEntity.ok(hrService.getObjectiveStats());
  }

  /**
   * HTTP GET request - Gets lists of employees and their development needs statistics
   *
   * @return
   */
  @RequestMapping(value = "/getDevelopmentNeedStats", method = GET)
  public ResponseEntity<?> getDevelopmentNeedStats()
  {
    LOGGER.debug("/hr/getDevelopmentNeedStats called");

    return ResponseEntity.ok(hrService.getDevelopmentNeedStats());
  }

  /**
   * HTTP GET request - Gets lists of employees with started and in progress development needs with categories.
   *
   * @return
   */
  @RequestMapping(value = "/getDevelopmentNeedBreakDown", method = GET)
  public ResponseEntity<?> getDevelopmentNeedBreakDown()
  {
    LOGGER.debug("/hr/getDevelopmentNeedBreakDown called");

    return ResponseEntity.ok(hrService.getDevelopmentNeedBreakDown());
  }

  /**
   * HTTP GET request - Gets list of sectors and statistics about employees, objectives and development needs in each
   * sector.
   *
   * @return
   */
  @RequestMapping(value = "/getSectorBreakDown", method = GET)
  public ResponseEntity<?> getSectorBreakDown()
  {
    LOGGER.debug("/hr/getSectorBreakDown called");

    return ResponseEntity.ok(hrService.getSectorBreakDown());
  }

}
