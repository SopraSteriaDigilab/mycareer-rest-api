package dataStructure;

import static services.validate.Validate.isNull;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.google.gson.Gson;

/**
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
 * @author Ridhwan Nacef
 * @author Mehmet Mehmet
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Employee object
 *
 */
@Entity("employeeDataDev")
public class Employee extends ADProfile_Advanced implements Serializable{

	private static final long serialVersionUID = 6218992334392107696L;
	//Global Variables
	@Id
	private ObjectId id;
	@Embedded
	private List<Feedback> feedback;
	@Embedded
	private List<List<Objective>> objectives;
	@Embedded
	private List<List<Note>> notes;
	@Embedded
	private List<List<DevelopmentNeed>> developmentNeeds;
	@Embedded
	private List<FeedbackRequest> feedbackRequests;
	@Embedded
	private List<List<Competency>> competencies;

	//Empty Constructor
	public Employee(){
		super();
		this.feedback=new ArrayList<Feedback>();
		this.objectives=new ArrayList<List<Objective>>();
		this.notes=new ArrayList<List<Note>>();
		this.developmentNeeds=new ArrayList<List<DevelopmentNeed>>();
		this.feedbackRequests=new ArrayList<FeedbackRequest>();
		this.competencies=new ArrayList<List<Competency>>();
	}

	//Constructor with parameters
	public Employee(
			long employeeID,
			String guid,
			String name, 
			String surname, 
			String email,
			String username,
			String company,
			String team,
			boolean isManager,
			List<Feedback> feeds,
			List<List<Objective>> objectives,
			List<List<Note>> notes,
			List<List<DevelopmentNeed>> needs,
			List<FeedbackRequest> requests,
			List<List<Competency>> competencies,
			List<String> reportees) throws InvalidAttributeValueException{
		super(employeeID, guid, name, surname,email,username,company,team,isManager,reportees);
		this.setFeedback(feeds);
		this.setObjectiveList(objectives);
		this.setNoteList(notes);
		this.setDevelopmentNeedsList(needs);
		this.setFeedbackRequestsList(requests);
		this.setCompetenciesList(competencies);
	}

	public Employee(ADProfile_Advanced userData) throws InvalidAttributeValueException{
		super(userData.getEmployeeID(),
				userData.getGUID(),
				userData.getForename(), 
				userData.getSurname(),
				userData.getEmailAddress(),
				userData.getUsername(), 
				userData.getCompany(),
				userData.getTeam(), 
				userData.getIsManager(),
				userData.getReporteeCNs());
		this.feedback=new ArrayList<Feedback>();
		this.objectives=new ArrayList<List<Objective>>();
		this.notes=new ArrayList<List<Note>>();
		this.developmentNeeds=new ArrayList<List<DevelopmentNeed>>();
		this.feedbackRequests=new ArrayList<FeedbackRequest>();
		this.competencies=new ArrayList<List<Competency>>();
	}

	/**
	 * 
	 * @return This return the _id created by MongoDB when inserting this object in the Collection
	 */
	public ObjectId getId() {
		return id;
	}

//	/**
//	 * 
//	 * @param feeds the list of feedback to assign to an employee
//	 * @throws InvalidClassException
//	 * @throws InvalidAttributeValueException
//	 */
//	public void setFeedbackList(List<Feedback> feeds) throws InvalidAttributeValueException{
//		if(feeds!=null){
//			//Create a counter that keeps count of the error produced
//			int errorCounter=0;
//			this.feedback=new ArrayList<Feedback>();
////			Check if the feedback objects inside the list are valid
//			for(Feedback temp:feeds){
//				if(temp.isFeedbackValid())
//					this.feedback.add(temp);
//				else
//					errorCounter++;
//			}
//			//Verify if there has been any error
//			if(errorCounter!=0)
//				throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKLIST);
//		}
//		else
//			throw new InvalidAttributeValueException(Constants.NULL_FEEDBACKLIST);
//	}
	
	

	public List<Feedback> getFeedback(){
		return this.feedback;
	}
	
	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

