package services;

import java.util.Iterator;

import org.mongodb.morphia.aggregation.Projection;
import org.mongodb.morphia.aggregation.Sort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import dataStructure.Employee;
import emailServices.IMAPService;
import functionalities.EmployeeDAO;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
    	System.out.println("Welcome! :)");
    	System.out.println("MyCareer is booting... It won't take a while!");
		try {
			//Start the Restful WebService
			SpringApplication.run(Application.class, args);
			//Start the EmailService that checks for new emails and add the feedback to a user
			//IMAPService.initiateIMAPService();
		} catch (Exception e) {
			System.err.println("Application Error: "+e.getMessage());
		}
		System.out.println("MyCareer is up and running! Enjoy ;)");
    }
}