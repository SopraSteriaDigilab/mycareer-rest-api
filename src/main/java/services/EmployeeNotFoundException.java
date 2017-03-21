package services;

public class EmployeeNotFoundException extends Exception
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  public EmployeeNotFoundException(final String msg)
  {
    super(msg);
  }
  
  public EmployeeNotFoundException(final String msg, final Exception wrappedException)
  {
    super(msg, wrappedException);
  }
}
