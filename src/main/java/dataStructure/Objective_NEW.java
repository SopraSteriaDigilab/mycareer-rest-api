package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

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
    
    Progress(String progressStr){
     this.progressStr = progressStr; 
    }
    
    public String getProgressStr() {
      return this.progressStr;
    }
  }

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -8210647573312345743L;

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
  public Objective_NEW(String title, String Description, LocalDate dueDate, String proposedBy)
  {
    super();
    this.setTitle(title);
    this.setDescription(description);
    this.setDueDate(dueDate);
    this.setProposedBy(proposedBy);
    this.setProgress(Progress.PROPOSED);
    this.setArchived(false);
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
    return instant.atZone(UK_TIMEZONE).toLocalDate();
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
    return this.progress.getProgressStr();
  }

  /**
   * @param progress The value to set the named property to. Must be one of the following: Proposed, InProgress,
   * Complete;
   */
  public void setProgress(Progress progress)
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

}
