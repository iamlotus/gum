package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FirstOfTest {

    @Test
    public void name() {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testReduce(){
        assertNull(Reducers.<String>firstOf(Objects::nonNull).reduce(null));
        assertNull(Reducers.firstOf(Objects::nonNull).reduce(new ArrayList().stream()));
        assertEquals("a",Reducers.<String>firstOf(Objects::nonNull).reduce(Stream.of(null,"a")));
        assertEquals("a",Reducers.<String>firstOf(Objects::nonNull).reduce(Stream.of(null,"a","b")));
    }

}