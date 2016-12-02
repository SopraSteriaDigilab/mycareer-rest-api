package services;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;
import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.MongoException;
import dataStructure.ADProfile_Basic;
import dataStructure.Competency;
import dataStructure.Constants;
import dataStructure.DevelopmentNeed;
import dataStructure.Note;
import dataStructure.Objective;
import emailServices.SMTPService;
import functionalities.ADProfileDAO;
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
		return ResponseEntity.ok("Welcome to the MyCareer Project :)");
	}

	/**
	 * 
	 * This method allows the front-end to retrieve the latest version for each objective related to a specific user
	 * 
	 * @param employeeID the employee ID (>0)
	 * @return the list of objectives (only the latest version of them)
	 */
	@RequestMapping(value="/getObjectives/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getObjectives(@PathVariable long employeeID){
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
	public ResponseEntity<?> getFeedback(@PathVariable long employeeID){
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
	public ResponseEntity<?> getNotes(@PathVariable long employeeID){
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
	public ResponseEntity<?> getDevelomentNeeds(@PathVariable long employeeID){
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

	/**
	 * 
	 * This method allows the front-end to retrieve all the competencies associated with a user
	 * 
	 * @param employeeID the ID of an employee
	 * @return list of competencies (only latest version for each one of them)
	 */
	@RequestMapping(value="/getCompetencies/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getCompetencies(
			@PathVariable("employeeID") long employeeID){
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
	 * This method allows the front-end to retrieve all the reportees associated with a user
	 * 
	 * @param employeeID the ID of an employee
	 * @return list of ADProfileBasics
	 */
	@RequestMapping(value="/getReportees/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getReportees(@PathVariable long employeeID){
		try{
			return ResponseEntity.ok(EmployeeDAO.getReporteesForUser(employeeID));
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@RequestMapping(value="/management/retrieveAllUser_Data/employee/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getAllUserData(@PathVariable long employeeID){
		if(employeeID>0)
			try{
				return ResponseEntity.ok(EmployeeDAO.getAllUserDataFromID(employeeID));
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
	 * @return a message explaining if the objective has been inserted or if there was an error while completing the task
	 */
	@RequestMapping(value="/addObjective/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addObjectiveToAUser(
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="completedBy") String completedBy,
			@RequestParam String proposedBy){
		try{
			Objective obj=new Objective(0,0,title,description,completedBy);
			obj.setProposedBy(proposedBy);
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
	 * 	-1 => Deleted
	 *  0 => Awaiting
	 *  1 => In Flight
	 *  2 => Done
	 * @return a message explaining if the objective has been updated or if there was an error while completing the task
	 */
	@RequestMapping(value="/editObjective/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addNewVersionObjectiveToAUser(
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="objectiveID") int objectiveID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="completedBy") String completedBy,
			@RequestParam(value="progress") int progress,
			@RequestParam String proposedBy){
		try{
			Objective obj=new Objective(objectiveID,progress,0,title,description,completedBy);
			obj.setProposedBy(proposedBy);
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
	 * This method allows the front-end to update the status of a objective. This corresponds to archiving or unarchiving an objective
	 * 
	 * @param employeeID The user ID
	 * @param objectiveID The objective ID to update
	 * @param isArchived boolean value (true=archive, false=unarchive)
	 * @return a message explaining if the objective has been updated or if there was an error while completing the task
	 */
	@RequestMapping(value="/changeStatusObjective/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> updateStatusUserObjective(
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="objectiveID") int objectiveID,
			@RequestParam(value="isArchived") boolean isArchived){
		try{
			//Retrieve the object with the given ID from the DB data
			Objective obj=EmployeeDAO.getSpecificObjectiveForUser(employeeID, objectiveID);
			if(obj.getIsArchived()==isArchived)
				return ResponseEntity.ok("The status of the objective has not changed!");
			//Create a new object which stores the data from the retrieved element but sets a new timestamp to it
			Objective newObjUpdated=new Objective(obj);			
			newObjUpdated.setIsArchived(isArchived); 
			//Store the new version to the system
			boolean inserted=EmployeeDAO.addNewVersionObjective(employeeID, objectiveID, newObjUpdated);
			if(inserted){
				if(isArchived)
					return ResponseEntity.ok("The objective has been archived!");
				else
					return ResponseEntity.ok("The objective has been unarchived!");
			}
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
			@PathVariable("employeeID") long employeeID,
			@RequestParam int noteType,
			@RequestParam int linkID,
			@RequestParam(value="from") String from,
			@RequestParam(value="body") String body){
		try{
			Note obj=new Note(1, noteType, linkID, body,from);
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
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="noteID") int noteID,
			@RequestParam int noteType,
			@RequestParam int linkID,
			@RequestParam(value="from") String from,
			@RequestParam(value="body") String body){
		try{
			Note obj=new Note(noteID, noteType, linkID, body,from);
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
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="category") int cat,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="timeToCompleteBy") String timeToCompleteBy){
		try{
			DevelopmentNeed obj=new DevelopmentNeed(1,0,cat,title,description,timeToCompleteBy);
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
	 * @return a message explaining if the development need has been added or if there was an error while completing the task
	 */
	@RequestMapping(value="/editDevelopmentNeed/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addNewVersionDevelopmentNeedToAUser(
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="category") int cat,
			@RequestParam(value="devNeedID") int devNeedID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="timeToCompleteBy") String timeToCompleteBy,
			@RequestParam int progress){
		try{
			DevelopmentNeed obj=new DevelopmentNeed(devNeedID,progress,cat,title,description,timeToCompleteBy);
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

	/**
	 * 
	 * @param employeeID the employee ID (>0)
	 * @param toFields an array containing the email addresses, separated by commas (max 20 elements)
	 * @param notes a string of max 1000 characters containing any additional notes to add to the feedback request email
	 * @return a message explaining whether the feedback request has been sent or if there was an error while completing the task
	 */
	@RequestMapping(value="/generateFeedbackRequest/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> createFeedbackRequest(
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="emailsTo") String toFields,
			@RequestParam(value="notes") String notes){
		try{
			//Split the email addresses from the toField into single elements
			String[] emailAddressesToField=toFields.split(",");
			for(int i=0; i<emailAddressesToField.length; i++){
				emailAddressesToField[i]=emailAddressesToField[i].trim();
			}
			if(emailAddressesToField[0].length()<1)
				return ResponseEntity.badRequest().body("No recipients inserted!");
			boolean done=SMTPService.createFeedbackRequest(employeeID, notes, emailAddressesToField);
			if(done)
				return ResponseEntity.ok("Feedback request sent! A Confirmation Email is on its way");
			else
				return ResponseEntity.badRequest().body("Error while creating a feedback request!");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * 
	 * @param employeeID the employee ID
	 * @return all the feedback requests created by the given user
	 */
	@RequestMapping(value="/getRequestedFeedback/{employeeID}", method=RequestMethod.GET)
	public ResponseEntity<?> getGroupFeedbackRequests(
			@PathVariable("employeeID") long employeeID){
		try{
			return ResponseEntity.ok(EmployeeDAO.getGroupFeedbackRequestsForUser(employeeID));
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
	 * @param title title of the competency (<200)
	 * @return a message explaining if the competency has been updated or if there was an error while completing the task
	 */
	@RequestMapping(value="/updateCompetency/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addCompetenciesToAUser(
			@PathVariable("employeeID") long employeeID,
			@RequestParam(value="title") String title,
			@RequestParam(value="status") boolean status){
		try{
			if(title==null || title.length()<1 || title.length()>200)
				return ResponseEntity.badRequest().body("The given title is invalid");
			int index=Constants.getCompetencyIDGivenTitle(title);
			if(index<0)
				return ResponseEntity.badRequest().body("The given title does not match any valid competency");
			Competency obj=new Competency(index,status);
			boolean inserted=EmployeeDAO.addNewVersionCompetency(employeeID,obj,title);
			if(inserted)
				return ResponseEntity.ok("Competency updated correctly!");
			else
				return ResponseEntity.badRequest().body("Error while updating the Competency");
		}
		catch(MongoException me){
			return ResponseEntity.badRequest().body("DataBase Connection Error");
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@RequestMapping(value="/authenticateUserProfile", method=RequestMethod.GET)
	public ResponseEntity<?> authenticateUserProfile(@RequestParam(value="userName_Email") String userName){
		try {
			if(userName!=null && !userName.equals("") && userName.length()<300 ){
				return ResponseEntity.ok(ADProfileDAO.authenticateUserProfile(userName));
			}else{
				return ResponseEntity.badRequest().body("The username given is invalid");
			}
		} catch (InvalidAttributeValueException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NamingException e){
//			return ResponseEntity.badRequest().body("AD Connection Error");
			return ResponseEntity.badRequest().body(e.toString());
		}	
	}
	
	
	/**
	 * 
	 * This method allows the front-end to propose a new objective for a list of users
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
	 * @return a message explaining if the objective has been inserted or if there was an error while completing the task
	 */
	@RequestMapping(value="/addProposedObjective/{employeeID}", method=RequestMethod.POST)
	public ResponseEntity<?> addProposedObjectiveToAUser(
			@PathVariable(value="employeeID") long employeeID,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="completedBy") String completedBy,
			@RequestParam(value="emails") String emails){
		String result = "Objective Proposed for: ";
		String errorResult = "Error: ";
		boolean errorInserting = false;
		boolean insertAccepted = false;
		Set<String> emailSet = new HashSet<>();
		try {

			//Check that input variables are not empty
			areInputValuesEmpty(title, description, completedBy);
			
			//Get email addresses and check they are not empty and limit to 20
			String[] emailAddresses=emails.split(",");
			if(emailAddresses.length >19){
				throw new InvalidAttributeValueException("There is a maximum of 20 allowed emails in one request.");
			}
			for(String email : emailAddresses){
				if(email.length() < 1){
					throw new InvalidAttributeValueException("One or more of the emails are invalid");
				}
				emailSet.add(email.trim());
			}
			
			//check date is not in the past
			YearMonth temp=YearMonth.parse(completedBy,Constants.YEAR_MONTH_FORMAT);
			if(temp.isBefore(YearMonth.now())){
				throw new InvalidAttributeValueException("Date can not be in the past");
			}
			
			//get user and loop through emails and add objective
			String proposedBy=EmployeeDAO.getFullNameUser(employeeID);
			for(String email : emailSet){
				try{
					ADProfile_Basic userInQuestion = ADProfileDAO.authenticateUserProfile(email);
					Objective obj=new Objective(0,0,title,description,completedBy);
					obj.setProposedBy(proposedBy);
					boolean inserted=EmployeeDAO.insertNewObjective(userInQuestion.getEmployeeID(), obj);
					if(inserted){
						insertAccepted = true;
						result+=  userInQuestion.getFullName() +", ";
					} else{
						errorInserting = true;
						errorResult+= "Could not send to " + userInQuestion.getEmployeeID() +", ";
					}
				}catch(InvalidAttributeValueException er){
					errorInserting = true;
					errorResult += er.getMessage();
				}
			}
			
			//If any error pop up, add to result
			if(errorInserting){
				if(!insertAccepted){ result = ""; }
				result += errorResult;
			}

			return ResponseEntity.ok(result);
			
		} catch (InvalidAttributeValueException e) {
			if(!insertAccepted){ result = ""; }
			return ResponseEntity.badRequest().body(result + e.getMessage()  +", ");
		} catch (NamingException e) {
			if(!insertAccepted){ result = ""; }
			return ResponseEntity.badRequest().body(result + e.getMessage()  +", ");
		} 
	}
	
	
	private void areInputValuesEmpty(String... args) throws InvalidAttributeValueException{
		for(String str : args){
			if(str.length() < 1 || str.isEmpty()){
				throw new InvalidAttributeValueException("One or more of the values are empty.");
			}
		}
	}
	
}
