package services;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class DataService
{
  /** Logger Constant - Represents an implementation of the Logger interface that may be used here.. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);
  
  private static final String DB_NAME = "Development";
  private static final String DB_COLLECTION = "employeeDataDev";
  
  private static final String PROFILE_EMAIL_ADDRESS = "profile.emailAddress";
  private static final String PROFILE = "profile";
  private static final String EMAIL_ADDRESS = "emailAddress";
  
  private final MongoCollection<Document> mongoCollection;
  
  public DataService(final MongoClient mongoClient)
  {
    this.mongoCollection = mongoClient.getDatabase(DB_NAME)
                                      .getCollection(DB_COLLECTION);
  }

  public List<String> getAllEmailAddresses()
  {
    final List<String> emails = new ArrayList<String>();
    
    mongoCollection.find(exists(PROFILE_EMAIL_ADDRESS))
                  .projection(fields(include(PROFILE_EMAIL_ADDRESS), excludeId()))
                  .sort(ascending(PROFILE_EMAIL_ADDRESS))
                  .forEach((Block<Document>) d -> emails.add(d.get(PROFILE, Document.class).getString(EMAIL_ADDRESS)));
    
    LOGGER.debug("Email address count is {}", emails.size());
    
    return emails;
  }
}