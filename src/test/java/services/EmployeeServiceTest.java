package services;

import static model.Models.*;
import static model.Models.FULL_NAME;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import application.AppControllerTest;
import dataStructure.Employee;
import dataStructure.Objective;


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
  private Objective objective;
  
  /** List<Objective> Property - Represents list of objectives */
  private List<Objective> objectives;

  /** Datastore Property - Mocked by Mockito. */
  @Mock
  private Datastore mockDatastore;

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
   * @throws InvalidAttributeValueException 
   */
  @SuppressWarnings("unchecked")
  @Before
  public void setup() throws InvalidAttributeValueException
  {
    MockitoAnnotations.initMocks(this);
    unitUnderTest = new EmployeeService(mockDatastore, mockEmployeeProfileService, mockEnvironment);
    employee = getEmployee();
    
    when(mockDatastore.createQuery(Mockito.any())).thenReturn(mockQuery);
    when(mockQuery.filter(Mockito.anyString(), Mockito.any())).thenReturn(mockQuery);
    when(mockQuery.get()).thenReturn(mockEmployee);

  }

  /**
   * Unit test for the getEmployee method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetEmployeeShouldWorkAsExpected() throws InvalidAttributeValueException
  {
    assertEquals(mockEmployee, unitUnderTest.getEmployee(EMPLOYEE_ID));
  }

  /**
   * Unit test for the getEmployee method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected = InvalidAttributeValueException.class)
  public void testGetEmployeeShouldThrowException() throws InvalidAttributeValueException
  {
    when(mockQuery.get()).thenReturn(null);
    unitUnderTest.getEmployee(EMPLOYEE_ID);
  }
  
  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testgetFullNameUserWorkAsExpected() throws InvalidAttributeValueException
  {
    when(mockQuery.get()).thenReturn(employee);
    assertEquals(FULL_NAME, unitUnderTest.getFullNameUser(EMPLOYEE_ID));
  }
  
  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected=InvalidAttributeValueException.class)
  public void testgetFullNameUserWithNulls() throws InvalidAttributeValueException
  {
    when(mockQuery.get()).thenReturn(null);
    unitUnderTest.getFullNameUser(EMPLOYEE_ID);
  }
  
  
  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testgetObjectivesForUserWorkAsExpected() throws InvalidAttributeValueException
  {
    
    objective = getObjective();
    objectives = Arrays.asList(objective);
    
    employee.addObjective(objective);
    when(mockQuery.get()).thenReturn(employee);
    
    assertEquals(objectives, unitUnderTest.getObjectivesForUser(EMPLOYEE_ID));
  }
  
  /**
   * Unit test for the getFullNameUser method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected=InvalidAttributeValueException.class)
  public void testgetObjectivesForUserWithNulls() throws InvalidAttributeValueException
  {
    when(mockQuery.get()).thenReturn(null);
    unitUnderTest.getObjectivesForUser(EMPLOYEE_ID);
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

}
