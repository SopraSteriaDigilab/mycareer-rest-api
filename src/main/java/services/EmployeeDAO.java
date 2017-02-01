package services;

import static dataStructure.Constants.DEVELOPMENTNEED_NOTADDED_ERROR;
import static dataStructure.Constants.INVALID_COMPETENCY_CONTEXT;
import static dataStructure.Constants.INVALID_COMPETENCY_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_CONTEXT_PROGRESS;
import static dataStructure.Constants.INVALID_CONTEXT_USERID;
import static dataStructure.Constants.INVALID_DEVNEEDID_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_CONTEXT;
import static dataStructure.Constants.INVALID_DEVNEED_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_IDNOTFOND;
import static dataStructure.Constants.INVALID_NOTE;
import static dataStructure.Constants.INVALID_NOTEID;
import static dataStructure.Constants.INVALID_NOTE_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_OBJECTIVE;
import static dataStructure.Constants.INVALID_OBJECTIVEID;
import static dataStructure.Constants.INVALID_OBJECTIVE_OR_EMPLOYEEID;
import static dataStructure.Constants.INVALID_USEREMAIL;
import static dataStructure.Constants.INVALID_USERGUID_NOTFOUND;
import static dataStructure.Constants.NOTE_NOTADDED_ERROR;
import static dataStructure.Constants.NULL_OBJECTIVE;
import static dataStructure.Constants.NULL_USER_DATA;
import static dataStructure.Constants.OBJECTIVE_NOTADDED_ERROR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

import dataStructure.ADProfile_Advanced;
import dataStructure.ADProfile_Basic;
import dataStructure.Competency;
import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;
import services.ad.ADProfileDAO;
import services.ews.EmailService;
import services.validate.Validate;

/**
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
 * @author Ridhwan Nacef
 * @author Mehmet Mehmet
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the EmployeeDAO object
 *
 */
public class EmployeeDAO {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);
	
	//There is only 1 instance of the Datastore in the whole system
    static Datastore dbConnection;
	
	public EmployeeDAO(Datastore dbConnection) {
		EmployeeDAO.dbConnection = dbConnection;
	}
	

	public static Employee getEmployee(long employeeID) throws InvalidAttributeValueException {
		Employee employee = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if (employee == null) {
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		}
		return employee;
	}
	
	
	//To be removed. Use the getEmployee method.
	private static Query<Employee> getEmployeeQuery(long employeeID) {
		return dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
	}

	public static String getFullNameUser(long employeeID) throws InvalidAttributeValueException {
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.getFullName();
	}

	public static List<Objective> getObjectivesForUser(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.getLatestVersionObjectives();
	}

	public static Objective getSpecificObjectiveForUser(long employeeID, int objectiveID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.getLatestVersionOfSpecificObjective(objectiveID);
	}

	public static List<Feedback> getFeedbackForUser(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		List<Feedback> feedbackList = queryRes.getFeedback();
		Collections.reverse(feedbackList);
		return feedbackList;
	}

//	public static int getLatestFeedbackIDForUser(long employeeID) throws InvalidAttributeValueException{
//		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
//		if(queryRes==null)
//			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
//		List<Feedback> feedbackList = queryRes.getAllFeedback();
//		Feedback latest=feedbackList.get(feedbackList.size()-1);
//		return latest.getID();
//	}

	public static List<Note> getNotesForUser(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.getLatestVersionNotes();
	}

	public static List<DevelopmentNeed> getDevelopmentNeedsForUser(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		
		return queryRes.getLatestVersionDevelopmentNeeds();
	}

