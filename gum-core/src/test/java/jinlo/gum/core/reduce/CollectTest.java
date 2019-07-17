package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class CollectTest {

    @Test
    public void name() {
    }

    @Test
    public void testReduce() {
        assertEquals(Collections.emptyList(), Reducers.<String>collect(Objects::nonNull).reduce(null));
        assertEquals(Collections.emptyList(), Reducers.collect(Objects::nonNull).reduce(new ArrayList().stream()));
        assertEquals(Arrays.asList("a"), Reducers.<String>collect(Objects::nonNull).reduce(Arrays.asList(null, "a").stream()));
        assertEquals(Arrays.asList("a", "a"), Reducers.<String>collect(Objects::nonNull).reduce(Arrays.asList(null, "a", null, "a").stream()));
    }

}