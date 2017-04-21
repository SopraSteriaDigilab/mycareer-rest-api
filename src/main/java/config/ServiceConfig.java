package config;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import services.DataService;
import services.EmployeeProfileService;
import services.EmployeeService;
import services.HRService;
import services.HistoryService;
import services.ManagerService;
import services.ad.ADSearchSettings;
import services.db.MongoOperations;
import services.db.MorphiaOperations;

@Configuration
@PropertySource("${ENVIRONMENT}.properties")
public class ServiceConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfig.class);

  @Autowired
  private Environment env;

  @Autowired
  private Datastore datastore;

  @Autowired
  private MorphiaOperations morphiaOperations;

  @Autowired
  private MongoOperations employeeOperations;

  @Autowired
  private MongoOperations objectivesHistoriesOperations;

  @Autowired
  private MongoOperations developmentNeedsHistoriesOperations;

  @Autowired
  private MongoOperations competenciesHistoriesOperations;

  @Autowired
  private ADSearchSettings sopraADSearchSettings;

  @Bean
  public EmployeeService employeeService()
  {
    return new EmployeeService(morphiaOperations, employeeOperations, objectivesHistoriesOperations, developmentNeedsHistoriesOperations,
        competenciesHistoriesOperations, employeeProfileService(), env);
  }

  @Bean
  public EmployeeProfileService employeeProfileService()
  {
    return new EmployeeProfileService(morphiaOperations, employeeOperations, sopraADSearchSettings);
  }

  @Bean
  public ManagerService managerService()
  {
    return new ManagerService(employeeService(), morphiaOperations, employeeOperations, objectivesHistoriesOperations,
        employeeProfileService(), env);
  }
  
  @Bean
  public HistoryService historyService()
  {
    return new HistoryService(morphiaOperations);
  }

  @Bean
  public HRService hrDataDAO()
  {
    return new HRService(datastore);
  }

  @Bean
  public DataService dataService()
  {
    return new DataService(employeeOperations);
  }
}
