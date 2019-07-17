package jinlo.gum.core.spec;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MethodSpecTest {

    public static int f1(List<String> in){return 0;}

    public int f2(){return 0;}

    public static void f3(){}

    @Test
    public void testConstructor() throws Exception {
        MethodSpec ms1= new MS(MethodSpecTest.class.getMethod("f1",List.class),"myname");
        assertEquals(MethodSpecTest.class.getName()+"#f1",ms1.getCode());
        assertEquals("myname",ms1.getName());
        assertEquals("f1",ms1.getMethodName());
        assertArrayEquals(new String[]{List.class.getName()},ms1.getParamTypes());
        assertEquals("int", ms1.getReturnType());

        MethodSpec ms2= new MS(MethodSpecTest.class.getMethod("f2"));
        assertEquals(MethodSpecTest.class.getName()+"#f2",ms2.getCode());
        assertEquals(ms2.getCode(),ms2.getName());
        assertEquals("f2",ms2.getMethodName());
        assertArrayEquals(new String[]{},ms2.getParamTypes());
        assertEquals("int", ms2.getReturnType());

        MethodSpec ms3= new MS(MethodSpecTest.class.getMethod("f3"));
        assertEquals(MethodSpecTest.class.getName()+"#f3",ms3.getCode());
        assertEquals(ms3.getCode(),ms3.getName());
        assertEquals("f3",ms3.getMethodName());
        assertArrayEquals(new String[]{},ms3.getParamTypes());
        assertEquals("void", ms3.getReturnType());
    }
}

class MS extends MethodSpec{
    public MS(Method m){
        super(m);
    }

    public MS(Method m, String name){
        super(m,name);
    }

}