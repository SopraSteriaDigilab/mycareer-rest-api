package application;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_AUTHENTICATION;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;

import java.util.ArrayList;
import java.util.Hashtable;
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
import services.EmployeeProfileService;
import services.EmployeeService;

@Configuration
@PropertySource("${ENVIRONMENT}.properties")
public class EnvironmentSetup
{
  private static final String AUTHENTICATION = "simple";
  private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
  private static final String BINARY_ATTRIBUTES_KEY = "java.naming.ldap.attributes.binary";
  private static final String TIMEOUT_ATTRIBUTE_KEY = "com.sun.jndi.ldap.read.timeout";
  private static final String TIMEOUT_ATTRIBUTE = "10000";
  private static final String AD_SOPRA_HOST = "ldap://duns.ldap-ad.dmsi.corp.sopra";
  private static final String AD_SOPRA_URL = AD_SOPRA_HOST.concat(":389");
  private static final String AD_SOPRA_USERNAME = "svc_mycareer@emea.msad.sopra";
  private static final String AD_SOPRA_PASSWORD = "N9T$SiPSZ";
  private static final String AD_SOPRA_BINARY_ATTRIBUTES = "objectGUID";
  private static final String AD_SOPRA_PRINCIPAL = AD_SOPRA_USERNAME;
  private static final String AD_STERIA_HOST = "ldap://one.steria.dom";
  private static final String AD_STERIA_URL = AD_STERIA_HOST.concat(":389");
  private static final String AD_STERIA_USERNAME = "UK-SVC-CAREER";
  private static final String AD_STERIA_PASSWORD = "3I=AkSiGRr";
  private static final String AD_STERIA_LOGIN_TREE = "OU=Service Accounts,OU=UKCentral,OU=UK,OU=Resources,DC=one,DC=steria,DC=dom";
  private static final String AD_STERIA_PRINCIPAL = new StringBuilder("cn=").append(AD_STERIA_USERNAME).append(",")
      .append(AD_STERIA_LOGIN_TREE).toString();

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
    return new EmployeeService(getMongoDBConnection(), employeeProfileService(), env);
  }

  @Bean
  public HRService hrDataDAO()
  {
    return new HRService(getMongoDBConnection());
  }
  
  @Bean
  public EmployeeProfileService employeeProfileService()
  {
    return new EmployeeProfileService(sopraADEnvironmentSettings(), steriaADEnvironmentSettings());
  }
  
  @Bean
  public Hashtable<String, String> sopraADEnvironmentSettings()
  {
    final Hashtable<String, String> sopraADEnvironmentSettings = new Hashtable<>();
    
    sopraADEnvironmentSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    sopraADEnvironmentSettings.put(PROVIDER_URL, AD_SOPRA_URL);
    sopraADEnvironmentSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    sopraADEnvironmentSettings.put(BINARY_ATTRIBUTES_KEY, AD_SOPRA_BINARY_ATTRIBUTES);
    sopraADEnvironmentSettings.put(SECURITY_PRINCIPAL, AD_SOPRA_PRINCIPAL);
    sopraADEnvironmentSettings.put(SECURITY_CREDENTIALS, AD_SOPRA_PASSWORD);
    sopraADEnvironmentSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);
    
    return sopraADEnvironmentSettings;
  }
  
  @Bean
  public Hashtable<String, String> steriaADEnvironmentSettings()
  {
    final Hashtable<String, String> steriaADEnvironmentSettings = new Hashtable<>();
    
    steriaADEnvironmentSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    steriaADEnvironmentSettings.put(PROVIDER_URL, AD_STERIA_URL);
    steriaADEnvironmentSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    steriaADEnvironmentSettings.put(SECURITY_PRINCIPAL, AD_STERIA_PRINCIPAL);
    steriaADEnvironmentSettings.put(SECURITY_CREDENTIALS, AD_STERIA_PASSWORD);
    steriaADEnvironmentSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);
    
    return steriaADEnvironmentSettings;
  }
}