//	public static List<GroupFeedbackRequest> getGroupFeedbackRequestsForUser(long employeeID) throws InvalidAttributeValueException{
//		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
//		if(queryRes==null)
//			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
//		List<GroupFeedbackRequest> requested=queryRes.getGroupFeedbackRequestsList();
//
//		//Each Feedback contained within the groupFeedbackRequest->feedbackRequest->feedback is not completed. 
//		//For each of them we need to get the full feedback object from the feedback list, stored separately within the user data
//		for(GroupFeedbackRequest groupReq: requested){
//			List<FeedbackRequest> feedReqListTemp=groupReq.getRequestList();
//			for(FeedbackRequest req: feedReqListTemp){
//				//Get all the feedback included within the feedbackRequest object
//				List<Feedback> listF=req.getReplies();
//
//				//The feedback will be retrieved from the user data
//				List<Feedback> filledList=new ArrayList<>();
//				for(int i=0; i<listF.size(); i++){
//					filledList.add(queryRes.getSpecificFeedback(listF.get(i).getID()));
//				}
//				//Substitute the list of feedback
//				req.setReplies(filledList);
//			}
//		}
//		return requested;
//	}

	public static Map<String, Map<Integer, String>> getIDTitlePairsDataStructure(long employeeID) throws InvalidAttributeValueException{
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		//Get the whole employee object
		Employee e = query.get();
		//Get the latest version of all the user objectives
		List<Objective> objectives=e.getLatestVersionObjectives();
		//Get the user competencies
		List<Competency> competencies=e.getLatestVersionCompetencies();
		//Get all the development needs
		List<DevelopmentNeed> developmentNeeds=e.getLatestVersionDevelopmentNeeds();
		//Get Feedback (generic and requested)
		List<Feedback> allFeedback=e.getFeedback();

		List<String> reportees=new ArrayList<>();
		if(e.getIsManager()){
			reportees=e.getReporteeCNs();
		}
		//For each reportee, get the full name
		Map<Integer, String> reporteesDetails=new HashMap<>();
		try{
			int counter=1;
			for(String reportee: reportees){
				reporteesDetails.put(counter++, ADProfileDAO.findEmployeeFullNameFromID(reportee));
			}

		}
		catch(Exception error){}

		//Let's create the object that is going to be returned to the front-end
		Map<String, Map<Integer, String>> map=new HashMap<>();
		map.put("Objectives", convertListToHashMap(objectives));
		map.put("Competencies", convertListToHashMap(competencies));
		map.put("Development-Needs", convertListToHashMap(developmentNeeds));
		map.put("Feedback", convertListToHashMap(allFeedback));
		map.put("Team", reporteesDetails);
		return map;		
	}

	private static Map<Integer,String> convertListToHashMap(List<? extends Object> data) throws InvalidAttributeValueException{
		if(data!=null){
			Map<Integer,String> mapConverted=new HashMap<>(); 
			for(Object temp: data){
				if(temp instanceof Objective){
					Objective dataConv=(Objective)temp;
					mapConverted.put(dataConv.getID(), dataConv.getTitle());
				}
				else if(temp instanceof Competency){
					Competency dataConv=(Competency)temp;
					mapConverted.put(dataConv.getID(), dataConv.getTitle());
				}
				else if(temp instanceof DevelopmentNeed){
					DevelopmentNeed dataConv=(DevelopmentNeed)temp;
					mapConverted.put(dataConv.getID(), dataConv.getTitle());
				}
				else if (temp instanceof Feedback){
					Feedback dataConv=(Feedback)temp;
					mapConverted.put(dataConv.getId(), dataConv.getProviderName());
				}
				else
					throw new InvalidAttributeValueException("Invalid Data type");
			}
			return mapConverted;
		}
		return null;
	}

	public static String getUserFullNameFromUserID(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.getFullName();
	}

	public static long getUserIDFromEmailAddress(String email) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("emailAddress =", email).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_USEREMAIL);
		return queryRes.getEmployeeID();
	}

	public static String getUserEmailAddressFromID(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.getEmailAddress();
	}

	public static String getAllUserDataFromID(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.toString();
	}

	//Returns list of Competencies for a user
	public static List<Competency> getCompetenciesForUser(long employeeID) throws InvalidAttributeValueException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
		return queryRes.getLatestVersionCompetencies();
	}

	//Returns list of reportees for a user
	public static List<ADProfile_Basic> getReporteesForUser(long employeeID) throws InvalidAttributeValueException, NamingException{
		Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
		if(queryRes==null)
			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);

		List<ADProfile_Basic> reporteeList = new ArrayList<>();

		for(String str : queryRes.getReporteeCNs()){
			long temp =  Long.parseLong(str.substring(str.indexOf('-') + 1).trim());
			try{
				reporteeList.add(ADProfileDAO.verifyIfUserExists(temp));
			}catch(Exception e){
				throw new InvalidAttributeValueException("Sorry there were some connectiviy errors. Please try again later.");
			}
			
		}

		return reporteeList;

	}

	/**
	 * 
	 * @param employeeID
	 * @return 
	 * This method inserts a new objective for a specific employee given their ID
	 * @throws InvalidAttributeValueException 
	 */
	public static boolean insertNewObjective(long employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof Objective){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Add the new objective to the list
					if(e.addObjective((Objective)data)){
						//Update the List<List<objective>> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("objectives", e.getObjectiveList());
						//Commit the changes to the DB
						dbConnection.update(querySearch, ops);
						return true;
					}
					else
						throw new InvalidAttributeValueException(OBJECTIVE_NOTADDED_ERROR);
				}
				else
					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
			}
			else
				throw new InvalidAttributeValueException(NULL_OBJECTIVE);
		}
		else
			throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
	}

