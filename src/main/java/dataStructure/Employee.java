package dataStructure;

import java.io.InvalidClassException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;
import com.google.gson.Gson;

/**
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Employee object
 *
 */
@Entity("employeeDataDev")
public class Employee implements Serializable{

	private static final long serialVersionUID = 6218992334392107696L;
	//Global Variables
	@Id
	private ObjectId id;
	private int employeeID, level, LM_ID;
	private String forename, surname, role, LM_Name, emailAddress;
	private String dob, joinDate;
	@Embedded
	private List<Feedback> feedback;
	@Embedded
	private List<List<Objective>> objectives;
	@Embedded
	private List<List<Note>> notes;
	private boolean isAManager;
	private List<List<DevelopmentNeed>> developmentNeeds;
	private List<FeedbackRequest> feedbackRequests;
	@Embedded
	private List<List<Competency>> competencies;

	//Empty Constructor
	public Employee(){
		this.employeeID=Constants.INVALID_INT;
		this.level=Constants.INVALID_INT;
		this.LM_ID=Constants.INVALID_INT;
		this.forename=Constants.INVALID_STRING;
		this.surname=Constants.INVALID_STRING;
		this.emailAddress=Constants.INVALID_STRING;
		this.role=Constants.INVALID_STRING;
		this.LM_Name=Constants.INVALID_STRING;
		this.dob=null;
		this.joinDate=null;
		this.feedback=new ArrayList<Feedback>();
		this.objectives=new ArrayList<List<Objective>>();
		this.notes=new ArrayList<List<Note>>();
		this.isAManager=false;
		this.developmentNeeds=new ArrayList<List<DevelopmentNeed>>();
		this.feedbackRequests=new ArrayList<FeedbackRequest>();
		this.competencies=new ArrayList<List<Competency>>();
	}

	//Constructor with parameters
	public Employee(
			int id,
			int level, 
			String name, 
			String surname, 
			String email,
			String role, 
			boolean isManager,
			String LMName,
			int LMID,
			String dob, 
			String joinDate,
			List<Feedback> feeds,
			List<List<Objective>> objectives,
			List<List<Note>> notes,
			List<List<DevelopmentNeed>> needs,
			List<FeedbackRequest> requests,
			List<List<Competency>> competencies) throws InvalidAttributeValueException, InvalidClassException{
		this.setEmployeeID(id);
		this.setLevel(level);
		this.setForename(name);
		this.setSurname(surname);
		this.setEmailAddress(email);
		this.setRole(role);
		this.setIsAManager(isManager);
		this.setLineManagerName(LMName);
		this.setLineManagerID(LMID);
		this.setDateOFBirth(dob);
		this.setJoinDate(joinDate);
		this.setFeedbackList(feeds);
		this.setObjectiveList(objectives);
		this.setNoteList(notes);
		this.setDevelopmentNeedsList(needs);
		this.setFeedbackRequestsList(requests);
		this.setCompetenciesList(competencies);
	}

	/**
	 * 
	 * @return This return the _id created by MongoDB when inserting this object in the Collection
	 */
	public ObjectId getId() {
		return id;
	}

	public void setEmployeeID(int id) throws InvalidAttributeValueException{
		if(id>0)
			this.employeeID=id;
		else{
			this.employeeID=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The value "+id+" is not valid in this context");
		}
	}

	public int getEmployeeID(){
		return this.employeeID;
	}

	/**
	 * 
	 * @param level This is a value between 1 and 15
	 */
	public void setLevel(int level) throws InvalidAttributeValueException{
		if(level>0 && level<16)
			this.level=level;
		else{
			this.level=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The given 'level' is not valid in this context");
		}
	}

	public int getLevel(){
		return this.level;
	}

	/**
	 * 
	 * @param name The name must not exceed 200 characters
	 */
	public void setForename(String name) throws InvalidAttributeValueException{
		if(name!=null && name.length()>0 && name.length()<201)
			this.forename=name;
		else{
			this.forename=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'forename' is not valid in this context");
		}
	}

