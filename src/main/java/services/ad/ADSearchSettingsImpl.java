package services.ad;

import static javax.naming.Context.*;

import java.util.Hashtable;

import javax.naming.directory.SearchControls;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class ADSearchSettingsImpl implements ADSearchSettings
{
  public enum LdapPort
  {
    LOCAL("389"), LOCAL_SECURE("636"), GLOBAL("3268"), GLOBAL_SECURE("3269");

    private final String portString;

    private LdapPort(final String portString)
    {
      this.portString = portString;
    }

    public String getPortString()
    {
      return portString;
    }

    public String generateUrl(final String host)
    {
      return new StringBuilder(host).append(":").append(portString).toString();
    }
  }

  private final SearchControls searchControls;
  private final Hashtable<String, String> environmentSettings;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param searchControls
   * @param environmentSettings
   * @param hashtable
   */
  public ADSearchSettingsImpl(final SearchControls searchControls, final Hashtable<String, String> environmentSettings)
  {
    this.searchControls = searchControls;
    this.environmentSettings = environmentSettings;
  }

  /**
   * 
   * Override of NAME method.
   *
   * TODO: Describe this method.
   *
   * @see services.ad.ADSearchSettings#getSearchControls()
   *
   * @return
   */
  @Override
  public SearchControls getSearchControls()
  {
    return searchControls;
  }

  /**
   * 
   * Override of NAME method.
   *
   * TODO: Describe this method.
   *
   * @see services.ad.ADSearchSettings#getEnvironmentSettings()
   *
   * @return
   */
  @Override
  public Hashtable<String, String> getEnvironmentSettings(final LdapPort ldapPort)
  {
    final Hashtable<String, String> envSettingsCopy = new Hashtable<>(environmentSettings);
    final String url = ldapPort.generateUrl(environmentSettings.get(PROVIDER_URL));
    envSettingsCopy.put(PROVIDER_URL, url);
    
    return envSettingsCopy;
  }
}
