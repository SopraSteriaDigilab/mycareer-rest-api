package application;

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

/**
 * Container for the {@code main(String[])} method which starts the application.
 */
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
@ComponentScan(basePackages = { "application", "config", "services", "utils", "controller" })
@EnableScheduling
public class Application
{
  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  private static final String DEV_SERVER_NAME = "ldunsmycareerdev01";
  private static final String UAT_SERVER_NAME = "ldunsmycareeruat01";
  private static final String LIVE_SERVER_NAME = "ldunsmycareer01";

  /**
   * Sets the environment property and runs the Spring application.
   * 
   * @param args Not used
   */
  public static void main(String[] args)
  {
    try
    {
      setEnvironmentProperty();
      SpringApplication.run(Application.class, args);

    }
    catch (UnknownHostException e)
    {
      LOGGER.error("Application Error: " + e.getMessage());
    }
  }

  // Sets the environment variable based on where the application is deployed
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