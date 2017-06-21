package model;

import static utils.Utils.generateFeedbackRequestID;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import dataStructure.Activity;
import dataStructure.Competency;
import dataStructure.Competency.CompetencyTitle;
import dataStructure.DevelopmentNeed;
import dataStructure.DevelopmentNeed.Category;
import dataStructure.EmailAddresses;
import dataStructure.EmployeeProfile;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import dataStructure.Rating;

/**
 * Models to be used in the tests.
 *
 */
public class TestModels
{
  /** Long Constant - Represents a valid employee ID. */
  public static final long EMPLOYEE_ID = 666666;

  /** Employee Property - Represents an Employee object... */
  public static final String FIRST_NAME = "First";

  /** Employee Property - Represents an Employee object... */
  public static final String LAST_NAME = "Last";

  /** Employee Property - Represents an Employee object... */
  public static final String FULL_NAME = "First Last";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String EMAIL_ADDRESS = "a@b.c";

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
  public static final boolean IS_MANAGER = true;

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final boolean IS_HR = true;

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final List<String> REPORTEES = Arrays.asList(FIRST_NAME, FIRST_NAME, FIRST_NAME);

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final int DB_OBJECT_ID = 1;

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String TITLE = "Title";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final String DESCRIPTION = "Description";

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final Date DATE_FIXED = new GregorianCalendar(2099, Calendar.JUNE, 20).getTime();

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final LocalDate LOCALDATE_FIXED = LocalDate.of(2099, 07, 20);

  /** TYPE Property|Constant - Represents|Indicates... */
  public static final boolean STATUS = true;

  public static final String EMP_TYPE = "EMP";

  /**
   * Get an employee profile object.
   *
   * @return
   * @throws InvalidAttributeValueException
   */
  public static EmployeeProfile newEmployeeProfile()
  {

    return new EmployeeProfile.Builder().employeeID(EMPLOYEE_ID).employeeType(EMP_TYPE).forename(FIRST_NAME)
        .surname(LAST_NAME).emailAddresses(newEmailAddresses()).username(USERNAME).company(COMPANY)
        .superSector(SUPER_SECTOR).sector(SECTOR).steriaDepartment(STERIA_DEPARTMENT).manager(IS_MANAGER)
        .hasHRDash(IS_MANAGER).accountExpires(DATE_FIXED).reporteeCNs(REPORTEES).build();
  }

  public static EmailAddresses newEmailAddresses()
  {
    EmailAddresses emailAddresses = new EmailAddresses.Builder().mail(EMAIL_ADDRESS).targetAddress(EMAIL_ADDRESS)
        .build();
    return emailAddresses;
  }

  /**
   * Get a feedback
   *
   * @return
   */
  public static Feedback newFeedback()
  {
    return new Feedback(DB_OBJECT_ID, EMAIL_ADDRESS, FULL_NAME, DESCRIPTION);
  }

  /**
   * Get a feedback request
   *
   * @return
   */
  public static FeedbackRequest newFeedbackRequest()
  {
    return new FeedbackRequest(generateFeedbackRequestID(EMPLOYEE_ID), EMAIL_ADDRESS);
  }

  /**
   * Get an Objective
   *
   * @return
   */
  public static Objective newObjective()
  {
    return new Objective(DB_OBJECT_ID, TITLE, DESCRIPTION, LOCALDATE_FIXED);
  }

  /**
   * Get a Note
   *
   * @return
   */
  public static Note newNote()
  {
    return new Note(USERNAME, DESCRIPTION);
  }

  /**
   * Get a DevelopmentNeed
   *
   * @return
   */
  public static DevelopmentNeed newDevelopmentNeed()
  {
    return new DevelopmentNeed(DB_OBJECT_ID, TITLE, DESCRIPTION, LOCALDATE_FIXED, Category.CLASSROOM_TRAINING);
  }
  
  /**
   * Get a Competency
   *
   * @return
   */
  public static Competency newCompetency()
  {
    return new Competency(DB_OBJECT_ID, CompetencyTitle.ACCOUNTABILITY);
  }
  
  /**
   * Get a Rating
   *
   * @return
   */
  public static Rating newRating()
  {
    return new Rating(2017);
  }
  
  /**
   * Get an Activity
   *
   * @return
   */
  public static Activity newActivity()
  {
    return new Activity(DESCRIPTION, DATE_FIXED);
  }

}
