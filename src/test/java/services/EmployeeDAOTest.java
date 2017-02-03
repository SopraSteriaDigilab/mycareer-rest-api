package services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
import org.springframework.http.ResponseEntity;

import application.AppController;
import application.AppControllerTest;
import dataStructure.Employee;
import dataStructure.Objective;

/**
 * Unit tests for the EmployeeDAO class.
 */
public class EmployeeDAOTest
{
  /** Logger Property - Represents an implementation of the Logger interface that may be used here. */
  private static final Logger LOG = LoggerFactory.getLogger(AppControllerTest.class);

  /** Long Constant - Represents a valid employee ID. */
  private final long VALID_EMPLOYEE_ID = 675590;

  /** Datastore Property - Mocked by Mockito. */
  @Mock
  private Datastore mockDatastore;

  /** EmployeeDAO Property - Mocked by Mockito. */
  @Mock
  private EmployeeDAO mockEmployeeDao;

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

  /** ProcessComponentsImpl Property - Represents the unit under test. */
  @InjectMocks
  private EmployeeDAO unitUnderTest;

  /**
   * Setup method that runs once before each test method.
   */
  @SuppressWarnings("unchecked")
  @Before
  public void setup()
  {
    // LOG.debug("AppControllerTest.setup()", true);

    unitUnderTest = new EmployeeDAO();

    MockitoAnnotations.initMocks(this);

    when(mockDatastore.createQuery(Mockito.any())).thenReturn(mockQuery);
    when(mockQuery.filter(Mockito.anyString(), Mockito.any())).thenReturn(mockQuery);
    when(mockQuery.get()).thenReturn(mockEmployee);

    mockEmployeeDao = new EmployeeDAO(mockDatastore);
  }

  /**
   * Unit test for the getEmployee method.
   * 
   * @throws InvalidAttributeValueException
   */
  @SuppressWarnings("static-access")
  @Test
  public void testGetEmployeeShouldWorkAsExpected() throws InvalidAttributeValueException
  {
    // LOG.debug("EmployeeDAOTest.testGetEmployeeShouldWorkAsExpected()");

    assertEquals(mockEmployee, unitUnderTest.getEmployee(VALID_EMPLOYEE_ID));
  }

  /**
   * Unit test for the getEmployee method.
   * 
   * @throws InvalidAttributeValueException
   */
  @SuppressWarnings("static-access")
  @Test(expected = InvalidAttributeValueException.class)
  public void testGetEmployeeShouldThrowException() throws InvalidAttributeValueException
  {
    // LOG.debug("EmployeeDAOTest.testGetEmployeeShouldWorkAsExpected()");

    when(mockQuery.get()).thenReturn(null);

    unitUnderTest.getEmployee(VALID_EMPLOYEE_ID);
  }

}
