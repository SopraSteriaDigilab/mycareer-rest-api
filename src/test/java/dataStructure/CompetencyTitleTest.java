package dataStructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dataStructure.Competency.CompetencyTitle;

public class CompetencyTitleTest
{
  @Test
  public void getCompetencyTitleFromStringAccountabilityTest()
  {
    // arrange
    final CompetencyTitle expected = CompetencyTitle.ACCOUNTABILITY;
    final String accountability = "Accountability";

    // act
    CompetencyTitle actual = CompetencyTitle.getCompetencyTitleFromString(accountability);

    // assert
    assertEquals(actual, expected);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void getCompetencyTitleFromStringFailureTest()
  {
    final String invalid = "invalid";

    // act
    CompetencyTitle actual = CompetencyTitle.getCompetencyTitleFromString(invalid);
  }
}
