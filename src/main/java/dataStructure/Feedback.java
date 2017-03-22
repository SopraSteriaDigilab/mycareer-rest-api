package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.management.InvalidAttributeValueException;

import utils.Validate;

/**
 * Feedback object for MyCareer
 * 
 */
public class Feedback implements Serializable
{
  private static final long serialVersionUID = 1L;

  /** Unique ID for the object. */
  private int id;

  /** Email address of feedback provider */
  private String providerEmail;

  /** Name of feedback provider */
  private String providerName;

  /** The feedback */
  private String feedbackDescription;

  /** Time stamp of feedback */
  private String timestamp;

  /** Empty Constructor */
  public Feedback()
  {
  }

  /**
   * @param id
   * @param providerEmail
   * @param feedbackDescription
   */
  public Feedback(int id, String providerEmail, String feedbackDescription)
  {
    super();
    this.id = id;
    this.providerEmail = providerEmail;
    this.feedbackDescription = feedbackDescription;
    this.providerName = "";
    setTimestamp();
  }

  /**
   * @param id
   * @param providerEmail
   * @param providerName
   * @param feedbackDescription
   */
  public Feedback(int id, String providerEmail, String providerName, String feedbackDescription)
  {
    super();
    this.id = id;
    this.providerEmail = providerEmail;
    this.feedbackDescription = feedbackDescription;
    this.providerName = providerName;
    setTimestamp();
  }

  /** @return the id */
  public int getId()
  {
    return id;
  }

  /** @param id the id to set */
  public void setId(int id)
  {
    this.id = id;
  }

  /** @return the providerEmail */
  public String getProviderEmail()
  {
    return providerEmail;
  }

  /**
   * @param providerEmail the providerEmail to set
   * @throws InvalidAttributeValueException
   */
  public void setProviderEmail(String providerEmail) throws InvalidAttributeValueException
  {
    if (Validate.isValidEmailSyntax(providerEmail)) this.providerEmail = providerEmail;
    else {
      throw new InvalidAttributeValueException("This email address is not valid syntax.");
    }
  }

  /** @return the providerName */
  public String getProviderName()
  {
    return providerName;
  }

  /** @param providerName the providerName to set */
  public void setProviderName(String providerName)
  {
    this.providerName = providerName;
  }

  /** @return the feedbackDescription */
  public String getFeedbackDescription()
  {
    return feedbackDescription;
  }

  /** @param feedbackDescription the feedbackDescription to set */
  public void setFeedbackDescription(String feedbackDescription)
  {
    this.feedbackDescription = feedbackDescription;
  }

  /** @return the timestamp */
  public String getTimeStamp()
  {
    return timestamp;
  }

  /** Set timestamp to current time */
  public void setTimestamp()
  {
    this.timestamp = LocalDateTime.now(UK_TIMEZONE).toString();
  }

}
