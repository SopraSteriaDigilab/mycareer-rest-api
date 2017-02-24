package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * This class contains the definition of the Note object
 *
 */
public class Note implements Serializable
{

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -7758646259468792018L;
  
  private static final String ERROR_NOTE_EMPTY = "Note feilds can not be empty.";
  private static final String ERROR_NOTE_DESCRIPTION_LIMIT = "Max Description length is 1000 characters.";
  private static final String ERROR_PROVIDER_NAME_LIMIT = "Max Provider Name length is 150 characters.";

  /** int Property - Represents Unique ID for the object. */
  private int id;

  /** String Property - Represents name of the not provider. */
  @NotNull(message = ERROR_NOTE_EMPTY)
  @NotBlank(message = ERROR_NOTE_EMPTY)
  @Size(max = 150, message = ERROR_PROVIDER_NAME_LIMIT)
  private String providerName;

  /** String Property - Represents the description of the note. */
  @NotNull(message = ERROR_NOTE_EMPTY)
  @NotBlank(message = ERROR_NOTE_EMPTY)
  @Size(max = 1000, message = ERROR_NOTE_DESCRIPTION_LIMIT)
  private String noteDescription;

  /** String Property - Represents the timestamp of the note. */
  private String timestamp;

  /** Default Constructor - Responsible for initialising this object. */
  public Note()
  {
  }

  /**
   * Note_NEW Constructor - Responsible for initialising this object.
   *
   * @param note
   */
  public Note(Note note)
  {
    this.setProviderName(note.providerName);
    this.setNoteDescription(note.noteDescription);
    this.setTimestamp();
  }

  /** @return the id */
  public int getId()
  {
    return id;
  }

  /** @param id */
  public void setId(int id)
  {
    this.id = id;
  }

  /** @return the id */
  public String getProviderName()
  {
    return providerName;
  }

  /** @param providerName */
  public void setProviderName(String providerName)
  {
    this.providerName = providerName;
  }

  /** @return the id */
  public String getNoteDescription()
  {
    return noteDescription;
  }

  /** @param noteDescription */
  public void setNoteDescription(String noteDescription)
  {
    this.noteDescription = noteDescription;
  }

  /** @return the timestamp */
  public String getTimestamp()
  {
    return timestamp;
  }

  /** @param timestamp */
  public void setTimestamp()
  {
    this.timestamp = LocalDateTime.now(ZoneId.of(UK_TIMEZONE)).toString();
  }

}
