package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import com.google.gson.Gson;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 24th October 2016
 * 
 * This class contains the definition of the FeedbackRequest object
 *
 */
public class FeedbackRequest implements Serializable {

	private static final long serialVersionUID = 47606158926933500L;
	//Global Variables
	private String feedbackID, timeStamp;
	private List<String> recipientEmails, replierEmails;
	private String status;
	
	public FeedbackRequest(){
		this.feedbackID="";
		this.timeStamp=null;
		recipientEmails=new ArrayList<String>();
		replierEmails=new ArrayList<String>();
		status=Constants.PENDING_FEEDBACK;
	}

	public FeedbackRequest(long id){
		this.setTimeStamp();
		this.createUniqueID(id);
		recipientEmails=new ArrayList<String>();
		replierEmails=new ArrayList<String>();
		status=Constants.PENDING_FEEDBACK;
	}

	private void createUniqueID(long id){
		LocalDateTime date=LocalDateTime.now();
		//Remove all the symbols that we don't need
		String dateS=date.toString().replace("-", "").replace("T", "").replace(":", "").replace(".", "");
		this.feedbackID=id+"_"+dateS;
	}

	public String getID(){
		return this.feedbackID;
	}

	private void updateStatus(){
		if(replierEmails.size()>=recipientEmails.size())
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
			this.recipientEmails=data;
			return;
		}
		throw new InvalidAttributeValueException("The given list of people is empty");
	}
	
	public boolean removeRecipient(String user) throws InvalidAttributeValueException{
		if(user!=null && user.length()>0){
			if(recipientEmails.contains(user))
				return recipientEmails.remove(user);
			return false;
		}
		else
			throw new InvalidAttributeValueException("Invalid Recipient");
	}

	public List<String> getRecipients(){
		return this.recipientEmails;
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
		if(recipientEmails==null)
			recipientEmails=new ArrayList<String>();
		//Validate the feedback
		if(!email.equals("")){
			if(!recipientEmails.contains(email)){
				if(recipientEmails.add(email)){
					updateStatus();
					return true;
				}
			}
		}
		return false;
	}

	public boolean addSender(String email){
		if(replierEmails==null)
			replierEmails=new ArrayList<String>();
		//Validate the feedback
		if(!email.equals("")){
			if(!replierEmails.contains(email)){
				if(replierEmails.add(email)){
					updateStatus();
					return true;
				}
			}
		}
		return false;
	}

	public String getRepliesOutOf(){
		return "("+replierEmails.size()+"/"+recipientEmails.size()+")";
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
		for(String temp: this.recipientEmails){
			s+=temp;
		}
		s+="Repliers:\n";
		for(String temp: this.replierEmails){
			s+=temp;
		}
		return s;
	}

}
