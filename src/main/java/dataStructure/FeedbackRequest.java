package dataStructure;

import static utils.Conversions.*;
import static dataStructure.Action.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class FeedbackRequest implements Comparable<FeedbackRequest>, Serializable
{
  private static final long serialVersionUID = 1L;

  /** TODO describe */
  public static final String ID = "feedbackRequests.id";

  /** TODO describe */
  public static final String RECIPIENT = "feedbackRequests.recipient";

  /** TODO describe */
  public static final String REPLY_RECEIVED = "feedbackRequests.replyReceived";

  /** TODO describe */
  public static final String DISMISSED = "feedbackRequests.dismissed";

  /** TODO describe */
  public static final String TIMESTAMP = "feedbackRequests.timestamp";

  private String id;
  private String recipient;
  private boolean replyReceived;
  private boolean dismissed;
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
    this.id = id;
    this.recipient = recipient;
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

  /** @param dismissed set whether this feedback request has been dismissed */
  public void setDismissed(boolean dismissed)
  {
    this.dismissed = dismissed;
  }

  /** @return {@code true} if this feedback request has been dismissed */
  public boolean isDismissed()
  {
    return dismissed;
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
    final String activityString = new StringBuilder(profile.getFullName()).append(" ").append(REQUEST.getVerb())
        .append(" feedback from ").append(recipient).toString();

    return new Activity(activityString, timestamp);
  }

  /**
   * @return {@code true} if this feedback request has not been dismissed by the requester, has not had a response from
   *         the recipient, and was sent within the last year.
   */
  public boolean isCurrent()
  {
    if (replyReceived || dismissed)
    {
      return false;
    }

    final LocalDateTime cutOffDate = LocalDateTime.now(UK_TIMEZONE).minusYears(1);
    final LocalDateTime added = LocalDateTime.parse(timestamp);
    final boolean isCurrent = added.isAfter(cutOffDate);

    return isCurrent;
  }

  /**
   * 
   * Override of NAME method.
   *
   * TODO: Describe this method.
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   *
   * @param other
   * @return
   */
  @Override
  public int compareTo(final FeedbackRequest other)
  {
    final LocalDateTime thisTimestamp = LocalDateTime.parse(timestamp);
    final LocalDateTime otherTimestamp = LocalDateTime.parse(other.timestamp);

    return thisTimestamp.compareTo(otherTimestamp);
  }
}