	public String getForname(){
		return this.forename;
	}

	/**
	 * 
	 * @param surname The surname cannot exceed the 200 characters
	 */
	public void setSurname(String surname) throws InvalidAttributeValueException{
		if(surname!=null && surname.length()>0 && surname.length()<201)
			this.surname=surname;
		else{
			this.surname=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'surname' is not valid in this context");
		}
	}

	public String getSurname(){
		return this.surname;
	}

	/**
	 * 
	 * @param email The user email address
	 * @throws InvalidAttributeValueException
	 */
	public void setEmailAddress(String email) throws InvalidAttributeValueException{
		if(email!=null && email.length()>0 && email.contains("@"))
			this.emailAddress=email;
		else{
			this.emailAddress=Constants.INVALID_EMAIL;
			throw new InvalidAttributeValueException("The given 'Email Address' is not valid in this context");
		}
	}

	public String getEmailAddress(){
		return this.emailAddress;
	}

	/**
	 * 
	 * @param role the role value does not exceed the 250 characters
	 */
	public void setRole(String role) throws InvalidAttributeValueException{
		if(role!=null && role.length()>0 && role.length()<251)
			this.role=role;
		else{
			this.role=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'role' is not valid in this context");
		}
	}

	public String getRole(){
		return this.role;
	}

	/**
	 * 
	 * @param value boolean value that says whether a user is a manager or not
	 */
	public void setIsAManager(boolean value){
		this.isAManager=value;
	}

	public boolean getIsAManager(){
		return this.isAManager;
	}

	/**
	 * 
	 * @param name The name of the line manager does not exceed the 250 characters
	 */
	public void setLineManagerName(String name) throws InvalidAttributeValueException{
		if(name!=null && name.length()>0 && name.length()<251)
			this.LM_Name=name;
		else{
			this.LM_Name=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'line Manager Name' is not valid in this context");
		}
	}

	public String getLineManagerName(){
		return this.LM_Name;
	}

	/**
	 * 
	 * @param id The line manager ID
	 * @throws InvalidAttributeValueException
	 */
	public void setLineManagerID(int id) throws InvalidAttributeValueException{
		if(id>0)
			this.LM_ID=id;
		else{
			this.LM_ID=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The value "+id+" is not valid in this context");
		}
	}

	public int getLineManagerID(){
		return this.LM_ID;
	}

	/**
	 * 
	 * @param date The date of birth of the employee
	 * @throws InvalidAttributeValueException
	 */
	public void setDateOFBirth(String date) throws InvalidAttributeValueException{
		if(date!=null){
			try{
				LocalDate tempD=LocalDate.parse(date,Constants.DATE_FORMAT);
				//Verify if the date given is valid
				if(tempD.isBefore(LocalDate.now()))
					this.dob=tempD.toString();
				else
					throw new InvalidAttributeValueException("The 'date of birth' cannot be greater than today");
			}
			catch(Exception e){
				throw new InvalidAttributeValueException("The given 'date of birth' is not valid in this context");
			}
		}
		else{
			this.dob=null;
			throw new InvalidAttributeValueException("The given 'date of birth' is null");
		}
	}

	public String getDateOfBirth(){
		return this.dob;
	}

	/**
	 * 
	 * @param date the date when the employee joined the company
	 * @throws InvalidAttributeValueException
	 */
	public void setJoinDate(String date) throws InvalidAttributeValueException{
		if(date!=null){
			try{
				LocalDate tempD=LocalDate.parse(date,Constants.DATE_FORMAT);
				//Verify if the given data is before the current date
				if(tempD.isBefore(LocalDate.now()))
					this.joinDate=tempD.toString();
				else
					throw new InvalidAttributeValueException("The 'join date' cannot be greater than today");
			}
			catch(Exception e){
				throw new InvalidAttributeValueException("The given 'join date' is not valid in this context");
			}
		}
		else{
			this.joinDate=null;
			throw new InvalidAttributeValueException("The given 'join date' is null");
		}
	}

