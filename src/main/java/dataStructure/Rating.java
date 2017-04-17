package dataStructure;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;

public class Rating implements Serializable
{
  public static final String EMPTY_STRING = "";

  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  /** int property - Represents the year of the rating. */
  private int year;

  /** String Property - Represents self evaluation of employee. */
  private String selfEvaluation;

  /** String Property - Represents the employees manager evaluation. */
  private String managerEvaluation;

  /** String Property - Represents the users score. */
  private int score;

  /**
   * Default Constructor - Responsible for initialising this object.
   */
  public Rating(int year)
  {
    this.setYear(year);
    this.setScore(0);
    this.setSelfEvaluation(EMPTY_STRING);
    this.setManagerEvaluation(EMPTY_STRING);
  }

  /** @return the year */
  public int getYear()
  {
    return year;
  }

  /** @param year The value to set. */
  public void setYear(int year)
  {
    this.year = year;
  }

  /** @return the selfEvaluation */
  public String getSelfEvaluation()
  {
    return selfEvaluation;
  }

  /** @param selfEvaluation The value to set. */
  public void setSelfEvaluation(String selfEvaluation)
  {
    this.selfEvaluation = selfEvaluation;
  }

  /** @return the managerEvaluation */
  public String getManagerEvaluation()
  {
    return managerEvaluation;
  }

  /** @param managerEvaluation The value to set. */
  public void setManagerEvaluation(String managerEvaluation)
  {
    this.managerEvaluation = managerEvaluation;
  }

  /** @return the score */
  public int getScore()
  {
    return score;
  }

  /** @param score The value to set. */
  public void setScore(int score)
  {
    this.score = score;
  }

  /**
   * Gets the rating year based on the current date. If it is the first two months of the year, the rating year is for
   * the previous year. Other wise it is the current year.
   *
   * @return the rating year.
   */
  public static int getRatingYear()
  {
    LocalDate date = LocalDate.now();

    if (date.getMonth().equals(Month.JANUARY) || date.getMonth().equals(Month.FEBRUARY)) return date.getYear() - 1;
    return date.getYear();
  }

}
