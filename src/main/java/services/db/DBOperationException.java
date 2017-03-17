package services.db;

public class DBOperationException extends Exception
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  public DBOperationException(final String msg)
  {
    super(msg);
  }
  
  public DBOperationException(final String msg, final Exception wrappedException)
  {
    super(msg, wrappedException);
  }
}
