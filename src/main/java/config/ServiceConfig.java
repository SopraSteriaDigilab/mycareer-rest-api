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
 * Spring Configuration class for spring beans providing services required by the REST controllers.
 * 
 * @see EmployeeService
 * @see EmployeeProfileService
 * @see DataService
 * @see HistoryService
 * @see HRService
 * @see ManagerService
 * @see DistributionListService
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
   * Spring bean definition for the Employee service
   *
   * @return the Employee service
   * @see EmployeeService
   * @see EmployeeController
   * @see Employee
   */
  @Bean
  public EmployeeService employeeService()
  {
    LOGGER.debug("Creating bean employeeService");

    return new EmployeeService(morphiaOperations, employeeOperations, objectivesHistoriesOperations,
        developmentNeedsHistoriesOperations, competenciesHistoriesOperations, employeeProfileService(), env);
  }

  /**
   * Spring bean definition for the EmployeeProfile service
   *
   * @return the EmployeeProfile service
   * @see EmployeeProfileService
   * @see EmployeeController
   * @see EmployeeProfile
   */
  @Bean
  public EmployeeProfileService employeeProfileService()
  {
    LOGGER.debug("Creating bean employeeProfileService");

    return new EmployeeProfileService(morphiaOperations, employeeOperations, sopraADSearchSettings);
  }

  /**
   * Spring bean definition for the manager service
   *
   * @return the manager service
   * @see Employee
   * @see ManagerController
   */
  @Bean
  public ManagerService managerService()
  {
    LOGGER.debug("Creating bean managerService");

    return new ManagerService(employeeService(), morphiaOperations, employeeOperations, objectivesHistoriesOperations,
        employeeProfileService(), env);
  }

  /**
   * Spring bean definition for the distribution list service
   *
   * @return the distribution list service
   * @see DistributionListService
   * @see DistributionList
   */
  @Bean
  public DistributionListService distributionListService()
  {
    LOGGER.debug("Creating bean distributionListService");

    return new DistributionListService(distributionListCache(), employeeProfileService(), sopraADSearchSettings,
        steriaADSearchSettings);
  }

  /**
   * Spring bean definition for the distribution list cache, required by the DistributionListService.
   *
   * @return the distribution list cache
   * @see Cache
   * @see DistributionListService
   * @see DistributionList
   */
  @Bean
  public Cache<String, DistributionList> distributionListCache()
  {
    LOGGER.debug("Creating bean distributionListCache");

    return new DistributionListCache(new HashMap<String, DistributionList>());
  }

  /**
   * Spring bean definition for the history service
   *
   * @return the history service
   * @see HistoryService
   * @see HistoryController
   */
  @Bean
  public HistoryService historyService()
  {
    LOGGER.debug("Creating bean historyService");

    return new HistoryService(morphiaOperations);
  }

  /**
   * Spring bean definition for the EmployeeProfile service
   *
   * @return the HR service
   * @see HRService
   * @see HRController
   */
  @Bean
  public HRService hrService()
  {
    LOGGER.debug("Creating bean hrService");

    return new HRService(datastore, morphiaOperations);
  }

  /**
   * Spring bean definition for the data service
   *
   * @return the data service
   * @see DataService
   * @see DataController
   */
  @Bean
  public DataService dataService()
  {
    LOGGER.debug("Creating bean dataService");

    return new DataService(employeeOperations);
  }
}
