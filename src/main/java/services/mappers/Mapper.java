package services.mappers;

public interface Mapper<T, R>
{
  R map(T t);
}
