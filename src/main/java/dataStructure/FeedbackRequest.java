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
	private String feedbackID, timeStamp, recipient;
	private List<Feedback> replies;

	public FeedbackRequest(){
		this.feedbackID="";
		this.timeStamp=null;
		this.recipient="";
		replies=new ArrayList<Feedback>();
	}

	public FeedbackRequest(long id){
		this.setTimeStamp();
		this.generateID(id);
		this.recipient="";
		replies=new ArrayList<Feedback>();
	}

	public FeedbackRequest(FeedbackRequest req){
		this.feedbackID=req.getID();
		this.timeStamp=req.getTimeStamp();
		this.recipient=req.getRecipient();
		this.replies=req.getReplies();
	}

	private void generateID(long id){
		LocalDateTime date=LocalDateTime.now();
		//Remove all the symbols that we don't need
		String dateS=date.toString().replace("-", "").replace("T", "").replace(":", "").replace(".", "");
		this.feedbackID=id+"_"+dateS;
	}

	public String getID(){
		return this.feedbackID;
	}

	//	public String getGroupID(){
	//		return this.groupFeedbackID;
	//	}

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

	public void setRecipient(String toField) throws InvalidAttributeValueException{
		if(toField!=null && !toField.equals("")){
			this.recipient=toField;
			return;
		}
		throw new InvalidAttributeValueException("The given TO Field is Invalid");
	}

	public String getRecipient(){
		return this.recipient;
	}

	public void setReplies(List<Feedback> data) throws InvalidAttributeValueException{
		if(data!=null){
			this.replies=data;
			return;
		}
		throw new InvalidAttributeValueException("The given list of Replies is empty");
	}

	public List<Feedback> getReplies(){
		return this.replies;
	}

	public boolean addReply(Feedback reply) throws InvalidAttributeValueException{
		if(this.replies==null)
			this.replies=new ArrayList<Feedback>();
		//Validate the feedback
		if(reply!=null && reply.isFeedbackValidForFeedbackRequest()){
			//Check if the element already exists within the user data 
			if(replies.contains(reply))
				return false;
			//If it doesn't exists, add it after updating the feedback ID
			//reply.setID((replies.size()+1));
			return replies.add(new Feedback(reply.getID()));
		}
		return false;
	}

	public boolean removeReply(Feedback reply) throws InvalidAttributeValueException{
		if(reply!=null && reply.isFeedbackValidForFeedbackRequest() && this.replies!=null){
			for(Feedback t: replies){
				if(t.getID()==reply.getID())
					return replies.remove(t);
			}
			return false;
		}
		else
			throw new InvalidAttributeValueException("Invalid Reply");
	}
	
	public boolean isValid(){
		return this.feedbackID!=null && !this.feedbackID.equals("") && this.recipient!=null && !this.recipient.equals("");
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
				+ "Recipient:"+this.getRecipient()+"\n";
		s+="Replies:\n";
		for(Feedback temp: this.replies){
			s+=temp.toString();
		}
		return s;
	}

}
