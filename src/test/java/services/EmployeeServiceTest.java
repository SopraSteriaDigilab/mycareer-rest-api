package services;

import static model.Models.EMPLOYEE_ID;
import static model.Models.FULL_NAME;
import static model.Models.getEmployee;
import static model.Models.getObjective;
//import static model.Models.getEmployee;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import application.AppControllerTest;
import dataStructure.Employee;
import dataStructure.Objective_OLD;
import services.db.MongoOperations;
import services.db.MorphiaOperations;

/**
 * Unit tests for the EmployeeDAO class.
 */
public class EmployeeServiceTest
{
  /** Logger Property - Represents an implementation of the Logger interface that may be used here. */
  private static final Logger LOG = LoggerFactory.getLogger(AppControllerTest.class);

  /** Employee Property - Represents an Employee object... */
  private Employee employee;

  /** Objective Property - Represents an objective. */
  private Objective_OLD objective;

  /** List<Objective> Property - Represents list of objectives */
  private List<Objective_OLD> objectives;

  /** MorphiaOperations Property - Mocked by Mockito. */
  @Mock
  private MorphiaOperations mockMorphiaOperations;

  /** MongoOperations Property - Mocked by Mockito. */
  @Mock
  private MongoOperations mockObjectivesHistoriesOperations;

  /** MongoOperations Property - Mocked by Mockito. */
  @Mock
  private MongoOperations mockDevelopmentNeedsHistoriesOperations;

  /** MongoOperations Property - Mocked by Mockito. */
  @Mock
  private MongoOperations mockCompetenciesHistoriesOperations;

  /** Environment Property - Mocked by Mockito. */
  @Mock
  private Environment mockEnvironment;

  /** EmployeeProfileSerivce Property - Mocked by Mockito. */
  @Mock
  private EmployeeProfileService mockEmployeeProfileService;

  /** EmployeeDAO Property - Mocked by Mockito. */
  @Mock
  private EmployeeService mockEmployeeService;

  /** Employee Property - Mocked by Mockito. */
  @Mock
  private Employee mockEmployee;

  /** Query Property - Mocked by Mockito. */
  @SuppressWarnings("rawtypes")
  @Mock
  private Query mockQuery;

  /** ProcessComponentsImpl Property - Represents the unit under test. */
  @InjectMocks
  private EmployeeService unitUnderTest;

  /**
   * Setup method that runs once before each test method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Before
  public void setup() throws InvalidAttributeValueException
  {
    MockitoAnnotations.initMocks(this);
    unitUnderTest = new EmployeeService(mockMorphiaOperations, mockObjectivesHistoriesOperations,
        mockDevelopmentNeedsHistoriesOperations, mockCompetenciesHistoriesOperations, mockEmployeeProfileService,
        mockEnvironment);
    employee = getEmployee();

  }

  /**
   * Unit test for the getEmployee method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetEmployeeShouldWorkAsExpected() throws EmployeeNotFoundException
  {
    when(mockMorphiaOperations.getEmployee("profile.employeeID", EMPLOYEE_ID)).thenReturn(mockEmployee);
    assertEquals(mockEmployee, unitUnderTest.getEmployee(EMPLOYEE_ID));
  }

  /**
   * Unit test for the getEmployee method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected = EmployeeNotFoundException.class)
  public void testGetEmployeeShouldThrowException() throws EmployeeNotFoundException
  {
    when(mockMorphiaOperations.getEmployee("profile.employeeID", EMPLOYEE_ID)).thenReturn(null);
    unitUnderTest.getEmployee(EMPLOYEE_ID);
  }

  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testgetFullNameUserWorkAsExpected() throws EmployeeNotFoundException
  {
    when(mockMorphiaOperations.getEmployee("profile.employeeID", EMPLOYEE_ID)).thenReturn(mockEmployee);
    assertEquals(FULL_NAME, unitUnderTest.getFullNameUser(EMPLOYEE_ID));
  }

  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected = EmployeeNotFoundException.class)
  public void testgetFullNameUserWithNulls() throws EmployeeNotFoundException
  {
    when(mockMorphiaOperations.getEmployee("profile.employeeID", EMPLOYEE_ID)).thenReturn(null);
    unitUnderTest.getFullNameUser(EMPLOYEE_ID);
  }

  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testgetObjectivesForUserWorkAsExpected() throws EmployeeNotFoundException, InvalidAttributeValueException
  {

    objective = getObjective();
    objectives = Arrays.asList(objective);

    employee.addObjective(objective);
    when(mockMorphiaOperations.getEmployee("profile.employeeID", EMPLOYEE_ID)).thenReturn(mockEmployee);

    assertEquals(objectives, unitUnderTest.getObjectivesForUser(EMPLOYEE_ID));
  }

  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testgetObjectivesForUserWithNulls() throws EmployeeNotFoundException
  {
    when(mockMorphiaOperations.getEmployee("profile.employeeID", EMPLOYEE_ID)).thenReturn(mockEmployee);
    unitUnderTest.getObjectivesForUser(EMPLOYEE_ID);
  }

}
