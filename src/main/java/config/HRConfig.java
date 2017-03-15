package config;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import services.HRService;

@Configuration
public class HRConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(HRConfig.class);
  
  @Autowired
  private Datastore dbConnection;
  
  @Bean
  public HRService hrDataDAO()
  {
    return new HRService(dbConnection);
  }
}