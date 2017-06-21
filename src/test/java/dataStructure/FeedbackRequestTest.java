package dataStructure;

import static org.mockito.MockitoAnnotations.*;
import static org.junit.Assert.*;
import static model.TestModels.*;
import static utils.Utils.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import model.TestModels;

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
    // arrange
    final LocalDateTime closeToExpectedTimestamp = LocalDateTime.now();
    final LocalDateTime afterExpectedTimestamp = closeToExpectedTimestamp.plusSeconds(10);

    // act
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);

    // assert
    final String actual = unitUnderTest.getTimestamp();
    final LocalDateTime actualTimestamp = LocalDateTime.parse(actual);

    assertEquals(unitUnderTest.getId(), DEFAULT_ID);
    assertEquals(unitUnderTest.getRecipient(), DEFAULT_RECIPIENT);
    assertFalse(unitUnderTest.isDismissed());
    assertFalse(unitUnderTest.isReplyReceived());
    assertFalse(actualTimestamp.isBefore(closeToExpectedTimestamp));
    assertTrue(actualTimestamp.isBefore(afterExpectedTimestamp));
  }

  @Test
  public void dismissTest()
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);

    // act
    unitUnderTest.dismiss();

    // assert
    assertTrue(unitUnderTest.isDismissed());
  }

  @Test
  public void dismissIdempotenceTest()
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);

    // act
    unitUnderTest.dismiss();
    unitUnderTest.dismiss();

    // assert
    assertTrue(unitUnderTest.isDismissed());
  }

  @Test
  public void createActivityTest()
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);
    final EmployeeProfile profile = newEmployeeProfile();
    final String description = "First Last requested feedback from ".concat(DEFAULT_RECIPIENT);
    final String timestamp = unitUnderTest.getTimestamp();
    final Activity expected = new Activity(description, timestamp);

    // act
    final Activity actual = unitUnderTest.createActivity(profile);

    // assert
    assertEquals(expected, actual);
  }

  @Test
  public void isCurrentTrueTest()
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);

    // act
    final boolean isCurrent = unitUnderTest.isCurrent();

    // assert
    assertTrue(isCurrent);
  }

  @Test
  public void isCurrentReplyReceivedFalseTest()
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);
    unitUnderTest.setReplyReceived(true);
    // act
    final boolean isCurrent = unitUnderTest.isCurrent();

    // assert
    assertFalse(isCurrent);
  }

  @Test
  public void isCurrentDismissedFalseTest()
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);
    unitUnderTest.dismiss();
    // act
    final boolean isCurrent = unitUnderTest.isCurrent();

    // assert
    assertFalse(isCurrent);
  }

  @Test
  public void compareToEqual()
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);

    // act
    final int comparison = unitUnderTest.compareTo(unitUnderTest);

    // assert
    assertTrue(comparison == 0);
  }

  @Test
  public void compareToLessThan() throws InterruptedException
  {
    // arrange
    final FeedbackRequest other = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);
    Thread.sleep(1);
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);

    // act
    final int comparison = unitUnderTest.compareTo(other);

    // assert
    assertTrue(comparison > 0);
  }

  @Test
  public void compareToGreaterThan() throws InterruptedException
  {
    // arrange
    unitUnderTest = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);
    Thread.sleep(1);
    final FeedbackRequest other = new FeedbackRequest(DEFAULT_ID, DEFAULT_RECIPIENT);

    // act
    final int comparison = unitUnderTest.compareTo(other);

    // assert
    assertTrue(comparison < 0);
  }
}
