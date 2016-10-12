package dataStructure;

import java.time.LocalDateTime;

import com.google.gson.Gson;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Note object
 *
 */
public class Note {
	
	//Global Variables
	private int id;
	private String body;
	private LocalDateTime timeStamp;
	
	//Empty Constructor
	public Note(){
		this.id=Constants.INVALID_INT;
		this.body="";
		this.timeStamp=null;
	}
	
	//Constructor with Parameters
	public Note(int id, String body){
		this.setID(id);
		this.setBody(body);
		this.timeStamp=null;
		this.setTimeStamp();
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
	 * @param body This contains the notes of the user, but the text must not exceed the 1000 characters
	 */
	public void setBody(String body){
		if(body!=null && body.length()<1001)
			this.body=body;
		else
			this.body=Constants.INVALID_STRING;
	}
	
	public String getBody(){
		return this.body;
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
			+ "Body "+this.body+"/n"
			+ "Time "+this.timeStamp.format(Constants.DATE_TIME_FORMAT);
		return s;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}

}
