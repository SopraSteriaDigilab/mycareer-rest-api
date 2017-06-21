package dataStructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dataStructure.Objective.Progress;

public class ProgressTest
{
  @Test
  public void getProgressFromStringProposedTest()
  {
    // arrange
    final Progress expected = Progress.PROPOSED;
    final String proposed = "Proposed";

    // act
    final Progress actual = Progress.getProgressFromString(proposed);

    // assert
    assertEquals(actual, expected);
  }
  
  @Test
  public void getProgressFromStringInProgressTest()
  {
    // arrange
    final Progress expected = Progress.IN_PROGRESS;
    final String inProgress = "In-Progress";

    // act
    final Progress actual = Progress.getProgressFromString(inProgress);

    // assert
    assertEquals(actual, expected);
  }
  
  @Test
  public void getProgressFromStringCompleteTest()
  {
    // arrange
    final Progress expected = Progress.COMPLETE;
    final String complete = "Complete";

    // act
    final Progress actual = Progress.getProgressFromString(complete);

    // assert
    assertEquals(actual, expected);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void getProgressFromStringFailureTest()
  {
    // arrange
    final String invalid = "invalid";

    // act
    final Progress actual = Progress.getProgressFromString(invalid);
  }
}
