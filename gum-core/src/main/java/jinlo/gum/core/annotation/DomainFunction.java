package jinlo.gum.core.annotation;

import jinlo.gum.core.reduce.Reducers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 域方法,管理用。在运行期某个{@link Extension}可能有若干(按照优先级排序的)实现，在{@link DomainFunction}通过指定{@link Reducers}
 * 确定如何使用这若干实现返回的结果，通过使用{@link DomainFunction}后续业务逻辑不用再考虑扩展点的具体实现。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DomainFunction {

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
