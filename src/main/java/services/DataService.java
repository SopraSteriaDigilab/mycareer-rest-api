package services;

import static dataStructure.EmployeeProfile.*;
import static dataStructure.EmailAddresses.*;

import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.FindIterable;

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
    final Set<String> emails = employeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS,
        USER_ADDRESS);

    emails.remove(null);
    emails.remove("");
    LOGGER.debug("Email address count is {}", emails.size());

    return emails;
  }

  /**
   * TODO: Describe this method.
   *
   * @return
   */
  public FindIterable<Document> getAllNamesAndIds()
  {
    return employeeOperations.getFields(new Document(), "profile.forename", "profile.surname", "profile.employeeID");
  }
}