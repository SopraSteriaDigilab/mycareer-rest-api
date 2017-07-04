package dataStructure;

/**
 * Exception thrown when a Java object could not be converted to a {@code Document}.
 * 
 * @see Document
 * @see DBObject
 */
public class DocumentConversionException extends Exception
{
  private static final long serialVersionUID = 1L;

  /** @param message The exception message. */
  public DocumentConversionException(final String message)
  {
    super(message);
  }

  /** @param wrappedException The exception which caused this exception. */
  public DocumentConversionException(Exception wrappedException)
  {
    super(wrappedException);
  }

  /**
   * @param message The exception message.
   * @param wrappedException The exception which caused this exception.
   */
  public DocumentConversionException(String message, Exception wrappedException)
  {
    super(message, wrappedException);
  }

}
