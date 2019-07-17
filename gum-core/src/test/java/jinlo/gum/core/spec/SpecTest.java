package jinlo.gum.core.spec;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpecTest {
    class MySpec extends Spec{

        public MySpec(String code, String name) {
            super(code, name);
        }
    }

    MySpec m0=new MySpec("code1","code1");
    MySpec m1=new MySpec("code1","name1");
    MySpec m2=new MySpec("code1","name2");
    MySpec m3=new MySpec("code2","name1");

    @Test
    public void testEquals(){
        Assert.assertEquals(m1,m2);
        Assert.assertNotEquals(m1,m3);
    }

    @Test
    public void testToString(){
        assertEquals("[MySpec code=\"code1\"]",m0.toString());
        assertEquals("[MySpec code=\"code1\", name=\"name1\"]",m1.toString());
    }

}