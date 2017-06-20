package dataStructure;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import model.Models;

public class EmployeeProfileTest {
	/** TYPE Property|Constant - Represents|Indicates... */
	private final long VALID_EMPLOYEE_ID = 675590;

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_NAME = "b";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_USERNAME = "abrard";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_COMPANY = "sopra steria";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_SECTOR_SUPERSECTOR = "sector";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final String VALID_DEPARTMENT = "department";

	/** TYPE Property|Constant - Represents|Indicates... */
	private final List<String> VALID_REPORTEE_LIST = Arrays.asList("a");

	@InjectMocks
	private EmployeeProfile unitUnderTest, unitUnderTestEmpty;

	/**
	 * Setup method that runs once before each test method.
	 * 
	 * @throws InvalidAttributeValueException
	 * 
	 */
	@Before
	public void setup() throws InvalidAttributeValueException {
		unitUnderTestEmpty = new EmployeeProfile();
		unitUnderTest = Models.getProfile();
		initMocks(this);
	}

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

		setPrivateField("company", obj, null);
		setPrivateField("emailAddresses", obj, null);
		setPrivateField("employeeID", obj, 0);
		setPrivateField("employeeType",obj,null);
		setPrivateField("forename", obj, null);
		setPrivateField("reporteeCNs", obj, null);
		setPrivateField("isManager", obj, true);
		setPrivateField("hasHRDash", obj, true);
		setPrivateField("sector", obj, null);
		setPrivateField("superSector", obj, null);
		setPrivateField("steriaDepartment", obj, null);
		setPrivateField("reporteeCNs", obj, null);
		setPrivateField("username", obj, null);
		setPrivateField("surname", obj, null);
		setPrivateField("accountExpires",obj,null);
	}

	/**
	 * Unit test for the setEmailAddress method : valid email address.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetEmailAddressWithValidEmailAddress() throws InvalidAttributeValueException {
		assertEquals(unitUnderTest.getEmailAddresses(), Models.getEmailAddresses());
	}

	/**
	 * Unit test for the setEmailAddress method : valid email address.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetEmployeeIDWithValidEmployeeID() throws InvalidAttributeValueException {
		unitUnderTest.setEmployeeID(VALID_EMPLOYEE_ID);
		assertEquals(unitUnderTest.getEmployeeID(), VALID_EMPLOYEE_ID);
	}

	/**
	 * Unit test for the setUsername method : valid username.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetUsernameWithValidEmployeeID() throws InvalidAttributeValueException {
		unitUnderTest.setUsername(VALID_USERNAME);
		assertEquals(unitUnderTest.getUsername(), VALID_USERNAME);
	}

	/**
	 * Unit test for the setSurname method : valid name.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetSurnameWithValidName() throws InvalidAttributeValueException {
		unitUnderTest.setSurname("A");
		assertEquals(unitUnderTest.getSurname(), "A");
	}

	/**
	 * Unit test for the setForename method : valid name.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetForenameWithValidName() throws InvalidAttributeValueException {
		unitUnderTest.setForename(VALID_NAME);
		assertEquals(unitUnderTest.getForename(), VALID_NAME);
	}

	/**
	 * Unit test for the isManager method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetIsManager() throws InvalidAttributeValueException {
		unitUnderTest.setIsManager(true);
		assertEquals(unitUnderTest.getIsManager(), true);
	}

	/**
	 * Unit test for the setHasHRDash method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetHasHRDash() throws InvalidAttributeValueException {
		unitUnderTest.setHasHRDash(true);
		assertEquals(unitUnderTest.getHasHRDash(), true);
	}

	/**
	 * Unit test for the setReporteeCNs method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetReporteeCNs() throws InvalidAttributeValueException {
		unitUnderTest.setReporteeCNs(VALID_REPORTEE_LIST);
		assertEquals(unitUnderTest.getReporteeCNs(), VALID_REPORTEE_LIST);
	}

	/**
	 * Unit test for the setCompany method : valid company.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetCompanyWithValidCompany() throws InvalidAttributeValueException {
		unitUnderTest.setCompany(VALID_COMPANY);
		assertEquals(unitUnderTest.getCompany(), VALID_COMPANY);
	}

	/**
	 * Unit test for the setSteriaDepartment method : valid Department.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetSteriaDepartmentWithValidDepartment() throws InvalidAttributeValueException {
		unitUnderTest.setSteriaDepartment(VALID_DEPARTMENT);
		assertEquals(unitUnderTest.getSteriaDepartment(), VALID_DEPARTMENT);
	}

	/**
	 * Unit test for the setSector method : valid Sector.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetSectorWithValidSector() throws InvalidAttributeValueException {
		unitUnderTest.setSector(VALID_SECTOR_SUPERSECTOR);
		assertEquals(unitUnderTest.getSector(), VALID_SECTOR_SUPERSECTOR);
	}

	/**
	 * Unit test for the setSuperSector method : valid SuperSector.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testSetSuperSectorWithValidSuperSector() throws InvalidAttributeValueException {
		unitUnderTest.setSuperSector(VALID_SECTOR_SUPERSECTOR);
		assertEquals(unitUnderTest.getSuperSector(), VALID_SECTOR_SUPERSECTOR);
	}

	/**
	 * Unit test for the getFullName method.
	 * 
	 * @throws InvalidAttributeValueException
	 */
	@Test
	public void testGetFullName() throws InvalidAttributeValueException {
		unitUnderTest.setForename("a");
		unitUnderTest.setSurname("b");
		assertEquals(unitUnderTest.getFullName(), "a b");
	}

	/**
	 * Unit test for the equals method : same object.
	 * 
	 */
	@Test
	public void testEqualsSameObject() throws InvalidAttributeValueException {
		assertEquals(unitUnderTest.equals(unitUnderTest), true);
	}

	/**
	 * Unit test for the equals method : null.
	 * 
	 */
	@Test
	public void testEqualsNull() throws InvalidAttributeValueException {
		assertEquals(unitUnderTest.equals(null), false);
	}

	/**
	 * Unit test for the equals method : different classes.
	 * 
	 */
	@Test
	public void testEqualsDifferentClasses() throws InvalidAttributeValueException {
		String a = "String class";
		assertEquals(unitUnderTest.equals(a), false);
	}

	/**
	 * Unit test for the equals method : null fields for both classes.
	 * 
	 */
	@Test
	public void testEqualsNullFieldsForBothClasses() throws InvalidAttributeValueException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		EmployeeProfile employeeProfile = new EmployeeProfile();
		setEveryPrivateFieldsToNullOrDefault(unitUnderTest);
		setEveryPrivateFieldsToNullOrDefault(employeeProfile);
		assertEquals(unitUnderTest.equals(employeeProfile), true);
	}

	/**
	 * Unit test for the equals method :null field.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * 
	 */
	@Test
	public void testEqualsOneOfTheTwoClassesHasANullField() throws InvalidAttributeValueException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		EmployeeProfile employeeProfile = new EmployeeProfile();

		setEveryPrivateFieldsToNullOrDefault(unitUnderTest);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("company", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("emailAddresses", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("employeeID", employeeProfile, 0);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("forename", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("hasHRDash", employeeProfile, true);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("isManager", employeeProfile, true);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("sector", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("steriaDepartment", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("superSector", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("surname", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("username", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("reporteeCNs", employeeProfile, null);
		assertEquals(unitUnderTest.equals(employeeProfile), true);
	}

	/**
	 * Unit test for the equals method :null field.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * 
	 */
	@Test
	public void testEqualsFieldNotNullButDifferentValues() throws InvalidAttributeValueException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		EmployeeProfile employeeProfile = new EmployeeProfile();

		assertEquals(unitUnderTest.equals(employeeProfile), false);

		employeeProfile.setCompany(unitUnderTest.getCompany());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		employeeProfile.setEmailAddresses(unitUnderTest.getEmailAddresses());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		employeeProfile.setEmployeeID(unitUnderTest.getEmployeeID());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		employeeProfile.setForename(unitUnderTest.getForename());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		employeeProfile.setHasHRDash(unitUnderTest.getHasHRDash());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		employeeProfile.setIsManager(unitUnderTest.getIsManager());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		employeeProfile.setReporteeCNs(unitUnderTest.getReporteeCNs());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("sector", employeeProfile, unitUnderTest.getSector());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("steriaDepartment", employeeProfile, unitUnderTest.getSteriaDepartment());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("superSector", employeeProfile, unitUnderTest.getSuperSector());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("surname", employeeProfile, unitUnderTest.getSurname());
		assertEquals(unitUnderTest.equals(employeeProfile), false);

		setPrivateField("username", employeeProfile, unitUnderTest.getUsername());
		assertEquals(unitUnderTest.equals(employeeProfile), false);
	}
	
	/**
	 * Unit test for differences method
	 * 
	 * */
	 
	@Test
	public void differencesExistTest(){

		EmployeeProfile differentEmployeeProfile= Models.getProfile();
		
		differentEmployeeProfile.setEmployeeID(123456);
		
		Document expectedDocument=new Document().append("profile.employeeID", 123456l);
		
		Document actualDocument=unitUnderTest.differences(differentEmployeeProfile);
				
		assertEquals(expectedDocument, actualDocument);
	}	
	
	@Test
	public void noDifferencesTest(){
		EmployeeProfile differentEmployeeProfile= Models.getProfile();
		
		Document expectedDocument=new Document();
		
		Document actualDocument=unitUnderTest.differences(differentEmployeeProfile);
		
		assertEquals(expectedDocument, actualDocument);
	}
	

	/**
	 * Unit test for the toString method.
	 * 
	 */
	@Test
	public void testToString() {
		assertEquals(unitUnderTest.toString(), "EmployeeProfile [employeeID=" + unitUnderTest.getEmployeeID()
				+ ", employeeType=" + unitUnderTest.getEmployeeType() + ", surname=" + unitUnderTest.getSurname()
				+ ", forename=" + unitUnderTest.getForename() + ", username=" + unitUnderTest.getUsername()
				+ ", emailAddresses=" + unitUnderTest.getEmailAddresses() + ", isManager="
				+ unitUnderTest.getIsManager() + ", hasHRDash=" + unitUnderTest.getHasHRDash() + ", company="
				+ unitUnderTest.getCompany() + ", steriaDepartment=" + unitUnderTest.getSteriaDepartment() + ", sector="
				+ unitUnderTest.getSector() + ", superSector=" + unitUnderTest.getSuperSector() + ", reporteeCNs="
				+ unitUnderTest.getReporteeCNs() + ", accountExpires=" + unitUnderTest.getAccountExpires() + "]");
	}
}