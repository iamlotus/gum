package jinlo.gum.core.runtime;


import jinlo.gum.core.spec.*;

import java.util.Set;

/**
 * 运行环境，一个应用有且只有一个运行环境，包含应用内所有可被识别的域、能力、扩展点、门面及模板。
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
    Set<BusinessTemplateSpec> getBusinessTemplates();


    /**
     * 所有的产品
     * @return
     */
    Set<SystemTemplateSpec> getSystemTemplates();


}
