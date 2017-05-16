package services;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class EmployeeNotFoundException extends Exception
{
  private static final long serialVersionUID = 1L;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param msg
   */
  public EmployeeNotFoundException(final String msg)
  {
    super(msg);
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param msg
   * @param wrappedException
   */
  public EmployeeNotFoundException(final String msg, final Exception wrappedException)
  {
    super(msg, wrappedException);
  }
}
