package services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dataStructure.Objective;
import functionalities.EmployeeDAO;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains all the available roots of the web service
 *
 */
@CrossOrigin(origins = "*")
@RestController
public class AppController {

//	@RequestMapping(value="/", method=RequestMethod.GET)
//	public void welcomePage(){
//		//return ResponseEntity.ok("Welcome to the MyCareer Project");
//		EmployeeDAO.insertTempData();
//		EmployeeDAO.getData();
//	}
	
	@RequestMapping(value="/getObjectives/{employeeID}", method=RequestMethod.GET)
	public List<Objective> getObjectives(@PathVariable int employeeID){

		return EmployeeDAO.getObjectivesForUser(employeeID);
		
	}
	
	
	
}
