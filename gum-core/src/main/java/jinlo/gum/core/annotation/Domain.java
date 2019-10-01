package jinlo.gum.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Domain} is the root concept, all design concepts are associated with their domain (explicitly or implicitly).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Domain {

    /**
     * name, default to class name, for document purpose
     *
     * @return
     */
    String name() default "";

    /**
     * description, for document purpose
     *
     * @return
     */
    String desc() default "";


    /**
     * parent, for document purpose.
     * @return
     */
    Class<?> parentDomainClass() default void.class;



}