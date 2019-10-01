package jinlo.gum.core.utils;

import jinlo.gum.core.annotation.Extension;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;



public class AnnotationUtils {
    private AnnotationUtils() {

    }

    /**
     * Is clz a valid {@link Extension}, e.g. {@link FunctionalInterface}
     *
     * @param clz
     * @return
     */
    public static boolean isValidExtension(Class<?> clz) {
        if (!clz.isInterface()) {
            return false;
        }

        long abstractMethodCount = Arrays.stream(clz.getMethods()).filter(m -> Modifier.isAbstract(m.getModifiers())).collect(Collectors.counting());

        return abstractMethodCount==1;

    }
}
