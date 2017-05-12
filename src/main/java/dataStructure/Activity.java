package dataStructure;

import static dataStructure.Employee.*;
import static utils.Conversions.*;

import java.util.Date;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.bson.Document;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class Activity implements Serializable
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  /** TODO describe */
  public static final String DESCRIPTION = "activityFeed.description";

  /** TODO describe */
  public static final String TIMESTAMP = "activityFeed.timestamp";

  private String description;
  private Date timestamp;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   */
  public Activity()
  {
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param description
   * @param timestamp
   */
  public Activity(final String description, final Date timestamp)
  {
    this.description = description;
    this.timestamp = timestamp;
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param description
   * @param timestamp
   */
  public Activity(final String description, final String timestamp)
  {
    this.description = description;
    LocalDateTime localDateTime = LocalDateTime.parse(timestamp);
    this.timestamp = localDateTimetoDate(localDateTime);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param document
   * @return
   */
  public static Activity ofDocument(final Document document)
  {
    final Document activityFeed = document.get(ACTIVITY_FEED, Document.class);
    final String description = activityFeed.getString("description");
    final Date timestamp = activityFeed.getDate("timestamp");

    return new Activity(description, timestamp);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public Document toDocument()
  {
    return new Document("description", description).append("timestamp", timestamp);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String getTimestamp()
  {
    return dateToLocalDateTime(timestamp).toString();
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param timestamp
   */
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
}
