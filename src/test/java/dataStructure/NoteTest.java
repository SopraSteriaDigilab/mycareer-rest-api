
//  TODO Commented code to be reviewed
  
//package dataStructure;
//
//import static org.mockito.MockitoAnnotations.*;
//import static dataStructure.Constants.UK_TIMEZONE;
//import static org.junit.Assert.*;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//
//import javax.management.InvalidAttributeValueException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//
//public class NoteTest
//{  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String PROVIDER_NAME = "alexandre brard";
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int NOTE_ID = 675590;
//  
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String NOTE_DESCRIPTION = "note description";
//   
//  @Mock
//  private Note unitUnderTestEmpty;
//  
//  @InjectMocks
//  private Note unitUnderTest;
//  
//  /**
//   * Setup method that runs once before each test method.
//   * 
//   */
//  @Before
//  public void setup()
//  {
//   initMocks(this);
//   unitUnderTestEmpty = new Note();
//   unitUnderTestEmpty.setId(NOTE_ID);
//   unitUnderTestEmpty.setProviderName(PROVIDER_NAME);
//   unitUnderTestEmpty.setNoteDescription(NOTE_DESCRIPTION);   
//   unitUnderTestEmpty.setTimestamp();
//  }
//   
//  
//  /**
//   * Unit test for the setID method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetID() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setId(NOTE_ID);
//    assertEquals(unitUnderTest.getId(), NOTE_ID);
//  }
//  
//  /**
//   * Unit test for the setProvider method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetProviderName() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setProviderName(PROVIDER_NAME);
//    assertEquals(unitUnderTest.getProviderName(), PROVIDER_NAME);
//  }
//  
//  /**
//   * Unit test for the setDescription method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetNoteDescription() throws InvalidAttributeValueException
//  {  
//    unitUnderTest.setNoteDescription(NOTE_DESCRIPTION);
//    assertEquals(unitUnderTest.getNoteDescription(), NOTE_DESCRIPTION);
//  }
//  
//  /**
//   * Unit test for the getTimestamp method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetTimestamp() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.getTimestamp(),LocalDateTime.now(UK_TIMEZONE).toString());
//  }
//
//}