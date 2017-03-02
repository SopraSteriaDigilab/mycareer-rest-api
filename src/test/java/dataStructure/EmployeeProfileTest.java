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
  private final String GUID = "guid";
  
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
  private final String VALID_TEAM = "valid team";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final String INVALID_TEAM = "astringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150charactersastringwithmorethan150characters";
  
  /** TYPE Property|Constant - Represents|Indicates... */
  private final boolean IS_MANAGER = false;

  /** TYPE Property|Constant - Represents|Indicates... */
  private final List<String> VALID_REPORTEE_LIST = Arrays.asList("a", "b");
  
  @InjectMocks
  private EmployeeProfile_NEW employeeProfile;
    
  /**
   * Setup method that runs once before each test method.
   * @throws InvalidAttributeValueException 
   * 
   */
  @Before
  public void setup() throws InvalidAttributeValueException
  {
   employeeProfile = new EmployeeProfile_NEW(VALID_EMPLOYEE_ID,GUID,VALID_NAME,VALID_NAME,VALID_EMAIL_ADDRESS,VALID_USERNAME,VALID_COMPANY,VALID_TEAM,IS_MANAGER,VALID_REPORTEE_LIST);
   initMocks(this);
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
  public void testSetTeamWithValidCompany() throws InvalidAttributeValueException
  {  
    employeeProfile.setCompany(VALID_COMPANY);
    assertEquals(employeeProfile.getCompany(),VALID_COMPANY);
  }
  
  /**
   * Unit test for the setTeam method : null.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTeamWithNull() throws InvalidAttributeValueException
  {  
    employeeProfile.setTeam(null);
  }
   
  /**
   * Unit test for the setTeam method : empty list.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTeamWithEmptyString() throws InvalidAttributeValueException
  {  
    employeeProfile.setTeam("");
  }
  
  /**
   * Unit test for the setTeam method : invalid team.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test(expected= InvalidAttributeValueException.class)
  public void testSetTeamWithInvalidTeam() throws InvalidAttributeValueException
  {  
    employeeProfile.setTeam(INVALID_TEAM);
  }
  
  /**
   * Unit test for the setTeam method : valid team.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testSetTeamWithValidTeam() throws InvalidAttributeValueException
  {  
    employeeProfile.setTeam(VALID_TEAM);
    assertEquals(employeeProfile.getTeam(),VALID_TEAM);
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
   * Unit test for the getFullName method.
   * 
   * @throws InvalidAttributeValueException
   */
  @Test
  public void testGetFullName() throws InvalidAttributeValueException
  {  
    employeeProfile.setForename("a");
    employeeProfile.setSurname("b");
    assertEquals(employeeProfile.getFullName(),"a b");
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
}
