package jinlo.gum.core.runtime;


import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.spec.BusinessSpec;
import jinlo.gum.core.spec.ExtensionImplementationSpec;
import jinlo.gum.core.spec.ProductSpec;

import java.util.List;
import java.util.Set;

/**
 * 业务配置，决定当前环境中对指定业务编码生效的系统模板/业务模板及扩展点的冲突优先级。每个业务配置都隶属于一个当前环境({@link Environment})
 */
public interface BusinessConfig {
    /**
     * 能否识别指定业务编码, 要求{@link BusinessConfig}必须能识别自己配置的{@link BusinessCodeParser}
     * parse出的业务编码，否则会抛异常。
     * @param businessCode
     * @return
     */
    boolean knows(BusinessCode businessCode);

    /**
     * 所属当前环境
     * @return
     */
    Environment getParentEnvironment();

    /**
     * 当前配置中生效的产品
     * @return
     */
    Set<ProductSpec> getProducts();

    /**
     * 当前配置中生效的业务
     * @return
     */
    BusinessSpec getBusiness();

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
