package services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.db.MongoOperations;

public class DataService
{
  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);
  
  private static final String EMAIL_ADDRESSES = "emailAddresses";
  private static final String PROFILE_EMAIL_ADDRESSES = "profile.emailAddresses";

  private final MongoOperations employeeOperations;

  public DataService(final MongoOperations employeeOperations)
  {
    this.employeeOperations = employeeOperations;
  }

  public List<String> getAllEmailAddresses()
  {
    final List<String> emails = employeeOperations.getFieldAndUnwind(EMAIL_ADDRESSES, PROFILE_EMAIL_ADDRESSES);

    LOGGER.debug("Email address count is {}", emails.size());

    return emails;
  }
}