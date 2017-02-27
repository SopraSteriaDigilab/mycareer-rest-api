package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ObjectiveTest
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_EMPLOYEE_ID = -675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_EMPLOYEE_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PROGRESS =3;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_NAME = "Alexandre Brard";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PERFORMANCE = 3;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_TITLE = "atitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150charactersatitlewithmorethan150characters";;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_TITLE = "a valid title";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_DESCRIPTION = "adescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000charactersadescriptionwithmorethan2000characters";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_DESCRIPTION = "a valid description";
  
  private final String PAST_DATE = "2010-01";
  
  @InjectMocks
  private Objective objectiveTest;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   objectiveTest = new Objective();
  }
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetIDwithInvalidID() throws InvalidAttributeValueException
  {  
    objectiveTest.setID(INVALID_EMPLOYEE_ID);
  }
  
  /**
   * Unit test for the setID method.
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
   * Unit test for the setProgress method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProgresswithInvalidProgress() throws InvalidAttributeValueException
  {
    objectiveTest.setProgress(INVALID_PROGRESS);
  }
  
  /**
   * Unit test for the setProgress method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetProgresswithValidProgress() throws InvalidAttributeValueException
  {
    for (int i = -1 ; i >= 2 ; i++){
      objectiveTest.setProgress(i);
      assertEquals(objectiveTest.getProgress(),i);
    }
  }
  
  /**
   * Unit test for the setProbosedBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetProposedBywithInvalidName() throws InvalidAttributeValueException
  {
    objectiveTest.setProposedBy(null);
  }
  
  /**
   * Unit test for the setProbosedBy method.
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
   * Unit test for the setPerformance method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetPerformancewithInvalidPerformance() throws InvalidAttributeValueException
  {
    objectiveTest.setPerformance(INVALID_PERFORMANCE);
  }
  
  /**
   * Unit test for the setPerformance method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetPerformancewithValidPerformance() throws InvalidAttributeValueException
  {
    for (int i = 0 ; i >= 2 ; i++){
      objectiveTest.setPerformance(i);
      assertEquals(objectiveTest.getPerformance(),i);
    }
  }
  
  /**
   * Unit test for the setTitle method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitleWithNullTitle() throws InvalidAttributeValueException
  {
    objectiveTest.setTitle(null);
  }
  
  /**
   * Unit test for the setTitle method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitleWithInvalidTitle() throws InvalidAttributeValueException
  {
    objectiveTest.setTitle(INVALID_TITLE);
  }
  
  /**
   * Unit test for the setTitle method.
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
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithNullDescription() throws InvalidAttributeValueException
  {
    objectiveTest.setDescription(null);
  }
  
  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithInvalidDescription() throws InvalidAttributeValueException
  {
    objectiveTest.setDescription(INVALID_DESCRIPTION);
  }
  
  /**
   * Unit test for the setDescription method.
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
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithEmptyString() throws InvalidAttributeValueException
  {
    objectiveTest.setTimeToCompleteBy("");
  }
  
  /**
   * Unit test for the setTimeToCompleteBy method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTimeToCompleteByWithPastDate() throws InvalidAttributeValueException
  {
    objectiveTest.setTimeToCompleteBy(PAST_DATE);
  }  
}
