package functionalities;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

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

	
	public static List<Objective> getObjectivesForUser(int epmloyeeID){
		Datastore datastore = getMongoDBConnection();
		Query<Employee> query = datastore.createQuery(Employee.class).filter("employeeID =", epmloyeeID);
		Employee e = query.get();
		return e.getLatestVersionObjectives();
		
	}

//
//	public static void insertTempData(){
//		try{
//			Datastore datastore=getMongoDBConnection();
//			Employee e1=new Employee(2312,8,"Red","Red","red.red@soprasteria.com","Junior Software Engineer"
//					+ "",false,"Dhananjay Malpure",1111,"1993-09-14","2016-07-04",null,null,null);
//			//Create a list of feedback
//			Feedback fe1=new Feedback(1,2,"William","Wow, this guys is great at developing stuff","Internal","Email");
//			Feedback fe2=new Feedback(2,3,"Red","Well, I was born ready!","Internal","Yammer");
//			//Add the feedback to the employee object
//			e1.addGenericFeedback(fe1);
//			e1.addGenericFeedback(fe2);
//			//Create 2 Objectives
//			Objective o1=new Objective(1,0,1,"First Objective","This is the first objective that Michael has added to the system","2017-03");
//			Objective o2=new Objective(2,1,2,"Second Objective","This is the second objective that Michael has added to the system","2016-12");
//			//Add the objective to the employee object
//			e1.addObjective(o1);
//			e1.addObjective(o2);
//
//			//Add feedback to the objective
//			Feedback fe3=new Feedback(1,2,"William","Wow, this guys is great at developing stuff","Internal","Email");
//			Feedback fe4=new Feedback(2,3,"Red","Well, I was born ready!","Internal","Yammer");
//			e1.addFeedbackToObjective(2, fe3);
//			e1.addFeedbackToObjective(2, fe4);
//			e1.addFeedbackToObjective(1, fe4);
//
//			datastore.save(e1);
//		}
//		catch(Exception e){
//			System.out.println("There has been a problem!");
//		}
//	}
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