	public String getJoinDate(){
		return this.joinDate;
	}

	/**
	 * 
	 * @param feeds the list of feedback to assign to an employee
	 * @throws InvalidClassException
	 * @throws InvalidAttributeValueException
	 */
	public void setFeedbackList(List<Feedback> feeds)throws InvalidClassException, InvalidAttributeValueException{
		if(feeds!=null){
			//Create a counter that keeps count of the error produced
			int errorCounter=0;
			this.feedback=new ArrayList<Feedback>();
			//Check if the feedback objects inside the list are valid
			for(Feedback temp:feeds){
				if(temp.isFeedbackValid())
					this.feedback.add(temp);
				else{
					errorCounter++;
				}
			}
			//Verify if there has been any error
			if(errorCounter!=0)
				throw new InvalidClassException("Not all the feedback were added due to their format/status");
		}
		else{
			this.feedback=null;
			//throw new InvalidAttributeValueException("The list of feedback given is null");
		}
	}

	public List<Feedback> getFeedbackList(){
		return this.feedback;
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
					throw new InvalidAttributeValueException("The list of objectives given is null");
			}
			//Verify if there were errors during the import of objectives
			if(errorCounter!=0)
				throw new InvalidAttributeValueException("Not all objective elements were valid");
		}
		else{
			this.objectives=null;
			//throw new InvalidAttributeValueException("The list of feedback given is null");
		}
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
			//Once the list if full, return it to the user
			return organisedList;
		}
	}

	/**
	 * 
	 * @param id id of the objective to search
	 * @return the objective data
	 */
	public Objective getLatestVersionOfSpecificObjective(int id){
		//Verify if the id is valid
		if(id<0)
			return null;
		//Search for the objective with the given ID
		for(List<Objective> subList:objectives){
			if((subList.get(0)).getID()==id){
				//Now that the Objective has been found, return the latest version of it
				//which is stored at the end of the List
				return (subList.get(subList.size()-1));
			}
		}
		return null;
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
					throw new InvalidAttributeValueException("The given list of notes is null");
			}
			//Verify if there were errors during the import of objectives
			if(errorCounter!=0)
				throw new InvalidAttributeValueException("Not all note elements were valid");
		}
		else{
			this.notes=null;
			throw new InvalidAttributeValueException("The list of feedback given is null");
		}
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
	 */
	public Note getLatestVersionOfSpecificNotee(int id){
		//Verify if the id is valid
		if(id<0)
			return null;
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
					throw new InvalidAttributeValueException("The list of development needs given is null");
			}
			//Verify if there were errors during the import of development needs
			if(errorCounter!=0)
				throw new InvalidAttributeValueException("Not all development need elements were valid");
		}
		else{
			this.objectives=null;
			//throw new InvalidAttributeValueException("The list of feedback given is null");
		}
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
			//Once the list if full, return it to the user
			return organisedList;
		}
	}

	/**
	 * 
	 * This method returns the latest version of a specific development need, given its ID
	 * 
	 * @param id development need ID
	 * @return the DevelopmentNeed data object
	 */
	public DevelopmentNeed getLatestVersionOfSpecificDevelopmentNeed(int id){
		//Verify if the id is valid
		if(id<0)
			return null;
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
	 * 
	 * This method copies the data from an already exciting list to the one for the current user
	 * 
	 * @param data the list of feedback request object
	 * @throws InvalidAttributeValueException
	 */
	public void setFeedbackRequestsList(List<FeedbackRequest> data) throws InvalidAttributeValueException{
		if(data!=null){
			//Counter that keeps tracks of the error while adding elements
			int errorCounter=0;
			this.feedbackRequests=new ArrayList<FeedbackRequest>();
			//Verify if each development need is valid
			for(int i=0; i<data.size(); i++){
				//Add a new List to the list of developmentNeeds
				if(data.get(i)!=null){
					if(!feedbackRequests.add(data.get(i)))
						errorCounter++;
				}
			}
			//Verify if there were errors during the import of development needs
			if(errorCounter!=0)
				throw new InvalidAttributeValueException("Not all feedback requests were valid");
		}
		else{
			this.feedbackRequests=null;
			//throw new InvalidAttributeValueException("The list of feedback given is null");
		}
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
	 * 
	 * This method retrieves a specific feedback request based 
	 * 
	 * @param id
	 * @return
	 */
	public FeedbackRequest getSpecificFeedbackRequest(String id){
		if(id!=null && !id.equals("")){
			for(int i=0; i<feedbackRequests.size(); i++){
				if(feedbackRequests.get(i).getID().equals(id))
					return feedbackRequests.get(i);
			}
		}
		return null;
	}

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
					}//for
				}//if
				else
					throw new InvalidAttributeValueException("The list of development needs given is null");
			}//for
			//Verify if there have been any error during the insertion of competencies
			if(errorCounter>0)
				throw new InvalidAttributeValueException("There have been "+errorCounter+" errors during the insertion of competencies");
		}//if
	}//setCompetenciesList

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
	}//getCompetenciesList

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
	}//getLatestVersionCompetencies

	/**
	 * 
	 * This method returns the latest version of a specific Competency, given its ID
	 * 
	 * @param id Competency need ID
	 * @return the Competency data object
	 */
	public Competency getLatestVersionOfSpecificCompetency(int id){
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
			}//if
			index++;
		}//for
		return null;
	}//getLatestVersionOfSpecificCompetency

	@Override
	public String toString(){
		String s="";
		//Add the generic information
		s+="ID "+this.getEmployeeID()+"\n"
				+ "Forename "+this.getForname()+"\n"
				+ "Surname "+this.getSurname()+"\n"
				+ "DOB "+this.getDateOfBirth()+"\n"
				+ "EmailAddress "+this.getEmailAddress()+"\n"
				+ "Level "+this.getLevel()+"\n"
				+ "Role "+this.getRole()+"\n"
				+ "IsAManager "+this.getIsAManager()+"\n"
				+ "JoinDate "+this.getJoinDate()+"\n"
				+ "LineManagerID "+this.getLineManagerID()+"\n"
				+ "LineManagerName "+this.getLineManagerName()+"\n";
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
		//Add the Feedback requests
		s+="Feedback Requests:\n";
		counter=1;
		for(FeedbackRequest t:feedbackRequests){
			s+="Request "+counter++ +"\n";
			s+=t.toString();
		}
		//Add the Competencies
		s+="Competencies";
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

	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}

	/**
	 * 
	 * @param obj This method adds a Generic feedback to the employee feedback list
	 * @return It returns true when a feedback has been successfully added to the list, false otherwise
	 * @throws InvalidAttributeValueException 
	 */
	public boolean addGenericFeedback(Feedback obj) throws InvalidAttributeValueException{
		if(feedback==null)
			feedback=new ArrayList<Feedback>();
		//Verify that the object is not null
		if(obj==null)
			return false;
		//At this point the Feedback hasn't got an ID, let's create it
		obj.setID(feedback.size()+1);
		if(obj.isFeedbackValid())
			return feedback.add(obj);
		return false;
	}

	/**
	 * 
	 * @param objectiveID
	 * @param obj objective data
	 * @return true if the objective has been added correctly
	 * @throws InvalidAttributeValueException
	 */
	public boolean addFeedbackToObjective(int objectiveID, Feedback obj) throws InvalidAttributeValueException{
		//Verify the data is valid
		if(objectiveID<0 && obj==null)
			return false;
		//Search for the objective ID within the list of objectives
		//add the objective if the ID is found
		for(int i=0; i<this.objectives.size(); i++){
			//If the appropriate objective is found, add the feedback to its list
			if(this.objectives.get(i).get(0).getID()==objectiveID){
				//Now that the related objective is found, create an ID for this feedback
				obj.setID(this.objectives.get(i).size()+1);
				//Validate the data
				if(obj.isFeedbackValid())
					//Try to add the new feedback
					return this.objectives.get(i).get(this.objectives.get(i).size()-1).addFeedback(obj);
				else
					return false;
			}
		}
		return false;
	}

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
			return false;
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
		return false;
	}

	/**
	 * This method adds a new updated version of the objective
	 * 
	 * @param obj objective data
	 * @return
	 */
	public boolean editObjective(Objective obj){
		//Verify that the object is not null
		if(obj==null)
			return false;
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
		return false;
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
			return false;
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
		return false;
	}

	/**
	 * 
	 * This method adds a new version of the note
	 * 
	 * @param obj note data
	 * @return
	 */
	public boolean editNote(Note obj){
		//Verify that the object is not null
		if(obj==null)
			return false;
		//Step 1: Verify that the object contains valid data 
		if(obj.isNoteValid()){
			//Step 2: Verify that the ID contained within the note object is in the system
			for(int i=0; i<notes.size(); i++){
				List<Note> listTemp=notes.get(i);
				//The elements within each list has all the same ID, so pick the first one and compare it
				if((listTemp.get(0)).getID()==obj.getID()){
					//Add the note to the end of the list
					return notes.get(i).add(obj);
				}
			}
		}
		return false;
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
			return false;
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
		return false;
	}

	/**
	 * 
	 * This method adds a new version to an already existing development need
	 * 
	 * @param obj the updated version of the development need
	 * @return a boolean value that indicates whether the task has been successfully or not
	 */
	public boolean editDevelopmentNeed(DevelopmentNeed obj){
		//Verify that the object is not null
		if(obj==null)
			return false;
		//Step 1: Verify that the object contains valid data 
		if(obj.isDevelopmentNeedValid()){
			//Step 2: Verify that the ID contained within the note object is in the system
			for(int i=0; i<developmentNeeds.size(); i++){
				List<DevelopmentNeed> listTemp=developmentNeeds.get(i);
				//The elements within each list has all the same ID, so pick the first one and compare it
				if((listTemp.get(0)).getID()==obj.getID()){
					//Add the note to the end of the list
					return developmentNeeds.get(i).add(obj);
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * This method add a feedback request to a given user ID
	 * 
	 * @param obj the feedback request object to insert
	 * @return true if it completes correctly, false otherwise
	 */
	public boolean addFeedbackRequest(FeedbackRequest obj){
		if(feedbackRequests==null)
			feedbackRequests=new ArrayList<FeedbackRequest>();
		//Verify that the object is not null
		if(obj==null)
			return false;
		//The object has now been validated and can be added to the list for this user
		return feedbackRequests.add(obj);
	}

	/**
	 * 
	 * This method updates the content of a feedback request
	 * 
	 * @param obj the new feedback request object containing the right ID
	 * @return true if it completes correctly, false otherwise
	 */
	public boolean updateFeedbackRequest(FeedbackRequest obj){
		if(obj!=null){
			for(int i=0; i<feedbackRequests.size(); i++){
				if(feedbackRequests.get(i).getID().equals(obj.getID())){
					//Remove the obsolete object and add the new one
					return (feedbackRequests.remove(feedbackRequests.get(i))) && (feedbackRequests.add(obj));
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * This method verifies if the ID for the feedback request is unique to the user
	 * 
	 * @param req the feedback request object
	 * @return true if it completes correctly, false otherwise
	 */
	public boolean isFeedbackRequestUniqueToEmployee(FeedbackRequest req){
		for(FeedbackRequest t:feedbackRequests){
			if(t.getID().equals(req.getID()))
				return false;
		}
		return true;
	}

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
			throw new InvalidAttributeValueException("The given Competency object is empty");
		//Step 1: Verify that the object contains valid data 
		if(obj.isValid()){
			//Step 2: Verify that the ID contained within the competency object is in the system
			competencies.get(obj.getID()).add(obj);
			return true;
		}
		return false;
	}

}
