package services.ad.query;

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
}
