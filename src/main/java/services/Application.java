/*package services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	System.out.println("Welcome! :)");
    	System.out.println("MyCareer is booting... It won't take a while!");
        @SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("MyCareer is up and running! Enjoy ;)");
    }

} */

package services;

import java.util.List;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dataStructure.Employee;
import dataStructure.Feedback;
import dataStructure.Note;
import dataStructure.Objective;

public class Application {

	public static void main(String[] args) {
		System.out.println("Welcome! :)");
		String mongoClientURI = "mongodb://" + "michael" + ":" + "leahcim" + "@" + "172.25.111.64" + ":" + 27777 + "/" + "Development";;
		@SuppressWarnings("resource")
		MongoClient client = new MongoClient(new MongoClientURI(mongoClientURI));
		//Get the Development DB
		MongoDatabase db=client.getDatabase("Development");
		//Retrieve the Collection we will be working on
		MongoCollection<Document> collection = db.getCollection("employeeDataDev");
		
		//Write the first object to the collection
		Employee e1=new Employee(2342,8,"Michael","Piccoli","michael.piccoli@soprasteria.com","Junior Software Engineer"
				+ "",false,"Dhananjay Malpure",1111,"1993-09-14","2016-07-04",null,null,null);
		String jsonFormat=e1.toGson();
		System.out.println(jsonFormat);
		//collection.insertOne(jsonFormat);
		
	}

}
