package controller;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class EmployeeDataRestrictionException extends Exception
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   */
  public EmployeeDataRestrictionException()
  {
    super("Access denied: this employee's MyCareer data has restricted access");
  }
}
