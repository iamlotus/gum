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

    // express config in bean
    static class BusinessConfigXMLBean {
        private Set<String> businessCodes = new HashSet<>();
        private Set<String> products = new HashSet<>();
        private String business;
        private Map<String, List<String>> orders = new HashMap<>();

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public Set<String> getBusinessCodes() {
            return businessCodes;
        }

        public Set<String> getProducts() {
            return products;
        }

        public Map<String, List<String>> getOrders() {
            return orders;
        }
    }

    public static class BusinessConfigImpl implements BusinessConfig {


        private Set<BusinessCode> businessCodes = new HashSet<>();

        private Environment environment;

        private Set<ProductSpec> productSpecs = new HashSet<>();

        private BusinessSpec businessSpec;

        private Map<String, List<ExtensionImplementationSpec>> implementations = new HashMap<>();


        @Override
        public boolean knows(BusinessCode businessCode) {
            return businessCodes.contains(businessCode);
        }

        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        public void setBusinessSpec(BusinessSpec businessSpec) {
            this.businessSpec = businessSpec;
        }

        @Override
        public Environment getParentEnvironment() {
            return environment;
        }

        @Override
        public Set<ProductSpec> getProducts() {
            return productSpecs;
        }

        @Override
        public BusinessSpec getBusiness() {
            return businessSpec;
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
            return "[BusinessConfig " + businessSpec == null ? "" : businessSpec.getCode() + "]";
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
        // USE DOM && XPATH
        // TODO: use SAX for less memory usage
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        XPath xpath = XPathFactory.newInstance().newXPath();
        BusinessConfigXMLBean bean = new BusinessConfigXMLBean();

        NodeList businessCodes = (NodeList) xpath.evaluate("/BusinessConfig/BusinessCodes/BusinessCode", doc, XPathConstants.NODESET);
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

        String business = (String) xpath.evaluate("/BusinessConfig/Business/@code", doc, XPathConstants.STRING);
        if (business == null) {
            String msg = "must specify code of Business";
            LOGGER.error(msg);
            throw new BusinessConfigException(msg);
        }
        bean.business = business;


        NodeList products = (NodeList) xpath.evaluate("/BusinessConfig/Products/Product/@code", doc, XPathConstants.NODESET);
        for (int i = 0; i < products.getLength(); i++) {
            String product = products.item(i).getTextContent();
            if (product.isEmpty()) {
                String msg = "find empty Product Code";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }
            if (!bean.products.add(product)) {
                String msg = "find duplicate Product Code:" + product;
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            }
        }

        NodeList orderExtensions = (NodeList) xpath.evaluate("/BusinessConfig/Order/Extension", doc, XPathConstants.NODESET);
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

        Optional<BusinessSpec> business = env.getBusinesses().stream().filter(e -> e.getCode().equals(bean.business)).findFirst();
        if (!business.isPresent()) {
            String msg = "can not find Business " + bean.business + " in environment";
            LOGGER.error(msg);
            throw new BusinessConfigException(msg);
        } else {
            config.businessSpec = business.get();
        }

        Map<String, ProductSpec> products = env.getProducts().stream().collect(Collectors.toMap(Spec::getCode, Function.identity()));
        bean.products.forEach(productCode -> {
            ProductSpec product = products.get(productCode);
            if (product == null) {
                String msg = "can not find Product " + productCode + " in environment";
                LOGGER.error(msg);
                throw new BusinessConfigException(msg);
            } else {
                config.productSpecs.add(product);
            }
        });

        // collect all effective extension and candidate facades from Business and Product
        Map<ExtensionSpec, Set<ExtensionFacadeSpec>> candidateExt2Facades = new HashMap<>();
        // initialize from business
        candidateExt2Facades.putAll(config.businessSpec.getExtensions());
        // supplement from products
        for (ProductSpec systemTemplate : config.productSpecs) {
            for (Map.Entry<ExtensionSpec, Set<ExtensionFacadeSpec>> e : systemTemplate.getExtensions().entrySet()) {
                ExtensionSpec extensionSpec = e.getKey();
                Set<ExtensionFacadeSpec> facadeSpecs = e.getValue();
                candidateExt2Facades.computeIfAbsent(extensionSpec, k -> new HashSet<>()).addAll(facadeSpecs);
            }
        }

        Map<String, Set<ExtensionFacadeSpec>> optionalExtCode2Facades = candidateExt2Facades.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().getCode(), Map.Entry::getValue
        ));

        Map<String, ExtensionSpec> candidateCode2Exts = candidateExt2Facades.keySet().stream().collect(Collectors.toMap(Spec::getCode, Function.identity()));
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
            ExtensionSpec extension = candidateCode2Exts.get(extensionCode);

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


        for (Map.Entry<ExtensionSpec, Set<ExtensionFacadeSpec>> e : candidateExt2Facades.entrySet()) {
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
