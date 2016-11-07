package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.management.InvalidAttributeValueException;

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
public class Competencies {
	
	private int id;
	private boolean isSelected;
	private String title;
	private String description;
	private String timeStamp;
	private String competencyName;
	private String competencyDescription;
	
	
	public Competencies() {
		this.id = Constants.INVALID_INT;
		this.isSelected = false;
		this.title=Constants.INVALID_STRING;
		this.description=Constants.INVALID_STRING;
		this.timeStamp = null; 
		this.competencyName = Constants.INVALID_STRING;
		this.competencyDescription = Constants.INVALID_STRING;
	}//Competencies Constructor
	
	public Competencies(int id, String title,
			String description) throws InvalidAttributeValueException {
		this.id = id;
		this.isSelected = isSelected();
		this.timeStamp = null;
		this.setCompetencyName(id);
		this.setCompetencyDescription(id);
	}//Competencies Constructor with Parameters
	
	public boolean isSelected() {
		if (isSelected == true) {
			return true;
		}//if
		else{ return false;}
	}//isSelected
	
	public int getID(){
		return this.id;
	}
	
	/**
	 * 
	 * @param title The title of the development need cannot exceed the 150 characters
	 */
	public void setTitle(String title) throws InvalidAttributeValueException{
		if(title!=null && title.length()>0 && title.length()<151)
			this.title=title;
		else{
			this.title=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'title' is not valid in this context");
		}
	}

	public String getTitle(){
		return this.title;
	}

	/**
	 * 
	 * @param description The description of the objective cannot exceed the 1000 characters
	 */
	public void setDescription(String description) throws InvalidAttributeValueException{
		if(description!=null && description.length()>0 && description.length()<1001)
			this.description=description;
		else{
			this.description=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'description' is not valid in this context");
		}
	}

	public String getDescription(){
		return this.description;
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
		//return this.timeStamp.format(Constants.DATE_TIME_FORMAT);
		//DateFormat dateFormat = new SimpleDateFormat(Constants.COMPLETE_DATE_TIME_FORMAT);
		return this.timeStamp;
	}//getTimeStamp
	
	
	/**
	 * 
	 * This method sets the Competency based on a valid id as well as returning true
	 * for IsSelected method
	 */
	public void setCompetencyName(int compId) {
		if(compId == 0 | compId < Constants.NO_OF_COMPS & isSelected) {
			this.competencyName = Constants.competencyName[compId];
		}//if
		else{
			this.competencyName = Constants.INVALID_STRING;
		}//else
	}//setCompetencyName
	
	//Method to return Competency Name
	public String getCompentencyName(){
		return competencyName;
	}//getCompetencyName
	
	/**
	 * 
	 * This method sets the competency Description based on a valid id & returning true
	 * for IsSelected method
	 */
		public void setCompetencyDescription(int compId) {
			if(compId == 0 | compId < Constants.NO_OF_COMPS & isSelected) {
				this.competencyDescription = Constants.competencyDescription[compId];
			}//if
			else{
				this.competencyDescription = Constants.INVALID_STRING;
			}//else
		}//setCompetencyName
		
		//Method to return Competency Name
		public String getCompentencyDescription(){
			return competencyDescription;
		}//getCompetencyName	
	
	
	
	
	
	

}//Competencies
