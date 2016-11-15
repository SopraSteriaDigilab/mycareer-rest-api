package dataStructure;

import java.io.Serializable;

import javax.management.InvalidAttributeValueException;

import com.google.gson.Gson;

public class ADProfile_Basic implements Serializable{
	
	//Global Constants
	private static final long serialVersionUID = 6335090383770271897L;
	
	//Global Variables
	private long employeeID;
	private String surname, forename;
	private boolean isManager;
	
	//Empty Constructor
	public ADProfile_Basic(){
		this.surname=Constants.INVALID_STRING;
		this.forename=Constants.INVALID_STRING;
		this.employeeID=Constants.INVALID_INT;
		this.isManager=false;
	}
	
	//Constructor with parameters
	public ADProfile_Basic(long employeeID, String surname, String forename, boolean manager) throws InvalidAttributeValueException{
		this.setSurname(surname);
		this.setForename(forename);
		this.setEmployeeID(employeeID);
		this.isManager=manager;
	}
	
	public void setEmployeeID(long id) throws InvalidAttributeValueException{
		if(id>0)
			this.employeeID=id;
		else{
			this.employeeID=Constants.INVALID_INT;
			throw new InvalidAttributeValueException("The value "+id+" is not valid in this context");
		}
	}

	public long getEmployeeID(){
		return this.employeeID;
	}

	public void setSurname(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<300){
			this.surname=name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
		}
		else
			throw new InvalidAttributeValueException("The surname provided is not valid in this context");
	}
	
	public String getSurname(){
		return this.surname;
	}
	
	public void setForename(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<300){
			this.forename=name;
		}
		else
			throw new InvalidAttributeValueException("The forename provided is not valid in this context");
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
		s+="IsManager: "+this.getIsManager()+"\n";
		return s;
	}

}
