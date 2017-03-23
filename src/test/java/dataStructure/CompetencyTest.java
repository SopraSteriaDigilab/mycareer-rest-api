package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.management.InvalidAttributeValueException;
import java.lang.ArrayIndexOutOfBoundsException;



import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;


/**
 * Unit tests for the CompetencyTest class.
 *
 */
public class CompetencyTest
{
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_ID = -675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int VALID_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PROGRESS =3;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_NAME = "Alexandre Brard";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int INVALID_PERFORMANCE = 3;
 
  @InjectMocks
  private Competency_OLD unitUnderTest, unitUnderTest2;
    
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   unitUnderTest = new Competency_OLD();
   unitUnderTest2 = new Competency_OLD(VALID_ID , true);
  }
  
  /**
   * Unit test for the setID method : invalid ID.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetIDwithInvalidID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setID(INVALID_ID);
  }
   
  /**
   * Unit test for the setID method : valid ID.
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
   * Unit test for the setTitle method invalid title.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTitleWithInvalidTitle() throws InvalidAttributeValueException
  {
    unitUnderTest.setTitle(INVALID_ID);
  }
  
  /**
   * Unit test for the setTitle method : valid title.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTitleWithValidTitle() throws InvalidAttributeValueException
  {
    for(int i=0; i<Constants.COMPETENCY_NAMES.length; i++){
      unitUnderTest.setTitle(i);
      assertTrue(unitUnderTest.getTitle()==Constants.COMPETENCY_NAMES[i]);
    }
  }
  
  /**
   * Unit test for the setDescription method : invalid title.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetDescriptionWithInvalidTitle() throws InvalidAttributeValueException
  {
      unitUnderTest.setDescription(INVALID_ID);
  }
  
  /**
   * Unit test for the setDescription method : valid title.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetDescriptionWithValidTitle() throws InvalidAttributeValueException
  {
    for(int i = 0 ; i < Constants.COMPETENCY_NAMES.length ; i++){
      unitUnderTest.setDescription(i);
      assertTrue(Constants.COMPETENCY_DESCRIPTIONS[i]==unitUnderTest.getCompentencyDescription());
    }
  }
  
  /**
   * Unit test for the isValid method : invalid competency.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testIsValidWithInvalidCompetency() throws InvalidAttributeValueException
  {
    assertEquals(unitUnderTest.isValid(),false);
  }
  
  /**
   * Unit test for the isValid method : valid competency.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testIsValidWithValidCompetency() throws InvalidAttributeValueException
  {
    assertEquals(unitUnderTest2.isValid(),true);
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
    int index=5;
    assertEquals(unitUnderTest2.toString(index), "ID: " + unitUnderTest2.getID() + "\n"+ "Is Selected: " + unitUnderTest2.getIsSelected() + "\n" + "Title: " + Constants.COMPETENCY_NAMES[index] + "\n" + "Description: " + Constants.COMPETENCY_DESCRIPTIONS[index] + "\n" + "Time Stamp: " + unitUnderTest2.getTimeStamp() + "\n"); 
   }
}