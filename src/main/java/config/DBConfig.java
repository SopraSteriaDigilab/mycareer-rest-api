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
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import services.db.MongoOperations;
import services.db.MorphiaOperations;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
@Configuration
@PropertySource("${ENVIRONMENT}.properties")
public class DBConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DBConfig.class);

  @Autowired
  private Environment env;

  /**
   * 
   * TODO: Describe this method.
   *
   * @param client
   * @return
   * @throws MongoException
   */
  @Bean
  public Datastore datastore(final MongoClient client) throws MongoException
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
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public MongoOperations employeeOperations()
  {
    LOGGER.debug("Creating bean employeeOperations");

    return new MongoOperations(mongoClient(), EMPLOYEE);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public MongoOperations objectivesHistoriesOperations()
  {
    LOGGER.debug("Creating bean objectivesHistoriesOperations");

    return new MongoOperations(mongoClient(), OBJECTIVES_HISTORY);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public MongoOperations developmentNeedsHistoriesOperations()
  {
    LOGGER.debug("Creating bean developmentNeedsHistoriesOperations");

    return new MongoOperations(mongoClient(), DEVELOPMENT_NEEDS_HISTORY);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public MongoOperations competenciesHistoriesOperations()
  {
    LOGGER.debug("Creating bean competenciesHistoriesOperations");

    return new MongoOperations(mongoClient(), COMPETENCIES_HISTORY);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param datastore
   * @return
   */
  @Bean
  public MorphiaOperations morphiaOperations(final Datastore datastore)
  {
    LOGGER.debug("Creating bean morphiaOperations");

    return new MorphiaOperations(datastore(mongoClient()));
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public MongoClient mongoClient()
  {
    LOGGER.debug("Creating bean mongoClient");

    // TODO This currently doesn't fully work. the waitTime is not taken by the DB
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
