package services.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

/**
 * This class contains the definition of the MongoOperations object
 * 
 * This class should be used for any CRUD operations using the the Mongo Java driver.
 *
 */
public class MongoOperations
{

  /**
   * Collection enum. Represents the different collections on the Development database.
   *
   */
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

  private static final String ID = "_id";
  private static final String EMPLOYEE_ID = "employeeID";
  private static final String OBJECTIVE_ID = "objectiveID";
  private static final String DEVELOPMENT_NEED_ID = "developmentNeedID";
  private static final String CREATED_ON = "createdOn";
  
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
  private void setCollection(Collection collection)
  {
    this.mongoCollection = this.mongoDB.getCollection(collection.getCollectionStr());
  }

  /**
   * Sets the collection to the employee collection.
   *
   * @return this
   */
  public MongoOperations employeeCollection()
  {
    this.setCollection(Collection.EMPLOYEE);
    return this;
  }

  /**
   * Sets the collection to the employee collection.
   *
   * @return this
   */
  public MongoOperations objectivesHistoriesCollection()
  {
    this.setCollection(Collection.OBJECTIVES_HISTORY);
    return this;
  }

  /**
   * Sets the collection to the employee collection.
   *
   * @return this
   */
  public MongoOperations developmentNeedsHistoriesCollection()
  {
    this.setCollection(Collection.DEVELOPMENT_NEEDS_HISTORY);
    return this;
  }

  /**
   * Sets the collection to the employee collection.
   *
   * @return this
   */
  public MongoOperations competenciesHistoriesCollection()
  {
    this.setCollection(Collection.COMPETENCIES_HISTORY);
    return this;
  }

  /**
   * Method that returns an List of all the values of a given field in the database. This method assumes the field is a
   * list in the database
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

  /**
   * Method to add one of the history collections. Upsert is set to true. 
   *
   * @param find The filter of _id document to match
   * @param update The update to store
   */
  public void addToHistory(Document find, Document update)
  {
    mongoCollection.updateOne(Filters.eq("_id", find), new Document("$push", new Document("history", update)),
        new UpdateOptions().upsert(true));
  }

  /**
   * Creates an Document that represents the _id field in the objectives history collections.
   *
   * @param employeeId The employee id
   * @param objectiveID The objective id
   * @param createdOn The creation date.
   * @return A Document with the fields passed through
   */
  public static Document objectiveHistoryIdFilter(long employeeId, int objectiveID, String createdOn)
  {
    return new Document(EMPLOYEE_ID, employeeId).append(OBJECTIVE_ID, objectiveID).append(CREATED_ON, createdOn);
  }
  
  /**
   * Creates an Document that represents the _id field in the development needs history collections.
   *
   * @param employeeId The employee id
   * @param objectiveID The objective id
   * @param createdOn The creation date.
   * @return A Document with the fields passed through
   */
  public static Document developmentNeedHistoryIdFilter(long employeeId, int developmentNeedID, String createdOn)
  {
    return new Document(EMPLOYEE_ID, employeeId).append(DEVELOPMENT_NEED_ID, developmentNeedID).append(CREATED_ON, createdOn);
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
