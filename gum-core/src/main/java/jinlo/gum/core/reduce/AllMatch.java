package jinlo.gum.core.reduce;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Is all elements satisfy predicate
 *
 * @param <T>
 */
public class AllMatch<T> implements Reducer<T, Boolean> {
    private Predicate<T> predicate;

    public AllMatch(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public Boolean reduce(Stream<T> elements) {
        if (elements == null) {
            return false;
        } else {
            return elements.allMatch(predicate);
        }
    }
}
