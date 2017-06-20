package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import model.Models;

/**
 * Unit tests for the EmployeeTest class.
 *
 */
public class EmployeeTest {
	/** TYPE Property|Constant - Represents|Indicates... */
	private final int VALID_ID = 675590;

	/** TYPE Property|Constant - Represents|Indicates... */
	private final int INVALID_ID = -675590;

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_GUID = "guid";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_NAME = "b";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String INVALID_NAME = "astringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300characters";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_EMAIL_ADDRESS = "alexandre.brard@soprasteria.com";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String INVALID_EMAIL_ADDRESS = "alexandre.brard$soprasteria.com";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_USERNAME = "abrard";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String INVALID_USERNAME = "astringwithmorethan50charactersastringwithmorethan50characters";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_COMPANY = "sopra steria";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String INVALID_COMPANY = "astringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150characters";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_SECTOR_SUPERSECTOR = "sector";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String INVALID_SECTOR = "astringwithmorethan15characters";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String INVALID_SUPERSECTOR = "astringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150characters";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_DEPARTMENT = "department";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String INVALID_DEPARTMENT = "astringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150characters";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final boolean HAS_HRDASH = false;

	/** TYPE Property|Constant - Represents|Indicates... */

	private final boolean IS_MANAGER = false;
	/** TYPE Property|Constant - Represents|Indicates... */
	private Date date = new Date();

	/** TYPE Property|Constant - Represents|Indicates... */
	private final List<String> VALID_REPORTEE_LIST = Arrays.asList("a");

	@Mock
	/** TYPE Property|Constant - Represents|Indicates... */
	private EmployeeProfile profile;

	@Mock
	/** TYPE Property|Constant - Represents|Indicates... */
	private List<Feedback> mockFeedbackList;

	/** TYPE Property|Constant - Represents|Indicates... */
	@Mock
	private List<List<Objective>> mockObjectiveList;

	/** TYPE Property|Constant - Represents|Indicates... */
	@Mock
	private List<Note> mockNotesList;

	/** TYPE Property|Constant - Represents|Indicates... */
	@Mock
	List<List<DevelopmentNeed>> mockDevelopmentNeedsList;

	/** TYPE Property|Constant - Represents|Indicates... */
	@Mock
	private List<FeedbackRequest> mockFeedbackRequestsList;

	/** TYPE Property|Constant - Represents|Indicates... */
	@Mock
	private List<List<Competency>> mockCompetenciesList;

	@Mock
	private Feedback mockFeedback;

	@Mock
	private Competency mockCompetency;

	@Mock
	private FeedbackRequest mockFeedbackRequest;

	@Mock
	private DevelopmentNeed mockDevelopmentNeed;

	@Mock
	private Note mockNote;

	/** AppController Property - Represents the unit under test. */
	@InjectMocks
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
		profile = Models.getProfile();
		unitUnderTest = new Employee(profile);
		unitUnderTestEmpty = new Employee();
		MockitoAnnotations.initMocks(this);
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

	/**
	 * Unit test for the setFeedback method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetFeedback() {
		List<Feedback> feedbackList = Arrays.asList(Models.getFeedback());
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
		Feedback feedback=Models.getFeedback();
		List<Feedback> feedbackList = Arrays.asList(feedback);
		unitUnderTest.setFeedback(feedbackList);
		assertEquals(unitUnderTest.getFeedback(VALID_ID),feedback);
	}

	/*
	/**
	 * Unit test for the getLatestVersionObjectives method : null objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 *
	@Test
	public void testGetLatestVersionOfObjectivesNullCase() throws InvalidAttributeValueException,
			IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field f = unitUnderTest.getClass().getDeclaredField("objectives");
		f.setAccessible(true);
		f.set(unitUnderTest, null);
		assertEquals(unitUnderTest.getObjectives(), null);
	}
	*/

