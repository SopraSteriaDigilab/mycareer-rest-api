package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
 
public class EmployeeProfileTest
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private final long VALID_EMPLOYEE_ID = 675590;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final long INVALID_EMPLOYEE_ID = -675590;
  
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
  private final boolean HAS_HRDASH = true;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final boolean IS_MANAGER = true;

  /** TYPE Property|Constant - Represents|Indicates... */
  private final List<String> VALID_REPORTEE_LIST = Arrays.asList("a");
  
  @InjectMocks
  private EmployeeProfile unitUnderTest, unitUnderTestEmpty;
    
  /**
   * Setup method that runs once before each test method.
   * @throws InvalidAttributeValueException 
   * 
   */
  @Before
  public void setup() throws InvalidAttributeValueException
  {
    unitUnderTestEmpty = new EmployeeProfile();
    Set<String> emailAddresses = new HashSet<>();
    emailAddresses.add(VALID_EMAIL_ADDRESS);
    unitUnderTest = new EmployeeProfile.Builder().employeeID(VALID_EMPLOYEE_ID).guid(VALID_GUID).forename(VALID_NAME)
        .surname(VALID_NAME).emailAddress(emailAddresses).username(VALID_USERNAME).company(VALID_COMPANY)
        .superSector(VALID_SECTOR_SUPERSECTOR).sector(VALID_SECTOR_SUPERSECTOR).steriaDepartment(VALID_DEPARTMENT)
        .sopraDepartment(VALID_DEPARTMENT).manager(IS_MANAGER).hasHRDash(HAS_HRDASH).reporteeCNs(new ArrayList<>(VALID_REPORTEE_LIST))
        .build();
    initMocks(this);
  }
  
  /**
   * For unit tests.
   * 
   */
  public void setPrivateField(String field, Object obj, Object newValue) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException 
  {
    Field f = obj.getClass().getDeclaredField(field);
    f.setAccessible(true);
    f.set(obj,newValue);
  }
  
  /**
   * For unit tests.
   * 
   */
  public void setEveryPrivateFieldsToNullOrDefault(Object obj) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException 
  {
    setPrivateField("guid", obj, null);
    setPrivateField("company", obj, null);
    setPrivateField("emailAddress", obj, null);
    setPrivateField("employeeID", obj, 0);
    setPrivateField("forename", obj, null);
    setPrivateField("reporteeCNs", obj, null);
    setPrivateField("isManager", obj, true);
    setPrivateField("hasHRDash", obj, true);
    setPrivateField("sector", obj, null);
    setPrivateField("superSector", obj, null);
    setPrivateField("sopraDepartment", obj, null);
    setPrivateField("steriaDepartment", obj, null);
    setPrivateField("reporteeCNs", obj, null);
    setPrivateField("username", obj, null);
    setPrivateField("surname", obj, null);
  }  
  
  /**
   * Unit test for the setEmailAddress method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmailAddressWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setEmailAddress(null);
  }
  
  /**
   * Unit test for the setEmailAddress method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmailAddressWithEmptySet() throws InvalidAttributeValueException
  {  
    unitUnderTest.setEmailAddress(new HashSet<>());
  }
  
  /**
   * Unit test for the setEmailAddress method : invalid email address.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmailAddressWithInvalidEmailAddress() throws InvalidAttributeValueException
  {  
    Set<String> emailAddresses = new HashSet<>();
    emailAddresses.add(INVALID_EMAIL_ADDRESS);
    unitUnderTest.setEmailAddress(emailAddresses);
  }
  
  /**
   * Unit test for the setEmailAddress method : valid email address.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetEmailAddressWithValidEmailAddress() throws InvalidAttributeValueException
  {
    Set<String> emailAddresses = new HashSet<>();
    emailAddresses.add(VALID_EMAIL_ADDRESS);
    assertEquals(unitUnderTest.getEmailAddress(), emailAddresses);
  }
  
  /**
   * Unit test for the setEmployeeID method : invalid employee id.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmployeeIDWithInvalidEmployeeID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setEmployeeID(INVALID_EMPLOYEE_ID);
  }
  
  /**
   * Unit test for the setEmailAddress method : valid email address.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetEmployeeIDWithValidEmployeeID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setEmployeeID(VALID_EMPLOYEE_ID);
    assertEquals(unitUnderTest.getEmployeeID(),VALID_EMPLOYEE_ID);
  }

  /**
   * Unit test for the setUsername method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetUsernameWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setUsername(null);
  }
  
  /**
   * Unit test for the setUsername method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetUsernamesWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setUsername("");
  }
  
  /**
   * Unit test for the setUsername method : invalid username.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetUsernameWithInvalidEmployeeID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setUsername(INVALID_USERNAME);
  }
  
  /**
   * Unit test for the setUsername method : valid username.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetUsernameWithValidEmployeeID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setUsername(VALID_USERNAME);
    assertEquals(unitUnderTest.getUsername(),VALID_USERNAME);
  }
  
  /**
   * Unit test for the setSurname method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSurnameWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSurname(null);
  }
  
  /**
   * Unit test for the setSurname method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSurnameWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSurname("");
  }
  
  /**
   * Unit test for the setSurname method : invalid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSurnameWithInvalidName() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSurname(INVALID_NAME);
  }
  
  /**
   * Unit test for the setSurname method : valid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSurnameWithValidName() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSurname("A");
    assertEquals(unitUnderTest.getSurname(),"A");
  }
  
  /**
   * Unit test for the setForename method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetForenameWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setForename(null);
  }
  
  /**
   * Unit test for the setForename method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetForenameWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setForename("");
  }
  
  /**
   * Unit test for the setForename method : invalid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetForenameWithInvalidName() throws InvalidAttributeValueException
  {  
    unitUnderTest.setForename(INVALID_NAME);
  }
  
  /**
   * Unit test for the setForename method : valid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetForenameWithValidName() throws InvalidAttributeValueException
  {  
    unitUnderTest.setForename(VALID_NAME);
    assertEquals(unitUnderTest.getForename(),VALID_NAME);
  }
  
  /**
   * Unit test for the isManager method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetIsManager() throws InvalidAttributeValueException
  {  
    unitUnderTest.setIsManager(true);
    assertEquals(unitUnderTest.getIsManager(),true);
  }
  
  /**
   * Unit test for the setHasHRDash method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetHasHRDash() throws InvalidAttributeValueException
  {  
    unitUnderTest.setHasHRDash(true);
    assertEquals(unitUnderTest.getHasHRDash(),true);
  }
  
  /**
   * Unit test for the setReporteeCNs method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetReporteeCNs() throws InvalidAttributeValueException
  {
    List<String> list = unitUnderTest.getReporteeCNs();
    unitUnderTest.setReporteeCNs(Arrays.asList("a"));
    list.add("a");
    assertEquals(unitUnderTest.getReporteeCNs(), list);
  }
  
  /**
   * Unit test for the setReporteeCNs method with null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetReporteeCNsWithNull() throws InvalidAttributeValueException
  {
    unitUnderTest.setReporteeCNs(null);
  }
  
  /**
   * Unit test for the setCompany method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetCompanyWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setCompany(null);
  }
   
  /**
   * Unit test for the setCompany method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetCompanyWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setCompany("");
  }
  
  /**
   * Unit test for the setCompany method : invalid company.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetCompanyWithInvalidCompany() throws InvalidAttributeValueException
  {  
    unitUnderTest.setCompany(INVALID_COMPANY);
  }
  
  /**
   * Unit test for the setCompany method : valid company.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetCompanyWithValidCompany() throws InvalidAttributeValueException
  {  
    unitUnderTest.setCompany(VALID_COMPANY);
    assertEquals(unitUnderTest.getCompany(),VALID_COMPANY);
  }
  
  /**
   * Unit test for the setSopraDepartment method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSopraDepartmentWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSopraDepartment(null);
  }
   
  /**
   * Unit test for the setSopraDepartment method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSopraDepartmentWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSopraDepartment("");
  }
  
  /**
   * Unit test for the setSopraDepartment method : invalid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSopraDepartmentWithInvalidCompany() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSopraDepartment(INVALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSopraDepartment method : valid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSopraDepartmentWithValidCompany() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSopraDepartment(VALID_DEPARTMENT);
    assertEquals(unitUnderTest.getSopraDepartment(),VALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSteriaDepartment method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSteriaDepartmentWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSteriaDepartment(null);
  }
   
  /**
   * Unit test for the setSteriaDepartment method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSteriaDepartmentWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSteriaDepartment("");
  }
  
  /**
   * Unit test for the setSteriaDepartment method : invalid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSteriaDepartmentWithInvalidDepartment() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSteriaDepartment(INVALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSteriaDepartment method : valid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSteriaDepartmentWithValidDepartment() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSteriaDepartment(VALID_DEPARTMENT);
    assertEquals(unitUnderTest.getSteriaDepartment(),VALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSector method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSectorWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSector(null);
  }
   
  /**
   * Unit test for the setSector method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSectorWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSector("");
  }
  
  /**
   * Unit test for the setSector method : invalid Sector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSectorWithInvalidSector() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSector(INVALID_SECTOR);
  }
  
  /**
   * Unit test for the setSector method : valid Sector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSectorWithValidSector() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSector(VALID_SECTOR_SUPERSECTOR);
    assertEquals(unitUnderTest.getSector(),VALID_SECTOR_SUPERSECTOR);
  }
  
  /**
   * Unit test for the setSuperSector method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSuperSectorWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSuperSector(null);
  }
   
  /**
   * Unit test for the setSuperSector method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSuperSectorWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSuperSector("");
  }
  
  /**
   * Unit test for the setSuperSector method : invalid SuperSector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSuperSectorWithInvalidSuperSector() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSuperSector(INVALID_SUPERSECTOR);
  }
  
  /**
   * Unit test for the setSuperSector method : valid SuperSector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSuperSectorWithValidSuperSector() throws InvalidAttributeValueException
  {  
    unitUnderTest.setSuperSector(VALID_SECTOR_SUPERSECTOR);
    assertEquals(unitUnderTest.getSuperSector(),VALID_SECTOR_SUPERSECTOR);
  }
  
  /**
   * Unit test for the setGUID method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetGUIDWithNull() throws InvalidAttributeValueException
  {  
    unitUnderTest.setGuid(null);
  }
  
  /**
   * Unit test for the setGUID method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetGUIDWithEmptyString() throws InvalidAttributeValueException
  {  
    unitUnderTest.setGuid("");
  }
  
  /**
   * Unit test for the setGUID method : valid GUID.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetGUIDWithValidGUID() throws InvalidAttributeValueException
  {  
    unitUnderTest.setGuid(VALID_GUID);
    assertEquals(unitUnderTest.getGuid(),VALID_GUID);
  }
  
//  /**
//   * Unit test for the toGson method.
//   * 
//   */
//  @Test
//  public void testToGson()
//  {
//    Gson gsonData = new Gson();
//    assertEquals(unitUnderTest.toGson(),gsonData.toJson(unitUnderTest));
//  }
  
  /**
   * Unit test for the getFullName method.
   * 
   * @throws InvalidAttributeValueException
   */  
  @Test
  public void testGetFullName() throws InvalidAttributeValueException
  {  
    unitUnderTest.setForename("a");
    unitUnderTest.setSurname("b");
    assertEquals(unitUnderTest.getFullName(),"a B");
  }
  
  
  /**
   * Unit test for the addReportee method : check if reporteeCNs is null.
   * @throws SecurityException 
   * @throws NoSuchFieldException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   * 
   */
  @Test
  public void testAddReporteeCheckIfReporteeCNsIsNull() throws InvalidAttributeValueException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
  {
    setPrivateField("reporteeCNs", unitUnderTest, null);
    assertTrue(unitUnderTest.addReportee("test"));
  }
  
  /**
   * Unit test for the addReportee method : Null.
   * 
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testAddReporteeWithNull() throws InvalidAttributeValueException
  {
    unitUnderTest.addReportee(null);
  }
  
  /**
   * Unit test for the addReportee method : empty string.
   * 
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testAddReporteeWithEmptyString() throws InvalidAttributeValueException
  {
    unitUnderTest.addReportee("");
  }
  
  /**
   * Unit test for the addReportee method : single element string.
   * 
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testAddReporteeWithSingleElementString() throws InvalidAttributeValueException
  {
    assertTrue(unitUnderTest.addReportee("1"));
  }
  
  /**
   * Unit test for the addReportee method : valid string.
   * 
   */
  @Test
  public void testAddReporteeWithValidString() throws InvalidAttributeValueException
  {
    assertTrue(unitUnderTest.addReportee("Alex"));
  }
  
  /**
   * Unit test for the equals method : same object.
   * 
   */
  @Test
  public void testEqualsSameObject() throws InvalidAttributeValueException
  {
    assertEquals(unitUnderTest.equals(unitUnderTest),true);
  }
  
  /**
   * Unit test for the equals method : null.
   * 
   */
  @Test
  public void testEqualsNull() throws InvalidAttributeValueException
  {
    assertEquals(unitUnderTest.equals(null),false);
  }
  
  /**
   * Unit test for the equals method : different classes.
   * 
   */
  @Test
  public void testEqualsDifferentClasses() throws InvalidAttributeValueException
  {
    String a="String class";
    assertEquals(unitUnderTest.equals(a),false);
  } 
  
  /**
   * Unit test for the equals method : null fields for both classes.
   * 
   */
  @Test
  public void testEqualsNullFieldsForBothClasses() throws InvalidAttributeValueException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
  {
    EmployeeProfile employeeProfile = new EmployeeProfile();
    setEveryPrivateFieldsToNullOrDefault(unitUnderTest);
    setEveryPrivateFieldsToNullOrDefault(employeeProfile);
    assertEquals(unitUnderTest.equals(employeeProfile),true);
  }
  
  /**
   * Unit test for the equals method :null field.
   * @throws SecurityException 
   * @throws NoSuchFieldException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   * 
   */
  @Test
  public void testEqualsOneOfTheTwoClassesHasANullField() throws InvalidAttributeValueException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
  {
    EmployeeProfile employeeProfile = new EmployeeProfile();
    
    setEveryPrivateFieldsToNullOrDefault(unitUnderTest);    //One GUID is null
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("guid", employeeProfile, null);     //Same GUID, one company is null
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("company", employeeProfile, null);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("emailAddress", employeeProfile, null);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("employeeID", employeeProfile, 0);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("forename", employeeProfile, null);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("hasHRDash", employeeProfile, true);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("isManager", employeeProfile, true);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("reporteeCNs", employeeProfile, null);     
    assertEquals(unitUnderTest.equals(employeeProfile), false);
    
    setPrivateField("sector", employeeProfile, null);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("sopraDepartment", employeeProfile, null);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("steriaDepartment", employeeProfile, null);    
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("superSector", employeeProfile, null);     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("surname", employeeProfile, null);    
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("username", employeeProfile, null);
    assertEquals(unitUnderTest.equals(employeeProfile),true);
  }
  
  /**
   * Unit test for the equals method :null field.
   * @throws SecurityException 
   * @throws NoSuchFieldException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   * 
   */
  @Test
  public void testEqualsFieldNotNullButDifferentValues() throws InvalidAttributeValueException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
  {
    EmployeeProfile employeeProfile = new EmployeeProfile();

    
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setGuid(unitUnderTest.getGuid());     //Same GUID, one company is null
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setCompany(unitUnderTest.getCompany());     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setEmailAddress(unitUnderTest.getEmailAddress());     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setEmployeeID(unitUnderTest.getEmployeeID());     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setForename(unitUnderTest.getForename()); 
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setHasHRDash(unitUnderTest.getHasHRDash()); 
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setIsManager(unitUnderTest.getIsManager()); 
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    employeeProfile.setReporteeCNs(unitUnderTest.getReporteeCNs());     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("sector", employeeProfile, unitUnderTest.getSector());     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("sopraDepartment", employeeProfile, unitUnderTest.getSopraDepartment());     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("steriaDepartment", employeeProfile, unitUnderTest.getSteriaDepartment());    
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("superSector", employeeProfile, unitUnderTest.getSuperSector());     
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("surname", employeeProfile, unitUnderTest.getSurname());    
    assertEquals(unitUnderTest.equals(employeeProfile),false);
    
    setPrivateField("username", employeeProfile, unitUnderTest.getUsername());
    assertEquals(unitUnderTest.equals(employeeProfile),true);
  }

  /**
   * Unit test for the toString method.
   * 
   */
  @Test
  public void testToString()
  {
    assertEquals(unitUnderTest.toString() , "EmployeeProfile [employeeID=" + unitUnderTest.getEmployeeID() + ", surname=" + unitUnderTest.getSurname() + ", forename=" + unitUnderTest.getForename()
    + ", username=" + unitUnderTest.getUsername() + ", emailAddress=" + unitUnderTest.getEmailAddress() + ", isManager=" + unitUnderTest.getIsManager() + ", GUID=" + unitUnderTest.getGuid()
    + ", company=" + unitUnderTest.getCompany() + ", sopraDepartment=" + unitUnderTest.getSopraDepartment() + ", steriaDepartment="
    + unitUnderTest.getSteriaDepartment() + ", sector=" + unitUnderTest.getSector() + ", superSector=" + unitUnderTest.getSuperSector() + ", reporteeCNs=" + unitUnderTest.getReporteeCNs()
    + "]" );
  }
}