package services.ad;

import java.util.Hashtable;

import javax.naming.directory.SearchControls;

public class ADSearchSettingsImpl implements ADSearchSettings
{
  private final SearchControls searchControls;
  private final Hashtable<String, String> environmentSettings;
  
  public ADSearchSettingsImpl(final SearchControls searchControls, final Hashtable<String, String> environmentSettings)
  {
    this.searchControls = searchControls;
    this.environmentSettings = environmentSettings;
  }
  
  @Override
  public SearchControls getSearchControls()
  {
    return searchControls;
  }

  @Override
  public Hashtable<String, String> getEnvironmentSettings()
  {
    return environmentSettings;
  }
}
