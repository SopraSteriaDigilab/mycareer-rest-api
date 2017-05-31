package dataStructure;

import static dataStructure.Action.*;
import static java.time.Month.*;
import static utils.Conversions.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;

public class Rating implements Serializable
{
  private static final long serialVersionUID = 1L;

  /** TODO describe */
  public static final String YEAR = "ratings.year";

  /** TODO describe */
  public static final String SELF_EVALUATION = "ratings.selfEvaluation";

  /** TODO describe */
  public static final String MANAGER_EVALUATION = "ratings.managerEvaluation";

  /** TODO describe */
  public static final String SCORE = "ratings.score";

  /** TODO describe */
  public static final String IS_SELF_EVALUATION_SUBMITTED = "ratings.isSelfEvaluationSubmitted";

  /** TODO describe */
  public static final String IS_MANAGER_EVALUATION_SUBMITTED = "ratings.isManagerEvaluationSubmitted";

  private int year;
  private String selfEvaluation;
  private String managerEvaluation;
  private int score;
  private boolean isSelfEvaluationSubmitted;
  private boolean isManagerEvaluationSubmitted;

  /**
   * No-args Constructor - Responsible for initialising this object.
   */
  public Rating()
  {
    this.setYear(getRatingYear());
    this.setScore(0);
    this.setSelfEvaluation("");
    this.setManagerEvaluation("");
    this.setSelfEvaluationSubmitted(false);
    this.setManagerEvaluationSubmitted(false);
  }

  /**
   * Rating Constructor - Responsible for initialising this object.
   */
  public Rating(int year)
  {
    this.setYear(year);
    this.setScore(0);
    this.setSelfEvaluation("");
    this.setManagerEvaluation("");
    this.setSelfEvaluationSubmitted(false);
    this.setManagerEvaluationSubmitted(false);
  }
  
  /**
   * Creates an item of activity describing the submission of this rating's self-evaluation by the given employee.
   *
   * @param profile The profile of the employee to whom this rating belongs.
   * @return The generated activity.
   */
  public Activity createActivity(EmployeeProfile profile)
  {
    final String activityString = new StringBuilder(profile.getFullName()).append(" ").append(SUBMIT.getVerb())
        .append(" their self-evaluation.").toString();

    return new Activity(activityString, LocalDateTime.now(UK_TIMEZONE));
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

  /** @return the isSelfEvaluationSubmitted */
  public boolean isSelfEvaluationSubmitted()
  {
    return isSelfEvaluationSubmitted;
  }

  /** @param isSelfEvaluationSubmitted The value to set. */
  public void setSelfEvaluationSubmitted(boolean isSelfEvaluationSubmitted)
  {
    this.isSelfEvaluationSubmitted = isSelfEvaluationSubmitted;
  }

  /** @return the isManagerEvaluationSubmitted */
  public boolean isManagerEvaluationSubmitted()
  {
    return isManagerEvaluationSubmitted;
  }

  /** @param isManagerEvaluationSubmitted The value to set. */
  public void setManagerEvaluationSubmitted(boolean isManagerEvaluationSubmitted)
  {
    this.isManagerEvaluationSubmitted = isManagerEvaluationSubmitted;
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

    if (date.getMonth().equals(JANUARY) || date.getMonth().equals(FEBRUARY)) return date.getYear() - 1;
    return date.getYear();
  }

  /**
   * Checks whether the current moment falls within the ratings window. The ratings window runs from 1st October until
   * 28th/29th February inclusive.
   *
   * @return {@code true} if the current moment falls within the ratings window. {@code false} otherwise.
   */
  public static boolean isRatingPeriod()
  {
    final Month currentMonth = YearMonth.now().plusMonths(5).getMonth();

    return currentMonth.equals(OCTOBER) || currentMonth.equals(NOVEMBER) || currentMonth.equals(DECEMBER)
        || currentMonth.equals(JANUARY) || currentMonth.equals(FEBRUARY);
  }
}