	/**
	 * Unit test for the getLatestVersionObjectives method : valid objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetObjectivesValidObjectives() throws InvalidAttributeValueException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		List<Objective> objList = Arrays.asList(Models.getObjective());
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.getObjectives(), objList);
	}

	/**
	 * Unit test for the getLatestVersionOfSpecificObjective method : valid
	 * objectives.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetSpecificObjectiveValidObjectives() throws InvalidAttributeValueException {
		Objective obj = Models.getObjective();
		List<Objective> objList = Arrays.asList(obj);
		unitUnderTest.setObjectives(objList);
		assertEquals(unitUnderTest.getObjective(VALID_ID), obj);
	}

	/**
	 * Unit test for the setNotes method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetNotes() throws InvalidAttributeValueException {
		List<Note> notesList = Arrays.asList(Models.getNote());
		unitUnderTest.setNotes(notesList);
		assertEquals(unitUnderTest.getNotes(), notesList);
	}
	/*
	  *//**
		 * Unit test for the getLatestVersionDevelopmentNeedsList method.
		 * 
		 * @throws InvalidAttributeValueException
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 * @throws SecurityException
		 * @throws NoSuchFieldException
		 */
	/*
	 * @Test public void testGetLatestVersionDevelopmentNeedsListNullCase()
	 * throws InvalidAttributeValueException, IllegalArgumentException,
	 * IllegalAccessException, NoSuchFieldException, SecurityException { Field f
	 * = unitUnderTest.getClass().getDeclaredField("developmentNeeds");
	 * f.setAccessible(true); f.set(unitUnderTest, null);
	 * assertEquals(unitUnderTest.getLatestVersionDevelopmentNeeds(), null); }
	 * 
	 *//**
		 * Unit test for the getLatestVersionDevelopmentNeedsList method. valid
		 * development need.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void
	 * testGetLatestVersionDevelopmentNeedsListValidDevelopmentNeeds() throws
	 * InvalidAttributeValueException, IllegalArgumentException,
	 * IllegalAccessException, NoSuchFieldException, SecurityException {
	 * DevelopmentNeed dev = new DevelopmentNeed(1, 1, 1, "1", "1");
	 * List<DevelopmentNeed> devList = Arrays.asList(dev);
	 * List<List<DevelopmentNeed>> devListList = Arrays.asList(devList);
	 * unitUnderTest.setDevelopmentNeedsList(devListList);
	 * assertEquals(unitUnderTest.getLatestVersionDevelopmentNeeds(), devList);
	 * }
	 * 
	 *//**
		 * Unit test for the getLatestVersionOfSpecificDevelopmentNeeds method :
		 * valid development need.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void
	 * testGetLatestVersionOfSpecificDevelopmentNeedsValidDevelopmentNeeds()
	 * throws InvalidAttributeValueException { DevelopmentNeed dev = new
	 * DevelopmentNeed(1, 1, 1, "1", "1"); List<DevelopmentNeed> devList =
	 * Arrays.asList(dev); List<List<DevelopmentNeed>> devListList =
	 * Arrays.asList(devList);
	 * unitUnderTest.setDevelopmentNeedsList(devListList);
	 * assertEquals(unitUnderTest.getLatestVersionOfSpecificDevelopmentNeed(1),
	 * dev); }
	 * 
	 *//**
		 * Unit test for the getLatestVersionOfSpecificDevelopmentNeed method :
		 * development need not found.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testGetLatestVersionOfSpecificDevelopmentNeedNotFound()
	 * throws InvalidAttributeValueException {
	 * assertEquals(unitUnderTest.getLatestVersionOfSpecificDevelopmentNeed(5649
	 * ), null); }
	 * 
	 *//**
		 * Unit test for the getFeedbackRequest method : valid ID.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testGetFeedbackRequestValidID() throws
	 * InvalidAttributeValueException { FeedbackRequest fbr = new
	 * FeedbackRequest("1", "1"); List<FeedbackRequest> fbrList =
	 * Arrays.asList(fbr); unitUnderTest.setFeedbackRequestsList(fbrList);
	 * assertEquals(unitUnderTest.getFeedbackRequest("1"), fbr); }
	 * 
	 *//**
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
	 *//**
		 * Unit test for the getLatestVersionCompetencies method. valid
		 * competency.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testGetLatestVersionCompetenciesValidCompetencies()
	 * throws InvalidAttributeValueException, IllegalArgumentException,
	 * IllegalAccessException, NoSuchFieldException, SecurityException {
	 * Competency comp = new Competency(1, true); List<Competency> compList =
	 * Arrays.asList(comp); List<List<Competency>> compListList =
	 * Arrays.asList(compList); unitUnderTest.setCompetenciesList(compListList);
	 * assertEquals(unitUnderTest.getLatestVersionCompetencies(), compList); }
	 * 
	 *//**
		 * Unit test for the getLatestVersionOfSpecificCompetency method :
		 * invalid ID.
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
	 *//**
		 * Unit test for the setLastLogon method.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testSetLastLogon() throws
	 * InvalidAttributeValueException { unitUnderTest.setLastLogon(date);
	 * assertEquals(unitUnderTest.getLastLogon(), date); }
	 * 
	 * 
	 *//**
		 * Unit test for the addFeedback method : valid feedback.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testAddFeedbackWithValidFeedback() throws
	 * InvalidAttributeValueException {
	 * assertEquals(unitUnderTest.addFeedback(mockFeedback), true); }
	 * 
	 *//**
		 * Unit test for the addObjective method : valid objective.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testAddObjectiveWithValidObjective() throws
	 * InvalidAttributeValueException { Objective obj = new Objective(1, 1, 1,
	 * "1", "1", "3010-01"); assertEquals(unitUnderTest.addObjective(obj),
	 * true); }
	 * 
	 *//**
		 * Unit test for the addObjective method : check if objectives is null.
		 * 
		 * @throws SecurityException
		 * @throws NoSuchFieldException
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 * 
		 */
	/*
	 * @Test public void testAddObjectiveCheckIfObjectivesIsNull() throws
	 * InvalidAttributeValueException, IllegalArgumentException,
	 * IllegalAccessException, NoSuchFieldException, SecurityException {
	 * Objective obj = new Objective(1, 1, 1, "1", "1", "3010-01");
	 * setPrivateField("objectives", unitUnderTest, null);
	 * assertTrue(unitUnderTest.addObjective(obj)); }
	 * 
	 *//**
		 * Unit test for the editObjective method : valid objective.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testEditObjectiveWithValidObjective() throws
	 * InvalidAttributeValueException {
	 * 
	 * Objective obj = new Objective(1, 1, 1, "1", "1", "3010-01");
	 * List<Objective> objList = Arrays.asList(obj); List<List<Objective>>
	 * objListList = Arrays.asList(objList);
	 * unitUnderTest.setObjectives(objListList);
	 * assertEquals(unitUnderTest.editObjective(obj), true); }
	 * 
	 *//**
		 * Unit test for the addNote method.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testAddNote() throws InvalidAttributeValueException {
	 * assertEquals(unitUnderTest.addNote(mockNote),
	 * unitUnderTest.getNotes().add(mockNote)); }
	 * 
	 *//**
		 * Unit test for the addObjective method : valid objective.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testAddDevelopmentNeedWithValidObjective() throws
	 * InvalidAttributeValueException { DevelopmentNeed dev = new
	 * DevelopmentNeed(1, 1, 1, "1", "1");
	 * assertEquals(unitUnderTest.addDevelopmentNeed(dev), true); }
	 * 
	 *//**
		 * Unit test for the addDevelopmentNeed method : check if
		 * developmentNeeds is null.
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
	 * DevelopmentNeed dev = new DevelopmentNeed(1, 1, 1, "1", "1");
	 * setPrivateField("developmentNeeds", unitUnderTest, null);
	 * assertTrue(unitUnderTest.addDevelopmentNeed(dev)); }
	 * 
	 *//**
		 * Unit test for the editDevelopmentNeed method : valid development
		 * need.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testEditDevelopmentNeedWithValidDevelopmentNeed()
	 * throws InvalidAttributeValueException {
	 * 
	 * DevelopmentNeed dev = new DevelopmentNeed(1, 1, 1, "1", "1");
	 * List<DevelopmentNeed> devList = Arrays.asList(dev);
	 * List<List<DevelopmentNeed>> devListList = Arrays.asList(devList);
	 * unitUnderTest.setDevelopmentNeedsList(devListList);
	 * assertEquals(unitUnderTest.editDevelopmentNeed(dev), true); }
	 * 
	 *//**
		 * Unit test for the addFeedbackRequest method : valid feedback request.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testAddFeedbackRequestWithValidFeedbackRequest() throws
	 * InvalidAttributeValueException {
	 * assertEquals(unitUnderTest.addFeedbackRequest(mockFeedbackRequest),
	 * unitUnderTest.getFeedbackRequestsList().add(mockFeedbackRequest)); }
	 * 
	 *//**
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
	 *//**
		 * Unit test for the nextFeedbackID method.
		 * 
		 * @throws InvalidAttributeValueException
		 */
	/*
	 * @Test public void testNextFeedbackID() throws
	 * InvalidAttributeValueException {
	 * assertEquals(unitUnderTest.nextFeedbackID(),
	 * unitUnderTest.getFeedback().size() + 1); }
	 * 
	 *//**
		 * Unit test for the setProfile method.
		 * 
		 * @throws InvalidAttributeValueException
		 *//*
		 * @Test public void testSetProfile() throws
		 * InvalidAttributeValueException { unitUnderTest.setProfile(profile);
		 * assertEquals(unitUnderTest.getProfile(), profile); }
		 */
}