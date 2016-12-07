package functionalities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import dataStructure.ADProfile_Advanced;
import dataStructure.ADProfile_Basic;
import dataStructure.Competency;
import dataStructure.Constants;
import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.GroupFeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;

/**
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the EmployeeDAO object
 *
 */
public  class EmployeeDAO {

	//There is only 1 instance of the Datastore in the whole system
	private static Datastore dbConnection=null;

	public static String getFullNameUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getFullName();
	}

	public static List<Objective> getObjectivesForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getLatestVersionObjectives();
	}

	public static Objective getSpecificObjectiveForUser(long employeeID, int objectiveID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		List<Objective> latestVersion=e.getLatestVersionObjectives();
		try{
			Objective temp= latestVersion.stream().filter(t-> t.getID()==objectiveID).findFirst().get();
			if(temp==null)
				throw new InvalidAttributeValueException("No Objective with such ID");
			return temp;
		}
		catch(Exception err){
			throw new InvalidAttributeValueException("No Objective with such ID");
		}
	}

	public static List<Feedback> getFeedbackForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		List<Feedback> feedbackList = e.getAllFeedback();
		Collections.reverse(feedbackList);
		return feedbackList;
	}

	public static int getLatestFeedbackIDForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		//db.employeeDataDev.aggregate([{$project:{employeeID:1,"feedback.id":1, _id:0}},{$match:{employeeID:675599}}, {$unwind:"feedback"},{$sort:{feedback:-1}},{$limit:1},{$project:{latestFeedbackID:"$feedback.id"}}])
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		//		Iterator<String> aggregate=dbConnection
		//				.createAggregation(Employee.class)
		//				.project(Projection.projection("employeeID"), Projection.projection("feedback.id"))
		//				.match(dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID))
		//				.unwind("feedback")
		//				.sort(Sort.descending("feedback"))
		//				.limit(1)
		//				.project(Projection.projection("feedback.id", "latestFeedbackID"))
		//				.aggregate(String.class);
		//		
		//		if(aggregate!=null){
		//			String s=aggregate.next();
		//			System.out.print(Integer.valueOf(s));
		//			//int num=aggregate.next();
		//		}
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		List<Feedback> feedbackList = e.getAllFeedback();
		Feedback latest=feedbackList.get(feedbackList.size()-1);
		return latest.getID();
	}

	public static List<Note> getNotesForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getLatestVersionNotes();
	}

	public static List<DevelopmentNeed> getDevelopmentNeedsForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getLatestVersionDevelopmentNeeds();
	}

	public static List<GroupFeedbackRequest> getGroupFeedbackRequestsForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		List<GroupFeedbackRequest> requested=e.getGroupFeedbackRequestsList();
		
		//Each Feedback contained within the groupFeedbackRequest->feedbackRequest->feedback is not completed. 
		//For each of them we need to get the full feedback object from the feedback list, stored separately within the user data
		for(GroupFeedbackRequest groupReq: requested){
			List<FeedbackRequest> feedReqListTemp=groupReq.getRequestList();
			for(FeedbackRequest req: feedReqListTemp){
				//Get all the feedback included within the feedbackRequest object
				List<Feedback> listF=req.getReplies();
				
				//The feedback will be retrieved from the user data
				List<Feedback> filledList=new ArrayList<>();
				for(int i=0; i<listF.size(); i++){
					filledList.add(e.getSpecificFeedback(listF.get(i).getID()));
				}
				//Substitute the list of feedback
				req.setReplies(filledList);
			}
		}
		
		return requested;
	}

	public static Map<String, Map<Integer, String>> getIDTitlePairsDataStructure(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		//Get the whole employee object
		Employee e = query.get();
		//Get the latest version of all the user objectives
		List<Objective> objectives=e.getLatestVersionObjectives();
		//Get the user competencies
		List<Competency> competencies=e.getLatestVersionCompetencies();
		//Get all the development needs
		List<DevelopmentNeed> developmentNeeds=e.getLatestVersionDevelopmentNeeds();
		//Get Feedback (generic and requested)
		List<Feedback> allFeedback=e.getAllFeedback();

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
					mapConverted.put(dataConv.getID(), dataConv.getFromWho());
				}
				else
					throw new InvalidAttributeValueException("Invalid Data type");
			}
			return mapConverted;
		}
		return null;
	}

	public static String getUserFullNmeFromUserID(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getFullName();
	}

	public static long getUserIDFromEmailAddress(String email) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("emailAddress =", email);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such Email");
		Employee e = query.get();
		return e.getEmployeeID();
	}

	public static String getUserEmailAddressFromID(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getEmailAddress();
	}

	public static String getAllUserDataFromID(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.toString();
	}

	//Returns list of Competencies for a user
	public static List<Competency> getCompetenciesForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getLatestVersionCompetencies();
	}

	//Returns list of reportees for a user
	public static List<ADProfile_Basic> getReporteesForUser(long employeeID) throws InvalidAttributeValueException, NamingException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();

		List<ADProfile_Basic> reporteeList = new ArrayList<>();

		for(String str : e.getReporteeCNs()){
			long temp =  Long.parseLong(str.substring(str.indexOf('-') + 1).trim());
			reporteeList.add(ADProfileDAO.verifyIfUserExists(temp));
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
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
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
						throw new InvalidAttributeValueException("The given object couldn't be added to the objectives list");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The ID provided is not valid");
	}

	//	public static boolean updateEmail(String email) throws InvalidAttributeValueException, MongoException{
	//		if(dbConnection==null)
	//			dbConnection=getMongoDBConnection();
	//		//Check the employeeID
	//		Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", 4323);
	//		if(querySearch.get()!=null){
	//			//Update the List<List<objective>> in the DB passing the new list
	//			UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("emailAddress", email);
	//			//Commit the changes to the DB
	//			dbConnection.update(querySearch, ops);
	//			return true;
	//		}
	//		return false;
	//	}

	public static boolean insertNewGeneralFeedback(long employeeID, Object data)throws InvalidAttributeValueException, MongoException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof Feedback){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Extract its List of Objectives
					List<Feedback> dataFromDB=e.getAllFeedback();
					//Verify if the feedback is already within the user DB to avoid adding duplicated feedback
					for(Feedback f:dataFromDB){
						if(f.compare((Feedback)data))
							throw new InvalidAttributeValueException("The given feedback is a duplicate");
					}
					Feedback temp=(Feedback)data;
					try{
						temp.setID(getLatestFeedbackIDForUser(employeeID)+1);
					}
					catch(Exception error){
						temp.setID(1);
					}
					//Add the new objective to the list
					if(e.addGenericFeedback((Feedback)data)){
						//Update the List<List<objective>> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedback", e.getAllFeedback());
						//Commit the changes to the DB
						dbConnection.update(querySearch, ops);
						return true;
					}
					else
						throw new InvalidAttributeValueException("The given object couldn't be added to the feedback list");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The ID provided is not valid");
	}

	public static boolean addNewVersionObjective(long employeeID, int objectiveID, Object data) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
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
						throw new InvalidAttributeValueException("The given ObjectiveID is invalid");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The given EmployeeID or ObjectiveID are invalid");
		return false;
	}

	public static boolean insertNewNote(long employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
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
						throw new InvalidAttributeValueException("The given object couldn't be added to the notes list");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The ID provided is not valid");
	}

	public static boolean addNewVersionNote(long employeeID, int noteID, Object data) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
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
						throw new InvalidAttributeValueException("The given NoteID is invalid");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The given EmployeeID or NoteID are invalid");
		return false;
	}

	public static boolean insertNewDevelopmentNeed(long employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
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
						throw new InvalidAttributeValueException("The given object couldn't be added to the development need list");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The ID provided is not valid");
	}

	public static boolean addNewVersionDevelopmentNeed(long employeeID, int devNeedID, Object data) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
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
						throw new InvalidAttributeValueException("The given ID associated with the development need is invalid");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The given EmployeeID or DevelopmentNeedID are invalid");
		return false;
	}

	//	public static boolean insertNewFeedbackRequest(long employeeID, Object data) throws InvalidAttributeValueException, MongoException{
	//		if(dbConnection==null)
	//			dbConnection=getMongoDBConnection();
	//		//Check the employeeID
	//		if(employeeID>0){
	//			if(data!=null && data instanceof FeedbackRequest){
	//				//Retrieve Employee with the given ID
	//				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
	//				if(querySearch.get()!=null){
	//					Employee e = querySearch.get();
	//					//Add the new FeedbackRequest to the list
	//					if(e.addFeedbackRequest((FeedbackRequest)data)){
	//						//Update the List<FeedbackRequest> in the DB passing the new list
	//						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests", e.getFeedbackRequestsList());
	//						//Commit the changes to the DB
	//						dbConnection.update(querySearch, ops);
	//						return true;
	//					}
	//					else
	//						throw new InvalidAttributeValueException("The given object couldn't be added to the feeback request list");
	//				}
	//				else
	//					throw new InvalidAttributeValueException("No employee found with the ID provided");
	//			}
	//			else
	//				throw new InvalidAttributeValueException("The data provided is not valid");
	//		}
	//		else
	//			throw new InvalidAttributeValueException("The ID provided is not valid");
	//	}

	public static boolean insertNewGroupFeedbackRequest(long employeeID, Object data) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof GroupFeedbackRequest){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Add the new FeedbackRequest to the list
					if(e.addGroupFeedbackRequest((GroupFeedbackRequest)data)){
						//Update the List<FeedbackRequest> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("groupFeedbackRequests", e.getGroupFeedbackRequestsList());
						//Commit the changes to the DB
						dbConnection.update(querySearch, ops);
						return true;
					}
					else
						throw new InvalidAttributeValueException("The given object couldn't be added to the group feeback request list");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The ID provided is not valid");
	}

	//	public static boolean updateFeedbackRequest(long employeeID, int feedbackReqID, Object data) throws InvalidAttributeValueException{
	//		if(dbConnection==null)
	//			dbConnection=getMongoDBConnection();
	//		//Check EmployeeID and feedbackReqID
	//		if(employeeID>0 && feedbackReqID>0){
	//			if(data!=null && data instanceof FeedbackRequest){
	//				//Retrieve Employee with the given ID
	//				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
	//				if(querySearch.get()!=null){
	//					Employee e = querySearch.get();
	//					//Extract its List of feedback Requests
	//					List<FeedbackRequest> requestsList=e.getFeedbackRequestsList();
	//					//Search for the objective Id within the list of notes
	//					int indexFeedReqList=-1;
	//					for(int i=0; i<requestsList.size(); i++){
	//						//Save the index of the list when the noteID is found
	//						if(requestsList.get(i).getID().equals(feedbackReqID)){
	//							indexFeedReqList=i;
	//							//Exit the for loop once the value has been found
	//							break;
	//						}
	//					}
	//					//verify that the index variable has changed its value
	//					if(indexFeedReqList!=-1){ 
	//						//Add the updated version of the note
	//						if(e.updateFeedbackRequest((FeedbackRequest)data)){
	//							//Update the List<List<Note>> in the DB passing the new list
	//							UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests", e.getFeedbackRequestsList());
	//							//Commit the changes to the DB
	//							dbConnection.update(querySearch, ops);
	//							return true;
	//						}
	//					}
	//					//if the index hasn't changed its value it means that there is no feedback request with such ID, therefore throw and exception
	//					else
	//						throw new InvalidAttributeValueException("The given ID associated with the feedback request is invalid");
	//				}
	//				else
	//					throw new InvalidAttributeValueException("No employee found with the ID provided");
	//			}
	//			else
	//				throw new InvalidAttributeValueException("The data provided is not valid");
	//		}
	//		else
	//			throw new InvalidAttributeValueException("The given EmployeeID or feedbackReqID are invalid");
	//		return false;
	//	}

	/*
//	public static void insertTempData(){
		try{
			Datastore datastore=getMongoDBConnection();
			Employee e1=new Employee(3422,8,"Bill","Bill","bill.bill@soprasteria.com","Junior Software Engineer"
					+ "",false,"Steven Malpure",3432,"1973-12-10","1987-11-19",null,null,null);
			//Create a list of feedback
			Feedback fe1=new Feedback(1,2,"William","Wow, this guys is great at developing stuff","Internal","Email");
			Feedback fe2=new Feedback(2,3,"Red","Well, I was born ready!","Internal","Yammer");
			//Add the feedback to the employee object
			e1.addGenericFeedback(fe1);
			e1.addGenericFeedback(fe2);
			//Create 2 Objectives
			Objective o1=new Objective(1,0,1,"First Objective","This is the first objective that Michael has added to the system","2017-03");
			Objective o2=new Objective(2,1,2,"Second Objective","This is the second objective that Michael has added to the system","2016-12");
			//Add the objective to the employee object
			e1.addObjective(o1);
			e1.addObjective(o2);

			//Add feedback to the objective
			Feedback fe3=new Feedback(1,2,"William","Wow, this guys is great at developing stuff","Internal","Email");
			Feedback fe4=new Feedback(2,3,"Red","Well, I was born ready!","Internal","Yammer");
			e1.addFeedbackToObjective(2, fe3);
			e1.addFeedbackToObjective(2, fe4);
			e1.addFeedbackToObjective(1, fe4);

			datastore.save(e1);
		}
		catch(Exception e){
			System.out.println("There has been a problem!");
		}
	}
	 */

	//	public static void changeEmployeeNotes(int id,String email){
	//		try{
	//			Datastore datastore=getMongoDBConnection();
	//			Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", id);
	//			if(querySearch.get()!=null){
	//				Employee e = querySearch.get();
	//				//e.setForename(forename);
	//				//e.setSurname(surname);
	//				e.setEmailAddress(email);
	//				//List<List<Note>> n=new ArrayList<List<Note>>();
	//				//e.setNoteList(n);
	//				//Note note=new Note(7,"Blaaaaaaaaaaaaaaaaa", "Michael");
	//				DevelopmentNeed devNeed1=new DevelopmentNeed(8,2,"Title21", "Description 21", "2017-03");
	//				DevelopmentNeed devNeed2=new DevelopmentNeed(10,0,"Title2", "Description2");
	//				//e.setDevelopmentNeedsList(new ArrayList<List<DevelopmentNeed>>());
	//				//e.addDevelopmentNeed(devNeed1);
	//				//e.addDevelopmentNeed(devNeed2);
	//				//datastore.findAndDelete(querySearch);
	//				UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("emailAddress", email);
	//				//Commit the changes to the DB
	//				dbConnection.update(querySearch, ops);
	//				//datastore.save(e);
	//			}
	//		}
	//		catch(Exception re){
	//			System.err.println(re);
	//		}
	//	}

	public static boolean validateGroupFeedbackRequestID(long employeeID, String id) throws InvalidAttributeValueException{
		if(employeeID>0 && !id.equals("")){
			if(dbConnection==null)
				dbConnection=getMongoDBConnection();
			//Retrieve Employee with the given ID
			Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
			if(querySearch.get()!=null){
				Employee e = querySearch.get();
				//Extract its List of notes
				List<GroupFeedbackRequest> requests=e.getGroupFeedbackRequestsList();
				//Check if the feedbackID is already contained within the system
				for(GroupFeedbackRequest f: requests){
					if(f.getID().equals(id))
						return false;
				}
				return true;
			}
			else{
				throw new InvalidAttributeValueException("The given Employee ID is invalid");
			}
		}
		else
			throw new InvalidAttributeValueException("The given EmployeeID or FeedbackRequestID are invalid");
	}

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
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
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
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The given EmployeeID or competency title are invalid");
		return false;
	}

	public static ADProfile_Basic matchADWithMongoData(ADProfile_Advanced userData) throws InvalidAttributeValueException{
		if(userData!=null){
			//Establish a connection with the DB
			if(dbConnection==null)
				dbConnection=getMongoDBConnection();
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
				return new ADProfile_Basic(userData.getEmployeeID(),userData.getSurname(), userData.getForename(), userData.getIsManager(), userData.getUsername());
			}
			else
				throw new InvalidAttributeValueException("The user GUID is undefined");
		}else
			throw new InvalidAttributeValueException("The data provided is not valid");
	}

	public static boolean linkFeedbackReqReplyToUserGroupFBReq(String requester, String fbReqID, Feedback data) throws InvalidAttributeValueException, NamingException{
		if(data!=null && data.isFeedbackValid()){
			//Establish a connection with the DB
			if(dbConnection==null)
				dbConnection=getMongoDBConnection();

			//Get the userData from the DB/AD as well as updating the user profile if needed
			ADProfile_Basic basicProfile=ADProfileDAO.authenticateUserProfile(requester);
			//Search for the feedbackReqID inside the user data (using the employeeID inside the basicProfile
			//Get the user from the DB
			Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", basicProfile.getEmployeeID());
			if(querySearch.get()!=null){
				Employee e = querySearch.get();
				//Retrieve specific GRoupFeedbackRequest object from user data
				GroupFeedbackRequest groupFBReq=e.getSpecificGroupFeedbackRequest(fbReqID.trim());
				
				boolean flag=false;
				try{
					data.setID(getLatestFeedbackIDForUser(basicProfile.getEmployeeID())+1);
				}
				catch(Exception error){
					data.setID(1);
				}
				//Verify that something has been returned, if so, we have the feedback request object, let's add the feedback to it
				if(groupFBReq!=null){
					flag=true;
					FeedbackRequest feedbackReqUpdated=e.getSpecificFeedbackRequestFromGroupFBRequests(fbReqID.trim());
					//Add the feedback to the current feedback request object 
					//Add the general feedback to the user
					e.addGenericFeedback(data);
					boolean res1=feedbackReqUpdated.addReply(data);
					//Update the feedback request object inside the group feedback request object
					boolean res2=groupFBReq.updateFeedbackRequest(feedbackReqUpdated);
					boolean res3=e.updateGroupFeedbackRequest(groupFBReq);
					//Verify if the operations have been completed successfully
					if(!res1 || !res2 || !res3)
						throw new InvalidAttributeValueException("Something went wrong while adding the feedback to user "+basicProfile.getEmployeeID());
					//Update the data in the DB
					UpdateOperations<Employee> ops2 = dbConnection.createUpdateOperations(Employee.class).set("groupFeedbackRequests", e.getGroupFeedbackRequestsList());
					//Commit the changes to the DB
					dbConnection.update(querySearch, ops2);
				}

				if(!flag){
					data.setIsRequested(false);
					//Add the general feedback to the user
					e.addGenericFeedback(data);
				}
				//Update the user data in the DB
				UpdateOperations<Employee> ops1 = dbConnection.createUpdateOperations(Employee.class).set("feedback", e.getAllFeedback());
				dbConnection.update(querySearch, ops1);
				//}
				return true;
			}
			//Since we use the authenticateUSerProfile method, the system will never go in the else statement
			//else{}
		}
		else{
			throw new InvalidAttributeValueException("Invalid Feedback information");
		}
		return false;
	}

	//	public static boolean removeEmailAddressFromFeedbackReq(String email, String reqID, String addressToRemove) throws InvalidAttributeValueException{
	//		if(email.length()<0 || reqID.length()<0)
	//			throw new InvalidAttributeValueException("The email address provided or the request ID are invalid");
	//		//Establish a connection with the DB
	//		if(dbConnection==null)
	//			dbConnection=getMongoDBConnection();
	//		//Get the user from the DB
	//		Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("emailAddress =", email);
	//		if(querySearch.get()!=null){
	//			Employee e = querySearch.get();
	//			FeedbackRequest requestObj=e.getSpecificGroupFeedbackRequest(reqID);
	//			if(requestObj!=null){
	//				return requestObj.removeRecipient(addressToRemove);
	//			}
	//			throw new InvalidAttributeValueException("No feedback request matched the given ID");
	//		}
	//		else{
	//			throw new InvalidAttributeValueException("No user with such email address has been found in the DB");
	//		}
	//	}

	private static Datastore getMongoDBConnection() throws MongoException{
		if(dbConnection==null){
			String mongoClientURI = "mongodb://"
					+ "" + Constants.MONGODB_USERNAME + ":"
					+ "" + Constants.MONGODB_PASSWORD + "@"
					+ "" + Constants.MONGODB_HOST + ":"
					+ "" + Constants.MONGODB_PORT + "/"
					+ "" + Constants.MONGODB_DB_NAME;
			MongoClient client = new MongoClient(new MongoClientURI(mongoClientURI));
			final Morphia morphia =new Morphia();
			//client.getMongoOptions().setMaxWaitTime(10);
			//Add packages
			morphia.mapPackage("dataStructure.Employee");
			dbConnection=morphia.createDatastore(client, Constants.MONGODB_DB_NAME);
			dbConnection.ensureIndexes();
		}
		return dbConnection;
	}
}
