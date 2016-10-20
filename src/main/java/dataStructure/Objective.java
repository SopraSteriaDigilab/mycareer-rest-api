package dataStructure;

import java.io.InvalidClassException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.mongodb.morphia.annotations.Embedded;
import com.google.gson.Gson;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Objective object
 *
 */
@Embedded
public class Objective implements Serializable{
	
	private static final long serialVersionUID = -274154678364673992L;
	//Global Variables
	private int id, progress, performance;
	private String title, description;
	private String timeStamp;
	private String timeToCompleteBy;
	@Embedded
	private List<Feedback> feedback;
	
	//Empty Constructor
	public Objective(){
		this.id=Constants.INVALID_INT;
		this.progress=Constants.INVALID_INT;
		this.performance=Constants.INVALID_INT;
		this.title=Constants.INVALID_STRING;
		this.description=Constants.INVALID_STRING;
		this.timeStamp=null;
		this.timeToCompleteBy=null;
		feedback=new ArrayList<Feedback>();
	}
	
	//Constructor with Parameters
	public Objective(
			int id, 
			int prog, 
			int perf, 
			String title, 
			String descr, 
			String dateToCompleteBy) throws InvalidAttributeValueException{
		this.setID(id);
		this.setProgress(prog);
		this.setPerformance(perf);
		this.setTitle(title);
		this.setDescription(descr);
		this.timeStamp=null;
		this.setTimeStamp();
		this.setTimeToCompleteBy(dateToCompleteBy);
		feedback=new ArrayList<Feedback>();	
	}
	
	//Constructor with Parameters
		public Objective( 
				int prog, 
				int perf, 
				String title, 
				String descr, 
				String dateToCompleteBy) throws InvalidAttributeValueException{
			this.setProgress(prog);
			this.setPerformance(perf);
			this.setTitle(title);
			this.setDescription(descr);
			this.timeStamp=null;
			this.setTimeStamp();
			this.setTimeToCompleteBy(dateToCompleteBy);
			feedback=new ArrayList<Feedback>();	
		}
	
	public void setID(int id) throws InvalidAttributeValueException{
		if(id>0)
			this.id=id;
		else{
			this.id=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The value "+id+" is not valid in this context");
		}
	}
	
	public int getID(){
		return this.id;
	}
	
	/**
	 * 
	 * @param progress This variable can assume only 3 values:
	 *  0 => Awaiting
	 *  1 => In Flight
	 *  2 => Done
	 */
	public void setProgress(int progress) throws InvalidAttributeValueException{
		if(progress>=0 && progress<=2)
			this.progress=progress;
		else{
			this.progress=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The given 'progress' value is not valid in this context");
		}
	}
	
	public int getProgress(){
		return this.progress;
	}
	
	/**
	 * 
	 * @param performance This variable can assume only 3 values:
	 * 0 => Green
	 * 1 => Amber
	 * 2 => Red
	 */
	public void setPerformance(int performance) throws InvalidAttributeValueException{
		if(performance>=0 && performance<=2)
			this.performance=performance;
		else{
			this.performance=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The given 'performance' value is not valid in this context");
		}
	}
	
	public int getPerformance(){
		return this.performance;
	}
	
	/**
	 * 
	 * @param title The title of the object cannot exceed the 150 characters
	 */
	public void setTitle(String title) throws InvalidAttributeValueException{
		if(title!=null && title.length()<151)
			this.title=title;
		else{
			this.title=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'title' value is not valid in this context");
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
		if(description!=null && description.length()<1001)
			this.description=description;
		else{
			this.description=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'description' value is not valid in this context");
		}
	}
	
	public String getDescription(){
		return this.description;
	}
	
	private void setTimeStamp(){
		//Check if the timeStamp has already a value assigned
		if(timeStamp==null){
			LocalDateTime temp=LocalDateTime.now();
			this.timeStamp=temp.toString();
		}
	}
	
	public String getTimeStamp(){
		//return this.timeStamp.format(Constants.DATE_TIME_FORMAT);
		///DateFormat dateFormat = new SimpleDateFormat(Constants.COMPLETE_DATE_TIME_FORMAT);
		return this.timeStamp;
	}
	
	public void setTimeToCompleteBy(String date) throws InvalidAttributeValueException{
		//Convert the String to a YearMonth object
		if(!date.equals("")){
			YearMonth temp=YearMonth.parse(date,Constants.YEAR_MONTH_FORMAT);
			//Verify that the month and year inserted are greater than the current month and year
			//Every year has 12 months, so if the values are 2017 and 2016 the difference will be 1 which is 12 months
			int yearDifference=(temp.getYear()-LocalDate.now().getYear())*12;
			int monthDifference=temp.getMonthValue()-LocalDate.now().getMonthValue();
			//Sum these 2 values up and if the result is <0, the date is in the past which is invalid
			int totalMonthsApart=yearDifference+monthDifference;
			if(totalMonthsApart>=0)
				this.timeToCompleteBy=temp.toString();
		}
		else{
			this.timeToCompleteBy=null;
			throw new InvalidAttributeValueException("The format for the given 'date' is not valid");
		}
	}
	
	public String getTimeToCompleteBy(){
		YearMonth temp=YearMonth.parse(this.timeToCompleteBy,Constants.YEAR_MONTH_FORMAT);
		return temp.format(Constants.YEAR_MONTH_FORMAT);
	}
	
	public void setFeedback(List<Feedback> listData) throws InvalidClassException, InvalidAttributeValueException{
		if(listData!=null){
			//Create a counter that keeps count of the error produced
			int errorCounter=0;
			this.feedback=new ArrayList<Feedback>();
			//Check if the feedback objects inside the list are valid
			for(Feedback temp:listData){
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
			throw new InvalidAttributeValueException("The list of feedback given is null");
		}
	}
	
	public List<Feedback> getFeedback(){
		List<Feedback> data=new ArrayList<Feedback>();
		for(Feedback temp: this.feedback){
			data.add(temp);
		}
		return data;
	}
	
	@Override
	public String toString(){
		String s="";
		s+="ID "+this.id+"\n"
			+ "Progress "+this.progress+"\n"
			+ "Performance "+this.performance+"\n"
			+ "Title "+this.title+"\n"
			+ "Description "+this.description+"\n"
			+ "TimeStamp "+this.getTimeStamp()+"\n"
			+ "TimeToCompleteBy "+this.getTimeToCompleteBy()+"\n";
		for(Feedback temp: this.feedback){
			s+=temp.toString();
		}
		return s;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	public boolean addFeedback(Feedback obj){
		if(feedback==null)
			feedback=new ArrayList<Feedback>();
		if(obj.isFeedbackValid())
			return feedback.add(obj);
		return false;
	}
	
	public boolean isObjectiveValid(){
		 return (this.getID()>0 && !this.getTitle().contains("Invalid") && !this.getDescription().contains("Invalid") && this.getTimeStamp()!=null && this.getTimeToCompleteBy()!=null);
	}
	public boolean isObjectiveValidWithoutID(){
		 return (this.getTitle().contains("Invalid") && !this.getDescription().contains("Invalid") && this.getTimeStamp()!=null && this.getTimeToCompleteBy()!=null);
	}
}
