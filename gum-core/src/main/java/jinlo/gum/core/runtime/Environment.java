package jinlo.gum.core.runtime;


import jinlo.gum.core.spec.*;

import java.util.Set;

/**
 * {@link Environment} includes all basic components (Domain, Extensions, DomainFunction, etc.) that can be used to construct {@link BusinessConfig}.
 * An environment is initialized by scanning classpath during application initialization, and it NOT designed for hot-replacement
 * during application's lifetime ( as a contrast, {@link BusinessConfig} is designed to be able to be replaced in runtime.
 */
public interface Environment {

    /**
     * 所有的域
     * @return
     */
    Set<DomainSpec> getDomains();

    /**
     * 所有的扩展点
     * @return
     */
    Set<ExtensionSpec> getExtensions();


    /**
     * 所有的域函数
     * @return
     */
    Set<DomainFunctionSpec> getDomainFunctions();

    /**
     * 所有的域服务
     * @return
     */
    Set<DomainServiceSpec> getDomainServices();

    /**
     * 所有的业务
     * @return
     */
    Set<BusinessSpec> getBusinesses();


    /**
     * 所有的产品
     * @return
     */
    Set<ProductSpec> getProducts();


}
