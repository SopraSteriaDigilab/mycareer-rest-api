package controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.HRData;
import domain.HRObjectiveData;
import services.HRDataDAO;

/**
 * HRController Class - Rest controller for the HR of MyCareer.
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/hr")
public class HRController
{

  /**
   * This method allows the front-end to retrieve a summary of a user's details and un-archived objectives.
   */
  @RequestMapping(value = "/getHRObjectiveData", method = GET)
  public ResponseEntity<List<HRObjectiveData>> getHRObjectiveData()
  {
    return ResponseEntity.ok(HRDataDAO.getHRObjectiveData());
  }

  /**
   * This method allows the front-end to retrieve a totals, objective counts, and development need counts in a single
   * API call.
   * 
   */
  @RequestMapping(value = "/getHRData", method = GET)
  public ResponseEntity<HRData> getHRData()
  {
    return ResponseEntity.ok(HRDataDAO.getHRData());
  }

//  /**
//   * GET End Point - Gets lists of employees and the number of feedback they have received
//   *
//   * @return
//   */
//  @RequestMapping(value = "/getFeedbackStats", method = GET)
//  public ResponseEntity<?> getFeedbackStats()
//  {
//    return ResponseEntity.ok(HRDataDAO.getFeedbackStats());
//  }

}
