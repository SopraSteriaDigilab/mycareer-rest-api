package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;

import javax.management.InvalidAttributeValueException;
import org.mongodb.morphia.annotations.Embedded;
import com.google.gson.Gson;

/**
 * This class contains the definition of the DevelopmentNeed object
 *
 */
@Embedded
public class DevelopmentNeed implements Serializable
{

  private static final long serialVersionUID = -5067508122602507151L;
  // Global Variables
  private int id, progress, category;
  private String title, description, timeStamp, timeToCompleteBy;

  // Empty Constructor
  public DevelopmentNeed()
  {
    this.id = Constants.INVALID_INT;
    this.progress = Constants.INVALID_INT;
    this.category = Constants.INVALID_INT;
    this.title = Constants.INVALID_STRING;
    this.description = Constants.INVALID_STRING;
    this.timeStamp = null;
    this.timeToCompleteBy = "";
  }

  // Constructor with parameters
  public DevelopmentNeed(int id, int progress, int cat, String title, String description)
      throws InvalidAttributeValueException
  {
    this.setID(id);
    this.setProgress(progress);
    this.setCategory(cat);
    this.setTitle(title);
    this.setDescription(description);
    this.timeStamp = null;
    this.setTimeStamp();
    this.timeToCompleteBy = Constants.COMPLETE_DATE_NOT_SET;
  }

  // Constructor with parameters
  public DevelopmentNeed(int id, int progress, int cat, String title, String description, String completeBy)
      throws InvalidAttributeValueException
  {
    this.setID(id);
    this.setProgress(progress);
    this.setCategory(cat);
    this.setTitle(title);
    this.setDescription(description);
    this.timeStamp = null;
    this.setTimeStamp();
    this.setTimeToCompleteBy(completeBy);
  }

  // Getters and Setters

  public void setID(int id) throws InvalidAttributeValueException
  {
    if (id > 0) this.id = id;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERID);
  }

  public int getID()
  {
    return this.id;
  }

  /**
   * 
   * @param progress This variable can assume only 4 values: -1 => Deleted 0 => Proposed 1 => Started 2 => Completed
   */
  public void setProgress(int progress) throws InvalidAttributeValueException
  {
    if (progress >= -0 && progress <= 2) this.progress = progress;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_PROGRESS);
  }

  public int getProgress()
  {
    return this.progress;
  }

  public void setCategory(int cat) throws InvalidAttributeValueException
  {
    if (cat >= 0 && cat <= 5) this.category = cat;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_CATEGORY);
  }

  public int getCategory()
  {
    return this.category;
  }

  /**
   * 
   * @param title The title of the development need cannot exceed the 150 characters
   */
  public void setTitle(String title) throws InvalidAttributeValueException
  {
    if (title != null && title.length() > 0 && title.length() < 151) this.title = title;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_TITLE);
  }

  public String getTitle()
  {
    return this.title;
  }

  /**
   * 
   * @param description The description of the objective cannot exceed the 1000 characters
   */
  public void setDescription(String description) throws InvalidAttributeValueException
  {
    if (description != null && description.length() > 0 && description.length() < 2001) this.description = description;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_DESCRIPTION);
  }

  public String getDescription()
  {
    return this.description;
  }

  /**
   * This method creates a timestamp when the object is created
   */
  private void setTimeStamp()
  {
    // Check if the timeStamp has already a value assigned
    if (timeStamp == null) this.timeStamp = LocalDateTime.now(ZoneId.of(UK_TIMEZONE)).toString();
  }

  public String getTimeStamp()
  {
    return this.timeStamp;
  }

  /**
   * 
   * @param date the date of when the objective needs to be completed by
   * @throws InvalidAttributeValueException
   */
  public void setTimeToCompleteBy(String date) throws InvalidAttributeValueException
  {
    // Convert the String to a YearMonth object
    if (!date.equals(""))
    {
      YearMonth temp = YearMonth.parse(date, Constants.YEAR_MONTH_FORMAT);
      // Verify that the month and year inserted are greater than the current month and year
      // Every year has 12 months, so if the values are 2017 and 2016 the difference will be 1 which is 12 months
      int yearDifference = (temp.getYear() - LocalDate.now(ZoneId.of(UK_TIMEZONE)).getYear()) * 12;
      int monthDifference = temp.getMonthValue() - LocalDate.now(ZoneId.of(UK_TIMEZONE)).getMonthValue();
      // Sum these 2 values up and if the result is <0, the date is in the past which is invalid
      int totalMonthsApart = yearDifference + monthDifference;
      if (totalMonthsApart >= 0) this.timeToCompleteBy = temp.toString();
      else throw new InvalidAttributeValueException(Constants.INVALID_PASTDATE);
    }
    else throw new InvalidAttributeValueException(Constants.INVALID_DATEFORMAT);
  }

  public String getTimeToCompleteBy()
  {
    if (!this.timeToCompleteBy.equals(Constants.COMPLETE_DATE_NOT_SET))
    {
      YearMonth temp = YearMonth.parse(this.timeToCompleteBy, Constants.YEAR_MONTH_FORMAT);
      return temp.format(Constants.YEAR_MONTH_FORMAT);
    }
    return this.timeToCompleteBy;
  }

  public YearMonth getTimeToCompleteByYearMonth()
  {
    return YearMonth.parse(this.timeToCompleteBy, Constants.YEAR_MONTH_FORMAT);
  }

  public boolean isDevelopmentNeedValid()
  {
    return (this.getID() > 0 && this.getCategory() >= 0 && !this.getTitle().contains("Invalid")
        && !this.getDescription().contains("Invalid") && this.timeToCompleteBy != null);
  }

  public String toGson()
  {
    Gson gsonData = new Gson();
    return gsonData.toJson(this);
  }

  @Override
  public String toString()
  {
    String s = "";
    s += "ID " + this.id + "\n" + "Category " + this.category + "\n" + "Title " + this.title + "\n" + "Description "
        + this.description + "\n" + "TimeStamp " + this.getTimeStamp() + "\n" + "TimeToCompleteBy "
        + this.getTimeToCompleteBy() + "\n";
    return s;
  }

}
