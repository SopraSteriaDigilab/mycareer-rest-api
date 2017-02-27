package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.*;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class DevelopmentNeedTest
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_ID = -675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PROGRESS =3;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_TITLE ="atitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150characters";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_TITLE ="a valid title";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_CATEGORY =6;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_DESCRIPTION = "adescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000characters";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_DESCRIPTION = "a valid description";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String PAST_DATE = "2010-01";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String FUTURE_DATE = "3010-01";
  
  @InjectMocks
  private DevelopmentNeed devNeed;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   devNeed = new DevelopmentNeed();
  }
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetIDwithInvalidID() throws InvalidAttributeValueException
  {  
    devNeed.setID(INVALID_ID);
  }
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetIDwithValidID() throws InvalidAttributeValueException
  {  
    devNeed.setID(VALID_ID);
    assertEquals(devNeed.getID(),VALID_ID);
  }
  
  /**
   * Unit test for the setProgress method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProgressWithInvalidProgress() throws InvalidAttributeValueException
  {  
    devNeed.setProgress(INVALID_PROGRESS);
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
      devNeed.setProgress(i);
      assertEquals(devNeed.getProgress(), i);   
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
    devNeed.setCategory(INVALID_CATEGORY);
  }
  
  /**
   * Unit test for the setCategory method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetCategoryWithValidCategory() throws InvalidAttributeValueException
  {  
    for (int i =0; i<=5; i++){
      devNeed.setCategory(i);
      assertEquals(devNeed.getCategory(), i);   
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
    devNeed.setTitle(null);
  }
  
  /**
   * Unit test for the setTitle method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitlewithInvalidTitle() throws InvalidAttributeValueException
  {
    devNeed.setTitle(INVALID_TITLE);
  }
  
  /**
   * Unit test for the setTitle method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTitleWithValidTitle() throws InvalidAttributeValueException
  {
    devNeed.setTitle(VALID_TITLE);
    assertEquals(devNeed.getTitle(), VALID_TITLE);
  }


  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithNullDescription() throws InvalidAttributeValueException
  {
    devNeed.setDescription(null);
  }
  
  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithInvalidDescription() throws InvalidAttributeValueException
  {
    devNeed.setDescription(INVALID_DESCRIPTION);
  }
  
  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetDescriptionWithValidDescription() throws InvalidAttributeValueException
  {
    devNeed.setDescription(VALID_DESCRIPTION);
      assertEquals(devNeed.getDescription(),VALID_DESCRIPTION);
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test (expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithEmptyString() throws InvalidAttributeValueException
  {
    devNeed.setTimeToCompleteBy("");
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test (expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithPastDate() throws InvalidAttributeValueException
  {
    devNeed.setTimeToCompleteBy(PAST_DATE);
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTimeToCompleteByWithFutureDate() throws InvalidAttributeValueException
  {
    devNeed.setTimeToCompleteBy(FUTURE_DATE);
    assertEquals(devNeed.getTimeToCompleteBy(),FUTURE_DATE);
  }
}