//package dataStructure;
//
//import static dataStructure.Constants.UK_TIMEZONE;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.MockitoAnnotations.initMocks;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.mock;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.time.LocalDateTime;
//import java.time.YearMonth;
//import java.time.ZoneId;
//
//import javax.management.InvalidAttributeValueException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//
//
//import com.google.gson.Gson;
//
//public class ObjectiveTest
//{
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int INVALID_EMPLOYEE_ID = -675590;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int VALID_EMPLOYEE_ID = 675590;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int INVALID_PROGRESS =3;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int VALID_PROGRESS =2;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_NAME = "Alexandre Brard";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int INVALID_PERFORMANCE = 3;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int VALID_PERFORMANCE =2;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_TITLE = "atitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150characters";;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_TITLE = "a valid title";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_DESCRIPTION = "adescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000characters";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_DESCRIPTION = "a valid description";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String PAST_DATE = "2010-01";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String FUTURE_DATE = "3010-01";
//  
//  @InjectMocks
//  private Objective_OLD unitUnderTest, unitUnderTest2, unitUnderTest3, unitUnderTest4;
//  
//  /**
//   * Setup method that runs once before each test method.
//   * @throws InvalidAttributeValueException 
//   * 
//   */
//  @Before
//  public void setup() throws InvalidAttributeValueException
//  {
//   initMocks(this);
//   unitUnderTest = new Objective_OLD();
//   unitUnderTest2 = new Objective_OLD(VALID_EMPLOYEE_ID, VALID_PROGRESS, VALID_PERFORMANCE, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
//   unitUnderTest3 = new Objective_OLD(unitUnderTest2);
//   unitUnderTest4 = new Objective_OLD(VALID_PROGRESS, VALID_PERFORMANCE, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
//  }
//  
//  /**
//   * For unit tests.
//   * 
//   */
//  public void setPrivateField(String field, Object obj, Object newValue) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException 
//  {
//    Field f = obj.getClass().getDeclaredField(field);
//    f.setAccessible(true);
//    f.set(obj,newValue);
//  }
//  
//  /**
//   * Unit test for the setID method : Invalid ID
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetIDwithInvalidID() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setID(INVALID_EMPLOYEE_ID);
//  }
//      
//  /**
//   * Unit test for the setID method : Valid ID.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetIDwithValidID() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setID(VALID_EMPLOYEE_ID);
//    assertEquals(unitUnderTest.getID(),VALID_EMPLOYEE_ID);
//  }
//  
//  /**
//   * Unit test for the getProgress method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetProgress() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setProgress(2);
//    assertEquals(unitUnderTest.getProgress(),2);
//  }
//  
//  /**
//   * Unit test for the setProgress method : invalid progress.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetProgresswithInvalidProgress() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setProgress(INVALID_PROGRESS);
//  }
//  
//  /**
//   * Unit test for the setProgress method : valid progress.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetProgresswithValidProgress() throws InvalidAttributeValueException
//  {
//    for (int i = -1 ; i < 3 ; i++){
//      unitUnderTest.setProgress(i);
//      assertEquals(unitUnderTest.getProgress(),i);
//    }
//  }
//  
//  /**
//   * Unit test for the setProbosedBy method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetProposedBywithInvalidName() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setProposedBy(null);
//  }
//   
//  /**
//   * Unit test for the setProbosedBy method : valid name.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetProposedBywithValidName() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setProposedBy(VALID_NAME);
//   assertEquals(unitUnderTest.getProposedBy(),VALID_NAME);
//  }
//   
//  /**
//   * Unit test for the setPerformance method : invalid performance.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetPerformancewithInvalidPerformance() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setPerformance(INVALID_PERFORMANCE);
//  }
//  
//  /**
//   * Unit test for the setPerformance method : valid performance.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetPerformancewithValidPerformance() throws InvalidAttributeValueException
//  {
//    for (int i = 0 ; i < 3 ; i++){
//      unitUnderTest.setPerformance(i);
//      assertEquals(unitUnderTest.getPerformance(),i);
//    }
//  }
//  
//  /**
//   * Unit test for the getIsArchived method.
//   * 
//   */
//  @Test
//  public void testGetIsArchived()
//  {  
//    unitUnderTest.setIsArchived(true);
//    assertEquals(unitUnderTest.getIsArchived(),true);
//  }
//  
//  /**
//   * Unit test for the setTitle method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetTitleWithNullTitle() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTitle(null);
//  }
//  
//  /**
//   * Unit test for the setTitle method : empty string.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetTitleWithEmptyString() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTitle("");
//  }
//  
//  /**
//   * Unit test for the setTitle method invalid title.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetTitleWithInvalidTitle() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTitle(INVALID_TITLE);
//  }
//  
//  /**
//   * Unit test for the setTitle method : valid title.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetTitleWithValidTitle() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTitle(VALID_TITLE);
//      assertEquals(unitUnderTest.getTitle(),VALID_TITLE);
//  }
//  
//  /**
//   * Unit test for the setDescription method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetDescriptionWithNullDescription() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setDescription(null);
//  }
//  
//  /**
//   * Unit test for the setDescription method : empty string.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetDescriptionWithEmptyString() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setDescription("");
//  }
//  
//  /**
//   * Unit test for the setDescription method : invalid description.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetDescriptionWithInvalidDescription() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setDescription(INVALID_DESCRIPTION);
//  }
//   
//  /**
//   * Unit test for the setDescription method : valid description.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetDescriptionWithValidDescription() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setDescription(VALID_DESCRIPTION);
//    assertEquals(unitUnderTest.getDescription(),VALID_DESCRIPTION);
//  }
//  
//  /**
//   * Unit test for the updateArchiveStatus method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testUpdateArchiveStatusTrue() throws InvalidAttributeValueException
//  {
//    unitUnderTest.updateArchiveStatus(true);
//    assertEquals(unitUnderTest.getIsArchived(),true);
//  }
//  
//  /**
//   * Unit test for the setTimeToCompleteBy method : empty string.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetTimeToCompleteByWithEmptyString() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTimeToCompleteBy("");
//  }
//   
//  /**
//   * Unit test for the setTimeToCompleteBy method : past date.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected= InvalidAttributeValueException.class)
//  public void testSetTimeToCompleteByWithPastDate() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTimeToCompleteBy(PAST_DATE);
//  }  
//    
//  /**
//   * Unit test for the setTimeToCompleteBy method : valid date.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetTimeToCompleteByWithValidDate() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTimeToCompleteBy(FUTURE_DATE);
//  }
//   
//  /**
//   * Unit test for the getTimeToCompleteBy method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetTimeToCompleteByWithValidDate() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTimeToCompleteBy(FUTURE_DATE);
//    assertEquals(unitUnderTest.getTimeToCompleteBy(),FUTURE_DATE);
//  }
//  
//  /**
//   * Unit test for the getTimeToCompleteByYearMonth method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetTimeToCompleteByYearMonth() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setTimeToCompleteBy(FUTURE_DATE);
//    assertEquals(unitUnderTest.getTimeToCompleteByYearMonth(),YearMonth.parse(FUTURE_DATE));
//  }
// 
//  /**
//   * Unit test for the isObjectiveValid and isObjectiveValidWithoutID  methods : valid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testIsObjectiveValidWithValidObjective() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest2.isObjectiveValid(),true);
//    assertEquals(unitUnderTest2.isObjectiveValidWithoutID(),false);
//  }
//  
//  /**
//   * Unit test for the isObjectiveValid and isObjectiveValidWithoutID methods : invalid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testIsObjectiveValidWithInvalidObjective() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.isObjectiveValid(),false);
//    assertEquals(unitUnderTest.isObjectiveValidWithoutID(),false);
//  }
//  
//  /**
//   * Unit test for the toGson method.
//   * 
//   */
//  @Test
//  public void testToGson()
//  {
//    Gson gsonData = new Gson();
//    assertEquals(unitUnderTest.toGson(),gsonData.toJson(unitUnderTest));
//  }
//   
//  /**
//   * Unit test for the toString method.
//   * 
//   */
//  @Test
//  public void testToString()
//  {
//    assertEquals(unitUnderTest2.toString(), "ID " + unitUnderTest2.getID() + "\n" + "Progress " + unitUnderTest2.getProgress() + "\n" + "Performance " + unitUnderTest2.getPerformance() + "\n"
//        + "Is Archived  " + unitUnderTest2.getIsArchived() + "\n" + "Title " + unitUnderTest2.getTitle() + "\n" + "Description " + unitUnderTest2.getDescription()
//        + "\n" + "TimeStamp " + unitUnderTest2.getTimeStamp() + "\n" + "TimeToCompleteBy " + unitUnderTest2.getTimeToCompleteBy() + "\n"
//        + "ProposedBy " + unitUnderTest2.getProposedBy() + "\n");
//  }
//}
