package dataStructure;

import static utils.Validate.isNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

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
 * This class contains the definition of the Employee object
 *
 */
@Entity("employeeDataDev")
public class Employee implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static final String PROFILE = "profile";
  public static final String OBJECTIVES = "objectives";
  public static final String DEVELOPMENT_NEEDS = "developmentNeeds";
  public static final String COMPETENCIES = "competencies";
  public static final String NOTES = "notes";
  public static final String FEEDBACK = "feedback";
  public static final String FEEDBACK_REQUESTS = "feedbackRequests";
  public static final String RATINGS = "ratings";
  public static final String LAST_LOGON = "lastLogon";
  public static final String ACTIVITY_FEED = "activityFeed";

  private static final int MAX_ACTIVITY_FEED_SIZE = 50;

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

  /** Date Property - Represents the date of the last logon for the user */
  private Date lastLogon;

  /** Queue<String> Property - Represents the last 50 actions performed by this employee */
  @Embedded
  private List<Activity> activityFeed;

  /** Default Constructor - Responsible for initialising this object. */
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

  /** Default Constructor - Responsible for initialising this object. */
  public Employee(final EmployeeProfile profile)
  {
    this();
    this.profile = profile;
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////// OBJECTIVES METHODS FOLLOW ///////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  public boolean addObjectiveNEW(Objective objective)
  {
    objective.setId(nextObjectiveID());

    return this.objectives.add(objective);
  }

  public boolean editObjectiveNEW(Objective objective) throws InvalidAttributeValueException
  {

    Objective objectiveToEdit = getObjectiveNEW(objective.getId());

    if (objectiveToEdit.getArchived() || objectiveToEdit.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot Edit archived/complete Objective.");

    objectiveToEdit.setDescription(objective.getDescription());
    objectiveToEdit.setTitle(objective.getTitle());
    objectiveToEdit.setDueDate(LocalDate.parse(objective.getDueDate()));

    return true;
  }

  public boolean deleteObjectiveNEW(int objectiveId) throws InvalidAttributeValueException
  {
    Objective objective = getObjectiveNEW(objectiveId);

    if (!objective.getArchived())
    {
      throw new InvalidAttributeValueException("Objective must be archived before deleting.");
    }

    notes.forEach(n -> n.removeObjectiveTag(objectiveId));
    feedback.forEach(f -> f.removeObjectiveTag(objectiveId));

    return this.getObjectives().remove(objective);
  }

  public boolean updateObjectiveNEWProgress(int objectiveId, Progress progress) throws InvalidAttributeValueException
  {
    Objective objective = getObjectiveNEW(objectiveId);

    if (objective.getProgress().equals(progress.getProgressStr()))
      throw new InvalidAttributeValueException("Progress is already at the given state: " + progress.getProgressStr());

    if (objective.getArchived() || objective.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot update progress of archived/complete Objective.");

    objective.setProgress(progress);

    return true;
  }

  public boolean toggleObjectiveNEWArchive(int objectiveId) throws InvalidAttributeValueException
  {
    Objective objective = getObjectiveNEW(objectiveId);

    objective.isArchived(!objective.getArchived());

    return true;
  }

  public Objective getObjectiveNEW(int objectiveId) throws InvalidAttributeValueException
  {
    Optional<Objective> objective = getObjectives().stream().filter(o -> o.getId() == objectiveId).findFirst();

    if (!objective.isPresent()) throw new InvalidAttributeValueException("Objective not found.");

    return objective.get();
  }

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

  public boolean addDevelopmentNeedNEW(DevelopmentNeed developmentNeed)
  {
    developmentNeed.setId(nextDevelopmentNeedID());

    return this.developmentNeeds.add(developmentNeed);
  }

  public boolean editDevelopmentNeedNEW(DevelopmentNeed developmentNeed) throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeedToEdit = getDevelopmentNeedNEW(developmentNeed.getId());

    if (developmentNeedToEdit.getArchived()
        || developmentNeedToEdit.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot Edit archived/complete Development Needs.");

    developmentNeedToEdit.setDescription(developmentNeed.getDescription());
    developmentNeedToEdit.setTitle(developmentNeed.getTitle());
    developmentNeedToEdit.setDueDate(LocalDate.parse(developmentNeed.getDueDate()));
    developmentNeedToEdit.setCategory(Category.getCategoryFromString(developmentNeed.getCategory()));

    return true;
  }

  public boolean deleteDevelopmentNeedNEW(int developmentNeedId) throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeed = getDevelopmentNeedNEW(developmentNeedId);

    if (!developmentNeed.getArchived())
      throw new InvalidAttributeValueException("Development Need must be archived before deleting.");

    notes.forEach(n -> n.removeDevelopmentNeedTag(developmentNeedId));
    feedback.forEach(f -> f.removeDevelopmentNeedTag(developmentNeedId));

    return this.getDevelopmentNeeds().remove(developmentNeed);
  }

  public boolean updateDevelopmentNeedNEWProgress(int developmentNeedId, Progress progress)
      throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeed = getDevelopmentNeedNEW(developmentNeedId);

    if (developmentNeed.getProgress().equals(progress.getProgressStr()))
      throw new InvalidAttributeValueException("Progress is already at the given state: " + progress.getProgressStr());

    if (developmentNeed.getArchived() || developmentNeed.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot update progress of archived/complete Development Needs.");

    developmentNeed.setProgress(progress);

    return true;
  }

  public boolean toggleDevelopmentNeedNEWArchive(int developmentNeedId) throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeed = getDevelopmentNeedNEW(developmentNeedId);

    developmentNeed.isArchived(!developmentNeed.getArchived());

    return true;
  }

  public DevelopmentNeed getDevelopmentNeedNEW(int developmentNeedId) throws InvalidAttributeValueException
  {
    Optional<DevelopmentNeed> developmentNeed = getDevelopmentNeeds().stream()
        .filter(d -> d.getId() == developmentNeedId).findFirst();

    if (!developmentNeed.isPresent()) throw new InvalidAttributeValueException("Development Need not found.");

    return developmentNeed.get();
  }

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

  public boolean toggleCompetencyNEW(CompetencyTitle competencyTitle) throws InvalidAttributeValueException
  {
    Competency competency = getCompetencyNEW(competencyTitle);

    competency.setSelected(!competency.isSelected());

    return true;
  }

  public Competency getCompetencyNEW(CompetencyTitle competencyTitle) throws InvalidAttributeValueException
  {
    Optional<Competency> competency = getCompetencies().stream()
        .filter(c -> c.getTitle().equals(competencyTitle.getCompetencyTitleStr())).findFirst();

    if (!competency.isPresent()) throw new InvalidAttributeValueException("Competency not found.");

    return competency.get();
  }

  ///////////////////////////////////////////////////////////////////////////
  //////////////////////// NOTES METHODS FOLLOW /////////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  public boolean addNote(Note note)
  {
    note.setId(this.getNotes().size() + 1);
    return this.notes.add(note);
  }

  public void updateNotesTags(int noteId, Set<Integer> objectiveIds, Set<Integer> developmentNeedIds)
      throws InvalidAttributeValueException
  {
    Note note = getNote(noteId);

    note.setTaggedObjectiveIds(objectiveIds);
    note.setTaggedDevelopmentNeedIds(developmentNeedIds);
  }

  public Note getNote(int noteId) throws InvalidAttributeValueException
  {
    Optional<Note> note = getNotes().stream().filter(n -> n.getId() == noteId).findFirst();

    if (!note.isPresent()) throw new InvalidAttributeValueException("Note not found.");

    return note.get();
  }

  ///////////////////////////////////////////////////////////////////////////
  ////////////////////// FEEDBACK METHODS FOLLOW ////////////////////////////
  ///////////////////////////////////////////////////////////////////////////

  public boolean addFeedback(Feedback feedback) throws InvalidAttributeValueException
  {
    isNull(feedback);
    return this.feedback.add(feedback);
  }

  public int nextFeedbackID()
  {
    return this.feedback.size() + 1;
  }

  public void updateFeedbackTags(int feedbackId, Set<Integer> objectiveIds, Set<Integer> developmentNeedIds)
      throws InvalidAttributeValueException
  {
    Feedback feedback = getFeedback(feedbackId);

    feedback.setTaggedObjectiveIds(objectiveIds);
    feedback.setTaggedDevelopmentNeedIds(developmentNeedIds);
  }

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

  public void addSelfEvaluation(int year, String selfEvaluation) throws InvalidAttributeValueException
  {
    Rating rating = getRating(year);

    if (rating.isSelfEvaluationSubmitted())
      throw new InvalidAttributeValueException("The self evaluation has been submitted and can no longer be updated.");

    rating.setSelfEvaluation(selfEvaluation);
  }

  public void submitSelfEvaluation(int year)
  {
    Rating rating = getRating(year);
    rating.setSelfEvaluationSubmitted(true);
  }

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

  public List<Activity> getActivityFeed()
  {
    return activityFeed;
  }

  public void setActivityFeed(final List<Activity> activityFeed)
  {
    this.activityFeed = activityFeed;
  }
}
