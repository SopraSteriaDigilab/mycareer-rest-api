package services;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import emailServices.IMAPService;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
    	System.out.println("Welcome! :)");
    	System.out.println("MyCareer is booting... It won't take a while!");
		try {
			//Start the Restful WebService
			SpringApplication.run(Application.class, args);
			//ConfigurableApplicationContext app=SpringApplication.run(Application.class, args);
			//SpringApplication.exit(app, ExitCodeGenerator.class);
			//Start the EmailService that checks for new emails and add the feedback to a user
			//IMAPService.initiateIMAPService();
			//SendRandomEmails.sendRandomEmail("michael.piccoli@soprasteria.com",500);
		} catch (Exception e) {
			System.err.println("Application Error: "+e.getMessage());
		}
		System.out.println("MyCareer is up and running! Enjoy ;)");
    }
}