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
import utils.sequence.SequenceException;

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

  @Bean
  public BulkUpdateService bulkUpdateService() throws SequenceException
  {
    return new BulkUpdateService(morphiaOperations, employeeOperations, steriaADSearchSettings,
        employeeProfileMapper(), distributionListCache);
  }

  @Bean
  public EmployeeProfileMapper employeeProfileMapper()
  {
    return new EmployeeProfileMapper();
  }
}
