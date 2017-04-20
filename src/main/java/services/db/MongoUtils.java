package services.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.BasicBSONList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class MongoUtils
{
  private static final String DEFAULT_DATE_FORMAT = "%Y-%m-%dT%H:%M:%S.%L";

  private static final String ID = "_id";
  private static final String UNWIND = "$unwind";
  private static final String PROJECT = "$project";
  private static final String MATCH = "$match";
  private static final String GROUP = "$group";
  private static final String SORT = "$sort";
  private static final String PUSH = "$push";
  private static final String SET = "$set";
  private static final String DATE_TO_STRING = "$dateToString";
  private static final String COND = "$cond";
  private static final String IF_NULL = "$ifNull";
  private static final String EQ = "$eq";
  private static final String LT = "$lt";
  private static final String CONCAT = "$concat";
  private static final String INDEX_OF_CP = "$indexOfCP";
  private static final String SUBSTR_CP = "$substrCP";
  private static final String ADD_TO_SET = "$addToSet";
  private static final String IN = "$in";
  private static final String OR = "$or";

  
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
  
  public static Document projectExcludeId()
  {
    return project(excludeId());
  }
  
  public static Document projectFieldsToArray(String arrayName, String... fieldNames)
  {
    List<String> toArray = new ArrayList<>();
    
    for (String fieldName : fieldNames)
    {
      toArray.add(reference(fieldName));
    }
    
    Document projection = excludeId().append(arrayName, toArray);
    
    return project(projection);
  }
  
  public static Document projectRenamedField(final String newName, final String field)
  {
    return project(new Document(newName, reference(field)));
  }
  
  public static Document projectField(final String field)
  {
    return projectRenamedField(field, field);
  }
  
  public static Document addSubstringToSet(final String field, final String indexString, final int substringLength)
  {
    Document indexOfCP = indexOfCP(field, indexString);
    Document substrCP = substrCP(field, indexOfCP, substringLength);
    Document addToSet = addToSet(substrCP);
    Document groupDocument = nullGrouping().append(field, addToSet);
    
    return group(groupDocument);
  }

  public static Document nullGrouping()
  {
    return new Document(ID, null);
  }

  public static Document group(Document document)
  {
    return new Document(GROUP, document);
  }

  public static <T> Document addToSet(T value)
  {
    return new Document(ADD_TO_SET, value);
  }
  
  public static Document sortDescending(final String fieldName)
  {
    return sort(new Document(fieldName, -1));
  }
  
  public static Document sort(final Document sortDocument)
  {
    return new Document(SORT, sortDocument);
  }

  public static Document substrCP(String field, Document startIndex, int substringLength)
  {
    BasicBSONList substrCPList = new BasicBSONList();
    substrCPList.put("0", reference(field));
    substrCPList.put("1", startIndex);
    substrCPList.put("2", substringLength);
    
    return new Document(SUBSTR_CP, substrCPList);
  }

  public static Document indexOfCP(String field, String indexString)
  {
    BasicBSONList indexOfCPList = new BasicBSONList();
    indexOfCPList.put("0", reference(field));
    indexOfCPList.put("1", indexString);
    
    return new Document(INDEX_OF_CP, indexOfCPList);
  }

//  public static Document concat(final Object... objects)
//  {
//    return new Document(CONCAT, objects);
//  }

//  public static <T, U> Document lt(final T expression1, final U expression2)
//  {
//    final List<Object> lessThanArray = Arrays.asList(expression1, expression2);
//
//    return new Document(LT, lessThanArray);
//  }

//  public static <T, U> Document eq(final T expression1, final U expression2)
//  {
//    final List<Object> equalityArray = Arrays.asList(expression1, expression2);
//    return new Document(EQ, equalityArray);
//  }

//  public static Document ifNull(final String field, final String ifNull)
//  {
//    final String[] ifNullArray = new String[] { reference(field), ifNull };
//    return new Document(IF_NULL, ifNullArray);
//  }

//  public static <T, U> Document cond(final Document booleanExpression, final T trueExpression, final U falseExpression)
//  {
//    final List conditional = new BasicDBList();
//    conditional.addAll(booleanExpression, trueExpression, falseExpression);
//    return new Document(COND, conditional);
//  }

  public static <T> Document matchField(final String fieldToMatch, final T value)
  {
    return match(new Document(fieldToMatch, value));
  }
  
  public static Document match(Document document)
  {
    return new Document(MATCH, document);
  }

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
  
  public static <T> Document in(List<T> list)
  {
    return new Document(IN, list);
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
