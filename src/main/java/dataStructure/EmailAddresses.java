package dataStructure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EmailAddresses
{
  public static final String MAIL = "profile.emailAddresses.mail";
  public static final String TARGET_ADDRESS = "profile.emailAddresses.targetAddress";
  public static final String USER_ADDRESS = "profile.emailAddresses.userAddress";
  
  private String mail;
  private String targetAddress;
  private String userAddress;

  public EmailAddresses()
  {
  }

  public EmailAddresses(Builder builder)
  {
    this.mail = builder.mail;
    this.targetAddress = builder.targetAddress;
    this.userAddress = builder.userAddress;
  }


  public Set<String> toSet()
  {
    Set<String> emailAddresses = new HashSet<>();

    emailAddresses.add(mail);
    emailAddresses.add(targetAddress);

    if (hasUserAddress())
    {
      emailAddresses.add(userAddress);
    }

    return emailAddresses;
  }
  
  public boolean hasUserAddress()
  {
    return userAddress != null;
  }

  public String getMail()
  {
    return mail;
  }

  public void setMail(String mail)
  {
    this.mail = mail;
  }

  public String getTargetAddress()
  {
    return targetAddress;
  }

  public void setTargetAddress(String targetAddress)
  {
    this.targetAddress = targetAddress;
  }

  public String getUserAddress()
  {
    return userAddress;
  }

  public void setUserAddress(String userAddress)
  {
    this.userAddress = userAddress;
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == this)
    {
      return true;
    }

    if (!(o instanceof EmailAddresses))
    {
      return false;
    }

    EmailAddresses emailAddresses = (EmailAddresses) o;

    return Objects.equals(mail, emailAddresses.mail) && Objects.equals(targetAddress, emailAddresses.targetAddress);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(mail, targetAddress);
  }

  @Override
  public String toString()
  {
    return "EmailAddresses [mail= " + mail + ", targetAddress=" + targetAddress + ", userAddress=" + userAddress + "]";
  }

  public static class Builder
  {
    private String mail;
    private String targetAddress;
    private String userAddress;

    public Builder()
    {
    }

    public Builder mail(String mail)
    {
      this.mail = mail;
      return this;
    }

    public Builder targetAddress(String targetAddress)
    {
      this.targetAddress = targetAddress;
      return this;
    }

    public Builder userAddress(String userAddress)
    {
      this.userAddress = userAddress;
      return this;
    }

    public EmailAddresses build()
    {
      return new EmailAddresses(this);
    }
  }

  public String getPreferred(final String emailAddress)
  {
    if (userAddress != null && !userAddress.isEmpty())
    {
      return userAddress;
    }
    
    return emailAddress;
  }

  public String getPreferred()
  {
    return getPreferred(mail);
  }
}
