package services;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.db.MongoOperations;

public class DataService
{
  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

  private static final String EMAIL_ADDRESSES = "emailAddresses";
  private static final String MAIL = "profile.emailAddresses.mail";
  private static final String TARGET_ADDRESS = "profile.emailAddresses.targetAddress";
  private static final String USER_ADDRESS = "profile.emailAddresses.userAddress";

  private final MongoOperations employeeOperations;

  public DataService(final MongoOperations employeeOperations)
  {
    this.employeeOperations = employeeOperations;
  }

  public Set<String> getAllEmailAddresses()
  {
    final Set<String> emails = employeeOperations.getFieldValuesAsSet(EMAIL_ADDRESSES, MAIL, TARGET_ADDRESS,
        USER_ADDRESS);

    LOGGER.debug("Email address count is {}", emails.size());

    return emails;
  }
}