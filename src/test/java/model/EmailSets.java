package model;

import java.util.HashSet;
import java.util.Set;

public class EmailSets
{
  public static Set<? super String> emailsOnly()
  {
    final Set<String> emailsOnly = new HashSet<>();

    emailsOnly.add("email@address.com");
    emailsOnly.add("address@another.com");

    return emailsOnly;
  }

  public static Set<? super String> emailsAndNull()
  {
    final Set<? super String> emailsAndNull = emailsOnly();

    emailsAndNull.add(null);

    return emailsAndNull;
  }

  public static Set<? super String> emailsAndEmpty()
  {
    final Set<? super String> emailsAndEmpty = emailsOnly();

    emailsAndEmpty.add("");

    return emailsAndEmpty;
  }

  public static Set<? super String> emailsNullAndEmpty()
  {
    final Set<? super String> emailsNullAndEmpty = emailsAndNull();

    emailsNullAndEmpty.add("");

    return emailsNullAndEmpty;

  }

  public static Set<? super String> nullOnly()
  {
    final Set<String> nullOnly = new HashSet<>();

    nullOnly.add(null);

    return nullOnly;

  }

  public static Set<? super String> emptyOnly()
  {
    final Set<String> emptyOnly = new HashSet<>();

    emptyOnly.add("");

    return emptyOnly;
  }
}
