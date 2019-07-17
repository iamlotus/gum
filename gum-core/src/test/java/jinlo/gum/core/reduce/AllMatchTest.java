package jinlo.gum.core.reduce;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AllMatchTest {

    @Test
    public void name() {
    }

    @Test
    public void testReduce() {
        assertFalse( Reducers.<String>allMatch(Objects::nonNull).reduce(null));
        // If the stream is empty then {@code true} is returned and the predicate is not evaluated.
        assertTrue( Reducers.allMatch(Objects::nonNull).reduce(new ArrayList().stream()));
        assertFalse( Reducers.<String>allMatch(Objects::nonNull).reduce(Lists.newArrayList((String)null).stream()));
        assertFalse( Reducers.<String>allMatch(Objects::nonNull).reduce(Arrays.asList(null,"a").stream()));
        assertFalse( Reducers.<String>allMatch(Objects::nonNull).reduce(Arrays.asList("a",null).stream()));
        assertTrue( Reducers.<String>allMatch(Objects::nonNull).reduce(Arrays.asList("a").stream()));
        assertTrue( Reducers.<String>allMatch(Objects::nonNull).reduce(Arrays.asList("a","b").stream()));

    }

}