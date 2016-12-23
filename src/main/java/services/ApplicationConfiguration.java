package services;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import dataStructure.Constants;

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
    
}
