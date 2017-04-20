package services.db;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static services.db.MongoUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

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
  private static final String TITLE = "title";
  private static final String IS_SELECTED = "isSelected";
  private static final String LAST_MODIFIED = "lastModified";
  private static final String HISTORY = "history";

  private final MongoDatabase mongoDB;

  private final MongoCollection<Document> mongoCollection;

  /**
   * MongoOperations Constructor - Responsible for initialising this object.
   *
   * @param mongoClient
   */
  public MongoOperations(final MongoClient mongoClient, Collection collection)
  {
    this.mongoDB = mongoClient.getDatabase(DB_NAME);
    this.mongoCollection = this.mongoDB.getCollection(collection.getCollectionStr());
  }
  
  /**
   * Performs an aggregation using the given pipeline and returns the results as a List.
   *
   * @param aggregationStages The aggregation pipeline stages
   * @return The results of the aggregation query
   */
  public <T> List<T> aggregateAsList(String fieldToList, Document... aggregationStages)
  {
    AggregateIterable<Document> rawResults = mongoCollection.aggregate(Arrays.asList(aggregationStages));
    
    return aggregationResultsToList(rawResults, fieldToList);
  }
  
  /**
   * Performs an aggregation using the given pipeline and returns the results as a List.
   *
   * @param aggregationStages The aggregation pipeline stages
   * @return The results of the aggregation query
   */
  public List<Document> aggregateAsList(Document... aggregationStages)
  {
    AggregateIterable<Document> rawResults = mongoCollection.aggregate(Arrays.asList(aggregationStages));
    
    return aggregationResultsToList(rawResults);
  }
  
  /**
   * Performs an aggregation using the given pipeline and returns the results as a List.
   *
   * @param aggregationStages The aggregation pipeline stages
   * @return The results of the aggregation query
   */
  public <T> T aggregateSingleResult(String fieldToGet, Class<T> fieldClass, Document... aggregationStages)
  {
    AggregateIterable<Document> rawResults = mongoCollection.aggregate(Arrays.asList(aggregationStages));
    
    return rawResults.first().get(fieldToGet, fieldClass);
  }

  /**
   * Method that returns an List of all the values of a given field in the database. This method assumes the field is a
   * list in the database
   *
   * @param key The new key value of the field
   * @param reference The reference field in the database
   * @return An List<T> of the results.
   */
  public <T> Set<T> getFieldValuesAsSet(String listName, String... fieldNames)
  {
    AggregateIterable<Document> rawResults = mongoCollection.aggregate(Arrays.asList(projectFieldsToArray(listName, fieldNames), unwind(listName)));
    
    return aggregationResultsToSet(rawResults, listName);
  }
  
  @SuppressWarnings("unchecked")
  private <T> List<T> aggregationResultsToList(final AggregateIterable<Document> aggregationResults, final String fieldToGet)
  {
    List<T> resultList = new ArrayList<>();
    aggregationResults.forEach((Block<Document>) d -> resultList.add((T) d.get(fieldToGet)));
    
    return resultList;
  }
  
  @SuppressWarnings("unchecked")
  private List<Document> aggregationResultsToList(final AggregateIterable<Document> aggregationResults)
  {
    List<Document> resultList = new ArrayList<>();
    aggregationResults.forEach((Block<Document>) d -> resultList.add(d));
    
    return resultList;
  }
  
  @SuppressWarnings("unchecked")
  private <T> Set<T> aggregationResultsToSet(final AggregateIterable<Document> aggregationResults, final String fieldToGet)
  {
    Set<T> resultSet = new HashSet<>();
    aggregationResults.forEach((Block<Document>) d -> resultSet.add((T) d.get(fieldToGet)));
    
    return resultSet;
  }

  /**
   * Method to add to either the Objectives or Development Needs history collections. Upsert is set to true.
   *
   * @param find The filter of _id document to match
   * @param update The update to store
   */
  public void addToObjDevHistory(Document find, Document update)
  {
    mongoCollection.updateOne(eq(ID, find), push(HISTORY, update), new UpdateOptions().upsert(true));
  }

  /**
   * Performs an update of a single document using the given filter, setting the field/value pairs in keyValuePairs.
   *
   * @param bson the filter to find the document to update
   * @param keyValuePairs a document representing zero or more fields to update in the document matching the filter
   * @return true if the field was updated, false otherwise
   */
  public boolean setFields(Bson filter, Document keyValuePairs)
  {
    UpdateResult result = mongoCollection.updateOne(filter, set(keyValuePairs));
    
    return result.getModifiedCount() == 1;
  }

  /**
   * Method to add to competencies history collections. Upsert is set to true.
   *
   * @param find The filter of _id document to match
   * @param update The update to store
   */
  public void addToCompetenciesHistory(long employeeId, String title, boolean isSelected, String lastModified)
  {
    mongoCollection.insertOne(new Document(ID, new Document(EMPLOYEE_ID, employeeId).append(TITLE, title)
        .append(IS_SELECTED, isSelected).append(LAST_MODIFIED, lastModified)));
  }

  /**
   * Creates an Document that represents the _id field in the objectives history collections.
   *
   * @param employeeId The employee id
   * @param objectiveId The objective id
   * @param createdOn The creation date.
   * @return A Document with the fields passed through
   */
  public static Document objectiveHistoryIdFilter(long employeeId, int objectiveId, String createdOn)
  {
    return new Document(EMPLOYEE_ID, employeeId).append(OBJECTIVE_ID, objectiveId).append(CREATED_ON, createdOn);
  }

  /**
   * Creates an Document that represents the _id field in the development needs history collections.
   *
   * @param employeeId The employee id
   * @param objectiveID The objective id
   * @param createdOn The creation date.
   * @return A Document with the fields passed through
   */
  public static Document developmentNeedHistoryIdFilter(long employeeId, int developmentNeedId, String createdOn)
  {
    return new Document(EMPLOYEE_ID, employeeId).append(DEVELOPMENT_NEED_ID, developmentNeedId).append(CREATED_ON,
        createdOn);
  }
}
