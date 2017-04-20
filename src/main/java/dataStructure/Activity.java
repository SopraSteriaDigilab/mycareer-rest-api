package dataStructure;

import static dataStructure.Employee.*;
import static utils.Utils.*;

import java.util.Date;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.bson.Document;

public class Activity implements Serializable
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;
  
  public static final String DESCRIPTION = "activityFeed.description";
  public static final String TIMESTAMP = "activityFeed.timestamp";
  
  private String description;
  private Date timestamp;

  public Activity()
  {
  }

  public Activity(final String description, final Date timestamp)
  {
    this.description = description;
    this.timestamp = timestamp;
  }

  public Activity(final String description, final String timestamp)
  {
    this.description = description;
    LocalDateTime localDateTime = LocalDateTime.parse(timestamp);
    this.timestamp = localDateTimetoDate(localDateTime);
  }
  
  public static Activity ofDocument(final Document document)
  {
    final Document activityFeed = document.get(ACTIVITY_FEED, Document.class);
    final String description = activityFeed.getString("description");
    final Date timestamp = activityFeed.getDate("timestamp");
    
    return new Activity(description, timestamp);
  }
  
  public Document toDocument()
  {
    return new Document("description", description).append("timestamp", timestamp);
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getTimestamp()
  {
    return dateToLocalDateTime(timestamp).toString();
  }

  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
}
