package services;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import application.Application;
import services.db.MongoOperations;
import services.db.MorphiaOperations;

@Ignore
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class })
public class EmployeeProfileServiceTest2 {

	@MockBean
	private MorphiaOperations mockMorphiaOperations;

	@MockBean(name = "employeeOperations")
	private MongoOperations mockEmployeeOperations;
	
	@MockBean(name = "competenciesHistoriesOperations")
	private MongoOperations mockCompetenciesHistoriesOperations;
	
	@MockBean(name = "developmentNeedsHistoriesOperations")
	private MongoOperations mockDevelopmentNeedsHistoriesOperations;
	
	@MockBean(name = "objectivesHistoriesOperations")
	private MongoOperations mockObjectivesHistoriesOperations;
	
	@InjectMocks
	private EmployeeProfileService serviceUnderTest;

	@Before
	public void setup() throws Exception {
		initMocks(this);
	}

	@Test
	public void nonsense() {
		assertTrue(true);
	}
	
	@Test
	public void testFetchEmployeeProfile(){
		
	}
	
	

}
