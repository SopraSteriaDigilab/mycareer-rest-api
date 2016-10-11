package dataStructure;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class Objective {
	
	//Global Variables
	private int id, progress, performance;
	private String title, description;
	private LocalDateTime timeStamp;
	private LocalDate timeToCompleteBy;
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
	public Objective(int id, int prog, int perf, String title, String descr, LocalDate dateToCompleteBy){
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
	
	public void setID(int id){
		if(id>0)
			this.id=id;
		else
			this.id=Constants.INVALID_INT;
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
	public void setProgress(int progress){
		if(progress>=0 && progress<=2)
			this.progress=progress;
		else
			this.progress=Constants.INVALID_INT;
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
	public void setPerformance(int performance){
		if(performance>=0 && performance<=2)
			this.performance=performance;
		else
			this.performance=Constants.INVALID_INT;
	}
	
	public int getPerformance(){
		return this.performance;
	}
	
	/**
	 * 
	 * @param title The title of the object cannot exceed the 150 characters
	 */
	public void setTitle(String title){
		if(title!=null && title.length()<150)
			this.title=title;
		else
			this.title=Constants.INVALID_STRING;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * 
	 * @param description The description of the objective cannot exceed the 1000 characters
	 */
	public void setDescription(String description){
		if(description!=null && description.length()<1001)
			this.description=description;
		else
			this.description=Constants.INVALID_STRING;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	private void setTimeStamp(){
		//Check if the timeStamp has already a value assigned
		if(timeStamp==null)
			timeStamp=LocalDateTime.now();
	}
	
	public LocalDateTime getTimeStamp(){
		return this.timeStamp;
	}
	
	public void setTimeToCompleteBy(LocalDate date){
		if(date!=null && date.isAfter(LocalDate.now()))
			this.timeToCompleteBy=date;
		else
			this.timeToCompleteBy=null;
	}
	
	public LocalDate getTimeToCompleteBy(){
		return this.timeToCompleteBy;
	}
	
	public void setFeedback(List<Feedback> listData){
		if(listData!= null)
			this.feedback=listData;
	}
	
	public List<Feedback> getFeedback(){
		List<Feedback> data=new ArrayList<Feedback>();
		for(Feedback temp: this.feedback){
			data.add(temp);
		}
		return data;
	}
	
	public String toString(){
		String s="";
		s+="ID "+this.id+"/n"
			+ "Progress "+this.progress+"/n"
			+ "Performance "+this.performance+"/n"
			+ "Title "+this.title+"/n"
			+ "Description "+this.description+"/n"
			+ "TimeStamp "+this.timeStamp+"/n"
			+ "TimeToCompleteBy "+this.timeToCompleteBy+"/n";
		for(Feedback temp: this.feedback){
			s+=temp.toString();
		}
		return s;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}

}
