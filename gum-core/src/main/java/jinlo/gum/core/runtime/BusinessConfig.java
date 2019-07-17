package jinlo.gum.core.runtime;


import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.spec.BusinessTemplateSpec;
import jinlo.gum.core.spec.ExtensionImplementationSpec;
import jinlo.gum.core.spec.SystemTemplateSpec;

import java.util.List;
import java.util.Set;

/**
 * 业务配置，决定当前环境中对指定业务编码生效的系统模板/业务模板及扩展点的冲突优先级。每个业务配置都隶属于一个当前环境({@link Environment})
 */
public interface BusinessConfig {

    /**
     * 当前业务配置对哪些业务编码生效，主要起文档作用。如果对应业务的{@link BusinessCodeParser}返回不在
     * 这里的编码，运行时会有warning，但不会抛异常
     * @return
     */
    Set<BusinessCode> getBusinessCodes();

    /**
     * 所属当前环境
     * @return
     */
    Environment getParentEnvironment();

    /**
     * 当前配置中生效的产品
     * @return
     */
    Set<SystemTemplateSpec> getSystemTemplates();


    /**
     * 当前配置中生效的业务
     * @return
     */
    BusinessTemplateSpec getBusinessTemplate();

    /**
     * 当前配置中生效的所有扩展点的编码
     * @return
     */
    Set<String> getEffectiveExtensionCodes();

    /**
     * 当前配置中指定扩展点编码的所有实现(按照优先级排序)，如果编码不合法，抛出异常
     * @param extensionCode
     * @return
     */
    List<ExtensionImplementationSpec> getImplementationsByCode(String extensionCode);
}
