package dataStructure;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import dataStructure.DevelopmentNeed.Category;
import model.Models;

public class DevelopmentNeedTest {
	
	private DevelopmentNeed unitUnderTest;

	/**
	 * Setup method that runs once before each test method.
	 * 
	 * @throws InvalidAttributeValueException
	 * 
	 */
	private int id = 1;
	private String title = "title";
	private String description = "description";
	private LocalDate dueDate = LocalDate.now().plusMonths(5);
	private Category category = Category.CLASSROOM_TRAINING;

	@Before
	public void setup() throws InvalidAttributeValueException {
		unitUnderTest = new DevelopmentNeed(id, title, description, dueDate, category);
	}

	@Test
	public void createActivitySuccessTest() {
		Action activityType = Action.ADD;
		EmployeeProfile profile = Models.getProfile();

		Activity actualActivity = unitUnderTest.createActivity(activityType, profile);

		final String activityString = new StringBuilder(profile.getFullName()).append(" ")
				.append(activityType.getVerb()).append(" ").append("development need").append(" #")
				.append(unitUnderTest.getId()).append(": ").append(unitUnderTest.getTitle()).toString();

		Activity expectedActivity = new Activity(activityString, unitUnderTest.getLastModified());

		assertEquals(expectedActivity, actualActivity);
	}
	
	@Test
	public void differencesExistTest(){
		DevelopmentNeed differentDevelopmentNeed=new DevelopmentNeed(id, title, description, dueDate, Category.JOB_TRAINING);
		
		Document expectedDocument=new Document().append("category", Category.JOB_TRAINING.getCategoryStr());
		
		Document actualDocument=unitUnderTest.differences(differentDevelopmentNeed);
		
		assertEquals(expectedDocument, actualDocument);
	}
	
	@Test
	public void noDifferencesTest(){
		DevelopmentNeed differentDevelopmentNeed=new DevelopmentNeed(id, title, description, dueDate, Category.CLASSROOM_TRAINING);
		
		Document expectedDocument=new Document();
		
		Document actualDocument=unitUnderTest.differences(differentDevelopmentNeed);
		
		assertEquals(expectedDocument, actualDocument);
	}
	
	@Test(expected=ClassCastException.class)
	public void differencesClassCastExceptionTest(){
		
		Objective differentDevelopmentNeed=new Objective();
			
		Document actualDocument=unitUnderTest.differences(differentDevelopmentNeed);
			
	}
		

}