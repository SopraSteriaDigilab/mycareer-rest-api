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

@Configuration
@PropertySource("${ENVIRONMENT}.properties")
public class DBConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DBConfig.class);
  
  @Autowired
  private Environment env;
  
  @Bean
  public Datastore datastore(final MongoClient client) throws MongoException
  {
    Datastore dbConnection;
    final Morphia morphia = new Morphia();
    morphia.mapPackage("dataStructure.Employee");
    dbConnection = morphia.createDatastore(client, env.getProperty("db.name"));
    dbConnection.ensureIndexes();

    return dbConnection;
  }
  
  @Bean
  public MongoOperations employeeOperations()
  {
    return new MongoOperations(mongoClient(), EMPLOYEE);
  }
  
  @Bean
  public MongoOperations objectivesHistoriesOperations()
  {
    return new MongoOperations(mongoClient(), OBJECTIVES_HISTORY);
  }
  
  @Bean
  public MongoOperations developmentNeedsHistoriesOperations()
  {
    return new MongoOperations(mongoClient(), DEVELOPMENT_NEEDS_HISTORY);
  }
  
  @Bean
  public MongoOperations competenciesHistoriesOperations()
  {
    return new MongoOperations(mongoClient(), COMPETENCIES_HISTORY);
  }
  
  @Bean
  public MorphiaOperations morphiaOperations(final Datastore datastore)
  {
    return new MorphiaOperations(datastore(mongoClient()));
  }
  
  @Bean
  public MongoClient mongoClient()
  {
    // This currently doesn't fully work. the waitTime is not taken by the DB
    MongoClientOptions options = MongoClientOptions.builder()
                                                  .maxWaitTime(10000)
                                                  .connectTimeout(10000)
                                                  .build();
    List<ServerAddress> serverList = new ArrayList<>();
    serverList.add(new ServerAddress(env.getProperty("db.host1"), Integer.parseInt(env.getProperty("db.host1.port"))));
    serverList.add(new ServerAddress(env.getProperty("db.host2"), Integer.parseInt(env.getProperty("db.host2.port"))));
    serverList.add(new ServerAddress(env.getProperty("db.host3"), Integer.parseInt(env.getProperty("db.host3.port"))));
    MongoCredential credentials = MongoCredential.createCredential(env.getProperty("db.username"),
                                                                  env.getProperty("db.name"),
                                                                  env.getProperty("db.password").toCharArray());
    List<MongoCredential> credentialList = new ArrayList<>();
    credentialList.add(credentials);
    MongoClient client = new MongoClient(serverList, credentialList, options);
    
    return client;
  }
}
