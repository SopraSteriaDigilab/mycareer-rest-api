package dataStructure;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.InjectMocks;

import dataStructure.Competency.CompetencyTitle;

/**
 * Unit tests for the CompetencyTest class.
 *
 */
public class CompetencyTest
{
  private static final CompetencyTitle DEFAULT_TITLE = CompetencyTitle.ACCOUNTABILITY;
  private static final int DEFAULT_ID = 0;

  @InjectMocks
  private Competency unitUnderTest;

  @Test
  public void constructorTest()
  {
    // arrange + act
    final Competency unitUnderTest = new Competency(DEFAULT_ID, DEFAULT_TITLE);

    // assert
    assertEquals(unitUnderTest.getTitle(), DEFAULT_TITLE.getCompetencyTitleStr());
    assertEquals(unitUnderTest.isSelected(), false);
  }

  @Test
  public void setTitleTest() throws InterruptedException
  {
    // arrange
    final Competency unitUnderTest = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    final CompetencyTitle newTitle = CompetencyTitle.BUSINESS_AWARENESS;
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.setTitle(newTitle);
    final String after = unitUnderTest.getLastModified();

    // assert
    assertNotEquals(after, before);
    assertEquals(unitUnderTest.getTitle(), newTitle.getCompetencyTitleStr());
  }

  @Test
  public void setSelectedTest() throws InterruptedException
  {
    // arrange
    final Competency unitUnderTest = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    final boolean newSelected = true;
    final String before = unitUnderTest.getLastModified();
    Thread.sleep(1);

    // act
    unitUnderTest.setSelected(newSelected);
    final String after = unitUnderTest.getLastModified();

    // assert
    assertNotEquals(after, before);
    assertEquals(unitUnderTest.isSelected(), newSelected);
  }

  @Test
  public void compareToEqualFalseTest()
  {
    // arrange
    final Competency unitUnderTest = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    final Competency other = new Competency(DEFAULT_ID, DEFAULT_TITLE);

    // act
    final int comparison = unitUnderTest.compareTo(other);

    // assert
    assertTrue(comparison == 0);
  }

  @Test
  public void compareToEqualTrueTest()
  {
    // arrange
    final Competency unitUnderTest = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    final Competency other = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    unitUnderTest.setSelected(true);
    other.setSelected(true);

    // act
    final int comparison = unitUnderTest.compareTo(other);

    // assert
    assertTrue(comparison == 0);
  }

  @Test
  public void compareToLessThanTest()
  {
    // arrange
    final Competency unitUnderTest = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    final Competency other = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    unitUnderTest.setSelected(true);

    // act
    final int comparison = unitUnderTest.compareTo(other);

    // assert
    assertTrue(comparison < 0);
  }

  @Test
  public void compareToGreaterThanTest()
  {
    // arrange
    final Competency unitUnderTest = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    final Competency other = new Competency(DEFAULT_ID, DEFAULT_TITLE);
    other.setSelected(true);

    // act
    final int comparison = unitUnderTest.compareTo(other);

    // assert
    assertTrue(comparison > 0);
  }
}