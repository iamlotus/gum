package jinlo.gum.core.runtime;


/**
 * 创建业务配置
 */
public interface BusinessConfigBuilder {
    /**
     * 从某个环境创建业务配置
     *
     * @param env
     * @return
     */
    BusinessConfig build(Environment env);
}
