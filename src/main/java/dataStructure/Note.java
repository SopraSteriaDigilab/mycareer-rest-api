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
	private int id, noteType, linkID;
	private String body, timeStamp, fromWho, linkTitle;
	
	//Empty Constructor
	public Note(){
		this.id=Constants.INVALID_INT;
		this.noteType=Constants.INVALID_INT;
		this.linkID=Constants.INVALID_INT;
		this.body="";
		this.timeStamp=null;
		this.fromWho="";
		this.linkTitle="";
	}
	
	//Constructor with Parameters
	public Note(int id, int noteType, int linkID, String body, String from) throws InvalidAttributeValueException{
		this.setID(id);
		this.setNoteType(noteType);
		this.setLinkID(linkID);
		this.setBody(body);
		this.timeStamp=null;
		this.setTimeStamp();
		this.setFromWho(from);
		this.linkTitle="";
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
		else
			throw new InvalidAttributeValueException(Constants.INVALID_USERID_CONTEXT);
	}
	
	public int getID(){
		return this.id;
	}
	
	/**
	 * 
	 * @param noteType This contains the type of note at int.
	 * 0 - General
	 * 1 - Objective
	 * 2 - Competency
	 * 3 - Feedback
	 * 4 - Development
	 * 5 - Team
	 * @throws InvalidAttributeValueException 
	 */
	public void setNoteType(int noteType) throws InvalidAttributeValueException {
		if(noteType >= 0 && noteType <= 6){
			this.noteType = noteType;
		}else
			throw new InvalidAttributeValueException(Constants.INVALID_NOTE_TYPE);
	}

	public int getNoteType() {
		return noteType;
	}
	
	public void setLinkTitle(String linkTitle) throws InvalidAttributeValueException{
		if(linkTitle!=null && linkTitle.length()>0 && linkTitle.length()<Constants.MAX_TITLE_LENGTH){
			this.linkTitle=linkTitle;
		}
		else
			throw new InvalidAttributeValueException(Constants.INVALID_NOTE_LINKTYTLE);
	}
	
	public String getLinkTitle(){
		return this.linkTitle;
	}
	
	
	/**
	 * This method sets the linkID of the note
	 * 
	 * @param id
	 * @throws InvalidAttributeValueException
	 */
	public void setLinkID(int linkID) throws InvalidAttributeValueException {
		if(id>0)
			this.linkID = linkID;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_USERID_CONTEXT);
	}
	
	public int getLinkID() {
		return linkID;
	}



	/**
	 * 
	 * @param body This contains the notes of the user, but the text must not exceed the 1000 characters
	 */
	public void setBody(String body) throws InvalidAttributeValueException{
		if(body!=null && body.length()>0 && body.length()<1001)
			this.body=body;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_NOTE_BODY);
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
		else
			throw new InvalidAttributeValueException(Constants.INVALID_NOTE_FROMWHO);
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
		return this.timeStamp;
	}
	
	@Override
	public String toString(){
		String s="";
		s+="ID "+this.id+"\n"
			+ "Body "+this.body+"\n"
			+ "From "+this.fromWho+"\n"
			+ "Time "+this.getTimeStamp()
			+ "Note Type "+this.noteType+"\n"
			+ "LinkID "+this.linkID+"\n"
			+ "Link Title "+this.linkTitle+"\n";
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
