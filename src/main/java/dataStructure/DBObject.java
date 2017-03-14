package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class contains the definition of an object to be stored in the DB.
 *
 */
public class DBObject implements Serializable
{
  
  /** ZoneId Constant - Represents Time Zone for UK... */
  public static final ZoneId UK_TIMEZONE = ZoneId.of("Europe/London");

  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = -3282647892949110214L;

  /** int Property - Represents Unique ID for the object. */
  private int id;

  /** String Property - Represents the timestamp of the objective. */
  private String timestamp;

  /**
   * Default Constructor - Responsible for initialising this object.
   *
   * @param id
   * @param timestamp
   */
  public DBObject()
  {
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

  /** @return the timeStamp. */
  public String getTimeStamp()
  {
    return timestamp;
  }

  /** @param timestamp */
  public void setTimestamp()
  {
    this.timestamp = LocalDateTime.now(UK_TIMEZONE).toString();
  }
}
