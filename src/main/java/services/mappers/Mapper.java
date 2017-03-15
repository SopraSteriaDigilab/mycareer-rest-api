package services.mappers;

public interface Mapper<T, R>
{
  R map(T t1, T t2);
}
