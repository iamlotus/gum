package jinlo.gum.core.runtime;

import jinlo.gum.core.exception.BusinessConfigException;
import jinlo.gum.core.exception.BusinessProcessException;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.spec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 从XML文件创建
 */
public class XmlBusinessConfigBuilder implements BusinessConfigBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlainEnvironmentBuilder.class);

    // express wrong_config in bean
    static class BusinessConfigXMLBean {
        private Set<String> businessCodes = new HashSet<>();
        private Set<String> systemTemplates = new HashSet<>();
        private String businessTemplate;
        private Map<String, List<String>> orders = new HashMap<>();

        public String getBusinessTemplate() {
            return businessTemplate;
        }

        public void setBusinessTemplate(String businessTemplate) {
            this.businessTemplate = businessTemplate;
        }

        public Set<String> getBusinessCodes() {
            return businessCodes;
        }

        public Set<String> getSystemTemplates() {
            return systemTemplates;
        }

        public Map<String, List<String>> getOrders() {
            return orders;
        }
    }

    public static class BusinessConfigImpl implements BusinessConfig {


        private Set<BusinessCode> businessCodes = new HashSet<>();

        private Environment environment;

        private Set<SystemTemplateSpec> systemTemplates = new HashSet<>();

        private BusinessTemplateSpec businessTemplateSpec;

        private Map<String, List<ExtensionImplementationSpec>> implementations = new HashMap<>();


        @Override
        public Set<BusinessCode> getBusinessCodes() {
            return businessCodes;
        }


        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        public void setBusinessTemplateSpec(BusinessTemplateSpec businessTemplateSpec) {
            this.businessTemplateSpec = businessTemplateSpec;
        }

        @Override
        public Environment getParentEnvironment() {
            return environment;
        }

        @Override
        public Set<SystemTemplateSpec> getSystemTemplates() {
            return systemTemplates;
        }

        @Override
        public BusinessTemplateSpec getBusinessTemplate() {
            return businessTemplateSpec;
        }

        @Override
        public Set<String> getEffectiveExtensionCodes() {
            return implementations.keySet();
        }

        @Override
        public List<ExtensionImplementationSpec> getImplementationsByCode(String extensionCode) {
            List<ExtensionImplementationSpec> impls = implementations.get(extensionCode);
            if (impls == null) {
                throw new BusinessProcessException(extensionCode + " is not an effective Extension Code");
            }

            return impls;
        }

        @Override
        public String toString() {
            return "[BusinessConfig " + businessTemplateSpec == null ? "" : businessTemplateSpec.getCode() + "]";
        }
    }

    // for test
    BusinessConfigXMLBean bean;

    public XmlBusinessConfigBuilder(InputStream is) {
        try {
            this.bean = parseXmlBean(is);
        } catch (Exception e) {
            if (e instanceof BusinessConfigException) {
                throw (BusinessConfigException) e;
            } else {
                throw new BusinessConfigException("can not create BusinessConfig", e);
            }
        }
        LOGGER.info("load BusinessConfig for code {} successfully", bean.getBusinessCodes());
    }

    public XmlBusinessConfigBuilder(BusinessConfigXMLBean bean) {
        this.bean = bean;
    }

    private BusinessConfigXMLBean parseXmlBean(InputStream is) throws Exception {
        // 目前使用JDK自带DOM和XPATH，未来考虑采用内存占用更小的SAX
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        XPath xpath = XPathFactory.newInstance().newXPath();
        BusinessConfigXMLBean bean = new BusinessConfigXMLBean();

        NodeList businessCodes = (NodeList) xpath.evaluate("/BusinessTemplateConfig/BusinessCodes/BusinessCode", doc, XPathConstants.NODESET);
        for (int i = 0; i < businessCodes.getLength(); i++) {
            String businessCode = businessCodes.item(i).getTextContent();
            if (businessCode.isEmpty()) {
                String msg = "find empty businessCode";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }
            if (!bean.businessCodes.add(businessCode)) {
                String msg = "find duplicate businessCode:" + businessCode;
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }
        }

        if (bean.businessCodes.isEmpty()) {
            String msg = "must specify at least one BusinessCode";
            LOGGER.error(msg);
            throw new BusinessConfigException(msg);
        }

        String businessTemplateCode = (String) xpath.evaluate("/BusinessTemplateConfig/BusinessTemplate/@code", doc, XPathConstants.STRING);
        if (businessTemplateCode == null) {
            String msg = "must specify code of BusinessTemplate";
            LOGGER.error(msg);
            throw new BusinessConfigException(msg);
        }
        bean.businessTemplate = businessTemplateCode;


        NodeList systemTemplateCodes = (NodeList) xpath.evaluate("/BusinessTemplateConfig/SystemTemplates/SystemTemplate/@code", doc, XPathConstants.NODESET);
        for (int i = 0; i < systemTemplateCodes.getLength(); i++) {
            String systemTemplateCode = systemTemplateCodes.item(i).getTextContent();
            if (systemTemplateCode.isEmpty()) {
                String msg = "find empty SystemTemplate Code";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }
            if (!bean.systemTemplates.add(systemTemplateCode)) {
                String msg = "find duplicate SystemTemplate Code:" + systemTemplateCode;
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }
        }

        NodeList orderExtensions = (NodeList) xpath.evaluate("/BusinessTemplateConfig/Order/Extension", doc, XPathConstants.NODESET);
        for (int i = 0; i < orderExtensions.getLength(); i++) {
            Node extensionNode = orderExtensions.item(i);
            String extensionCode = xpath.evaluate("./@code", extensionNode);

            if (extensionCode.isEmpty()) {
                String msg = "find empty Extension Code";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }

            NodeList extensionFacadeCodes = (NodeList) xpath.evaluate("./ExtensionFacade/@code", extensionNode, XPathConstants.NODESET);
            List<String> facadeCodes = new ArrayList<>();
            for (int j = 0; j < extensionFacadeCodes.getLength(); j++) {
                String code = extensionFacadeCodes.item(j).getTextContent();
                if (code.isEmpty()) {
                    String msg = "find empty ExtensionFacade Code";
                    LOGGER.error(msg);
                    throw new BusinessConfigException(msg);
                } else {
                    facadeCodes.add(code);
                }
            }

            if (facadeCodes.isEmpty()) {
                String msg = "find no ExtensionFacade Code for Extension " + extensionCode;
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }

            if (new HashSet<>(facadeCodes).size() != facadeCodes.size()) {
                String msg = "find duplicated ExtensionFacade Code for Extension " + extensionCode;
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }

            if (bean.getOrders().put(extensionCode, facadeCodes) != null) {
                String msg = "find duplicated Extension Code " + extensionCode;
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }
        }

        return bean;
    }

    @Override
    public BusinessConfig build(Environment env) {

        BusinessConfigImpl config = new BusinessConfigImpl();

        config.environment = env;
        config.businessCodes.addAll(bean.businessCodes.stream().map(BusinessCode::of).collect(Collectors.toSet()));

        Optional<BusinessTemplateSpec> business = env.getBusinessTemplates().stream().filter(e -> e.getCode().equals(bean.businessTemplate)).findFirst();
        if (!business.isPresent()) {
            String msg = "can not find BusinessTemplate " + bean.businessTemplate + " in environment";
            LOGGER.error(msg);
            throw new BusinessConfigException(msg);
        } else {
            config.businessTemplateSpec = business.get();
        }

        Map<String, SystemTemplateSpec> systems = env.getSystemTemplates().stream().collect(Collectors.toMap(Spec::getCode, Function.identity()));
        bean.systemTemplates.forEach(systemCode -> {
            SystemTemplateSpec system = systems.get(systemCode);
            if (system == null) {
                String msg = "can not find SystemTemplate " + systemCode + " in environment";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            } else {
                config.systemTemplates.add(system);
            }
        });

        // collect all effective extension and optional facades from BusinessTemplate and SystemTemplates
        Map<ExtensionSpec, Set<ExtensionFacadeSpec>> optionalExt2Facades = new HashMap<>();
        // initialize from business
        optionalExt2Facades.putAll(config.businessTemplateSpec.getExtensions());
        // supplement from systems
        for (SystemTemplateSpec systemTemplate : config.systemTemplates) {
            for (Map.Entry<ExtensionSpec, Set<ExtensionFacadeSpec>> e : systemTemplate.getExtensions().entrySet()) {
                ExtensionSpec extensionSpec = e.getKey();
                Set<ExtensionFacadeSpec> facadeSpecs = e.getValue();
                optionalExt2Facades.computeIfAbsent(extensionSpec, k -> new HashSet<>()).addAll(facadeSpecs);
            }
        }

        Map<String, Set<ExtensionFacadeSpec>> optionalExtCode2Facades = optionalExt2Facades.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().getCode(), Map.Entry::getValue
        ));

        Map<String, ExtensionSpec> optionalCode2Exts = optionalExt2Facades.keySet().stream().collect(Collectors.toMap(Spec::getCode, Function.identity()));
        Map<String, List<ExtensionImplementationSpec>> configImplementations = new HashMap<>();

        // 计算最终生效的扩展点顺序时以配置为基础。 如果某个扩展点在配置中显式指定了顺序，则必须指定所有实现的排序(不能只指定部分)
        for (Map.Entry<String, List<String>> e : bean.getOrders().entrySet()) {
            String extensionCode = e.getKey();
            List<String> facadeCodes = e.getValue();
            // 配置的扩展点必须存在于环境中
            Set<ExtensionFacadeSpec> optionalFacades = optionalExtCode2Facades.get(extensionCode);
            if (optionalFacades == null) {
                String msg = "can not find Extension " + extensionCode + " from config, make sure this extension is satisfied by any Template specified in config";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }

            // extension must exists
            ExtensionSpec extension = optionalCode2Exts.get(extensionCode);

            // 指定的对应实现必须存在于配置和环境中
            Map<String, ExtensionFacadeSpec> optionalCode2Facades = optionalFacades.stream().collect(Collectors.toMap(Spec::getCode, Function.identity()));
            List<ExtensionImplementationSpec> specifiedImplementations = new ArrayList<>();
            for (String facadeCode : facadeCodes) {
                ExtensionFacadeSpec facade = optionalCode2Facades.get(facadeCode);
                if (facade == null) {
                    String msg = "can not find ExtensionFacade " + facadeCode + " from config, make sure this Facade satisfies Extension " + extensionCode + " and is in any Template specified in config";
                    LOGGER.error(msg);
                    throw new BusinessConfigException(msg);
                }

                ExtensionImplementationSpec implementation = facade.getExtension2Implementations().get(extension);
                if (implementation == null) {
                    String msg = "ExtensionFacade " + facadeCode + " does not satisfy Extension " + extensionCode;
                    LOGGER.error(msg);
                    throw new BusinessConfigException(msg);
                }
                specifiedImplementations.add(implementation);
            }

            // 必须指定所有实现的顺序，如果有未指定的实现会抛异常
            if (specifiedImplementations.size() != optionalFacades.size()) {
                Set<String> specifiedImplementationCodes = specifiedImplementations.stream().map(Spec::getCode).collect(Collectors.toSet());
                Set<String> implementationsCodes = optionalFacades.stream().map(f -> f.getExtension2Implementations().get(extension).getCode()).collect(Collectors.toSet());
                implementationsCodes.removeAll(specifiedImplementationCodes);
                String msg = "Extension " + extensionCode + " is specified in Order, but implementations:" + implementationsCodes + " are not specified. You must specify the order of all its implementations if you specify any extension in Order";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }

            configImplementations.put(extensionCode, specifiedImplementations);
            LOGGER.debug("specify Order of extension {} explicitly", extensionCode);
        }


        for (Map.Entry<ExtensionSpec, Set<ExtensionFacadeSpec>> e : optionalExt2Facades.entrySet()) {
            ExtensionSpec extensionSpec = e.getKey();
            String extensionCode = extensionSpec.getCode();
            if (!configImplementations.containsKey(extensionCode)) {
                // 如果某个扩展点在配置中未显式指定顺序，实现是随机的
                LOGGER.debug("specify Order of extension {} implicitly", extensionCode);
                Set<ExtensionFacadeSpec> facadeSpecs = e.getValue();
                if (facadeSpecs.size() > 1) {
                    LOGGER.warn("Order of extension {} has multiple implementations, but they are not specified explicitly", extensionCode);
                }

                // 为了避免随机造成的排查困难，这里把所有实现按code排序，保证排序可重现
                List<ExtensionImplementationSpec> implementationSpecs = facadeSpecs.stream().map(facade -> facade.getExtension2Implementations()
                        .get(extensionSpec)).sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());

                configImplementations.put(extensionCode, implementationSpecs);
            }
        }

        config.implementations.putAll(configImplementations);

        return config;
    }
}
