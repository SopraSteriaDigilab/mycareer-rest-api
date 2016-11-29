package dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.activity.InvalidActivityException;

public class GroupFeedbackRequest implements Serializable{

	//Global Constant
	private static final long serialVersionUID = 6950535901949268747L;
	
	//Global Variables
	private String id;
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
	
	public void setRequestList(List<FeedbackRequest> reqs) throws InvalidActivityException{
		if(reqs!=null)
			this.requests=reqs;
		else
			throw new InvalidActivityException("The feedback request list is invalid");
	}
	
	public List<FeedbackRequest> getRequestList(){
		return this.requests;
	}
	
	public boolean addFeedbackRequest(FeedbackRequest fb) throws InvalidActivityException{
		if(this.requests==null)
			this.requests=new ArrayList<FeedbackRequest>();
		if(fb!=null && fb.isValid()){
			for(FeedbackRequest t: requests){
				if(t.getID().equals(fb.getID()))
					throw new InvalidActivityException("The Feedback request is a duplicate and cannot be added to this GroupFeedback");
			}
			//If the object passes this stage, it's a valid feedback request and can be added to this groupRequests
			return requests.add(fb);
		}
		return false;
	}
	
	public boolean removeFeedbackRequest(String id){
		if(id!=null && this.requests!=null){
			for(FeedbackRequest t: requests){
				if(t.getID().equals(id))
					return requests.remove(t);
			}
		}
		return false;
	}
	
	public boolean updateFeedbackRequest(FeedbackRequest fb){
		if(fb!=null && fb.isValid() && this.requests!=null){
			for(int i=0; i<requests.size(); i++){
				//Once the item is found, remove it from the list and insert the updated one
				if(requests.get(i).getID().equals(fb.getID())){
					requests.remove(i);
					return requests.add(fb);
				}
			}
		}
		return false;
	}
	
	public FeedbackRequest searchFeedbackRequestID(String id) throws InvalidActivityException{
		if(id!=null && !id.equals("")){
			for(FeedbackRequest t:requests){
				if(t.getID().equals(id))
					return t;
			}
			return null;
		}
		throw new InvalidActivityException("The Feedback Request ID is invalid");
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

}
