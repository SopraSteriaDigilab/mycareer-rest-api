package dataStructure;

import java.awt.Container;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import com.google.gson.Gson;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Employee object
 *
 */
public class Employee {
	
	//Global Variables
	private int employeeID, level, LM_ID;
	private String forename, surname, role, LM_Name, emaiAddress;
	private LocalDate dob, joinDate;
	private List<Feedback> feedback;
	private List<List<Objective>> objectives;
	private List<Note> notes;
	private boolean isAManager;
	
	//Empty Constructor
	public Employee(){
		this.employeeID=Constants.INVALID_INT;
		this.level=Constants.INVALID_INT;
		this.LM_ID=Constants.INVALID_INT;
		this.forename=Constants.INVALID_STRING;
		this.surname=Constants.INVALID_STRING;
		this.emaiAddress=Constants.INVALID_STRING;
		this.role=Constants.INVALID_STRING;
		this.LM_Name=Constants.INVALID_STRING;
		this.dob=null;
		this.joinDate=null;
		this.feedback=new ArrayList<Feedback>();
		this.objectives=new ArrayList<List<Objective>>();
		this.notes=new ArrayList<Note>();
		this.isAManager=false;
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
			List<Note> notes){
		this.setID(id);
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
	}
	
	public void setID(int id){
		if(id>0)
			this.employeeID=id;
		else
			this.employeeID=Constants.INVALID_INT;
	}
	
	public int getID(){
		return this.employeeID;
	}
	
	/**
	 * 
	 * @param level This is a value between 1 and 15
	 */
	public void setLevel(int level){
		if(level>0 && level<16)
			this.level=level;
		else
			this.level=Constants.INVALID_INT;
	}
	
	public int getLevel(){
		return this.level;
	}
	
	/**
	 * 
	 * @param name The name must not exceed 200 characters
	 */
	public void setForename(String name){
		if(name!=null && name.length()<201)
			this.forename=name;
		else
			this.forename=Constants.INVALID_STRING;
	}
	
	public String getForname(){
		return this.forename;
	}
	
	/**
	 * 
	 * @param surname The surname cannot exceed the 200 characters
	 */
	public void setSurname(String surname){
		if(surname!=null && surname.length()<201)
			this.surname=surname;
		else
			this.surname=Constants.INVALID_STRING;
	}
	
	public String getSurname(){
		return this.surname;
	}
	
	public void setEmailAddress(String email){
		if(email!=null && email.contains("@"))
			this.emaiAddress=email;
		else
			this.emaiAddress=Constants.INVALID_EMAIL;
	}
	
	public String getEmailAddress(){
		return this.emaiAddress;
	}
	
	/**
	 * 
	 * @param role the role value does not exceed the 250 characters
	 */
	public void setRole(String role){
		if(role!=null && role.length()<251)
			this.role=role;
		else
			this.role=Constants.INVALID_STRING;
	}
	
	public String getRole(){
		return this.role;
	}
	
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
	public void setLineManagerName(String name){
		if(name!=null && name.length()<251)
			this.LM_Name=name;
		else
			this.LM_Name=Constants.INVALID_STRING;
	}
	
	public String getLineManagerName(){
		return this.LM_Name;
	}
	
	public void setLineManagerID(int id){
		if(id>0)
			this.LM_ID=id;
		else
			this.LM_ID=Constants.INVALID_INT;
	}
	
	public int getLineManagerID(){
		return this.LM_ID;
	}
	
	public void setDateOFBirth(String date){
		if(date!=null){
			LocalDate tempD=LocalDate.parse(date,Constants.DATE_FORMAT);
			if(tempD.isBefore(LocalDate.now()))
				this.dob=tempD;
		}
		else
			this.dob=null;
	}
	
	public String getDateOfBirth(){
		return this.dob.format(Constants.DATE_FORMAT);
	}
	
	public void setJoinDate(String date){
		if(date!=null){
			LocalDate tempD=LocalDate.parse(date,Constants.DATE_FORMAT);
			if(tempD.isBefore(LocalDate.now()))
				this.joinDate=tempD;
		}
		else
			this.joinDate=null;
	}
	
	public String getJoinDate(){
		return this.joinDate.format(Constants.DATE_FORMAT);
	}
	
	public void setFeedbackList(List<Feedback> feeds){
		if(feeds!=null)
			this.feedback=feeds;
		else
			this.feedback=null;
	}
	
	public List<Feedback> getFeedbackList(){
		return this.feedback;
	}
	
	public void setObjectiveList(List<List<Objective>> objectives){
		if(objectives!=null)
			this.objectives=objectives;
		else
			this.objectives=null;
	}
	
	public List<List<Objective>> getObjectiveList(){
		return this.objectives;
	}
	
	public void setNoteList(List<Note> notes){
		if(notes!=null)
			this.notes=notes;
		else
			this.notes=null;
	}
	
	public List<Note> getNoteList(){
		return this.notes;
	}
	
	public String toString(){
		String s="";
		//Add the generic information
		s+="ID "+this.getID()+"/n"
			+ "Forename "+this.getForname()+"/n"
			+ "Surname "+this.getSurname()+"/n"
			+ "DOB "+this.getDateOfBirth()+"/n"
			+ "EmailAddress "+this.getEmailAddress()+"/n"
			+ "Level "+this.getLevel()+"/n"
			+ "Role "+this.getRole()+"/n"
			+ "IsAManager "+this.getIsAManager()+"/n"
			+ "JoinDate "+this.getJoinDate()+"/n"
			+ "LineManagerID "+this.getLineManagerID()+"/n"
			+ "LineManagerName "+this.getLineManagerName()+"/n";
		//Add the feedback
		s+="Feedback:/n";
		for(Feedback temp:this.feedback){
			s+=temp.toString()+"/n";
		}
		//Add the objectives
		s+="Objectives:/n";
		int counter=1;
		for(List<Objective> objList: objectives){
			s+="Objective "+counter++ +"/n";
			int version=1;
			for(Objective obj: objList){
				s+="Version "+version++ +"/n"+obj.toString()+"/n";
			}
		}
		//Add the Notes
		s+="Notes:/n";
		for(Note tempN:notes){
			s+=tempN.toString()+"/n";
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
	 */
	public boolean addGenericFeedback(Feedback obj){
		if(feedback==null)
			feedback=new ArrayList<Feedback>();
		if(obj.isFeedbackValid())
			return feedback.add(obj);
		return false;
	}
	
	public boolean addFeedbackToObjective(int objectiveID, Feedback obj){
		//Search for the objective ID within the list of objectives
		//add the objective if the ID is found
		for(int i=0; i<objectives.size(); i++){
			//If the appropriate objective is found, add the feedback to its list
			if(((Objective) objectives.get(i)).getID()==objectiveID)
				return ((Objective) objectives.get(i)).addFeedback(obj); 
		}
		return false;
	}
	
	

}
