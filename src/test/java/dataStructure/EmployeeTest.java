
//  TODO Commented code to be reviewed
  
//package dataStructure;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Matchers.any;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.lang.reflect.Field;
//import org.bson.types.ObjectId;
//
//import javax.management.InvalidAttributeValueException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import com.google.gson.Gson;
//
///**
// * Unit tests for the EmployeeTest class.
// *
// */
//public class EmployeeTest
//{
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int VALID_ID = 675590;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final int INVALID_ID = -675590;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_GUID = "guid";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_NAME = "b";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_NAME = "astringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300charactersastringwithmorethan300characters";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_EMAIL_ADDRESS = "alexandre.brard@soprasteria.com";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_EMAIL_ADDRESS = "alexandre.brard$soprasteria.com";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_USERNAME = "abrard";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_USERNAME = "astringwithmorethan50charactersastringwithmorethan50characters";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_COMPANY = "sopra steria";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_COMPANY = "astringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150characters";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_SECTOR_SUPERSECTOR = "sector";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_SECTOR = "astringwithmorethan15characters";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_SUPERSECTOR = "astringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150characters";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String VALID_DEPARTMENT = "department";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final String INVALID_DEPARTMENT = "astringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150characters";
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final boolean HAS_HRDASH = false;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//
//  private final boolean IS_MANAGER = false;
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private Date date = new Date();
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private final List<String> VALID_REPORTEE_LIST = Arrays.asList("a");
//
//  @Mock
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private EmployeeProfile profile;
//
//  @Mock
//  /** TYPE Property|Constant - Represents|Indicates... */
//  private List<Feedback> mockFeedbackList;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  @Mock
//  private List<List<Objective_OLD>> mockObjectiveList;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  @Mock
//  private List<Note> mockNotesList;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  @Mock
//  List<List<DevelopmentNeed_OLD>> mockDevelopmentNeedsList;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  @Mock
//  private List<FeedbackRequest> mockFeedbackRequestsList;
//
//  /** TYPE Property|Constant - Represents|Indicates... */
//  @Mock
//  private List<List<Competency_OLD>> mockCompetenciesList;
//
//  @Mock
//  private Feedback mockFeedback;
//
//  @Mock
//  private Competency_OLD mockCompetency;
//
//  @Mock
//  private FeedbackRequest mockFeedbackRequest;
//
//  @Mock
//  private Objective_OLD mockObjective;
//
//  @Mock
//  private DevelopmentNeed_OLD mockDevelopmentNeed;
//
//  @Mock
//  private Note mockNote;
//
//  /** AppController Property - Represents the unit under test. */
//  @InjectMocks
//  private Employee unitUnderTest, unitUnderTestEmpty;
//
//  /**
//   * Setup method that runs once before each test method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @SuppressWarnings("unchecked")
//  @Before
//  public void setup() throws InvalidAttributeValueException
//  {
//    // LOG.debug("AppControllerTest.setup()", true);
//    Set<String> emailAddresses = new HashSet<>();
//    emailAddresses.add(VALID_EMAIL_ADDRESS);
//    profile = new EmployeeProfile.Builder().employeeID(VALID_ID).forename(VALID_NAME).surname(VALID_NAME)
//        .emailAddresses(emailAddresses).username(VALID_USERNAME).company(VALID_COMPANY)
//        .superSector(VALID_SECTOR_SUPERSECTOR).sector(VALID_SECTOR_SUPERSECTOR).steriaDepartment(VALID_DEPARTMENT)
//        .manager(IS_MANAGER).hasHRDash(HAS_HRDASH).reporteeCNs(VALID_REPORTEE_LIST).build();
//    unitUnderTest = new Employee(profile);
//    unitUnderTestEmpty = new Employee();
//    MockitoAnnotations.initMocks(this);
//  }
//
//  /**
//   * @Test public void testMatchAndUpdate() throws InvalidAttributeValueException { boolean updated =
//   *       unitUnderTest.matchAndUpdated(profile);
//   * 
//   *       // TODO User overridden equals method for employee... assertFalse(updated);
//   *       assertEquals(profile.getReporteeCNs(), unitUnderTest.getReporteeCNs()); assertEquals(profile.getCompany(),
//   *       unitUnderTest.getCompany()); assertEquals(profile.getEmailAddress(), unitUnderTest.getEmailAddress());
//   *       assertEquals(profile.getEmployeeID(), unitUnderTest.getEmployeeID()); assertEquals(profile.getForename(),
//   *       unitUnderTest.getForename()); assertEquals(profile.getFullName(), unitUnderTest.getFullName());
//   *       assertEquals(profile.getIsManager(), unitUnderTest.getIsManager()); assertEquals(profile.getSurname(),
//   *       unitUnderTest.getSurname()); assertEquals(profile.getUsername(), unitUnderTest.getUsername());
//   * 
//   *       unitUnderTest.setReporteeCNs(VALID_REPORTEE_LIST_2); updated =
//   *       unitUnderTest.matchAndUpdated(adProfileAdvanced);
//   * 
//   *       assertTrue(updated); assertEquals(profile.getReporteeCNs(), unitUnderTest.getReporteeCNs());
//   *       assertEquals(profile.getCompany(), unitUnderTest.getCompany()); assertEquals(profile.getEmailAddress(),
//   *       unitUnderTest.getEmailAddress()); assertEquals(profile.getEmployeeID(), unitUnderTest.getEmployeeID());
//   *       assertEquals(profile.getForename(), unitUnderTest.getForename()); assertEquals(profile.getFullName(),
//   *       unitUnderTest.getFullName()); assertEquals(profile.getIsManager(), unitUnderTest.getIsManager());
//   *       assertEquals(profile.getSurname(), unitUnderTest.getSurname()); assertEquals(profile.getUsername(),
//   *       unitUnderTest.getUsername()); }
//   */
//
//  /**
//   * For unit tests.
//   * 
//   */
//  public void setPrivateField(String field, Object obj, Object newValue)
//      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    Field f = obj.getClass().getDeclaredField(field);
//    f.setAccessible(true);
//    f.set(obj, newValue);
//  }
//
//  /**
//   * For unit tests.
//   * 
//   */
//  public void setEveryPrivateFieldsToNullOrDefault(Object obj)
//      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    setPrivateField("feedback", obj, null);
//    setPrivateField("objectives", obj, null);
//    setPrivateField("notes", obj, null);
//    setPrivateField("developmentNeeds", obj, null);
//    setPrivateField("feedbackRequests", obj, null);
//    setPrivateField("competencies", obj, null);
//  }
//
//  /**
//   * Unit test for the setFeedback method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetFeedback()
//  {
//    unitUnderTest.setFeedback(mockFeedbackList);
//    assertEquals(unitUnderTest.getFeedback(), mockFeedbackList);
//  }
//
//  // /**
//  // * Unit test for the setSpecificFeedback method : invalid ID.
//  // *
//  // * @throws InvalidAttributeValueException
//  // */
//  // @Test(expected= InvalidAttributeValueException.class)
//  // public void testGetSpecificFeedbackWithInvalidID() throws InvalidAttributeValueException
//  // {
//  // unitUnderTest.getSpecificFeedback(INVALID_ID);
//  // }
//  //
//  // /**
//  // * Unit test for the setSpecificFeedback method : valid ID.
//  // *
//  // * @throws InvalidAttributeValueException
//  // */
//  // @Test
//  // public void testGetSpecificFeedbackWithValidID() throws InvalidAttributeValueException
//  // {
//  // Feedback fbk=Mockito.mock(Feedback.class);
//  // when(mockFeedbackList.stream().filter(f -> f.getId() == anyInt()).findFirst().get()).thenReturn(fbk);
//  // assertEquals(fbk,unitUnderTest.getSpecificFeedback(VALID_ID));
//  // }
//
//  /**
//   * Unit test for the setObjectiveList method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetObjectiveListWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setObjectiveList(null);
//  }
//
//  /**
//   * Unit test for the setObjectiveList method : null objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetObjectiveListWithNullObjective() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getObjectiveList().add(null);
//    unitUnderTest.setObjectiveList(unitUnderTest.getObjectiveList());
//  }
//
//  /**
//   * Unit test for the setObjectiveList method :invalid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetObjectiveListWithInvalidObjective() throws InvalidAttributeValueException
//  {
//    Objective_OLD invalidObj = new Objective_OLD();
//    List<Objective_OLD> invalidObjList = Arrays.asList(invalidObj);
//    List<List<Objective_OLD>> invalidObjListList = Arrays.asList(invalidObjList);
//    unitUnderTest.setObjectiveList(invalidObjListList);
//  }
//
//  /**
//   * Unit test for the getLatestVersionObjectives method : null objectives.
//   * 
//   * @throws InvalidAttributeValueException
//   * @throws IllegalAccessException
//   * @throws IllegalArgumentException
//   * @throws SecurityException
//   * @throws NoSuchFieldException
//   */
//  @Test
//  public void testGetLatestVersionOfObjectivesNullCase() throws InvalidAttributeValueException,
//      IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    Field f = unitUnderTest.getClass().getDeclaredField("objectives");
//    f.setAccessible(true);
//    f.set(unitUnderTest, null);
//    assertEquals(unitUnderTest.getLatestVersionObjectives(), null);
//  }
//
//  /**
//   * Unit test for the getLatestVersionObjectives method : valid objectives.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionObjectivesValidObjectives() throws InvalidAttributeValueException,
//      IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    Objective_OLD obj = new Objective_OLD(1, 1, 1, "1", "1", "3010-01");
//    List<Objective_OLD> objList = Arrays.asList(obj);
//    List<List<Objective_OLD>> objListList = Arrays.asList(objList);
//    unitUnderTest.setObjectiveList(objListList);
//    assertEquals(unitUnderTest.getLatestVersionObjectives(), objList);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificObjective method : invalid ID.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testGetLatestVersionOfSpecificObjectiveInvalidID() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getLatestVersionOfSpecificObjective(INVALID_ID);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificObjective method : objective not found.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testGetLatestVersionOfSpecificObjectiveNotFound() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getLatestVersionOfSpecificObjective(VALID_ID);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificObjective method : valid objectives.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionOfSpecificObjectiveValidObjectives() throws InvalidAttributeValueException
//  {
//    Objective_OLD obj = new Objective_OLD(1, 1, 1, "1", "1", "3010-01");
//    List<Objective_OLD> objList = Arrays.asList(obj);
//    List<List<Objective_OLD>> objListList = Arrays.asList(objList);
//    unitUnderTest.setObjectiveList(objListList);
//    assertEquals(unitUnderTest.getLatestVersionOfSpecificObjective(obj.getID()), obj);
//  }
//
//  /**
//   * Unit test for the setNotes method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetNotes() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setNotes(mockNotesList);
//    assertEquals(unitUnderTest.getNotes(), mockNotesList);
//  }
//
//  /**
//   * Unit test for the setDevelopmentNeedsList method with null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetDevelopmentNeedsListWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setDevelopmentNeedsList(null);
//  }
//
//  /**
//   * Unit test for the setDevelopmentNeedsList method : null development need.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetDevelopmentNeedsListWithNullDevelopmentNeed() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getDevelopmentNeedsList().add(null);
//    unitUnderTest.setDevelopmentNeedsList(unitUnderTest.getDevelopmentNeedsList());
//  }
//
//  /**
//   * Unit test for the setDevelopmentNeedsList method : Invalid development need.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetDevelopmentNeedsListWithInvalidDevelopmentNeed() throws InvalidAttributeValueException
//  {
//    DevelopmentNeed_OLD invalidDevNeed = new DevelopmentNeed_OLD();
//    List<DevelopmentNeed_OLD> invalidDevNeedList = Arrays.asList(invalidDevNeed);
//    unitUnderTest.getDevelopmentNeedsList().add(invalidDevNeedList);
//    unitUnderTest.setDevelopmentNeedsList(unitUnderTest.getDevelopmentNeedsList());
//  }
//
//  /**
//   * Unit test for the getLatestVersionDevelopmentNeedsList method.
//   * 
//   * @throws InvalidAttributeValueException
//   * @throws IllegalAccessException
//   * @throws IllegalArgumentException
//   * @throws SecurityException
//   * @throws NoSuchFieldException
//   */
//  @Test
//  public void testGetLatestVersionDevelopmentNeedsListNullCase() throws InvalidAttributeValueException,
//      IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    Field f = unitUnderTest.getClass().getDeclaredField("developmentNeeds");
//    f.setAccessible(true);
//    f.set(unitUnderTest, null);
//    assertEquals(unitUnderTest.getLatestVersionDevelopmentNeeds(), null);
//  }
//
//  /**
//   * Unit test for the getLatestVersionDevelopmentNeedsList method. valid development need.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionDevelopmentNeedsListValidDevelopmentNeeds() throws InvalidAttributeValueException,
//      IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    DevelopmentNeed_OLD dev = new DevelopmentNeed_OLD(1, 1, 1, "1", "1");
//    List<DevelopmentNeed_OLD> devList = Arrays.asList(dev);
//    List<List<DevelopmentNeed_OLD>> devListList = Arrays.asList(devList);
//    unitUnderTest.setDevelopmentNeedsList(devListList);
//    assertEquals(unitUnderTest.getLatestVersionDevelopmentNeeds(), devList);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificDevelopmentNeeds method : valid development need.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionOfSpecificDevelopmentNeedsValidDevelopmentNeeds()
//      throws InvalidAttributeValueException
//  {
//    DevelopmentNeed_OLD dev = new DevelopmentNeed_OLD(1, 1, 1, "1", "1");
//    List<DevelopmentNeed_OLD> devList = Arrays.asList(dev);
//    List<List<DevelopmentNeed_OLD>> devListList = Arrays.asList(devList);
//    unitUnderTest.setDevelopmentNeedsList(devListList);
//    assertEquals(unitUnderTest.getLatestVersionOfSpecificDevelopmentNeed(1), dev);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificDevelopmentNeed method : invalid ID.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testGetLatestVersionOfSpecificDevelopmentNeedInvalidID() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getLatestVersionOfSpecificDevelopmentNeed(INVALID_ID);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificDevelopmentNeed method : development need not found.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionOfSpecificDevelopmentNeedNotFound() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.getLatestVersionOfSpecificDevelopmentNeed(5649), null);
//  }
//
//  /**
//   * Unit test for the setFeedbackRequestsList method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetFeedbackRequestsListWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setFeedbackRequestsList(null);
//  }
//
//  /**
//   * Unit test for the setFeedbackRequestsList method : null feedback request.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetFeedbackRequestsListWithNullObjective() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getFeedbackRequestsList().add(null);
//    unitUnderTest.setFeedbackRequestsList(unitUnderTest.getFeedbackRequestsList());
//  }
//
//  /**
//   * Unit test for the getFeedbackRequest method : feedback request not found.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testGetFeedbackRequestNotFound() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getFeedbackRequest("1");
//  }
//
//  /**
//   * Unit test for the getFeedbackRequest method : valid ID.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetFeedbackRequestValidID() throws InvalidAttributeValueException
//  {
//    FeedbackRequest fbr = new FeedbackRequest("1", "1");
//    List<FeedbackRequest> fbrList = Arrays.asList(fbr);
//    unitUnderTest.setFeedbackRequestsList(fbrList);
//    assertEquals(unitUnderTest.getFeedbackRequest("1"), fbr);
//  }
//
//  /**
//   * Unit test for the setCompetenciesList method with null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetCompetenciesListWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setCompetenciesList(null);
//  }
//
//  /**
//   * Unit test for the setCompetenciesList method : null competency.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testSetCompetenciesListWithNullCompetency() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getCompetenciesList().add(null);
//    unitUnderTest.setCompetenciesList(unitUnderTest.getCompetenciesList());
//  }
//
//  /**
//   * Unit test for the getLatestVersionCompetencies method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionCompetenciesEmptyCompetencies() throws InvalidAttributeValueException,
//      IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    List<List<Competency_OLD>> compListList = new ArrayList<List<Competency_OLD>>();
//
//    Field f = unitUnderTest.getClass().getDeclaredField("competencies");
//    f.setAccessible(true);
//    f.set(unitUnderTest, compListList);
//    assertEquals(unitUnderTest.getLatestVersionCompetencies(), compListList.get(0));
//  }
//
//  /**
//   * Unit test for the getLatestVersionCompetencies method. valid competency.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionCompetenciesValidCompetencies() throws InvalidAttributeValueException,
//      IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    Competency_OLD comp = new Competency_OLD(1, true);
//    List<Competency_OLD> compList = Arrays.asList(comp);
//    List<List<Competency_OLD>> compListList = Arrays.asList(compList);
//    unitUnderTest.setCompetenciesList(compListList);
//    assertEquals(unitUnderTest.getLatestVersionCompetencies(), compList);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificCompetency method : invalid ID.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionOfSpecificCompetencyInvalidID() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.getLatestVersionOfSpecificCompetency(INVALID_ID), null);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificCompetency method : competency not found.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testGetLatestVersionOfSpecificCompetencyNotFound() throws InvalidAttributeValueException
//  {
//    unitUnderTest.getLatestVersionOfSpecificCompetency(VALID_ID);
//  }
//
//  /**
//   * Unit test for the getLatestVersionOfSpecificCompetency method : valid competency.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testGetLatestVersionOfSpecificCompetencyValidCompetency() throws InvalidAttributeValueException
//  {
//    Competency_OLD comp = new Competency_OLD(1, true);
//    List<Competency_OLD> compList = Arrays.asList(comp);
//    List<List<Competency_OLD>> compListList = Arrays.asList(compList);
//    unitUnderTest.setCompetenciesList(compListList);
//    assertEquals(unitUnderTest.getLatestVersionOfSpecificCompetency(1), comp);
//  }
//
//  /**
//   * Unit test for the setLastLogon method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetLastLogon() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setLastLogon(date);
//    assertEquals(unitUnderTest.getLastLogon(), date);
//  }
//
//  /**
//   * Unit test for the addFeedback method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testAddFeedbackWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.addFeedback(null);
//  }
//
//  /**
//   * Unit test for the addFeedback method : valid feedback.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testAddFeedbackWithValidFeedback() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.addFeedback(mockFeedback), true);
//  }
//
//  /**
//   * Unit test for the addObjective method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testAddObjectiveWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.addObjective(null);
//  }
//
//  /**
//   * Unit test for the addObjective method : invalid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testAddObjectiveWithInvalidObjective() throws InvalidAttributeValueException
//  {
//    when(mockObjective.isObjectiveValid()).thenReturn(false);
//    unitUnderTest.addObjective(mockObjective);
//  }
//
//  /**
//   * Unit test for the addObjective method : valid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testAddObjectiveWithValidObjective() throws InvalidAttributeValueException
//  {
//    Objective_OLD obj = new Objective_OLD(1, 1, 1, "1", "1", "3010-01");
//    assertEquals(unitUnderTest.addObjective(obj), true);
//  }
//
//  /**
//   * Unit test for the addObjective method : check if objectives is null.
//   * 
//   * @throws SecurityException
//   * @throws NoSuchFieldException
//   * @throws IllegalAccessException
//   * @throws IllegalArgumentException
//   * 
//   */
//  @Test
//  public void testAddObjectiveCheckIfObjectivesIsNull() throws InvalidAttributeValueException, IllegalArgumentException,
//      IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    Objective_OLD obj = new Objective_OLD(1, 1, 1, "1", "1", "3010-01");
//    setPrivateField("objectives", unitUnderTest, null);
//    assertTrue(unitUnderTest.addObjective(obj));
//  }
//
//  /**
//   * Unit test for the editObjective method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testEditObjectiveWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.editObjective(null);
//  }
//
//  /**
//   * Unit test for the editObjective method : invalid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testEditObjectiveWithInvalidObjective() throws InvalidAttributeValueException
//  {
//    when(mockObjective.isObjectiveValid()).thenReturn(false);
//    unitUnderTest.editObjective(mockObjective);
//  }
//
//  /**
//   * Unit test for the editObjective method : valid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testEditObjectiveWithValidObjective() throws InvalidAttributeValueException
//  {
//
//    Objective_OLD obj = new Objective_OLD(1, 1, 1, "1", "1", "3010-01");
//    List<Objective_OLD> objList = Arrays.asList(obj);
//    List<List<Objective_OLD>> objListList = Arrays.asList(objList);
//    unitUnderTest.setObjectiveList(objListList);
//    assertEquals(unitUnderTest.editObjective(obj), true);
//  }
//
//  /**
//   * Unit test for the addNote method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testAddNote() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.addNote(mockNote), unitUnderTest.getNotes().add(mockNote));
//  }
//
//  /**
//   * Unit test for the addDevelopmentNeed method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testAddDevelopmentNeedWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.addDevelopmentNeed(null);
//  }
//
//  /**
//   * Unit test for the addDevelopmentNeed method : invalid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testAddDevelopmentNeedWithInvalidDevNeed() throws InvalidAttributeValueException
//  {
//    when(mockDevelopmentNeed.isDevelopmentNeedValid()).thenReturn(false);
//    unitUnderTest.addDevelopmentNeed(mockDevelopmentNeed);
//  }
//
//  /**
//   * Unit test for the addObjective method : valid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testAddDevelopmentNeedWithValidObjective() throws InvalidAttributeValueException
//  {
//    DevelopmentNeed_OLD dev = new DevelopmentNeed_OLD(1, 1, 1, "1", "1");
//    assertEquals(unitUnderTest.addDevelopmentNeed(dev), true);
//  }
//
//  /**
//   * Unit test for the addDevelopmentNeed method : check if developmentNeeds is null.
//   * 
//   * @throws SecurityException
//   * @throws NoSuchFieldException
//   * @throws IllegalAccessException
//   * @throws IllegalArgumentException
//   * 
//   */
//  @Test
//  public void testAddDevelopmentNeedCheckIfDevelopmentNeedsIsNull() throws InvalidAttributeValueException,
//      IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
//  {
//    DevelopmentNeed_OLD dev = new DevelopmentNeed_OLD(1, 1, 1, "1", "1");
//    setPrivateField("developmentNeeds", unitUnderTest, null);
//    assertTrue(unitUnderTest.addDevelopmentNeed(dev));
//  }
//
//  /**
//   * Unit test for the editDevelopmentNeed method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testEditDevelopmentNeedWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.editDevelopmentNeed(null);
//  }
//
//  /**
//   * Unit test for the editDevelopmentNeed method : invalid objective.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testEditDevelopmentNeedWithInvalidDevNeed() throws InvalidAttributeValueException
//  {
//    when(mockDevelopmentNeed.isDevelopmentNeedValid()).thenReturn(false);
//    unitUnderTest.editDevelopmentNeed(mockDevelopmentNeed);
//  }
//
//  /**
//   * Unit test for the editDevelopmentNeed method : valid development need.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testEditDevelopmentNeedWithValidDevelopmentNeed() throws InvalidAttributeValueException
//  {
//
//    DevelopmentNeed_OLD dev = new DevelopmentNeed_OLD(1, 1, 1, "1", "1");
//    List<DevelopmentNeed_OLD> devList = Arrays.asList(dev);
//    List<List<DevelopmentNeed_OLD>> devListList = Arrays.asList(devList);
//    unitUnderTest.setDevelopmentNeedsList(devListList);
//    assertEquals(unitUnderTest.editDevelopmentNeed(dev), true);
//  }
//
//  /**
//   * Unit test for the addFeedbackRequest method : null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testAddFeedbackRequestWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.addFeedbackRequest(null);
//  }
//
//  /**
//   * Unit test for the addFeedbackRequest method : valid feedback request.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testAddFeedbackRequestWithValidFeedbackRequest() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.addFeedbackRequest(mockFeedbackRequest),
//        unitUnderTest.getFeedbackRequestsList().add(mockFeedbackRequest));
//  }
//
//  /**
//   * Unit test for the updateCompetency method with null.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testUpdateCompetencyWithNull() throws InvalidAttributeValueException
//  {
//    unitUnderTest.updateCompetency(null, "test");
//  }
//
//  /**
//   * Unit test for the updateCompetency method with invalid competency.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test(expected = InvalidAttributeValueException.class)
//  public void testUpdateCompetencyWithInvalidCompetency() throws InvalidAttributeValueException
//  {
//    when(mockCompetency.isValid()).thenReturn(false);
//    unitUnderTest.updateCompetency(mockCompetency, "test");
//  }
//
//  /**
//   * Unit test for the updateCompetency method with valid competency.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testUpdateCompetencyWithValidCompetency() throws InvalidAttributeValueException
//  {
//    Competency_OLD comp = new Competency_OLD(1, true);
//    List<Competency_OLD> compList = Arrays.asList(comp);
//    List<List<Competency_OLD>> compListList = Arrays.asList(compList);
//    unitUnderTest.setCompetenciesList(compListList);
//    assertEquals(unitUnderTest.updateCompetency(comp, "1"), true);
//  }
//
//  /**
//   * Unit test for the nextFeedbackID method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testNextFeedbackID() throws InvalidAttributeValueException
//  {
//    assertEquals(unitUnderTest.nextFeedbackID(), unitUnderTest.getFeedback().size() + 1);
//  }
//
//  /**
//   * Unit test for the setProfile method.
//   * 
//   * @throws InvalidAttributeValueException
//   */
//  @Test
//  public void testSetProfile() throws InvalidAttributeValueException
//  {
//    unitUnderTest.setProfile(profile);
//    assertEquals(unitUnderTest.getProfile(), profile);
//  }
//}