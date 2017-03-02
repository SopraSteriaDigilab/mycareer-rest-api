package utils;

public class StringSequence implements Sequence<String>
{
  private String current;
  
  private final int characterToChange;
  private final int increment;
  private final int size;
  
  private int counter = 0;
  
  private StringSequence(StringSequenceBuilder builder)
  {
    this.current = builder.initial;
    this.characterToChange = builder.characterToChange;
    this.increment = builder.increment;
    this.size = builder.size;
  }
  
  @Override
  public String next() throws SequenceException
  {
    if (!hasNext())
    {
      throw new SequenceException("The Sequence has ended.");
    }
    
    final String retVal = current;
    counter++;
    
    if (hasNext())
    {
      updateCurrent();
    }
    
    return retVal;
  }
  
  @Override
  public boolean hasNext()
  {
    return size > counter;
  }
  
  protected void updateCurrent()
  {
    char updateChar = current.charAt(characterToChange);
    updateChar += increment;
    current = current.substring(0, characterToChange) + updateChar + current.substring(characterToChange + 1);
  }
  
  public static final class StringSequenceBuilder
  {
    private String initial;
    private int characterToChange;
    private int increment;
    private int size;
    
    public StringSequenceBuilder initial(String initial)
    {
      this.initial = initial;
      return this;
    }
    
    public StringSequenceBuilder characterToChange(int characterToChange)
    {
      this.characterToChange = characterToChange;
      return this;
    }
    
    public StringSequenceBuilder increment(int increment)
    {
      this.increment = increment;
      return this;
    }
    
    public StringSequenceBuilder size(int size)
    {
      this.size = size;
      return this;
    }
    
    public Sequence<String> build() throws SequenceException
    {
      if (!validate())
      {
        throw new SequenceException();
      }
      
      return new StringSequence(this);
    }
    
    // TODO: separate validation, tailor exception messages, improve check size in range
    private boolean validate()
    {
      boolean initialNotNull = initial != null;
      boolean characterInRange = initial.length() >= characterToChange;
      boolean incrementNotZero = increment != 0;
      boolean sizeInRange = size <= Character.MAX_VALUE;
      
      return initialNotNull && characterInRange && incrementNotZero;
    }
  }
}
