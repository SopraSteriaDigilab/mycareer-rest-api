package dataStructure;

import static dataStructure.Constants.INVALID_NULLREPORTEE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.management.InvalidAttributeValueException;

import services.mappers.InvalidEmployeeProfileException;

/**
 * This class contains the definition of the EmployeeProfile object
 */
// TODO remove validation on all setters.
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
  private String guid;

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

  /** Date Property - Represents the date the employees account expires. */
  private Date accountExpires;

  /** Default Constructor - Responsible for initialising this object. */
  public EmployeeProfile()
  {
    reporteeCNs = new ArrayList<String>();
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
    this.guid = builder.guid;
    this.company = builder.company;
    this.sopraDepartment = builder.sopraDepartment;
    this.steriaDepartment = builder.steriaDepartment;
    this.sector = builder.sector;
    this.superSector = builder.superSector;
    this.reporteeCNs = builder.reporteeCNs;
    this.accountExpires = builder.accountExpires;
  }

  /** @return the employeeID */
  public long getEmployeeID()
  {
    return employeeID;
  }

  /** @param employeeID The value to set. */
  public void setEmployeeID(long employeeID)
  {
    this.employeeID = employeeID;
  }

  /** @return the surname */
  public String getSurname()
  {
    return surname;
  }

  /** @param surname The value to set. */
  public void setSurname(String surname)
  {
    this.surname = surname;
  }

  /** @return the forename */
  public String getForename()
  {
    return forename;
  }

  /** @param forename The value to set. */
  public void setForename(String forename)
  {
    this.forename = forename;
  }

  /** @return the username */
  public String getUsername()
  {
    return username;
  }

  /** @param username The value to set. */
  public void setUsername(String username)
  {
    this.username = username;
  }

  /** @return the emailAddress */
  public String getEmailAddress()
  {
    return emailAddress;
  }

  /** @param emailAddress The value to set. */
  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  }

  /** @return the isManager */
  public boolean getIsManager()
  {
    return isManager;
  }

  /** @param isManager The value to set. */
  public void setIsManager(boolean isManager)
  {
    this.isManager = isManager;
  }

  /** @return the hasHRDash */
  public boolean getHasHRDash()
  {
    return hasHRDash;
  }

  /** @param hasHRDash The value to set. */
  public void setHasHRDash(boolean hasHRDash)
  {
    this.hasHRDash = hasHRDash;
  }

  /** @return the guid */
  public String getGuid()
  {
    return guid;
  }

  /** @param guid The value to set. */
  public void setGuid(String guid)
  {
    this.guid = guid;
  }

  /** @return the company */
  public String getCompany()
  {
    return company;
  }

  /** @param company The value to set. */
  public void setCompany(String company)
  {
    this.company = company;
  }

  /** @return the sopraDepartment */
  public String getSopraDepartment()
  {
    return sopraDepartment;
  }

  /** @param sopraDepartment The value to set. */
  public void setSopraDepartment(String sopraDepartment)
  {
    this.sopraDepartment = sopraDepartment;
  }

  /** @return the steriaDepartment */
  public String getSteriaDepartment()
  {
    return steriaDepartment;
  }

  /** @param steriaDepartment The value to set. */
  public void setSteriaDepartment(String steriaDepartment)
  {
    this.steriaDepartment = steriaDepartment;
  }

  /** @return the sector */
  public String getSector()
  {
    return sector;
  }

  /** @param sector The value to set. */
  public void setSector(String sector)
  {
    this.sector = sector;
  }

  /** @return the superSector */
  public String getSuperSector()
  {
    return superSector;
  }

  /** @param superSector The value to set. */
  public void setSuperSector(String superSector)
  {
    this.superSector = superSector;
  }

  /** @return the reporteeCNs */
  public List<String> getReporteeCNs()
  {
    return reporteeCNs;
  }

  /** @param reporteeCNs The value to set. */
  public void setReporteeCNs(List<String> reporteeCNs)
  {
    this.reporteeCNs = reporteeCNs;
  }

  /** @return the accountExpires */
  public Date getAccountExpires()
  {
    return accountExpires;
  }

  /** @param accountExpires The value to set. */
  public void setAccountExpires(Date accountExpires)
  {
    this.accountExpires = accountExpires;
  }

  /** @return the employees full name */
  public String getFullName()
  {
    return this.forename + " " + this.surname;
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
    if (cn != null && cn.length() > 1) return this.reporteeCNs.add(cn);
    else throw new InvalidAttributeValueException(INVALID_NULLREPORTEE);
  }

  /**
   * Override of equals method.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   *
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof EmployeeProfile)) return false;
    EmployeeProfile employeeProfile = (EmployeeProfile) o;

    return employeeID == employeeProfile.employeeID && isManager == employeeProfile.isManager
        && hasHRDash == employeeProfile.hasHRDash && Objects.equals(surname, employeeProfile.surname)
        && Objects.equals(forename, employeeProfile.forename) && Objects.equals(username, employeeProfile.username)
        && Objects.equals(emailAddress, employeeProfile.emailAddress) && Objects.equals(guid, employeeProfile.guid)
        && Objects.equals(company, employeeProfile.company)
        && Objects.equals(sopraDepartment, employeeProfile.sopraDepartment)
        && Objects.equals(steriaDepartment, employeeProfile.steriaDepartment)
        && Objects.equals(sector, employeeProfile.sector) && Objects.equals(superSector, employeeProfile.superSector)
        && Objects.deepEquals(reporteeCNs, employeeProfile.reporteeCNs)
        && Objects.equals(accountExpires, employeeProfile.accountExpires);
  }

  /**
   * Override of hashCode method.
   *
   * @see java.lang.Object#hashCode()
   *
   * @return int hashCode value
   */
  @Override
  public int hashCode()
  {
    return Objects.hash(employeeID, surname, forename, username, emailAddress, isManager, hasHRDash, guid, company,
        sopraDepartment, steriaDepartment, sector, superSector, reporteeCNs, accountExpires);
  }

  @Override
  public String toString()
  {
    return "EmployeeProfile [employeeID=" + employeeID + ", surname=" + surname + ", forename=" + forename
        + ", username=" + username + ", emailAddress=" + emailAddress + ", isManager=" + isManager + ", GUID=" + guid
        + ", company=" + company + ", sopraDepartment=" + sopraDepartment + ", steriaDepartment=" + steriaDepartment
        + ", sector=" + sector + ", superSector=" + superSector + ", reporteeCNs=" + reporteeCNs + ", accountExpires="
        + accountExpires + "]";
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
    private String guid;

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

    /** Date Property - Represents the date the employees account expires. */
    private Date accountExpires;

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
    public Builder guid(String guid)
    {
      this.guid = guid;
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

    /** @param accountExpires The date to set. */
    public Builder accountExpires(Date accountExpires)
    {
      this.accountExpires = accountExpires;
      return this;
    }

    /**
     * Builds an Employee Profile
     * 
     * @return the EmployeeProfile
     */
    public EmployeeProfile build()
    {
      if (!validate()) throw new InvalidEmployeeProfileException("Invalid ID");

      return new EmployeeProfile(this);
    }

    /**
     * Validates the builder properties
     * 
     * @return true if valid, false otherwise.
     */
    private boolean validate()
    {
      if (this.reporteeCNs == null)
      {
        this.reporteeCNs = new ArrayList<>();
      }
      return (this.employeeID > 0);
    }

  }
}
