package dataStructure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public class EmailAddresses
{
  /** TODO describe */
  public static final String MAIL = "profile.emailAddresses.mail";

  /** TODO describe */
  public static final String TARGET_ADDRESS = "profile.emailAddresses.targetAddress";

  /** TODO describe */
  public static final String USER_ADDRESS = "profile.emailAddresses.userAddress";

  private String mail;
  private String targetAddress;
  private String userAddress;

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   */
  public EmailAddresses()
  {
  }

  /**
   * 
   * TYPE Constructor - Responsible for initialising this object.
   *
   * @param builder
   */
  public EmailAddresses(Builder builder)
  {
    this.mail = builder.mail;
    this.targetAddress = builder.targetAddress;
    this.userAddress = builder.userAddress;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
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

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public boolean hasUserAddress()
  {
    return userAddress != null;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String getMail()
  {
    return mail;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param mail
   */
  public void setMail(String mail)
  {
    this.mail = mail;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String getTargetAddress()
  {
    return targetAddress;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param targetAddress
   */
  public void setTargetAddress(String targetAddress)
  {
    this.targetAddress = targetAddress;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String getUserAddress()
  {
    return userAddress;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param userAddress
   */
  public void setUserAddress(String userAddress)
  {
    this.userAddress = userAddress;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @param emailAddress
   * @return
   */
  public String getPreferred(final String emailAddress)
  {
    if (userAddress != null && !userAddress.isEmpty())
    {
      return userAddress;
    }

    return emailAddress;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String getPreferred()
  {
    return getPreferred(mail);
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

  /**
   * 
   * TODO: Describe this TYPE.
   *
   */
  public static class Builder
  {
    private String mail;
    private String targetAddress;
    private String userAddress;

    /**
     * 
     * TYPE Constructor - Responsible for initialising this object.
     *
     */
    public Builder()
    {
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @param mail
     * @return
     */
    public Builder mail(String mail)
    {
      this.mail = mail;
      return this;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @param targetAddress
     * @return
     */
    public Builder targetAddress(String targetAddress)
    {
      this.targetAddress = targetAddress;
      return this;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @param userAddress
     * @return
     */
    public Builder userAddress(String userAddress)
    {
      this.userAddress = userAddress;
      return this;
    }

    /**
     * 
     * TODO: Describe this method.
     *
     * @return
     */
    public EmailAddresses build()
    {
      return new EmailAddresses(this);
    }
  }
}
