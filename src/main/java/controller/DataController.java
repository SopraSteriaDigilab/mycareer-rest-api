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

@CrossOrigin
@RestController
@RequestMapping("/data")
public class DataController
{
  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);
  
  private DataService dataService;
  
  @Autowired
  public DataController(final DataService dataService)
  {
    this.dataService = dataService;
  }
  
  @RequestMapping(value = "/getAllEmailAddresses", method = GET)
  public ResponseEntity<Set<String>> getAllEmailAddresses()
  {
    LOGGER.debug("Retrieving all email addresses");
    final Set<String> emailAddresses = dataService.getAllEmailAddresses();
    
    return ok(emailAddresses);
  }
}
