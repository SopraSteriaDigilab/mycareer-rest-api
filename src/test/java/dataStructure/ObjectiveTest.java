package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.time.YearMonth;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;


import com.google.gson.Gson;

public class ObjectiveTest
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_EMPLOYEE_ID = -675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_EMPLOYEE_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PROGRESS =3;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_PROGRESS =2;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_NAME = "Alexandre Brard";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PERFORMANCE = 3;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_PERFORMANCE =2;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_TITLE = "atitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150characters";;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_TITLE = "a valid title";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_DESCRIPTION = "adescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000characters";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_DESCRIPTION = "a valid description";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String PAST_DATE = "2010-01";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String FUTURE_DATE = "3010-01";
  
  @InjectMocks
  private Objective objectiveTest, objectiveTest2, objectiveTest3, objectiveTest4;
  
  /**
   * Setup method that runs once before each test method.
   * @throws InvalidAttributeValueException 
   * 
   */
  @Before
  public void setup() throws InvalidAttributeValueException
  {
   initMocks(this);
   objectiveTest = new Objective();
   objectiveTest2 = new Objective(VALID_EMPLOYEE_ID, VALID_PROGRESS, VALID_PERFORMANCE, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
   objectiveTest3 = new Objective(objectiveTest2);
   objectiveTest4 = new Objective(VALID_PROGRESS, VALID_PERFORMANCE, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
  }
  
  /**
   * Unit test for the setID method : Invalid ID
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetIDwithInvalidID() throws InvalidAttributeValueException
  {  
    objectiveTest.setID(INVALID_EMPLOYEE_ID);
  }
      
  /**
   * Unit test for the setID method : Valid ID.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetIDwithValidID() throws InvalidAttributeValueException
  {  
    objectiveTest.setID(VALID_EMPLOYEE_ID);
    assertEquals(objectiveTest.getID(),VALID_EMPLOYEE_ID);
  }
  
  /**
   * Unit test for the getProgress method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetProgress() throws InvalidAttributeValueException
  {  
    objectiveTest.setProgress(2);
    assertEquals(objectiveTest.getProgress(),2);
  }
  
  /**
   * Unit test for the setProgress method : invalid progress.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProgresswithInvalidProgress() throws InvalidAttributeValueException
  {
    objectiveTest.setProgress(INVALID_PROGRESS);
  }
  
  /**
   * Unit test for the setProgress method : valid progress.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetProgresswithValidProgress() throws InvalidAttributeValueException
  {
    for (int i = -1 ; i < 3 ; i++){
      objectiveTest.setProgress(i);
      assertEquals(objectiveTest.getProgress(),i);
    }
  }
  
  /**
   * Unit test for the setProbosedBy method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProposedBywithInvalidName() throws InvalidAttributeValueException
  {
    objectiveTest.setProposedBy(null);
  }
   
  /**
   * Unit test for the setProbosedBy method : valid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetProposedBywithValidName() throws InvalidAttributeValueException
  {
    objectiveTest.setProposedBy(VALID_NAME);
   assertEquals(objectiveTest.getProposedBy(),VALID_NAME);
  }
   
  /**
   * Unit test for the setPerformance method : invalid performance.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetPerformancewithInvalidPerformance() throws InvalidAttributeValueException
  {
    objectiveTest.setPerformance(INVALID_PERFORMANCE);
  }
  
  /**
   * Unit test for the setPerformance method : valid performance.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetPerformancewithValidPerformance() throws InvalidAttributeValueException
  {
    for (int i = 0 ; i < 3 ; i++){
      objectiveTest.setPerformance(i);
      assertEquals(objectiveTest.getPerformance(),i);
    }
  }
  
  /**
   * Unit test for the getIsArchived method.
   * 
   */
  @Test
  public void testGetIsArchived()
  {  
    objectiveTest.setIsArchived(true);
    assertEquals(objectiveTest.getIsArchived(),true);
  }
  
  /**
   * Unit test for the setTitle method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitleWithNullTitle() throws InvalidAttributeValueException
  {
    objectiveTest.setTitle(null);
  }
  
  /**
   * Unit test for the setTitle method : empty string.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitleWithEmptyString() throws InvalidAttributeValueException
  {
    objectiveTest.setTitle("");
  }
  
  /**
   * Unit test for the setTitle method invalid title.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitleWithInvalidTitle() throws InvalidAttributeValueException
  {
    objectiveTest.setTitle(INVALID_TITLE);
  }
  
  /**
   * Unit test for the setTitle method : valid title.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTitleWithValidTitle() throws InvalidAttributeValueException
  {
    objectiveTest.setTitle(VALID_TITLE);
      assertEquals(objectiveTest.getTitle(),VALID_TITLE);
  }
  
  /**
   * Unit test for the setDescription method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithNullDescription() throws InvalidAttributeValueException
  {
    objectiveTest.setDescription(null);
  }
  
  /**
   * Unit test for the setDescription method : empty string.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithEmptyString() throws InvalidAttributeValueException
  {
    objectiveTest.setDescription("");
  }
  
  /**
   * Unit test for the setDescription method : invalid description.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithInvalidDescription() throws InvalidAttributeValueException
  {
    objectiveTest.setDescription(INVALID_DESCRIPTION);
  }
   
  /**
   * Unit test for the setDescription method : valid description.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetDescriptionWithValidDescription() throws InvalidAttributeValueException
  {
    objectiveTest.setDescription(VALID_DESCRIPTION);
    assertEquals(objectiveTest.getDescription(),VALID_DESCRIPTION);
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method : empty string.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithEmptyString() throws InvalidAttributeValueException
  {
    objectiveTest.setTimeToCompleteBy("");
  }
   
  /**
   * Unit test for the setTimeToCompleteBy method : past date.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithPastDate() throws InvalidAttributeValueException
  {
    objectiveTest.setTimeToCompleteBy(PAST_DATE);
  }  
    
  /**
   * Unit test for the setTimeToCompleteBy method : valid date.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTimeToCompleteByWithValidDate() throws InvalidAttributeValueException
  {
    objectiveTest.setTimeToCompleteBy(FUTURE_DATE);
  }
   
  /**
   * Unit test for the getTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetTimeToCompleteByWithValidDate() throws InvalidAttributeValueException
  {
    objectiveTest.setTimeToCompleteBy(FUTURE_DATE);
    assertEquals(objectiveTest.getTimeToCompleteBy(),FUTURE_DATE);
  }
    
  /**
   * Unit test for the getTimeToCompleteByYearMonth method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetTimeToCompleteByYearMonth() throws InvalidAttributeValueException
  {
    objectiveTest.setTimeToCompleteBy(FUTURE_DATE);
    assertEquals(objectiveTest.getTimeToCompleteByYearMonth(),YearMonth.parse(FUTURE_DATE));
  }
   
  /**
   * Unit test for the toGson method.
   * 
   */
  @Test
  public void testToGson()
  {
    Gson gsonData = new Gson();
    assertEquals(objectiveTest.toGson(),gsonData.toJson(objectiveTest));
  }
   
  /**
   * Unit test for the toString method.
   * 
   */
  @Test
  public void testToString()
  {
    assertEquals(objectiveTest2.toString(), "ID " + objectiveTest2.getID() + "\n" + "Progress " + objectiveTest2.getProgress() + "\n" + "Performance " + objectiveTest2.getPerformance() + "\n"
        + "Is Archived  " + objectiveTest2.getIsArchived() + "\n" + "Title " + objectiveTest2.getTitle() + "\n" + "Description " + objectiveTest2.getDescription()
        + "\n" + "TimeStamp " + objectiveTest2.getTimeStamp() + "\n" + "TimeToCompleteBy " + objectiveTest2.getTimeToCompleteBy() + "\n"
        + "ProposedBy " + objectiveTest2.getProposedBy() + "\n");
  }
}
