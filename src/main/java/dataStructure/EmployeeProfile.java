package dataStructure;

import static dataStructure.Constants.INVALID_CONTEXT_COMPANY;
import static dataStructure.Constants.INVALID_NULLREPORTEE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import com.google.gson.Gson;

public class EmployeeProfile implements Serializable
{
  private static final long serialVersionUID = 6335090383770271897L;

  private long employeeID;
  private String surname;
  private String forename;
  private String username;
  private String emailAddress;
  private boolean isManager;
  private boolean hasHRDash;
  private String GUID;
  private String company;
  private String sopraDepartment;
  private String steriaDepartment;
  private String sector;
  private String superSector;
  private List<String> reporteeCNs;
  
  public EmployeeProfile() throws InvalidAttributeValueException
  {
    reporteeCNs = new ArrayList<>(); 
  }

  public EmployeeProfile(long employeeId, String guid, String forename, String surname, String emailAddress,
      String username, String company, String superSector, String sector, String steriaDepartment,
      String sopraDepartment, boolean isManager, boolean hasHRDash, List<String> reporteesCNs)
          throws InvalidAttributeValueException
  {
    setEmployeeID(employeeID);
    setGUID(guid);
    setForename(forename);
    setSurname(surname);
    setEmailAddress(emailAddress);
    setUsername(username);
    setCompany(company);
    setSuperSector(superSector);
    setSector(sector);
    setSteriaDepartment(steriaDepartment);
    setSopraDepartment(sopraDepartment);
    this.isManager = isManager;
    setHasHRDash(hasHRDash);
    setReporteeCNs(reporteesCNs);
  }

  public String getEmailAddress()
  {
    return this.emailAddress;
  }

