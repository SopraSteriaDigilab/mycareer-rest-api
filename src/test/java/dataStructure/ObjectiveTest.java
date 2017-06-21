package dataStructure;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import dataStructure.Objective.Progress;
import model.Models;

public class ObjectiveTest
{
  private final String VALID_TITLE = "a valid title";
  private final String VALID_DESCRIPTION = "a valid description";
  private final LocalDate FUTURE_DATE = LocalDate.now().plusMonths(6);

  @InjectMocks
  private Objective unitUnderTest;

  /**
   * Setup method that runs once before each test method.
   * 
   * @throws InvalidAttributeValueException
   * 
   */
  @Before
  public void setup() throws InvalidAttributeValueException
  {
    unitUnderTest = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
  }

  @Test
  public void getCreatedOnTest()
  {
    final LocalDateTime expected = LocalDateTime.now();

    unitUnderTest = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);

    final String createdOn = unitUnderTest.getCreatedOn();
    final LocalDateTime actual = LocalDateTime.parse(createdOn);
    final boolean sameOrBefore = expected.isBefore(actual) || expected.equals(actual);
    final boolean after10Forward = expected.plusSeconds(10).isAfter(actual);

    assertTrue(sameOrBefore && after10Forward);
  }

  @Test
  public void setTitleTest() throws InterruptedException
  {
    // arrange
    final String newTitle = "new title";
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.setTitle(newTitle);
    final String after = unitUnderTest.getLastModified();

    // assert

    assertNotEquals(before, after);
    assertEquals(newTitle, unitUnderTest.getTitle());
  }

  @Test
  public void setDescriptionTest() throws InterruptedException
  {
    // arrange
    final String newDescription = "new description";
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.setDescription(newDescription);
    final String after = unitUnderTest.getLastModified();

    // assert

    assertNotEquals(before, after);
    assertEquals(newDescription, unitUnderTest.getDescription());
  }

  @Test
  public void getDueDateTest()
  {
    final String dueDate = unitUnderTest.getDueDate();
    final LocalDate actual = LocalDate.parse(dueDate);

    assertEquals(FUTURE_DATE, actual);
  }

  @Test
  public void setDueDateTest() throws InterruptedException
  {
    // arrange
    final LocalDate newDueDate = LocalDate.now().plusMonths(12);
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.setDueDate(newDueDate);
    final String after = unitUnderTest.getLastModified();
    final LocalDate actual = LocalDate.parse(unitUnderTest.getDueDate());

    // assert
    assertNotEquals(before, after);
    assertEquals(newDueDate, actual);
  }

  @Test
  public void setProposedByTest() throws InterruptedException
  {
    // arrange
    final String newProposedBy = "new entity";
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.setProposedBy(newProposedBy);
    final String after = unitUnderTest.getLastModified();

    // assert
    assertNotEquals(before, after);
    assertEquals(newProposedBy, unitUnderTest.getProposedBy());
  }

  @Test
  public void setProgressTest() throws InterruptedException
  {
    // arrange
    final Progress newProgress = Progress.COMPLETE;
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.setProgress(newProgress);
    final String after = unitUnderTest.getLastModified();

    // assert

    assertNotEquals(before, after);
    assertEquals(newProgress.getProgressStr(), unitUnderTest.getProgress());
  }

  @Test
  public void isArchivedTest() throws InterruptedException
  {
    // arrange
    final boolean newIsArchived = true;
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.isArchived(newIsArchived);
    final String after = unitUnderTest.getLastModified();

    // assert
    assertNotEquals(before, after);
    assertEquals(newIsArchived, unitUnderTest.getArchived());
  }

  @Test
  public void compareToBeforeTest()
  {
    final LocalDate beforeFutureDate = FUTURE_DATE.minusMonths(1);
    final Objective beforeObjective = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, beforeFutureDate);
    final int comparison = unitUnderTest.compareTo(beforeObjective);

    assertTrue(comparison > 0);
  }

  @Test
  public void compareToAfterTest()
  {
    final LocalDate afterFutureDate = FUTURE_DATE.plusMonths(1);
    final Objective afterObjective = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, afterFutureDate);
    final int comparison = unitUnderTest.compareTo(afterObjective);

    assertTrue(comparison < 0);
  }

  @Test
  public void compareToEqualTest()
  {
    final LocalDate sameFutureDate = FUTURE_DATE;
    final Objective sameObjective = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, sameFutureDate);
    final int comparison = unitUnderTest.compareTo(sameObjective);

    assertTrue(comparison == 0);
  }

  @Test
  public void differencesNoneTest()
  {
    final Objective otherObjective = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
    final Document expected = new Document();
    final Document actual = unitUnderTest.differences(otherObjective);

    assertEquals(expected, actual);
  }

  @Test
  public void differencesTitleTest()
  {
    final String differentTitle = "different title";
    final Objective otherObjective = new Objective(Models.DB_OBJECT_ID, differentTitle, VALID_DESCRIPTION, FUTURE_DATE);
    final Document expected = new Document("title", differentTitle);
    final Document actual = unitUnderTest.differences(otherObjective);

    assertEquals(expected, actual);
  }

  @Test
  public void differencesDescriptionTest()
  {
    final String differentDescription = "different description";
    final Objective otherObjective = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, differentDescription, FUTURE_DATE);
    final Document expected = new Document("description", differentDescription);
    final Document actual = unitUnderTest.differences(otherObjective);

    assertEquals(expected, actual);
  }

  @Test
  public void differencesDueDateTest()
  {
    final LocalDate differentDueDate = FUTURE_DATE.minusMonths(1);
    final Objective otherObjective = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, differentDueDate);
    final Document expected = new Document("dueDate", differentDueDate.toString());
    final Document actual = unitUnderTest.differences(otherObjective);

    assertEquals(expected, actual);
  }

  @Test
  public void differencesTitleDescriptionTest()
  {
    final String differentTitle = "different title";
    final String differentDescription = "different description";
    final Objective otherObjective = new Objective(Models.DB_OBJECT_ID, differentTitle, differentDescription, FUTURE_DATE);
    final Document expected = new Document("title", differentTitle).append("description", differentDescription);
    final Document actual = unitUnderTest.differences(otherObjective);

    assertEquals(expected, actual);
  }

  @Test
  public void differencesAllTest()
  {
    final String differentTitle = "different title";
    final String differentDescription = "different description";
    final LocalDate differentDueDate = FUTURE_DATE.minusMonths(1);
    final Objective otherObjective = new Objective(Models.DB_OBJECT_ID, differentTitle, differentDescription, differentDueDate);
    final Document expected = new Document("title", differentTitle).append("description", differentDescription)
        .append("dueDate", differentDueDate.toString());
    final Document actual = unitUnderTest.differences(otherObjective);

    assertEquals(expected, actual);
  }

  @Test
  public void differencesIrrelevantTest()
  {
    final int differentID = -1;
    final Objective otherObjective = new Objective(differentID, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
    final Document expected = new Document();
    final Document actual = unitUnderTest.differences(otherObjective);

    assertEquals(expected, actual);
  }

  @Test
  public void createActivityTest()
  {
    // arrange
    unitUnderTest = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
    final Action action = Action.COMPLETE;
    final EmployeeProfile profile = Models.getProfile();
    final String description = "First Last completed objective #1: a valid title";
    final String timestamp = unitUnderTest.getCreatedOn();
    final Activity expected = new Activity(description, timestamp);
    
    // act
    final Activity actual = unitUnderTest.createActivity(action, profile);

    assertEquals(expected, actual);
  }

  @Test
  public void isCurrentTrueNotCompleteTest()
  {
    assertTrue(unitUnderTest.isCurrent());
  }
  
  @Test
  public void isCurrentTrueModifiedRecentlyTest()
  {
    unitUnderTest = new Objective(Models.DB_OBJECT_ID, VALID_TITLE, VALID_DESCRIPTION, FUTURE_DATE);
    unitUnderTest.setProgress(Progress.COMPLETE);
    
    assertTrue(unitUnderTest.isCurrent());
  }
}
