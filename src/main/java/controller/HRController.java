package controller;

import static application.GlobalExceptionHandler.error;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
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
 * HRController Class - Rest controller for the HR of MyCareer.
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/hr")
public class HRController
{
  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(HRController.class);

  private static final String ERROR_EMPLOYEE_ID = "The given Employee ID is invalid";

  /** HRDataDAO Constant - Represents the service to be user for hr data. */
  @Autowired
  private HRService hrService;
  
  @Autowired
  private EmployeeProfileService employeeProfileService;

  @RequestMapping(value = "/getMyCareer/{employeeId}", method = GET)
  public ResponseEntity<?> getMyCareer(@PathVariable @Min(value = 1, message = ERROR_EMPLOYEE_ID) long employeeId)
  {
    try
    {
      final Employee employee = hrService.getMyCareer(employeeId);
      final EmployeeProfile profile = employee.getProfile();
      employeeProfileService.setHasHRDash(profile);
      
      if (profile.getHasHRDash())
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
   * GET End Point - Gets overview of my career stats
   *
   * @return
   */
  @RequestMapping(value = "/getMyCareerStats", method = GET)
  public ResponseEntity<?> getMyCareerStats()
  {
    return ResponseEntity.ok(hrService.getMyCareerStats());
  }

  /**
   * GET End Point - Gets lists of employees with department data
   *
   * @return
   */
  @RequestMapping(value = "/getEmployeeStats", method = GET)
  public ResponseEntity<?> getEmployeeStats()
  {
    return ResponseEntity.ok(hrService.getEmployeeStats());
  }

  /**
   * GET End Point - Gets lists of employees and the number of feedback they have received
   *
   * @return
   */
  @RequestMapping(value = "/getFeedbackStats", method = GET)
  public ResponseEntity<?> getFeedbackStats()
  {
    return ResponseEntity.ok(hrService.getFeedbackStats());
  }

  /**
   * GET End Point - Gets lists of employees and their objective statistics
   *
   * @return
   */
  @RequestMapping(value = "/getObjectiveStats", method = GET)
  public ResponseEntity<?> getObjectiveStats()
  {
    return ResponseEntity.ok(hrService.getObjectiveStats());
  }

  /**
   * GET End Point - Gets lists of employees and their development needs statistics
   *
   * @return
   */
  @RequestMapping(value = "/getDevelopmentNeedStats", method = GET)
  public ResponseEntity<?> getDevelopmentNeedStats()
  {
    return ResponseEntity.ok(hrService.getDevelopmentNeedStats());
  }

  /**
   * GET End Point - Gets lists of employees with started and in progress development needs with categories.
   *
   * @return
   */
  @RequestMapping(value = "/getDevelopmentNeedBreakDown", method = GET)
  public ResponseEntity<?> getDevelopmentNeedBreakDown()
  {
    return ResponseEntity.ok(hrService.getDevelopmentNeedBreakDown());
  }

  /**
   * GET End Point - Gets list of sectors and statistics about employees, objectives and development needs in each
   * sector.
   *
   * @return
   */
  @RequestMapping(value = "/getSectorBreakDown", method = GET)
  public ResponseEntity<?> getSectorBreakDown()
  {
    return ResponseEntity.ok(hrService.getSectorBreakDown());
  }

}
