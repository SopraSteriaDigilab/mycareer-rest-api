package dataStructure;

public enum CRUD
{
  ADD("added"), EDIT("edited"), COMPLETE("completed"), ARCHIVE("archive"), DELETE("deleted");
  
  private final String verb;
  
  private CRUD(final String verb)
  {
    this.verb = verb;
  }
  
  public String getVerb()
  {
    return verb;
  }
}
