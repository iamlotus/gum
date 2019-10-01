package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FirstTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testReduce(){
        assertNull(Reducers.first().reduce(null));
        assertNull(Reducers.first().reduce(new ArrayList().stream()));
        assertEquals("a",Reducers.<String>first().reduce(Stream.of("a")));
        assertEquals("a",Reducers.<String>first().reduce(Stream.of("a","b")));
    }

}