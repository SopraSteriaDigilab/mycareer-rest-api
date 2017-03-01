package dataStructure;

import static org.mockito.MockitoAnnotations.*;
import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


public class NoteTest
{  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String PROVIDER_NAME = "alexandre brard";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final int NOTE_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String NOTE_DESCRIPTION = "note description";
   
  @Mock
  private Note noteTestEmpty;
  
  @InjectMocks
  private Note noteTest;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   noteTestEmpty = new Note();
   noteTestEmpty.setId(NOTE_ID);
   noteTestEmpty.setProviderName(PROVIDER_NAME);
   noteTestEmpty.setNoteDescription(NOTE_DESCRIPTION);   
   noteTestEmpty.setTimestamp();   
   noteTest = new Note(noteTestEmpty);
  }
   
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetID() throws InvalidAttributeValueException
  {  
    noteTest.setId(NOTE_ID);
    assertEquals(noteTest.getId(), NOTE_ID);
  }
  
  /**
   * Unit test for the setProvider method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetProviderName() throws InvalidAttributeValueException
  {  
    noteTest.setProviderName(PROVIDER_NAME);
    assertEquals(noteTest.getProviderName(), PROVIDER_NAME);
  }
  
  /**
   * Unit test for the setDescription method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetNoteDescription() throws InvalidAttributeValueException
  {  
    noteTest.setNoteDescription(NOTE_DESCRIPTION);
    assertEquals(noteTest.getNoteDescription(), NOTE_DESCRIPTION);
  }

}