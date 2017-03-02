package utils.sequence;

public interface Sequence<E>
{
  E next() throws SequenceException;
  boolean hasNext();
}
