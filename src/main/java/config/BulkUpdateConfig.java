package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import services.BulkUpdateService;
import services.EmployeeService;
import services.ad.ADSearchSettings;
import utils.sequence.Sequence;
import utils.sequence.SequenceException;
import utils.sequence.StringSequence;

@Configuration
public class BulkUpdateConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkUpdateConfig.class);
  
  @Autowired
  private EmployeeService employeeService;
  
  @Autowired
  private ADSearchSettings steriaADSearchSettings;
  
  @Bean
  public BulkUpdateService bulkUpdateService() throws SequenceException
  {
    return new BulkUpdateService(employeeService, steriaADSearchSettings);
  }
}
