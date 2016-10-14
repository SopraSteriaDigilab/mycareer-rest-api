package services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value="/", method=RequestMethod.GET)
	public ResponseEntity<?> welcomePage(){
		return ResponseEntity.ok("Welcome to the MyCareer Project");
	}
	
	@RequestMapping(value="/getObjectives/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getObjectives(@PathVariable int employeeID){
		if(employeeID>0)
			return ResponseEntity.ok(EmployeeDAO.getObjectivesForUser(employeeID));
		else
			return ResponseEntity.badRequest().body("The given ID is invalid");
	}
	
	@RequestMapping(value="/getFeedback/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getFeedback(@PathVariable int employeeID){
		if(employeeID>0)
			try{
				error here,  not returning the actual error
			return ResponseEntity.ok(EmployeeDAO.getFeedbackForUser(employeeID));
			}catch(Exception e){
				return ResponseEntity.badRequest().body(e);
			}
		else
			return ResponseEntity.badRequest().body("The given ID is invalid");
		
	}
	
	/**
	 * 
	 * @param employeeID A value >0
	 * @param title a string that doesn't exceed 150 characters
	 * @param description a string that doesn't exceed 1000 characters
	 * @param completedBy a valid month and year in the following format: yyyy-MM
	 * @param progress a value between 0 and 2
	 * @return
	 */
	@RequestMapping(value="/addObjective/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addObjectiveToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="completedBy") String completedBy,
			@RequestParam(value="progress") int progress){
		//Validate the data
		//boolean val=DataComingInValidator.validateAddObjective(employeeID,title,description,completedBy,progress);
		//if(val){
		try{
			Objective obj=new Objective(progress,0,title,description,completedBy);
			return ResponseEntity.ok(EmployeeDAO.insertNewObjective(employeeID,obj));
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e);
		}
	}
	
	
	
}
