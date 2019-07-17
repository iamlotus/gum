package jinlo.gum.core.reduce;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Find first element satisfy predicate
 * @param <T>
 */
public class FirstOf<T> implements Reducer<T, T> {

    private Predicate<T> predicate;

    public FirstOf(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public T reduce(Stream<T> src) {
        if (src == null) {
            return null;
        } else {
                Optional<T> result=src.filter(predicate).findFirst();
            return result.isPresent() ? result.get() : null;
        }
    }




}
