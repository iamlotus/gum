package jinlo.gum.core.annotation;


import jinlo.gum.core.model.TemplateChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 系统模板，由系统开发者维护，在一次实例中可以有多个系统模板生效
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SystemTemplate {

    /**
     * 名称，缺省是code(类名)
     * @return
     */
    String name() default "";

    /**
     * 描述，缺省为空
     * @return
     */
    String desc() default "";

    /**
     * 判断模板是否生效
     * @return
     */
    Class<? extends TemplateChecker> checker() default TemplateChecker.AlwaysKnowChecker.class;

}
