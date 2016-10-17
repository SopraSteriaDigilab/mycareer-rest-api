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


	public static List<Objective> getObjectivesForUser(int epmloyeeID) throws InvalidAttributeValueException{
		Datastore datastore = getMongoDBConnection();
		Query<Employee> query = datastore.createQuery(Employee.class).filter("employeeID =", epmloyeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getLatestVersionObjectives();

	}

	public static List<Feedback> getFeedbackForUser(int employeeID) throws InvalidAttributeValueException{

		Datastore datastore = getMongoDBConnection();
		Query<Employee> query = datastore.createQuery(Employee.class).filter("employeeID =", employeeID);
		if(query.get()==null)
			throw new InvalidAttributeValueException("No user with such ID");
		Employee e = query.get();
		return e.getFeedbackList();
	}//getFeedbackForUser


	/**
	 * 
	 * @param employeeID
	 * @return 
	 * This method inserts a new objective for a specific employee given their ID
	 * @throws InvalidAttributeValueException 
	 */
	public static boolean insertNewObjective(int employeeID, Object data) throws InvalidAttributeValueException, MongoException{
		//Check the employeeID
		if(employeeID>0){
			if(data!=null && data instanceof Objective){
				//Connect to the DB
				Datastore conn=getMongoDBConnection();
				//Retrieve Employee with the given ID
				Query<Employee> querySearch = conn.createQuery(Employee.class).filter("employeeID =", employeeID);
				if(querySearch.get()==null)
					throw new InvalidAttributeValueException("No user with such ID");
				Employee e = querySearch.get();
				if(e!=null){
					//Extract its List of Objectives
					List<List<Objective>> dataFromDB=e.getObjectiveList();
					//Add the new objective to the list
					if(e.addObjective((Objective)data)){
						//Update the List<objective> in the DB passing the new list
						UpdateOperations<Employee> ops = conn.createUpdateOperations(Employee.class).set("objectives", dataFromDB);
						//Commit the changes to the DB
						conn.update(querySearch, ops);
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

		}else
			throw new InvalidAttributeValueException("The ID provided is not valid");
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
	//
	//	public static void getData(){
	//		try{
	//			Datastore datastore=getMongoDBConnection();
	//			Query<Employee> query = datastore.createQuery(Employee.class).filter("employeeID =", 2342);
	//			for(Employee e:query.asList()){
	//				System.out.println(e.toString());
	//				System.out.print("\n----------------------------------------------------------------\n");
	//			}
	//		}
	//		catch(Exception e){
	//			System.out.println("There has been a problem!");
	//		}
	//	}


	private static Datastore getMongoDBConnection() throws MongoException{
		String mongoClientURI = "mongodb://" + "michael" + ":" + "leahcim" + "@" + "172.25.111.64" + ":" + 27777 + "/" + "Development";
		MongoClient client = new MongoClient(new MongoClientURI(mongoClientURI));
		final Morphia morphia =new Morphia();
		//Add packages
		morphia.mapPackage("dataStructure.Employee");
		final Datastore datastore =morphia.createDatastore(client, "Development");
		datastore.ensureIndexes();
		return datastore;
	}
}
