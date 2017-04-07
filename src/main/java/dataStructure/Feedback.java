package dataStructure;

import static dataStructure.Constants.UK_TIMEZONE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

/**
 * This class contains the definition of the feedback in MyCareer.
 */
public class Feedback extends DBObject
{
  private static final long serialVersionUID = 1L;

  /** Email address of feedback provider */
  private String providerEmail;

  /** Name of feedback provider */
  private String providerName;

  /** The feedback */
  private String feedbackDescription;

  /** The objective ids tagged */
  private Set<Integer> taggedObjectiveIds;

  /** The development need ids tagged */
  private Set<Integer> taggedDevelopmentNeedIds;

  /** Time stamp of feedback */
  private String timestamp;

  /** Empty Constructor */
  public Feedback()
  {
    taggedObjectiveIds = new HashSet<>();
    taggedDevelopmentNeedIds = new HashSet<>();
  }

  /**
   * @param id
   * @param providerEmail
   * @param feedbackDescription
   */
  public Feedback(int id, String providerEmail, String feedbackDescription)
  {
    super();
    this.setId(id);
    this.setProviderEmail(providerEmail);
    this.setFeedbackDescription(feedbackDescription);
    this.setProviderName("");
    this.setTimestamp();
    taggedObjectiveIds = new HashSet<>();
    taggedDevelopmentNeedIds = new HashSet<>();
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
    this.setId(id);
    this.setProviderEmail(providerEmail);
    this.setFeedbackDescription(feedbackDescription);
    this.setProviderName(providerName);
    this.setTimestamp();
    taggedObjectiveIds = new HashSet<>();
    taggedDevelopmentNeedIds = new HashSet<>();
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
  public void setProviderEmail(String providerEmail)
  {
    this.providerEmail = providerEmail;
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

  /** @return the taggedObjectiveIds */
  public Set<Integer> getTaggedObjectiveIds()
  {
    return taggedObjectiveIds;
  }

  /** @param taggedObjectiveIds The value to set. */
  public void setTaggedObjectiveIds(Set<Integer> taggedObjectiveIds)
  {
    this.taggedObjectiveIds = taggedObjectiveIds;
  }

  /** @return the taggedDevelopmentNeedIds */
  public Set<Integer> getTaggedDevelopmentNeedIds()
  {
    return taggedDevelopmentNeedIds;
  }

  /** @param taggedDevelopmentNeedIds The value to set. */
  public void setTaggedDevelopmentNeedIds(Set<Integer> taggedDevelopmentNeedIds)
  {
    this.taggedDevelopmentNeedIds = taggedDevelopmentNeedIds;
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
   * Removes a development need from taggedDevelopmentNeedIds.
   *
   * @param id
   * @return {@code true} if the developmentNeedId existed in the map and was succesfully removed. {@code false} otherwise.
   */
  public boolean removeDevelopmentNeedTag(final Integer id)
  {
    return taggedDevelopmentNeedIds.remove(id);
  }
  
  /**
   * Removes an objective from taggedObjectiveIds.
   *
   * @param id
   * @return {@code true} if the objectiveId existed in the map and was succesfully removed. {@code false} otherwise.
   */
  public boolean removeObjectiveTag(final Integer id)
  {
    return taggedObjectiveIds.remove(id);
  }

}
