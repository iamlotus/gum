package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class NoneTest {

    @Test
    public void name() {
    }

    @Test
    public void testReduce() {
        assertEquals(Collections.emptyList(), Reducers.none().reduce(null));
        assertEquals(Collections.emptyList(), Reducers.none().reduce(new ArrayList().stream()));
        assertEquals(Arrays.asList("a"), Reducers.<String>none().reduce(Arrays.asList("a").stream()));
        assertEquals(Arrays.asList("a", "b"), Reducers.<String>none().reduce(Arrays.asList("a", "b").stream()));
    }

}