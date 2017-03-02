package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.management.InvalidAttributeValueException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
 
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
  private final boolean HAS_HRDASH = false;
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final boolean IS_MANAGER = false;

  /** TYPE Property|Constant - Represents|Indicates... */
  private final List<String> VALID_REPORTEE_LIST = Arrays.asList("a");
  
  @InjectMocks
  private EmployeeProfile employeeProfile, employeeProfileEmpty;
    
  /**
   * Setup method that runs once before each test method.
   * @throws InvalidAttributeValueException 
   * 
   */
  @Before
  public void setup() throws InvalidAttributeValueException
  {
   employeeProfileEmpty = new EmployeeProfile();
   employeeProfile = new EmployeeProfile(VALID_EMPLOYEE_ID,VALID_GUID,VALID_NAME,VALID_NAME,VALID_EMAIL_ADDRESS,VALID_USERNAME,VALID_COMPANY,VALID_SECTOR_SUPERSECTOR,VALID_SECTOR_SUPERSECTOR,VALID_DEPARTMENT,VALID_DEPARTMENT,IS_MANAGER,HAS_HRDASH,VALID_REPORTEE_LIST);
   initMocks(this);
  }
  
  /**
   * Unit test for the setEmailAddress method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmailAddressWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setEmailAddress(null);
  }
  
  /**
   * Unit test for the setEmailAddress method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmailAddressWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setEmailAddress("");
  }
  
  /**
   * Unit test for the setEmailAddress method : invalid email address.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmailAddressWithInvalidEmailAddress() throws InvalidAttributeValueException
  {  
    employeeProfile.setEmailAddress(INVALID_EMAIL_ADDRESS);
  }
  
  /**
   * Unit test for the setEmailAddress method : valid email address.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetEmailAddressWithValidEmailAddress() throws InvalidAttributeValueException
  {  
    employeeProfile.setEmailAddress(VALID_EMAIL_ADDRESS);
    assertEquals(employeeProfile.getEmailAddress(),VALID_EMAIL_ADDRESS);
  }
  
  /**
   * Unit test for the setEmployeeID method : invalid employee id.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetEmployeeIDWithInvalidEmployeeID() throws InvalidAttributeValueException
  {  
    employeeProfile.setEmployeeID(INVALID_EMPLOYEE_ID);
  }
  
  /**
   * Unit test for the setEmailAddress method : valid email address.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetEmployeeIDWithValidEmployeeID() throws InvalidAttributeValueException
  {  
    employeeProfile.setEmployeeID(VALID_EMPLOYEE_ID);
    assertEquals(employeeProfile.getEmployeeID(),VALID_EMPLOYEE_ID);
  }

  /**
   * Unit test for the setUsername method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetUsernameWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setUsername(null);
  }
  
  /**
   * Unit test for the setUsername method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetUsernamesWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setUsername("");
  }
  
  /**
   * Unit test for the setUsername method : invalid username.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetUsernameWithInvalidEmployeeID() throws InvalidAttributeValueException
  {  
    employeeProfile.setUsername(INVALID_USERNAME);
  }
  
  /**
   * Unit test for the setUsername method : valid username.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetUsernameWithValidEmployeeID() throws InvalidAttributeValueException
  {  
    employeeProfile.setUsername(VALID_USERNAME);
    assertEquals(employeeProfile.getUsername(),VALID_USERNAME);
  }
  
  /**
   * Unit test for the setSurname method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSurnameWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setSurname(null);
  }
  
  /**
   * Unit test for the setSurname method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSurnameWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setSurname("");
  }
  
  /**
   * Unit test for the setSurname method : invalid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSurnameWithInvalidName() throws InvalidAttributeValueException
  {  
    employeeProfile.setSurname(INVALID_NAME);
  }
  
  /**
   * Unit test for the setSurname method : valid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSurnameWithValidName() throws InvalidAttributeValueException
  {  
    employeeProfile.setSurname(VALID_NAME);
    assertEquals(employeeProfile.getSurname(),VALID_NAME);
  }
  
  /**
   * Unit test for the setForename method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetForenameWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setForename(null);
  }
  
  /**
   * Unit test for the setForename method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetForenameWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setForename("");
  }
  
  /**
   * Unit test for the setForename method : invalid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetForenameWithInvalidName() throws InvalidAttributeValueException
  {  
    employeeProfile.setForename(INVALID_NAME);
  }
  
  /**
   * Unit test for the setForename method : valid name.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetForenameWithValidName() throws InvalidAttributeValueException
  {  
    employeeProfile.setForename(VALID_NAME);
    assertEquals(employeeProfile.getForename(),VALID_NAME);
  }
  
  /**
   * Unit test for the isManager method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetIsManager() throws InvalidAttributeValueException
  {  
    employeeProfile.setIsManager(true);
    assertEquals(employeeProfile.getIsManager(),true);
  }
  
  /**
   * Unit test for the setHasHRDash method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetHasHRDash() throws InvalidAttributeValueException
  {  
    employeeProfile.setHasHRDash(true);
    assertEquals(employeeProfile.getHasHRDash(),true);
  }
  
  /**
   * Unit test for the setReporteeCNs method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetReporteeCNs() throws InvalidAttributeValueException
  {
    employeeProfile.setReporteeCNs(VALID_REPORTEE_LIST);
    assertEquals(employeeProfile.getReporteeCNs(),VALID_REPORTEE_LIST);
  }
  
  /**
   * Unit test for the setReporteeCNs method with null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetReporteeCNsWithNull() throws InvalidAttributeValueException
  {
    employeeProfile.setReporteeCNs(null);
  }
  
  /**
   * Unit test for the setCompany method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetCompanyWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setCompany(null);
  }
   
  /**
   * Unit test for the setCompany method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetCompanyWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setCompany("");
  }
  
  /**
   * Unit test for the setCompany method : invalid company.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetCompanyWithInvalidCompany() throws InvalidAttributeValueException
  {  
    employeeProfile.setCompany(INVALID_COMPANY);
  }
  
  /**
   * Unit test for the setCompany method : valid company.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetCompanyWithValidCompany() throws InvalidAttributeValueException
  {  
    employeeProfile.setCompany(VALID_COMPANY);
    assertEquals(employeeProfile.getCompany(),VALID_COMPANY);
  }
  
  /**
   * Unit test for the setSopraDepartment method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSopraDepartmentWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setSopraDepartment(null);
  }
   
  /**
   * Unit test for the setSopraDepartment method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSopraDepartmentWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setSopraDepartment("");
  }
  
  /**
   * Unit test for the setSopraDepartment method : invalid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSopraDepartmentWithInvalidCompany() throws InvalidAttributeValueException
  {  
    employeeProfile.setSopraDepartment(INVALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSopraDepartment method : valid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSopraDepartmentWithValidCompany() throws InvalidAttributeValueException
  {  
    employeeProfile.setSopraDepartment(VALID_DEPARTMENT);
    assertEquals(employeeProfile.getSopraDepartment(),VALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSteriaDepartment method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSteriaDepartmentWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setSteriaDepartment(null);
  }
   
  /**
   * Unit test for the setSteriaDepartment method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSteriaDepartmentWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setSteriaDepartment("");
  }
  
  /**
   * Unit test for the setSteriaDepartment method : invalid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSteriaDepartmentWithInvalidDepartment() throws InvalidAttributeValueException
  {  
    employeeProfile.setSteriaDepartment(INVALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSteriaDepartment method : valid Department.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSteriaDepartmentWithValidDepartment() throws InvalidAttributeValueException
  {  
    employeeProfile.setSteriaDepartment(VALID_DEPARTMENT);
    assertEquals(employeeProfile.getSteriaDepartment(),VALID_DEPARTMENT);
  }
  
  /**
   * Unit test for the setSector method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSectorWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setSector(null);
  }
   
  /**
   * Unit test for the setSector method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSectorWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setSector("");
  }
  
  /**
   * Unit test for the setSector method : invalid Sector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSectorWithInvalidSector() throws InvalidAttributeValueException
  {  
    employeeProfile.setSector(INVALID_SECTOR);
  }
  
  /**
   * Unit test for the setSector method : valid Sector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSectorWithValidSector() throws InvalidAttributeValueException
  {  
    employeeProfile.setSector(VALID_SECTOR_SUPERSECTOR);
    assertEquals(employeeProfile.getSector(),VALID_SECTOR_SUPERSECTOR);
  }
  
  /**
   * Unit test for the setSuperSector method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSuperSectorWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setSuperSector(null);
  }
   
  /**
   * Unit test for the setSuperSector method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSuperSectorWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setSuperSector("");
  }
  
  /**
   * Unit test for the setSuperSector method : invalid SuperSector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetSuperSectorWithInvalidSuperSector() throws InvalidAttributeValueException
  {  
    employeeProfile.setSuperSector(INVALID_SUPERSECTOR);
  }
  
  /**
   * Unit test for the setSuperSector method : valid SuperSector.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetSuperSectorWithValidSuperSector() throws InvalidAttributeValueException
  {  
    employeeProfile.setSuperSector(VALID_SECTOR_SUPERSECTOR);
    assertEquals(employeeProfile.getSuperSector(),VALID_SECTOR_SUPERSECTOR);
  }
  
  /**
   * Unit test for the setGUID method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetGUIDWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setGUID(null);
  }
  
  /**
   * Unit test for the setGUID method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetGUIDWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setGUID("");
  }
  
  /**
   * Unit test for the setGUID method : valid GUID.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetGUIDWithValidGUID() throws InvalidAttributeValueException
  {  
    employeeProfile.setGUID(VALID_GUID);
    assertEquals(employeeProfile.getGUID(),VALID_GUID);
  }
  
  /**
   * Unit test for the toGson method.
   * 
   */
  @Test
  public void testToGson()
  {
    Gson gsonData = new Gson();
    assertEquals(employeeProfile.toGson(),gsonData.toJson(employeeProfile));
  }
  
  /**
   * Unit test for the getFullName method.
   * 
   * @throws InvalidAttributeValueException
   */  
  @Test
  public void testGetFullName() throws InvalidAttributeValueException
  {  
    employeeProfile.setForename("a");
    employeeProfile.setSurname("b");
    assertEquals(employeeProfile.getFullName(),"a B");
  }
  
  /**
   * Unit test for the addReportee method : Null.
   * 
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testAddReporteeWithNull() throws InvalidAttributeValueException
  {
    employeeProfile.addReportee(null);
  }
  
  /**
   * Unit test for the addReportee method : empty string.
   * 
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testAddReporteeWithEmptyString() throws InvalidAttributeValueException
  {
    employeeProfile.addReportee("");
  }
  
  /**
   * Unit test for the addReportee method : single element string.
   * 
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testAddReporteeWithSingleElementString() throws InvalidAttributeValueException
  {
    employeeProfile.addReportee("1");
  }
  
  /**
   * Unit test for the addReportee method : valid string.
   * 
   */
  @Test
  public void testAddReporteeWithValidString() throws InvalidAttributeValueException
  {
    employeeProfile.addReportee("Alex");
  }
  
  /**
   * Unit test for the equals method : same object.
   * 
   */
  @Test
  public void testEqualsSameObject() throws InvalidAttributeValueException
  {
    assertEquals(employeeProfile.equals(employeeProfile),true);
  }
  
  /**
   * Unit test for the equals method : null.
   * 
   */
  @Test
  public void testEqualsNull() throws InvalidAttributeValueException
  {
    assertEquals(employeeProfile.equals(null),false);
  }
  
  /**
   * Unit test for the equals method : different classes.
   * 
   */
  @Test
  public void testEqualsDifferentClasses() throws InvalidAttributeValueException
  {
    String a="String class";
    assertEquals(employeeProfile.equals(a),false);
  }
  
  /**
   * Unit test for the equals method : different classes.
   * 
   */
  @Test
  public void testEqualsDifferentFields() throws InvalidAttributeValueException
  {
    String a="String class";
    EmployeeProfile employeeProfile2 = new EmployeeProfile();
    assertEquals(employeeProfile.equals(employeeProfile2),false);
  }

  /**
   * Unit test for the toString method.
   * 
   */
  @Test
  public void testToString()
  {
    assertEquals(employeeProfile.toString() , "EmployeeProfile [employeeID=" + employeeProfile.getEmployeeID() + ", surname=" + employeeProfile.getSurname() + ", forename=" + employeeProfile.getForename()
    + ", username=" + employeeProfile.getUsername() + ", emailAddress=" + employeeProfile.getEmailAddress() + ", isManager=" + employeeProfile.getIsManager() + ", GUID=" + employeeProfile.getGUID()
    + ", company=" + employeeProfile.getCompany() + ", sopraDepartment=" + employeeProfile.getSopraDepartment() + ", steriaDepartment="
    + employeeProfile.getSteriaDepartment() + ", sector=" + employeeProfile.getSector() + ", superSector=" + employeeProfile.getSuperSector() + ", reporteeCNs=" + employeeProfile.getReporteeCNs()
    + "]" );
  }
}
