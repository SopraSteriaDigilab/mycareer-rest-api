package dataStructure;

/**
 * Actions which may taken by users and recorded as part of an activity.
 * 
 * @see Activity
 */
public enum Action
{
  ADD("added"), EDIT("edited"), COMPLETE("completed"), ARCHIVE("archived"), RESTORE("restored"), DELETE(
      "deleted"), REQUEST("requested");

  private final String verb;

  private Action(final String verb)
  {
    this.verb = verb;
  }

  /**
   * @return The past participle of this action. For example, this method called on the {@code ADD} action would return
   *         the {@code String} "added".
   */
  public String getVerb()
  {
    return verb;
  }
}
