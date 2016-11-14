package dataStructure;

import java.io.Serializable;

import javax.management.InvalidAttributeValueException;

import com.google.gson.Gson;

public class ADProfile implements Serializable{
	
	//Global Constants
	private static final long serialVersionUID = 6335090383770271897L;
	
	//Global Variables
	private String displayName, company, sAMAccountName, team; 
	private long employeeID;
	
	//Empty Constructor
	public ADProfile(){
		this.displayName=Constants.INVALID_STRING;
		this.company=Constants.INVALID_STRING;
		this.sAMAccountName=Constants.INVALID_STRING;
		this.team=Constants.INVALID_STRING;
		this.employeeID=Constants.INVALID_INT;
	}
	
	//Constructor with parameters
	public ADProfile(long employeeID, String fullName, String company, String accountName, String team) throws InvalidAttributeValueException{
		this.setEmployeeID(employeeID);
		this.setDisplayName(fullName);
		this.setCompany(company);
		this.setSAMAccountName(accountName);
		this.setTeam(team);
	}
	
	public void setEmployeeID(long id) throws InvalidAttributeValueException{
		if(id>0){
			this.employeeID=id;
		}
		else
			throw new InvalidAttributeValueException("The ID with value "+id+" is not valid in this context");
	}
	
	public long getEmployeeID(){
		return this.employeeID;
	}
	
	public void setDisplayName(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<300){
			this.displayName=name;
		}
		else
			throw new InvalidAttributeValueException("The name provided is not valid in this context");
	}
	
	public String getDisplayName(){
		return this.displayName;
	}
	
	public void setCompany(String company) throws InvalidAttributeValueException{
		if(company!=null && !company.equals("") && company.length()<150){
			this.company=company;
		}
		else
			throw new InvalidAttributeValueException("The company name provided is not valid in this context");
	}
	
	public String getCompanyName(){
		return this.company;
	}
	
	public void setSAMAccountName(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<50){
			this.sAMAccountName=name;
		}
		else
			throw new InvalidAttributeValueException("The account name provided is not valid in this context");
	}
	
	public String getSAMAccountName(){
		return this.sAMAccountName;
	}
	
	public void setTeam(String team) throws InvalidAttributeValueException{
		if(team!=null && !team.equals("") && team.length()<50){
			this.team=team;
		}
		else
			throw new InvalidAttributeValueException("The team name provided is not valid in this context");
	}
	
	public String getTeam(){
		return this.team;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	@Override
	public String toString(){
		String s="";
		s+="EmployeeID: "+this.getEmployeeID()+"\n";
		s+="DisplayName: "+this.getDisplayName()+"\n";
		s+="Company: "+this.getCompanyName()+"\n";
		s+="sAMAccount: "+this.getSAMAccountName()+"\n";
		s+="Team: "+this.getTeam()+"\n";
		return s;
	}

}