//	public static boolean insertNewGeneralFeedback(long employeeID, Object data)throws InvalidAttributeValueException, MongoException {
//		//Check the employeeID
//		if(employeeID>0){
//			if(data!=null && data instanceof Feedback){
//				//Retrieve Employee with the given ID
//				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
//				if(querySearch.get()!=null){
//					Employee e = querySearch.get();
//					//Extract its List of Objectives
//					List<Feedback> dataFromDB=e.getAllFeedback();
//					//Verify if the feedback is already within the user DB to avoid adding duplicated feedback
//					for(Feedback f:dataFromDB){
//						if(f.compare((Feedback)data))
//							throw new InvalidAttributeValueException(DUPLICATE_FEEDBACK);
//					}
//					Feedback temp=(Feedback)data;
//					try{
//						temp.setID(getLatestFeedbackIDForUser(employeeID)+1);
//					}
//					catch(Exception error){
//						temp.setID(1);
//					}
//					//Add the new objective to the list
//					if(e.addGenericFeedback((Feedback)data)){
//						//Update the List<List<objective>> in the DB passing the new list
//						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedback", e.getAllFeedback());
//						//Commit the changes to the DB
//						dbConnection.update(querySearch, ops);
//						return true;
//					}
//					else
//						throw new InvalidAttributeValueException(FEEDBACK_NOTADDED_ERROR);
//				}
//				else
//					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
//			}
//			else
//				throw new InvalidAttributeValueException(INVALID_FEEDBACK);
//		}
//		else
//			throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
//	}
	
	public static boolean updateProgressObjective(long employeeID, int objectiveID, int progress) throws InvalidAttributeValueException {
		if (employeeID < 1 || objectiveID < 1) {
			throw new InvalidAttributeValueException(INVALID_OBJECTIVE_OR_EMPLOYEEID);
		} else if (progress < -1 || progress > 2) {
			throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
		}
		
		boolean updated = false;
		final Query<Employee> querySearch = getEmployeeQuery(employeeID);
		final Employee employee = querySearch.get();
		final Objective objective = employee.getLatestVersionOfSpecificObjective(objectiveID);
		
		if (objective.getProgress() == progress) {
			updated = true;
		} else {
			objective.setProgress(progress);
			
			if (employee.editObjective(objective)) {
				UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("objectives", employee.getObjectiveList());
				dbConnection.update(querySearch, ops);
				updated = true;
			}
		}
		
		return updated;
	}

	public static boolean addNewVersionObjective(long employeeID, int objectiveID, Object data) throws InvalidAttributeValueException {
		//Check EmployeeID and ObjectiveID
		if(employeeID>0 && objectiveID>0){
			if(data!=null && data instanceof Objective){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Extract its List of Objectives
					List<List<Objective>> dataFromDB=e.getObjectiveList();
					//Search for the objective Id within the list of objectives
					int indexObjectiveList=-1;
					for(int i=0; i<dataFromDB.size(); i++){
						//Save the index of the list when the objectiveID is found
						if(dataFromDB.get(i).get(0).getID()==objectiveID){
							indexObjectiveList=i;
							//Exit the for loop once the value has been found
							break;
						}
					}
					//verify that the index variable has changed its value
					if(indexObjectiveList!=-1){ 
						//Add the updated version of the objective
						if(e.editObjective((Objective)data)){
							//Update the List<List<objective>> in the DB passing the new list
							UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("objectives", e.getObjectiveList());
							//Commit the changes to the DB
							dbConnection.update(querySearch, ops);
							return true;
						}
					}
					//if the index hasn't changed its value it means that there is no objective with such ID, therefore throw and exception
					else
						throw new InvalidAttributeValueException(INVALID_OBJECTIVEID);
				}
				else
					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
			}
			else
				throw new InvalidAttributeValueException(INVALID_OBJECTIVE);
		}
		else
			throw new InvalidAttributeValueException(INVALID_OBJECTIVE_OR_EMPLOYEEID);
		
		return false;
	}

	public static boolean insertNewNote(long employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof Note){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Add the new note to the list
					if(e.addNote((Note)data)){
						//Update the List<List<Note>> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("notes", e.getNoteList());
						//Commit the changes to the DB
						dbConnection.update(querySearch, ops);
						return true;
					}
					else
						throw new InvalidAttributeValueException(NOTE_NOTADDED_ERROR);
				}
				else
					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
			}
			else
				throw new InvalidAttributeValueException(INVALID_NOTE);
		}
		else
			throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
	}

	public static boolean addNewVersionNote(long employeeID, int noteID, Object data) throws InvalidAttributeValueException{
		//Check EmployeeID and noteID
		if(employeeID>0 && noteID>0){
			if(data!=null && data instanceof Note){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Extract its List of notes
					List<List<Note>> dataFromDB=e.getNoteList();
					//Search for the objective Id within the list of notes
					int indexNoteList=-1;
					for(int i=0; i<dataFromDB.size(); i++){
						//Save the index of the list when the noteID is found
						if(dataFromDB.get(i).get(0).getID()==noteID){
							indexNoteList=i;
							//Exit the for loop once the value has been found
							break;
						}
					}
					//verify that the index variable has changed its value
					if(indexNoteList!=-1){ 
						//Add the updated version of the note
						if(e.editNote((Note)data)){
							//Update the List<List<Note>> in the DB passing the new list
							UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("notes", e.getNoteList());
							//Commit the changes to the DB
							dbConnection.update(querySearch, ops);
							return true;
						}
					}
					//if the index hasn't changed its value it means that there is no note with such ID, therefore throw and exception
					else
						throw new InvalidAttributeValueException(INVALID_NOTEID);
				}
				else
					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
			}
			else
				throw new InvalidAttributeValueException(INVALID_NOTE);
		}
		else
			throw new InvalidAttributeValueException(INVALID_NOTE_OR_EMPLOYEEID);
		return false;
	}

	public static boolean insertNewDevelopmentNeed(long employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof DevelopmentNeed){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Add the new development need to the list
					if(e.addDevelopmentNeed((DevelopmentNeed)data)){
						//Update the List<List<developmentNeed>> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("developmentNeeds", e.getDevelopmentNeedsList());
						//Commit the changes to the DB
						dbConnection.update(querySearch, ops);
						return true;
					}
					else
						throw new InvalidAttributeValueException(DEVELOPMENTNEED_NOTADDED_ERROR);
				}
				else
					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
			}
			else
				throw new InvalidAttributeValueException(INVALID_DEVNEED_CONTEXT);
		}
		else
			throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
	}
	
	
	public static boolean updateProgressDevelopmentNeed(long employeeID, int devNeedID, int progress) throws InvalidAttributeValueException {
		if (employeeID < 1 || devNeedID < 1) {
			throw new InvalidAttributeValueException(INVALID_DEVNEED_OR_EMPLOYEEID);
		} else if (progress < -1 || progress > 2) {
			throw new InvalidAttributeValueException(INVALID_CONTEXT_PROGRESS);
		}
		
		boolean updated = false;
		final Query<Employee> querySearch = getEmployeeQuery(employeeID);
		final Employee employee = querySearch.get();
		final DevelopmentNeed devNeed = employee.getLatestVersionOfSpecificDevelopmentNeed(devNeedID);
		
		if (devNeed.getProgress() == progress) {
			updated = true;
		} else {
			devNeed.setProgress(progress);
			
			if (employee.editDevelopmentNeed(devNeed)) {
				UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("developmentNeeds", employee.getDevelopmentNeedsList());
				dbConnection.update(querySearch, ops);
				updated = true;
			}
		}
		
		return updated;
	}

	public static boolean addNewVersionDevelopmentNeed(long employeeID, int devNeedID, Object data) throws InvalidAttributeValueException{
		//Check EmployeeID and noteID
		if(employeeID>0 && devNeedID>0){
			if(data!=null && data instanceof DevelopmentNeed){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Extract its List of notes
					List<List<DevelopmentNeed>> dataFromDB=e.getDevelopmentNeedsList();
					//Search for the objective Id within the list of development needs
					int indexDevNeedList=-1;
					for(int i=0; i<dataFromDB.size(); i++){
						//Save the index of the list when the devNeedID is found
						if(dataFromDB.get(i).get(0).getID()==devNeedID){
							indexDevNeedList=i;
							//Exit the for loop once the value has been found
							break;
						}
					}
					//verify that the index variable has changed its value
					if(indexDevNeedList!=-1){ 
						//Add the updated version of the DevelopmentNeed
						if(e.editDevelopmentNeed((DevelopmentNeed)data)){
							//Update the List<List<DevelopmentNeed>> in the DB passing the new list
							UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("developmentNeeds", e.getDevelopmentNeedsList());
							//Commit the changes to the DB
							dbConnection.update(querySearch, ops);
							return true;
						}
					}
					//if the index hasn't changed its value it means that there is no development need with such ID, therefore throw and exception
					else
						throw new InvalidAttributeValueException(INVALID_DEVNEEDID_CONTEXT);
				}
				else
					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
			}
			else
				throw new InvalidAttributeValueException(INVALID_DEVNEED_CONTEXT);
		}
		else
			throw new InvalidAttributeValueException(INVALID_DEVNEED_OR_EMPLOYEEID);
		return false;
	}
	
	/**
	 * Sends Emails to the recipients and updates the database.
	 * 
	 * @param employeeID
	 * @param emailsString
	 * @param notes
	 * @throws Exception
	 */
	public static void processFeedbackRequest(long employeeID, String emailsString, String notes) throws Exception {
		Set<String> recipientList = Helper.stringEmailsToHashSet(emailsString);
		List<String> errorRecipientList = new ArrayList<String>();
		
		for(String recipient : recipientList){
			String tempID = Helper.generateFeedbackRequestID(employeeID);
			String body = notes + " \n\n Feedback_Request: " + tempID;
			// TODO Replace above with template.
			try {
				EmailService.sendEmail(recipient, "Feedback Request", body);
			} catch (Exception e) {
				logger.error(e.getMessage());
				errorRecipientList.add(recipient);
				continue;
			}
			Employee requester = EmployeeDAO.getEmployee(employeeID);
			EmployeeDAO.addFeedbackRequest(requester, new FeedbackRequest(tempID, recipient));
		}
		
		if(!errorRecipientList.isEmpty()){
			throw new Exception("There were issues sending requests to the following addresses: \n" + errorRecipientList.toString());
		}
	}
	
	
	/**
	 * Add a feedbackRequest to an employee.
	 * 
	 * @param employee
	 * @param feedbackRequest
	 * @return
	 * @throws InvalidAttributeValueException
	 */
	public static void addFeedbackRequest(Employee employee, FeedbackRequest feedbackRequest) throws InvalidAttributeValueException {
		Validate.isNull(employee, feedbackRequest);
		
		employee.addFeedbackRequest(feedbackRequest);
		UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests", employee.getFeedbackRequestsList());
		dbConnection.update(employee, ops);
	}
	
	/**
	 * Add a feedback to an employee
	 * @throws NamingException 
	 * @throws InvalidAttributeValueException 
	 */
	public static void addFeedback(String providerEmail, String recipientEmail, String feedbackDescription) throws InvalidAttributeValueException, NamingException{
		Validate.areStringsEmptyorNull(providerEmail, recipientEmail, feedbackDescription);
		
		long employeeID = ADProfileDAO.authenticateUserProfile(recipientEmail).getEmployeeID();
		Employee employee = getEmployee(employeeID);

		employee.addFeedback(new Feedback(employee.nextFeedbackID(), providerEmail, feedbackDescription));
		
		UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedback", employee.getFeedback());
		dbConnection.update(employee, ops);
	}
	

	public static void addRequestedFeedback(String providerEmail, String feedbackRequestID, String feedbackDescription) throws InvalidAttributeValueException, NamingException {
		Validate.areStringsEmptyorNull(providerEmail, feedbackRequestID, feedbackDescription);
		
		long employeeID = Helper.getEmployeeIDFromRequestID(feedbackRequestID);
		Employee employee = getEmployee(employeeID);
		
		employee.getFeedbackRequest(feedbackRequestID).setReplyReceived(true);
		
		UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests", employee.getFeedbackRequestsList());
		dbConnection.update(employee, ops);
		
		addFeedback(providerEmail, employee.getEmailAddress(), feedbackDescription);
	}

