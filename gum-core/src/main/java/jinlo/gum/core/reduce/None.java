package jinlo.gum.core.reduce;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collect all elements to List
 *
 * @param <T>
 */
public class None<T> implements Reducer<T, List<T>> {
    @Override
    public List<T> reduce(Stream<T> elements) {
        if (elements == null) {
            return Collections.emptyList();
        } else {
            return elements.collect(Collectors.toList());
        }
    }
}
