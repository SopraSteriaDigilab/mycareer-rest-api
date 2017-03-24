package dataStructure;

import static utils.Validate.isNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;

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
  private static final String INVALID_OBJECTIVE_ID = "No objective ID matches the user data";

  // Global Variables
  @Id
  private ObjectId id;

  @Embedded
  private EmployeeProfile profile;

  @Embedded
  private List<Feedback> feedback;

  @Embedded
  private List<List<Objective_OLD>> objectives;

  @Embedded
  private List<Objective> newObjectives;

  @Embedded
  private List<Note> notes;

  @Embedded
  private List<List<DevelopmentNeed_OLD>> developmentNeeds;

  @Embedded
  private List<DevelopmentNeed> newDevelopmentNeeds;

  @Embedded
  private List<FeedbackRequest> feedbackRequests;

  @Embedded
  private List<List<Competency_OLD>> competencies;

  @Embedded
  private List<Competency> newCompetencies;

  /** Date Property - Represents the date of the last logon for the user */
  private Date lastLogon;

  /** Default Constructor - Responsible for initialising this object. */
  public Employee()
  {
    this.feedback = new ArrayList<Feedback>();
    this.objectives = new ArrayList<List<Objective_OLD>>();
    this.newObjectives = new ArrayList<Objective>();
    this.notes = new ArrayList<Note>();
    this.developmentNeeds = new ArrayList<List<DevelopmentNeed_OLD>>();
    this.newDevelopmentNeeds = new ArrayList<DevelopmentNeed>();
    this.feedbackRequests = new ArrayList<FeedbackRequest>();
    this.competencies = new ArrayList<List<Competency_OLD>>();
    this.setCompetenciesNEW();
  }

  /** Default Constructor - Responsible for initialising this object. */
  public Employee(final EmployeeProfile profile)
  {
    this();
    this.profile = profile;
  }

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
   * @param objectives the list of objectives to assign to an employee
   * @throws InvalidAttributeValueException
   */
  public void setObjectiveList(List<List<Objective_OLD>> objectives) throws InvalidAttributeValueException
  {
    if (objectives != null)
    {
      // Counter that keeps tracks of the error while adding elements
      int errorCounter = 0;
      this.objectives = new ArrayList<List<Objective_OLD>>();
      // Verify if each each objective is valid
      for (int i = 0; i < objectives.size(); i++)
      {
        // Add a new List to the list of Objectives
        this.objectives.add(new ArrayList<Objective_OLD>());
        if (objectives.get(i) != null)
        {
          for (int j = 0; j < objectives.get(i).size(); j++)
          {
            if (objectives.get(i).get(j).isObjectiveValid())
              this.objectives.get(this.objectives.size() - 1).add(objectives.get(i).get(j));
            else errorCounter++;
          }
        }
        else throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVE);
      }
      // Verify if there were errors during the import of objectives
      if (errorCounter != 0) throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVELIST);
    }
    else throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVELIST);
  }

  public List<List<Objective_OLD>> getObjectiveList()
  {
    return this.objectives;
  }

  /**
   * 
   * @return a list containing only the latest version of each objective
   */
  public List<Objective_OLD> getLatestVersionObjectives()
  {
    List<Objective_OLD> organisedList = new ArrayList<Objective_OLD>();
    if (objectives == null) return null;
    else
    {
      // If the list if not empty, retrieve all the elements and add them to the list
      // that is going to be returned
      for (List<Objective_OLD> subList : objectives)
      {
        // The last element contains the latest version for the objective
        organisedList.add(subList.get(subList.size() - 1));
      }

      // Sort list by timeToCompelte
      sortObjectivesByTimeToComplete(organisedList);

      // Once the list if full, return it to the user
      return organisedList;
    }
  }

  /**
   * 
   * @param id id of the objective to search
   * @return the objective data
   * @throws InvalidAttributeValueException
   */
  public Objective_OLD getLatestVersionOfSpecificObjective(int id) throws InvalidAttributeValueException
  {
    // Search for the objective with the given ID
    for (List<Objective_OLD> subList : objectives)
    {
      if ((subList.get(0)).getID() == id)
      {
        // Now that the Objective has been found, return the latest version of it
        // which is stored at the end of the List
        return (subList.get(subList.size() - 1));
      }
    }
    throw new InvalidAttributeValueException(INVALID_OBJECTIVE_ID);
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

  /**
   * 
   * This method inserts all the development needs from another list, validating each element before inserting
   * 
   * @param developments the List<List<DevelopmentNeed> to copy the data from
   * @throws InvalidAttributeValueException
   */
  public void setDevelopmentNeedsList(List<List<DevelopmentNeed_OLD>> developments) throws InvalidAttributeValueException
  {
    if (developments != null)
    {
      // Counter that keeps tracks of the error while adding elements
      int errorCounter = 0;
      this.developmentNeeds = new ArrayList<List<DevelopmentNeed_OLD>>();
      // Verify if each development need is valid
      for (int i = 0; i < developments.size(); i++)
      {
        // Add a new List to the list of developmentNeeds
        this.developmentNeeds.add(new ArrayList<DevelopmentNeed_OLD>());
        if (developments.get(i) != null)
        {
          for (int j = 0; j < developments.get(i).size(); j++)
          {
            if (developments.get(i).get(j).isDevelopmentNeedValid())
              this.developmentNeeds.get(this.developmentNeeds.size() - 1).add(developments.get(i).get(j));
            else errorCounter++;
          }
        }
        else throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEED_CONTEXT);
      }
      // Verify if there were errors during the import of development needs
      if (errorCounter != 0) throw new InvalidAttributeValueException(Constants.INVALID_DEVNEEDSLIST_CONTEXT);
    }
    else throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEEDSLIST_CONTEXT);
  }

  /**
   * 
   * This method returns the list of development needs
   * 
   * @return List<List<DevelopmentNeed>
   */
  public List<List<DevelopmentNeed_OLD>> getDevelopmentNeedsList()
  {
    return this.developmentNeeds;
  }

  /**
   * 
   * This method returns the latest version saved in the system of each development need
   * 
   * @return List<DevelopmentNeed>
   */
  public List<DevelopmentNeed_OLD> getLatestVersionDevelopmentNeeds()
  {
    List<DevelopmentNeed_OLD> organisedList = new ArrayList<DevelopmentNeed_OLD>();
    if (developmentNeeds == null) return null;
    else
    {
      // If the list if not empty, retrieve all the elements and add them to the list
      // that is going to be returned
      for (List<DevelopmentNeed_OLD> subList : developmentNeeds)
      {
        // The last element contains the latest version of the development need
        organisedList.add(subList.get(subList.size() - 1));
      }
      // Sort list by timeToCompelte
      sortDevNeedsByTimeToComplete(organisedList);

      // Once the list if full, return it to the user
      return organisedList;
    }
  }

  /**
   * @throws InvalidAttributeValueException
   * 
   *           This method returns the latest version of a specific development need, given its ID
   * 
   * @param id development need ID @return the DevelopmentNeed data object @throws
   */
  public DevelopmentNeed_OLD getLatestVersionOfSpecificDevelopmentNeed(int id) throws InvalidAttributeValueException
  {
    // Verify if the id is valid
    if (id < 0) throw new InvalidAttributeValueException(Constants.INVALID_DEVNEEDID_CONTEXT);
    // Search for the development need with the given ID
    for (List<DevelopmentNeed_OLD> subList : developmentNeeds)
    {
      if ((subList.get(0)).getID() == id)
      {
        // Now that the development need has been found, return the latest version of it
        // which is stored at the end of the List
        return (subList.get(subList.size() - 1));
      }
    }
    return null;
  }

  /**
   * @param data the list of feedback request object
   * @throws InvalidAttributeValueException
   */
  public void setFeedbackRequestsList(List<FeedbackRequest> feedbackRequestList) throws InvalidAttributeValueException
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

  /**
   * 
   * This method returns the list of feedbackRequests
   * 
   * @return
   */
  public List<FeedbackRequest> getFeedbackRequestsList()
  {
    return this.feedbackRequests;
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

  // /**
  // *
  // * This method retrieves a specific feedback request based
  // *
  // * @param id
  // * @return
  // * @throws InvalidAttributeValueException
  // */
  // public String removeSpecificFeedbackRequest(String fbID) throws InvalidAttributeValueException{
  // if(fbID!=null && !fbID.equals("")){
  // for(int i=0; i<groupFeedbackRequests.size(); i++){
  // if(groupFeedbackRequests.get(i).searchFeedbackRequestID(fbID)!=null){
  // //Remove the full group Request Feedback if it contains only 1 feedback request
  // String emailRecipient=groupFeedbackRequests.get(i).searchFeedbackRequestID(fbID).getRecipient();
  // if(groupFeedbackRequests.get(i).getRequestList().size()==1)
  // groupFeedbackRequests.remove(i);
  // //Alternatively, remove the given feedback request
  // else
  // groupFeedbackRequests.get(i).removeFeedbackRequest(fbID);
  // //Return the email address found
  // return emailRecipient;
  // }
  // }
  // throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_NOTFOUND_CONTEXT);
  // }
  // throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_ID_CONTEXT);
  // }

  /**
   * 
   * This method inserts all the competencies from another list, validating each element before inserting
   * 
   * @param developments the List<List<Competencies> to copy the data from
   * @throws InvalidAttributeValueException
   */
  public void setCompetenciesList(List<List<Competency_OLD>> comps) throws InvalidAttributeValueException
  {
    if (comps != null)
    {
      // Counter that keeps tracks of the error while adding elements
      int errorCounter = 0;
      this.competencies = new ArrayList<List<Competency_OLD>>();
      // Verify if each development need is valid
      for (int i = 0; i < comps.size(); i++)
      {
        // Add a new List to the list of competencies
        this.competencies.add(new ArrayList<Competency_OLD>());
        if (comps.get(i) != null)
        {
          for (int j = 0; j < comps.get(i).size(); j++)
          {
            if (!this.competencies.get(this.competencies.size() - 1).add(comps.get(i).get(j))) errorCounter++;
          }
        }
        else throw new InvalidAttributeValueException(Constants.INVALID_NULLCOMPETENECYLIST_CONTEXT);
      }
      // Verify if there have been any error during the insertion of competencies
      if (errorCounter > 0) throw new InvalidAttributeValueException(Constants.INVALID_COMPETENCYLIST_CONTEXT);
    }
    else throw new InvalidAttributeValueException(Constants.INVALID_NULLCOMPETENECYLIST_CONTEXT);
  }

  /**
   * 
   * This method returns a list of competencies
   * 
   * @return List<List<competencies>
   */
  public List<List<Competency_OLD>> getCompetenciesList()
  {
    if (this.competencies.size() == 0)
    {
      int index = 0;
      while (competencies.size() < Constants.COMPETENCY_NAMES.length)
      {
        List<Competency_OLD> tempList = new ArrayList<Competency_OLD>();
        tempList.add(new Competency_OLD(index++, false));
        competencies.add(tempList);
      }
    }
    return this.competencies;
  }

  /**
   * 
   * This method returns the latest version saved in the system of the list of competencies
   * 
   * @return List<Competencies>
   */
  public List<Competency_OLD> getLatestVersionCompetencies()
  {
    List<Competency_OLD> organisedList = new ArrayList<Competency_OLD>();
    if (this.competencies.size() == 0)
    {
      int index = 0;
      while (competencies.size() < Constants.COMPETENCY_NAMES.length)
      {
        List<Competency_OLD> tempList = new ArrayList<Competency_OLD>();
        tempList.add(new Competency_OLD(index++, false));
        competencies.add(tempList);
      }
    }
    // If the list if not empty, retrieve all the elements and add them to the list
    // that is going to be returned
    for (int i = 0; i < Constants.COMPETENCY_NAMES.length; i++)
    {
      try
      {
        List<Competency_OLD> subList = competencies.get(i);
        // The last element contains the latest version of the development need
        Competency_OLD temp = subList.get(subList.size() - 1);
        // Add a title and a description to the competency
        temp.setTitle(i);
        temp.setDescription(i);
        organisedList.add(temp);
      }
      catch (Exception e)
      {
      }
    }
    // Now that all the elements are retrieved, let's sort them
    List<Competency_OLD> selected = new ArrayList<>();
    List<Competency_OLD> notSelected = new ArrayList<>();
    // Split the elements between selected and not selected
    for (Competency_OLD c : organisedList)
    {
      if (c.getIsSelected()) selected.add(c);
      else notSelected.add(c);
    }
    // Once this is done, let's sort them alphabetically (I USED LAMBDA FOR A MORE EFFICIENT CODE :) )
    selected.sort((Competency_OLD c1, Competency_OLD c2) -> c1.getID() - c2.getID());
    notSelected.sort((Competency_OLD c1, Competency_OLD c2) -> c1.getID() - c2.getID());
    // Add these 2 lists to the list to return to the user
    organisedList.clear();
    organisedList.addAll(selected);
    organisedList.addAll(notSelected);
    // Once the list if full, return it to the user
    return organisedList;
  }

  /**
   * 
   * This method returns the latest version of a specific Competency, given its ID
   * 
   * @param id Competency need ID
   * @return the Competency data object
   * @throws InvalidAttributeValueException
   */
  public Competency_OLD getLatestVersionOfSpecificCompetency(int id) throws InvalidAttributeValueException
  {
    // Verify if the id is valid
    if (id < 0) return null;
    int index = 0;
    // Search for the Competency with the given ID
    for (List<Competency_OLD> subList : competencies)
    {
      if ((subList.get(0)).getID() == id)
      {
        // Now that the competency has been found, return the latest version of it
        // which is stored at the end of the List
        Competency_OLD temp = subList.get(subList.size() - 1);
        temp.setTitle(index);
        temp.setDescription(index);
        return (temp);
      }
      index++;
    }
    throw new InvalidAttributeValueException(Constants.INVALID_COMPETENCYTID_CONTEXT);
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

  public boolean addFeedback(Feedback feedback) throws InvalidAttributeValueException
  {
    isNull(feedback);
    return this.feedback.add(feedback);
  }

  /**
   * 
   * @param obj objective data
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean addObjective(Objective_OLD obj) throws InvalidAttributeValueException
  {
    if (objectives == null) objectives = new ArrayList<List<Objective_OLD>>();
    // Verify that the objective is not null
    if (obj == null) throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVE);
    // At this point, the objective hasn't got an ID, let's create one
    obj.setID(objectives.size() + 1);
    if (obj.isObjectiveValid())
    {
      List<Objective_OLD> tempList = new ArrayList<Objective_OLD>();
      // add the first version of this objective
      boolean res1 = tempList.add(obj);
      boolean res2 = objectives.add(tempList);
      // Action completed, verify the results
      return (res1 && res2);
    }
    throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVE);
  }

  /**
   * This method adds a new updated version of the objective
   * 
   * @param obj objective data
   * @return
   * @throws InvalidAttributeValueException
   */
  public boolean editObjective(Objective_OLD obj) throws InvalidAttributeValueException
  {
    // Verify that the object is not null
    if (obj == null) throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVE);
    // Step 1: Verify that the object contains valid data
    if (obj.isObjectiveValid())
    {
      // Step 2: Verify that the ID contained within the Objective object is in the system
      for (int i = 0; i < objectives.size(); i++)
      {
        List<Objective_OLD> listTemp = objectives.get(i);
        // The elements within each list has all the same ID, so pick the first one and compare it
        if ((listTemp.get(0)).getID() == obj.getID())
        {
          // Add the objective to the end of the list
          return objectives.get(i).add(obj);
        }
      }
    }
    throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVE);
  }

  public boolean addNote(Note note)
  {
    note.setId(this.getNotes().size());
    return this.notes.add(note);
  }

  /**
   * 
   * This method inserts a development need inside the list of developmentneeds
   * 
   * @param obj The developmentNeed object data
   * @return a boolean value that indicates whether the task has been successfully or not
   * @throws InvalidAttributeValueException
   */
  public boolean addDevelopmentNeed(DevelopmentNeed_OLD obj) throws InvalidAttributeValueException
  {
    if (developmentNeeds == null) developmentNeeds = new ArrayList<List<DevelopmentNeed_OLD>>();
    // Verify that the note is not null
    if (obj == null) throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEED_CONTEXT);
    // At this point, the note hasn't got an ID, let's create one
    obj.setID(developmentNeeds.size() + 1);
    if (obj.isDevelopmentNeedValid())
    {
      List<DevelopmentNeed_OLD> tempList = new ArrayList<DevelopmentNeed_OLD>();
      // add the first version of this note
      boolean res1 = tempList.add(obj);
      boolean res2 = developmentNeeds.add(tempList);
      // Action completed, verify the results
      return (res1 && res2);
    }
    throw new InvalidAttributeValueException(Constants.INVALID_DEVNEED_CONTEXT);
  }

  /**
   * 
   * This method adds a new version to an already existing development need
   * 
   * @param obj the updated version of the development need
   * @return a boolean value that indicates whether the task has been successfully or not
   * @throws InvalidAttributeValueException
   */
  public boolean editDevelopmentNeed(DevelopmentNeed_OLD obj) throws InvalidAttributeValueException
  {
    // Verify that the object is not null
    if (obj == null) throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEED_CONTEXT);
    // Step 1: Verify that the object contains valid data
    if (obj.isDevelopmentNeedValid())
    {
      // Step 2: Verify that the ID contained within the note object is in the system
      for (int i = 0; i < developmentNeeds.size(); i++)
      {
        List<DevelopmentNeed_OLD> listTemp = developmentNeeds.get(i);
        // The elements within each list has all the same ID, so pick the first one and compare it
        if ((listTemp.get(0)).getID() == obj.getID())
          // Add the note to the end of the list
          return developmentNeeds.get(i).add(obj);
      }
    }
    throw new InvalidAttributeValueException(Constants.INVALID_DEVNEED_CONTEXT);
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

  /**
   * 
   * This method adds a new version to an already existing Competency
   * 
   * @param obj the updated version of the development need
   * @return a boolean value that indicates whether the task has been successfully or not
   * @throws InvalidAttributeValueException
   */
  public boolean updateCompetency(Competency_OLD obj, String title) throws InvalidAttributeValueException
  {
    // Check if the number of competencies has changed
    int index = 0;
    while (competencies.size() < Constants.COMPETENCY_NAMES.length)
    {
      List<Competency_OLD> tempList = new ArrayList<Competency_OLD>();
      tempList.add(new Competency_OLD(index++, false));
      competencies.add(tempList);
    }
    // Verify that the object is not null
    if (obj == null) throw new InvalidAttributeValueException(Constants.INVALID_NULLCOMPETENCY_CONTEXT);
    // Step 1: Verify that the object contains valid data
    if (obj.isValid())
      // Step 2: Verify that the ID contained within the competency object is in the system
      return competencies.get(obj.getID()).add(obj);
    throw new InvalidAttributeValueException(Constants.INVALID_COMPETENCY_CONTEXT);
  }

  public int nextFeedbackID()
  {
    return this.feedback.size() + 1;
  }

  public int nextObjectiveID()
  {
    int max = 0;
    for (Objective objective : this.getObjectivesNEW())
    {
      if (objective.getId() > max) max = objective.getId();
    }
    return ++max;
  }

  public int nextDevelopmentNeedID()
  {
    int max = 0;
    for (DevelopmentNeed developmentNeed : this.getDevelopmentNeedsNEW())
    {
      if (developmentNeed.getId() > max) max = developmentNeed.getId();
    }
    return ++max;
  }

  /**
   * 
   * This method takes in a List of Objectives or Development Needs an sorts them based on the Due Date Sorted by
   * earliest date to latest date
   * 
   * @param o (An Objective or Development Need)
   * @return List (or Objectives or Development Needs)
   */
  private static void sortObjectivesByTimeToComplete(List<Objective_OLD> objectivesList)
  {
    Collections.sort(objectivesList, new Comparator<Objective_OLD>()
    {
      @Override
      public int compare(Objective_OLD o1, Objective_OLD o2)
      {
        YearMonth ym1 = o1.getTimeToCompleteByYearMonth();
        YearMonth ym2 = o2.getTimeToCompleteByYearMonth();
        // Ternary Expression that return 0 if the ym1 equals ym2, or return the result of the second ternary expression
        // which
        // checks if ym1 is before ym2 and returns -1 if true, 1 if false
        return (ym1.equals(ym2)) ? 0 : ((ym1.isBefore(ym2)) ? -1 : 1);
      }
    });
  }

  private static void sortDevNeedsByTimeToComplete(List<DevelopmentNeed_OLD> devNeeds)
  {
    Collections.sort(devNeeds, new Comparator<DevelopmentNeed_OLD>()
    {
      @Override
      public int compare(DevelopmentNeed_OLD o1, DevelopmentNeed_OLD o2)
      {
        YearMonth ym1 = o1.getTimeToCompleteByYearMonth();
        YearMonth ym2 = o2.getTimeToCompleteByYearMonth();
        // Ternary Expression that return 0 if the ym1 equals ym2, or return the result of the second ternary expression
        // which
        // checks if ym1 is before ym2 and returns -1 if true, 1 if false
        return (ym1.equals(ym2)) ? 0 : ((ym1.isBefore(ym2)) ? -1 : 1);
      }
    });
  }

  //////////////////// START NEW OBJECTIVES

  /** @return the newObjectives */
  public List<Objective> getObjectivesNEW()
  {
    return newObjectives;
  }

  /** @param newObjectives The value to set. */
  public void setObjectivesNEW(List<Objective> newObjectives)
  {
    this.newObjectives = newObjectives;
  }

  public boolean addObjectiveNEW(Objective objective)
  {
    objective.setId(nextObjectiveID());

    return this.newObjectives.add(objective);
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
      throw new InvalidAttributeValueException("Objective must be archived before deleting.");

    return this.getObjectivesNEW().remove(objective);
  }

  public boolean updateObjectiveNEWProgress(int objectiveId, Progress progress) throws InvalidAttributeValueException
  {
    Objective objective = getObjectiveNEW(objectiveId);

    if (objective.getArchived() || objective.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot Edit archived/complete Objective.");

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
    Optional<Objective> objective = getObjectivesNEW().stream().filter(o -> o.getId() == objectiveId).findFirst();

    if (!objective.isPresent()) throw new InvalidAttributeValueException("Objective not found.");

    return objective.get();
  }

  //////////////////// END NEW OBJECTIVES

  //////////////////// START NEW DEVELOPMENT NEEDS

  /** @return the newDevelopmentNeeds */
  public List<DevelopmentNeed> getDevelopmentNeedsNEW()
  {
    return newDevelopmentNeeds;
  }

  /** @param newDevelopmentNeeds The value to set. */
  public void setDevelopmentNeedsNEW(List<DevelopmentNeed> newDevelopmentNeeds)
  {
    this.newDevelopmentNeeds = newDevelopmentNeeds;
  }

  public boolean addDevelopmentNeedNEW(DevelopmentNeed developmentNeed)
  {
    developmentNeed.setId(nextDevelopmentNeedID());

    return this.newDevelopmentNeeds.add(developmentNeed);
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

    return this.getDevelopmentNeedsNEW().remove(developmentNeed);
  }

  public boolean updateDevelopmentNeedNEWProgress(int developmentNeedId, Progress progress)
      throws InvalidAttributeValueException
  {
    DevelopmentNeed developmentNeed = getDevelopmentNeedNEW(developmentNeedId);
    System.out.println(developmentNeed.getProgress() + " = " + Progress.COMPLETE.toString());
    if (developmentNeed.getArchived() || developmentNeed.getProgress().equals(Progress.COMPLETE.getProgressStr()))
      throw new InvalidAttributeValueException("Cannot Edit archived/complete Development Needs.");

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
    Optional<DevelopmentNeed> developmentNeed = getDevelopmentNeedsNEW().stream()
        .filter(d -> d.getId() == developmentNeedId).findFirst();

    if (!developmentNeed.isPresent()) throw new InvalidAttributeValueException("Development Need not found.");

    return developmentNeed.get();
  }

  //////////////////// END NEW DEVELOPMENT NEEDS

  //////////////////// START NEW COMPETENCIES

  /** @return the newCompetencies */
  public List<Competency> getCompetenciesNEW()
  {
    return newCompetencies;
  }

  public void setCompetenciesNEW()
  {
    if (newCompetencies == null)
    {
      this.newCompetencies = new ArrayList<Competency>();
      int id = 0;
      for (CompetencyTitle competenctyTitle : CompetencyTitle.values())
      {
        this.newCompetencies.add(new Competency(id++, competenctyTitle));
      }
    }
  }
  
  public boolean toggleCompetencyNEW(CompetencyTitle competencyTitle) throws InvalidAttributeValueException
  {
    Competency competency = getCompetencyNEW(competencyTitle);

    competency.setSelected(!competency.isSelected());

    return true;
  }
  
  public Competency getCompetencyNEW(CompetencyTitle competencyTitle) throws InvalidAttributeValueException
  {
    Optional<Competency> competency = getCompetenciesNEW().stream()
        .filter(c -> c.getTitle().equals(competencyTitle.getCompetencyTitleStr())).findFirst();

    if (!competency.isPresent()) throw new InvalidAttributeValueException("Competency Need not found.");

    return competency.get();
  }
  
  

  //////////////////// END NEW COMPETENCIES

}
