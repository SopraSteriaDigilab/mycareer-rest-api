package model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import dataStructure.DevelopmentNeed;
import dataStructure.DevelopmentNeed.Category;
import dataStructure.EmailAddresses;
import dataStructure.EmployeeProfile;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;

/**
 * Models to be used in the tests.
 *
 */
public class Models {

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
	public static final String TEAM = "Team";

	/** TYPE Property|Constant - Represents|Indicates... */
	public static final boolean IS_MANAGER = true;

	/** TYPE Property|Constant - Represents|Indicates... */
	public static final boolean IS_HR = true;

	/** TYPE Property|Constant - Represents|Indicates... */
	public static final List<String> REPORTEES = Arrays.asList(FIRST_NAME, FIRST_NAME, FIRST_NAME);

	/** TYPE Property|Constant - Represents|Indicates... */
	public static final int ID = 675590;

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
	public static EmployeeProfile getProfile() {

		return new EmployeeProfile.Builder().employeeID(EMPLOYEE_ID).employeeType(EMP_TYPE).forename(FIRST_NAME).surname(LAST_NAME)
				.emailAddresses(getEmailAddresses()).username(USERNAME).company(COMPANY).superSector(SUPER_SECTOR)
				.sector(SECTOR).steriaDepartment(STERIA_DEPARTMENT).manager(IS_MANAGER).hasHRDash(IS_MANAGER).accountExpires(DATE_FIXED).reporteeCNs(REPORTEES).build();
	}

	public static EmailAddresses getEmailAddresses() {
		EmailAddresses emailAddresses=new EmailAddresses.Builder().mail(EMAIL_ADDRESS).targetAddress(EMAIL_ADDRESS).build();
		return emailAddresses;
	}

/*	*//**
	 * Get an employee object populated by the {@linkplain #getProfile()
	 * getProfile()} method.
	 *
	 * @return
	 * @throws InvalidAttributeValueException
	 *//*
	public static Employee getEmployee() throws InvalidAttributeValueException {
		return new Employee(getProfile());
	}*/

	/**
	 * Get a feedback
	 *
	 * @return
	 */
	public static Feedback getFeedback() {
		return new Feedback(ID, EMAIL_ADDRESS, FULL_NAME, DESCRIPTION);
	}

	/**
	 * Get a feedback request
	 *
	 * @return
	 */
	public static FeedbackRequest getFeedbackRequest() {
		return new FeedbackRequest(GUID, EMAIL_ADDRESS);
	}
	
	/**
	 * Get an Objective
	 *
	 * @return
	 */
	public static Objective getObjective(){
		return new Objective(ID, TITLE, DESCRIPTION, LOCALDATE_FIXED);
	}
	
	/**
	 * Get a Note
	 *
	 * @return
	 */
	public static Note getNote(){
		return new Note(USERNAME, DESCRIPTION);
	}
	
	/**
	 * Get a DevelopmentNeed
	 *
	 * @return
	 */
	public static DevelopmentNeed getDevelopmentNeed(){
		return new DevelopmentNeed(ID, TITLE, DESCRIPTION, LOCALDATE_FIXED, Category.CLASSROOM_TRAINING);
	}
	
}