//	public static boolean insertNewGroupFeedbackRequest(long employeeID, Object data) throws InvalidAttributeValueException{
//		//Check the employeeID
//		if(employeeID>0){
//			if(data!=null && data instanceof GroupFeedbackRequest){
//				//Retrieve Employee with the given ID
//				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
//				if(querySearch.get()!=null){
//					Employee e = querySearch.get();
//					//Add the new FeedbackRequest to the list
//					if(e.addGroupFeedbackRequest((GroupFeedbackRequest)data)){
//						//Update the List<FeedbackRequest> in the DB passing the new list
//						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("groupFeedbackRequests", e.getGroupFeedbackRequestsList());
//						//Commit the changes to the DB
//						dbConnection.update(querySearch, ops);
//						return true;
//					}
//					else
//						throw new InvalidAttributeValueException(GROUPFBREQ_NOTADDED_ERROR);
//				}
//				else
//					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
//			}
//			else
//				throw new InvalidAttributeValueException(INVALID_FEEDBACKREQ_CONTEXT);
//		}
//		else
//			throw new InvalidAttributeValueException(INVALID_CONTEXT_USERID);
//	}

//	public static boolean validateGroupFeedbackRequestID(long employeeID, String id) throws InvalidAttributeValueException{
//		if(employeeID>0 && !id.equals("")){
//			//Retrieve Employee with the given ID
//			Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
//			if(querySearch.get()!=null){
//				Employee e = querySearch.get();
//				//Extract its List of notes
//				List<GroupFeedbackRequest> requests=e.getGroupFeedbackRequestsList();
//				//Check if the feedbackID is already contained within the system
//				for(GroupFeedbackRequest f: requests){
//					if(f.getID().equals(id))
//						return false;
//				}
//				return true;
//			}
//			else{
//				throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
//			}
//		}
//		else
//			throw new InvalidAttributeValueException(INVALID_FBREQ_OR_EMPLOYEEID);
//	}

	/**
	 * 
	 * @param employeeID the employee ID
	 * @param data the Competency to update
	 * @param title the title of the competency (max 200 characters)
	 * @return true or false to establish whether the task has been completed successfully or not
	 * This method inserts a new version of competencies list 
	 * @throws InvalidAttributeValueException 
	 */
	public static boolean addNewVersionCompetency(long employeeID, Object data, String title) throws InvalidAttributeValueException{
		//Check EmployeeID and noteID
		if(employeeID>0 && title!=null && title.length()>0){
			if(data!=null && data instanceof Competency && title!=null && title.length()>0){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Add the updated version of the note
					if(e.updateCompetency((Competency)data,title)){
						//Update the List<List<Competencies>> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("competencies", e.getCompetenciesList());
						//Commit the changes to the DB
						dbConnection.update(querySearch, ops);
						return true;
					}
				}
				else
					throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
			}
			else
				throw new InvalidAttributeValueException(INVALID_COMPETENCY_CONTEXT);
		}
		else
			throw new InvalidAttributeValueException(INVALID_COMPETENCY_OR_EMPLOYEEID);
		return false;
	}

	public static ADProfile_Basic matchADWithMongoData(ADProfile_Advanced userData) throws InvalidAttributeValueException{
		if(userData!=null){
			//Establish a connection with the DB
			//Search for a user GUID
			//Retrieve Employee with the given GUID
			if(!userData.getGUID().equals("")){
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("GUID =", userData.getGUID());

				//If a user exists in our system, verify that his/hers data is up-to-date
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					boolean resUpdate=e.verifyDataIsUpToDate(userData);
					//If the method returns true, the data has been updated
					if(resUpdate){
						//Reflect the changes to our system, updating the user data in the MongoDB
						//Remove incorrect document
						dbConnection.findAndDelete(querySearch);
						//Commit the changes to the DB
						dbConnection.save(e);
					}
				}
				//Else, Create the user in our system with the given data
				else{
					//Create the Employee Object
					Employee employeeNewData=new Employee(userData);
					//Save the new user to the DB
					dbConnection.save(employeeNewData);
				}
				//Return a smaller version of the current object to the user
				return new ADProfile_Basic(userData.getEmployeeID(),userData.getSurname(), userData.getForename(), userData.getIsManager(), userData.getUsername(), userData.getEmailAddress());
			}
			else
				throw new InvalidAttributeValueException(INVALID_USERGUID_NOTFOUND);
		}
		else
			throw new InvalidAttributeValueException(NULL_USER_DATA);
	}



