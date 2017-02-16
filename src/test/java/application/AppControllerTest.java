package application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import controller.EmployeeController;
import dataStructure.Employee;
import dataStructure.Objective;
import services.EmployeeService;

/**
 * Unit tests for the AppController class.
 */
public class AppControllerTest
{
  /** Logger Property - Represents an implementation of the Logger interface that may be used here. */
  private static final Logger LOG = LoggerFactory.getLogger(AppControllerTest.class);

  /** Long Constant - Represents a valid employee ID. */
  private final long VALID_EMPLOYEE_ID = 675590;

  /** Datastore Property - Mocked by Mockito. */
  @Mock
  private Datastore mockDatastore;

  /** Environment Property - Mocked by Mockito. */
  @Mock
  private Environment mockEnvironment;

  /** EmployeeDAO Property - Mocked by Mockito. */
  @Mock
  private EmployeeService mockEmployeeDao;

  /** Employee Property - Mocked by Mockito. */
  @Mock
  private Employee mockEmployee;

  /** Query Property - Mocked by Mockito. */
  @SuppressWarnings("rawtypes")
  @Mock
  private Query mockQuery;

  /** List<Objective> Property - Mocked by Mockito. */
  @Mock
  private List<Objective> mockListOfObjectives;

  /** AppController Property - Represents the unit under test. */
  @InjectMocks
  private EmployeeController unitUnderTest;

  /**
   * Setup method that runs once before each test method.
   */
  @SuppressWarnings("unchecked")
  @Before
  public void setup()
  {
    // LOG.debug("AppControllerTest.setup()", true);

    unitUnderTest = new EmployeeController();

    MockitoAnnotations.initMocks(this);

    when(mockDatastore.createQuery(Mockito.any())).thenReturn(mockQuery);
    when(mockQuery.filter(Mockito.anyString(), Mockito.any())).thenReturn(mockQuery);
    when(mockQuery.get()).thenReturn(mockEmployee);

    mockEmployeeDao = new EmployeeService(mockDatastore, mockEnvironment);
  }

  /**
   * Unit test for the testWelcome method.
   */
  @Test
  public void testWelcomePageShouldWorkAsExpected()
  {
    // LOG.debug("AppControllerTest.testWelcomePageShouldWorkAsExpected()");

    ResponseEntity<String> expected = ok("Welcome to the MyCareer Project");
    assertEquals(expected, unitUnderTest.welcomePage());
  }

  /**
   * Unit test for the testGetObjectives method
   * 
   * @throws InvalidAttributeValueException
   */
  @SuppressWarnings({ "static-access" })
  @Test
  public void testGetObjectivesShouldWorkAsExpected() throws InvalidAttributeValueException
  {
    // LOG.debug("AppControllerTest.testGetObjectives()");

    when(mockEmployeeDao.getObjectivesForUser(VALID_EMPLOYEE_ID)).thenReturn(mockListOfObjectives);

    assertEquals(OK, unitUnderTest.getObjectives(VALID_EMPLOYEE_ID).getStatusCode());
    assertEquals(OK, unitUnderTest.getObjectives(VALID_EMPLOYEE_ID).getStatusCode());
    assertEquals(OK, unitUnderTest.getObjectives(VALID_EMPLOYEE_ID).getStatusCode());
  }

}
