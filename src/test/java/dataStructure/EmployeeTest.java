package dataStructure;

import static org.junit.Assert.assertEquals;
import static utils.Utils.generateFeedbackRequestID;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import dataStructure.Objective.Progress;
import model.TestModels;

/**
 * Unit tests for the EmployeeTest class.
 *
 */
public class EmployeeTest {
	/** TYPE Property|Constant - Represents|Indicates... */
	private final int VALID_DB_OBJECT_ID = 1;

	/** TYPE Property|Constant - Represents|Indicates... */
	private final int INVALID_ID = -675590;

	/** TYPE Property|Constant - Represents|Indicates... */
	private final int EMPLOYEE_ID = 666666;

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_EMAIL_ADDRESS = "alexandre.brard@soprasteria.com";

	/** TYPE Property|Constant - Represents|Indicates... */
	private Date date = new Date();

	/** AppController Property - Represents the unit under test. */

	private Employee unitUnderTest, unitUnderTestEmpty;

	/**
	 * Setup method that runs once before each test method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws InvalidAttributeValueException {

		Set<String> emailAddresses = new HashSet<>();
		emailAddresses.add(VALID_EMAIL_ADDRESS);
		EmployeeProfile profile = TestModels.newEmployeeProfile();
		unitUnderTest = new Employee(profile);
		unitUnderTestEmpty = new Employee();
	}

	/**
	 * @Test public void testMatchAndUpdate() throws
	 *       InvalidAttributeValueException { boolean updated =
	 *       unitUnderTest.matchAndUpdated(profile);
	 * 
	 *       TODO User overridden equals method for employee...
	 *       assertFalse(updated); assertEquals(profile.getReporteeCNs(),
	 *       unitUnderTest.getReporteeCNs()); assertEquals(profile.getCompany(),
	 *       unitUnderTest.getCompany());
	 *       assertEquals(profile.getEmailAddress(),
	 *       unitUnderTest.getEmailAddress());
	 *       assertEquals(profile.getEmployeeID(),
	 *       unitUnderTest.getEmployeeID()); assertEquals(profile.getForename(),
	 *       unitUnderTest.getForename()); assertEquals(profile.getFullName(),
	 *       unitUnderTest.getFullName()); assertEquals(profile.getIsManager(),
	 *       unitUnderTest.getIsManager()); assertEquals(profile.getSurname(),
	 *       unitUnderTest.getSurname()); assertEquals(profile.getUsername(),
	 *       unitUnderTest.getUsername());
	 * 
	 *       unitUnderTest.setReporteeCNs(VALID_REPORTEE_LIST_2); updated =
	 *       unitUnderTest.matchAndUpdated(adProfileAdvanced);
	 * 
	 *       assertTrue(updated); assertEquals(profile.getReporteeCNs(),
	 *       unitUnderTest.getReporteeCNs()); assertEquals(profile.getCompany(),
	 *       unitUnderTest.getCompany());
	 *       assertEquals(profile.getEmailAddress(),
	 *       unitUnderTest.getEmailAddress());
	 *       assertEquals(profile.getEmployeeID(),
	 *       unitUnderTest.getEmployeeID()); assertEquals(profile.getForename(),
	 *       unitUnderTest.getForename()); assertEquals(profile.getFullName(),
	 *       unitUnderTest.getFullName()); assertEquals(profile.getIsManager(),
	 *       unitUnderTest.getIsManager()); assertEquals(profile.getSurname(),
	 *       unitUnderTest.getSurname()); assertEquals(profile.getUsername(),
	 *       unitUnderTest.getUsername()); }
	 */

