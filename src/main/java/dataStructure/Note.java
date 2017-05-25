package dataStructure;

import static utils.Conversions.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the definition of the Note object
 */
public class Note implements Serializable, Comparable<Note>
{
  private static final long serialVersionUID = 1L;

  /** TODO describe */
  public static final String ID = "notes.id";

  /** TODO describe */
  public static final String PROVIDER_NAME = "notes.providerName";

  /** TODO describe */
  public static final String DESCRIPTION = "notes.noteDescription";

  /** TODO describe */
  public static final String TAGGED_OBJECTIVES = "notes.taggedObjectiveIds";

  /** TODO describe */
  public static final String TAGGED_DEVELOPMENT_NEEDS = "notes.taggedDevelopmentNeedIds";

  /** TODO describe */
  public static final String TIMESTAMP = "notes.timestamp";

  private static final String NOTE = "note";

  /* int Property - Represents Unique ID for the object. */
  private int id;

  /* String Property - Represents name of the note provider. */
  private String providerName;

  /* String Property - Represents the description of the note. */
  private String noteDescription;

  /* The objective ids tagged */
  private Set<Integer> taggedObjectiveIds;

  /* The development need ids tagged */
  private Set<Integer> taggedDevelopmentNeedIds;

  /* String Property - Represents the timestamp of the note. */
  private String timestamp;

  /**
   * No-args constructor - Responsible for initialising this object.
   */
  public Note()
  {
    taggedObjectiveIds = new HashSet<>();
    taggedDevelopmentNeedIds = new HashSet<>();
  }

  /**
   * Note Constructor - Responsible for initialising this object.
   *
   * @param note
   */
  public Note(String providerName, String noteDescription)
  {
    this.setProviderName(providerName);
    this.setNoteDescription(noteDescription);
    this.setTimestamp();
    taggedObjectiveIds = new HashSet<>();
    taggedDevelopmentNeedIds = new HashSet<>();
  }

  /** @return the id */
  public int getId()
  {
    return id;
  }

  /** @param id */
  public void setId(int id)
  {
    this.id = id;
  }

  /** @return the id */
  public String getProviderName()
  {
    return providerName;
  }

  /** @param providerName */
  public void setProviderName(String providerName)
  {
    this.providerName = providerName;
  }

  /** @return the id */
  public String getNoteDescription()
  {
    return noteDescription;
  }

  /** @param noteDescription */
  public void setNoteDescription(String noteDescription)
  {
    this.noteDescription = noteDescription;
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

  /** @param timestamp */
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
   * @param activityType
   * @param profile
   * @return
   */
  public Activity createActivity(final Action activityType, final EmployeeProfile profile)
  {
    final String activityString = new StringBuilder(profile.getFullName()).append(" ").append(activityType.getVerb())
        .append(" ").append(NOTE).append(" #").append(getId()).append(": ").append(noteDescription).toString();

    return new Activity(activityString, timestamp);
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
  public int compareTo(final Note other)
  {
    final LocalDateTime thisTimestamp = LocalDateTime.parse(timestamp);
    final LocalDateTime otherTimestamp = LocalDateTime.parse(other.timestamp);

    return thisTimestamp.compareTo(otherTimestamp);
  }
}
