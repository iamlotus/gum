package jinlo.gum.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jinlo.gum.core.model.BusinessCodeParser;

/**
 * {@link Extension} must be a {@link FunctionalInterface}, it describes a method which is defined
 * by platform developer and implemented by other (platform or business) developers. There may be more
 * than one implementations in different {@link ExtensionFacade}, the core concept of gum framework
 * is to pick suitable implementations by {@link BusinessCodeParser} and {@link Business}
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Extension {

    /**
     * @return name, default to class name, for document purpose.
     */
    String name() default "";

    /**
     * @return description, for document purpose.
     */
    String desc() default "";

    /**
     * @return a class annotated with {@link Domain}
     */
    Class<?> domain();
}
