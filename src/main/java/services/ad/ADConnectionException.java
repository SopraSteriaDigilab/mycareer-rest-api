package services.ad;

/**
 * Wraps all exceptions encountered when using an {@code ADConnection} to connect to an active directory.
 * 
 * @see NamingException
 * @see RuntimeException
 * @see ADConnectionImpl
 */
public class ADConnectionException extends Exception
{
  private static final long serialVersionUID = 4781800554919237698L;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param message
   */
  public ADConnectionException(final String message)
  {
    super(message);
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param wrappedException
   */
  public ADConnectionException(Exception wrappedException)
  {
    super(wrappedException);
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param message
   * @param wrappedException
   */
  public ADConnectionException(String message, Exception wrappedException)
  {
    super(message, wrappedException);
  }
}
