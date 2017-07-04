package config;

import static services.db.MongoOperations.Collection.*;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import services.db.MongoOperations;
import services.db.MorphiaOperations;

/**
 * Spring Configuration class for spring beans related to the database
 * 
 * @see MorphiaOperations
 * @see MongoOperations
 */
@Configuration
@PropertySource("classpath:${ENVIRONMENT}.properties")
public class DBConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DBConfig.class);

  @Autowired
  private Environment env;

  /**
   * Spring bean definition for the Morphia Datastore.
   * 
   * Constructs a {@code Morphia} instance, and creates and returns its corresponding {@code Datastore} object. Builds
   * indexes before returning.
   *
   * @param client the Mongo client object used to create the Datastore
   * @return the Datastore
   */
  @Bean
  public Datastore datastore(final MongoClient client)
  {
    LOGGER.debug("Creating bean datastore");

    Datastore dbConnection;
    final Morphia morphia = new Morphia();
    morphia.mapPackage("dataStructure.Employee");
    dbConnection = morphia.createDatastore(client, env.getProperty("db.name"));
    dbConnection.ensureIndexes();

    return dbConnection;
  }

  /**
   * Spring bean definition for Mongo operations performed on a collection named "employees".
   *
   * @return a MongoOperations instance for the employees collection
   * @see MongoOperations
   */
  @Bean
  public MongoOperations employeeOperations()
  {
    LOGGER.debug("Creating bean employeeOperations");

    return new MongoOperations(mongoClient(), EMPLOYEES);
  }

  /**
   * Spring bean definition for Mongo operations performed on a collection named objectivesHistories.
   *
   * @return a MongoOperations instance for the objectivesHistories collection
   * @see MongoOperations
   */
  @Bean
  public MongoOperations objectivesHistoriesOperations()
  {
    LOGGER.debug("Creating bean objectivesHistoriesOperations");

    return new MongoOperations(mongoClient(), OBJECTIVES_HISTORY);
  }

  /**
   * Spring bean definition for Mongo operations performed on a collection named developmentNeedsHistories.
   *
   * @return a MongoOperations instance for the developmentNeedsHistories collection
   * @see MongoOperations
   */
  @Bean
  public MongoOperations developmentNeedsHistoriesOperations()
  {
    LOGGER.debug("Creating bean developmentNeedsHistoriesOperations");

    return new MongoOperations(mongoClient(), DEVELOPMENT_NEEDS_HISTORY);
  }

  /**
   * Spring bean definition for Mongo operations performed on a collection named competenciesHistories.
   *
   * @return a MongoOperations instance for the competenciesHistories collection
   * @see MongoOperations
   */
  @Bean
  public MongoOperations competenciesHistoriesOperations()
  {
    LOGGER.debug("Creating bean competenciesHistoriesOperations");

    return new MongoOperations(mongoClient(), COMPETENCIES_HISTORY);
  }

  /**
   * Spring bean definition for operations performed by Morphia on the given Datastore
   *
   * @param datastore the Datastore that operatiosn are to be performed on
   * @return the MorphiaOperations instance
   * @see MorphiaOperations
   */
  @Bean
  public MorphiaOperations morphiaOperations(final Datastore datastore)
  {
    LOGGER.debug("Creating bean morphiaOperations");

    return new MorphiaOperations(datastore(mongoClient()));
  }

  /**
   * Spring bean definition for the Mongo client defined in the "${ENVIRONMENT}.properties" file.
   *
   * @return the MongoClient
   * @see MongoClient
   */
  @Bean
  public MongoClient mongoClient()
  {
    LOGGER.debug("Creating bean mongoClient");

    MongoClientOptions options = MongoClientOptions.builder().maxWaitTime(10_000).connectTimeout(10_000).build();
    List<ServerAddress> serverList = new ArrayList<>();
    serverList.add(new ServerAddress(env.getProperty("db.host1"), Integer.parseInt(env.getProperty("db.host1.port"))));
    serverList.add(new ServerAddress(env.getProperty("db.host2"), Integer.parseInt(env.getProperty("db.host2.port"))));
    serverList.add(new ServerAddress(env.getProperty("db.host3"), Integer.parseInt(env.getProperty("db.host3.port"))));
    MongoCredential credentials = MongoCredential.createCredential(env.getProperty("db.username"),
        env.getProperty("db.name"), env.getProperty("db.password").toCharArray());
    List<MongoCredential> credentialList = new ArrayList<>();
    credentialList.add(credentials);
    MongoClient client = new MongoClient(serverList, credentialList, options);

    return client;
  }
}
