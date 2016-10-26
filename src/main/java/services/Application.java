package services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import emailServices.IMAPService;
import functionalities.EmployeeDAO;

//@SpringBootApplication
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
    	System.out.println("Welcome! :)");
    	System.out.println("MyCareer is booting... It won't take a while!");
		try {
			//Start the Restful WebService
			SpringApplication.run(Application.class, args);
			//Start the EmailService that checks for new emails and add the feedback to a user
			//IMAPConfig.initiateIMAPService();
			//EmployeeDAO.changeEmployeeNotes(4323,"Piccoli","michael.piccoli.mp@gmail.com");
			//EmployeeDAO.changeEmployeeNotes(2312,"ridhwan.nacef@soprasteria.com");
			//EmployeeDAO.changeEmployeeNotes(3422,"William","Kenny","william.kenny@soprasteria.com");
			//EmployeeDAO.changeEmployeeNotes(2312);
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("Application Error: "+e.getMessage());
		}
    	//Objective o1=new Objective(0,1,"Third Objective","This is the third objective that Michael has added to the system","2016-12");
    	//EmployeeDAO.insertNewObjective(4323, o1);
    	//System.out.println(EmployeeDAO.getFeedbackForUser(4323));
		System.out.println("MyCareer is up and running! Enjoy ;)");
    }

}
/*
package services;

import java.net.UnknownHostException;
import java.util.ArrayList;
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

public class Application{

	public static void main(String[] args) throws UnknownHostException, MongoException {
		System.out.println("Welcome! :)");
		String mongoClientURI = "mongodb://" + "michael" + ":" + "leahcim" + "@" + "172.25.111.64" + ":" + 27777 + "/" + "Development";
		MongoClient client = new MongoClient(new MongoClientURI(mongoClientURI));
		final Morphia morphia =new Morphia();
		//Add packages
		morphia.mapPackage("dataStructure.Employee");
		//morphia.mapPackage("dataStructure.Feedback");
		//morphia.mapPackage("dataStructure.Note");
		//morphia.mapPackage("dataStructure.Objective");
		try{
			final Datastore datastore =morphia.createDatastore(client, "Development");
			datastore.ensureIndexes();
			//Create Employee Object

			Employee e1=new Employee(2342,8,"Michael","Piccoli","michael.piccoli@soprasteria.com","Junior Software Engineer"
					+ "",false,"Dhananjay Malpure",1111,"1993-09-14","2016-07-04",null,null,null);
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
			Query<Employee> query = datastore.createQuery(Employee.class).field("feedback.fromWho").contains("William");
			for(Employee e:query.asList()){
				System.out.println(e.toString());
				System.out.print("\n----------------------------------------------------------------\n");
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		//List<Employee> employees=query.asList();
	}

}
*/
