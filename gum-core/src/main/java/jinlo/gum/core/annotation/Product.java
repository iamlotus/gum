package jinlo.gum.core.annotation;


import jinlo.gum.core.model.InstanceRecgonizer;
import jinlo.gum.core.runtime.BusinessConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A {@link Product} provides customized behaviors to simplify constructing {@link BusinessConfig}. One product can be used
 * in different BusinessConfig (while {@link Business} can be used in only one BusinessConfig).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Product {

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
     * customized behaviors
     * @return
     */
    Class<?>[] facades();

    /**
     * does business know instance?
     * @return
     */
    Class<? extends InstanceRecgonizer> recgonizer() default InstanceRecgonizer.PositiveRecgonizer.class;

}
