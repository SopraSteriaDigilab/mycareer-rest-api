package controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import services.DataService;

/**
 * REST controller for end points providing data directly from the database.
 * 
 * @see DataService
 */
@CrossOrigin
@RestController
@RequestMapping("/data")
public class DataController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

  private DataService dataService;

  /**
   * DataController Constructor - Responsible for initialising this object.
   *
   * @param dataService the service layer for DataController end points
   */
  @Autowired
  public DataController(final DataService dataService)
  {
    this.dataService = dataService;
  }

  /**
   * @return a {@code ResponseEntity<Set<String>>} of all email addresses in the database.
   */
  @RequestMapping(value = "/getAllEmailAddresses", method = GET)
  public ResponseEntity<Set<String>> getAllEmailAddresses()
  {
    LOGGER.debug("Retrieving all email addresses");

    final Set<String> emailAddresses = dataService.getAllEmailAddresses();

    return ok(emailAddresses);
  }

  /**
   * @return a {@code ResponseEntity<?>} containing all forenames, surnames and employee IDs in the database
   */
  @RequestMapping(value = "/getAllNamesAndIds", method = GET)
  public ResponseEntity<?> getAllNamesAndIds()
  {
    LOGGER.debug("Retrieving all name and details");

    return ok(dataService.getAllNamesAndIds());
  }
}
