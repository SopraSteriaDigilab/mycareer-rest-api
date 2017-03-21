package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import services.ad.ADSearchSettings;
import services.db.MorphiaOperations;
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
  private ADSearchSettings sopraADSearchSettings;
  
  @Autowired
  private MorphiaOperations morphiaOperations;
  
  @Bean
  public EmployeeService employeeService()
  {
    return new EmployeeService(morphiaOperations, employeeProfileService(), env);
  }
  
  @Bean
  public EmployeeProfileService employeeProfileService()
  {
    return new EmployeeProfileService(morphiaOperations, sopraADSearchSettings);
  }
}