package services.ews;

import java.util.Map;

public class DistributionListCache implements Cache<String, DistributionList>
{
  private final Map<String, DistributionList> cache;
  
  public DistributionListCache(Map<String, DistributionList> cache)
  {
    this.cache = cache;
  }
  
  @Override
  public DistributionList put(String key, DistributionList value)
  {
    return cache.put(key, value);
  }

  @Override
  public DistributionList get(String key)
  {
    return cache.get(key);
  }

  @Override
  public boolean contains(String key)
  {
    return cache.containsKey(key);
  }

  @Override
  public void clear()
  {
    cache.clear();
  }
}
