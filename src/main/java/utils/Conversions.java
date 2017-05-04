package utils;

import static dataStructure.Constants.UK_TIMEZONE;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  
  private Conversions() {}
  
  public static <E> List<E> namingEnumToList(NamingEnumeration<E> namingEnum) throws NamingException
  {
    final List<E> result = new ArrayList<>();
    
    while (namingEnum.hasMore())
    {
      result.add(namingEnum.next());
    }
    
    return result;
  }
  
  public static Date ldapTimestampToDate(final String ldapTimestamp)
  {
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
    return date.toInstant().atZone(UK_TIMEZONE).toLocalDate();
  }

  /**
   * Converts LocalDateTime to a java.util.Date
   *
   * @param localDateTime
   * @return
   */
  public static Date localDateTimetoDate(LocalDateTime localDateTime)
  {
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
    Instant instant = date.toInstant();
    return instant.atZone(UK_TIMEZONE).toLocalDateTime();
  }
  
}
