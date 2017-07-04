package services;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import application.Application;
import services.db.MongoOperations;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class })
public class DataServiceIntTest {



	@MockBean(name = "employeeOperations")
	private MongoOperations mockEmployeeOperations;
	
	
	@InjectMocks
	private DataService serviceUnderTest;

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
