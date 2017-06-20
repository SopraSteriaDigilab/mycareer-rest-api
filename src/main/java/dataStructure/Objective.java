package dataStructure;

import static utils.Conversions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.bson.Document;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class Objective extends DBObject implements Comparable<Objective>
{
  private static final long serialVersionUID = -8210647573312345743L;

  /** TODO describe */
  public static final String ID = "objectives.id";

  /** TODO describe */
  public static final String LAST_MODIFIED = "objectives.lastModified";

  /** TODO describe */
  public static final String CREATED_ON = "objectives.createdOn";

  /** TODO describe */
  public static final String TITLE = "objectives.title";

  /** TODO describe */
  public static final String DESCRIPTION = "objectives.description";

  /** TODO describe */
  public static final String DUE_DATE = "objectives.dueDate";

  /** TODO describe */
  public static final String PROPOSED_BY = "objectives.proposedBy";

  /** TODO describe */
  public static final String PROGRESS = "objectives.progress";

  /** TODO describe */
  public static final String IS_ARCHIVED = "objectives.isArchived";

  private static final String OBJECTIVE = "objective";

  private Date createdOn;
  private String title;
  private String description;
  private Date dueDate;
  private String proposedBy;
  private String progress;
  private boolean isArchived;

  /**
   * No-args Constructor - Responsible for initialising this object.
   */
  public Objective()
  {
    this.createdOn = getLastModifiedAsDate();
  }

  /**
   * Objective Constructor - Responsible for initialising this object.
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
   * Objective Constructor - Responsible for initialising this object.
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
    return dateToLocalDateTime(this.createdOn).toString();
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
    return dateToLocalDate(this.dueDate).toString();
  }

  /** @param dueDate The value to set the named property to. */
  public void setDueDate(LocalDate dueDate)
  {
    this.dueDate = localDatetoDate(dueDate);
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

  /**
   * 
   * Override of NAME method.
   *
   * TODO: Describe this method.
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   *
   * @param objective
   * @return
   */
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

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public Date getCreatedOnAsDate()
  {
    return createdOn;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public Date getDueDateAsDate()
  {
    return dueDate;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param activityType
   * @param profile
   * @return
   */
  public Activity createActivity(final Action activityType, final EmployeeProfile profile)
  {
    final String activityString = new StringBuilder(profile.getFullName()).append(" ").append(activityType.getVerb())
        .append(" ").append(OBJECTIVE).append(" #").append(getId()).append(": ").append(title).toString();

    return new Activity(activityString, getLastModified());
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public boolean isCurrent()
  {
    final LocalDateTime cutOffDate = LocalDateTime.now(UK_TIMEZONE).minusYears(1);
    final LocalDateTime lastModified = dateToLocalDateTime(getLastModifiedAsDate());
    final LocalDateTime dueDate = dateToLocalDateTime(this.dueDate);
    final boolean isCurrent = !progress.equals(Progress.COMPLETE.getProgressStr()) || lastModified.isAfter(cutOffDate)
        || dueDate.isAfter(cutOffDate);

    return isCurrent;
  }

  /** Represents progress of any Objective object. */
  public enum Progress
  {
    PROPOSED("Proposed"), IN_PROGRESS("In-Progress"), COMPLETE("Complete");

    private String progressStr;

    private Progress(String progressStr)
    {
      this.progressStr = progressStr;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @return
     */
    public String getProgressStr()
    {
      return this.progressStr;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @param progressString
     * @return
     */
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
}
