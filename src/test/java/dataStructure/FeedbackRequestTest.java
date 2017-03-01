package dataStructure;

import static org.mockito.MockitoAnnotations.*;
import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

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
  private FeedbackRequest fbkReq, fbkReqEmpty;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
   initMocks(this);
   fbkReq = new FeedbackRequest(VALID_ID, VALID_RECIPIENT);
   fbkReqEmpty = new FeedbackRequest();
  }
  
  
  /**
   * Unit test for the setID method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetID() throws InvalidAttributeValueException
  {  
    fbkReq.setId(VALID_ID);
    assertEquals(fbkReq.getId(), VALID_ID);
  }
  
  /**
   * Unit test for the setRecipient method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetRecipient() throws InvalidAttributeValueException
  {  
    fbkReq.setRecipient(VALID_RECIPIENT);
    assertEquals(fbkReq.getRecipient(), VALID_RECIPIENT);
  }
  
  /**
   * Unit test for the ReplyReceived method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testReplyReceived() throws InvalidAttributeValueException
  {  
    fbkReq.setReplyReceived(true);
    assertEquals(fbkReq.isReplyReceived(), true);
  }
}