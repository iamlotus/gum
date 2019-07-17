package jinlo.gum.core.annotation;


import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.model.TemplateChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 业务模板，由共建者创建，在一次实例中只能有一个业务模板生效(通过{@link BusinessCodeParser}的返回值确定)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BusinessTemplate {

    /**
     * 名称，缺省是类名
     *
     * @return
     */
    String name() default "";

    /**
     * 描述，缺省为空
     *
     * @return
     */
    String desc() default "";

    /**
     * 业务编码Parser
     * @return
     */
    Class<? extends BusinessCodeParser> parser();

    /**
     * 判断模板是否生效，这两者
     * @return
     */
    Class<? extends TemplateChecker> checker() default TemplateChecker.AlwaysKnowChecker.class;

}
