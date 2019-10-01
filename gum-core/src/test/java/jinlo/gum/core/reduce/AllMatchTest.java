package jinlo.gum.core.reduce;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class AllMatchTest {

    @Test
    public void name() {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testReduce() {
        assertFalse( Reducers.<String>allMatch(Objects::nonNull).reduce(null));
        // If the stream is empty then {@code true} is returned and the predicate is not evaluated.
        assertEquals(true, Reducers.allMatch(Objects::nonNull).reduce(new ArrayList().stream()));
        assertEquals(false, Reducers.<String>allMatch(Objects::nonNull).reduce(Lists.newArrayList((String) null).stream()));
        assertFalse( Reducers.<String>allMatch(Objects::nonNull).reduce(Stream.of(null,"a")));
        assertFalse( Reducers.<String>allMatch(Objects::nonNull).reduce(Stream.of("a",null)));
        assertTrue( Reducers.<String>allMatch(Objects::nonNull).reduce(Stream.of("a")));
        assertTrue( Reducers.<String>allMatch(Objects::nonNull).reduce(Stream.of("a","b")));

    }

}