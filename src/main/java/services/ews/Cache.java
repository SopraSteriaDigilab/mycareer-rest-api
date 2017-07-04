package services.ews;

public interface Cache<K, V>
{
  V put(K key, V value);
  V get(K key);
  boolean contains(K key);
  void clear();
}
