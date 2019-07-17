package jinlo.gum.core.reduce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FirstTest {

    @Test
    public void name() {
    }

    @Test
    public void testReduce(){
        assertEquals(null,Reducers.first().reduce(null));
        assertEquals(null,Reducers.first().reduce(new ArrayList().stream()));
        assertEquals("a",Reducers.<String>first().reduce(Arrays.asList("a").stream()));
        assertEquals("a",Reducers.<String>first().reduce(Arrays.asList("a","b").stream()));
    }

}