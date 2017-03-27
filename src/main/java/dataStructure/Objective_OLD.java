package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;

import javax.management.InvalidAttributeValueException;

import org.mongodb.morphia.annotations.Embedded;

import com.google.gson.Gson;

/**
 * This class contains the definition of the Objective object
 *
 */
@Deprecated
@Embedded
public class Objective_OLD implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private int id, progress, performance;
  private boolean isArchived;
  private String title, description, timeStamp, timeToCompleteBy, proposedBy;

  // Empty Constructor
  public Objective_OLD()
  {
    this.id = Constants.INVALID_INT;
    this.progress = Constants.INVALID_INT;
    this.performance = Constants.INVALID_INT;
    this.isArchived = false;
    this.title = Constants.INVALID_STRING;
    this.description = Constants.INVALID_STRING;
    this.proposedBy = "";
    this.timeStamp = null;
    this.timeToCompleteBy = null;
  }

  // Constructor with Parameters
  public Objective_OLD(int id, int prog, int perf, String title, String descr, String dateToCompleteBy)
      throws InvalidAttributeValueException
  {
    this.setID(id);
    this.setProgress(prog);
    this.setPerformance(perf);
    this.isArchived = false;
    this.setTitle(title);
    this.setDescription(descr);
    this.timeStamp = null;
    this.setTimeStamp();
    this.setTimeToCompleteBy(dateToCompleteBy);
    // this.feedback=new ArrayList<Feedback>();
    this.proposedBy = "";
  }

  // Constructor with Parameters
  public Objective_OLD(Objective_OLD o) throws InvalidAttributeValueException
  {
    this.setID(o.getID());
    this.setProgress(o.getProgress());
    this.setPerformance(o.getPerformance());
    this.isArchived = false;
    this.setTitle(o.getTitle());
    this.setDescription(o.getDescription());
    this.timeStamp = null;
    this.setTimeStamp();
    this.setTimeToCompleteBy(o.getTimeToCompleteBy());
    // this.feedback=o.getFeedback();
    this.proposedBy = o.getProposedBy();
  }

  // Constructor with Parameters
  public Objective_OLD(int prog, int perf, String title, String descr, String dateToCompleteBy)
      throws InvalidAttributeValueException
  {
    this.setProgress(prog);
    this.setPerformance(perf);
    this.setTitle(title);
    this.setDescription(descr);
    this.timeStamp = null;
    this.setTimeStamp();
    this.setTimeToCompleteBy(dateToCompleteBy);
    // feedback=new ArrayList<Feedback>();
    this.proposedBy = "";
  }

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
    if (progress >= -1 && progress <= 2) this.progress = progress;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_PROGRESS);
  }

  public int getProgress()
  {
    return this.progress;
  }

  public void setProposedBy(String name) throws InvalidAttributeValueException
  {
    if (name != null) this.proposedBy = name;
    else throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVE_PROPOSEDBY);
  }

  public String getProposedBy()
  {
    return this.proposedBy;
  }

  /**
   * 
   * @param performance This variable can assume only 3 values: 0 => Green 1 => Amber 2 => Red
   */
  public void setPerformance(int performance) throws InvalidAttributeValueException
  {
    if (performance >= 0 && performance <= 2) this.performance = performance;
    else throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVE_PERFORMANCE);
  }

  public int getPerformance()
  {
    return this.performance;
  }

  public void setIsArchived(boolean val)
  {
    this.isArchived = val;
  }

  public boolean getIsArchived()
  {
    return isArchived;
  }

  /**
   * 
   * @param title The title of the object cannot exceed the 150 characters
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
    if (timeStamp == null) this.timeStamp = LocalDateTime.now(UK_TIMEZONE).toString();
  }

  public String getTimeStamp()
  {
    return this.timeStamp;
  }

  public boolean updateArchiveStatus(boolean isArchived)
  {
    timeStamp = LocalDateTime.now(UK_TIMEZONE).toString();
    this.isArchived = isArchived;

    return this.isArchived;
  }

  /**
   * 
   * @param date the date of when the objective needs to be completed by
   * @throws InvalidAttributeValueException
   */
  public void setTimeToCompleteBy(String date) throws InvalidAttributeValueException
  {
    if (date.equals(""))
    {
      throw new InvalidAttributeValueException(Constants.INVALID_DATEFORMAT);
    }

    YearMonth temp = YearMonth.parse(date, Constants.YEAR_MONTH_FORMAT);
    YearMonth now = YearMonth.now(UK_TIMEZONE);
    boolean pastDate = temp.isBefore(now);

    if (!pastDate)
    {
      timeToCompleteBy = date;
    }
    else
    {
      throw new InvalidAttributeValueException(Constants.INVALID_PASTDATE);
    }
  }

  public String getTimeToCompleteBy()
  {
    YearMonth temp = YearMonth.parse(this.timeToCompleteBy, Constants.YEAR_MONTH_FORMAT);
    return temp.format(Constants.YEAR_MONTH_FORMAT);
  }

  public YearMonth getTimeToCompleteByYearMonth()
  {
    return YearMonth.parse(this.timeToCompleteBy, Constants.YEAR_MONTH_FORMAT);
  }

  // /**
  // *
  // * @param listData the list of feedback that is going to be assigned to this objective
  // * @throws InvalidClassException
  // * @throws InvalidAttributeValueException
  // */
  // public void setFeedback(List<Feedback> listData) throws InvalidAttributeValueException{
  // if(listData!=null){
  // //Create a counter that keeps count of the error produced
  // int errorCounter=0;
  // this.feedback=new ArrayList<Feedback>();
  // //Check if the feedback objects inside the list are valid
  // for(Feedback temp:listData){
  // if(temp.isFeedbackValid())
  // this.feedback.add(temp);
  // else
  // errorCounter++;
  // }
  // //Verify if there has been any error
  // if(errorCounter!=0)
  // throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKLIST);
  // }
  // else
  // throw new InvalidAttributeValueException(Constants.NULL_FEEDBACKLIST);
  // }

  // public List<Feedback> getFeedback(){
  // List<Feedback> data=new ArrayList<Feedback>();
  // for(Feedback temp: this.feedback){
  // data.add(temp);
  // }
  // return data;
  // }

  // /**
  // * This method adds a feedback to this objective
  // *
  // * @param obj feedback data
  // * @return
  // * @throws InvalidAttributeValueException
  // */
  // public boolean addFeedback(Feedback obj) throws InvalidAttributeValueException{
  // if(feedback==null)
  // feedback=new ArrayList<Feedback>();
  // //Validate the feedback
  // if(obj==null)
  // throw new InvalidAttributeValueException(Constants.NULL_FEEDBACK);
  // if(obj.isFeedbackValid())
  // return feedback.add(obj);
  // throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACK);
  // }

  public boolean isObjectiveValid()
  {
    return (this.getID() > 0 && !this.getTitle().contains("Invalid") && !this.getDescription().contains("Invalid")
        && this.getTimeStamp() != null && this.getTimeToCompleteBy() != null);
  }

  public boolean isObjectiveValidWithoutID()
  {
    return (this.getTitle().contains("Invalid") && !this.getDescription().contains("Invalid")
        && this.getTimeStamp() != null && this.getTimeToCompleteBy() != null);
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
    s += "ID " + this.id + "\n" + "Progress " + this.progress + "\n" + "Performance " + this.performance + "\n"
        + "Is Archived  " + this.isArchived + "\n" + "Title " + this.title + "\n" + "Description " + this.description
        + "\n" + "TimeStamp " + this.getTimeStamp() + "\n" + "TimeToCompleteBy " + this.getTimeToCompleteBy() + "\n"
        + "ProposedBy " + this.getProposedBy() + "\n";
    // for(Feedback temp: this.feedback){
    // s+=temp.toString();
    // }
    return s;
  }

}
