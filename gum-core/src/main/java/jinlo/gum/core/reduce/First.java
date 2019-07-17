package jinlo.gum.core.reduce;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Find first element
 * @param <T>
 */
public class First<T> implements Reducer<T, T> {

    @Override
    public T reduce(Stream<T> src) {
        if (src == null) {
            return null;
        } else {
            Optional<T> result = src.findFirst();
            return result.isPresent() ? result.get() : null;
        }
    }
}
