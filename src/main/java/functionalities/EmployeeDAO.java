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

import dataStructure.Constants;
import dataStructure.Employee;
import dataStructure.Feedback;
import dataStructure.Objective;

/**
 * 
 * @author Michael Piccoli
 * @version 1.0
 * @since 10th October 2016
 * 
 * This class contains the definition of the Note object
 *
 */
public  class EmployeeDAO {

	//There is only 1 instance of the Datastore in the whole system
	private static Datastore dbConnection=null;


	public static List<Objective> getObjectivesForUser(int employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getLatestVersionObjectives();

	}

	public static List<Feedback> getFeedbackForUser(int employeeID) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getFeedbackList();
	}

	public static int getUserIDFromEmailAddress(String email) throws InvalidAttributeValueException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		Query<Employee> query = dbConnection.createQuery(Employee.class).filter("emaiAddress =", email);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such Email");
		Employee e = query.get();
		return e.getEmployeeID();

	}


	/**
	 * 
	 * @param employeeID
	 * @return 
	 * This method inserts a new objective for a specific employee given their ID
	 * @throws InvalidAttributeValueException 
	 */
	public static boolean insertNewObjective(int employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		if(dbConnection==null)
			dbConnection=getMongoDBConnection();
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof Objective){
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = dbConnection.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()!=null){
					Employee e = querySearch.get();
					//Extract its List of Objectives
					List<List<Objective>> dataFromDB=e.getObjectiveList();
					//Add the new objective to the list
					if(e.addObjective((Objective)data)){
						//Update the List<List<objective>> in the DB passing the new list
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("objectives", dataFromDB);
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
//			UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("emaiAddress", email);
//			//Commit the changes to the DB
//			dbConnection.update(querySearch, ops);
//			return true;
//		}
//		return false;
//	}

	public static boolean insertNewGeneralFeedback(int employeeID, Object data)throws InvalidAttributeValueException, MongoException{
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
						UpdateOperations<Employee> ops = dbConnection.createUpdateOperations(Employee.class).set("feedback", dataFromDB);
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

	public static boolean addNewVersionObjective(int employeeID, int objectiveID, Object data) throws InvalidAttributeValueException{
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

	public static Datastore getMongoDBConnection() throws MongoException{
		if(dbConnection==null){
			String mongoClientURI = "mongodb://"
					+ "" + Constants.MONGODB_USERNAME + ":"
					+ "" + Constants.MONGODB_PASSWORD + "@"
					+ "" + Constants.MONGODB_HOST + ":"
					+ "" + Constants.MONGODB_PORT + "/"
					+ "" + Constants.MONGODB_COLECTION_NAME;
			MongoClient client = new MongoClient(new MongoClientURI(mongoClientURI));
			final Morphia morphia =new Morphia();
			//client.getMongoOptions().setMaxWaitTime(10);
			//Add packages
			morphia.mapPackage("dataStructure.Employee");
			dbConnection=morphia.createDatastore(client, Constants.MONGODB_COLECTION_NAME);
			dbConnection.ensureIndexes();
		}
		return dbConnection;
	}
}
