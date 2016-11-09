package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.mongodb.morphia.annotations.Embedded;
import com.google.gson.Gson;

/**
 * 
 * @author Christopher Kai
 * @version 1.0
 * @since 26th October 2016
 * 
 * This class contains the definition of the Competency object
 *
 */
@Embedded
public class Competency implements Serializable {

	private static final long serialVersionUID = -1186038518710616207L;
	private int id;
	private boolean isSelected;
	private String title;
	private String description;
	private String timeStamp;

	//Empty constructor
	public Competency() {
		this.id = Constants.INVALID_INT;
		this.isSelected = false;
		this.title=Constants.INVALID_STRING;
		this.description=Constants.INVALID_STRING;
		this.timeStamp = null; 
	}

	//Constructor with Parameters
	public Competency(int id, boolean status){
		this.id = id;
		this.isSelected=status;
		this.title="";
		this.description="";
		this.timeStamp = null;
		this.setTimeStamp();
	}

	//Method to set ID
	public void setID(int id){
		if(id>0)
			this.id=id;
		else
			this.id=Constants.INVALID_INT;
	}

	//Method returning ID
	public int getID(){
		return this.id;
	}
	
	//Method returning selected Competency based on true or false
	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * 
	 * This method saves the current DateTime inside the timeStamp object only if the object does not
	 * contain anything yet
	 */
	private void setTimeStamp(){
		if(this.timeStamp==null){
			LocalDateTime temp=LocalDateTime.now();
			this.timeStamp=temp.toString();
		}//if
	}//setTimeStamp

	public String getTimeStamp(){
		return this.timeStamp;
	}//getTimeStamp


	/**
	 * 
	 * This method sets the Competency based on a valid id as well as returning true
	 * for IsSelected method
	 */
	public void setTitle(int compId) {
		if(compId == 0 | compId < Constants.NO_OF_COMPS) {
			this.title = Constants.COMPETENCY_NAMES[compId];
		}//if
		else{
			this.title = Constants.INVALID_STRING;
		}//else
	}//setCompetencyName

	//Method to return Competency Name
	public String getTitle(){
		return title;
	}//getCompetencyName

	/**
	 * 
	 * This method sets the competency Description based on a valid id & returning true
	 * for IsSelected method
	 */
	public void setDescription(int compId) {
		if(compId == 0 | compId < Constants.NO_OF_COMPS) {
			this.description = Constants.COMPETENCY_DESCRIPTIONS[compId];
		}//if
		else{
			this.description = Constants.INVALID_STRING;
		}//else
	}//setCompetencyName

	//Method to return Competency Description
	public String getCompentencyDescription(){
		return description;
	}//getCompetencyName	

	//returns boolean result determining if TimeStamp is valid
	public boolean isValid(){
		return this.getTimeStamp()!=null && this.getID()>=0;
	}
	
	//Converts data type from Gson to Json
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	//toString method returning objects details
	public String toString(int index){
		String s="";
		s+="ID: "+id+"\n";
		s+="Is Selected: "+isSelected+"\n";
		s+="Title: "+Constants.COMPETENCY_NAMES[index]+"\n";
		s+="Description: "+Constants.COMPETENCY_DESCRIPTIONS[index]+"\n";
		s+="Time Stamp: "+timeStamp+"\n";
		return s;
	}

}//Competencies
