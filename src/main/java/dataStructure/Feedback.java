package dataStructure;

import java.time.LocalDateTime;
import com.google.gson.Gson;
//import java.util.ArrayList;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Feedback object
 *
 */
public class Feedback {
	
	//Global Variables
	private int id, performance;
	private String fromWho, description, type, source;
	private LocalDateTime timeStamp;
	//private List<String> attachments;
	
	//Empty Constructor
	public Feedback(){
		this.id=Constants.INVALID_INT;
		this.performance=Constants.INVALID_INT;
		this.fromWho="";
		this.description="";
		this.type="";
		this.source="";
		timeStamp=null;
		//this.attachments=new ArrayList<String>;
	}
	
	//Constructor with parameters
	public Feedback(
			int id, 
			int perf, 
			String from, 
			String desc, 
			String type, 
			String source){
		this.setID(id);
		this.setPerformance(perf);
		this.setFromWho(from);
		this.setDescription(desc);
		this.setType(type);
		this.setSource(source);
		this.timeStamp=null;
		this.setTimeStamp();
		//this.attachments=new ArrayList<String>;
		//this.attachments=attac;
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
	
	public void setPerformance(int performance){
		if(performance>=0 && performance<=100)
			this.performance=performance;
		else
			this.performance=Constants.INVALID_INT;
	}
	
	public int getPerformance(){
		return this.performance;
	}
	
	/**
	 * 
	 * @param from this string contains the name of who left the feedback and it
	 * must not exceed the 150 characters
	 */
	public void setFromWho(String from){
		if(from!=null && from!="" && from.length()<150)
			this.fromWho=from;
		else
			this.fromWho=Constants.INVALID_STRING;
	}
	
	public String getFromWho(){
		return this.fromWho;
	}
	
	/**
	 * 
	 * @param description This string must be valid and with a length less than 1000 characters
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
	
	/**
	 * 
	 * @param type This string must be valid and can only contain the value Internal or External
	 */
	public void setType(String type){
		if(type!=null && (type.toLowerCase().equals("internal") || type.toLowerCase().equals("external")))
			this.type=type;
		else
			this.type=Constants.INVALID_STRING;
	}
	
	public String getType(){
		return this.type;
	}
	
	/**
	 * 
	 * @param source This string must be valid and its length must be contained within 30 characters
	 */
	public void setSource(String source){
		if(source!=null && source.length()<30)
			this.source=source;
		else
			this.source=Constants.INVALID_STRING;
	}
	
	public String getSource(){
		return this.source;
	}
	
	/**
	 * 
	 * This method saves the current DateTime inside the timeStamp object only if the object does not
	 * contain anything yet
	 */
	private void setTimeStamp(){
		if(this.timeStamp==null)
			this.timeStamp=LocalDateTime.now();
	}
	
	public String getTimeStamp(){
		return this.timeStamp.format(Constants.DATE_TIME_FORMAT);
	}
	
	public String toString(){
		String s="";
		s+="ID "+this.id+"/n"
			+ "Performance "+this.performance+"/n"
			+ "From "+this.fromWho+"/n"
			+ "Description "+this.description+"/n"
			+ "Type "+this.type+"/n"
			+ "Source "+this.source+"/n"
			+ "TimeStamp "+this.getTimeStamp();
		return s;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	public boolean isFeedbackValid(){
		return (this.getID()!=-1 && this.getTimeStamp()!=null);
	}

}
