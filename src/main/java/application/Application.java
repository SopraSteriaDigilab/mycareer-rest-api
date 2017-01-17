package application;

import static dataStructure.Constants.DEV_SERVER_NAME;
import static dataStructure.Constants.LIVE_SERVER_NAME;
import static dataStructure.Constants.UAT_SERVER_NAME;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

//import externalServices.ews.IMAPService;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
    public static void main(String[] args) {   	
		try {	
			logger.info("Setting the Environment");
			setEnvironmentProperty();
			//Start the Restful WebService
			logger.info("Starting the Spring Application");
			SpringApplication.run(Application.class, args);
			//Start the EmailService that checks for new emails and add the feedback to a user
			logger.info("Starting the Email Service");
//			IMAPService.initiateIMAPService();
		} catch (Exception e) {
			logger.error("Application Error: " + e.getMessage());
		}
		logger.info("MyCareer is running successfully on post 8080");
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