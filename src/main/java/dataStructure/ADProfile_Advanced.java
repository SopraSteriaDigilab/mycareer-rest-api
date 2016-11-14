package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;

public class ADProfile_Advanced extends ADProfile_Basic implements Serializable{

	//Global Constant
	private static final long serialVersionUID = -5982675570485578676L;
	//Global Variables
	private long employeeID;
	private String GUID, emailAddress, username, company, team ; 
	private List<String> reporteeGUIDs;
	
	//Empty Constructor
	public ADProfile_Advanced(){
		super();
		this.company=Constants.INVALID_STRING;
		this.team=Constants.INVALID_STRING;
		this.employeeID=Constants.INVALID_INT;
		this.emailAddress=Constants.INVALID_STRING;
		this.username=Constants.INVALID_STRING;
		this.GUID=Constants.INVALID_STRING;
		this.reporteeGUIDs=new ArrayList<String>();
	}
	
	//Constructor with parameters
	public ADProfile_Advanced(
			long employeeID,
			String guid,
			String name, 
			String surname, 
			String email,
			String username,
			String company,
			String team,
			boolean isManager) throws InvalidAttributeValueException{
		super(surname, name, isManager);
		this.setEmployeeID(employeeID);
		this.setGUID(guid);
		this.setEmailAddress(email);
		this.setUsername(username);
		this.setCompany(company);
		this.setTeam(team);
		this.reporteeGUIDs=new ArrayList<String>();
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
		else{
			this.username=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'username' is not valid in this context");
		}
	}
	
	public String getUsername(){
		return this.username;
	}

	/**
	 * 
	 * @param email The user email address
	 * @throws InvalidAttributeValueException
	 */
	public void setEmailAddress(String email) throws InvalidAttributeValueException{
		if(email!=null && email.length()>0 && email.contains("@"))
			this.emailAddress=email;
		else{
			this.emailAddress=Constants.INVALID_EMAIL;
			throw new InvalidAttributeValueException("The given 'Email Address' is not valid in this context");
		}
	}

	public String getEmailAddress(){
		return this.emailAddress;
	}
	
	/**
	 * 
	 * @param guid this is a unique value created for each employee of the company 
	 * @throws InvalidAttributeValueException
	 */
	public void setGUID(String guid) throws InvalidAttributeValueException{
		if(guid!=null && guid.length()>0){
			this.GUID=guid;
		}
		else{
			this.GUID=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'GUID' is not valid in this context");
		}
	}
	
	public String getGUID(){
		return this.GUID;
	}
	
	/**
	 * 
	 * @param com the company name which length must be less than 150 characters
	 * @throws InvalidAttributeValueException
	 */
	public void setCompany(String com) throws InvalidAttributeValueException{
		if(com!=null && com.length()>0 && com.length()<150){
			this.company=com;
		}
		else{
			this.company=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'company' is not valid in this context");
		}
	}
	
	public String getCompany(){
		return this.company;
	}
	
	/**
	 * 
	 * @param team the team name which length must be less than 150 characters
	 * @throws InvalidAttributeValueException
	 */
	public void setTeam(String team) throws InvalidAttributeValueException{
		if(team!=null && team.length()>0 && team.length()<150){
			this.team=team;
		}
		else{
			this.team=Constants.INVALID_STRING;
			throw new InvalidAttributeValueException("The given 'team' is not valid in this context");
		}
	}
	
	public String getTeam(){
		return this.team;
	}
	
	/**
	 * This method assigns reportees to a manager
	 * 
	 * @param repostees
	 */
	public void setreporteeGUIDs(List<String> reportees){
		//Instantiate the list if it hasn't been already done so
		if(this.reporteeGUIDs==null)
			this.reporteeGUIDs=new ArrayList<String>();
		//Add each elements inside the list
		for(String temp:reportees){
			reporteeGUIDs.add(temp);
		}
	}
	
	public List<String> getReposteeGUIDs(){
		return this.reporteeGUIDs;
	}
	
	public boolean addReportee(String id){
		if(this.reporteeGUIDs==null)
			this.reporteeGUIDs=new ArrayList<>();
		if(id!=null && id.length()>1)
			return reporteeGUIDs.add(id);
		return false;
	}
	
	@Override
	public String toString(){
		String s="";
		s+=super.toString();
		s+="EmployeeID: "+this.getEmployeeID()+"\n";
		s+="Company: "+this.getCompany()+"\n";
		s+="Username: "+this.getUsername()+"\n";
		s+="Team: "+this.getTeam()+"\n";
		return s;
	}
	
}
