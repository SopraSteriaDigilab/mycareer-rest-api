package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.*;

import java.time.YearMonth;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.google.gson.Gson;

public class DevelopmentNeedTest
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_ID = -675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PROGRESS =3;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_PROGRESS =2;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_TITLE ="atitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150characters";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_TITLE ="a valid title";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_CATEGORY =6;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_CATEGORY =2;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_DESCRIPTION = "adescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000characters";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_DESCRIPTION = "a valid description";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String PAST_DATE = "2010-01";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String FUTURE_DATE = "3010-01";
  
  @InjectMocks
  private DevelopmentNeed_OLD unitUnderTest, unitUnderTest2, unitUnderTest3;
  
  /**
   * Setup method that runs once before each test method.
   * @throws InvalidAttributeValueException 
   * 
   */ 
  @Before
  public void setup() throws InvalidAttributeValueException
  {
   initMocks(this);
   unitUnderTest = new DevelopmentNeed_OLD();
   unitUnderTest2 = new DevelopmentNeed_OLD(VALID_ID,VALID_PROGRESS,VALID_CATEGORY,VALID_TITLE,VALID_DESCRIPTION);
   unitUnderTest3 = new DevelopmentNeed_OLD(VALID_ID,VALID_PROGRESS,VALID_CATEGORY,VALID_TITLE,VALID_DESCRIPTION,FUTURE_DATE);
  }
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetIDwithInvalidID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setID(INVALID_ID);
  }
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetIDwithValidID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setID(VALID_ID);
    assertEquals(unitUnderTest.getID(),VALID_ID);
  }
  
  /**
   * Unit test for the setProgress method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProgressWithInvalidProgress() throws InvalidAttributeValueException
  {  
    unitUnderTest.setProgress(INVALID_PROGRESS);
  }
  
  /**
   * Unit test for the setProgress method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetProgressWithValidProgress() throws InvalidAttributeValueException
  {  
    for (int i =0; i<=2; i++){
      unitUnderTest.setProgress(i);
      assertEquals(unitUnderTest.getProgress(), i);   
    }
  }
  
  /**
   * Unit test for the setCategory method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetCategoryWithInvalidCategory() throws InvalidAttributeValueException
  {  
    unitUnderTest.setCategory(INVALID_CATEGORY);
  }
  
  /**
   * Unit test for the setCategory method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetCategoryWithValidCategory() throws InvalidAttributeValueException
  {  
    for (int i =0; i<=4; i++){
      unitUnderTest.setCategory(i);
      assertEquals(unitUnderTest.getCategory(), i);   
    }
  }
  
  /**
   * Unit test for the setTitle method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitlewithNull() throws InvalidAttributeValueException
  {
    unitUnderTest.setTitle(null);
  }
  
  /**
   * Unit test for the setTitle method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitlewithInvalidTitle() throws InvalidAttributeValueException
  {
    unitUnderTest.setTitle(INVALID_TITLE);
  }
  
  /**
   * Unit test for the setTitle method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTitleWithValidTitle() throws InvalidAttributeValueException
  {
    unitUnderTest.setTitle(VALID_TITLE);
    assertEquals(unitUnderTest.getTitle(), VALID_TITLE);
  }


  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithNullDescription() throws InvalidAttributeValueException
  {
    unitUnderTest.setDescription(null);
  }
  
  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithInvalidDescription() throws InvalidAttributeValueException
  {
    unitUnderTest.setDescription(INVALID_DESCRIPTION);
  }
  
  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetDescriptionWithValidDescription() throws InvalidAttributeValueException
  {
    unitUnderTest.setDescription(VALID_DESCRIPTION);
      assertEquals(unitUnderTest.getDescription(),VALID_DESCRIPTION);
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test (expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithEmptyString() throws InvalidAttributeValueException
  {
    unitUnderTest.setTimeToCompleteBy("");
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test (expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithPastDate() throws InvalidAttributeValueException
  {
    unitUnderTest.setTimeToCompleteBy(PAST_DATE);
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTimeToCompleteByWithFutureDate() throws InvalidAttributeValueException
  {
    unitUnderTest.setTimeToCompleteBy(FUTURE_DATE);
    assertEquals(unitUnderTest.getTimeToCompleteBy(),FUTURE_DATE);
  }
  
  /**
   * Unit test for the getTimeToCompleteByYearMonth method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetTimeToCompleteByYearMonth() throws InvalidAttributeValueException
  {
    unitUnderTest.setTimeToCompleteBy(FUTURE_DATE);
    assertEquals(unitUnderTest.getTimeToCompleteByYearMonth(),YearMonth.parse(FUTURE_DATE));
  }
  
  /**
   * Unit test for the isDevelopmentNeedValid : invalid development need.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testIsDevelopmentNeedValidWithInvalidDevelopmentNeed() throws InvalidAttributeValueException
  {
    assertEquals(unitUnderTest.isDevelopmentNeedValid(),false);
  }
  
  /**
   * Unit test for the isDevelopmentNeedValid : valid development need.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testIsDevelopmentNeedValidWithValidDevelopmentNeed() throws InvalidAttributeValueException
  {
    assertEquals(unitUnderTest2.isDevelopmentNeedValid(),true);
  }
  
  /**
   * Unit test for the toGson method.
   * 
   */
  @Test
  public void testToGson()
  {
    Gson gsonData = new Gson();
    assertEquals(unitUnderTest.toGson(),gsonData.toJson(unitUnderTest));
  }
   
  /**
   * Unit test for the toString method.
   * 
   */
  @Test
  public void testToString()
  {
    assertEquals(unitUnderTest2.toString(), "ID " + unitUnderTest2.getID() + "\n" + "Category " + unitUnderTest2.getCategory() + "\n" + "Title " + unitUnderTest2.getTitle() + "\n" + "Description "
        + unitUnderTest2.getDescription() + "\n" + "TimeStamp " + unitUnderTest2.getTimeStamp() + "\n" + "TimeToCompleteBy "
        + unitUnderTest2.getTimeToCompleteBy() + "\n"); 
  }
}