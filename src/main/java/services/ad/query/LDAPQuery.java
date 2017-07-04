package services.ad.query;

import java.util.Objects;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class LDAPQuery
{
  private final String query;
  
  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param query
   */
  /* package-private */ LDAPQuery(final String query)
  {
    this.query = query;
  }
  
  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String get()
  {
    return query;
  }
  

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return get();
  }
  
  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object other)
  {
    if (!(other instanceof LDAPQuery))
    {
      return false;
    }
    
    final LDAPQuery otherLDAPQuery = (LDAPQuery) other;
    
    return query.equals(otherLDAPQuery.query);
  }
  

  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return Objects.hash(query);
  }
}
