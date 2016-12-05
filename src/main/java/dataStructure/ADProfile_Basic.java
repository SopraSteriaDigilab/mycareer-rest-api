package dataStructure;

import java.io.Serializable;

import javax.management.InvalidAttributeValueException;

import com.google.gson.Gson;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 14th November 2016
 * 
 * This class contains the definition of the ADProfile_Basic object
 *
 */
public class ADProfile_Basic implements Serializable{
	
	//Global Constants
	private static final long serialVersionUID = 6335090383770271897L;
	
	//Global Variables
	private long employeeID;
	private String surname, forename, username;
	private boolean isManager;
	
	//Empty Constructor
	public ADProfile_Basic(){
		this.surname=Constants.INVALID_STRING;
		this.forename=Constants.INVALID_STRING;
		this.employeeID=Constants.INVALID_INT;
		this.username=Constants.INVALID_STRING;
		this.isManager=false;
	}
	
	//Constructor with parameters
	public ADProfile_Basic(long employeeID, String surname, String forename, boolean manager, String username) throws InvalidAttributeValueException{
		this.setSurname(surname);
		this.setForename(forename);
		this.setEmployeeID(employeeID);
		this.setUsername(username);
		this.isManager=manager;
	}
	
	public void setEmployeeID(long id) throws InvalidAttributeValueException{
		if(id>0)
			this.employeeID=id;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_USERID_CONTEXT);
	}

	public long getEmployeeID(){
		return this.employeeID;
	}
	
	/**
	 * 
	 * This method sets the user name of the employee which length must be less than 50 characters
	 * 
	 * @param user
	 * @throws InvalidAttributeValueException
	 */
	public void setUsername(String user) throws InvalidAttributeValueException{
		if(user!=null && user.length()>0 && user.length()<50){
			this.username=user;
		}
		else
			throw new InvalidAttributeValueException(Constants.INVALID_USERNAME_CONTEXT);
	}

	public String getUsername(){
		return this.username;
	}

	public void setSurname(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<300){
			this.surname=name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
		}
		else
			throw new InvalidAttributeValueException(Constants.INVALID_SURNAME_CONTEXT);
	}
	
	public String getSurname(){
		return this.surname;
	}
	
	public void setForename(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<300){
			this.forename=name;
		}
		else
			throw new InvalidAttributeValueException(Constants.INVALID_FORENAME_CONTEXT);
	}
	
	public String getForename(){
		return this.forename;
	}
	
	public String getFullName(){
		return this.forename+" "+this.surname;
	}
	
	public void setIsManager(boolean value){
		this.isManager=value;
	}
	
	public boolean getIsManager(){
		return isManager;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	@Override
	public String toString(){
		String s="";
		s+="FullName: "+this.getFullName()+"\n";
		s+="EmployeeID: "+this.getEmployeeID()+"\n";
		s+="IsManager: "+this.getIsManager()+"\n";
		s+="Username: "+this.getUsername()+"\n";
		s+="IsManager: "+this.getIsManager()+"\n";
		return s;
	}

}
