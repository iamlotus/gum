package jinlo.gum.core.annotation;


import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.model.InstanceRecgonizer;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.runtime.BusinessConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * A {@link Business} owns business instances of specified {@link BusinessCode}, recognize them by its {@link BusinessCodeParser}, and
 * providing customized behaviors by its facades. Business is exclusive in {@link BusinessConfig}, one and only one Business
 * is effective for specified business instance during the lifetime of a {@link jinlo.gum.core.runtime.BusinessProcess}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Business {

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
     * business code parser
     * @return
     */
    Class<? extends BusinessCodeParser> parser();

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
