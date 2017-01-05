package services;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import dataStructure.Constants;
import net.sourceforge.spnego.SpnegoHttpFilter;

@Configuration
//@PropertySource("classpath:/development.properties")
//@Import({EnvironmentSetup.class})
//@PropertySource({"development.properties", "production.properties", "uat.properties"})
public class ApplicationConfiguration extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	//Allow the back-end to be accessible by only this address. If you need to grant access to the back-end to multiple domains
    	// change the setHeader to addHeader and create as may addHeader as the number of domains, like below:
    	//response.addHeader("Access-Control-Allow-Origin", "IP_ADDRESS");
    	//response.addHeader("Access-Control-Allow-Origin", "IP_ADDRESS");
        response.setHeader("Access-Control-Allow-Origin","*");        
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else { 
            filterChain.doFilter(request, response);
        }
    }
    
    @Bean
	public FilterRegistrationBean spnegoFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(spnegoHttpFilter());
		registration.setName("spnegoHttpFilter");
		registration.addInitParameter("spnego.allow.basic", "true");
		registration.addInitParameter("spnego.allow.localhost", "true");
		registration.addInitParameter("spnego.allow.unsecure.basic", "true");
		registration.addInitParameter("spnego.login.client.module", "spnego-client");
		registration.addInitParameter("spnego.krb5.conf", "krb5.conf");
		registration.addInitParameter("spnego.login.conf", "login.conf");
		registration.addInitParameter("spnego.preauth.username", "username");
		registration.addInitParameter("spnego.preauth.password", "password");
		registration.addInitParameter("spnego.login.server.module", "spnego-server");
		registration.addInitParameter("spnego.prompt.ntlm", "true");
		registration.addInitParameter("spnego.logger.level", "1");
		
		return registration;
	}

	private Filter spnegoHttpFilter() {
		return new SpnegoHttpFilter();
	}
	
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
    
}
