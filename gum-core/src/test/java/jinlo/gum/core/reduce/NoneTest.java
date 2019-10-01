package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class NoneTest {


    @Test
    @SuppressWarnings("unchecked")
    public void testReduce() {
        assertEquals(Collections.emptyList(), Reducers.none().reduce(null));
        assertEquals(Collections.emptyList(), Reducers.none().reduce(new ArrayList().stream()));
        assertEquals(Collections.singletonList("a"), Reducers.<String>none().reduce(Stream.of("a")));
        assertEquals(Arrays.asList("a", "b"), Reducers.<String>none().reduce(Stream.of("a", "b")));
    }

}