	public Feedback getSpecificFeedback(int id) throws InvalidAttributeValueException {
		if(id>0){
			return feedback.stream()
					.filter(f->f.getId()==id)
					.findFirst()
					.get();
		}
		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKID);
	}

	/**
	 * 
	 * @param objectives the list of objectives to assign to an employee
	 * @throws InvalidAttributeValueException
	 */
	public void setObjectiveList(List<List<Objective>> objectives) throws InvalidAttributeValueException{
		if(objectives!=null){
			//Counter that keeps tracks of the error while adding elements
			int errorCounter=0;
			this.objectives=new ArrayList<List<Objective>>();
			//Verify if each each objective is valid
			for(int i=0; i<objectives.size(); i++){
				//Add a new List to the list of Objectives
				this.objectives.add(new ArrayList<Objective>());
				if(objectives.get(i)!=null){
					for(int j=0; j<objectives.get(i).size(); j++){
						if(objectives.get(i).get(j).isObjectiveValid())
							this.objectives.get(this.objectives.size()-1).add(objectives.get(i).get(j));
						else
							errorCounter++;
					}
				}
				else
					throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVE);
			}
			//Verify if there were errors during the import of objectives
			if(errorCounter!=0)
				throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVELIST);
		}
		else
			throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVELIST);
	}

	public List<List<Objective>> getObjectiveList(){
		return this.objectives;
	}

	/**
	 * 
	 * @return a list containing only the latest version of each objective
	 */
	public List<Objective> getLatestVersionObjectives(){
		List<Objective> organisedList=new ArrayList<Objective>();
		if(objectives==null)
			return null;
		else{
			//If the list if not empty, retrieve all the elements and add them to the list
			//that is going to be returned
			for(List<Objective> subList:objectives){ 
				//The last element contains the latest version for the objective
				organisedList.add(subList.get(subList.size()-1));
			}
			
			//Sort list by timeToCompelte
			sortObjectivesByTimeToComplete(organisedList);
			
			//Once the list if full, return it to the user
			return organisedList;
		}
	}

	/**
	 * 
	 * @param id id of the objective to search
	 * @return the objective data
	 * @throws InvalidAttributeValueException 
	 */
	public Objective getLatestVersionOfSpecificObjective(int id) throws InvalidAttributeValueException{
		//Verify if the id is valid
		if(id<0)
			throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVEID);
		//Search for the objective with the given ID
		for(List<Objective> subList:objectives){
			if((subList.get(0)).getID()==id){
				//Now that the Objective has been found, return the latest version of it
				//which is stored at the end of the List
				return (subList.get(subList.size()-1));
			}
		}
		throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVEIDNOTFOND);
	}

	/**
	 * 
	 * @param notes the list of notes to assign to an employee
	 * @throws InvalidAttributeValueException
	 */
	public void setNoteList(List<List<Note>> notes) throws InvalidAttributeValueException{
		if(notes!=null){
			//Counter that keeps tracks of the error while adding elements
			int errorCounter=0;
			this.notes=new ArrayList<List<Note>>();
			//Verify if each each objective is valid
			for(int i=0; i<notes.size(); i++){
				//Add a new List to the list of Objectives
				this.notes.add(new ArrayList<Note>());
				if(notes.get(i)!=null){
					for(int j=0; j<notes.get(i).size(); j++){
						//Check iof the current note is valid
						if(notes.get(i).get(j).isNoteValid())
							this.notes.get(this.notes.size()-1).add( notes.get(i).get(j));
						else
							errorCounter++;
					}
				}
				else
					throw new InvalidAttributeValueException(Constants.NULL_NOTE);
			}
			//Verify if there were errors during the import of objectives
			if(errorCounter!=0)
				throw new InvalidAttributeValueException(Constants.INVALID_NOTELIST);
		}
		else
			throw new InvalidAttributeValueException(Constants.NULL_NOTELIST);
	}

	public List<List<Note>> getNoteList(){
		return this.notes;
	}

	/**
	 * 
	 * @return a list containing the latest version of each note
	 */
	public List<Note> getLatestVersionNotes(){
		List<Note> organisedList=new ArrayList<Note>();
		if(notes==null)
			return null;
		else{
			//If the list if not empty, retrieve all the elements and add them to the list
			//that is going to be returned
			for(List<Note> subList:notes){
				//The last element contains the latest version for the note
				organisedList.add(subList.get(subList.size()-1));
			}
			//Once the list if full, return it to the user
			return organisedList;
		}
	}

	/**
	 * 
	 * @param id id of the note
	 * @return the note data
	 * @throws InvalidAttributeValueException 
	 */
	public Note getLatestVersionOfSpecificNotee(int id) throws InvalidAttributeValueException{
		//Verify if the id is valid
		if(id<0)
			throw new InvalidAttributeValueException(Constants.INVALID_NOTEID);
		//Search for the note with the given ID
		for(List<Note> subList:notes){
			if((subList.get(0)).getID()==id){
				//Now that the note has been found, return the latest version of it
				//which is stored at the end of the List
				return (subList.get(subList.size()-1));
			}
		}
		return null;
	}

	/**
	 * 
	 * This method inserts all the development needs from another list, validating each element before inserting
	 * 
	 * @param developments the List<List<DevelopmentNeed> to copy the data from
	 * @throws InvalidAttributeValueException
	 */
	public void setDevelopmentNeedsList(List<List<DevelopmentNeed>> developments) throws InvalidAttributeValueException{
		if(developments!=null){
			//Counter that keeps tracks of the error while adding elements
			int errorCounter=0;
			this.developmentNeeds=new ArrayList<List<DevelopmentNeed>>();
			//Verify if each development need is valid
			for(int i=0; i<developments.size(); i++){
				//Add a new List to the list of developmentNeeds
				this.developmentNeeds.add(new ArrayList<DevelopmentNeed>());
				if(developments.get(i)!=null){
					for(int j=0; j<developments.get(i).size(); j++){
						if(developments.get(i).get(j).isDevelopmentNeedValid())
							this.developmentNeeds.get(this.developmentNeeds.size()-1).add(developments.get(i).get(j));
						else
							errorCounter++;
					}
				}
				else
					throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEED_CONTEXT);
			}
			//Verify if there were errors during the import of development needs
			if(errorCounter!=0)
				throw new InvalidAttributeValueException(Constants.INVALID_DEVNEEDSLIST_CONTEXT);
		}
		else
			throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEEDSLIST_CONTEXT);
	}

	/**
	 * 
	 * This method returns the list of development needs 
	 * 
	 * @return List<List<DevelopmentNeed>
	 */
	public List<List<DevelopmentNeed>> getDevelopmentNeedsList(){
		return this.developmentNeeds;
	}

	/**
	 * 
	 * This method returns the latest version saved in the system of each development need
	 * 
	 * @return List<DevelopmentNeed>
	 */
	public List<DevelopmentNeed> getLatestVersionDevelopmentNeeds(){
		List<DevelopmentNeed> organisedList=new ArrayList<DevelopmentNeed>();
		if(developmentNeeds==null)
			return null;
		else{
			//If the list if not empty, retrieve all the elements and add them to the list
			//that is going to be returned
			for(List<DevelopmentNeed> subList:developmentNeeds){
				//The last element contains the latest version of the development need
				organisedList.add(subList.get(subList.size()-1));
			}
			//Sort list by timeToCompelte
			sortDevNeedsByTimeToComplete(organisedList);
			
			//Once the list if full, return it to the user
			return organisedList;
		}
	}

	/**
	 * @throws InvalidAttributeValueException 
	 * 
	 * This method returns the latest version of a specific development need, given its ID
	 * 
	 * @param id development need ID
	 * @return the DevelopmentNeed data object
	 * @throws  
	 */
	public DevelopmentNeed getLatestVersionOfSpecificDevelopmentNeed(int id) throws InvalidAttributeValueException{
		//Verify if the id is valid
		if(id<0)
			throw new InvalidAttributeValueException(Constants.INVALID_DEVNEEDID_CONTEXT);
		//Search for the development need with the given ID
		for(List<DevelopmentNeed> subList:developmentNeeds){
			if((subList.get(0)).getID()==id){
				//Now that the development need has been found, return the latest version of it
				//which is stored at the end of the List
				return (subList.get(subList.size()-1));
			}
		}
		return null;
	}

	/** 
	 * @param data the list of feedback request object
	 * @throws InvalidAttributeValueException
	 */
	public void setFeedbackRequestsList(List<FeedbackRequest> feedbackRequestList) throws InvalidAttributeValueException {
		
		if(feedbackRequestList == null /*|| feedbackRequestList.isEmpty()*/)
			throw new InvalidAttributeValueException("The list is invalid. Please try again with a valid list.");
		
		for(FeedbackRequest feedbackRequest : feedbackRequestList){
			if(feedbackRequest == null)
				throw new InvalidAttributeValueException("One or more items in this list is invalid. Please try again with a valid list.");
		}
		
		this.feedbackRequests = new ArrayList<>(feedbackRequestList);
	}
	

	/**
	 * 
	 * This method returns the list of feedbackRequests
	 * 
	 * @return
	 */
	public List<FeedbackRequest> getFeedbackRequestsList(){
		return this.feedbackRequests;
	}
	
	
	/**
	 * @param id
	 * @return Returns the feedback request with the given id.
	 * @throws InvalidAttributeValueException 
	 */
	public FeedbackRequest getFeedbackRequest(String id) throws InvalidAttributeValueException {
		for(FeedbackRequest feedbackRequest : this.feedbackRequests){
			if(feedbackRequest.getId().equals(id))
				return feedbackRequest;
		}
		throw new InvalidAttributeValueException("Feedback Request does not exist.");
	}

