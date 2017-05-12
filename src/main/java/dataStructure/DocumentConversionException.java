package dataStructure;

/**
 * TODO: Describe this TYPE.
 *
 */
public class DocumentConversionException extends Exception
{
  /** long Constant - Represents the serial version. */
  private static final long serialVersionUID = 1L;
  
  public DocumentConversionException(final String message)
  {
    super(message);
  }
  
  public DocumentConversionException(Exception wrappedException)
  {
    super(wrappedException);
  }
  
  public DocumentConversionException(String message, Exception wrappedException)
  {
    super(message, wrappedException);
  }
  
}
