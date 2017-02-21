package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Objective_NEW implements Serializable
{

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -8210647573312345743L;

  /** int Property - Represents Unique ID for the object. */
  private int id;

  /** String Property - Represents the objective title */
  private String title;

  /** String Property - Represents the objective description */
  private String description;

  /** String Property - Represents the due date of the objective */
  private String dueDate;

  /** String Property - Represents the name of the person that made the objective */
  private String proposedBy;

  /** int Property - Represents the progress of the objective */
  private int progress;

  /** boolean Property - Represents the state of the objective */
  private boolean isArchived;

  /** String Property - Represents the timestamp of the objective. */
  private String timestamp;
  
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
  public Objective_NEW(Objective_NEW objective)
  {
    this.setTitle(objective.getTitle());
    this.setDescription(objective.getDescription());
    this.setDueDate(objective.getDueDate());
    this.setProposedBy(objective.getProposedBy());
    this.setProgress(0);
    this.setArchived(false);
    this.setTimestamp();
  }

  
  /** @return the id. */
  public int getId()
  {
    return id;
  }

  /** @param id The value to set the named property to. */
  public void setId(int id)
  {
    this.id = id;
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
  }

  /** @return the dueDate. */
  public String getDueDate()
  {
    return dueDate;
  }

  /** @param dueDate The value to set the named property to. */
  public void setDueDate(String dueDate)
  {
    this.dueDate = dueDate;
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
  }

  /** @return the progress. */
  public int getProgress()
  {
    return progress;
  }

  /**  @param progress The value to set the named property to. */
  public void setProgress(int progress)
  {
    this.progress = progress;
  }

  /** @return the isArchived */
  public boolean isArchived()
  {
    return isArchived;
  }

  /** @param isArchived The value to set the named property to. */
  public void setArchived(boolean isArchived)
  {
    this.isArchived = isArchived;
  }

  /** @return the timeStamp. */
  public String getTimeStamp()
  {
    return timestamp;
  }

  /** @param timestamp */
  public void setTimestamp()
  {
    this.timestamp = LocalDateTime.now(ZoneId.of(UK_TIMEZONE)).toString();
  }
  
  

}
