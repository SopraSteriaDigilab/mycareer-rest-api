package controller;

/**
 * Exception which indicates that an {@code Employee}s data is restricted in the current context and may not be
 * accessed.
 *
 * @see Employee
 */
public class EmployeeDataRestrictionException extends Exception
{
  private static final long serialVersionUID = 1L;

  /**
   * EmployeeDataRestrictionException Constructor - Initialises this exception with a standard exception message for
   * this type.
   */
  public EmployeeDataRestrictionException()
  {
    super("Access denied: this employee's MyCareer data has restricted access");
  }
}
