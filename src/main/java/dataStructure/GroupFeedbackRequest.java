package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import org.mongodb.morphia.annotations.Embedded;
import com.google.gson.Gson;

@Embedded
public class GroupFeedbackRequest implements Serializable{//, Iterable<FeedbackRequest>{

	//Global Constant
	private static final long serialVersionUID = 6950535901949268747L;
	
	//Global Variables
	private String id;
	@Embedded
	private List<FeedbackRequest> requests;
	
	//Empty Constructor
	public GroupFeedbackRequest(){
		this.id="";
		this.requests=new ArrayList<FeedbackRequest>();
	}
	
	//Constructor with parameters
	public GroupFeedbackRequest(long id){
		this.setID(id);
		this.requests=new ArrayList<FeedbackRequest>();
	}
	
	private void setID(long id){
		LocalDateTime date=LocalDateTime.now();
		//Remove all the symbols that we don't need
		String dateS=date.toString().replace("-", "").replace("T", "").replace(":", "").replace(".", "");
		this.id=id+"_"+dateS;
	}
	
	public String getID(){
		return this.id;
	}
	
	public void setRequestList(List<FeedbackRequest> reqs) throws InvalidAttributeValueException{
		if(reqs!=null)
			this.requests=reqs;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_NULLFEEDBACKREQLIST_CONTEXT);
	}
	
	public List<FeedbackRequest> getRequestList(){
		return this.requests;
	}
	
	public boolean addFeedbackRequest(FeedbackRequest fb) throws InvalidAttributeValueException{
		if(this.requests==null)
			this.requests=new ArrayList<FeedbackRequest>();
		if(fb!=null && fb.isValid()){
			for(FeedbackRequest t: requests){
				if(t.getID().equals(fb.getID()))
					throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKDUPLICATE_CONTEXT);
			}
			//If the object passes this stage, it's a valid feedback request and can be added to this groupRequests
			return requests.add(fb);
		}
		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQLIST_CONTEXT);
	}
	
	public boolean removeFeedbackRequest(String id) throws InvalidAttributeValueException{
		if(id!=null && this.requests!=null){
			for(FeedbackRequest t: requests){
				if(t.getID().equals(id))
					return requests.remove(t);
			}
		}
		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_ID_CONTEXT);
	}
	
	public boolean updateFeedbackRequest(FeedbackRequest fb) throws InvalidAttributeValueException{
		if(fb!=null && fb.isValid() && this.requests!=null){
			for(int i=0; i<requests.size(); i++){
				//Once the item is found, remove it from the list and insert the updated one
				if(requests.get(i).getID().equals(fb.getID())){
					requests.remove(i);
					return requests.add(fb);
				}
			}
			return false;
			//throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_NOTFOUND_CONTEXT);
		}
		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_CONTEXT);
	}
	
	public FeedbackRequest searchFeedbackRequestID(String id) throws InvalidAttributeValueException{
		if(id!=null && !id.equals("")){
			for(FeedbackRequest t:requests){
				if(t.getID().equals(id))
					return t;
			}
			return null;
			//throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_NOTFOUND_CONTEXT);
		}
		throw new InvalidAttributeValueException(Constants.INVALID_FEEDBACKREQ_ID_CONTEXT);
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	@Override
	public String toString(){
		String s="Group Feeback ID: "+this.id+"\n"
				+ "List of Feedback Requests:\n";
		for(FeedbackRequest t:requests){
			s+=t.toString()+"\n";
		}
		return s;
	}

	/*
	 * Adapted from Source: http://stackoverflow.com/questions/5849154/can-we-write-our-own-iterator-in-java
	 */
//	@Override
//	public Iterator<FeedbackRequest> iterator() {
//		Iterator<FeedbackRequest> iterator=new Iterator<FeedbackRequest>(){
//
//			private int currentIndex=0;
//			
//			@Override
//			public boolean hasNext() {
//				return currentIndex < requests.size() && requests.get(currentIndex)!=null;
//			}
//
//			@Override
//			public FeedbackRequest next() {
//				return requests.get(currentIndex++);
//			}
//			
//		};
//		return iterator;
//	}
}
