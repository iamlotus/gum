package jinlo.gum.core.runtime;

import org.junit.Test;

import static jinlo.gum.core.utils.AnnotationUtils.isValidExtension;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnnotationUtilsTest {

    abstract class C1 {
        public abstract int i();
    }

    class C2 {
        public int i() {
            return 1;
        }
    }

    interface I1 {

    }

    @FunctionalInterface
    interface I2 extends Runnable {
    }

    @FunctionalInterface
    interface I3 {
        int i();
    }

    interface I4 extends Runnable {
        int i();
    }

    @FunctionalInterface
    interface I5 {
        int i();

        default int i2() {
            return 1;
        }
    }

    interface I6 {
        int i();

        int j();
    }

    interface I7 {
        int i();

        int j();

        default int i2() {
            return 1;
        }
    }

    @Test
    public void testIsValidExtension() {
        assertFalse(isValidExtension(C1.class));
        assertFalse(isValidExtension(C2.class));
        assertFalse(isValidExtension(I1.class));
        assertTrue(isValidExtension(I2.class));
        assertTrue(isValidExtension(I3.class));
        assertFalse(isValidExtension(I4.class));
        assertTrue(isValidExtension(I5.class));
        assertFalse(isValidExtension(I6.class));
        assertFalse(isValidExtension(I7.class));
    }

}