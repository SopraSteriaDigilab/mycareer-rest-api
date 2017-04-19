package services;

import static dataStructure.EmployeeProfile.*;
import static dataStructure.EmailAddresses.*;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.db.MongoOperations;

public class DataService
{
  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

  private final MongoOperations employeeOperations;

  public DataService(final MongoOperations employeeOperations)
  {
    this.employeeOperations = employeeOperations;
  }

  public Set<String> getAllEmailAddresses()
  {
    final Set<String> emails = employeeOperations.getFieldValuesAsSet(EMAIL_ADDRESSES, MAIL, TARGET_ADDRESS,
        USER_ADDRESS);
    emails.remove(null);
    LOGGER.debug("Email address count is {}", emails.size());

    return emails;
  }
}