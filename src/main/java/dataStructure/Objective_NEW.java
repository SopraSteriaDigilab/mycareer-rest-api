package dataStructure;

import static application.GlobalExceptionHandler.ERROR_DESCRIPTION_LIMIT_2000;
import static application.GlobalExceptionHandler.ERROR_EMPTY;
import static application.GlobalExceptionHandler.ERROR_NAME_LIMIT;
import static application.GlobalExceptionHandler.ERROR_TITLE_LIMIT;
import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * This class contains the definition of the Objective object.
 */
public class Objective_NEW implements Serializable
{

  /** Represents progress of any Objecitve object. */
  public enum Progress
  {
    Proposed, InProgress, Complete;
  }

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -8210647573312345743L;

  /** int Property - Represents Unique ID for the object. */
  private int id;

  /** String Property - Represents the objective title */
  @NotBlank(message = ERROR_EMPTY)
  @Size(max = 150, message = ERROR_TITLE_LIMIT)
  private String title;

  /** String Property - Represents the objective description */
  @NotBlank(message = ERROR_EMPTY)
  @Size(max = 2000, message = ERROR_DESCRIPTION_LIMIT_2000)
  private String description;

  /** String Property - Represents the due date of the objective */
  @NotBlank(message = ERROR_EMPTY)
  @DateTimeFormat(pattern = "dd/MM/yyyy") // TODO Figure out the best pattern.
  private Date dueDate; // TODO Use LocalDate and convert to Date? or vice versa?

  /** String Property - Represents the name of the person that made the objective */
  @NotBlank(message = ERROR_EMPTY)
  @Size(max = 150, message = ERROR_NAME_LIMIT)
  private String proposedBy;

  /** String Property - Represents the progress of the objective */
  @NotBlank(message = ERROR_EMPTY)
  @Pattern(regexp = "^(Proposed)|(InProgress)|(Complete)$")
  private String progress;

  /** boolean Property - Represents the state of the objective */
  @Pattern(regexp = "^(True)|(False)$") // TODO Will this work...?
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
    this.setProgress(Progress.Proposed);
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
  public LocalDate getDueDate()
  {
    // TODO Move to Utils
    Instant instant = this.dueDate.toInstant();
    return instant.atZone(ZoneId.of(UK_TIMEZONE)).toLocalDate();
  }

  /** @param dueDate The value to set the named property to. */
  public void setDueDate(LocalDate dueDate)
  {
    // TODO Move to Utils
    Long milliDate = Instant.from(dueDate).toEpochMilli();
    this.dueDate = new Date(milliDate);
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
  public String getProgress()
  {
    return Progress.valueOf(this.progress).toString();
  }

  /** @param progress The value to set the named property to. */
  public void setProgress(Progress progress)
  {
    this.progress = progress.toString();
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
