package services.ad.query;

import java.util.Objects;

public class LDAPQuery
{
  private final String query;
  
  /* package-private */ LDAPQuery(final String query)
  {
    this.query = query;
  }
  
  public String get()
  {
    return query;
  }
  
  @Override
  public String toString()
  {
    return get();
  }
  
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
  
  @Override
  public int hashCode()
  {
    return Objects.hash(query);
  }
}
