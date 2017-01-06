package services;

import static dataStructure.Constants.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class EnvironmentSetup {
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws UnknownHostException {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		Resource resource;
		String host = InetAddress.getLocalHost().getHostName();
		
		switch (host) {
			case DEV_SERVER_NAME:
				resource = new ClassPathResource("dev.properties");
				break;
			case UAT_SERVER_NAME:
				resource = new ClassPathResource("uat.properties");
				break;
			case LIVE_SERVER_NAME:
				resource = new ClassPathResource("live.properties");
				break;
			default:
				resource = new ClassPathResource("dev.properties");
				break;
		}
		
		pspc.setLocations(resource);
		
		return pspc;
    }	
}
