package services;

import static dataStructure.Constants.DEV_SERVER_NAME;
import static dataStructure.Constants.LIVE_SERVER_NAME;
import static dataStructure.Constants.UAT_SERVER_NAME;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import externalServices.ews.IMAPService;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {
	
    public static void main(String[] args) {
    	System.out.println("Welcome! :)");
    	System.out.println("MyCareer is booting... It won't take a while!");
    	    	
		try {
			setEnvironmentProperty();
			//Start the Restful WebService
			SpringApplication.run(Application.class, args);
			//ConfigurableApplicationContext app=SpringApplication.run(Application.class, args);
			//SpringApplication.exit(app, ExitCodeGenerator.class);
			//Start the EmailService that checks for new emails and add the feedback to a user
			IMAPService.initiateIMAPService();
		} catch (Exception e) {
			System.err.println("Application Error: " + e.getMessage());
		}
		
		System.out.println("MyCareer is up and running! Enjoy ;)");
    }
    
    private static void setEnvironmentProperty() throws UnknownHostException {
		String environment;
		String host = InetAddress.getLocalHost().getHostName();
		
		switch (host) {
			case DEV_SERVER_NAME:
				environment = "dev";
				break;
			case UAT_SERVER_NAME:
				environment = "uat";
				break;
			case LIVE_SERVER_NAME:
				environment = "live";
				break;
			default:
				environment = "local";
				break;
		}
		
		System.setProperty("ENVIRONMENT", environment);
    }
}