package application;

import static dataStructure.Constants.DEV_SERVER_NAME;
import static dataStructure.Constants.LIVE_SERVER_NAME;
import static dataStructure.Constants.UAT_SERVER_NAME;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages = {"application","services","utils", "controller"})
@EnableScheduling
public class Application
{

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws FileNotFoundException, IOException
  {
     try {
     setEnvironmentProperty();
     SpringApplication.run(Application.class, args);
     } catch (UnknownHostException e) {
     logger.error("Application Error: " + e.getMessage());
     }
  }

  private static void setEnvironmentProperty() throws UnknownHostException
  {
    String environment;
    String host = InetAddress.getLocalHost().getHostName();

    switch (host)
    {
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