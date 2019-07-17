package jinlo.gum.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 域服务，管理用。通常会组装{@link DomainFunction}并提供更粗粒度(更具有业务含义)的实现。在流程节点中一般会调用域服务。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DomainService {

    /**
     * @return 能力名称，仅仅管理和文档用，缺省为类名
     */
    String name() default "";


    /**
     * @return 所属Domain的类名，管理和文档用
     */
    Class<?> domain();

    /**
     * 描述，仅仅管理和文档用，缺省为空
     *
     * @return
     */
    String desc() default "";

}
