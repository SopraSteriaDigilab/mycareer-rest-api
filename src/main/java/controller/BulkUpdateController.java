package controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import services.BulkUpdateService;
import services.ad.ADConnectionException;
import utils.SequenceException;

/**
 * This class contains all the available roots of the web service
 */
@CrossOrigin
@RestController
public class BulkUpdateController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateController.class);
  
  private final BulkUpdateService bulkUpdateService;
  
  @Autowired
  public BulkUpdateController(final BulkUpdateService bulkUpdateService)
  {
    this.bulkUpdateService = bulkUpdateService;
  }

  /**
   * GET End Point - Syncs EMEAD and AD One employee profile data with the MyCareer database
   * employee profiles.
   * 
   * TODO: REMOVE! For testing purposes only.
   *
   * @return
   * @throws NamingException 
   * @throws ADConnectionException 
   * @throws SequenceException 
   */
  @RequestMapping(value = "/bulkUpdate", method = GET)
  public ResponseEntity<?> bulkUpdateEmployeeProfiles() throws ADConnectionException, NamingException, SequenceException
  {
    return ResponseEntity.ok(bulkUpdateService.fetchAllEmployeeProfiles());
  }
}
