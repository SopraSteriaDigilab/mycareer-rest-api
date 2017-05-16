package dataStructure;

import static utils.Conversions.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class FeedbackRequest implements Serializable
{
  private static final long serialVersionUID = 1L;

  /* Unique ID for the object. */
  private String id;

  /* Email of recipient */
  private String recipient;

  /* State of whether feedback has been given */
  private boolean replyReceived;

  /* Time stamp of feedback request */
  private String timestamp;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   */
  public FeedbackRequest()
  {
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param id
   * @param recipient
   */
  public FeedbackRequest(String id, String recipient)
  {
    super();
    this.id = id;
    this.recipient = recipient;
    setReplyReceived(false);
    setTimestamp();
  }

  /** @return the id */
  public String getId()
  {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id)
  {
    this.id = id;
  }

  /** @return the recipient */
  public String getRecipient()
  {
    return recipient;
  }

  /** @param recipient the recipient to set */
  public void setRecipient(String recipient)
  {
    this.recipient = recipient;
  }

  /** @return the replyReceived */
  public boolean isReplyReceived()
  {
    return replyReceived;
  }

  /** @param replyReceived the replyReceived to set */
  public void setReplyReceived(boolean replyReceived)
  {
    this.replyReceived = replyReceived;
  }

  /** @return the timestamp */
  public String getTimestamp()
  {
    return timestamp;
  }

  /** Set timestamp to current time */
  public void setTimestamp()
  {
    this.timestamp = LocalDateTime.now(UK_TIMEZONE).toString();
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param profile
   * @return
   */
  public Activity createActivity(final EmployeeProfile profile)
  {
    final String activityString = new StringBuilder(profile.getFullName()).append(" requested feedback from ")
        .append(recipient).toString();

    return new Activity(activityString, timestamp);
  }
}
