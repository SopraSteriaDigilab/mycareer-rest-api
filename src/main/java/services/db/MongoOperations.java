package services.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dataStructure.Objective_NEW.Progress;

/**
 * This class contains the definition of the MongoOperations object
 * 
 * This class should be used for any CRUD operations using the the Mongo Java driver.
 *
 */
/**
 * TODO: Describe this TYPE.
 *
 */
public class MongoOperations
{

  public enum Collection
  {
    EMPLOYEE("employeeDataDev"), OBJECTIVES_HISTORY("objectivesHistories"), DEVELOPMENT_NEEDS_HISTORY(
        "developmentNeedsHistories"), COMPETENCIES_HISTORY("competenciesHistories");

    private String collectionStr;

    Collection(String collectionStr)
    {
      this.collectionStr = collectionStr;
    }

    public String getCollectionStr()
    {
      return this.collectionStr;
    }
  }

  private static final String DB_NAME = "Development";

  private static final String PROFILE_EMAIL_ADDRESSES = "profile.emailAddresses";
  private static final String ID = "_id";
  private static final String EMAIL_ADDRESSES = "emailAddresses";
  private static final String UNWIND = "$unwind";
  private static final String PROJECT = "$project";

  private final MongoDatabase mongoDB;

  private MongoCollection<Document> mongoCollection;

  /**
   * MongoOperations Constructor - Responsible for initialising this object.
   *
   * @param mongoClient
   */
  public MongoOperations(final MongoClient mongoClient)
  {
    this.mongoDB = mongoClient.getDatabase(DB_NAME);
  }

  /**
   * Use this for operations on the employee collection.
   * 
   * @return A MongoCollection reference to the employee collection.
   */
  public void setCollection(Collection collection)
  {
    this.mongoCollection = this.mongoDB.getCollection(collection.getCollectionStr());
  }

  /**
   * Method that returns an Iterable of all the values of a given field in the database. This method assumes the field
   * is a list.
   *
   * @param key The new key value of the field
   * @param reference The reference field in the database
   * @return An List<T> of the results.
   */
  public <T> List<T> getFieldAndUnwind(String key, String reference)
  {
    List<T> result = new ArrayList<>();
    mongoCollection.aggregate(Arrays.asList(project(excludeId().append(key, reference(reference))), unwind(key)))
        .forEach((Block<Document>) d -> result.add((T) d.get(key)));
    return result;
  }

  private Bson project(final Bson projection)
  {
    return new Document(PROJECT, projection);
  }

  private Document excludeId()
  {
    return new Document(ID, 0);
  }

  private String reference(final String field)
  {
    return "$".concat(field);
  }

  private Bson unwind(final String field)
  {
    return new Document(UNWIND, reference(field));
  }

}
