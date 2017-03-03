package application;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import net.sourceforge.spnego.SpnegoHttpFilter;

@Configuration
@PropertySource("${ENVIRONMENT}.properties")
public class WebConfiguration extends OncePerRequestFilter
{

  private static final String SPNEGO_USERNAME = "mycareersvc";
  private static final String SPNEGO_PASSWORD = "Czam2mc2!";

  @Autowired
  private Environment env;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException
  {

    response.setHeader("Access-Control-Allow-Origin", env.getProperty("domain.url.full"));
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "authorization, xsrf-token, Content-Type, Accept");
    response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    if ("OPTIONS".equals(request.getMethod()))
    {
      response.setStatus(HttpServletResponse.SC_OK);
    }
    else
    {
      filterChain.doFilter(request, response);
    }
  }

  @Bean
  public FilterRegistrationBean spnegoFilterRegistration()
  {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(spnegoHttpFilter());
    registration.setName("spnegoHttpFilter");
    registration.addInitParameter("spnego.allow.basic", "true");
    registration.addInitParameter("spnego.allow.localhost", "true");
    registration.addInitParameter("spnego.allow.unsecure.basic", "true");
    registration.addInitParameter("spnego.login.client.module", "spnego-client");
    registration.addInitParameter("spnego.krb5.conf", "krb5.conf");
    registration.addInitParameter("spnego.login.conf", "login.conf");
    registration.addInitParameter("spnego.preauth.username", SPNEGO_USERNAME);
    registration.addInitParameter("spnego.preauth.password", SPNEGO_PASSWORD);
    registration.addInitParameter("spnego.login.server.module", "spnego-server");
    registration.addInitParameter("spnego.prompt.ntlm", "true");
    registration.addInitParameter("spnego.logger.level", "1");

    return registration;
  }

  private Filter spnegoHttpFilter()
  {
    return new SpnegoHttpFilter();
  }

}