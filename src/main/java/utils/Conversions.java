package utils;

import java.util.ArrayList;
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
}
