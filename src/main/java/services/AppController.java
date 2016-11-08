package services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.MongoException;

import dataStructure.Competency;
import dataStructure.DevelopmentNeed;
import dataStructure.Note;
import dataStructure.Objective;
import emailServices.SMTPService;
import functionalities.EmployeeDAO;

/**
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
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

	/**
	 * 
	 * This method allows the front-end to retrieve the latest version for each objective related to a specific user
	 * 
	 * @param employeeID the employee ID (>0)
	 * @return the list of objectives (only the latest version of them)
	 */
	@RequestMapping(value="/getObjectives/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getObjectives(@PathVariable int employeeID){
		if(employeeID>0)
			try {
				//Retrieve and return the objectives from the system
				return ResponseEntity.ok(EmployeeDAO.getObjectivesForUser(employeeID));
			}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		else
			return ResponseEntity.badRequest().body("The given ID is invalid");
	}

	/**
	 * 
	 * This method allows the front-end to retrieve the latest version of each feedback related to a specific user
	 * 
	 * @param employeeID the employee ID (>0)
	 * @return list of feedback (only the latest version of them)
	 */
	@RequestMapping(value="/getFeedback/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getFeedback(@PathVariable int employeeID){
		if(employeeID>0)
			try{
				return ResponseEntity.ok(EmployeeDAO.getFeedbackForUser(employeeID));
			}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		else
			return ResponseEntity.badRequest().body("The given ID is invalid");
	}
	
	/**
	 * 
	 * This method allows the front-end to retrieve all the notes associated to a specific user
	 * 
	 * @param employeeID the ID of an employee
	 * @return list of notes (only the latest version for each of them)
	 */
	@RequestMapping(value="/getNotes/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getNotes(@PathVariable int employeeID){
		if(employeeID>0)
			try{
				return ResponseEntity.ok(EmployeeDAO.getNotesForUser(employeeID));
			}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		else
			return ResponseEntity.badRequest().body("The given ID is invalid");
	}
	
	/**
	 * 
	 * This method allows the front-end to retrieve all the development needs associated to a user
	 * 
	 * @param employeeID the ID of an employee
	 * @return list of development needs (only latest version for each one of them)
	 */
	@RequestMapping(value="/getDevelopmentNeeds/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getDevelomentNeeds(@PathVariable int employeeID){
		if(employeeID>0)
			try{
				return ResponseEntity.ok(EmployeeDAO.getDevelopmentNeedsForUser(employeeID));
			}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		else
			return ResponseEntity.badRequest().body("The given ID is invalid");
	}

	@RequestMapping(value="/getCompetencies/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getCompetencies(
			@PathVariable("employeeID") int employeeID){
		try{
			return ResponseEntity.ok(EmployeeDAO.getCompetenciesForUser(employeeID));
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * 
	 * This method allows the front-end to add a new objective to a user
	 * 
	 * @param employeeID A value >0
	 * @param title a string that doesn't exceed 150 characters
	 * @param description a string that doesn't exceed 1000 characters
	 * @param completedBy a valid month and year in the following format: yyyy-MM
	 * @param progress a value between -1 and 2
	 * 	-1 => Not Relevant to my career anymore
	 *  0 => Awaiting
	 *  1 => In Flight
	 *  2 => Done
	 * @return a boolean value or an error
	 */
	@RequestMapping(value="/addObjective/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addObjectiveToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="completedBy") String completedBy){
		try{
			Objective obj=new Objective(0,0,title,description,completedBy);
			boolean inserted=EmployeeDAO.insertNewObjective(employeeID,obj);
			if(inserted)
				return ResponseEntity.ok("Objective inserted correctly!");
			else
				return ResponseEntity.badRequest().body("Error while adding the objective");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * 
	 * This method allows the front-end to edit a new version of an objective currently stored within the system
	 * 
	 * @param employeeID the employee ID 
	 * @param objectiveID the ID of the objective (>0)
	 * @param title the title of the objective (< 150)
	 * @param description the description of the objective (< 3000)
	 * @param completedBy (string with format: yyyy-MM)
	 * @param progress a value between -1 and 2
	 * 	-1 => Not Relevant to my career anymore
	 *  0 => Awaiting
	 *  1 => In Flight
	 *  2 => Done
	 * @return a boolean value or an error
	 */
	@RequestMapping(value="/editObjective/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addNewVersionObjectiveToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="objectiveID") int objectiveID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="completedBy") String completedBy,
			@RequestParam(value="progress") int progress){
		try{
			Objective obj=new Objective(objectiveID,progress,0,title,description,completedBy);
			boolean inserted=EmployeeDAO.addNewVersionObjective(employeeID, objectiveID, obj);
			if(inserted)
				return ResponseEntity.ok("Objective modified correctly!");
			else
				return ResponseEntity.badRequest().body("Error while editing the objective");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 
	 * This method allows the front-end to add a new note to a specific user
	 * 
	 * @param employeeID the employee ID (>0)
	 * @param from the author of the note (<150)
	 * @param body the content of the note (<1000)
	 * @return a message explaining if the note has been added or if there was an error while completing the task
	 */
	@RequestMapping(value="/addNote/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addNoteToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="from") String from,
			@RequestParam(value="body") String body){
		try{
			Note obj=new Note(1,body,from);
			boolean inserted=EmployeeDAO.insertNewNote(employeeID,obj);
			if(inserted)
				return ResponseEntity.ok("Note inserted correctly!");
			else
				return ResponseEntity.badRequest().body("Error while adding the Note");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 
	 * This method allows the front-end to edit a new version of a note currently stored within the system
	 * 
	 * @param employeeID the employeeID (>0)
	 * @param noteID the ID of the note to edit (>0)
	 * @param from the author of the note (<150)
	 * @param body the content of the note (<1000)
	 * @return a message explaining if the note has been added or if there was an error while completing the task
	 */
	@RequestMapping(value="/editNote/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addNewVersionNoteToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="noteID") int noteID,
			@RequestParam(value="from") String from,
			@RequestParam(value="body") String body){
		try{
			Note obj=new Note(noteID,body,from);
			boolean inserted=EmployeeDAO.addNewVersionNote(employeeID, noteID, obj);
			if(inserted)
				return ResponseEntity.ok("Note modified correctly!");
			else
				return ResponseEntity.badRequest().body("Error while editing the Note");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 
	 * This method allows the front-end to insert a new development need in the system
	 * 
	 * @param employeeID the employee ID (>0)
	 * @param title title of the development need (<150)
	 * @param description content of the development need (<1000)
	 * @param timeToCompleteBy String containing a date with format yyyy-MM or empty ""
	 * @return a message explaining if the development need has been added or if there was an error while completing the task
	 */
	@RequestMapping(value="/addDevelopmentNeed/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addDevelopmentNeedToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="category") int cat,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="timeToCompleteBy") String timeToCompleteBy){
		try{
			DevelopmentNeed obj=new DevelopmentNeed(1,cat,title,description,timeToCompleteBy);
			boolean inserted=EmployeeDAO.insertNewDevelopmentNeed(employeeID,obj);
			if(inserted)
				return ResponseEntity.ok("Development need inserted correctly!");
			else
				return ResponseEntity.badRequest().body("Error while adding the Development need");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 
	 * This method allows the front-end to edit a development need previously inserted in the system
	 * 
	 * @param employeeID employeeID of the employee (>0)
	 * @param devNeedID ID of the development need to edit (>0)
	 * @param title title of the development need (<150>
	 * @param description content of the development need (<1000)
	 * @param timeToCompleteBy String containing a date with format yyyy-MM or empty ""
	 * @return a message explaineing if the development need has been added or if there was an error while completing the task
	 */
	@RequestMapping(value="/editDevelopmentNeed/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addNewVersionDevelopmentNeedToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="category") int cat,
			@RequestParam(value="devNeedID") int devNeedID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="timeToCompleteBy") String timeToCompleteBy){
		try{
			DevelopmentNeed obj=new DevelopmentNeed(devNeedID,cat,title,description,timeToCompleteBy);
			boolean inserted=EmployeeDAO.addNewVersionDevelopmentNeed(employeeID, devNeedID, obj);
			if(inserted)
				return ResponseEntity.ok("Development need modified correctly!");
			else
				return ResponseEntity.badRequest().body("Error while editing the Development need");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@RequestMapping(value="/generateFeedbackRequest/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> createFeedbackRequest(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="emailsTo") String toFields,
			@RequestParam(value="notes") String notes){
		try{
			//Split the email addresses from the toField into single elements
			String[] emailAddressesToField=toFields.split(",");
			for(int i=0; i<emailAddressesToField.length; i++){
				emailAddressesToField[i]=emailAddressesToField[i].trim();
			}
			boolean done=SMTPService.createFeedbackRequest(employeeID, notes, emailAddressesToField);
			if(done)
				return ResponseEntity.ok("Feedback request sent!");
			else
				return ResponseEntity.badRequest().body("Error while creating a feedback request!");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getFeedbackRequests/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getFeedbackRequests(
			@PathVariable("employeeID") int employeeID){
		try{
			return ResponseEntity.ok(EmployeeDAO.getFeedbackRequestsForUser(employeeID));
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 
	 * This method allows the front-end to insert new competencies in the system
	 * 
	 * @param employeeID the employee ID (>0)
	 * @param title title of the development need (<150)
	 * @return a message explaining if the competency has been updated or if there was an error while completing the task
	 */
	@RequestMapping(value="/updateCompetency/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addCompetenciesToAUser(
			@PathVariable("employeeID") int employeeID,
			@RequestParam(value="title") String title,
			@RequestParam(value="status") boolean status){
		try{
			Competency obj;
				obj=new Competency(1,status);
			boolean inserted=EmployeeDAO.addNewVersionCompetency(employeeID,obj,title);
			if(inserted)
				return ResponseEntity.ok("Competency inserted correctly!");
			else
				return ResponseEntity.badRequest().body("Error while adding the Competency");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