	/**
	 * For unit tests.
	 * 
	 */
	public void setPrivateField(String field, Object obj, Object newValue)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field f = obj.getClass().getDeclaredField(field);
		f.setAccessible(true);
		f.set(obj, newValue);
	}

	/**
	 * For unit tests.
	 * 
	 */
	public void setEveryPrivateFieldsToNullOrDefault(Object obj)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		setPrivateField("feedback", obj, null);
		setPrivateField("objectives", obj, null);
		setPrivateField("notes", obj, null);
		setPrivateField("developmentNeeds", obj, null);
		setPrivateField("feedbackRequests", obj, null);
		setPrivateField("competencies", obj, null);
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////// OBJECTIVES TEST METHODS FOLLOW //////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the getCurrentObjectives method : valid objectives.
	 * 
	 */
	@Test
	public void testGetCurrentObjectives() {
		List<Objective> objList = Arrays.asList(TestModels.newObjective());
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.getCurrentObjectives(), objList);
	}

	/**
	 * Unit test for the addObjective method : valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testAddObjectiveWithValidObjective() throws InvalidAttributeValueException {
		Objective obj = TestModels.newObjective();
		assertEquals(unitUnderTest.addObjective(obj), true);
	}

	/**
	 * Unit test for the editObjective method : valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testEditObjectiveWithValidObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.editObjective(obj), true);
	}

	/**
	 * Unit test for the editObjective method : invalid objective. Objective
	 * archived.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testEditObjectiveWithArchivedObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		obj.isArchived(true);
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		unitUnderTest.editObjective(obj);
	}

	/**
	 * Unit test for the editObjective method : invalid objective. Objective
	 * complete
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testEditObjectiveWithCompleteObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		obj.setProgress(Progress.COMPLETE);
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		unitUnderTest.editObjective(obj);
	}

	/**
	 * Unit test for the deleteObjective method : valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testDeleteObjectiveWithValidObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		obj.isArchived(true);
		List<Objective> objList = new ArrayList<Objective>(Arrays.asList(obj));
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.deleteObjective(VALID_DB_OBJECT_ID), true);
	}

	/**
	 * Unit test for the deleteObjective method : invalid objective.
	 * 
	 * Objective not archived.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testDeleteObjectiveWithInvalidObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		List<Objective> objList = new ArrayList<Objective>(Arrays.asList(obj));
		unitUnderTest.setObjectives(objList);
		unitUnderTest.deleteObjective(VALID_DB_OBJECT_ID);
	}

	/**
	 * Unit test for the updateObjectiveProgress method : invalid objective.
	 * 
	 * Progress is the same as already set.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testUpdateObjectiveProgressWithValidObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.updateObjectiveProgress(VALID_DB_OBJECT_ID, Progress.COMPLETE), true);
	}

	/**
	 * Unit test for the updateObjective method : valid objective.
	 * 
	 * Invalid Progress > Same as already set.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateObjectiveProgressWithInvalidProgress() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		unitUnderTest.updateObjectiveProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the updateObjective method : valid objective.
	 * 
	 * Objective complete.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateObjectiveProgressWithCompleteObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		obj.setProgress(Progress.COMPLETE);
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		unitUnderTest.updateObjectiveProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the updateObjective method : valid objective.
	 * 
	 * Objective archived.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateObjectiveProgressWithArchivedObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		obj.isArchived(true);
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		unitUnderTest.updateObjectiveProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the updateObjective method : valid objective.
	 * 
	 * Objective archived and completed.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateObjectiveProgressWithArchivedAndComplete() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		obj.isArchived(true);
		obj.setProgress(Progress.COMPLETE);
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		unitUnderTest.updateObjectiveProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the toggleObjectiveArchive method : Valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testToggleObjectiveArchiveFalseWithValidObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.toggleObjectiveArchive(VALID_DB_OBJECT_ID), true);
	}

	/**
	 * Unit test for the toggleObjectiveArchive method : Valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testToggleObjectiveArchiveTrueWithValidObjective() throws InvalidAttributeValueException {

		Objective obj = TestModels.newObjective();
		obj.isArchived(true);
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.toggleObjectiveArchive(VALID_DB_OBJECT_ID), true);
	}

	/**
	 * Unit test for the getSpecificObjective method : valid objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetSpecificObjectiveValidObjective() throws InvalidAttributeValueException {
		Objective obj = TestModels.newObjective();
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.getObjective(VALID_DB_OBJECT_ID), obj);
	}

	/**
	 * Unit test for the getSpecificObjective method : Invalid objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testGetSpecificObjectiveInvalidObjective() throws InvalidAttributeValueException {
		Objective obj = TestModels.newObjective();
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.getObjective(INVALID_ID), obj);
	}

	/**
	 * Unit test for the getObjectives method : valid objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetObjectivesValidObjectives() throws InvalidAttributeValueException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		List<Objective> objList = Arrays.asList(TestModels.newObjective());
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.getObjectives(), objList);
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////// DEVELOPMENT NEEDS TEST METHODS FOLLOW ///////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the getCurrentDevelopmentNeeds method : valid objectives.
	 * 
	 */
	@Test
	public void testGetCurrentDevelopmentNeeds() {
		List<DevelopmentNeed> devNeedList = Arrays.asList(TestModels.newDevelopmentNeed());
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.getCurrentDevelopmentNeeds(), devNeedList);
	}

	/**
	 * Unit test for the addDevelopmentNeed method : valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testAddDevelopmentNeedWithValidDevelopmentNeed() throws InvalidAttributeValueException {
		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		assertEquals(unitUnderTest.addDevelopmentNeed(devNeed), true);
	}

	/**
	 * Unit test for the addDevelopmentNeed method : check if objectives is
	 * null.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * 
	 */
	/*
	 * @Test public void testAddDevelopmentNeedCheckIfDevelopmentNeedsIsNull()
	 * throws InvalidAttributeValueException, IllegalArgumentException,
	 * IllegalAccessException, NoSuchFieldException, SecurityException {
	 * DevelopmentNeed devNeed = new DevelopmentNeed(1, 1, 1, "1", "1",
	 * "3010-01"); setPrivateField("objectives", unitUnderTest, null);
	 * assertTrue(unitUnderTest.addDevelopmentNeed(devNeed)); }
	 * 
	 */

	/**
	 * Unit test for the editDevelopmentNeed method : valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testEditDevelopmentNeedWithValidDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.editDevelopmentNeed(devNeed), true);
	}

	/**
	 * Unit test for the editDevelopmentNeed method : invalid objective.
	 * DevelopmentNeed archived.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testEditDevelopmentNeedWithArchivedDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		devNeed.isArchived(true);
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		unitUnderTest.editDevelopmentNeed(devNeed);
	}

	/**
	 * Unit test for the editDevelopmentNeed method : invalid objective.
	 * DevelopmentNeed complete
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testEditDevelopmentNeedWithCompleteDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		devNeed.setProgress(Progress.COMPLETE);
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		unitUnderTest.editDevelopmentNeed(devNeed);
	}

	/**
	 * Unit test for the deleteDevelopmentNeed method : valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testDeleteDevelopmentNeedWithValidDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		devNeed.isArchived(true);
		List<DevelopmentNeed> devNeedList = new ArrayList<DevelopmentNeed>(Arrays.asList(devNeed));
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.deleteDevelopmentNeed(VALID_DB_OBJECT_ID), true);
	}

	/**
	 * Unit test for the deleteDevelopmentNeed method : invalid objective.
	 * 
	 * DevelopmentNeed not archived.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testDeleteDevelopmentNeedWithInvalidDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		List<DevelopmentNeed> devNeedList = new ArrayList<DevelopmentNeed>(Arrays.asList(devNeed));
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		unitUnderTest.deleteDevelopmentNeed(VALID_DB_OBJECT_ID);
	}

	/**
	 * Unit test for the updateDevelopmentNeedProgress method : invalid
	 * objective.
	 * 
	 * Progress is the same as already set.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testUpdateDevelopmentNeedProgressWithValidDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.updateDevelopmentNeedProgress(VALID_DB_OBJECT_ID, Progress.COMPLETE), true);
	}

	/**
	 * Unit test for the updateDevelopmentNeed method : valid objective.
	 * 
	 * Invalid Progress > Same as already set.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateDevelopmentNeedProgressWithInvalidProgress() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		unitUnderTest.updateDevelopmentNeedProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the updateDevelopmentNeed method : valid objective.
	 * 
	 * DevelopmentNeed complete.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateDevelopmentNeedProgressWithCompleteDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		devNeed.setProgress(Progress.COMPLETE);
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		unitUnderTest.updateDevelopmentNeedProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the updateDevelopmentNeed method : valid objective.
	 * 
	 * DevelopmentNeed archived.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateDevelopmentNeedProgressWithArchivedDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		devNeed.isArchived(true);
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		unitUnderTest.updateDevelopmentNeedProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the updateDevelopmentNeed method : valid objective.
	 * 
	 * DevelopmentNeed archived and completed.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testUpdateDevelopmentNeedProgressWithArchivedAndComplete() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		devNeed.isArchived(true);
		devNeed.setProgress(Progress.COMPLETE);
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		unitUnderTest.updateDevelopmentNeedProgress(VALID_DB_OBJECT_ID, Progress.PROPOSED);
	}

	/**
	 * Unit test for the toggleDevelopmentNeedArchive method : Valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testToggleDevelopmentNeedArchiveFalseWithValidDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.toggleDevelopmentNeedArchive(VALID_DB_OBJECT_ID), true);
	}

	/**
	 * Unit test for the toggleDevelopmentNeedArchive method : Valid objective.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testToggleDevelopmentNeedArchiveTrueWithValidDevelopmentNeed() throws InvalidAttributeValueException {

		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		devNeed.isArchived(true);
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.toggleDevelopmentNeedArchive(VALID_DB_OBJECT_ID), true);
	}

	/**
	 * Unit test for the getSpecificDevelopmentNeed method : valid objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetSpecificDevelopmentNeedValidDevelopmentNeed() throws InvalidAttributeValueException {
		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.getDevelopmentNeed(VALID_DB_OBJECT_ID), devNeed);
	}

	/**
	 * Unit test for the getSpecificDevelopmentNeed method : Invalid objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testGetSpecificDevelopmentNeedInvalidDevelopmentNeed() throws InvalidAttributeValueException {
		DevelopmentNeed devNeed = TestModels.newDevelopmentNeed();
		List<DevelopmentNeed> devNeedList = Arrays.asList(devNeed);
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.getDevelopmentNeed(INVALID_ID), devNeed);
	}

	/**
	 * Unit test for the getDevelopmentNeeds method : valid objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetDevelopmentNeedsValidDevelopmentNeeds() throws InvalidAttributeValueException,
			IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		List<DevelopmentNeed> devNeedList = Arrays.asList(TestModels.newDevelopmentNeed());
		unitUnderTest.setDevelopmentNeeds(devNeedList);
		assertEquals(unitUnderTest.getDevelopmentNeeds(), devNeedList);
	}

	///////////////////////////////////////////////////////////////////////////
	//////////////////// COMPETENCIES METHODS FOLLOW //////////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the getLatestVersionCompetencies method.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	/*
	 * @Test public void testGetLatestVersionCompetenciesEmptyCompetencies()
	 * throws InvalidAttributeValueException, IllegalArgumentException,
	 * IllegalAccessException, NoSuchFieldException, SecurityException {
	 * List<List<Competency>> compListList = new ArrayList<List<Competency>>();
	 * 
	 * Field f = unitUnderTest.getClass().getDeclaredField("competencies");
	 * f.setAccessible(true); f.set(unitUnderTest, compListList);
	 * assertEquals(unitUnderTest.getLatestVersionCompetencies(),
	 * compListList.get(0)); }
	 * 
	 */
	/*
	 * /** Unit test for the getLatestVersionCompetencies method. valid
	 * competency.
	 * 
	 * @throws InvalidAttributeValueException
	 * 
	 * 
	 * @Test public void testGetLatestVersionCompetenciesValidCompetencies()
	 * throws InvalidAttributeValueException, IllegalArgumentException,
	 * IllegalAccessException, NoSuchFieldException, SecurityException {
	 * Competency comp = TestModels.newCompetency(); List<Competency> compList =
	 * Arrays.asList(comp); unitUnderTest.com
	 * assertEquals(unitUnderTest.getLatestVersionCompetencies(), compList); }
	 * 
	 */

	/**
	 * Unit test for the getLatestVersionOfSpecificCompetency method : invalid
	 * ID.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	/*
	 * @Test public void testGetLatestVersionOfSpecificCompetencyInvalidID()
	 * throws InvalidAttributeValueException {
	 * assertEquals(unitUnderTest.getLatestVersionOfSpecificCompetency(
	 * INVALID_ID), null); }
	 * 
	 *//**
		 * Unit test for the getLatestVersionOfSpecificCompetency method : valid
		 * competency.
		 * 
		 * @throws InvalidAttributeValueException
		 */

	/*
	 * @Test public void
	 * testGetLatestVersionOfSpecificCompetencyValidCompetency() throws
	 * InvalidAttributeValueException { Competency comp = new Competency(1,
	 * true); List<Competency> compList = Arrays.asList(comp);
	 * List<List<Competency>> compListList = Arrays.asList(compList);
	 * unitUnderTest.setCompetenciesList(compListList);
	 * assertEquals(unitUnderTest.getLatestVersionOfSpecificCompetency(1),
	 * comp); }
	 * 
	 */

	/**
	 * Unit test for the updateCompetency method with valid competency.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	/*
	 * @Test public void testUpdateCompetencyWithValidCompetency() throws
	 * InvalidAttributeValueException { Competency comp = new Competency(1,
	 * true); List<Competency> compList = Arrays.asList(comp);
	 * List<List<Competency>> compListList = Arrays.asList(compList);
	 * unitUnderTest.setCompetenciesList(compListList);
	 * assertEquals(unitUnderTest.updateCompetency(comp, "1"), true); }
	 * 
	 */

	///////////////////////////////////////////////////////////////////////////
	//////////////////////// NOTES METHODS FOLLOW /////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the geCurrentNotes method.
	 * 
	 */
	@Test
	public void testGetCurrentNotes() {

		List<Note> notesList = Arrays.asList(TestModels.newNote());
		unitUnderTest.setNotes(notesList);
		assertEquals(unitUnderTest.getCurrentNotes(), notesList);
	}

	/**
	 * Unit test for the addNote method.
	 * 
	 */
	@Test
	public void testAddNote() {
		List<Note> notesList = new ArrayList<Note>(Arrays.asList(TestModels.newNote()));
		unitUnderTest.setNotes(notesList);
		assertEquals(unitUnderTest.addNote(TestModels.newNote()), true);
	}

	/**
	 * Unit test for the getNote method : Valid note.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetNoteValidNote() throws InvalidAttributeValueException {
		Note note = TestModels.newNote();
		note.setId(1);
		List<Note> notesList = Arrays.asList(note);
		unitUnderTest.setNotes(notesList);
		assertEquals(unitUnderTest.getNote(1), note);
	}

	/**
	 * Unit test for the getNote method : Invalid note.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testGetNoteInvalidNote() throws InvalidAttributeValueException {
		Note note = TestModels.newNote();
		note.setId(1);
		List<Note> notesList = Arrays.asList(note);
		unitUnderTest.setNotes(notesList);
		unitUnderTest.getNote(0);
	}

	/**
	 * Unit test for the setNotes method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetNotes() throws InvalidAttributeValueException {
		List<Note> notesList = Arrays.asList(TestModels.newNote());
		unitUnderTest.setNotes(notesList);
		assertEquals(unitUnderTest.getNotes(), notesList);
	}

	///////////////////////////////////////////////////////////////////////////
	////////////////////// FEEDBACK METHODS FOLLOW ////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the addFeedback method : valid feedback.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void getCurrentFeedback() {
		List<Feedback> feedbackList = Arrays.asList(TestModels.newFeedback());
		unitUnderTest.setFeedback(feedbackList);
		assertEquals(unitUnderTest.getCurrentFeedback(), feedbackList);
	}

	/**
	 * Unit test for the addFeedback method : valid feedback.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testAddFeedbackWithValidFeedback() throws InvalidAttributeValueException {
		Feedback feedback = TestModels.newFeedback();
		assertEquals(unitUnderTest.addFeedback(feedback), true);
	}

	@Test
	public void testNextFeedbackID() {
		List<Feedback> feedbackList = Arrays.asList(TestModels.newFeedback());
		unitUnderTest.setFeedback(feedbackList);
		assertEquals(2, unitUnderTest.nextFeedbackID());
	}

	/**
	 * Unit test for the getFeedback method.
	 * 
	 */
	@Test
	public void testGetFeedback() {
		List<Feedback> feedbackList = Arrays.asList(TestModels.newFeedback());
		unitUnderTest.setFeedback(feedbackList);
		assertEquals(unitUnderTest.getFeedback(), feedbackList);
	}

	/**
	 * Unit test for the setSpecificFeedback method : valid ID.
	 *
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetSpecificFeedbackWithValidID() throws InvalidAttributeValueException {
		Feedback feedback = TestModels.newFeedback();
		List<Feedback> feedbackList = Arrays.asList(feedback);
		unitUnderTest.setFeedback(feedbackList);
		assertEquals(unitUnderTest.getFeedback(VALID_DB_OBJECT_ID), feedback);
	}

	/**
	 * Unit test for the setSpecificFeedback method : invalid ID.
	 *
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testGetSpecificFeedbackInvalidID() throws InvalidAttributeValueException {
		Feedback feedback = TestModels.newFeedback();
		List<Feedback> feedbackList = Arrays.asList(feedback);
		unitUnderTest.setFeedback(feedbackList);
		assertEquals(unitUnderTest.getFeedback(INVALID_ID), feedback);
	}

	///////////////////////////////////////////////////////////////////////////
	////////////////// FEEDBACK REQUESTS METHODS FOLLOW ///////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the addFeedbackRequest method : valid feedbackRequest.
	 * 
	 * @throws InvalidAttributeValueException
	 * 
	 */

	@Test
	public void getCurrentFeedbackRequest() throws InvalidAttributeValueException {
		List<FeedbackRequest> feedbackRequestList = Arrays.asList(TestModels.newFeedbackRequest());
		unitUnderTest.setFeedbackRequests(feedbackRequestList);
		assertEquals(unitUnderTest.getCurrentFeedbackRequests(), feedbackRequestList);
	}

	/**
	 * Unit test for the addFeedbackRequest method : valid feedback request.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testAddFeedbackRequestWithValidFeedbackRequest() throws InvalidAttributeValueException {
		assertEquals(unitUnderTest.addFeedbackRequest(TestModels.newFeedbackRequest()), true);
	}

	/**
	 * Unit test for the addFeedbackRequest method : Invalid feedback request.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testAddFeedbackRequestWithInvalidFeedbackRequest() throws InvalidAttributeValueException {
		unitUnderTest.addFeedbackRequest(null);
	}

	/**
	 * Unit test for the dismissFeedbackRequest method : valid feedbackRequest.
	 * 
	 * @throws InvalidAttributeValueException
	 * 
	 */

	@Test
	public void testDismissFeedbackRequest() throws InvalidAttributeValueException {
		List<FeedbackRequest> feedbackRequestList = Arrays.asList(TestModels.newFeedbackRequest());
		unitUnderTest.setFeedbackRequests(feedbackRequestList);
		assertEquals(unitUnderTest.dismissFeedbackRequest(generateFeedbackRequestID(EMPLOYEE_ID)), true);
	}

	/**
	 * Unit test for the dismissFeedbackRequest method : valid feedbackRequest.
	 * 
	 * @throws InvalidAttributeValueException
	 * 
	 */

	@Test(expected = InvalidAttributeValueException.class)
	public void testDismissFeedbackRequestInvalid() throws InvalidAttributeValueException {
		List<FeedbackRequest> feedbackRequestList = Arrays.asList(TestModels.newFeedbackRequest());
		unitUnderTest.setFeedbackRequests(feedbackRequestList);
		assertEquals(unitUnderTest.dismissFeedbackRequest(generateFeedbackRequestID(INVALID_ID)), true);
	}

	///////////////////////////////////////////////////////////////////////////
	/////////////////////// RATINGS METHODS FOLLOW ////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the addManagerEvaluation method. Valid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testAddManagerEvaluation() throws InvalidAttributeValueException {

		unitUnderTest.addManagerEvaluation(2017, "evaluation", 5);
		assertEquals(5, unitUnderTest.getRating(2017).getScore());
	}

	/**
	 * Unit test for the addManagerEvaluation method. Invalid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testAddManagerEvaluationInvalidManagerEvaluation() throws InvalidAttributeValueException {
		Rating rating = TestModels.newRating();
		rating.setManagerEvaluationSubmitted(true);
		List<Rating> ratingsList = new ArrayList<Rating>(Arrays.asList(rating));
		unitUnderTest.setRatings(ratingsList);

		unitUnderTest.addManagerEvaluation(2017, "evaluation", 5);
	}

	/**
	 * Unit test for the addSelfEvaluation method. Valid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testAddSelfEvaluation() throws InvalidAttributeValueException {

		unitUnderTest.addSelfEvaluation(2017, "self evaluation");
		assertEquals("self evaluation", unitUnderTest.getRating(2017).getSelfEvaluation());
	}

	/**
	 * Unit test for the addSelfEvaluation method. Invalid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testAddSelfEvaluationInvalidManagerEvaluation() throws InvalidAttributeValueException {
		Rating rating = TestModels.newRating();
		rating.setManagerEvaluationSubmitted(true);
		List<Rating> ratingsList = new ArrayList<Rating>(Arrays.asList(rating));
		unitUnderTest.setRatings(ratingsList);

		unitUnderTest.addSelfEvaluation(2017, "self evaluation");
	}

	/**
	 * Unit test for the addSelfEvaluation method. Invalid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testAddSelfEvaluationInvalidSelfEvaluation() throws InvalidAttributeValueException {
		Rating rating = TestModels.newRating();
		rating.setSelfEvaluationSubmitted(true);
		List<Rating> ratingsList = new ArrayList<Rating>(Arrays.asList(rating));
		unitUnderTest.setRatings(ratingsList);

		unitUnderTest.addSelfEvaluation(2017, "self evaluation");
	}

	/**
	 * Unit test for the submitSelfEvaluation method. Valid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSubmitSelfEvaluation() throws InvalidAttributeValueException {

		unitUnderTest.submitSelfEvaluation(2017);
		assertEquals(true, unitUnderTest.getRating(2017).isSelfEvaluationSubmitted());
	}

	/**
	 * Unit test for the submitSelfEvaluation method. Invalid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testSubmitSelfEvaluationInvalidManagerEvaluation() throws InvalidAttributeValueException {
		Rating rating = TestModels.newRating();
		rating.setManagerEvaluationSubmitted(true);
		List<Rating> ratingsList = new ArrayList<Rating>(Arrays.asList(rating));
		unitUnderTest.setRatings(ratingsList);

		unitUnderTest.submitSelfEvaluation(2017);
	}

	/**
	 * Unit test for the submitManagerEvaluation method. Valid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSubmitManagerEvaluationInvalid() throws InvalidAttributeValueException {
		unitUnderTest.addManagerEvaluation(2017, "evaluation", 5);
		unitUnderTest.submitManagerEvaluation(2017);
		assertEquals(true, unitUnderTest.getRating(2017).isManagerEvaluationSubmitted());
	}

	/**
	 * Unit test for the submitManagerEvaluation method. Invalid rating
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test(expected = InvalidAttributeValueException.class)
	public void testSubmitManagerEvaluation() throws InvalidAttributeValueException {
		unitUnderTest.addManagerEvaluation(2017, "evaluation", 0);
		unitUnderTest.submitManagerEvaluation(2017);

	}

	///////////////////////////////////////////////////////////////////////////
	////////////////////// LAST LOGON METHODS FOLLOW //////////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for the setLastLogon method.
	 * 
	 * @throws InvalidAttributeValueException
	 */

	@Test
	public void testSetLastLogon() throws InvalidAttributeValueException {
		unitUnderTest.setLastLogon(date);
		assertEquals(unitUnderTest.getLastLogon(), date);
	}

	///////////////////////////////////////////////////////////////////////////
	//////////////////// ACTIVITY FEED METHODS FOLLOW /////////////////////////
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Unit test for addActivity method, filling feed.
	 */
	@Test
	public void testAddActivityFullFeed() {
		for (int i = 0; i < 20; i++) {
			unitUnderTest.addActivity(TestModels.newActivity());
		}
		assertEquals(true, unitUnderTest.addActivity(TestModels.newActivity()));
	}
	
	/**
	 * Unit test for getActivityFeedAsDocument method
	 */
	@Test
	public void testGetActivityFeedAsDocument() {
		Activity activity=TestModels.newActivity();
		unitUnderTest.addActivity(activity);
		
		List<Document> activityFeedList=new ArrayList<>();
		activityFeedList.add(activity.toDocument());

		Document expectedDocument=new Document("activityFeed", activityFeedList);
		
		Document actualDocument=unitUnderTest.getActivityFeedAsDocument();
		
		assertEquals(expectedDocument, actualDocument);
	}

	
	
	/**
	 * Unit test for the setProfile method.
	 * 
	 * @throws InvalidAttributeValueException
	 *//*
		 * @Test public void testSetProfile() throws
		 * InvalidAttributeValueException { unitUnderTest.setProfile(profile);
		 * assertEquals(unitUnderTest.getProfile(), profile); }
		 */
}