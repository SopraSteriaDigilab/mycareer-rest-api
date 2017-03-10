package utils;

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
}
