package dataStructure;

import static dataStructure.Constants.INVALID_CONTEXT_COMPANY;
import static dataStructure.Constants.INVALID_INT;
import static dataStructure.Constants.INVALID_NULLREPORTEE;
import static dataStructure.Constants.INVALID_STRING;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import com.google.gson.Gson;

/**
 * This class contains the definition of the EmployeeProfile object
 */
public class EmployeeProfile implements Serializable
{
  private static final long serialVersionUID = 6335090383770271897L;

  /** long Property - Represents the employee id */
  private long employeeID;

  /** String Property - Represents the employee surname. */
  private String surname;

  /** String Property - Represents the employee forename. */
  private String forename;

  /** String Property - Represents the employee username. */
  private String username;

  /** String Property - Represents the employee emailAddress. */
  private String emailAddress;

  /** boolean Property - Represents if the employee is a manager. */
  private boolean isManager;

  /** boolean Property - Represents if the employee has access to the hr dashboard. */
  private boolean hasHRDash;

  /** String Property - Represents the GUID from the AD. */
  private String GUID;

  /** String Property - Represents the employee company. */
  private String company;

  /** String Property - Represents the employee sopraDepartment. */
  private String sopraDepartment;

  /** String Property - Represents the employee steriaDepartment. */
  private String steriaDepartment;

  /** String Property - Represents the employee sector. */
  private String sector;

  /** String Property - Represents the employee superSector. */
  private String superSector;

  /** List<String> Property - Represents the list of the employees reportees. */
  private List<String> reporteeCNs;

  /** Default Constructor - Responsible for initialising this object. */
  public EmployeeProfile()
  {
    surname = "";
    forename = "";
    employeeID = INVALID_INT; 
    username = "";
    emailAddress = "";
    isManager = false;
    hasHRDash = false;
    company = "";
    superSector = "";
    sector = "";
    steriaDepartment = "";
    sopraDepartment = "";
    GUID = "";
    reporteeCNs = new ArrayList<String>();
  }

  /**
   * Constructor - Responsible for initialising this object.
   *
   * @param employeeID
   * @throws InvalidAttributeValueException
   */
  public EmployeeProfile(long employeeID, String guid, String forename, String surname, String emailAddress,
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

  /**
   * Constructor - Responsible for initialising with a builder.
   *
   * @param employeeID
   * @throws InvalidAttributeValueException
   */
  public EmployeeProfile(Builder builder)
  {
    this.employeeID = builder.employeeID;
    this.surname = builder.surname;
    this.forename = builder.forename;
    this.username = builder.username;
    this.emailAddress = builder.emailAddress;
    this.isManager = builder.isManager;
    this.hasHRDash = builder.hasHRDash;
    this.GUID = builder.GUID;
    this.company = builder.company;
    this.sopraDepartment = builder.sopraDepartment;
    this.steriaDepartment = builder.steriaDepartment;
    this.sector = builder.sector;
    this.superSector = builder.superSector;
    this.reporteeCNs = builder.reporteeCNs;
  }

  /** @return the employee ID. */
  public long getEmployeeID()
  {
    return this.employeeID;
  }

  /**
   * @param id the ID to set.
   * @throws InvalidAttributeValueException
   */
  public void setEmployeeID(long id) throws InvalidAttributeValueException
  {
    if (id > 0) this.employeeID = id;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERID);
  } 

  /** @return the employee username. */
  public String getUsername()
  {
    return this.username;
  }