  /**
   * 
   * @param email The user email address
   * @throws InvalidAttributeValueException
   */
  public void setEmailAddress(String email) throws InvalidAttributeValueException
  {
    if (email != null && email.length() > 0 && email.contains("@")) this.emailAddress = email;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_GUID);
  }

  public long getEmployeeID()
  {
    return this.employeeID;
  }

  public void setEmployeeID(long id) throws InvalidAttributeValueException
  {
    if (id > 0) this.employeeID = id;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERID);
  }

  public String getUsername()
  {
    return this.username;
  }

  /**
   * 
   * This method sets the user name of the employee which length must be less than 50 characters
   * 
   * @param user
   * @throws InvalidAttributeValueException
   */
  public void setUsername(String user) throws InvalidAttributeValueException
  {
    if (user != null && user.length() > 0 && user.length() < 50) this.username = user;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERNAME);
  }

  public String getSurname()
  {
    return this.surname;
  }

  public void setSurname(String name) throws InvalidAttributeValueException
  {
    if (name != null && !name.equals("") && name.length() < 300)
      this.surname = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_SURNAME);
  }

  public String getForename()
  {
    return this.forename;
  }

  public void setForename(String name) throws InvalidAttributeValueException
  {
    if (name != null && !name.equals("") && name.length() < 300) this.forename = name;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_FORENAME);
  }

  public boolean getIsManager()
  {
    return isManager;
  }

  public void setIsManager(boolean value)
  {
    this.isManager = value;
  }

  public void setHasHRDash(boolean hasHRDash)
  {
    this.hasHRDash = hasHRDash;
  }

  public boolean getHasHRDash()
  {
    return hasHRDash;
  }

  public List<String> getReporteeCNs()
  {
    return new ArrayList<String>(reporteeCNs);
  }

  /**
   * This method assigns reportees to a manager
   * 
   * @param repostees
   * @throws InvalidAttributeValueException
   */
  public void setReporteeCNs(List<String> reportees) throws InvalidAttributeValueException
  {
    // Instantiate the list if it hasn't been already done so
    if (this.reporteeCNs == null) this.reporteeCNs = new ArrayList<String>();
    // Add each elements inside the list
    if (reportees != null)
    {
      for (String temp : reportees)
      {
        reporteeCNs.add(temp);
      }
    }
    else throw new InvalidAttributeValueException(Constants.INVALID_NULLREPORTEESLIST);
  }

  public String getCompany()
  {
    return this.company;
  }

  /**
   * 
   * @param com the company name which length must be less than 150 characters
   * @throws InvalidAttributeValueException
   */
  public void setCompany(String com) throws InvalidAttributeValueException
  {
    if (com != null && com.length() > 0 && com.length() < 150) this.company = com;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_COMPANY);
  }
  
  public String getSopraDepartment()
  {
    return sopraDepartment;
  }

  public void setSopraDepartment(String sopraDepartment) throws InvalidAttributeValueException
  {
    if (sopraDepartment != null && sopraDepartment.length() > 0 && sopraDepartment.length() < 150)
    {
      this.sopraDepartment = sopraDepartment;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public String getSteriaDepartment()
  {
    return steriaDepartment;
  }
  
  public void setSteriaDepartment(String steriaDepartment) throws InvalidAttributeValueException
  {
    if (steriaDepartment != null && steriaDepartment.length() > 0 && steriaDepartment.length() < 150)
    {
      this.steriaDepartment = steriaDepartment;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public String getSector()
  {
    return sector;
  }

  public void setSector(String sector) throws InvalidAttributeValueException
  {
    if (sector != null && sector.length() > 0 && sector.length() < 15)
    {
      this.sector = sector;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public String getSuperSector()
  {
    return superSector;
  }

  public void setSuperSector(String superSector) throws InvalidAttributeValueException
  {
    if (superSector != null && superSector.length() > 0 && superSector.length() < 150)
    {
      this.superSector = superSector;
    }
    else
    {
      throw new InvalidAttributeValueException(INVALID_CONTEXT_COMPANY);
    }
  }

  public String getGUID()
  {
    return GUID;
  }

  /**
   * 
   * @param guid this is a unique value created for each employee of the company
   * @throws InvalidAttributeValueException
   */
  public void setGUID(String guid) throws InvalidAttributeValueException
  {
    if (guid != null && guid.length() > 0) this.GUID = guid;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_GUID);
  }

  public String getFullName()
  {
    return this.forename + " " + this.surname;
  }

  public String toGson()
  {
    Gson gsonData = new Gson();
    return gsonData.toJson(this);
  }
  
  @Override
  public boolean equals(Object other)
  {
    if (!(other instanceof EmployeeProfile))
    {
      return false;
    }
    
    EmployeeProfile otherEmployee = (EmployeeProfile) other;

    return employeeID == otherEmployee.employeeID
        && surname.equals(otherEmployee.surname)
        && forename.equals(otherEmployee.forename)
        && username.equals(otherEmployee.username)
        && emailAddress.equals(otherEmployee.emailAddress)
        && isManager == otherEmployee.isManager
        && GUID.equals(otherEmployee.GUID)
        && company.equals(otherEmployee.company)
        && sopraDepartment.equals(otherEmployee.sopraDepartment)
        && steriaDepartment.equals(otherEmployee.steriaDepartment)
        && sector.equals(otherEmployee.sector)
        && superSector.equals(otherEmployee.superSector)
        && reporteeCNs.equals(otherEmployee.reporteeCNs);
  }

  @Override
  public String toString()
  {
    return "EmployeeProfile_NEW [employeeID=" + employeeID + ", surname=" + surname + ", forename=" + forename
        + ", username=" + username + ", emailAddress=" + emailAddress + ", isManager=" + isManager + ", GUID=" + GUID
        + ", company=" + company + ", sopraDepartment=" + sopraDepartment + ", steriaDepartment="
        + steriaDepartment + ", sector=" + sector + ", superSector=" + superSector + ", reporteeCNs=" + reporteeCNs
        + "]";
  }

  /**
   * This method adds a reportee's CN to the list of reportees
   * 
   * @param cn
   * @return true of false indicating whether the operation was successful or not
   * @throws InvalidAttributeValueException
   */
  public boolean addReportee(String cn) throws InvalidAttributeValueException
  {
    if (this.reporteeCNs == null) this.reporteeCNs = new ArrayList<>();
    if (cn != null && cn.length() > 1) return reporteeCNs.add(cn);
    else throw new InvalidAttributeValueException(INVALID_NULLREPORTEE);
  }
}
