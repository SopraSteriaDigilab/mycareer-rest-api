package dataStructure;

/**
 * 
 * TODO: Describe this TYPE.
 *
 */
public enum CRUD
{
  ADD("added"), EDIT("edited"), COMPLETE("completed"), ARCHIVE("archived"), RESTORE("restored"), DELETE("deleted");

  private final String verb;

  private CRUD(final String verb)
  {
    this.verb = verb;
  }

  /**
   * 
   * TODO: Describe this method.
   *
   * @return
   */
  public String getVerb()
  {
    return verb;
  }
}
