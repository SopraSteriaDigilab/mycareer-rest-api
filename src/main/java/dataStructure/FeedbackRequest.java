package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * FeedbackRequest object for MyCareer.
 * 
 */
public class FeedbackRequest implements Serializable
{

  private static final long serialVersionUID = 5904650249033682895L;

  /** Unique ID for the object. */
  private String id;

  /** Email of recipient */
  private String recipient;

  /** State of whether feedback has been given */
  private boolean replyReceived;

  /** Time stamp of feedback request */
  private String timestamp;

  /** Empty Constructor */
  public FeedbackRequest()
  {
  }

  /**
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

}
