package services.ad;

/**
 * Wraps all exceptions encountered when using an {@code ADConnection} 
 * to connect to an active directory.
 * 
 * @see NamingException
 * @see RuntimeException
 * @see ADConnection
 */
public class ADConnectionException extends Exception
{
  private static final long serialVersionUID = 4781800554919237698L;
  
  public ADConnectionException(String message, Exception wrappedException)
  {
    super(message, wrappedException);
  }
}
