package functionalities;

import java.util.List;
import javax.management.InvalidAttributeValueException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

import dataStructure.Competency;
import dataStructure.Constants;
import dataStructure.DevelopmentNeed;
import dataStructure.Employee;
import dataStructure.Feedback;
import dataStructure.FeedbackRequest;
import dataStructure.Note;
import dataStructure.Objective;

/**
 * 
 * @author Michael Piccoli
 * @author Christopher Kai
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Note object
 *
 */
public  class EmployeeDAO {

	//There is only 1 instance of the Datastore in the whole system
	private static Datastore dbConnection=null;


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
		return e.getFeedbackList();
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

	public static List<FeedbackRequest> getFeedbackRequestsForUser(long employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getFeedbackRequestsList();
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
					List<Feedback> dataFromDB=e.getFeedbackList();
					//Verify if the feedback is already within the user DB to avoid adding duplicated feedback
					for(Feedback f:dataFromDB){
						if(f.compare((Feedback)data))
							throw new InvalidAttributeValueException("The given feedback is a duplicate");
					}
					//Add the new objective to the list
					if(e.addGenericFeedback((Feedback)data)){
						//Update the List<List<objective>> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedback", e.getFeedbackList());
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

	public static boolean insertNewFeedbackRequest(long employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof FeedbackRequest){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Add the new FeedbackRequest to the list
					if(e.addFeedbackRequest((FeedbackRequest)data)){
						//Update the List<FeedbackRequest> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests", e.getFeedbackRequestsList());
						//Commit the changes to the DB
						dbConnection.update(querySearch, ops);
						return true;
					}
					else
						throw new InvalidAttributeValueException("The given object couldn't be added to the feeback request list");
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

	public static boolean updateFeedbackRequest(long employeeID, int feedbackReqID, Object data) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		//Check EmployeeID and feedbackReqID
		if(employeeID>0 && feedbackReqID>0){
			if(data!=null && data instanceof FeedbackRequest){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Extract its List of feedback Requests
					List<FeedbackRequest> requestsList=e.getFeedbackRequestsList();
					//Search for the objective Id within the list of notes
					int indexFeedReqList=-1;
					for(int i=0; i<requestsList.size(); i++){
						//Save the index of the list when the noteID is found
						if(requestsList.get(i).getID().equals(feedbackReqID)){
							indexFeedReqList=i;
							//Exit the for loop once the value has been found
							break;
						}
					}
					//verify that the index variable has changed its value
					if(indexFeedReqList!=-1){ 
						//Add the updated version of the note
						if(e.updateFeedbackRequest((FeedbackRequest)data)){
							//Update the List<List<Note>> in the DB passing the new list
							UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedbackRequests", e.getFeedbackRequestsList());
							//Commit the changes to the DB
							dbConnection.update(querySearch, ops);
							return true;
						}
					}
					//if the index hasn't changed its value it means that there is no feedback request with such ID, therefore throw and exception
					else
						throw new InvalidAttributeValueException("The given ID associated with the feedback request is invalid");
				}
				else
					throw new InvalidAttributeValueException("No employee found with the ID provided");
			}
			else
				throw new InvalidAttributeValueException("The data provided is not valid");
		}
		else
			throw new InvalidAttributeValueException("The given EmployeeID or feedbackReqID are invalid");
		return false;
	}

	/*
	public static void insertTempData(){
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

	public static boolean validateFeedbackRequestID(long employeeID, String id) throws InvalidAttributeValueException{
		if(employeeID>0 && !id.equals("")){
			if(dbConnection==null)
				dbConnection=getMongoDBConnection();
			//Retrieve Employee with the given ID
			Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
			if(querySearch.get()!=null){
				Employee e = querySearch.get();
				//Extract its List of notes
				List<FeedbackRequest> requests=e.getFeedbackRequestsList();
				//Check if the feedbackID is already contained within the system
				for(FeedbackRequest f: requests){
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
						//Update the List<List<Note>> in the DB passing the new list
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
