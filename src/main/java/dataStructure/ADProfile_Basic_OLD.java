package dataStructure;

import static dataStructure.Constants.*;

import java.io.Serializable;
import javax.management.InvalidAttributeValueException;
import com.google.gson.Gson;

/**
 * This class contains the definition of the ADProfile_Basic object
 *
 */
public class ADProfile_Basic_OLD implements Serializable
{

  // Global Constants
  private static final long serialVersionUID = 6335090383770271897L;

  // Global Variables
  private long employeeID;
  private String surname, forename, username;
  private String emailAddress;
  private boolean isManager;
  private boolean hasHRDash;

  // Empty Constructor
  public ADProfile_Basic_OLD()
  {
    this.surname = INVALID_STRING;
    this.forename = INVALID_STRING;
    this.employeeID = INVALID_INT;
    this.username = INVALID_STRING;
    this.emailAddress = INVALID_STRING;
    this.isManager = false;
    this.hasHRDash = false;
  }

  // Constructor with parameters
  public ADProfile_Basic_OLD(long employeeID, String surname, String forename, boolean manager, String username,
      String emailAddress, boolean hasHRDash) throws InvalidAttributeValueException
  {
    this.setSurname(surname);
    this.setForename(forename);
    this.setEmployeeID(employeeID);
    this.setUsername(username);
    this.setEmailAddress(emailAddress);
    this.isManager = manager;
    this.hasHRDash = hasHRDash;
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

  public String getEmailAddress()
  {
    return this.emailAddress;
  }

  public void setEmployeeID(long id) throws InvalidAttributeValueException
  {
    if (id > 0) this.employeeID = id;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_USERID);
  }

  public long getEmployeeID()
  {
    return this.employeeID;
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

  public String getUsername()
  {
    return this.username;
  }

  public void setSurname(String name) throws InvalidAttributeValueException
  {
    if (name != null && !name.equals("") && name.length() < 300)
      this.surname = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_SURNAME);
  }

  public String getSurname()
  {
    return this.surname;
  }

  public void setForename(String name) throws InvalidAttributeValueException
  {
    if (name != null && !name.equals("") && name.length() < 300) this.forename = name;
    else throw new InvalidAttributeValueException(Constants.INVALID_CONTEXT_FORENAME);
  }

  public String getForename()
  {
    return this.forename;
  }

  public String getFullName()
  {
    return this.forename + " " + this.surname;
  }

  public void setIsManager(boolean value)
  {
    this.isManager = value;
  }

  public boolean getIsManager()
  {
    return isManager;
  }

  public void setHasHRDash(boolean hasHRDash)
  {
    this.hasHRDash = hasHRDash;
  }

  public boolean getHasHRDash()
  {
    return hasHRDash;
  }

  public String toGson()
  {
    Gson gsonData = new Gson();
    return gsonData.toJson(this);
  }

  @Override
  public String toString()
  {
    String s = "";
    s += "FullName: " + this.getFullName() + "\n";
    s += "EmployeeID: " + this.getEmployeeID() + "\n";
    s += "IsManager: " + this.getIsManager() + "\n";
    s += "Username: " + this.getUsername() + "\n";
    s += "IsManager: " + this.getIsManager() + "\n";
    s += "HasHRDash: " + hasHRDash + "\n";

    return s;
  }

}
