package services.ad;

import java.util.Hashtable;

import javax.naming.directory.SearchControls;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class ADSearchSettingsImpl implements ADSearchSettings
{
  private final SearchControls searchControls;
  private final Hashtable<String, String> environmentSettings;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param searchControls
   * @param environmentSettings
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
  public Hashtable<String, String> getEnvironmentSettings()
  {
    return environmentSettings;
  }
}
