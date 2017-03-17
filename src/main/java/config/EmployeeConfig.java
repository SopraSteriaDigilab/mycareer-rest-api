package config;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import services.ad.ADSearchSettings;
import services.EmployeeProfileService;
import services.EmployeeService;

@Configuration
@PropertySource("${ENVIRONMENT}.properties")
public class EmployeeConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeConfig.class);

  @Autowired
  private Environment env;
  
  @Autowired
  private Datastore dbConnection;
  
  @Autowired
  private ADSearchSettings sopraADSearchSettings;
  
  @Autowired
  private ADSearchSettings steriaADSearchSettings;
  
  @Bean
  public EmployeeService employeeService()
  {
    return new EmployeeService(dbConnection, employeeProfileService(), env);
  }
  
  @Bean
  public EmployeeProfileService employeeProfileService()
  {
    return new EmployeeProfileService(sopraADSearchSettings, steriaADSearchSettings);
  }
}
