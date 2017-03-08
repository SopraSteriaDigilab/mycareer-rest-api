package services.mappers;

public class InvalidEmployeeProfileException extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  
  public InvalidEmployeeProfileException(final String msg)
  {
    super(msg);
  }
  
  public InvalidEmployeeProfileException(final Exception wrappedException)
  {
    super(wrappedException);
  }
  
  public InvalidEmployeeProfileException(final String msg, final Exception wrappedException)
  {
    super(msg, wrappedException);
  }
}
