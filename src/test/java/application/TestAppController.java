package application;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mongodb.morphia.Datastore;
import org.springframework.http.ResponseEntity;

import dataStructure.Employee;
import dataStructure.Objective;
import services.EmployeeDAO;




public class TestAppController {
	
	private AppController controller = new AppController();
	
	private static final long VALID_EMPLOYEE_ID = 675590;
	
	
    @Test
    public void testWelcomePage() {
    	ResponseEntity<String> expected = ResponseEntity.ok("Welcome to the MyCareer Project");
    	assertEquals(expected, controller.welcomePage());
    }

	
    
//	@Test
//	public void testGetObjectives() throws InvalidAttributeValueException {
//		Datastore dbConnection = mock(Datastore.class);
//		when(dbConnection.createQuery(Mockito.any()).filter(Mockito.anyString(),Mockito.anyString()).get()).thenReturn(new ArrayList<Objective>());
//		EmployeeDAO.getObjectivesForUser(VALID_EMPLOYEE_ID);
//    }
	
}
