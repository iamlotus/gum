package jinlo.gum.core.model;

import jinlo.gum.core.testapps.app3.Business;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class BusinessCodeTest {
    @Test
    public void testEquals() {
        assertEquals(BusinessCode.of("a"), BusinessCode.of("a"));
        assertNotEquals(BusinessCode.of("a"), BusinessCode.of("b"));
        assertEquals(BusinessCode.of("a.b"), BusinessCode.of("a.b"));
    }

    @Test
    public void testOf() {
        illegalCode(".");
        illegalCode("..");
        illegalCode(".a");
        illegalCode(".abcd");
        illegalCode("a..");
        illegalCode("a..b");
        illegalCode("a.b.");
        illegalCode("a.b..");
        illegalCode("a.b.c.");
        illegalCode("a.b.c .");

        // legal
        BusinessCode.of("");
        BusinessCode.of("a");
        BusinessCode.of("abcd");
        BusinessCode.of("a1");
        BusinessCode.of("a   ");
        BusinessCode.of("a  __. ");
        BusinessCode.of("a  __. b");
        BusinessCode.of("第一");
        BusinessCode.of("第一.第二");
        BusinessCode.of("a.b");
        BusinessCode.of("a.b.c");
    }

    private void illegalCode(String code) {
        try {
            BusinessCode.of(code);
            fail("should throw IllegalArgumentException for \""+code+"\"");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testIsRoot(){
        assertTrue(BusinessCode.root().isRoot());
        assertTrue(BusinessCode.of("").isRoot());
        assertFalse(BusinessCode.of("a").isRoot());
    }


    @Test
    public void testJoin() {
        assertEquals(BusinessCode.root(),BusinessCode.root().join(BusinessCode.root()));
        assertEquals(BusinessCode.root(),BusinessCode.root().join(BusinessCode.of("a")));
        assertEquals(BusinessCode.root(),BusinessCode.root().join(BusinessCode.of("a.b")));
        assertEquals(BusinessCode.root(),BusinessCode.of("a.b.c").join(BusinessCode.of("b")));
        assertEquals(BusinessCode.of("a"),BusinessCode.of("a").join(BusinessCode.of("a")));
        assertEquals(BusinessCode.of("a.b"),BusinessCode.of("a.b").join(BusinessCode.of("a.b")));
        assertEquals(BusinessCode.of("a.b"),BusinessCode.of("a.b.c").join(BusinessCode.of("a.b")));
        assertEquals(BusinessCode.of("a"),BusinessCode.of("a.b.c").join(BusinessCode.of("a")));
        assertEquals(BusinessCode.of("a"),BusinessCode.of("a").join(BusinessCode.of("a.b")));
        assertEquals(BusinessCode.of("a.b"),BusinessCode.of("a.b").join(BusinessCode.of("a.b.c")));
    }

    @Test
    public void testAncestorOf() {
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf(BusinessCode.root()));
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf(BusinessCode.root(),BusinessCode.root()));
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf(BusinessCode.root(),BusinessCode.of("a")));
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf(BusinessCode.root(),BusinessCode.of("abc.d")));
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf(BusinessCode.of("a"),BusinessCode.of("b")));
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf(BusinessCode.of("a1"),BusinessCode.of("a2")));
        assertEquals(BusinessCode.of("a"),BusinessCode.ancestorOf(BusinessCode.of("a")));
        assertEquals(BusinessCode.of("a1.b2.c3"),BusinessCode.ancestorOf(BusinessCode.of("a1.b2.c3")));
        assertEquals(BusinessCode.of("a"),BusinessCode.ancestorOf(BusinessCode.of("a.1"),BusinessCode.of("a.2")));
        assertEquals(BusinessCode.of("a"),BusinessCode.ancestorOf(BusinessCode.of("a.b1"),BusinessCode.of("a.b2")));
        assertEquals(BusinessCode.of("a"),BusinessCode.ancestorOf(BusinessCode.of("a.b.1"),BusinessCode.of("a.b.2"),BusinessCode.of("a.c")));
        assertEquals(BusinessCode.of("a.b"),BusinessCode.ancestorOf(BusinessCode.of("a.b.1"),BusinessCode.of("a.b.2"),BusinessCode.of("a.b")));
        assertEquals(BusinessCode.of("a.b"),BusinessCode.ancestorOf(BusinessCode.of("a.b.1"),BusinessCode.of("a.b.2"),BusinessCode.of("a.b.3")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAncestorOf1() {
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAncestorOf2() {
        assertEquals(BusinessCode.root(),BusinessCode.ancestorOf(null));
    }
}