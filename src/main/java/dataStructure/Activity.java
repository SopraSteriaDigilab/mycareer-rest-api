package dataStructure;

import static dataStructure.Employee.*;
import static utils.Conversions.*;

import java.util.Date;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.bson.Document;

/**
 * An item of MyCareer activity. An item of MyCareer activity is a description of some action taken by a MyCareer user
 * coupled with a timestamp of when the activity occurred.
 * 
 * MyCareer activity can be any of the following:
 * 
 * <ul>
 * <li>adding, editing, archiving, restoring, deleting or completing an objective or development need</li>
 * <li>adding a note</li>
 * <li>requesting feedback</li>
 * </ul>
 * 
 * @see Action
 */
public class Activity implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * The fully qualified field name used to store the description of an activity within an {@code Employee} in the
   * employees collection in MongoDB
   * 
   * @see Employee
   */
  public static final String DESCRIPTION = "activityFeed.description";

  /**
   * The fully qualified field name used to store the timestamp of an activity within an {@code Employee} in the
   * employees collection in MongoDB
   * 
   * @see Employee
   */
  public static final String TIMESTAMP = "activityFeed.timestamp";

  private String description;
  private Date timestamp;

  /**
   * Activity Constructor - No-args constructor provided for use by Morphia. Should not be used in application code.
   */
  public Activity()
  {
  }

  /**
   * Activity Constructor - Responsible for initialising this object.
   *
   * @param description A description of this activity
   * @param timestamp The date that this activity occurred.
   */
  public Activity(final String description, final Date timestamp)
  {
    this.description = description;
    this.timestamp = timestamp;
  }

  /**
   * Activity Constructor - Responsible for initialising this object.
   *
   * @param description A description of this activity
   * @param timestamp The date that this activity occurred.
   */
  public Activity(final String description, final LocalDateTime timestamp)
  {
    this.description = description;
    this.timestamp = localDateTimetoDate(timestamp);
  }

  /**
   * Activity Constructor - Responsible for initialising this object.
   *
   * @param description A description of this activity
   * @param timestamp A string represenation of the date that this activity occurred.
   * @see Date:toString
   */
  public Activity(final String description, final String timestamp)
  {
    this.description = description;
    LocalDateTime localDateTime = LocalDateTime.parse(timestamp);
    this.timestamp = localDateTimetoDate(localDateTime);
  }

  /**
   * Constructs a new activity using the give {@code Document}.
   * 
   * The document should contain a field named {@code activityFeed} whose value is a {@code Document} containing a
   * field, {@code description}, whose value is a {@code String}, and a field, {@code timestamp}, whose value is an
   * {@code Date}.
   *
   * @param document The document from which a new activity will be constructed.
   * @return A new instance of {@code Activity}
   */
  public static Activity ofDocument(final Document document)
  {
    final Document activityFeed = document.get(ACTIVITY_FEED, Document.class);
    final String description = activityFeed.getString("description");
    final Date timestamp = activityFeed.getDate("timestamp");

    return new Activity(description, timestamp);
  }

  /** @return A {@code Document} representation of this activity */
  public Document toDocument()
  {
    return new Document("description", description).append("timestamp", timestamp);
  }

  /** @return The description of this activity */
  public String getDescription()
  {
    return description;
  }

  /** @param description A description of this activity */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /** @return a {@code String} representation of the date that this activity occurred. */
  public String getTimestamp()
  {
    return dateToLocalDateTime(timestamp).toString();
  }

  /** @param timestamp The date that this activity occurred */
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
}
