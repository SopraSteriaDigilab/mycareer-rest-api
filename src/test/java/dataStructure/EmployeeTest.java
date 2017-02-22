package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for the EmployeeTest class.
 *
 */
public class EmployeeTest
{

  /** TYPE Property|Constant - Represents|Indicates... */
  private final long VALID_EMPLOYEE_ID = 675590;

  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_STRING = "string";

  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_STRING_2 = "string";

  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_EMAIL = "a@b.c";

  /** TYPE Property|Constant - Represents|Indicates... */
  private final String VALID_EMAIL_2 = "a@b.c";

  /** TYPE Property|Constant - Represents|Indicates... */
  private final List<String> VALID_REPORTEE_LIST = Arrays.asList("a", "b");

  /** TYPE Property|Constant - Represents|Indicates... */
  private final List<String> VALID_REPORTEE_LIST_2 = Arrays.asList("a");

  /** TYPE Property|Constant - Represents|Indicates... */
  private ADProfile_Advanced adProfileAdvanced;

  /** TYPE Property|Constant - Represents|Indicates... */
  private List<Feedback> mockFeedbackList;

  /** TYPE Property|Constant - Represents|Indicates... */
  @Mock
  private List<List<Objective>> mockObjectiveList;

  /** TYPE Property|Constant - Represents|Indicates... */
  @Mock
  private List<List<Note_OLD>> mockNotesList;

  /** TYPE Property|Constant - Represents|Indicates... */
  @Mock
  List<List<DevelopmentNeed>> mockDevelopmentNeedsList;

  /** TYPE Property|Constant - Represents|Indicates... */
  @Mock
  private List<FeedbackRequest> mockFeedbackRequestsList;

  /** TYPE Property|Constant - Represents|Indicates... */
  @Mock
  private List<List<Competency>> mockCompetenciesList;

  /** AppController Property - Represents the unit under test. */
  @InjectMocks
  private Employee unitUnderTest;

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
//    adProfileAdvanced = new ADProfile_Advanced(VALID_EMPLOYEE_ID, VALID_STRING, VALID_STRING, VALID_STRING, VALID_EMAIL,
//        VALID_STRING, VALID_STRING, VALID_STRING, VALID_EMAIL, VALID_EMAIL, VALID_EMAIL, VALID_EMAIL, false, false,
//        VALID_REPORTEE_LIST);
//    unitUnderTest = new Employee(adProfileAdvanced);
//
//    MockitoAnnotations.initMocks(this);
//  }

//  @Test
//  public void testMatchAndUpdate() throws InvalidAttributeValueException
//  {
//    boolean updated = unitUnderTest.matchAndUpdated(adProfileAdvanced);
//
//    // TODO User overridden equals method for employee...
//    assertFalse(updated);
//    assertEquals(adProfileAdvanced.getReporteeCNs(), unitUnderTest.getReporteeCNs());
//    assertEquals(adProfileAdvanced.getCompany(), unitUnderTest.getCompany());
//    assertEquals(adProfileAdvanced.getEmailAddress(), unitUnderTest.getEmailAddress());
//    assertEquals(adProfileAdvanced.getEmployeeID(), unitUnderTest.getEmployeeID());
//    assertEquals(adProfileAdvanced.getForename(), unitUnderTest.getForename());
//    assertEquals(adProfileAdvanced.getFullName(), unitUnderTest.getFullName());
//    assertEquals(adProfileAdvanced.getIsManager(), unitUnderTest.getIsManager());
//    assertEquals(adProfileAdvanced.getSurname(), unitUnderTest.getSurname());
//    assertEquals(adProfileAdvanced.getTeam(), unitUnderTest.getTeam());
//    assertEquals(adProfileAdvanced.getUsername(), unitUnderTest.getUsername());
//
//    unitUnderTest.setReporteeCNs(VALID_REPORTEE_LIST_2);
//    updated = unitUnderTest.matchAndUpdated(adProfileAdvanced);
//
//    assertTrue(updated);
//    assertEquals(adProfileAdvanced.getReporteeCNs(), unitUnderTest.getReporteeCNs());
//    assertEquals(adProfileAdvanced.getCompany(), unitUnderTest.getCompany());
//    assertEquals(adProfileAdvanced.getEmailAddress(), unitUnderTest.getEmailAddress());
//    assertEquals(adProfileAdvanced.getEmployeeID(), unitUnderTest.getEmployeeID());
//    assertEquals(adProfileAdvanced.getForename(), unitUnderTest.getForename());
//    assertEquals(adProfileAdvanced.getFullName(), unitUnderTest.getFullName());
//    assertEquals(adProfileAdvanced.getIsManager(), unitUnderTest.getIsManager());
//    assertEquals(adProfileAdvanced.getSurname(), unitUnderTest.getSurname());
//    assertEquals(adProfileAdvanced.getTeam(), unitUnderTest.getTeam());
//    assertEquals(adProfileAdvanced.getUsername(), unitUnderTest.getUsername());
//
//  }

}