//	/**
//	 * 
//	 * This method retrieves a specific feedback request based 
//	 * 
//	 * @param id
//	 * @return
//	 * @throws InvalidAttributeValueException 
//	 */
//	public String removeSpecificFeedbackRequest(String fbID) throws InvalidAttributeValueException{
//		if(fbID!=null && !fbID.equals("")){
//			for(int i=0; i<groupFeedbackRequests.size(); i++){
//				if(groupFeedbackRequests.get(i).searchFeedbackRequestID(fbID)!=null){
//					//Remove the full group Request Feedback if it contains only 1 feedback request
//					String emailRecipient=groupFeedbackRequests.get(i).searchFeedbackRequestID(fbID).getRecipient();
//					if(groupFeedbackRequests.get(i).getRequestList().size()==1)
//						groupFeedbackRequests.remove(i);
//					//Alternatively, remove the given feedback request
//					else
//						groupFeedbackRequests.get(i).removeFeedbackRequest(fbID);
//					//Return the email address found
//					return emailRecipient;
//				}
//			}
//			throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_NOTFOUND_CONTEXT);
//		}
//		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_ID_CONTEXT);
//	}

	/**
	 * 
	 * This method inserts all the competencies from another list, validating each element before inserting
	 * 
	 * @param developments the List<List<Competencies> to copy the data from
	 * @throws InvalidAttributeValueException
	 */
	public void setCompetenciesList(List<List<Competency>> comps) throws InvalidAttributeValueException{
		if(comps!=null){
			//Counter that keeps tracks of the error while adding elements
			int errorCounter=0;
			this.competencies=new ArrayList<List<Competency>>();
			//Verify if each development need is valid
			for(int i=0; i<comps.size(); i++){
				//Add a new List to the list of competencies
				this.competencies.add(new ArrayList<Competency>());
				if(comps.get(i)!=null){
					for(int j=0; j<comps.get(i).size(); j++){	
						if(!this.competencies.get(this.competencies.size()-1).add(comps.get(i).get(j)))
							errorCounter++;
					}
				}
				else
					throw new InvalidAttributeValueException(Constants.INVALID_NULLCOMPETENECYLIST_CONTEXT);
			}
			//Verify if there have been any error during the insertion of competencies
			if(errorCounter>0)
				throw new InvalidAttributeValueException(Constants.INVALID_COMPETENCYLIST_CONTEXT);
		}
	}

	/**
	 * 
	 * This method returns a list of competencies 
	 * 
	 * @return List<List<competencies>
	 */
	public List<List<Competency>> getCompetenciesList(){
		if(this.competencies.size()==0){
			int index=0;
			while(competencies.size()<Constants.COMPETENCY_NAMES.length){
				List<Competency> tempList=new ArrayList<Competency>();
				tempList.add(new Competency(index++,false));
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
	public List<Competency> getLatestVersionCompetencies(){
		List<Competency> organisedList=new ArrayList<Competency>();
		if(this.competencies.size()==0){
			int index=0;
			while(competencies.size()<Constants.COMPETENCY_NAMES.length){
				List<Competency> tempList=new ArrayList<Competency>();
				tempList.add(new Competency(index++,false));
				competencies.add(tempList);
			}
		}
		//If the list if not empty, retrieve all the elements and add them to the list
		//that is going to be returned
		for(int i=0; i<Constants.COMPETENCY_NAMES.length; i++){
			try{
				List<Competency> subList=competencies.get(i);
				//The last element contains the latest version of the development need
				Competency temp=subList.get(subList.size()-1);
				//Add a title and a description to the competency
				temp.setTitle(i);
				temp.setDescription(i);
				organisedList.add(temp);
			}
			catch(Exception e){}
		}
		//Now that all the elements are retrieved, let's sort them 
		List<Competency> selected=new ArrayList<>();
		List<Competency> notSelected=new ArrayList<>();
		//Split the elements between selected and not selected
		for(Competency c:organisedList){
			if(c.getIsSelected())
				selected.add(c);
			else
				notSelected.add(c);
		}
		//Once this is done, let's sort them alphabetically (I USED LAMBDA FOR A MORE EFFICIENT CODE :) )
		selected.sort((Competency c1, Competency c2) -> c1.getID()-c2.getID());
		notSelected.sort((Competency c1, Competency c2) -> c1.getID()-c2.getID());
		//Add these 2 lists to the list to return to the user
		organisedList.clear();
		organisedList.addAll(selected);
		organisedList.addAll(notSelected);
		//Once the list if full, return it to the user
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
	public Competency getLatestVersionOfSpecificCompetency(int id) throws InvalidAttributeValueException{
		//Verify if the id is valid
		if(id<0)
			return null;
		int index=0;
		//Search for the Competency with the given ID
		for(List<Competency> subList:competencies){
			if((subList.get(0)).getID()==id){
				//Now that the competency has been found, return the latest version of it
				//which is stored at the end of the List
				Competency temp=subList.get(subList.size()-1);
				temp.setTitle(index);
				temp.setDescription(index);
				return (temp);
			}
			index++;
		}
		throw new InvalidAttributeValueException(Constants.INVALID_COMPETENCYTID_CONTEXT);
	}

//	/**
//	 * @deprecated Since January 2016. Use {@linkplain #addFeedback(Feedback)}
//	 * @param obj This method adds a Generic feedback to the employee feedback list
//	 * @return It returns true when a feedback has been successfully added to the list, false otherwise
//	 * @throws InvalidAttributeValueException 
//	 */
//	public boolean addGenericFeedback(Feedback obj) throws InvalidAttributeValueException{
//		if(feedback==null)
//			feedback=new ArrayList<Feedback>();
//		//Verify that the object is not null
//		if(obj==null)
//			throw new InvalidAttributeValueException(Constants.NULL_FEEDBACK);
//		//At this point the Feedback hasn't got an ID, let's create it
//		obj.setID((feedback.size()+1));
//		if(obj.isFeedbackValid())
//			return feedback.add(obj);
//		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACK);
//	}
	
	public boolean addFeedback(Feedback feedback) throws InvalidAttributeValueException {
		isNull(feedback);
		return this.feedback.add(feedback);
	}

//	/**
//	 * 
//	 * @param objectiveID
//	 * @param obj objective data
//	 * @return true if the objective has been added correctly
//	 * @throws InvalidAttributeValueException
//	 */
//	public boolean addFeedbackToObjective(int objectiveID, Feedback obj) throws InvalidAttributeValueException{
//		//Verify the data is valid
//		if(objectiveID<0 && obj==null)
//			throw new InvalidAttributeValueException(Constants.INVALID_NULLFEEDBACK_OBJECTIVEID_CONTEXT);
//		//Search for the objective ID within the list of objectives
//		//add the objective if the ID is found
//		for(int i=0; i<this.objectives.size(); i++){
//			//If the appropriate objective is found, add the feedback to its list
//			if(this.objectives.get(i).get(0).getID()==objectiveID){
//				//Now that the related objective is found, create an ID for this feedback
//				obj.setID(this.objectives.get(i).size()+1);
//				//Validate the data
//				if(obj.isFeedbackValid())
//					//Try to add the new feedback
//					return this.objectives.get(i).get(this.objectives.get(i).size()-1).addFeedback(obj);
//				else
//					throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACK);
//			}
//		}
//		return false;
//	}

	/**
	 * 
	 * @param obj objective data
	 * @return
	 * @throws InvalidAttributeValueException
	 */
	public boolean addObjective(Objective obj) throws InvalidAttributeValueException{
		if(objectives==null)
			objectives=new ArrayList<List<Objective>>();
		//Verify that the objective is not null
		if(obj==null)
			throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVE);
		//At this point, the objective hasn't got an ID, let's create one
		obj.setID(objectives.size()+1);
		if(obj.isObjectiveValid()){
			List<Objective> tempList=new ArrayList<Objective>();
			//add the first version of this objective
			boolean res1=tempList.add(obj);
			boolean res2=objectives.add(tempList);
			//Action completed, verify the results
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
	public boolean editObjective(Objective obj) throws InvalidAttributeValueException{
		//Verify that the object is not null
		if(obj==null)
			throw new InvalidAttributeValueException(Constants.NULL_OBJECTIVE);
		//Step 1: Verify that the object contains valid data 
		if(obj.isObjectiveValid()){
			//Step 2: Verify that the ID contained within the Objective object is in the system
			for(int i=0; i<objectives.size(); i++){
				List<Objective> listTemp=objectives.get(i);
				//The elements within each list has all the same ID, so pick the first one and compare it
				if((listTemp.get(0)).getID()==obj.getID()){
					//Add the objective to the end of the list
					return objectives.get(i).add(obj);
				}
			}
		}
		throw new InvalidAttributeValueException(Constants.INVALID_OBJECTIVE);
	}

	/**
	 * 
	 * This method adds a new note to a specific user
	 * 
	 * @param obj note object
	 * @return
	 * @throws InvalidAttributeValueException
	 */
	public boolean addNote(Note obj) throws InvalidAttributeValueException{
		if(notes==null)
			notes=new ArrayList<List<Note>>();
		//Verify that the note is not null
		if(obj==null)
			throw new InvalidAttributeValueException(Constants.NULL_NOTE);
		//At this point, the note hasn't got an ID, let's create one
		obj.setID(notes.size()+1);
		if(obj.isNoteValid()){
			List<Note> tempList=new ArrayList<Note>();
			//add the first version of this note
			boolean res1=tempList.add(obj);
			boolean res2=notes.add(tempList);
			//Action completed, verify the results
			return (res1 && res2);
		}
		throw new InvalidAttributeValueException(Constants.INVALID_NOTE);
	}

	/**
	 * 
	 * This method adds a new version of the note
	 * 
	 * @param obj note data
	 * @return
	 * @throws InvalidAttributeValueException 
	 */
	public boolean editNote(Note obj) throws InvalidAttributeValueException{
		//Verify that the object is not null
		if(obj==null)
			throw new InvalidAttributeValueException(Constants.NULL_NOTE);
		//Step 1: Verify that the object contains valid data 
		if(obj.isNoteValid()){
			//Step 2: Verify that the ID contained within the note object is in the system
			for(int i=0; i<notes.size(); i++){
				List<Note> listTemp=notes.get(i);
				//The elements within each list has all the same ID, so pick the first one and compare it
				if((listTemp.get(0)).getID()==obj.getID())
					//Add the note to the end of the list
					return notes.get(i).add(obj);
			}
		}
		throw new InvalidAttributeValueException(Constants.INVALID_NOTE);
	}

	/**
	 * 
	 * This method inserts a development need inside the list of developmentneeds
	 * 
	 * @param obj The developmentNeed object data
	 * @return a boolean value that indicates whether the task has been successfully or not
	 * @throws InvalidAttributeValueException
	 */
	public boolean addDevelopmentNeed(DevelopmentNeed obj) throws InvalidAttributeValueException{
		if(developmentNeeds==null)
			developmentNeeds=new ArrayList<List<DevelopmentNeed>>();
		//Verify that the note is not null
		if(obj==null)
			throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEED_CONTEXT);
		//At this point, the note hasn't got an ID, let's create one
		obj.setID(developmentNeeds.size()+1);
		if(obj.isDevelopmentNeedValid()){
			List<DevelopmentNeed> tempList=new ArrayList<DevelopmentNeed>();
			//add the first version of this note
			boolean res1=tempList.add(obj);
			boolean res2=developmentNeeds.add(tempList);
			//Action completed, verify the results
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
	public boolean editDevelopmentNeed(DevelopmentNeed obj) throws InvalidAttributeValueException{
		//Verify that the object is not null
		if(obj==null)
			throw new InvalidAttributeValueException(Constants.INVALID_NULLDEVNEED_CONTEXT);
		//Step 1: Verify that the object contains valid data 
		if(obj.isDevelopmentNeedValid()){
			//Step 2: Verify that the ID contained within the note object is in the system
			for(int i=0; i<developmentNeeds.size(); i++){
				List<DevelopmentNeed> listTemp=developmentNeeds.get(i);
				//The elements within each list has all the same ID, so pick the first one and compare it
				if((listTemp.get(0)).getID()==obj.getID())
					//Add the note to the end of the list
					return developmentNeeds.get(i).add(obj);
			}
		}
		throw new InvalidAttributeValueException(Constants.INVALID_DEVNEED_CONTEXT);
	}
	
	/**
	 * Add feedback request to employee
	 * @param feedbackRequest
	 * @return true if it was added, false otherwise
	 * @throws InvalidAttributeValueException if feedbackRequest is null
	 */
	public boolean addFeedbackRequest(FeedbackRequest feedbackRequest) throws InvalidAttributeValueException{
		if(feedbackRequest == null)
			throw new InvalidAttributeValueException("This object is invalid.");
		
		return this.feedbackRequests.add(feedbackRequest);
	}

//	/**
//	 * 
//	 * This method add a feedback request to a given user ID
//	 * 
//	 * @param obj the feedback request object to insert
//	 * @return true if it completes correctly, false otherwise
//	 * @throws InvalidAttributeValueException 
//	 */
//	public boolean addGroupFeedbackRequest(GroupFeedbackRequest obj) throws InvalidAttributeValueException{
//		if(groupFeedbackRequests==null)
//			groupFeedbackRequests=new ArrayList<GroupFeedbackRequest>();
//		//Verify that the object is not null
//		if(obj==null)
//			throw new InvalidAttributeValueException(Constants.INVALID_NULLGROUPFEEDBACKREQUEST_CONTEXT);
//		//The object has now been validated and can be added to the list for this user
//		return groupFeedbackRequests.add(obj);
//	}

//	/**
//	 * 
//	 * This method updates the content of a feedback request
//	 * 
//	 * @param obj the new feedback request object containing the right ID
//	 * @return true if it completes correctly, false otherwise
//	 * @throws InvalidAttributeValueException 
//	 */
//	public boolean updateGroupFeedbackRequest(GroupFeedbackRequest obj) throws InvalidAttributeValueException{
//		if(obj!=null){
//			for(int i=0; i<groupFeedbackRequests.size(); i++){
//				if(groupFeedbackRequests.get(i).getID().equals(obj.getID()))
//					//Remove the obsolete object and add the new one
//					return (groupFeedbackRequests.remove(groupFeedbackRequests.get(i))) && (groupFeedbackRequests.add(obj));
//			}
//		}
//		throw new InvalidAttributeValueException(Constants.INVALID_NULLGROUPFEEDBACKREQUEST_CONTEXT);
//	}

//	/**
//	 * 
//	 * This method verifies if the ID for the feedback request is unique to the user
//	 * 
//	 * @param req the feedback request object
//	 * @return true if it completes correctly, false otherwise
//	 */
//	public boolean isGroupFeedbackRequestUniqueToEmployee(GroupFeedbackRequest req){
//		for(GroupFeedbackRequest t:groupFeedbackRequests){
//			if(t.getID().equals(req.getID()))
//				return false;
//		}
//		return true;
//	}

	/**
	 * 
	 * This method adds a new version to an already existing Competency
	 * 
	 * @param obj the updated version of the development need
	 * @return a boolean value that indicates whether the task has been successfully or not
	 * @throws InvalidAttributeValueException 
	 */
	public boolean updateCompetency(Competency obj, String title) throws InvalidAttributeValueException{
		//Check if the number of competencies has changed
		int index=0;
		while(competencies.size()<Constants.COMPETENCY_NAMES.length){
			List<Competency> tempList=new ArrayList<Competency>();
			tempList.add(new Competency(index++,false));
			competencies.add(tempList);
		}
		//Verify that the object is not null
		if(obj==null)
			throw new InvalidAttributeValueException(Constants.INVALID_NULLCOMPETENCY_CONTEXT);
		//Step 1: Verify that the object contains valid data 
		if(obj.isValid())
			//Step 2: Verify that the ID contained within the competency object is in the system
			return competencies.get(obj.getID()).add(obj);
		throw new InvalidAttributeValueException(Constants.INVALID_COMPETENCY_CONTEXT);
	}
	
	/**
	 * 
	 * This method takes in a List of Objectives or Development Needs an sorts them based on the Due Date
	 * Sorted by earliest date to latest date
	 * @param o (An Objective or Development Need)
	 * @return List (or Objectives or Development Needs)
	 */
	private static void sortObjectivesByTimeToComplete(List<Objective> objectivesList){
		Collections.sort(objectivesList, new Comparator<Objective>(){
			@Override
			public int compare(Objective o1, Objective o2) {
				YearMonth ym1 = o1.getTimeToCompleteByYearMonth();
				YearMonth ym2 = o2.getTimeToCompleteByYearMonth();
				//Ternary Expression that return 0 if the ym1 equals ym2, or return the result of the second ternary expression which 
				//checks if ym1 is before ym2 and returns -1 if true, 1 if false 
				return (ym1.equals(ym2)) ?  0 :  ((ym1.isBefore(ym2)) ? -1 : 1);
			}
		});	
	}
	
	private static void sortDevNeedsByTimeToComplete(List<DevelopmentNeed> devNeeds){
		Collections.sort(devNeeds, new Comparator<DevelopmentNeed>(){
			@Override
			public int compare(DevelopmentNeed o1, DevelopmentNeed o2) {
				YearMonth ym1 = o1.getTimeToCompleteByYearMonth();
				YearMonth ym2 = o2.getTimeToCompleteByYearMonth();
				//Ternary Expression that return 0 if the ym1 equals ym2, or return the result of the second ternary expression which 
				//checks if ym1 is before ym2 and returns -1 if true, 1 if false 
				return (ym1.equals(ym2)) ?  0 :  ((ym1.isBefore(ym2)) ? -1 : 1);
			}
		});	
	}

	/**
	 * 
	 * This method checks and update the user data with the new given information 
	 * The only field that never changes and therefore it won't be checked is the GUID.
	 * The GUID is the AD value unique not only inside the enterprise, but in the entire globe
	 * 
	 * @param data The user data to compare with the corrent information
	 * @return True if the data had to be updated, false if the data didn't need changing
	 * @throws InvalidAttributeValueException 
	 */
	public boolean verifyDataIsUpToDate(ADProfile_Advanced data) throws InvalidAttributeValueException {
		int itemsUpdated=0;

		//Start checking the fields
		
		
		
		
		//Check the employeeID
		if(this.getEmployeeID()!=data.getEmployeeID()){
			//Update the counter and the user data
			itemsUpdated++;
			this.setEmployeeID(data.getEmployeeID());
		}

		//Check the Email address
		if(!this.getEmailAddress().equals(data.getEmailAddress())){
			//Update the counter and the user data
			itemsUpdated++;
			this.setEmailAddress(data.getEmailAddress());
		}

		//Check the username
		if(!this.getUsername().equals(data.getUsername())){
			//Update the counter and the user data
			itemsUpdated++;
			this.setUsername(data.getUsername());
		}

		//Check the company
		if(!this.getCompany().equals(data.getCompany())){
			//Update the counter and the user data
			itemsUpdated++;
			this.setCompany(data.getCompany());
		}

		//Check the team
		if(!this.getTeam().equals(data.getTeam())){
			//Update the counter and the user data
			itemsUpdated++;
			this.setTeam(data.getTeam());
		}

		//Check the list of reportees
//		int subCounter=0;
		List<String> currentReporteeList=this.getReporteeCNs();
		List<String> ADReporteeList=data.getReporteeCNs();
		
    if(!currentReporteeList.equals(ADReporteeList)){
      itemsUpdated++;
      this.setReporteeCNs(ADReporteeList);
    }
    
		
    // TODO This doesnt work... Logic doesn't work.
    // If repNewSubList is 0 (i.e. an employees reportee leaves the company) then this wont even check...
//		for(int i=0; i<repNewSubList.size(); i++){
//			if(!repOldSubList.contains(repNewSubList.get(i))){
//				subCounter++;
//			}
//		}
//		if(subCounter>0){
//			//Update the counter and the user data
//			itemsUpdated++;
//			this.setReporteeCNs(ADReporteeList);
//		}

		//Return a value
		if(itemsUpdated>0)
			return true;
		return false;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	@Override
	public String toString(){
		String s="";
		s+=super.toString();
		//Add the feedback
		s+="Feedback:\n";
		for(Feedback temp:this.feedback){
			s+=temp.toString()+"\n";
		}
		//Add the objectives
		s+="Objectives:\n";
		int counter=1;
		for(List<Objective> objList: objectives){
			s+="Objective "+counter++ +"\n";
			int version=1;
			for(Objective obj: objList){
				s+="Version "+version++ +"\n"+obj.toString()+"\n";
			}
		}
		//Add the Notes
		s+="Notes:\n";
		counter=1;
		for(List<Note> noteList: notes){
			s+="Note "+counter++ +"\n";
			int version=1;
			for(Note obj: noteList){
				s+="Version "+version++ +"\n"+obj.toString()+"\n";
			}
		}
		//Add the Development needs
		s+="Development Needs:\n";
		counter=1;
		for(List<DevelopmentNeed> noteList: developmentNeeds){
			s+="Development Need "+counter++ +"\n";
			int version=1;
			for(DevelopmentNeed obj: noteList){
				s+="Version "+version++ +"\n"+obj.toString()+"\n";
			}
		}
//		//Add the Feedback requests
//		s+="Group Feedback Requests:\n";
//		counter=1;
//		for(FeedbackRequest t:feedbackRequests){
//			s+="Request "+counter++ +"\n";
//			s+=t.toString();
//		}
		//Add the Competencies
		s+="Competencies: ";
		//Retrieve all the sublists
		int indexSubList=0;
		for(List<Competency> subList: this.competencies){
			//For each sublist, retrieve each element and add them to the s string including the title and description
			int compCounter=0;
			try{
				for(Competency comp:subList){
					s+="Competency: "+compCounter++ +"\n";
					s+=comp.toString(indexSubList++);
				}
			}catch(Exception r){}
		}
		return s;
	}

	public int nextFeedbackID() {
		return this.feedback.size()+1;
	}

}
