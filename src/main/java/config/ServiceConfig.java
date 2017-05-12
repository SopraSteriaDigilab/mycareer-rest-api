package config;

import java.util.HashMap;

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
import services.ews.Cache;
import services.ews.DistributionList;
import services.ews.DistributionListCache;
import services.ews.DistributionListService;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
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

  @Autowired
  private ADSearchSettings steriaADSearchSettings;

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public EmployeeService employeeService()
  {
    LOGGER.debug("Creating bean employeeService");

    return new EmployeeService(morphiaOperations, employeeOperations, objectivesHistoriesOperations,
        developmentNeedsHistoriesOperations, competenciesHistoriesOperations, employeeProfileService(), env);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public EmployeeProfileService employeeProfileService()
  {
    LOGGER.debug("Creating bean employeeProfileService");

    return new EmployeeProfileService(morphiaOperations, employeeOperations, sopraADSearchSettings);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public ManagerService managerService()
  {
    LOGGER.debug("Creating bean managerService");

    return new ManagerService(employeeService(), morphiaOperations, employeeOperations, objectivesHistoriesOperations,
        employeeProfileService(), env);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public DistributionListService distributionListService()
  {
    LOGGER.debug("Creating bean distributionListService");

    return new DistributionListService(distributionListCache(), employeeProfileService(), sopraADSearchSettings,
        steriaADSearchSettings);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public Cache<String, DistributionList> distributionListCache()
  {
    LOGGER.debug("Creating bean distributionListCache");

    return new DistributionListCache(new HashMap<String, DistributionList>());
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public HistoryService historyService()
  {
    LOGGER.debug("Creating bean historyService");

    return new HistoryService(morphiaOperations);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public HRService hrDataDAO()
  {
    LOGGER.debug("Creating bean hrDataDAO");

    return new HRService(datastore, morphiaOperations);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  @Bean
  public DataService dataService()
  {
    LOGGER.debug("Creating bean dataService");

    return new DataService(employeeOperations);
  }
}
