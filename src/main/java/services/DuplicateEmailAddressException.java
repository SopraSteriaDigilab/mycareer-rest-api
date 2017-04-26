package services;

public class DuplicateEmailAddressException extends Exception
{
  public DuplicateEmailAddressException()
  {
    super("Email address already exists");
  }
}
