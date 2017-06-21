package dataStructure;

import static org.mockito.MockitoAnnotations.*;
import static org.junit.Assert.*;
import static model.TestModels.*;
import static utils.Utils.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class FeedbackRequestTest
{
  private static final String DEFAULT_ID = generateFeedbackRequestID(EMPLOYEE_ID);
  private static final String DEFAULT_RECIPIENT = EMAIL_ADDRESS;
  
  @InjectMocks
  private FeedbackRequest unitUnderTest;
  
  /**
   * Setup method that runs once before each test method.
   * 
   */
  @Before
  public void setup()
  {
  }
  
  @Test
  public void constructorTest()
  {
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);
    
    
  }
}
