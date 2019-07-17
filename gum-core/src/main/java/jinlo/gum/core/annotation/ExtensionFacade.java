package jinlo.gum.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点门面类，提供若干扩展点的实现。{@link ExtensionFacade}必须是实体类且满足至少一个{@link Extension},否则会报错。
 * 门面类必须标注{@link ExtensionFacade}才能被系统发现并使用。
 * <p>
 * 门面类满足某个扩展点EXT定义为:在这个门面类里，存在一个无参公共实例方法，它的返回结果实现了EXT。EXT在某个门面类最多被满足一次
 * 如果这个门面类中有多于一个方法的返回结果实现了EXT，系统会报错。
 * <p>
 * 采用方法返回扩展点而不是门面类直接实现扩展点的原因是不同扩展点里的方法签名可能相同，若采用门面类直接实现，不同扩展点
 * 又包含签名相同的方法时，在同一个门面类中无法为这些扩展点提供不同的实现。但方法返回扩展点的方式可以。
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExtensionFacade {

    /**
     * @return 名称，可以是中文或英文，缺省是code(类名)
     */
    String name() default "";

    /**
     * @return 描述
     */
    String desc() default "";

    /**
     * 所属模板，可以是{@link BusinessTemplate}或者{@link SystemTemplate}，一个扩展门面类可同时属于多个模板
     *
     * @return
     */
    Class<?>[] belongsTo();
}
