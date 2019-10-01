package jinlo.gum.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Domain service, platform developer build {@link DomainService} with fine-grained {@link DomainFunction}s
 * to express more coarse-grained service. In general, {@link DomainService} is more analyser friendly, which
 * can be used as a node in BPMN.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DomainService {

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
     * @return a class annotated with {@link Domain}
     */
    Class<?> domain();



}
