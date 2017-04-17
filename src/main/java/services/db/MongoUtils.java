package services.db;

import java.util.Arrays;
import java.util.List;

import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class MongoUtils
{
  private static final String DEFAULT_DATE_FORMAT = "%Y-%m-%dT%H:%M:%S.%L";

  private static final String ID = "_id";
  private static final String UNWIND = "$unwind";
  private static final String PROJECT = "$project";
  private static final String PUSH = "$push";
  private static final String SET = "$set";
  private static final String DATE_TO_STRING = "$dateToString";
  private static final String COND = "$cond";
  private static final String IF_NULL = "$ifNull";
  private static final String EQ = "$eq";
  private static final String LT = "$lt";
  private static final String CONCAT = "$concat";
  
  private MongoUtils() {}

  public static Document projection(final Document... projections)
  {
    final Document combinedProjection = new Document();

    for (int i = 0; i < projections.length; ++i)
    {
      projections[i].forEach((k, v) -> combinedProjection.append(k, v));
    }

    return project(combinedProjection);
  }

  public static Document project(final Bson projection)
  {
    return new Document(PROJECT, projection);
  }

  public static Document concat(final Object... objects)
  {
    return new Document(CONCAT, objects);
  }

  public static <T, U> Document lt(final T expression1, final U expression2)
  {
    final List<Object> lessThanArray = Arrays.asList(expression1, expression2);

    return new Document(LT, lessThanArray);
  }

  public static <T, U> Document eq(final T expression1, final U expression2)
  {
    final List<Object> equalityArray = Arrays.asList(expression1, expression2);
    return new Document(EQ, equalityArray);
  }

  public static Document ifNull(final String field, final String ifNull)
  {
    final String[] ifNullArray = new String[] { reference(field), ifNull };
    return new Document(IF_NULL, ifNullArray);
  }

//  public static <T, U> Document cond(final Document booleanExpression, final T trueExpression, final U falseExpression)
//  {
//    final List conditional = new BasicDBList();
//    conditional.addAll(booleanExpression, trueExpression, falseExpression);
//    return new Document(COND, conditional);
//  }

  public static Document dateToString(String dateField)
  {
    return new Document(DATE_TO_STRING, new Document("format", DEFAULT_DATE_FORMAT).append("date", dateField));
  }

  public static Document unwind(final String field)
  {
    return new Document(UNWIND, reference(field));
  }

  public static Document push(String fieldName, Bson update)
  {
    return new Document(PUSH, new Document(fieldName, update));
  }

  public static Document set(Bson bson)
  {
    return new Document(SET, bson);
  }

  public static Document excludeId()
  {
    return new Document(ID, 0);
  }

  public static String reference(final String field)
  {
    return "$".concat(field);
  }
}
