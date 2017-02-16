package application;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
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

import services.HRService;
import services.EmployeeService;

@Configuration
@PropertySource("${ENVIRONMENT}.properties")
public class EnvironmentSetup
{

  @Autowired
  private Environment env;

  @Bean
  public Datastore getMongoDBConnection() throws MongoException
  {
    Datastore dbConnection;
    // This currently doesn't fully work. the waitTime is not taken by the DB
    MongoClientOptions options = MongoClientOptions.builder().maxWaitTime(10000)
        // .maxConnectionIdleTime(1000)
        .connectTimeout(10000)
        // .socketKeepAlive(true)
        // .cursorFinalizerEnabled(true)
        // .socketTimeout(100)
        // .readConcern(ReadConcern.MAJORITY)
        // .writeConcern(WriteConcern.ACKNOWLEDGED)
        .build();
    // Server details
    List<ServerAddress> serverList = new ArrayList<>();
    // UAT DB1, UAT DB2, UAT
    serverList.add(new ServerAddress(env.getProperty("db.host1"), Integer.parseInt(env.getProperty("db.host1.port"))));
    serverList.add(new ServerAddress(env.getProperty("db.host2"), Integer.parseInt(env.getProperty("db.host2.port"))));
    serverList.add(new ServerAddress(env.getProperty("db.host3"), Integer.parseInt(env.getProperty("db.host3.port"))));
    // Setup the credentials
    MongoCredential credentials = MongoCredential.createCredential(env.getProperty("db.username"),
        env.getProperty("db.name"), env.getProperty("db.password").toCharArray());
    List<MongoCredential> credentialList = new ArrayList<>();
    credentialList.add(credentials);
    // Instantiate mongo client and Morphia
    MongoClient client = new MongoClient(serverList, credentialList, options);
    final Morphia morphia = new Morphia();
    // Add packages to Morphia and open the connection
    morphia.mapPackage("dataStructure.Employee");
    dbConnection = morphia.createDatastore(client, env.getProperty("db.name"));
    dbConnection.ensureIndexes();

    return dbConnection;
  }

  @Bean
  public EmployeeService employeeDAO()
  {
    return new EmployeeService(getMongoDBConnection(), env);
  }

  @Bean
  public HRService hrDataDAO()
  {
    return new HRService(getMongoDBConnection());
  }
}
