package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.management.InvalidAttributeValueException;
import org.mongodb.morphia.annotations.Embedded;
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
@Embedded
public class Note implements Serializable{
	
	private static final long serialVersionUID = 3232489026577284657L;
	//Global Variables
	private int id;
	private String body, timeStamp, fromWho;
	
	//Empty Constructor
	public Note(){
		this.id=Constants.INVALID_INT;
		this.body="";
		this.timeStamp=null;
		this.fromWho="";
	}
	
	//Constructor with Parameters
	public Note(int id, String body, String from) throws InvalidAttributeValueException{
		this.setID(id);
		this.setBody(body);
		this.timeStamp=null;
		this.setTimeStamp();
		this.setFromWho(from);
	}
	
	/**
	 * This method sets the ID of the note
	 * 
	 * @param id
	 * @throws InvalidAttributeValueException
	 */
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
	 * @param body This contains the notes of the user, but the text must not exceed the 1000 characters
	 */
	public void setBody(String body) throws InvalidAttributeValueException{
		if(body!=null && body.length()>0 && body.length()<1001)
			this.body=body;
		else{
			this.body=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The body is not valid in this context");
		}
	}
	
	public String getBody(){
		return this.body;
	}
	
	/**
	 * 
	 * @param from String containing the name of the author of the note
	 * @throws InvalidAttributeValueException
	 */
	public void setFromWho(String from) throws InvalidAttributeValueException{
		if(from!=null && from.length()>0 && from.length()<150)
			this.fromWho=from;
		else{
			this.fromWho=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The name of the note writer is not valid in this context");
		}
	}
	
	public String getFromWho(){
		return this.fromWho;
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
		}
	}
	
	public String getTimeStamp(){
		//return this.timeStamp.format(Constants.DATE_TIME_FORMAT);
		//DateFormat dateFormat = new SimpleDateFormat(Constants.COMPLETE_DATE_TIME_FORMAT);
		return this.timeStamp;
	}
	
	@Override
	public String toString(){
		String s="";
		s+="ID "+this.id+"\n"
			+ "Body "+this.body+"\n"
			+ "From "+this.fromWho+"\n"
			+ "Time "+this.getTimeStamp();
		return s;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	/**
	 * Method used to check if the Note object is valid
	 * @return
	 */
	public boolean isNoteValid(){
		return (this.getID()>0 && !this.getBody().contains("Invalid") && !this.getFromWho().contains("Invalid"));
	}

}
