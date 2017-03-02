package services.mappers;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface BiMapper<T, R> extends BiFunction<T, T, R>
{
}
