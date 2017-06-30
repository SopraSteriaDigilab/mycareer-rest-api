package dataStructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dataStructure.DevelopmentNeed.Category;

public class CategoryTest
{
  @Test
  public void getCategoryFromStringJobTrainingTest()
  {
    // arrange
    final Category expected = Category.JOB_TRAINING;
    final String jobTraining = "On Job Training";

    // act
    final Category actual = Category.getCategoryFromString(jobTraining);

    // assert
    assertEquals(actual, expected);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void getCategoryFromStringFailureTest()
  {
    // arrange
    final String invalid = "invalid";

    // act
    Category.getCategoryFromString(invalid);
  }
}
