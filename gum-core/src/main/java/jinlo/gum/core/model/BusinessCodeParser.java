package jinlo.gum.core.model;

import jinlo.gum.core.annotation.Extension;

/**
 * 每个需要共建的应用都会定义若干核心业务对象，这些业务对象的实例上有足够信息，可以用来分辨{@link BusinessCode}。
 * 每个独立维护的业务都有一个自己的{@link BusinessCodeParser}，接收这些业务对象，并分辨业务编码。
 * <p>
 * 系统会轮询所有业务的{@link BusinessCodeParser}
 * 获得对应的{@link BusinessCode}，并结合业务配置确定如何解决{@link Extension}调用过程中的冲突。
 * 注意系统中所有实现必须<b>互斥</b>:对一个instance，有且只有一个实现能识别此实例,否则会报错。
 */
public interface BusinessCodeParser {

    /**
     * 是否能识别实例
     * @param instance
     * @return
     */
    boolean knows(Object instance);

    /**
     * 从实例中识别业务ID,系统会保证这里的入参通过了{@link #knows(Object)}校验。
     * @param instance
     * @return 业务ID，不能为null
     * @throws IllegalArgumentException 如果无法正确识别业务身份
     */
    BusinessCode parse(Object instance) throws IllegalArgumentException;
}
