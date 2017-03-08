package application;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.directory.SearchControls.*;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_AUTHENTICATION;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.directory.SearchControls;

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
import services.ad.ADSearchSettings;
import services.ad.ADSearchSettingsImpl;
import utils.sequence.Sequence;
import utils.sequence.SequenceException;
import utils.sequence.StringSequence;
import services.BulkUpdateService;
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
  private static final String TIMEOUT_ATTRIBUTE = "30000";
  private static final String AD_SOPRA_HOST = "ldap://duns.ldap-ad.dmsi.corp.sopra";
  private static final String AD_SOPRA_URL = AD_SOPRA_HOST.concat(":389");
  private static final String AD_SOPRA_USERNAME = "svc_mycareer@emea.msad.sopra";
  private static final String AD_SOPRA_PASSWORD = "N9T$SiPSZ";
  private static final String AD_SOPRA_BINARY_ATTRIBUTES = "objectGUID";
  private static final String AD_SOPRA_PRINCIPAL = AD_SOPRA_USERNAME;
  private static final String[] AD_SOPRA_ATTRIBUTES = { "sn", "givenName", "company", "sAMAccountName",
      "extensionAttribute7", "objectGUID", "mail", "department", "targetAddress", "memberOf" };

  private static final String AD_STERIA_HOST = "ldap://one.steria.dom";
  private static final String AD_STERIA_URL = AD_STERIA_HOST.concat(":389");
  private static final String AD_STERIA_USERNAME = "UK-SVC-CAREER";
  private static final String AD_STERIA_PASSWORD = "3I=AkSiGRr";
  private static final String AD_STERIA_LOGIN_TREE = "OU=Service Accounts,OU=UKCentral,OU=UK,OU=Resources,DC=one,DC=steria,DC=dom";
  private static final String AD_STERIA_PRINCIPAL = new StringBuilder("cn=").append(AD_STERIA_USERNAME).append(",")
      .append(AD_STERIA_LOGIN_TREE).toString();
  private static final String[] AD_STERIA_ATTRIBUTES = { "directReports", "extensionAttribute2", "sn", "givenName", "mail", "targetAddress",
      "company", "sAMAccountName", "department", "ou", "SteriaSectorUnit" };

  @Autowired
  private Environment env;

  @Bean
  public Datastore dBConnection() throws MongoException
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
  public EmployeeService employeeService()
  {
    return new EmployeeService(dBConnection(), employeeProfileService(), env);
  }

  @Bean
  public HRService hrDataDAO()
  {
    return new HRService(dBConnection());
  }
  
  @Bean
  public EmployeeProfileService employeeProfileService()
  {
    return new EmployeeProfileService(sopraADSearchSettings(), steriaADSearchSettings());
  }
  
  @Bean
  public BulkUpdateService bulkUpdateService() throws SequenceException
  {
    return new BulkUpdateService(employeeService(), steriaADSearchSettings(), steriaFilterSequence());
  }
  
  @Bean ADSearchSettings sopraADSearchSettings()
  {
    final ADSearchSettings sopraADSearchSettings = new ADSearchSettingsImpl(sopraSearchControls(), sopraADSettings());
    return sopraADSearchSettings;
  }
  
  @Bean ADSearchSettings steriaADSearchSettings()
  {
    final ADSearchSettings steriaADSearchSettings = new ADSearchSettingsImpl(steriaSearchControls(), steriaADSettings());
    return steriaADSearchSettings;
  }
  
  @Bean
  public Hashtable<String, String> sopraADSettings()
  {
    final Hashtable<String, String> sopraADSettings = new Hashtable<>();
    
    sopraADSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    sopraADSettings.put(PROVIDER_URL, AD_SOPRA_URL);
    sopraADSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    sopraADSettings.put(BINARY_ATTRIBUTES_KEY, AD_SOPRA_BINARY_ATTRIBUTES);
    sopraADSettings.put(SECURITY_PRINCIPAL, AD_SOPRA_PRINCIPAL);
    sopraADSettings.put(SECURITY_CREDENTIALS, AD_SOPRA_PASSWORD);
    sopraADSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);
    
    return sopraADSettings;
  }
  
  @Bean
  public Hashtable<String, String> steriaADSettings()
  {
    final Hashtable<String, String> steriaADSettings = new Hashtable<>();
    
    steriaADSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    steriaADSettings.put(PROVIDER_URL, AD_STERIA_URL);
    steriaADSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    steriaADSettings.put(SECURITY_PRINCIPAL, AD_STERIA_PRINCIPAL);
    steriaADSettings.put(SECURITY_CREDENTIALS, AD_STERIA_PASSWORD);
    steriaADSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);
    
    return steriaADSettings;
  }
  
  @Bean
  public SearchControls sopraSearchControls()
  {
    final SearchControls sopraSearchControls = new SearchControls();
    sopraSearchControls.setSearchScope(SUBTREE_SCOPE);
    sopraSearchControls.setReturningAttributes(AD_SOPRA_ATTRIBUTES);
    System.out.println("****** ****** Count limit was: " + sopraSearchControls.getCountLimit());
    sopraSearchControls.setCountLimit(10000);
    
    return sopraSearchControls;
  }
  
  @Bean
  public SearchControls steriaSearchControls()
  {
    final SearchControls steriaSearchControls = new SearchControls();
    steriaSearchControls.setSearchScope(SUBTREE_SCOPE);
    steriaSearchControls.setReturningAttributes(AD_STERIA_ATTRIBUTES);
    System.out.println("****** ****** Count limit was: " + steriaSearchControls.getCountLimit());
    steriaSearchControls.setCountLimit(10000);
    
    return steriaSearchControls;
  }
  
  @Bean
  public Sequence<String> steriaFilterSequence() throws SequenceException
  {
    return new StringSequence.StringSequenceBuilder()
                              .initial("CN=A*") // first call to next() will return this
                              .characterToChange(3) // 'A'
                              .increment(1) // increment by one character
                              .size(26) // 26 Strings in the sequence
                              .build();
  }
}
