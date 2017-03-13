package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class contains the definition of the Note object
 */
// TODO Add the spring validation here, (see the annotations in the employeeController) Then change the constructor to
// take in a Note object.
public class Note implements Serializable
{

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -7758646259468792018L;

  /** int Property - Represents Unique ID for the object. */
  private int id;

  /** String Property - Represents name of the not provider. */
  private String providerName;

  /** String Property - Represents the description of the note. */
  private String noteDescription;

  /** String Property - Represents the timestamp of the note. */
  private String timestamp;

  /** Default Constructor - Responsible for initialising this object. */
  public Note()
  {
  }

  /**
   * Note Constructor - Responsible for initialising this object.
   *
   * @param note
   */
  public Note(String providerName, String noteDescription)
  {
    this.setProviderName(providerName);
    this.setNoteDescription(noteDescription);
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
    this.timestamp = LocalDateTime.now(UK_TIMEZONE).toString();
  }

}
