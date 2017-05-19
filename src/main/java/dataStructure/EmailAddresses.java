package dataStructure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A set of email addresses associated with an employee profile.
 * 
 * @see EmployeeProfile
 */
public class EmailAddresses
{
  /**
   * The fully qualified field name used to store the mail field of an emailAddresses which is itself a field of the
   * profile field within an {@code Employee} in the employees collection in MongoDB
   * 
   * @see EmployeeProfile
   * @see Employee
   */
  public static final String MAIL = "profile.emailAddresses.mail";

  /**
   * The fully qualified field name used to store the targetAddress field of an emailAddresses which is itself a field
   * of the profile field within an {@code Employee} in the employees collection in MongoDB
   * 
   * @see EmployeeProfile
   * @see Employee
   */
  public static final String TARGET_ADDRESS = "profile.emailAddresses.targetAddress";

  /**
   * The fully qualified field name used to store the userAddress field of an emailAddresses which is itself a field of
   * the profile field within an {@code Employee} in the employees collection in MongoDB
   * 
   * @see EmployeeProfile
   * @see Employee
   */
  public static final String USER_ADDRESS = "profile.emailAddresses.userAddress";

  private String mail;
  private String targetAddress;
  private String userAddress;

  /**
   * EmailAddresses Constructor - No-args constructor provided for use by Morphia. Should not be used in application
   * code.
   *
   */
  public EmailAddresses()
  {
  }

  /** @param builder The builder from which to initialise a new instance of {@code EmailAddresses} */
  public EmailAddresses(Builder builder)
  {
    this.mail = builder.mail;
    this.targetAddress = builder.targetAddress;
    this.userAddress = builder.userAddress;
  }

  /** @return A {@code Set<String>} of the email addresses respresented by this instance. */
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

  /** @return {@code true} if this instance has a non-null userAddress field. */
  public boolean hasUserAddress()
  {
    return userAddress != null;
  }

  /** @return The mail email address */
  public String getMail()
  {
    return mail;
  }

  /** @param mail The value to set. */
  public void setMail(String mail)
  {
    this.mail = mail;
  }

  /** @return The targetAddress email address */
  public String getTargetAddress()
  {
    return targetAddress;
  }

  /** @param targetAddress The value to set. */
  public void setTargetAddress(String targetAddress)
  {
    this.targetAddress = targetAddress;
  }

  /** @return The userAddress email address */
  public String getUserAddress()
  {
    return userAddress;
  }

  /** @param userAddress The value to set. */
  public void setUserAddress(String userAddress)
  {
    this.userAddress = userAddress;
  }

  /**
   * @param emailAddress The return value of this method if the user address field is {@code null}.
   * @return The userAddress of this instance if it is non-null. The provided {@code emailAddress} parameter otherwise.
   */
  public String getPreferred(final String emailAddress)
  {
    if (userAddress != null && !userAddress.isEmpty())
    {
      return userAddress;
    }

    return emailAddress;
  }

  /** @return The userAddress of this instance if it is non-null. The mail of this instance otherwise. */
  public String getPreferred()
  {
    return getPreferred(mail);
  }

  /**
   * Override of equals method.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   *
   * @param o The other object to compare for equality with this object.
   * @return {@code true} if {@code o} is an instance of {@code EmailAddresses} and its members are all equal to this
   *         object's members, or it is the same instance. {@code false} otherwise.
   */
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

  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return Objects.hash(mail, targetAddress);
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return "EmailAddresses [mail= " + mail + ", targetAddress=" + targetAddress + ", userAddress=" + userAddress + "]";
  }

  /**
   * Builder used to fluently construct instances of {@code EmailAddresses}.
   */
  public static class Builder
  {
    private String mail;
    private String targetAddress;
    private String userAddress;

    /**
     * Builder Constructor - Responsible for initialising this object.
     */
    public Builder()
    {
    }

    /**
     * @param mail The value of mail to set.
     * @return This builder.
     */
    public Builder mail(String mail)
    {
      this.mail = mail;
      return this;
    }

    /**
     * @param targetAddress The value of targetAddress to set.
     * @return This builder.
     */
    public Builder targetAddress(String targetAddress)
    {
      this.targetAddress = targetAddress;
      return this;
    }

    /**
     * @param userAddress The value of userAddress to set.
     * @return This builder.
     */
    public Builder userAddress(String userAddress)
    {
      this.userAddress = userAddress;
      return this;
    }

    /** @return A new instance of {@code EmailAddresses} using the member values set in this builder. */
    public EmailAddresses build()
    {
      return new EmailAddresses(this);
    }
  }
}
