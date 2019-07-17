package jinlo.gum.core.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassPathTest {

    @Test
    public void test() {
        ClassLoader loader = this.getClass().getClassLoader();
        ClassPath cp = new ClassPath(loader);
        assertTrue(cp.getErrorMessages().isEmpty());
        assertTrue(cp.getScanTime()>0);
        ClassPath.Resource resource1 = cp.getResources().stream().filter(r -> this.getClass().getName().equals(r.getResourceName())).findAny().get();
        assertTrue(resource1.isClassResource());
        assertTrue(resource1.getPath().endsWith(this.getClass().getName().replace(".", "/") + ".class"));

        ClassPath.Resource resource2 = cp.getResources().stream().filter(r -> "classpath/dir1/somefile".equals(r.getResourceName())).findAny().get();
        assertFalse(resource2.isClassResource());
        assertTrue(resource2.getPath().endsWith("classpath/dir1/somefile"));
    }

}