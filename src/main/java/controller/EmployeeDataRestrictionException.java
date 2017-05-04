package controller;

public class EmployeeDataRestrictionException extends Exception
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  public EmployeeDataRestrictionException()
  {
    super("Access denied: this employee's MyCareer data has restricted access");
  }
}
