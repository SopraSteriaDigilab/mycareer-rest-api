package services.ews;

import javax.naming.NamingException;

public class DistributionListException extends Exception
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  public DistributionListException(final String msg)
  {
    super(msg);
  }

  public DistributionListException(String msg, Throwable t)
  {
    super(msg, t);
  }
}
