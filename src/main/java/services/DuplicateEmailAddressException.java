package services;

public class DuplicateEmailAddressException extends Exception
{
  /** TYPE Property|Constant - Represents|Indicates... */
  private static final long serialVersionUID = 1L;

  public DuplicateEmailAddressException()
  {
    super("Email address already exists");
  }
}