//	public static boolean linkFeedbackReqReplyToUserGroupFBReq(String requester, String fbReqID, Feedback data) throws InvalidAttributeValueException, NamingException{
//		if(data!=null && data.isFeedbackValid()){
//			//Establish a connection with the DB
//
//			UpdateResults updateResult1 = null, updateResult2=null;
//			//Get the userData from the DB/AD as well as updating the user profile if needed
//			ADProfile_Basic basicProfile=ADProfileDAO.authenticateUserProfile(requester);
//			//Search for the feedbackReqID inside the user data (using the employeeID inside the basicProfile
//			//Get the user from the DB
//			Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", basicProfile.getEmployeeID());
//			if(querySearch.get()!=null){
//				Employee e = querySearch.get();
//				//Retrieve specific GRoupFeedbackRequest object from user data
//				GroupFeedbackRequest groupFBReq=e.getSpecificGroupFeedbackRequest(fbReqID.trim());
//
//				boolean flag=false;
//				try{
//					data.setID(getLatestFeedbackIDForUser(basicProfile.getEmployeeID())+1);
//				}
//				catch(Exception error){
//					data.setID(1);
//				}
//				//Verify that something has been returned, if so, we have the feedback request object, let's add the feedback to it
//				if(groupFBReq!=null){
//					flag=true;
//					FeedbackRequest feedbackReqUpdated=e.getSpecificFeedbackRequestFromGroupFBRequests(fbReqID.trim());
//					//Add the feedback to the current feedback request object 
//					//Add the general feedback to the user
//					e.addGenericFeedback(data);
//					boolean res1=feedbackReqUpdated.addReply(data);
//					//Update the feedback request object inside the group feedback request object
//					boolean res2=groupFBReq.updateFeedbackRequest(feedbackReqUpdated);
//					boolean res3=e.updateGroupFeedbackRequest(groupFBReq);
//					//Verify if the operations have been completed successfully
//					if(!res1 || !res2 || !res3)
//						throw new InvalidAttributeValueException(ERROR_LINKING_FBTOUSER+basicProfile.getEmployeeID());
//					//Update the data in the DB
//					UpdateOperations<Employee> ops2 = dbConnection.createUpdateOperations(Employee.class).set("groupFeedbackRequests", e.getGroupFeedbackRequestsList());
//					//Commit the changes to the DB
//					updateResult1=dbConnection.update(querySearch, ops2);
//				}
//
//				if(!flag){
//					data.setIsRequested(false);
//					//Add the general feedback to the user
//					e.addGenericFeedback(data);
//				}
//				//Update the user data in the DB
//				UpdateOperations<Employee> ops1 = dbConnection.createUpdateOperations(Employee.class).set("feedback", e.getAllFeedback());
//				updateResult2=dbConnection.update(querySearch, ops1);
//				if(updateResult2!=null && updateResult2.getUpdatedCount()>0)
//					return true;
//				else
//					return false;
//				
//			}
//			//Since we use the authenticateUSerProfile method, the system will never go in the else statement
//			//else{}
//		}
//		else
//			throw new InvalidAttributeValueException(INVALID_FEEDBACK);
//		return false;
//	}

