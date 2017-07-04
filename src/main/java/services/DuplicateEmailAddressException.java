package services;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class DuplicateEmailAddressException extends Exception
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   */
  public DuplicateEmailAddressException()
  {
    super("Email address already exists");
  }
}
