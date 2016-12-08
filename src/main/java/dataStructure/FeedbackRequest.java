package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import org.mongodb.morphia.annotations.Embedded;
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
@Embedded
public class FeedbackRequest implements Serializable{//, Iterable<Feedback> {

	private static final long serialVersionUID = 47606158926933500L;
	//Global Variables
	private String feedbackID, timeStamp, recipient;
	@Embedded
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

	/**
	 * 
	 * This method saves the current DateTime inside the timeStamp object only if the object does not
	 * contain anything yet
	 */
	private void setTimeStamp(){
		if(this.timeStamp==null)
			this.timeStamp=LocalDateTime.now().toString();
	}

	public String getTimeStamp(){
		return this.timeStamp;
	}

	public void setRecipient(String toField) throws InvalidAttributeValueException{
		if(toField!=null && !toField.equals(""))
			this.recipient=toField;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_RECIPIENT_CONTEXT);
	}

	public String getRecipient(){
		return this.recipient;
	}

	public void setReplies(List<Feedback> data) throws InvalidAttributeValueException{
		if(data!=null)
			this.replies=data;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_NULLFEEDBACKREQ_REPLIES_CONTEXT);
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
			for(Feedback rep: replies){
				if(rep.getEmailBody().equalsIgnoreCase(reply.getEmailBody()))
					return false;
			}
			return replies.add(new Feedback(reply.getID()));
		}
		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACK);
	}

	public boolean removeReply(Feedback reply) throws InvalidAttributeValueException{
		if(reply!=null && reply.isFeedbackValidForFeedbackRequest() && this.replies!=null){
			for(Feedback t: replies){
				if(t.getID()==reply.getID())
					return replies.remove(t);
			}
			throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKNOTFOUND_CONTEXT);
		}
		else
			throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACK);
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
	
	/*
	 * Adapted from Source: http://stackoverflow.com/questions/5849154/can-we-write-our-own-iterator-in-java
	 */
//	@Override
//	public Iterator<Feedback> iterator() {
//		Iterator<Feedback> iterator=new Iterator<Feedback>(){
//
//			private int currentIndex=0;
//			
//			@Override
//			public boolean hasNext() {
//				return currentIndex < replies.size() && replies.get(currentIndex)!=null;
//			}
//
//			@Override
//			public Feedback next() {
//				return replies.get(currentIndex++);
//			}
//			
//		};
//		return iterator;
//	}

}
