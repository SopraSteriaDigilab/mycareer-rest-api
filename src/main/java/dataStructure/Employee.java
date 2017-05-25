package dataStructure;

import static utils.Validate.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import dataStructure.Objective.Progress;
import dataStructure.Competency.CompetencyTitle;
import dataStructure.DevelopmentNeed.Category;

/**
 * A Sopra Steria UK permanent employee and their MyCareer data. Used by Morphia for mapping from a MongoDB document.
 * Represents a top level document in MongoDB in the employees collection.
 * 
 * @see EmployeeController
 * @see EmployeeService
 * @see EmployeeProfile
 * @see Objective
 * @see DevelopmentNeed
 * @see Competency
 * @see Note
 * @see Feedback
 * @see FeedbackRequest
 * @see Rating
 * @see Activity
 */
@Entity("employees")
public class Employee implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * The field name used to store the employee's profile.
   * 
   * @see EmployeeProfile
   */
  public static final String PROFILE = "profile";

  /**
   * The field name used to store the employee's objectives.
   * 
   * @see Objective
   */
  public static final String OBJECTIVES = "objectives";

  /**
   * The field name used to store the employee's development needs.
   * 
   * @see DevelopmentNeed
   */
  public static final String DEVELOPMENT_NEEDS = "developmentNeeds";

  /**
   * The field name used to store the employee's competencies.
   * 
   * @see Competency
   */
  public static final String COMPETENCIES = "competencies";

  /**
   * The field name used to store the employee's notes.
   * 
   * @see Note
   */
  public static final String NOTES = "notes";

  /**
   * The field name used to store the employee's feedback.
   * 
   * @see Feedback
   */
  public static final String FEEDBACK = "feedback";

  /**
   * The field name used to store the employee's feedback requests.
   * 
   * @see FeedbackRequest
   */
  public static final String FEEDBACK_REQUESTS = "feedbackRequests";

  /**
   * The field name used to store the employee's ratings.
   * 
   * @see Rating
   */
  public static final String RATINGS = "ratings";

  /**
   * The field name used to store a string representation of the last time this employee logged on to MyCareer.
   */
  public static final String LAST_LOGON = "lastLogon";

  /**
   * The field name used to store the employee's recent activities.
   * 
   * @see Activity
   */
  public static final String ACTIVITY_FEED = "activityFeed";

  private static final int MAX_ACTIVITY_FEED_SIZE = 20;

  @Id
  private ObjectId id;

  @Embedded
  private EmployeeProfile profile;

  @Embedded
  private List<Objective> objectives;

  @Embedded
  private List<DevelopmentNeed> developmentNeeds;

  @Embedded
  private List<Competency> competencies;

  @Embedded
  private List<Note> notes;

  @Embedded
  private List<Feedback> feedback;

  @Embedded
  private List<FeedbackRequest> feedbackRequests;

  @Embedded
  private List<Rating> ratings;

  /* Date Property - Represents the date of the last logon for the user */
  private Date lastLogon;

  /* List<Activity> Property - Represents this employee's recent activities */
  @Embedded
  private List<Activity> activityFeed;

  /** Employee Constructor - No-args constructor responsible for initialising this object. */
  public Employee()
  {
    this.feedback = new ArrayList<Feedback>();
    this.objectives = new ArrayList<Objective>();
    this.notes = new ArrayList<Note>();
    this.developmentNeeds = new ArrayList<DevelopmentNeed>();
    this.feedbackRequests = new ArrayList<FeedbackRequest>();
    this.setCompetencies();
    this.ratings = new ArrayList<Rating>();
    this.activityFeed = new ArrayList<Activity>();
  }

  /**
   * 
   * Employee Constructor - Responsible for initialising this object.
   *
   * @param profile The employee's profile
   */
  public Employee(final EmployeeProfile profile)
  {
    this();
    this.profile = profile;
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////// OBJECTIVES METHODS FOLLOW ///////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * @return A list of this employee's current objectives.
   * @see Objective#isCurrent
   */
  public List<Objective> getCurrentObjectives()
  {
    final List<Objective> currentObjectives = objectives.stream().filter(Objective::isCurrent).sorted()
        .collect(Collectors.toList());

    return currentObjectives;
  }

  /**
   * Adds an objective to this employee.
   *
   * @param objective The objective to add.
   * @return {@code true} if the objective was successfully added. {@code false} otherwise.
   */
  public boolean addObjective(Objective objective)
  {
    objective.setId(nextObjectiveID());

    return this.objectives.add(objective);
  }

  /**
   * Edits one this employee's objective with the same ID number as the provided objective. The provided objective is
   * not edited.
   *
   * @param objective The objective whose ID number matches the objective to be edited.
   * @return {@code true} if the objective was successfully edited.
   * @throws InvalidAttributeValueException If an objective with the same ID number as the provided objective does not
   *           exist for this employee, or if it exists, but is archived or complete.
   */
  public boolean editObjective(Objective objective) throws InvalidAttributeValueException
  {
    Objective objectiveToEdit = getObjective(objective.getId());

    if (objectiveToEdit.getArchived() || objectiveToEdit.getProgress().equals(Progress.COMPLETE.getProgressStr()))
    {
      throw new InvalidAttributeValueException("Cannot Edit archived/complete Objective.");
    }

    objectiveToEdit.setDescription(objective.getDescription());
    objectiveToEdit.setTitle(objective.getTitle());
    objectiveToEdit.setDueDate(LocalDate.parse(objective.getDueDate()));

    return true;
  }

  /**
   * Deletes this employee's objective with the ID number {@code objectiveId}. Only archived objectives may be deleted.
   *
   * @param objectiveId The ID number of the objective to delete.
   * @return {@code true} if the objective was successfully deleted.
   * @throws InvalidAttributeValueException If an objective with the provided ID number does not exist for this
   *           employee, or if it exists but is not archived.
   */
  public boolean deleteObjective(int objectiveId) throws InvalidAttributeValueException
  {
    Objective objective = getObjective(objectiveId);

    if (!objective.getArchived())
    {
      throw new InvalidAttributeValueException("Objective must be archived before deleting.");
    }

    notes.forEach(n -> n.removeObjectiveTag(objectiveId));
    feedback.forEach(f -> f.removeObjectiveTag(objectiveId));
    objective.setLastModified();

    return this.getObjectives().remove(objective);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param objectiveId
   * @param progress
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean updateObjectiveProgress(int objectiveId, Progress progress) throws InvalidAttributeValueException
  {
    Objective objective = getObjective(objectiveId);

    if (objective.getProgress().equals(progress.getProgressStr()))
      throw new InvalidAttributeValueException("Progress is already at the given state: " + progress.getProgressStr());

    if (objective.getArchived() || objective.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot update progress of archived/complete Objective.");

    objective.setProgress(progress);

    return true;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param objectiveId
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean toggleObjectiveArchive(int objectiveId) throws InvalidAttributeValueException
  {
    Objective objective = getObjective(objectiveId);

    objective.isArchived(!objective.getArchived());

    return true;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param objectiveId
   * @return
   * @throws InvalidAttributeValueException
   */
  public Objective getObjective(int objectiveId) throws InvalidAttributeValueException
  {
    Optional<Objective> objective = getObjectives().stream().filter(o -> o.getId() == objectiveId).findFirst();

    if (!objective.isPresent()) throw new InvalidAttributeValueException("Objective not found.");

    return objective.get();
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public int nextObjectiveID()
  {
    int max = 0;
    for (Objective objective : this.getObjectives())
    {
      if (objective.getId() > max) max = objective.getId();
    }
    return ++max;
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////// DEVELOPMENT NEEDS METHODS FOLLOW ////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public List<DevelopmentNeed> getCurrentDevelopmentNeeds()
  {
    final List<DevelopmentNeed> currentDevelopmentNeeds = developmentNeeds.stream().filter(Objective::isCurrent)
        .sorted().collect(Collectors.toList());

    return currentDevelopmentNeeds;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param developmentNeed
   * @return
   */
  public boolean addDevelopmentNeed(DevelopmentNeed developmentNeed)
  {
    developmentNeed.setId(nextDevelopmentNeedID());

    return this.developmentNeeds.add(developmentNeed);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param developmentNeed
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean editDevelopmentNeed(DevelopmentNeed developmentNeed) throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeedToEdit = getDevelopmentNeed(developmentNeed.getId());

    if (developmentNeedToEdit.getArchived()
        || developmentNeedToEdit.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot Edit archived/complete Development Needs.");

    developmentNeedToEdit.setDescription(developmentNeed.getDescription());
    developmentNeedToEdit.setTitle(developmentNeed.getTitle());
    developmentNeedToEdit.setDueDate(LocalDate.parse(developmentNeed.getDueDate()));
    developmentNeedToEdit.setCategory(Category.getCategoryFromString(developmentNeed.getCategory()));

    return true;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param developmentNeedId
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean deleteDevelopmentNeed(int developmentNeedId) throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeed = getDevelopmentNeed(developmentNeedId);

    if (!developmentNeed.getArchived())
      throw new InvalidAttributeValueException("Development Need must be archived before deleting.");

    notes.forEach(n -> n.removeDevelopmentNeedTag(developmentNeedId));
    feedback.forEach(f -> f.removeDevelopmentNeedTag(developmentNeedId));
    developmentNeed.setLastModified();

    return this.getDevelopmentNeeds().remove(developmentNeed);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param developmentNeedId
   * @param progress
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean updateDevelopmentNeedProgress(int developmentNeedId, Progress progress)
      throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeed = getDevelopmentNeed(developmentNeedId);

    if (developmentNeed.getProgress().equals(progress.getProgressStr()))
      throw new InvalidAttributeValueException("Progress is already at the given state: " + progress.getProgressStr());

    if (developmentNeed.getArchived() || developmentNeed.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot update progress of archived/complete Development Needs.");

    developmentNeed.setProgress(progress);

    return true;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param developmentNeedId
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean toggleDevelopmentNeedArchive(int developmentNeedId) throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeed = getDevelopmentNeed(developmentNeedId);

    developmentNeed.isArchived(!developmentNeed.getArchived());

    return true;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param developmentNeedId
   * @return
   * @throws InvalidAttributeValueException
   */
  public DevelopmentNeed getDevelopmentNeed(int developmentNeedId) throws InvalidAttributeValueException
  {
    Optional<DevelopmentNeed> developmentNeed = getDevelopmentNeeds().stream()
        .filter(d -> d.getId() == developmentNeedId).findFirst();

    if (!developmentNeed.isPresent()) throw new InvalidAttributeValueException("Development Need not found.");

    return developmentNeed.get();
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public int nextDevelopmentNeedID()
  {
    int max = 0;
    for (DevelopmentNeed developmentNeed : this.getDevelopmentNeeds())
    {
      if (developmentNeed.getId() > max) max = developmentNeed.getId();
    }
    return ++max;
  }

  ///////////////////////////////////////////////////////////////////////////
  //////////////////// COMPETENCIES METHODS FOLLOW //////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * 
   * TODO: Describe this method.
   *
   * @param competencyTitle
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean toggleCompetency(CompetencyTitle competencyTitle) throws InvalidAttributeValueException
  {
    Competency competency = getCompetency(competencyTitle);

    competency.setSelected(!competency.isSelected());

    return true;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param competencyTitle
   * @return
   * @throws InvalidAttributeValueException
   */
  public Competency getCompetency(CompetencyTitle competencyTitle) throws InvalidAttributeValueException
  {
    Optional<Competency> competency = getCompetencies().stream()
        .filter(c -> c.getTitle().equals(competencyTitle.getCompetencyTitleStr())).findFirst();

    if (!competency.isPresent()) throw new InvalidAttributeValueException("Competency not found.");

    return competency.get();
  }

  ///////////////////////////////////////////////////////////////////////////
  //////////////////////// NOTES METHODS FOLLOW /////////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public List<Note> getCurrentNotes()
  {
    final List<Note> currentNotes = notes.stream().filter(Note::isCurrent).sorted().collect(Collectors.toList());

    return currentNotes;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param note
   * @return
   */
  public boolean addNote(Note note)
  {
    note.setId(this.getNotes().size() + 1);
    return this.notes.add(note);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param noteId
   * @param objectiveIds
   * @param developmentNeedIds
   * @throws InvalidAttributeValueException
   */
  public void updateNotesTags(int noteId, Set<Integer> objectiveIds, Set<Integer> developmentNeedIds)
      throws InvalidAttributeValueException
  {
    Note note = getNote(noteId);

    note.setTaggedObjectiveIds(objectiveIds);
    note.setTaggedDevelopmentNeedIds(developmentNeedIds);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param noteId
   * @return
   * @throws InvalidAttributeValueException
   */
  public Note getNote(int noteId) throws InvalidAttributeValueException
  {
    Optional<Note> note = getNotes().stream().filter(n -> n.getId() == noteId).findFirst();

    if (!note.isPresent()) throw new InvalidAttributeValueException("Note not found.");

    return note.get();
  }

  ///////////////////////////////////////////////////////////////////////////
  ////////////////////// FEEDBACK METHODS FOLLOW ////////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public List<Feedback> getCurrentFeedback()
  {
    final List<Feedback> currentFeedback = feedback.stream().filter(Feedback::isCurrent).sorted()
        .collect(Collectors.toList());

    return currentFeedback;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param feedback
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addFeedback(Feedback feedback) throws InvalidAttributeValueException
  {
    throwIfNull(feedback);
    return this.feedback.add(feedback);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public int nextFeedbackID()
  {
    return this.feedback.size() + 1;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param feedbackId
   * @param objectiveIds
   * @param developmentNeedIds
   * @throws InvalidAttributeValueException
   */
  public void updateFeedbackTags(int feedbackId, Set<Integer> objectiveIds, Set<Integer> developmentNeedIds)
      throws InvalidAttributeValueException
  {
    Feedback feedback = getFeedback(feedbackId);

    feedback.setTaggedObjectiveIds(objectiveIds);
    feedback.setTaggedDevelopmentNeedIds(developmentNeedIds);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param feedbackId
   * @return
   * @throws InvalidAttributeValueException
   */
  public Feedback getFeedback(int feedbackId) throws InvalidAttributeValueException
  {
    Optional<Feedback> feedback = getFeedback().stream().filter(f -> f.getId() == feedbackId).findFirst();

    if (!feedback.isPresent()) throw new InvalidAttributeValueException("Feedback not found.");

    return feedback.get();
  }

  ///////////////////////////////////////////////////////////////////////////
  ////////////////// FEEDBACK REQUESTS METHODS FOLLOW ///////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * @return A list of this employee's current feedback requests.
   */
  public List<FeedbackRequest> getCurrentFeedbackRequests()
  {
    final List<FeedbackRequest> currentFeedbackRequests = feedbackRequests.stream().filter(FeedbackRequest::isCurrent).sorted()
        .collect(Collectors.toList());

    return currentFeedbackRequests;
  }

  /**
   * @param id
   * @return Returns the feedback request with the given id.
   * @throws InvalidAttributeValueException
   */
  public FeedbackRequest getFeedbackRequest(String id) throws InvalidAttributeValueException
  {
    for (FeedbackRequest feedbackRequest : this.feedbackRequests)
    {
      if (feedbackRequest.getId().equals(id)) return feedbackRequest;
    }
    throw new InvalidAttributeValueException("Feedback Request does not exist.");
  }

  /**
   * Add feedback request to employee
   * 
   * @param feedbackRequest
   * @return true if it was added, false otherwise
   * @throws InvalidAttributeValueException if feedbackRequest is null
   */
  public boolean addFeedbackRequest(FeedbackRequest feedbackRequest) throws InvalidAttributeValueException
  {
    if (feedbackRequest == null) throw new InvalidAttributeValueException("This object is invalid.");

    return this.feedbackRequests.add(feedbackRequest);
  }

  ///////////////////////////////////////////////////////////////////////////
  /////////////////////// RATINGS METHODS FOLLOW ////////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * 
   * TODO: Describe this method.
   *
   * @param year
   * @param managerEvaluation
   * @param score
   * @throws InvalidAttributeValueException
   */
  public void addManagerEvaluation(int year, String managerEvaluation, int score) throws InvalidAttributeValueException
  {
    Rating rating = getRating(year);

    if (rating.isManagerEvaluationSubmitted())
    {
      throw new InvalidAttributeValueException(
          "The Manager evaluation has been submitted and can no longer be updated.");
    }

    rating.setManagerEvaluation(managerEvaluation);
    rating.setScore(score);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param year
   * @param selfEvaluation
   * @throws InvalidAttributeValueException
   */
  public void addSelfEvaluation(int year, String selfEvaluation) throws InvalidAttributeValueException
  {
    Rating rating = getRating(year);

    if (rating.isSelfEvaluationSubmitted() || rating.isManagerEvaluationSubmitted())
      throw new InvalidAttributeValueException("The self evaluation has been submitted and can no longer be updated.");

    if (rating.isManagerEvaluationSubmitted()) throw new InvalidAttributeValueException(
        "The manager evaluation has been submitted, the self evaluation can no longer be updated.");

    rating.setSelfEvaluation(selfEvaluation);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param year
   * @throws InvalidAttributeValueException
   */
  public void submitSelfEvaluation(int year) throws InvalidAttributeValueException
  {
    Rating rating = getRating(year);

    if (rating.isManagerEvaluationSubmitted()) throw new InvalidAttributeValueException(
        "The manager evaluation has been submitted, the self evaluation can no longer be submitted.");

    rating.setSelfEvaluationSubmitted(true);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param year
   * @throws InvalidAttributeValueException
   */
  public void submitManagerEvaluation(int year) throws InvalidAttributeValueException
  {
    Rating rating = getRating(year);

    if (rating.getScore() == 0) throw new InvalidAttributeValueException("You must enter a rating from 1 to 5.");

    rating.setManagerEvaluationSubmitted(true);
  }

  /**
   * Gets Rating with the requested year. If rating does not exists, it will create one.
   *
   * @param year
   * @return
   */
  public Rating getRating(int year)
  {
    Optional<Rating> rating = getRatings().stream().filter(r -> r.getYear() == year).findFirst();

    if (!rating.isPresent())
    {
      Rating r = new Rating(year);
      ratings.add(r);
      return r;
    }

    return rating.get();
  }

  ///////////////////////////////////////////////////////////////////////////
  ////////////////////// LAST LOGON METHODS FOLLOW //////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  // None!

  ///////////////////////////////////////////////////////////////////////////
  //////////////////// ACTIVITY FEED METHODS FOLLOW /////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Adds the given string to the activity feed queue, ensuring the max capacity of the queue is not exceeded.
   *
   * @param activityString the string to add to the queue
   * @return true if the string was successfully added to the queue
   */
  public boolean addActivity(final Activity activityString)
  {
    while (activityFeed.size() >= MAX_ACTIVITY_FEED_SIZE)
    {
      activityFeed.remove(0);
    }

    return activityFeed.add(activityString);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public Document getActivityFeedAsDocument()
  {
    List<Document> activityFeedList = new ArrayList<>();

    for (final Activity activity : activityFeed)
    {
      activityFeedList.add(activity.toDocument());
    }

    return new Document(ACTIVITY_FEED, activityFeedList);
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////// HORRIBLE GETTERS AND SETTERS FOLLOW /////////////////////
  ///////////////////////////////////////////////////////////////////////////

  /** @return The _id created by MongoDB when inserting this object in the Collection */
  public ObjectId getId()
  {
    return id;
  }

  /** @return the profile */
  public EmployeeProfile getProfile()
  {
    return profile;
  }

  /** @param profile The profile to set. */
  public void setProfile(EmployeeProfile profile)
  {
    this.profile = profile;
  }

  /** @return the newObjectives */
  public List<Objective> getObjectives()
  {
    Collections.sort(objectives);
    return objectives;
  }

  /** @param newObjectives The value to set. */
  public void setObjectives(List<Objective> newObjectives)
  {
    this.objectives = newObjectives;
  }

  /** @return the newDevelopmentNeeds */
  public List<DevelopmentNeed> getDevelopmentNeeds()
  {
    Collections.sort(developmentNeeds);
    return developmentNeeds;
  }

  /** @param newDevelopmentNeeds The value to set. */
  public void setDevelopmentNeeds(List<DevelopmentNeed> newDevelopmentNeeds)
  {
    this.developmentNeeds = newDevelopmentNeeds;
  }

  /** @return the newCompetencies */
  public List<Competency> getCompetencies()
  {
    Collections.sort(competencies);
    return competencies;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   */
  public void setCompetencies()
  {
    if (competencies == null)
    {
      this.competencies = new ArrayList<Competency>();
      int id = 0;
      for (CompetencyTitle competenctyTitle : CompetencyTitle.values())
      {
        this.competencies.add(new Competency(id++, competenctyTitle));
      }
    }
  }

  /** @return the notes */
  public List<Note> getNotes()
  {
    return notes;
  }

  /** @param newNotes */
  public void setNotes(List<Note> notes)
  {
    this.notes = notes;
  }

  /** @return the feedback */
  public List<Feedback> getFeedback()
  {
    return this.feedback;
  }

  /** @param feedback The feedback to set. */
  public void setFeedback(List<Feedback> feedback)
  {
    this.feedback = feedback;
  }

  /**
   * 
   * This method returns the list of feedbackRequests
   * 
   * @return
   */
  public List<FeedbackRequest> getFeedbackRequests()
  {
    return this.feedbackRequests;
  }

  /**
   * @param data the list of feedback request object
   * @throws InvalidAttributeValueException
   */
  public void setFeedbackRequests(List<FeedbackRequest> feedbackRequestList) throws InvalidAttributeValueException
  {

    if (feedbackRequestList == null /* || feedbackRequestList.isEmpty() */)
      throw new InvalidAttributeValueException("The list is invalid. Please try again with a valid list.");

    for (FeedbackRequest feedbackRequest : feedbackRequestList)
    {
      if (feedbackRequest == null) throw new InvalidAttributeValueException(
          "One or more items in this list is invalid. Please try again with a valid list.");
    }

    this.feedbackRequests = new ArrayList<>(feedbackRequestList);
  }

  /** @return the ratings */
  public List<Rating> getRatings()
  {
    return ratings;
  }

  /** @param ratings The value to set. */
  public void setRatings(List<Rating> ratings)
  {
    this.ratings = ratings;
  }

  /** @return the last login */
  public Date getLastLogon()
  {
    return this.lastLogon;
  }

  /** @param lastLoginDate The value to set the named property to. */
  public void setLastLogon(Date lastLogon)
  {
    this.lastLogon = lastLogon;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public List<Activity> getActivityFeed()
  {
    return activityFeed;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param activityFeed
   */
  public void setActivityFeed(final List<Activity> activityFeed)
  {
    this.activityFeed = activityFeed;
  }
}
