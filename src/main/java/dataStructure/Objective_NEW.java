package dataStructure;

import java.time.LocalDate;
import java.util.Date;

import javax.management.InvalidAttributeValueException;

import dataStructure.DevelopmentNeed_NEW.Category;
import utils.Utils;

/**
 * This class contains the definition of the Objective object.
 */
public class Objective_NEW extends DBObject
{
  public static final String ERROR_EMPTY = "Please fill in all the required feilds.";
  public static final String ERROR_DESCRIPTION_LIMIT_2000 = "Max Description length is 1000 characters.";
  public static final String ERROR_NAME_LIMIT = "Max Name length is 150 characters.";
  public static final String ERROR_TITLE_LIMIT = "Max Title is 150 characters.";
  public static final String ERROR_PROGRESS_LIMIT = "Please enter a valid progress (Proposed, In-Progress, Complete).";

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

    public static Progress getProgressFromString(String progressString) throws InvalidAttributeValueException
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
      throw new InvalidAttributeValueException("This enum string does not exist");
    }
  }

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -8210647573312345743L;

  /** String Property - Represents the objective title */
  private String title;

  /** String Property - Represents the objective description */
  private String description;

  /** String Property - Represents the due date of the objective */
  private Date dueDate;

  /** String Property - Represents the name of the person that made the objective */
  private String proposedBy;

  /** String Property - Represents the progress of the objective */
  private Progress progress;

  /** boolean Property - Represents the state of the objective */
  private boolean isArchived;

  /**
   * Default Constructor - Responsible for initialising this object.
   */
  public Objective_NEW()
  {
  }

  /**
   * Objective_NEW Constructor - Responsible for initialising this object.
   *
   */
  public Objective_NEW(String title, String description, LocalDate dueDate)
  {
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
  public Objective_NEW(int id, String title, String description, LocalDate dueDate)
  {
    this(title, description, dueDate);
    this.setId(id);
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
    return this.progress.getProgressStr();
  }

  /**
   * @param progress The value to set the named property to. Must be one of the following: Proposed, InProgress,
   *          Complete;
   */
  public void setProgress(Progress progress)
  {
    this.progress = progress;
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

}
