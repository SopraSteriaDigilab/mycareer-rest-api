package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Conversions
{
  private static final Logger LOGGER = LoggerFactory.getLogger(Conversions.class);
  
  public static final ZoneId UK_TIMEZONE = ZoneId.of("Europe/London");
  
  private Conversions() {}
  
  public static <E> List<E> namingEnumToList(NamingEnumeration<E> namingEnum) throws NamingException
  {
    LOGGER.debug("Converting NamingEnumeration to a List");
    
    final List<E> result = new ArrayList<>();
    
    while (namingEnum.hasMore())
    {
      result.add(namingEnum.next());
    }
    
    return result;
  }
  
  public static Date ldapTimestampToDate(final String ldapTimestamp)
  {
    LOGGER.debug("Converting LDAP timestamp to java.util.Date");
    
    long ldapTime = Long.parseLong(ldapTimestamp);
    long unixTime = (ldapTime - 0x19db1ded53e8000L) / 10_000L;
    
    return new Date(unixTime);
  }

  /**
   * Converts LocalDate to a java.util.Date
   *
   * @param localDateTime
   * @return
   */
  public static Date localDatetoDate(LocalDate localDate)
  {
    LOGGER.debug("Converting java.time.LocalDate to a java.util.Date");
    
    return Date.from(localDate.atStartOfDay(UK_TIMEZONE).toInstant());
  }

  /**
   * Converts java.util.Date to a LocalDate
   *
   * @param localDateTime
   * @return
   */
  public static LocalDate dateToLocalDate(Date date)
  {
    LOGGER.debug("Converting java.util.Date to a java.time.LocalDate");
    
    return date.toInstant().atZone(UK_TIMEZONE).toLocalDate();
  }

  /**
   * Converts LocalDateTime to a java.util.Date
   *
   * @param localDateTime
   * @return
   */
  public static Date localDateTimeToDate(LocalDateTime localDateTime)
  {
    LOGGER.debug("Converting java.time.LocalDateTime to a java.util.Date");
    
    return Date.from(localDateTime.atZone(UK_TIMEZONE).toInstant());
  }

  /**
   * Converts java.util.Date to a LocalDateTime
   *
   * @param localDateTime
   * @return
   */
  public static LocalDateTime dateToLocalDateTime(Date date)
  {
    LOGGER.debug("Converting java.util.Date to a java.time.LocalDateTime");
    
    Instant instant = date.toInstant();
    return instant.atZone(UK_TIMEZONE).toLocalDateTime();
  }
  
}
