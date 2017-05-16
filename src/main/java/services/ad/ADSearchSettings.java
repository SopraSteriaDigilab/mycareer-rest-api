package services.ad;

import java.util.Hashtable;

import javax.naming.directory.SearchControls;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public interface ADSearchSettings
{
  /**
   * Returns the {@code SearchControls} of these {@code ADSearchSettings}.
   *
   * @return A copy of the SearchControls
   */
  SearchControls getSearchControls();

  /**
   * Returns the environment settings for these {@code ADSearchSettings}.
   */
  Hashtable<String, String> getEnvironmentSettings();
}
