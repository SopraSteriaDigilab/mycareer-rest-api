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

  private final MongoOperations mongoOperations;

  public DataService(final MongoOperations mongoOperations)
  {
    this.mongoOperations = mongoOperations;
  }

  public List<String> getAllEmailAddresses()
  {
    final List<String> emails = mongoOperations.employeeCollection().getFieldAndUnwind(EMAIL_ADDRESSES, PROFILE_EMAIL_ADDRESSES);

    LOGGER.info("Email address count is {}", emails.size());

    return emails;
  }
}