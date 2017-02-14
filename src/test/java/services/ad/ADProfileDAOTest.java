package services.ad;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import static services.ad.ADProfileDAO.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dataStructure.ADProfile_Basic;

// TODO This doesn't work!
public class ADProfileDAOTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ADProfileDAOTest.class);
	
	/** Valid employee username */
	private final String VALID_EMPLOYEE_USERNAME = "mmehmet";
	
	/** Valid employee Sopra Steria email address */
	private final String VALID_SS_EMPLOYEE_EMAIL = "rnacef@soprasteria.com";
	
	/** Valid employee joint venture email address */
	private final String VALID_JV_EMPLOYEE_EMAIL = "rnacef@soprasteria.com";
	
	/** Invalid employee username */
	private final String INVALID_EMPLOYEE_USERNAME = "iminvalid";
	
	/** Invalid employee Sopra Steria email address */
	private final String INVALID_SS_EMPLOYEE_EMAIL = "iminvalid@soprasteria.com";
	
	/** Invalid employee joint venture email address */
	private final String INVALID_JV_EMPLOYEE_EMAIL = "iminvalid@soprasteria.com";
	
	/** External email address */
	private final String EXTERNAL_EMAIL = "m.i.mehmet@hotmail.co.uk";
	
	/** Invalid email address */
	private final String INVALID_EMAIL = "5non32existant86423@no.way.does.this.exist.co.uk";

	/** DirContext Property - Mocked by Mockito. */
	@Mock
	private static DirContext mockLdapContext;

	/** DirContext Property - Mocked by Mockito. */
	@Mock
	private static DirContext mockLdapSteriaContext;

	/** ADProfile_Basic Property - Mocked by Mockito. */
	@Mock
	private static ADProfile_Basic mockProfile;

	@Before
	public void setup() throws InvalidAttributeValueException, NamingException {
		initMocks(this);
		when(authenticateUserProfile(any())).thenReturn(mockProfile);
	}

	@Test
	public void authenticateUserProfileValidUsername() throws InvalidAttributeValueException, NamingException {
		assertEquals(mockProfile, authenticateUserProfile(VALID_EMPLOYEE_USERNAME));
	}

	@Test
	public void authenticateUserProfileValidSSEmail() throws InvalidAttributeValueException, NamingException {
		assertEquals(mockProfile, authenticateUserProfile(VALID_SS_EMPLOYEE_EMAIL));
	}
	
	@Test
	public void authenticateUserProfileValidJVEmail() throws InvalidAttributeValueException, NamingException {
		assertEquals(mockProfile, authenticateUserProfile(VALID_JV_EMPLOYEE_EMAIL));
	}
	
	@Test(expected = InvalidAttributeValueException.class)
	public void authenticateUserProfileNullArgument() throws InvalidAttributeValueException, NamingException {
		authenticateUserProfile(null);
	}
	
	@Test(expected = InvalidAttributeValueException.class)
	public void authenticateUserProfileEmptyString() throws InvalidAttributeValueException, NamingException {
		authenticateUserProfile("");
	}
}
