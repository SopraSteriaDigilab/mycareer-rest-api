package config;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_AUTHENTICATION;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;
import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;
import static services.ad.query.LDAPQueries.*;

import java.util.Hashtable;

import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import services.ad.ADSearchSettings;
import services.ad.ADSearchSettingsImpl;

/**
 * Spring Configuration class for spring beans related to active directory search settings.
 * 
 * @see services.ad
 */
@Configuration
@PropertySource("activeDirectory.properties")
public class ADConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ADConfig.class);

  private static final String AUTHENTICATION = "simple";
  private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
  private static final String TIMEOUT_ATTRIBUTE_KEY = "com.sun.jndi.ldap.read.timeout";
  private static final String TIMEOUT_ATTRIBUTE = "60000";

  private static final String AD_SOPRA_HOST_KEY = "sopra.host";
  private static final String AD_SOPRA_PRINCIPAL_KEY = "sopra.principal";
  private static final String AD_SOPRA_PASSWORD_KEY = "sopra.password";
  private static final String[] AD_SOPRA_ATTRIBUTES = { EXTENSION_ATTRIBUTE_7, MEMBER, MEMBER_OF };

  private static final String AD_STERIA_HOST_KEY = "steria.host";
  private static final String AD_STERIA_PRINCIPAL_KEY = "steria.principal";
  private static final String AD_STERIA_PASSWORD_KEY = "steria.password";
  private static final String[] AD_STERIA_ATTRIBUTES = { ACCOUNT_EXPIRES, COMPANY, DEPARTMENT, DIRECT_REPORTS,
      EMPLOYEE_TYPE, EXTENSION_ATTRIBUTE_2, GIVEN_NAME, MAIL, MEMBER, MEMBER_OF, OU, SAM_ACCOUNT_NAME, SN,
      STERIA_SECTOR_UNIT, TARGET_ADDRESS };

  @Autowired
  private Environment env;

  /**
   * Spring bean definition for the Sopra active directory (EMEAAD) search settings and controls.
   * 
   * @return the Sopra AD search settings
   * @see ADSearchSettings
   */
  @Bean
  public ADSearchSettings sopraADSearchSettings()
  {
    LOGGER.debug("Creating bean sopraADSearchSettings");

    final ADSearchSettings sopraADSearchSettings = new ADSearchSettingsImpl(sopraSearchControls(), sopraADSettings());

    return sopraADSearchSettings;
  }

  /**
   * Spring bean definition for the Steria active directory (AD One) search settings and controls.
   * 
   * @return the Steria AD search settings
   * @see ADSearchSettings
   */
  @Bean
  public ADSearchSettings steriaADSearchSettings()
  {
    LOGGER.debug("Creating bean steriaADSearchSettings");

    final ADSearchSettings steriaADSearchSettings = new ADSearchSettingsImpl(steriaSearchControls(),
        steriaADSettings());

    return steriaADSearchSettings;
  }

  /**
   * Spring bean definition for the Sopra AD connection settings.
   * 
   * @return Hashtable containing the Sopra AD connection settings
   */
  @Bean
  public Hashtable<String, String> sopraADSettings()
  {
    LOGGER.debug("Creating bean sopraADSettings");

    final Hashtable<String, String> sopraADSettings = new Hashtable<>();

    sopraADSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    sopraADSettings.put(PROVIDER_URL, env.getProperty(AD_SOPRA_HOST_KEY));
    sopraADSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    sopraADSettings.put(SECURITY_PRINCIPAL, env.getProperty(AD_SOPRA_PRINCIPAL_KEY));
    sopraADSettings.put(SECURITY_CREDENTIALS, env.getProperty(AD_SOPRA_PASSWORD_KEY));
    sopraADSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);

    return sopraADSettings;
  }

  /**
   * Spring bean definition for the Steria AD connection settings.
   * 
   * @return Hashtable containing the Steria AD connection settings
   */
  @Bean
  public Hashtable<String, String> steriaADSettings()
  {
    LOGGER.debug("Creating bean steriaADSettings");

    final Hashtable<String, String> steriaADSettings = new Hashtable<>();

    steriaADSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    steriaADSettings.put(PROVIDER_URL, env.getProperty(AD_STERIA_HOST_KEY));
    steriaADSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    steriaADSettings.put(SECURITY_PRINCIPAL, env.getProperty(AD_STERIA_PRINCIPAL_KEY));
    steriaADSettings.put(SECURITY_CREDENTIALS, env.getProperty(AD_STERIA_PASSWORD_KEY));
    steriaADSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);

    return steriaADSettings;
  }

  /**
   * Spring bean definition for the Sopra AD search controls
   * 
   * @return SearchControls for the Sopra AD
   * @see SearchControls
   */
  @Bean
  public SearchControls sopraSearchControls()
  {
    LOGGER.debug("Creating bean sopraSearchControls");

    final SearchControls sopraSearchControls = new SearchControls();

    sopraSearchControls.setSearchScope(SUBTREE_SCOPE);
    sopraSearchControls.setReturningAttributes(AD_SOPRA_ATTRIBUTES);

    return sopraSearchControls;
  }

  /**
   * Spring bean definition for the Steria AD search controls
   * 
   * @return SearchControls for the Steria AD
   * @see SearchControls
   */
  @Bean
  public SearchControls steriaSearchControls()
  {
    LOGGER.debug("Creating bean steriaSearchControls");

    final SearchControls steriaSearchControls = new SearchControls();

    steriaSearchControls.setSearchScope(SUBTREE_SCOPE);
    steriaSearchControls.setReturningAttributes(AD_STERIA_ATTRIBUTES);

    return steriaSearchControls;
  }
}