  /**
   * @param user the user to set
   * @throws InvalidAttributeValueException
   */
  public void setUsername(String user) throws InvalidAttributeValueException
  {
    if (user != null && user.length() > 0 && user.length() < 50) this.username = user;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERNAME);
  }

  /** @return the employee email address. */
  public String getEmailAddress()
  {
    return this.emailAddress;
  }

  /**
   * @param email The user email address
   * @throws InvalidAttributeValueException
   */
  public void setEmailAddress(String email) throws InvalidAttributeValueException
  {
    if (email != null && email.length() > 0 && email.contains("@")) this.emailAddress = email;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_GUID);
  }

  /** @return the employee surname. */
  public String getSurname()
  {
    return this.surname;
  }

  /**
   * @param name the surname to set
   * @throws InvalidAttributeValueException
   */
  public void setSurname(String name) throws InvalidAttributeValueException
  {
    if (name != null && !name.equals("") && name.length() < 300)
      this.surname = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_SURNAME);
  }

  /** @return the employee forename. */
  public String getForename()
  {
    return this.forename;
  }

  /**
   * @param name the forename to set
   * @throws InvalidAttributeValueException
   */
  public void setForename(String name) throws InvalidAttributeValueException
  {
    if (name != null && !name.equals("") && name.length() < 300) this.forename = name;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_FORENAME);
  }

  /** @return true if the employee is manager, false otherwise. */
  public boolean getIsManager()
  {
    return isManager;
  }

  /** @param value the value of whether the employee is a manager */
  public void setIsManager(boolean value)
  {
    this.isManager = value;
  }

  /** @return true if the employee has access to the hr dashboard, false otherwise. */
  public boolean getHasHRDash()
  {
    return hasHRDash;
  }

  /** @param value the value of whether the employee has access to the hr dashboard */
  public void setHasHRDash(boolean hasHRDash)
  {
    this.hasHRDash = hasHRDash;
  }

  /** @return the list of the employees reportees . */
  public List<String> getReporteeCNs()
  {
    return new ArrayList<String>(reporteeCNs);
  }

  /**
   * @param reportees the reportees to set
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

  /** @return the employee company. */
  public String getCompany()
  {
    return this.company;
  }

  /**
   * @param com the company name which length must be less than 150 characters
   * @throws InvalidAttributeValueException
   */
  public void setCompany(String com) throws InvalidAttributeValueException
  {
    if (com != null && com.length() > 0 && com.length() < 150) this.company = com;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_COMPANY);
  }

  /** @return the employee sopra department. */
  public String getSopraDepartment()
  {
    return sopraDepartment;
  }

  /**
   * @param sopraDepartment the sopra department to set
   * @throws InvalidAttributeValueException
   */
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

  /** @return the employee steria department. */
  public String getSteriaDepartment()
  {
    return steriaDepartment;
  }

  /**
   * @param steriaDepartment the steria department to set
   * @throws InvalidAttributeValueException
   */
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

  /** @return the employee sector. */
  public String getSector()
  {
    return sector;
  }

  /**
   * @param sector the sector to set
   * @throws InvalidAttributeValueException
   */
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

  /** @return the employee super sector. */
  public String getSuperSector()
  {
    return superSector;
  }

  /**
   * @param superSector the super sector to set
   * @throws InvalidAttributeValueException
   */
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

  /** @return the employee GUID. */
  public String getGUID()
  {
    return GUID;
  }

  /**
   * @param guid the guid to set. this will be a unique value for each employee.
   * @throws InvalidAttributeValueException
   */
  public void setGUID(String guid) throws InvalidAttributeValueException
  {
    if (guid != null && guid.length() > 0) this.GUID = guid;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_GUID);
  }

  /** @return the employees full name */
  public String getFullName()
  {
    return this.forename + " " + this.surname;
  }

  // public String toGson()
  // {
  // Gson gsonData = new Gson();
  // return gsonData.toJson(this);
  // }

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

  @Override
  public boolean equals(Object obj) 
  {
    // TODO update this to use Objects.equals
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    EmployeeProfile other = (EmployeeProfile) obj;
    if (GUID == null)
    {
      if (other.GUID != null) return false;
    }
    else if (!GUID.equals(other.GUID)) return false;
    if (company == null)
    {
      if (other.company != null) return false;
    }
    else if (!company.equals(other.company)) return false;
    if (emailAddress == null)
    {
      if (other.emailAddress != null) return false;
    }
    else if (!emailAddress.equals(other.emailAddress)) return false;
    if (employeeID != other.employeeID) return false;
    if (forename == null)
    {
      if (other.forename != null) return false;
    }
    else if (!forename.equals(other.forename)) return false;
    if (hasHRDash != other.hasHRDash) return false;
    if (isManager != other.isManager) return false;
    if (reporteeCNs == null)
    {
      if (other.reporteeCNs != null) return false;
    }
    else if (!reporteeCNs.equals(other.reporteeCNs)) return false;
    if (sector == null)
    {
      if (other.sector != null) return false;
    }
    else if (!sector.equals(other.sector)) return false;
    if (sopraDepartment == null)
    {
      if (other.sopraDepartment != null) return false;
    }
    else if (!sopraDepartment.equals(other.sopraDepartment)) return false;
    if (steriaDepartment == null)
    {
      if (other.steriaDepartment != null) return false;
    }
    else if (!steriaDepartment.equals(other.steriaDepartment)) return false;
    if (superSector == null)
    {
      if (other.superSector != null) return false;
    }
    else if (!superSector.equals(other.superSector)) return false;
    if (surname == null)
    {
      if (other.surname != null) return false;
    }
    else if (!surname.equals(other.surname)) return false;
    if (username == null)
    {
      if (other.username != null) return false;
    }
    else if (!username.equals(other.username)) return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "EmployeeProfile [employeeID=" + employeeID + ", surname=" + surname + ", forename=" + forename
        + ", username=" + username + ", emailAddress=" + emailAddress + ", isManager=" + isManager + ", GUID=" + GUID
        + ", company=" + company + ", sopraDepartment=" + sopraDepartment + ", steriaDepartment=" + steriaDepartment
        + ", sector=" + sector + ", superSector=" + superSector + ", reporteeCNs=" + reporteeCNs + "]";
  }

  public static class Builder
  {

    /** long Property - Represents the employee id */
    private long employeeID;

    /** String Property - Represents the employee surname. */
    private String surname;

    /** String Property - Represents the employee forename. */
    private String forename;

    /** String Property - Represents the employee username. */
    private String username;

    /** String Property - Represents the employee emailAddress. */
    private String emailAddress;

    /** boolean Property - Represents if the employee is a manager. */
    private boolean isManager;

    /** boolean Property - Represents if the employee has access to the hr dashboard. */
    private boolean hasHRDash;

    /** String Property - Represents the GUID from the AD. */
    private String GUID;

    /** String Property - Represents the employee company. */
    private String company;

    /** String Property - Represents the employee sopraDepartment. */
    private String sopraDepartment;

    /** String Property - Represents the employee steriaDepartment. */
    private String steriaDepartment;

    /** String Property - Represents the employee sector. */
    private String sector;

    /** String Property - Represents the employee superSector. */
    private String superSector;

    /** List<String> Property - Represents the list of the employees reportees. */
    private List<String> reporteeCNs;

    /** @param employeeID The value to set. */
    public Builder employeeID(long employeeID)
    {
      this.employeeID = employeeID;
      return this;
    }

    /** @param surname The value to set. */
    public Builder surname(String surname)
    {
      this.surname = surname;
      return this;
    }

    /** @param forename The value to set. */
    public Builder forename(String forename)
    {
      this.forename = forename;
      return this;
    }

    /** @param username The value to set. */
    public Builder username(String username)
    {
      this.username = username;
      return this;
    }

    /** @param emailAddress The value to set. */
    public Builder emailAddress(String emailAddress)
    {
      this.emailAddress = emailAddress;
      return this;
    }

    /** @param isManager The value to set. */
    public Builder manager(boolean isManager)
    {
      this.isManager = isManager;
      return this;
    }

    /** @param hasHRDash The value to set. */
    public Builder hasHRDash(boolean hasHRDash)
    {
      this.hasHRDash = hasHRDash;
      return this;
    }

    /** @param gUID The value to set. */
    public Builder GUID(String GUID)
    {
      this.GUID = GUID;
      return this;
    }

    /** @param company The value to set. */
    public Builder company(String company)
    {
      this.company = company;
      return this;
    }

    /** @param sopraDepartment The value to set. */
    public Builder sopraDepartment(String sopraDepartment)
    {
      this.sopraDepartment = sopraDepartment;
      return this;
    }

    /** @param steriaDepartment The value to set. */
    public Builder steriaDepartment(String steriaDepartment)
    {
      this.steriaDepartment = steriaDepartment;
      return this;
    }

    /** @param sector The value to set. */
    public Builder sector(String sector)
    {
      this.sector = sector;
      return this;
    }

    /** @param superSector The value to set. */
    public Builder superSector(String superSector)
    {
      this.superSector = superSector;
      return this;
    }

    /** @param reporteeCNs The value to set. */
    public Builder reporteeCNs(List<String> reporteeCNs)
    {
      this.reporteeCNs = reporteeCNs;
      return this;
    }

    /**
     * Builds an Employee Profile
     * @return the EmployeeProfile
     */
    public EmployeeProfile build()
    {
      return new EmployeeProfile(this);
    }

  }
}
