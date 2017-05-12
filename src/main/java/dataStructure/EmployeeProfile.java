package dataStructure;

import static utils.Utils.*;
import static dataStructure.EmailAddresses.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;

import services.mappers.InvalidEmployeeProfileException;
import utils.Utils;

/**
 * This class contains the definition of the EmployeeProfile object
 */
// TODO remove validation on all setters.
public class EmployeeProfile implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static final String EMPLOYEE_ID = "profile.employeeID";
  public static final String SURNAME = "profile.surname";
  public static final String FORENAME = "profile.forename";
  public static final String USERNAME = "profile.username";
  public static final String EMAIL_ADDRESSES = "profile.emailAddresses";
  public static final String IS_MANAGER = "profile.isManager";
  public static final String HAS_HR_DASH = "profile.hasHRDash";
  public static final String COMPANY = "profile.company";
  public static final String DEPARTMENT = "profile.steriaDepartment";
  public static final String SECTOR = "profile.sector";
  public static final String SUPER_SECTOR = "profile.superSector";
  public static final String REPORTEE_CNS = "profile.reporteeCNs";
  public static final String ACCOUNT_EXPIRES = "profile.accountExpires";

  /** long Property - Represents the employee id */
  private long employeeID;

  /** String property - Represents the employee type */
  private String employeeType;

  /** String Property - Represents the employee surname. */
  private String surname;

  /** String Property - Represents the employee forename. */
  private String forename;

  /** String Property - Represents the employee username. */
  private String username;

  /** String Property - Represents the employee emailAddress. */
  private EmailAddresses emailAddresses;

  /** boolean Property - Represents if the employee is a manager. */
  private boolean isManager;

  /** boolean Property - Represents if the employee has access to the hr dashboard. */
  private boolean hasHRDash;

  /** String Property - Represents the employee company. */
  private String company;

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
    emailAddresses = new EmailAddresses.Builder().build();
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
    this.employeeType = builder.employeeType;
    this.surname = builder.surname;
    this.forename = builder.forename;
    this.username = builder.username;
    this.emailAddresses = builder.emailAddresses;
    this.isManager = builder.isManager;
    this.hasHRDash = builder.hasHRDash;
    this.company = builder.company;
    this.steriaDepartment = builder.steriaDepartment;
    this.sector = builder.sector;
    this.superSector = builder.superSector;
    this.reporteeCNs = builder.reporteeCNs;
    this.accountExpires = builder.accountExpires;
  }

  public EmployeeProfile(EmployeeProfile employeeProfile)
  {
    this.employeeID = employeeProfile.employeeID;
    this.surname = employeeProfile.surname;
    this.forename = employeeProfile.forename;
    this.username = employeeProfile.username;
    this.emailAddresses = employeeProfile.emailAddresses;
    this.isManager = employeeProfile.isManager;
    this.hasHRDash = employeeProfile.hasHRDash;
    this.company = employeeProfile.company;
    this.steriaDepartment = employeeProfile.steriaDepartment;
    this.sector = employeeProfile.sector;
    this.superSector = employeeProfile.superSector;
    this.reporteeCNs = new ArrayList<String>(employeeProfile.reporteeCNs);
    this.accountExpires = employeeProfile.accountExpires;
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

  /** @return the employeeType */
  public String getEmployeeType()
  {
    return employeeType;
  }

  /** @param employeeType The value to set. */
  public void setEmployeeType(String employeeType)
  {
    this.employeeType = employeeType;
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
  public EmailAddresses getEmailAddresses()
  {
    return emailAddresses;
  }

  /** @param emailAddress The value to set. */
  public void setEmailAddresses(EmailAddresses emailAddresses)
  {
    this.emailAddresses = emailAddresses;
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
   * @return A {@code Document} representation of this employee profile.
   */
  public Document toDocument()
  {
    return new Document(EMPLOYEE_ID, employeeID).append(SURNAME, surname).append(FORENAME, forename)
        .append(USERNAME, username).append(MAIL, emailAddresses.getMail())
        .append(TARGET_ADDRESS, emailAddresses.getTargetAddress()).append(USER_ADDRESS, emailAddresses.getUserAddress())
        .append(IS_MANAGER, isManager).append(HAS_HR_DASH, hasHRDash).append(COMPANY, company)
        .append(DEPARTMENT, steriaDepartment).append(SECTOR, sector).append(SUPER_SECTOR, superSector)
        .append(REPORTEE_CNS, reporteeCNs).append(ACCOUNT_EXPIRES, accountExpires);
  }

  /**
   * Returns a document with only the key-value pairs which have a different value in this than that contained in other.
   * 
   * Fields which exist in this but do not exist in other are not included in the return value.
   * 
   * @param other
   * @return a document containing only the key values pairs which represent differences between this and other.
   */
  public Document differences(final EmployeeProfile other)
  {
    Document differences = toDocument();
    Document otherDocument = other.toDocument();

    removeNullValues(differences);
    removeNullValues(otherDocument);
    otherDocument.forEach((ok, ov) -> differences.merge(ok, ov, Utils::nullIfSame));

    return differences;
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
    if (o == this)
    {
      return true;
    }

    if (!(o instanceof EmployeeProfile))
    {
      return false;
    }

    EmployeeProfile employeeProfile = (EmployeeProfile) o;

    return employeeID == employeeProfile.employeeID && Objects.equals(employeeType, employeeProfile.employeeType)
        && Objects.equals(surname, employeeProfile.surname) && Objects.equals(forename, employeeProfile.forename)
        && Objects.equals(username, employeeProfile.username)
        && Objects.equals(emailAddresses, employeeProfile.emailAddresses) && isManager == employeeProfile.isManager
        && Objects.equals(hasHRDash, employeeProfile.hasHRDash) && Objects.equals(company, employeeProfile.company)
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
    return Objects.hash(employeeID, employeeType, surname, forename, username, emailAddresses, isManager, hasHRDash,
        company, steriaDepartment, sector, superSector, reporteeCNs, accountExpires);
  }

  @Override
  public String toString()
  {
    return "EmployeeProfile [employeeID=" + employeeID + ", employeeType=" + employeeType + ", surname=" + surname
        + ", forename=" + forename + ", username=" + username + ", emailAddresses=" + emailAddresses + ", isManager="
        + isManager + ", hasHRDash=" + hasHRDash + ", company=" + company + ", steriaDepartment=" + steriaDepartment
        + ", sector=" + sector + ", superSector=" + superSector + ", reporteeCNs=" + reporteeCNs + ", accountExpires="
        + accountExpires + "]";
  }

  public static class Builder
  {

    /** long Property - Represents the employee id */
    private long employeeID;

    /** String Property - Represents the employee type. */
    private String employeeType;

    /** String Property - Represents the employee surname. */
    private String surname;

    /** String Property - Represents the employee forename. */
    private String forename;

    /** String Property - Represents the employee username. */
    private String username;

    /** String Property - Represents the employee emailAddress. */
    private EmailAddresses emailAddresses;

    /** boolean Property - Represents if the employee is a manager. */
    private boolean isManager;

    /** boolean Property - Represents if the employee has access to the hr dashboard. */
    private boolean hasHRDash;

    /** String Property - Represents the employee company. */
    private String company;

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

    /** @param employeeType The value to set. */
    public Builder employeeType(String employeeType)
    {
      this.employeeType = employeeType;
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

    /** @param emailAddresses The value to set. */
    public Builder emailAddresses(EmailAddresses emailAddresses)
    {
      this.emailAddresses = emailAddresses;
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

    /** @param company The value to set. */
    public Builder company(String company)
    {
      this.company = company;
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

      if (emailAddresses == null)
      {
        this.emailAddresses = new EmailAddresses.Builder().build();
      }

      return (this.employeeID > 0);
    }

  }
}
