package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class CollectTest {

    @Test
    public void name() {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testReduce() {
        assertEquals(Collections.emptyList(), Reducers.<String>collect(Objects::nonNull).reduce(null));
        assertEquals(Collections.emptyList(), Reducers.collect(Objects::nonNull).reduce(new ArrayList().stream()));
        assertEquals(Collections.singletonList("a"), Reducers.<String>collect(Objects::nonNull).reduce(Stream.of(null, "a")));
        assertEquals(Arrays.asList("a", "a"), Reducers.<String>collect(Objects::nonNull).reduce(Stream.of(null, "a", null, "a")));
    }

}