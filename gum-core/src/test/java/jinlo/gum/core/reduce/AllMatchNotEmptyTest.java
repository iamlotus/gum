package jinlo.gum.core.reduce;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class AllMatchNotEmptyTest {

    @Test
    public void name() {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testReduce() {
        assertFalse( Reducers.<String>allMatchNotEmpty(Objects::nonNull).reduce(null));
        // If the stream is empty return false
        assertFalse( Reducers.allMatchNotEmpty(Objects::nonNull).reduce(new ArrayList().stream()));
        assertEquals(false, Reducers.<String>allMatchNotEmpty(Objects::nonNull).reduce(Lists.newArrayList((String) null).stream()));
        assertFalse( Reducers.<String>allMatchNotEmpty(Objects::nonNull).reduce(Stream.of(null,"a")));
        assertFalse( Reducers.<String>allMatchNotEmpty(Objects::nonNull).reduce(Stream.of("a",null)));
        assertTrue( Reducers.<String>allMatchNotEmpty(Objects::nonNull).reduce(Stream.of("a")));
        assertTrue( Reducers.<String>allMatchNotEmpty(Objects::nonNull).reduce(Stream.of("a","b")));
    }

}