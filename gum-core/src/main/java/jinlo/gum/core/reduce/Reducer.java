package jinlo.gum.core.reduce;

import java.util.stream.Stream;

public interface Reducer<T, R> {
    R reduce(Stream<T> src);
}
