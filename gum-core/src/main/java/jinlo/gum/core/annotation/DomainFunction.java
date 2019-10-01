package jinlo.gum.core.annotation;

import jinlo.gum.core.ExtensionCallback;
import jinlo.gum.core.reduce.Reducer;
import jinlo.gum.core.reduce.Reducers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jinlo.gum.core.runtime.BusinessProcesses;

/**
 * Domain function, marks function that encapsulates invocation of {@link BusinessProcesses#executeExtension}, which
 * specifies the way to deal with multiple results from {@link ExtensionFacade} by giving {@link Reducers}. Platform
 * developer use {@link DomainFunction} as basic business unit for further usage (constructing {@link DomainService}, for example).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DomainFunction {

    /**
     * @return name, default to class name, for document purpose.
     */
    String name() default "";


    /**
     * @return a class annotated with {@link Domain}
     */
    Class<?> domain();

    /**
     * @return description, for document purpose.
     */
    String desc() default "";

}
