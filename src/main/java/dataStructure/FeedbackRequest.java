package dataStructure;

import java.time.LocalDateTime;

import javax.management.InvalidAttributeValueException;

public class FeedbackRequest {
	
	//Global Variables
	private String Id, timeStamp;
	private int employeeID;
	
	public FeedbackRequest(int userLink) throws InvalidAttributeValueException{
		this.setEmployeeID(userLink);
		this.setTimeStamp();
		this.createUniqueID();
	}
	
	public void setEmployeeID(int id) throws InvalidAttributeValueException{
		if(id>0)
			this.employeeID=id;
		else{
			this.employeeID=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("An ID with value "+id+" is not valid in this context");
		}
	}
	
	public int getEmployeeID(){
		return this.employeeID;
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
	
	public void createUniqueID(){
		
	}
	
	public String getID(){
		return this.Id;
	}

}
