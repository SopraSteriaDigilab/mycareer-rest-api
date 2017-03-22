package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import utils.Utils;

/**
 * This class contains the definition of an object to be stored in the DB.
 *
 */
public abstract class DBObject implements Serializable
{
  /** long Constant - Represents serialVersionUID... */
  private static final long serialVersionUID = 1L;

  /** int Property - Represents Unique ID for the object. */
  private int id;

  /** String Property - Represents the timestamp of the objective. */
  private Date createdOn;

  /** String Property - Represents the time the objective was last modified. */
  private Date lastModified;

  /**
   * Default Constructor - Responsible for initialising this object.
   *
   * @param id
   * @param lastModified
   */
  public DBObject()
  {
    this.setCreatedOn();
    this.setLastModified();
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

  /** @return the timeStamp. */
  public String getLastModified()
  {
    return Utils.DateToLocalDateTime(this.lastModified).toString();
  }

  /** @param lastModified */
  public void setLastModified()
  {
    this.lastModified = Utils.localDateTimetoDate(LocalDateTime.now(UK_TIMEZONE));
  }

}
