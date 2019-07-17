package jinlo.gum.core.reduce;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collect all elements satisfy predicate
 *
 * @param <T>
 */
public class Collect<T> implements Reducer<T, List<T>> {
    private Predicate<T> predicate;

    public Collect( Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public List<T> reduce(Stream<T> elements) {
        if (elements == null) {
            return Collections.emptyList();
        } else {
            return elements.filter(predicate).collect(Collectors.toList());
        }
    }
}
