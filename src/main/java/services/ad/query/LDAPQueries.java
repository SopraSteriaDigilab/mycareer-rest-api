package services.ad.query;

import java.util.Arrays;
import java.util.List;

public class LDAPQueries
{
  public static final String DISTINGUISHED_NAME = "distinguishedName";
  public static final String CN = "cn";
  public static final String MEMBER = "member";
  public static final String MEMBER_OF = "memberOf";
  public static final String EXTENSION_ATTRIBUTE_2 = "extensionAttribute2";
  public static final String EXTENSION_ATTRIBUTE_7 = "extensionAttribute7";
  
  private static final String AND = "(&";
  private static final String OR = "(|";
  
  private LDAPQueries()
  {
  }

  public static LDAPQuery hasField(final String field)
  {
    final String query = new StringBuilder("(").append(field).append("=*)").toString();
    return new LDAPQuery(query);
  }

  public static LDAPQuery fieldBeginsWith(final String field, final String valueStart)
  {
    final String query = new StringBuilder("(").append(field).append("=").append(valueStart).append("*)").toString();
    return new LDAPQuery(query);
  }

  public static LDAPQuery basicQuery(final String field, final String value)
  {
    final String query = new StringBuilder("(").append(field).append("=").append(value).append(")").toString();
    return new LDAPQuery(query);
  }
  
  public static LDAPQuery and(LDAPQuery... ldapQueries)
  {
    return combine(AND, ldapQueries);
  }

  public static LDAPQuery and(List<LDAPQuery> queries)
  {
    return combine(AND, queries);
  }
  
  public static LDAPQuery or(LDAPQuery... ldapQueries)
  {
    return combine(OR, ldapQueries);
  }

  public static LDAPQuery or(List<LDAPQuery> queries)
  {
    return combine(OR, queries);
  }
  
  private static LDAPQuery combine(final String queryStart, LDAPQuery... ldapQueries)
  {
    List<LDAPQuery> queries = Arrays.asList(ldapQueries);
    
    return combine(queryStart, queries);
  }
  
  private static LDAPQuery combine(final String queryStart, List<LDAPQuery> queries)
  {
    String query;
    final StringBuilder stringBuilder = new StringBuilder(queryStart);
    
    queries.forEach(q -> stringBuilder.append(q.get()));    
    query = stringBuilder.append(")").toString();
    
    return new LDAPQuery(query);
  }
}
