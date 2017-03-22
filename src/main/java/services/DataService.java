package services;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Aggregates.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
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

  private static final String PROFILE_EMAIL_ADDRESS = "$profile.emailAddress";
  private static final String ID = "_id";
  private static final String EMAIL_ADDRESSES = "emailAddresses";
  private static final String UNWIND = "$unwind";

  private final MongoCollection<Document> mongoCollection;

  public DataService(final MongoClient mongoClient)
  {
    this.mongoCollection = mongoClient.getDatabase(DB_NAME).getCollection(DB_COLLECTION);
  }

  public List<String> getAllEmailAddresses()
  {
    final List<String> emails = new ArrayList<>();
    final List<Bson> aggregationPipeline = new ArrayList<>();
    final Bson projectEmailAddresses = excludeId().append(EMAIL_ADDRESSES, PROFILE_EMAIL_ADDRESS);
    final Bson unwindEmails = unwind(EMAIL_ADDRESSES);

    aggregationPipeline.add(projectEmailAddresses);
    aggregationPipeline.add(unwindEmails);
    mongoCollection.aggregate(aggregationPipeline)
        .forEach((Block<Document>) d -> emails.add(d.getString(EMAIL_ADDRESSES)));

    LOGGER.debug("Email address count is {}", emails.size());

    return emails;
  }
  
  private Document excludeId()
  {
    return new Document(ID, 0);
  }
  
  private Bson unwind(final String field)
  {
    return new Document(UNWIND, field);
  }
}