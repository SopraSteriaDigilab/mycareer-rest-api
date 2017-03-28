package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;

import utils.Utils;

/**
 * This class contains the definition of the Objective object.
 */
public class Objective extends DBObject implements Comparable<Objective>
{
  /** Represents progress of any Objective object. */
  public enum Progress
  {
    PROPOSED("Proposed"), IN_PROGRESS("In-Progress"), COMPLETE("Complete");

    private String progressStr;

    Progress(String progressStr)
    {
      this.progressStr = progressStr;
    }

    public String getProgressStr()
    {
      return this.progressStr;
    }

    public static Progress getProgressFromString(String progressString)
    {
      switch (progressString)
      {
        case "Proposed":
          return Progress.PROPOSED;
        case "In-Progress":
          return Progress.IN_PROGRESS;
        case "Complete":
          return Progress.COMPLETE;
      }
      throw new IllegalArgumentException("The String provided does not match a valid Progress enum");
    }
  }

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -8210647573312345743L;

  /** String Property - Represents the timestamp of the objective. */
  private Date createdOn;

  /** String Property - Represents the objective title */
  private String title;

  /** String Property - Represents the objective description */
  private String description;

  /** String Property - Represents the due date of the objective */
  private Date dueDate;

  /** String Property - Represents the name of the person that made the objective */
  private String proposedBy;

  /** String Property - Represents the progress of the objective */
  private String progress;

  /** boolean Property - Represents the state of the objective */
  private boolean isArchived;

  /**
   * Default Constructor - Responsible for initialising this object.
   */
  public Objective()
  {
    this.setCreatedOn();
  }

  /**
   * Objective_NEW Constructor - Responsible for initialising this object.
   *
   */
  public Objective(String title, String description, LocalDate dueDate)
  {
    this();
    this.setTitle(title);
    this.setDescription(description);
    this.setDueDate(dueDate);
    this.setProposedBy("");
    this.setProgress(Progress.PROPOSED);
    this.isArchived(false);
  }

  /**
   * Objective_NEW Constructor - Responsible for initialising this object.
   *
   */
  public Objective(int id, String title, String description, LocalDate dueDate)
  {
    this(title, description, dueDate);
    this.setId(id);
  }

  /** @return the createdOn */
  public String getCreatedOn()
  {
    return Utils.DateToLocalDateTime(this.createdOn).toString();
  }

  /** @param createdOn The value to set. */
  public void setCreatedOn()
  {
    this.createdOn = Utils.localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE));
  }

  /** @return the title. */
  public String getTitle()
  {
    return title;
  }

  /** @param title The value to set the named property to. */
  public void setTitle(String title)
  {
    this.title = title;
    this.setLastModified();
  }

  /** @return the description. */
  public String getDescription()
  {
    return description;
  }

  /** @param description The value to set the named property to. */
  public void setDescription(String description)
  {
    this.description = description;
    this.setLastModified();
  }

  /** @return the dueDate. */
  public String getDueDate()
  {
    return Utils.DateToLocalDate(this.dueDate).toString();
  }

  /** @param dueDate The value to set the named property to. */
  public void setDueDate(LocalDate dueDate)
  {
    this.dueDate = Utils.localDatetoDate(dueDate);
    this.setLastModified();
  }

  /** @return the proposedBy. */
  public String getProposedBy()
  {
    return proposedBy;
  }

  /** @param proposedBy The value to set the named property to. */
  public void setProposedBy(String proposedBy)
  {
    this.proposedBy = proposedBy;
    this.setLastModified();
  }

  /** @return the progress. */
  public String getProgress()
  {
    return progress;
  }

  /**
   * @param progress The value to set the named property to. Must be one of the following: Proposed, InProgress,
   *          Complete;
   */
  public void setProgress(Progress progress)
  {
    this.progress = progress.getProgressStr();
    this.setLastModified();
  }

  /** @return the isArchived */
  public boolean getArchived()
  {
    return isArchived;
  }

  /** @param isArchived The value to set the named property to. */
  public void isArchived(boolean isArchived)
  {
    this.isArchived = isArchived;
    this.setLastModified();
  }
  
  @Override
  public int compareTo(Objective objective)
  {
    LocalDate ld1 = LocalDate.parse(this.getDueDate());
    LocalDate ld2 = LocalDate.parse(objective.getDueDate());
    
    return (ld1.equals(ld2)) ? 0 : (ld1.isBefore(ld2) ? -1 : 1);
  }

  /**
   * Returns a document containing the differences (only title, description & dueDate) of the objectives.
   *
   * @param objective
   * @return a new document
   */
  public Document differences(Objective objective)
  {
    Document differences = new Document();
    if (!this.getTitle().equals(objective.getTitle()))
    {
      differences.append("title", objective.getTitle());
    }
    if (!this.getDescription().equals(objective.getDescription()))
    {
      differences.append("description", objective.getDescription());
    }
    if (!this.getDueDate().equals(objective.getDueDate()))
    {
      differences.append("dueDate", objective.getDueDate());
    }
    return differences;
  }
}
