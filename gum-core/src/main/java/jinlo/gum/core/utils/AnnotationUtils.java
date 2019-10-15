package jinlo.gum.core.utils;

import jinlo.gum.core.annotation.Extension;
import jinlo.gum.core.annotation.ExtensionFacade;

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

        return abstractMethodCount == 1;

    }

    /**
     * Is clz a valid {@link ExtensionFacade}, e.g. must be a public concrete class with a public no-param constructor
     *
     * @param clz
     * @return
     */
    public static boolean isValidExtensionFacade(Class<?> clz) {
        if (!Modifier.isPublic(clz.getModifiers())
                || Modifier.isAbstract(clz.getModifiers())
                || (clz.isMemberClass() && !Modifier.isStatic(clz.getModifiers())))
        {
            return false;
        }

        try {
            clz.getConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }

        return true;
    }
}
