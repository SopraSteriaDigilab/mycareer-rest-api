package dataStructure;

import static utils.Conversions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static dataStructure.Employee.*;

import java.time.LocalDateTime;
import java.util.Date;

import org.bson.Document;
import org.junit.Test;

public class ActivityTest
{
  private static final String DEFAULT_DESCRIPTION = "First Last added objective #1: obj1";
  private static final LocalDateTime DEFAULT_TIMESTAMP_LDT = LocalDateTime.of(2017, 1, 1, 12, 0);
  private static final Date DEFAULT_TIMESTAMP = localDateTimeToDate(DEFAULT_TIMESTAMP_LDT);
  private static final String DEFAULT_TIMESTAMP_STRING = DEFAULT_TIMESTAMP_LDT.toString();

  private Activity unitUnderTest;

  @Test
  public void constructorStringDateTest()
  {
    // arrange

    // act
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP);

    // assert
    assertEquals(unitUnderTest.getDescription(), DEFAULT_DESCRIPTION);
    assertEquals(unitUnderTest.getTimestamp(), DEFAULT_TIMESTAMP_STRING);
  }

  @Test
  public void constructorStringLocalDateTimeTest()
  {
    // arrange

    // act
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP_LDT);

    // assert
    assertEquals(unitUnderTest.getDescription(), DEFAULT_DESCRIPTION);
    assertEquals(unitUnderTest.getTimestamp(), DEFAULT_TIMESTAMP_STRING);
  }

  @Test
  public void constructorStringStringTest()
  {
    // arrange

    // act
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP_STRING);

    // assert
    assertEquals(unitUnderTest.getDescription(), DEFAULT_DESCRIPTION);
    assertEquals(unitUnderTest.getTimestamp(), DEFAULT_TIMESTAMP_STRING);
  }

  @Test
  public void ofDocumentSuccessTest()
  {
    // arrange
    final Document mockDocument = mock(Document.class);
    final Document mockActivityDocument = mock(Document.class);
    when(mockDocument.get(ACTIVITY_FEED, Document.class)).thenReturn(mockActivityDocument);
    when(mockActivityDocument.getString("description")).thenReturn(DEFAULT_DESCRIPTION);
    when(mockActivityDocument.getDate("timestamp")).thenReturn(DEFAULT_TIMESTAMP);

    // act
    unitUnderTest = Activity.ofDocument(mockDocument);

    // assert
    assertEquals(unitUnderTest.getDescription(), DEFAULT_DESCRIPTION);
    assertEquals(unitUnderTest.getTimestamp(), DEFAULT_TIMESTAMP_STRING);
  }

  @Test
  public void toDocumentTest()
  {
    // arrange
    final Document expected = new Document("description", DEFAULT_DESCRIPTION).append("timestamp", DEFAULT_TIMESTAMP);
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP_LDT);
    
    // act
    final Document actual = unitUnderTest.toDocument();
    
    // assert
    assertEquals(actual, expected);
  }
  
  @Test
  public void equalsTrueSameObjectTest()
  {
    // arrange
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP);
    final Activity other = unitUnderTest;
    
    // act
    final boolean equals = unitUnderTest.equals(other);
    
    // assert
    assertTrue(equals);
  }
  
  @Test
  public void equalsTrueDifferentObjectTest()
  {
    // arrange
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP);
    final Activity other = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP);
    
    // act
    final boolean equals = unitUnderTest.equals(other);
    
    // assert
    assertTrue(equals);
  }
  
  @Test
  public void equalsFalseNotAnActivityTest()
  {
    // arrange
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP);
    final String other = "";
    
    // act
    final boolean equals = unitUnderTest.equals(other);
    
    // assert
    assertFalse(equals);
  }
  
  @Test
  public void equalsFalseDifferentDescriptionTest()
  {
    // arrange
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP);
    final Activity other = new Activity("different description", DEFAULT_TIMESTAMP);
    
    // act
    final boolean equals = unitUnderTest.equals(other);
    
    // assert
    assertFalse(equals);
  }
  
  @Test
  public void equalsFalseDifferentTimestampTest()
  {
    // arrange
    unitUnderTest = new Activity(DEFAULT_DESCRIPTION, DEFAULT_TIMESTAMP);
    final Activity other = new Activity(DEFAULT_DESCRIPTION, LocalDateTime.now());
    
    // act
    final boolean equals = unitUnderTest.equals(other);
    
    // assert
    assertFalse(equals);
  }
}
