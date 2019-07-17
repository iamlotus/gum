package jinlo.gum.core.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassPathTest {

    class MyInner{

    }

    @Test
    public void test() {
        ClassLoader loader = this.getClass().getClassLoader();
        ClassPath cp = new ClassPath(loader);
        assertTrue(cp.getScanTime()>0);

        ClassPath.Resource resource1 = cp.getResources().stream().filter(r -> (r.getResourceName().endsWith(this.getClass().getSimpleName()+".class"))).findAny().get();
        assertTrue(resource1.isClass());
        assertEquals(this.getClass().getName(), resource1.getClassName());
        assertEquals(this.getClass().getName().replace(".","/")+".class", resource1.getResourceName());

        ClassPath.Resource resource2 = cp.getResources().stream().filter(r -> "classpath/dir1/somefile".equals(r.getResourceName())).findAny().get();
        assertFalse(resource2.isClass());

        ClassPath.Resource resource3 = cp.getResources().stream().filter(r -> (r.getResourceName().endsWith(MyInner.class.getSimpleName()+".class"))).findAny().get();
        assertTrue(resource3.isClass());
        assertEquals(MyInner.class.getName(), resource3.getClassName());
        assertEquals(MyInner.class.getName().replace(".","/")+".class", resource3.getResourceName());
    }

}