//		public static String removeFeedbackReqFromUser(String reqID, long empID) throws InvalidAttributeValueException{
//			if(reqID.length()<0 || empID<0)
//				throw new InvalidAttributeValueException(INVALID_FBREQ_OR_EMPLOYEEID);
//			//Get the user from the DB
//			Employee querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", empID).get();
//			if(querySearch!=null){
//				String emailRes=querySearch.removeSpecificFeedbackRequest(reqID);
//				if(!emailRes.equals("")){
//					//Update the user data in the DB
//					UpdateOperations<Employee> ops1 = dbConnection.createUpdateOperations(Employee.class).set("groupFeedbackRequests", querySearch.getGroupFeedbackRequestsList());
//					dbConnection.update(querySearch, ops1);
//					return emailRes;
//				}
//				throw new InvalidAttributeValueException(NOTDELETED_FBREQ);
//			}
//			else{
//				throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
//			}
//		}
	
//	private static MongoDatabase getMongoDBConnection() throws MongoException{
//		if(dbConnection==null){
//			//This currently doesn't fully work. the waitTime is not taken by the DB
//			MongoClientOptions options=MongoClientOptions.builder()
//					.maxWaitTime(10000)
//					//.maxConnectionIdleTime(1000)
//					.connectTimeout(10000)
//					//.socketKeepAlive(true)
//					//.cursorFinalizerEnabled(true)
//					//.socketTimeout(100)
//					//.readConcern(ReadConcern.MAJORITY)
//					//.writeConcern(WriteConcern.ACKNOWLEDGED)
//					.build();
//			//Server details
//			ServerAddress srvAddr = new ServerAddress(MONGODB_HOST, MONGODB_PORT);
//			List<ServerAddress> serverList=new ArrayList<>();
//			serverList.add(srvAddr);
//			//Setup the credentials
//			MongoCredential credentials= MongoCredential.createCredential(MONGODB_USERNAME, MONGODB_DB_NAME, MONGODB_PASSWORD.toCharArray());
//			List<MongoCredential> credentialList=new ArrayList<>();
//			credentialList.add(credentials);
//			//Instantiate mongo client and Morphia
//			MongoClient client = new MongoClient(serverList, credentialList, options);
//			//final Morphia morphia =new Morphia();
//			//Add packages to Morphia and open the connection
//			//morphia.mapPackage("dataStructure.Employee");
//			//dbConnection=morphia.createDatastore(client, MONGODB_DB_NAME);
//			//dbConnection.ensureIndexes();
//			MongoDatabase db=client.getDatabase(MONGODB_DB_NAME);
//			//dbConnection=db.getCollection("employeeDataDev");
//		}
//		return dbConnection;
//	}
//	
//	public static String getFullNameUser2(long employeeID) throws InvalidAttributeValueException {
//		if(dbConnection==null)
//			dbConnection=getMongoDBConnection();
////		BasicDBObject whereQuery=new BasicDBObject();
////		whereQuery.put("employeeID", employeeID);
//		//whereQuery.
//		Document query= new Document("employeeID", employeeID);
//		Document filter= new Document("surname", 1).append("forename", 1).append("_id", 0);
//		//MongoCollection<Document> coll=dbConnection.getCollection("employeeDataDev");
//		//ArrayList<Document> returnValue=coll.find(query).projection(filter).into(new ArrayList<Document>());
//		
//		Document doc0=returnValue.get(0);
//		
//		MongoCollection<Document> col=dbConnection.getCollection("employeeDataDev");
////		DBObject query2=BasicDBObjectBuilder.start().add("username", 675599).get();
////		FindIterable<Document> cursor=col.find(query);
//		List<Document> foundDocs=col.find(Filters.eq("",675599),"");
//		
//		
//		//coll.find().filter("employeeID =", employeeID).get();
//		//DBCollection coll=dbConnection.getCollection(MONGODB_DB_NAME);
//		//FindIterable<Document> doc=coll.find(new Document("",new Document("",""))).projection(new Document("surname",1)).projection(new Document("forename",1))
//		
//		//dbConnection.find(Filters.eq("employeeID", 676783), "{surname: 1, forename: 1, _id: 0}");
//		//Employee queryRes = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID).get();
////		if(queryRes==null)
////			throw new InvalidAttributeValueException(INVALID_IDNOTFOND);
////		return queryRes.getFullName();
//		
//		return "";
//	}
	
//		private static MongoDatabase dbConnection=null;
//		private static MongoDatabase dbConnection=null;
}
