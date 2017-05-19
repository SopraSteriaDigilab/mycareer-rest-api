package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import services.BulkUpdateService;
import services.ad.ADSearchSettings;
import services.db.MongoOperations;
import services.db.MorphiaOperations;
import services.ews.Cache;
import services.ews.DistributionList;
import services.mappers.EmployeeProfileMapper;

/**
 * Spring Configuration class for spring beans related to nightly bulk update service.
 * 
 * @see BulkUpdateService
 */
@Configuration
public class BulkUpdateConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateConfig.class);

  @Autowired
  private MorphiaOperations morphiaOperations;

  @Autowired
  private MongoOperations employeeOperations;

  @Autowired
  private ADSearchSettings steriaADSearchSettings;

  @Autowired
  private Cache<String, DistributionList> distributionListCache;

  /**
   * Spring bean definition for the nightly bulk update service
   * 
   * @return the bulk update service
   * @see BulkUpdateService
   */
  @Bean
  public BulkUpdateService bulkUpdateService()
  {
    LOGGER.debug("Creating bean bulkUpdateService");

    return new BulkUpdateService(morphiaOperations, employeeOperations, steriaADSearchSettings, employeeProfileMapper(),
        distributionListCache);
  }

  /**
   * Spring bean definition for the employee profile mapper.
   * 
   * @return the employee profile mapper
   * @see EmployeeProfileMapper
   */
  @Bean
  public EmployeeProfileMapper employeeProfileMapper()
  {
    LOGGER.debug("Creating bean employeeProfileMapper");

    return new EmployeeProfileMapper();
  }
}
