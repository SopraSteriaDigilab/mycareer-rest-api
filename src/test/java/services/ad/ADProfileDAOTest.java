package services.ad;

import javax.naming.directory.DirContext;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO!
public class ADProfileDAOTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ADProfileDAOTest.class);
	
	/** Long Constant - Represents a valid employee ID. */
	  private final long VALID_EMPLOYEE_ID = 676783;

	  /** DirContext Property - Mocked by Mockito. */
	  @Mock
	  private static DirContext ldapContext;
	  
	  /** DirContext Property - Mocked by Mockito. */
	  @Mock
	  private static DirContext ldapSteriaContext;
	  /** EmployeeDAO Property - Mocked by Mockito. */

	  /** ProcessComponentsImpl Property - Represents the unit under test. */
	  @InjectMocks
	  private ADProfileDAO unitUnderTest;
}
