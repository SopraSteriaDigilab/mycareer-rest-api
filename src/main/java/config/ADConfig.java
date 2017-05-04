package config;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_AUTHENTICATION;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;
import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

import java.util.Hashtable;

import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import services.ad.ADSearchSettings;
import services.ad.ADSearchSettingsImpl;
import utils.sequence.Sequence;
import utils.sequence.SequenceException;
import utils.sequence.StringSequence;

@Configuration
public class ADConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ADConfig.class);

  private static final String AUTHENTICATION = "simple";
  private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
  private static final String TIMEOUT_ATTRIBUTE_KEY = "com.sun.jndi.ldap.read.timeout";
  private static final String TIMEOUT_ATTRIBUTE = "30000";

  private static final String AD_SOPRA_HOST = "ldap://duns.ldap-ad.dmsi.corp.sopra";
  private static final String AD_SOPRA_URL = AD_SOPRA_HOST.concat(":389");
  private static final String AD_SOPRA_USERNAME = "svc_mycareer@emea.msad.sopra";
  private static final String AD_SOPRA_PASSWORD = "N9T$SiPSZ";
  private static final String AD_SOPRA_PRINCIPAL = AD_SOPRA_USERNAME;
  private static final String[] AD_SOPRA_ATTRIBUTES = { "memberOf", "member", "extensionAttribute7" };

  private static final String AD_STERIA_HOST = "ldap://one.steria.dom";
  private static final String AD_STERIA_URL = AD_STERIA_HOST.concat(":389");
  private static final String AD_STERIA_USERNAME = "UK-SVC-CAREER";
  private static final String AD_STERIA_PASSWORD = "3I=AkSiGRr";
  private static final String AD_STERIA_LOGIN_TREE = "OU=Service Accounts,OU=UKCentral,OU=UK,OU=Resources,DC=one,DC=steria,DC=dom";
  private static final String AD_STERIA_PRINCIPAL = new StringBuilder("cn=").append(AD_STERIA_USERNAME).append(",")
      .append(AD_STERIA_LOGIN_TREE).toString();
  private static final String[] AD_STERIA_ATTRIBUTES = { "sn", "givenName", "mail", "targetAddress",
      "extensionAttribute2", "sAMAccountName", "directReports", "department", "SteriaSectorUnit", "ou", "company",
      "accountExpires" };

  @Bean
  ADSearchSettings sopraADSearchSettings()
  {
    final ADSearchSettings sopraADSearchSettings = new ADSearchSettingsImpl(sopraSearchControls(), sopraADSettings());
    return sopraADSearchSettings;
  }

  @Bean
  ADSearchSettings steriaADSearchSettings()
  {
    final ADSearchSettings steriaADSearchSettings = new ADSearchSettingsImpl(steriaSearchControls(),
        steriaADSettings());
    return steriaADSearchSettings;
  }

  @Bean
  public Hashtable<String, String> sopraADSettings()
  {
    final Hashtable<String, String> sopraADSettings = new Hashtable<>();
    
    sopraADSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    sopraADSettings.put(PROVIDER_URL, AD_SOPRA_URL);
    sopraADSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    sopraADSettings.put(SECURITY_PRINCIPAL, AD_SOPRA_PRINCIPAL);
    sopraADSettings.put(SECURITY_CREDENTIALS, AD_SOPRA_PASSWORD);
    sopraADSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);
    
    return sopraADSettings;
  }

  @Bean
  public Hashtable<String, String> steriaADSettings()
  {
    final Hashtable<String, String> steriaADSettings = new Hashtable<>();

    steriaADSettings.put(INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
    steriaADSettings.put(PROVIDER_URL, AD_STERIA_URL);
    steriaADSettings.put(SECURITY_AUTHENTICATION, AUTHENTICATION);
    steriaADSettings.put(SECURITY_PRINCIPAL, AD_STERIA_PRINCIPAL);
    steriaADSettings.put(SECURITY_CREDENTIALS, AD_STERIA_PASSWORD);
    steriaADSettings.put(TIMEOUT_ATTRIBUTE_KEY, TIMEOUT_ATTRIBUTE);

    return steriaADSettings;
  }

  @Bean
  public SearchControls sopraSearchControls()
  {
    final SearchControls sopraSearchControls = new SearchControls();
    sopraSearchControls.setSearchScope(SUBTREE_SCOPE);
    sopraSearchControls.setReturningAttributes(AD_SOPRA_ATTRIBUTES);

    return sopraSearchControls;
  }

  @Bean
  public SearchControls steriaSearchControls()
  {
    final SearchControls steriaSearchControls = new SearchControls();
    steriaSearchControls.setSearchScope(SUBTREE_SCOPE);
    steriaSearchControls.setReturningAttributes(AD_STERIA_ATTRIBUTES);

    return steriaSearchControls;
  }
}
