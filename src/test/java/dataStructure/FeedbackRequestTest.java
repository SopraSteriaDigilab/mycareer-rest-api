package dataStructure;

import static org.mockito.MockitoAnnotations.*;
import static dataStructure.Constants.UK_TIMEZONE;
import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;


public class FeedbackRequestTest
{  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_RECIPIENT = "alexandre.brard@soprasteria.com";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_ID = "675590";
  
  @InjectMocks
  private FeedbackRequest unitUnderTest, unitUnderTestEmpty;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   unitUnderTest = new FeedbackRequest(VALID_ID, VALID_RECIPIENT);
   unitUnderTestEmpty = new FeedbackRequest();
  }
  
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setId(VALID_ID);
    assertEquals(unitUnderTest.getId(), VALID_ID);
  }
  
  /**
   * Unit test for the setRecipient method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetRecipient() throws InvalidAttributeValueException
  {  
    unitUnderTest.setRecipient(VALID_RECIPIENT);
    assertEquals(unitUnderTest.getRecipient(), VALID_RECIPIENT);
  }
  
  /**
   * Unit test for the ReplyReceived method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testReplyReceived() throws InvalidAttributeValueException
  {  
    unitUnderTest.setReplyReceived(true);
    assertEquals(unitUnderTest.isReplyReceived(), true);
  }
  
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
}