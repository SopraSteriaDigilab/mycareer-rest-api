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
 * This class contains the definition of the Feedback object
 *
 */
@Embedded
public class Feedback implements Serializable{

	private static final long serialVersionUID = -3137541299399492965L;
	//Global Variables
	private int id;
	private String fromWho, description, type, source, emailBody, timeStamp, fullName;
	private boolean isRequested;

	//Empty Constructor
	public Feedback(){
		this.id=0;
		this.fromWho="";
		this.description="";
		this.type="";
		this.source="";
		this.timeStamp=null;
		this.emailBody="";
		this.fullName="";
		this.isRequested=false;
	}
	
	
	//Constructor with parameter
	public Feedback(int id) throws InvalidAttributeValueException{
		this.setID(id);
	}

	//Constructor with parameters
	public Feedback(
			int id,
			String from, 
			String desc, 
			String type, 
			String source,
			boolean requested) throws InvalidAttributeValueException{
		this.setID(id);
		this.setFromWho(from);
		this.setDescription(desc);
		this.setType(type);
		this.setSource(source);
		this.timeStamp=null;
		this.setTimeStamp();
		this.emailBody="";
		this.fullName="";
		this.isRequested=requested;
	}

	//Constructor with parameters
	public Feedback(
			String from, 
			String desc, 
			String type, 
			String source,
			boolean requested) throws InvalidAttributeValueException{
		this.id=0;
		this.setFromWho(from);
		this.setDescription(desc);
		this.setType(type);
		this.setSource(source);
		this.timeStamp=null;
		this.setTimeStamp();
		this.emailBody="";
		this.fullName="";
		this.isRequested=requested;
	}

	public void setID(int id) throws InvalidAttributeValueException{
		if(id!=0)
			this.id=id;
		else{
			this.id=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The value "+id+" is not valid in this context");
		}
	}

	public int getID(){
		return this.id;
	}
	
	public void setIsRequested(boolean value){
		this.isRequested=value;
	}
	
	public boolean getIsRequested(){
		return this.isRequested;
	}

//	public void setRequestID(String id) throws InvalidAttributeValueException{
//		if(id!=null)
//			this.requestID=id;
//		else{
//			this.requestID=Constants.INVALID_STRING;
//			throw new InvalidAttributeValueException("The requestID is not valid in this context");
//		}
//	}
//
//	public String getRequestID(){
//		return this.requestID;
//	}

	/**
	 * 
	 * @param from this string contains the name of who left the feedback and it
	 * must not exceed the 150 characters
	 */
	public void setFromWho(String from) throws InvalidAttributeValueException{
		if(from!=null && from.length()>0 && from.length()<150)
			this.fromWho=from;
		else{
			this.fromWho=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'fromWho' value is not valid in this context");
		}
	}

	public String getFromWho(){
		return this.fromWho;
	}
	
	public void setFullName(String name) throws InvalidAttributeValueException{
		if(name!=null && name.length()>0 && name.length()<250)
			this.fullName=name;
		else{
			this.fullName=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'fullName' value is not valid in this context");
		}
	}

	public String getFullName(){
		return this.fromWho;
	}

	/**
	 * 
	 * @param description This string must be valid and with a length less than 1000 characters
	 */
	public void setDescription(String description) throws InvalidAttributeValueException{
		if(description!=null && description.length()>0 && description.length()<5001)
			this.description=description;
		else{
			this.description=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'description' value is not valid in this context");
		}
	}

	public String getDescription(){
		return this.description;
	}
	
	public void setEmailBody(String body){
		if(body!=null){
			this.emailBody=body;
		}
	}
	
	public String getEmailBody(){
		return this.emailBody;
	}

	/**
	 * 
	 * @param type This string must be valid and can only contain the value Internal or External
	 */
	public void setType(String type) throws InvalidAttributeValueException{
		if(type!=null && type.length()>0 && (type.toLowerCase().equals("internal") || type.toLowerCase().equals("external")))
			this.type=type;
		else{
			this.type=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'type' value is not valid in this context");
		}
	}

	public String getType(){
		return this.type;
	}

	/**
	 * 
	 * @param source This string must be valid and its length must be contained within 30 characters
	 */
	public void setSource(String source) throws InvalidAttributeValueException{
		if(source!=null && source.length()>0 && source.length()<30)
			this.source=source;
		else{
			this.source=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'source' value is not valid in this context");
		}
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
				+ "From "+this.fromWho+"\n"
				+ "Full Name "+this.fullName+"\n"
				+ "Description "+this.description+"\n"
				+ "Type "+this.type+"\n"
				+ "Source "+this.source+"\n"
				+ "Is Requested "+this.isRequested+"\n"
				+ "TimeStamp "+this.getTimeStamp()+"\n"
				+ "Full Email Body: "+this.getEmailBody();
		return s;
	}

	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}

	public boolean isFeedbackValid(){
		return (this.getID()!=Constants.INVALID_INT && this.getTimeStamp()!=null && !this.getFromWho().contains("Invalid") && !this.getDescription().contains("Invalid") && !this.getType().contains("Invalid") && !this.getSource().contains("Invalid"));
	}
	
	public boolean isFeedbackValidForFeedbackRequest(){
		return this.getID()!=Constants.INVALID_INT;
	}

	public boolean compare(Feedback obj){
		return ((this.description.contains(obj.getDescription())) && (this.fromWho.equals(obj.getFromWho())) && (this.source.equals(obj.getSource())));
	}

}
