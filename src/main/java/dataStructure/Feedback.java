package dataStructure;

import static utils.Conversions.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class Feedback extends DBObject implements Comparable<Feedback>
{
  private static final long serialVersionUID = 1L;

  /** TODO describe */
  public static final String ID = "feedback.id";

  /** TODO describe */
  public static final String LAST_MODIFIED = "feedback.lastModified";

  /** TODO describe */
  public static final String PROVIDER_EMAIL = "feedback.providerEmail";

  /** TODO describe */
  public static final String PROVIDER_NAME = "feedback.providerName";

  /** TODO describe */
  public static final String DESCRIPTION = "feedback.feedbackDescription";

  /** TODO describe */
  public static final String TAGGED_OBJECTIVES = "feedback.taggedObjectiveIds";

  /** TODO describe */
  public static final String TAGGED_DEVELOPMENT_NEEDS = "feedback.taggedDevelopmentNeedIds";

  /** TODO describe */
  public static final String TIMESTAMP = "feedback.timestamp";

  /* Email address of feedback provider */
  private String providerEmail;

  /* Name of feedback provider */
  private String providerName;

  /* The feedback */
  private String feedbackDescription;

  /* The objective ids tagged */
  private Set<Integer> taggedObjectiveIds;

  /* The development need ids tagged */
  private Set<Integer> taggedDevelopmentNeedIds;

  /* Time stamp of feedback */
  private String timestamp;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   */
  public Feedback()
  {
    taggedObjectiveIds = new HashSet<>();
    taggedDevelopmentNeedIds = new HashSet<>();
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
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
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
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
   * @return {@code true} if the developmentNeedId existed in the map and was succesfully removed. {@code false}
   *         otherwise.
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

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public boolean isCurrent()
  {
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
  public int compareTo(final Feedback other)
  {
    final LocalDateTime thisTimestamp = LocalDateTime.parse(timestamp);
    final LocalDateTime otherTimestamp = LocalDateTime.parse(other.timestamp);

    return thisTimestamp.compareTo(otherTimestamp);
  }
}
