package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.management.InvalidAttributeValueException;
import com.google.gson.Gson;

public class FeedbackRequest implements Serializable {
	
	private static final long serialVersionUID = 47606158926933500L;
	//Global Variables
	private String feedbackID, timeStamp;
	private List<String> recepientEmails, replierEmails;
	private String status;
	
	public FeedbackRequest(){
		this.setTimeStamp();
		this.createUniqueID();
		recepientEmails=new ArrayList<String>();
		replierEmails=new ArrayList<String>();
		status=Constants.PENDING_FEEDBACK;
	}
	
	private void createUniqueID(){
		UUID idGen=UUID.randomUUID();
		feedbackID=String.valueOf(idGen).replace("-", "");
	}
	
	public String getID(){
		return this.feedbackID;
	}
	
	public void updateStatus(){
		if(replierEmails.size()>=recepientEmails.size())
			this.status=Constants.RECEIVED_ALL_FEEDBACK;
		else
			this.status=Constants.PENDING_FEEDBACK;
	}
	
	public String getStatus(){
		return this.status;
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
	
	public void setRecipients(List<String> data) throws InvalidAttributeValueException{
		if(data!=null){
			this.recepientEmails=data;
			return;
		}
		throw new InvalidAttributeValueException("The given list of people is empty");
	}
	
	public List<String> getRecipients(){
		return this.recepientEmails;
	}
	
	public void setRepliers(List<String> data) throws InvalidAttributeValueException{
		if(data!=null){
			this.replierEmails=data;
			return;
		}
		throw new InvalidAttributeValueException("The given list of people is empty");
	}
	
	public List<String> getRepliers(){
		return this.replierEmails;
	}
	
	public boolean addRecipient(String email){
		if(recepientEmails==null)
			recepientEmails=new ArrayList<String>();
		//Validate the feedback
		if(!email.equals(""))
			return recepientEmails.add(email);
		return false;
	}
	
	public boolean addSender(String email){
		if(replierEmails==null)
			replierEmails=new ArrayList<String>();
		//Validate the feedback
		if(!email.equals(""))
			return replierEmails.add(email);
		return false;
	}
	
	public String getRepliesOutOf(){
		return "("+replierEmails.size()+"/"+recepientEmails.size()+")";
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	@Override
	public String toString(){
		String s="";
		s+="ID "+this.feedbackID+"\n"
			+ "TimeStamp "+this.timeStamp+"\n"
			+ "Status "+this.status+"\n";
		s+="Recipients:\n";
		for(String temp: this.recepientEmails){
			s+=temp;
		}
		s+="Repliers:\n";
		for(String temp: this.replierEmails){
			s+=temp;
		}
		return s;
	}

}
