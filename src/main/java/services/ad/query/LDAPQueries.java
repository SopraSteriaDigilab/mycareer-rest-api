package services.ad.query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class LDAPQueries
{
  // AD fields

  /** TODO describe */
  public static final String ACCOUNT_EXPIRES = "accountExpires";

  /** TODO describe */
  public static final String CN = "cn";

  /** TODO describe */
  public static final String COMPANY = "company";

  /** TODO describe */
  public static final String DEPARTMENT = "department";

  /** TODO describe */
  public static final String DIRECT_REPORTS = "directReports";

  /** TODO describe */
  public static final String DISPLAY_NAME = "displayName";

  /** TODO describe */
  public static final String DISTINGUISHED_NAME = "distinguishedName";

  /** TODO describe */
  public static final String EMPLOYEE_TYPE = "employeeType";

  /** TODO describe */
  public static final String EXTENSION_ATTRIBUTE_2 = "extensionAttribute2";

  /** TODO describe */
  public static final String EXTENSION_ATTRIBUTE_7 = "extensionAttribute7";

  /** TODO describe */
  public static final String GIVEN_NAME = "givenName";

  /** TODO describe */
  public static final String MAIL = "mail";

  /** TODO describe */
  public static final String MAIL_NICKNAME = "mailNickname";

  /** TODO describe */
  public static final String MEMBER = "member";

  /** TODO describe */
  public static final String MEMBER_OF = "memberOf";

  /** TODO describe */
  public static final String NAME = "name";

  /** TODO describe */
  public static final String OU = "ou";

  /** TODO describe */
  public static final String SAM_ACCOUNT_NAME = "sAMAccountName";

  /** TODO describe */
  public static final String SN = "sn";

  /** TODO describe */
  public static final String STERIA_SECTOR_UNIT = "SteriaSectorUnit";

  /** TODO describe */
  public static final String TARGET_ADDRESS = "targetAddress";

  // AD values

  /** TODO describe */
  public static final String AD_SOPRA_HR_DASH = "SSG UK_HR MyCareer Dash";

  /** TODO describe */
  public static final String EMPLOYEE = "EMP";

  private static final String AND = "(&";
  private static final String OR = "(|";

  private LDAPQueries()
  {
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param field
   * @return
   */
  public static LDAPQuery hasField(final String field)
  {
    final String query = new StringBuilder("(").append(field).append("=*)").toString();
    return new LDAPQuery(query);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param field
   * @param valueStart
   * @return
   */
  public static LDAPQuery fieldBeginsWith(final String field, final String valueStart)
  {
    final String query = new StringBuilder("(").append(field).append("=").append(valueStart).append("*)").toString();
    return new LDAPQuery(query);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param field
   * @param value
   * @return
   */
  public static LDAPQuery basicQuery(final String field, final String value)
  {
    final String query = new StringBuilder("(").append(field).append("=").append(value).append(")").toString();
    return new LDAPQuery(query);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param ldapQueries
   * @return
   */
  public static LDAPQuery and(LDAPQuery... ldapQueries)
  {
    return combine(AND, ldapQueries);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param queries
   * @return
   */
  public static LDAPQuery and(Set<LDAPQuery> queries)
  {
    return combine(AND, queries);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param ldapQueries
   * @return
   */
  public static LDAPQuery or(LDAPQuery... ldapQueries)
  {
    return combine(OR, ldapQueries);
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param queries
   * @return
   */
  public static LDAPQuery or(Set<LDAPQuery> queries)
  {
    return combine(OR, queries);
  }

  private static LDAPQuery combine(final String queryStart, LDAPQuery... ldapQueries)
  {
    Set<LDAPQuery> queries = new HashSet<>(Arrays.asList(ldapQueries));

    return combine(queryStart, queries);
  }

  private static LDAPQuery combine(final String queryStart, Set<LDAPQuery> queries)
  {
    String query;
    final StringBuilder stringBuilder = new StringBuilder(queryStart);

    queries.forEach(q -> stringBuilder.append(q.get()));
    query = stringBuilder.append(")").toString();

    return new LDAPQuery(query);
  }
}
