package dataStructure;

/**
 * TODO: Describe this TYPE.
 *
 */
public class DocumentConversionException extends Exception
{
  /** long Constant - Represents the serial version. */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param message
   */
  public DocumentConversionException(final String message)
  {
    super(message);
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param wrappedException
   */
  public DocumentConversionException(Exception wrappedException)
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
  public DocumentConversionException(String message, Exception wrappedException)
  {
    super(message, wrappedException);
  }

}
