package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class FirstOfTest {

    @Test
    public void name() {
    }

    @Test
    public void testReduce(){
        assertEquals(null,Reducers.<String>firstOf(Objects::nonNull).reduce(null));
        assertEquals(null,Reducers.firstOf(Objects::nonNull).reduce(new ArrayList().stream()));
        assertEquals("a",Reducers.<String>firstOf(Objects::nonNull).reduce(Arrays.asList(null,"a").stream()));
        assertEquals("a",Reducers.<String>firstOf(Objects::nonNull).reduce(Arrays.asList(null,"a","b").stream()));
    }

}