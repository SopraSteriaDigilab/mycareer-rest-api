package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import com.google.gson.Gson;

public class EmployeeProfile_NEW implements Serializable {		
	//Global Constants
	private static final long serialVersionUID = 6335090383770271897L;
	
	//Global Variables
	private long employeeID;
	private String surname;
	private String forename;
	private String username;
	private String emailAddress;
	private boolean isManager;
	private String GUID;
	private String company;
	private String team ; 
	private List<String> reporteeCNs;
	
	//Constructor with parameters
	public EmployeeProfile_NEW (
			long employeeID,
			String guid,
			String name, 
			String surname, 
			String emailAddress,
			String username,
			String company,
			String team,
			boolean isManager,
			List<String> reps) throws InvalidAttributeValueException {
		this.isManager=isManager;
		setSurname(surname);
		setForename(forename);
		setEmployeeID(employeeID);
		setUsername(username);
		setEmailAddress(emailAddress);
		setGUID(guid);
		setCompany(company);
		setTeam(team);
		setReporteeCNs(reps);
	}
	
	/**
	 * 
	 * @param guid this is a unique value created for each employee of the company 
	 * @throws InvalidAttributeValueException
	 */
	public void setGUID(String guid) throws InvalidAttributeValueException{
		if(guid!=null && guid.length()>0)
			this.GUID=guid;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_GUID);
	}
	
	/**
	 * 
	 * @param com the company name which length must be less than 150 characters
	 * @throws InvalidAttributeValueException
	 */
	public void setCompany(String com) throws InvalidAttributeValueException{
		if(com!=null && com.length()>0 && com.length()<150)
			this.company=com;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_COMPANY);
	}
	
	/**
	 * 
	 * @param team the team name which length must be less than 150 characters
	 * @throws InvalidAttributeValueException
	 */
	public void setTeam(String team) throws InvalidAttributeValueException{
		if(team!=null && team.length()>0 && team.length()<150)
			this.team=team;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_TEAM);
	}
	
	/**
	 * This method assigns reportees to a manager
	 * 
	 * @param repostees
	 * @throws InvalidAttributeValueException 
	 */
	public void setReporteeCNs(List<String> reportees) throws InvalidAttributeValueException{
		//Instantiate the list if it hasn't been already done so
		if(this.reporteeCNs==null)
			this.reporteeCNs=new ArrayList<String>();
		//Add each elements inside the list
		if(reportees!=null){
			for(String temp:reportees){
				reporteeCNs.add(temp);
			}
		}
		else
			throw new InvalidAttributeValueException(Constants.INVALID_NULLREPORTEESLIST);
	}

	/**
	 * 
	 * @param email The user email address
	 * @throws InvalidAttributeValueException
	 */
	public void setEmailAddress(String email) throws InvalidAttributeValueException{
		if(email!=null && email.length()>0 && email.contains("@"))
			this.emailAddress=email;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_GUID);
	}

	public String getEmailAddress(){
		return this.emailAddress;
	}

	public void setEmployeeID(long id) throws InvalidAttributeValueException{
		if(id>0)
			this.employeeID=id;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERID);
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
		if(user!=null && user.length()>0 && user.length()<50)
			this.username=user;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERNAME);
	}

	public String getUsername(){
		return this.username;
	}

	public void setSurname(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<300)
			this.surname=name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_SURNAME);
	}
	
	public String getSurname(){
		return this.surname;
	}
	
	public void setForename(String name) throws InvalidAttributeValueException{
		if(name!=null && !name.equals("") && name.length()<300)
			this.forename=name;
		else
			throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_FORENAME);
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
	
	public List<String> getReporteeCNs(){
		return new ArrayList<String>(reporteeCNs);
	}
	
	public String getTeam(){
		return this.team;
	}
	
	public String getCompany(){
		return this.company;
	}
	
	public String toGson(){
		Gson gsonData=new Gson();
		return gsonData.toJson(this);
	}
	
	@Override
	public String toString(){
		String s="";
		s+="FullName: "+getFullName()+"\n";
		s+="EmployeeID: "+employeeID+"\n";
		s+="IsManager: "+isManager+"\n";
		s+="Username: "+username+"\n";
		s+="IsManager: "+isManager+"\n";
		s+="GUID: "+GUID+"\n";
		s+="EmailAddress "+emailAddress+"\n";
		s+="Company: "+company+"\n";
		s+="Team: "+team+"\n";
		if(isManager){
			s+="List of Reportees: \n";
			s+=reporteeCNs.toString();
		}
		s+="\n";
		return s;
	}
	
	
}
