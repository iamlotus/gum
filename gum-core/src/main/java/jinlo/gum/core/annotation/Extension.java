package jinlo.gum.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点。扩展点必须是一个{@link FunctionalInterface}，以扩展点的类名作为扩展点的code，指代其中(唯一的)方法。
 * SDK维护者通常会定义若干{@link Extension}并以 {@link ExtensionFacade}的方式暴露给共建者，以实现业务自定义实现
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Extension {

    /**
     * @return 名称，可以是中文或英文，缺省是code(类名)
     */
    String name() default "";

    /**
     *
     * @return 描述
     */
    String desc() default "";

    Class<?> domain();
}
