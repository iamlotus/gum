package jinlo.gum.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务域
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Domain {

    /**
     * 所属父域，仅仅管理和文档用，缺省为null
     * @return
     */
    Class<?> parentDomainClass() default void.class;

    /**
     * 域名称，仅仅管理和文档用，缺省为类名
     * @return
     */
    String name() default "";

    /**
     * 域描述，仅仅管理和文档用，缺省为空
     * @return
     */
    String desc() default "";

}