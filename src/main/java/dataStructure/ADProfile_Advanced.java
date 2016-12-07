package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidAttributeValueException;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 14th November 2016
 * 
 * This class contains the definition of the ADProfile_Advanced object
 *
 */
public class ADProfile_Advanced extends ADProfile_Basic implements Serializable{

	//Global Constant
	private static final long serialVersionUID = -5982675570485578676L;
	//Global Variables
	private String GUID, emailAddress, company, team ; 
	private List<String> reporteeCNs;

	//Empty Constructor
	public ADProfile_Advanced(){
		super();
		this.company=Constants.INVALID_STRING;
		this.team=Constants.INVALID_STRING;
		this.emailAddress=Constants.INVALID_STRING;
		this.GUID=Constants.INVALID_STRING;
		this.reporteeCNs=new ArrayList<String>();
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
		super(employeeID, surname, name, isManager, username);
		this.setGUID(guid);
		this.setEmailAddress(email);
		this.setCompany(company);
		this.setTeam(team);
		this.reporteeCNs=new ArrayList<String>();
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
			throw new InvalidAttributeValueException(Constants.INVALID_MAIL_CONTEXT);
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
		else
			throw new InvalidAttributeValueException(Constants.INVALID_GUID_CONTEXT);
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
		else
			throw new InvalidAttributeValueException(Constants.INVALID_COMPANY_CONTEXT);
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
		else
			throw new InvalidAttributeValueException(Constants.INVALID_TEAM_CONTEXT);
	}

	public String getTeam(){
		return this.team;
	}

	/**
	 * This method assigns reportees to a manager
	 * 
	 * @param repostees
	 */
	public void setReporteeCNs(List<String> reportees){
		//Instantiate the list if it hasn't been already done so
		if(this.reporteeCNs==null)
			this.reporteeCNs=new ArrayList<String>();
		//Add each elements inside the list
		if(reportees!=null){
			for(String temp:reportees){
				reporteeCNs.add(temp);
			}
		}
	}

	public List<String> getReporteeCNs(){
		return this.reporteeCNs;
	}

	/**
	 * This method adds a reportee's CN to the list of reportees
	 * 
	 * @param cn
	 * @return true of false indicating whether the operation was successful or not
	 */
	public boolean addReportee(String cn){
		if(this.reporteeCNs==null)
			this.reporteeCNs=new ArrayList<>();
		if(cn!=null && cn.length()>1)
			return reporteeCNs.add(cn);
		return false;
	}

	public String toString(){
		String s="";
		s+=super.toString();
		//Add the generic information
		s+="GUID: "+this.getGUID()+"\n";
		s+="EmailAddress "+this.getEmailAddress()+"\n";
		s+="Company: "+this.getCompany()+"\n";
		s+="Team: "+this.getTeam()+"\n";
		if(getIsManager()){
			s+="List of Reportees: \n";
			s+=this.getReporteeCNs().toString();
		}
		s+="\n";
		return s;
	}

}
