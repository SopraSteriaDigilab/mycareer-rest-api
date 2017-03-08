package model;

import java.util.Arrays;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import dataStructure.ADProfile_Advanced_OLD;
import dataStructure.Competency;
import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.EmployeeProfile;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note_OLD;
import dataStructure.Objective;

/**
 * Models to be used in the tests.
 *
 */
public class Models
{

  /** Long Constant - Represents a valid employee ID. */
  public static final long EMPLOYEE_ID = 675590;

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String GUID = "1111-1111-1111-1111-1111-1111-1111-1111";

  /** Employee Property - Represents an Employee object... */
  public static final String FIRST_NAME = "First";

  /** Employee Property - Represents an Employee object... */
  public static final String LAST_NAME = "Last";
  
  /** Employee Property - Represents an Employee object... */
  public static final String FULL_NAME = "First Last";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String EMAIL_ADRESS = "a@b.c";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String USERNAME = "Username";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String COMPANY = "Company";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String SUPER_SECTOR = "Super Sector";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String SECTOR = "Sector";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String STERIA_DEPARTMENT = "Steria Department";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String SOPRA_DEPARTMENT = "Sopra Department";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String TEAM = "Team";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final boolean IS_MANAGER = true;

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final boolean IS_HR = true;

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final List<String> REPORTEES = Arrays.asList(FIRST_NAME, FIRST_NAME, FIRST_NAME);

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final int ID = 1;

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String TITLE = "Title";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String DESCRIPTION = "Description";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String DATE = "2018-12";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  public static final boolean STATUS = true;

  /**
   * Get an employee profile object.
   *
   * @return
   * @throws InvalidAttributeValueException
   */
  public static EmployeeProfile getProfile() throws InvalidAttributeValueException
  {
    return new EmployeeProfile(EMPLOYEE_ID, GUID, FIRST_NAME, LAST_NAME, EMAIL_ADRESS, USERNAME, COMPANY,
        SUPER_SECTOR, SECTOR, STERIA_DEPARTMENT, SOPRA_DEPARTMENT, IS_MANAGER, IS_HR, REPORTEES);
  }

  /**
   * Get an employee object populated by the {@linkplain #getProfile() getProfile()} method.
   *
   * @return
   * @throws InvalidAttributeValueException
   */
  public static Employee getEmployee() throws InvalidAttributeValueException
  {
    return new Employee(getProfile());
  }

  
  
  /**
   * Get a competency
   *
   * @return
   */
  public static Competency getCompetency(){
    return new Competency(ID, STATUS);
  }
  
  /**
   * Get a development need
   *
   * @return
   * @throws InvalidAttributeValueException
   */
  public static DevelopmentNeed getDevelopmentNeed() throws InvalidAttributeValueException
  {
    return new DevelopmentNeed(ID, ID, ID, TITLE, DESCRIPTION, DATE);
  }
  
  /**
   * Get a feedback
   *
   * @return
   */
  public static Feedback getFeedback() {
    return new Feedback(ID, EMAIL_ADRESS, FULL_NAME, DESCRIPTION);
  }
  
  /**
   * Get a feedback request
   *
   * @return
   */
  public static FeedbackRequest getFeedbackRequest() {
    return new FeedbackRequest(GUID, EMAIL_ADRESS);
  } 
  
  
  /**
   * Get a note
   *
   * @return
   * @throws InvalidAttributeValueException
   */
  public static Note_OLD getNote() throws InvalidAttributeValueException {
    return new Note_OLD(ID, ID, ID, DESCRIPTION, FULL_NAME);
  }
  
  /**
   * Get an objective
   *
   * @return
   * @throws InvalidAttributeValueException
   */
  public static Objective getObjective() throws InvalidAttributeValueException
  {
    return new Objective(ID, ID, ID, TITLE, DESCRIPTION, DATE);
  }
  
}
