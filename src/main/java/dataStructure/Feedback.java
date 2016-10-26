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
	private int id, performance;
	private String fromWho, description, type, source;
	private String timeStamp;
	private String requestID;
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
		requestID="";
		//this.attachments=new ArrayList<String>;
	}

	//Constructor with parameters
	public Feedback(
			int id, 
			int perf, 
			String from, 
			String desc, 
			String type, 
			String source) throws InvalidAttributeValueException{
		this.setID(id);
		this.setPerformance(perf);
		this.setFromWho(from);
		this.setDescription(desc);
		this.setType(type);
		this.setSource(source);
		this.timeStamp=null;
		this.setTimeStamp();
		requestID="";
		//this.attachments=new ArrayList<String>;
		//this.attachments=attac;
	}

	//Constructor with parameters
	public Feedback(
			int perf, 
			String from, 
			String desc, 
			String type, 
			String source) throws InvalidAttributeValueException{
		this.setPerformance(perf);
		this.setFromWho(from);
		this.setDescription(desc);
		this.setType(type);
		this.setSource(source);
		this.timeStamp=null;
		this.setTimeStamp();
		requestID="";
		//this.attachments=new ArrayList<String>;
		//this.attachments=attac;
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

	public void setPerformance(int performance) throws InvalidAttributeValueException{
		if(performance>=0 && performance<=100)
			this.performance=performance;
		else{
			this.performance=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The performance value is not valid in this context");
		}
	}

	public int getPerformance(){
		return this.performance;
	}

	public void setRequestID(String id) throws InvalidAttributeValueException{
		if(id!=null && !id.equals(""))
			this.requestID=id;
		else{
			this.requestID=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The requestID is not valid in this context");
		}
	}

	public String getRequestID(){
		return this.requestID;
	}

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

	/**
	 * 
	 * @param description This string must be valid and with a length less than 1000 characters
	 */
	public void setDescription(String description) throws InvalidAttributeValueException{
		if(description!=null && description.length()>0 && description.length()<3001)
			this.description=description;
		else{
			this.description=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'description' value is not valid in this context");
		}
	}

	public String getDescription(){
		return this.description;
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
				+ "Performance "+this.performance+"\n"
				+ "From "+this.fromWho+"\n"
				+ "Description "+this.description+"\n"
				+ "Type "+this.type+"\n"
				+ "Source "+this.source+"\n"
				+ "TimeStamp "+this.getTimeStamp()+"\n"
				+ "RequestID "+this.requestID;
		return s;
	}

	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}

	public boolean isFeedbackValid(){
		return (this.getID()!=-1 && this.getTimeStamp()!=null && !this.getFromWho().contains("Invalid") && !this.getDescription().contains("Invalid") && !this.getType().contains("Invalid") && !this.getSource().contains("Invalid"));
	}

	public boolean compare(Feedback obj){
		return ((this.description.contains(obj.getDescription())) && (this.fromWho.equals(obj.getFromWho())) && (this.source.equals(obj.getSource())));
	}

}
