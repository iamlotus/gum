package jinlo.gum.core.runtime;


import jinlo.gum.core.annotation.*;
import jinlo.gum.core.exception.EnvironmentException;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.model.InstanceRecgonizer;
import jinlo.gum.core.spec.*;
import jinlo.gum.core.utils.AnnotationUtils;
import jinlo.gum.core.utils.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通过扫描ClassPath中的Annotation创建环境,这里统一采用当前线程的ClassLoader
 */
public class PlainEnvironmentBuilder implements EnvironmentBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainEnvironmentBuilder.class);

    static class PackageNameMatcher {

        private String[] packageNames;

        PackageNameMatcher(String[] packageNames) {
            if (packageNames == null || packageNames.length == 0) {
                throw new IllegalArgumentException("packageNames can not be null");
            }
            this.packageNames = packageNames;
        }

        public boolean match(String className) {
            Objects.requireNonNull(className);

            for (String pname : packageNames) {
                if (className.startsWith(pname + ".")) {
                    return true;
                }
            }

            return false;
        }
    }

    static class Annotations {


        public static final String DOMAIN = "@" + Domain.class.getSimpleName();

        public static final String DOMAIN_FUNCTION = "@" + DomainFunction.class.getSimpleName();

        public static final String DOMAIN_SERVICE = "@" + DomainService.class.getSimpleName();

        public static final String EXTENSION = "@" + Extension.class.getSimpleName();

        public static final String EXTENSION_FACADE = "@" + ExtensionFacade.class.getSimpleName();

        public static final String PRODUCT = "@" + Product.class.getSimpleName();

        public static final String BUSINESS = "@" + Business.class.getSimpleName();


        private Domain domain;

        private Map<Method, DomainFunction> domainFunctions;

        private Map<Method, DomainService> domainServices;

        private Extension extension;

        private ExtensionFacade extensionFacade;

        private Product product;

        private Business business;

        public Annotations(Class<?> targetClz) {
            domain = targetClz.getAnnotation(Domain.class);
            business = targetClz.getAnnotation(Business.class);
            extension = targetClz.getAnnotation(Extension.class);
            extensionFacade = targetClz.getAnnotation(ExtensionFacade.class);
            product = targetClz.getAnnotation(Product.class);

            domainFunctions = new HashMap<>();
            domainServices = new HashMap<>();

            // DomainFunction and DomainService must be public (Class.getMethods return public methods only)
            for (Method method : targetClz.getMethods()) {
                DomainFunction domainFunction = method.getAnnotation(DomainFunction.class);
                DomainService domainService = method.getAnnotation(DomainService.class);

                if (domainFunction != null) {
                    domainFunctions.put(method, domainFunction);
                }

                if (domainService != null) {
                    domainServices.put(method, domainService);
                }

            }
        }

        public Map<Method, DomainFunction> getDomainFunctions() {
            return domainFunctions;
        }

        public Map<Method, DomainService> getDomainServices() {
            return domainServices;
        }

        public Business getBusiness() {
            return business;
        }

        public Domain getDomain() {
            return domain;
        }

        public Extension getExtension() {
            return extension;
        }

        public ExtensionFacade getExtensionFacade() {
            return extensionFacade;
        }

        public Product getProduct() {
            return product;
        }

        public boolean hasAnnotation() {
            return (!domainFunctions.isEmpty() || !domainServices.isEmpty() || business != null || domain != null || extension != null || extensionFacade != null || product != null);
        }


    }

    static class EnvironmentImpl implements Environment {

        private Set<DomainSpec> domains = new HashSet<>();

        private Set<DomainFunctionSpec> domainFunctions = new HashSet<>();

        private Set<DomainServiceSpec> domainServices = new HashSet<>();

        private Set<ExtensionSpec> extensions = new HashSet<>();

        private Set<BusinessSpec> businesses = new HashSet<>();

        private Set<ProductSpec> products = new HashSet<>();

        @Override
        public Set<DomainSpec> getDomains() {
            return domains;
        }

        @Override
        public Set<DomainServiceSpec> getDomainServices() {
            return domainServices;
        }

        @Override
        public Set<DomainFunctionSpec> getDomainFunctions() {
            return domainFunctions;
        }

        @Override
        public Set<ExtensionSpec> getExtensions() {
            return extensions;
        }

        @Override
        public Set<BusinessSpec> getBusinesses() {
            return businesses;
        }

        @Override
        public Set<ProductSpec> getProducts() {
            return products;
        }

    }


    //待扫描的包名
    private String[] packageNames;

    private BeanRepository beanRepository;

    public PlainEnvironmentBuilder(String[] packageNames, BeanRepository beanRepository) {
        Objects.requireNonNull(packageNames);
        Objects.requireNonNull(beanRepository);
        this.packageNames = packageNames;
        this.beanRepository = beanRepository;
    }

    @Override
    public Environment build() {

        PackageNameMatcher matcher = new PackageNameMatcher(packageNames);

        LOGGER.info("start scan classes to create environment");

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ClassPath cp = new ClassPath(loader);

        //找出所有带有Annotation的实现类，后续处理
        Map<Class<?>, Annotations> clz2Annotations = new HashMap<>();

        for (ClassPath.Resource resource : cp.getResources()) {
            if (resource.isClass()) {
                String className = resource.getClassName();
                if (!matcher.match(className)) {
                    LOGGER.trace("ignore {} because the package name does not match requirement", className);
                    continue;
                }

                Class<?> clz;
                try {
                    clz = resource.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                Annotations annotations = new Annotations(clz);
                if (!annotations.hasAnnotation()) {
                    LOGGER.trace("ignore {} because there is no annotation", className);
                } else {
                    clz2Annotations.put(clz, annotations);
                }
            }
        }


        Environment environment = buildEnvironment(clz2Annotations);
        LOGGER.info("finish scan classes to create environment");
        return environment;
    }


    // Iterate all annotated classes, construct environment, maintains bi-relationship
    private Environment buildEnvironment(Map<Class<?>, Annotations> clz2Annotations) {

        Map<TemplateSpec, Set<Class<?>>> templateSpec2FacadeClz=new HashMap<>();
        Map<Class<?>, DomainSpec> clz2DomainSpec = new HashMap<>();
        Map<DomainFunctionSpec, Class<?>> domainFunctionSpec2Domains = new HashMap<>();
        Map<DomainServiceSpec, Class<?>> domainServiceSpec2Domains = new HashMap<>();
        Map<Class<?>, ExtensionSpec> clz2ExtensionSpec = new HashMap<>();
        Map<Class<?>, ExtensionFacadeSpec> clz2FacadeSpec = new HashMap<>();
        Map<Class<?>, TemplateSpec> clz2TemplateSpec = new HashMap<>();

        // First Round
        for (Map.Entry<Class<?>, Annotations> e : clz2Annotations.entrySet()) {
            Class<?> clz = e.getKey();
            Annotations annotations = e.getValue();

            // scan domain
            Domain domain = annotations.getDomain();
            if (domain != null) {
                DomainSpec domainSpec = new DomainSpec(clz.getName(), domain.name());
                domainSpec.setDescription(domain.desc());
                clz2DomainSpec.put(clz, domainSpec);
                LOGGER.debug("find {} on class \"{}\"", Annotations.DOMAIN, clz);
            }

            // scan domainFunction
            for (Map.Entry<Method, DomainFunction> d : annotations.getDomainFunctions().entrySet()) {
                Method method = d.getKey();
                DomainFunction function = d.getValue();

                DomainFunctionSpec domainFunctionSpec = new DomainFunctionSpec(method, function.name());
                domainFunctionSpec.setDescription(function.desc());
                if (domainFunctionSpec2Domains.put(domainFunctionSpec, function.domain()) != null) {
                    String msg = "find duplicated " + Annotations.DOMAIN_FUNCTION + ":" + method + ", override is not allowed";
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                }

                LOGGER.debug("find {} on method \"{}\"", Annotations.DOMAIN_FUNCTION, method);
            }

            // scan domainService
            for (Map.Entry<Method, DomainService> d : annotations.getDomainServices().entrySet()) {
                Method method = d.getKey();
                DomainService service = d.getValue();
                DomainServiceSpec domainServiceSpec = new DomainServiceSpec(method, service.name());
                domainServiceSpec.setDescription(service.desc());
                if (domainServiceSpec2Domains.put(domainServiceSpec, service.domain()) != null) {
                    String msg = "find duplicated " + Annotations.DOMAIN_SERVICE + ":" + method + ", override is not allowed";
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                }

                LOGGER.debug("find {} on method \"{}\"", Annotations.DOMAIN_SERVICE, method);
            }

            // scan extension
            Extension extension = annotations.getExtension();
            if (extension != null) {
                if (!AnnotationUtils.isValidExtension(clz)) {
                    String msg = clz + " is " + Annotations.EXTENSION + " but it is not FunctionalInterface";
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                }
                ExtensionSpec extensionSpec = new ExtensionSpec(clz.getName(), extension.name());
                extensionSpec.setDescription(extension.desc());
                clz2ExtensionSpec.put(clz, extensionSpec);
                LOGGER.debug("find {} on class \"{}\"", Annotations.EXTENSION, clz);
            }

            // scan template
            Business business = annotations.getBusiness();
            Product product = annotations.getProduct();
            if (business != null && product != null) {
                String msg = clz + " is " + Annotations.BUSINESS + " as well as " + Annotations.PRODUCT + ", which is not allowed";
                LOGGER.error(msg);
                throw new EnvironmentException(msg);
            } else if (business != null) {
                // scan business
                InstanceRecgonizer recgonizer = beanRepository.getBean(business.recgonizer().getName());
                BusinessSpec businessSpec = new BusinessSpec(clz.getName(), business.name(), recgonizer);
                BusinessCodeParser parser = beanRepository.getBean(business.parser().getName());
                businessSpec.setParser(parser);
                businessSpec.setDescription(business.desc());

                // store facade classes, they will be converted to Set<FacadeSpec> late
                List<Class<?>> facadeClzList= Stream.of(business.facades()).collect(Collectors.toList());
                Set<Class<?>> facadeClzSet= new HashSet<>(facadeClzList);
                if (facadeClzList.size()!=facadeClzList.size()){
                    List<Class<?>> duplicatedClz=facadeClzSet.stream()
                            .filter(f->facadeClzList.indexOf(f)!=facadeClzList.lastIndexOf(f))
                            .collect(Collectors.toList());

                    String msg = clz + " is " + Annotations.BUSINESS + ", find duplicated class "+ duplicatedClz+" in its facade property";
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                }
                templateSpec2FacadeClz.put(businessSpec,facadeClzSet);

                clz2TemplateSpec.put(clz, businessSpec);
                LOGGER.debug("find {} on class \"{}\"", Annotations.BUSINESS, clz);
            } else if (product != null) {
                // scan product
                InstanceRecgonizer recgonizer = beanRepository.getBean(product.recgonizer().getName());
                ProductSpec productSpec = new ProductSpec(clz.getName(), product.name(), recgonizer);
                productSpec.setDescription(product.desc());
                clz2TemplateSpec.put(clz, productSpec);

                // store facade classes, they will be converted to Set<FacadeSpec> late
                List<Class<?>> facadeClzList= Stream.of(product.facades()).collect(Collectors.toList());
                Set<Class<?>> facadeClzSet= new HashSet<>(facadeClzList);
                if (facadeClzList.size()!=facadeClzList.size()){
                    List<Class<?>> duplicatedClz=facadeClzSet.stream()
                            .filter(f->facadeClzList.indexOf(f)!=facadeClzList.lastIndexOf(f))
                            .collect(Collectors.toList());
                    String msg = clz + " is " + Annotations.PRODUCT + ", find duplicated class "+ duplicatedClz+" in its facade property";
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                }
                templateSpec2FacadeClz.put(productSpec,facadeClzSet);
                LOGGER.debug("find {} on class \"{}\"", Annotations.PRODUCT, clz);
            }
        }


        //second round, scan FacadeExtension, this can not be done since we have to scan Extension firs
        for (Map.Entry<Class<?>, Annotations> e : clz2Annotations.entrySet()) {
            Class<?> clz = e.getKey();
            Annotations annotations = e.getValue();
            ExtensionFacade extensionFacade = annotations.getExtensionFacade();
            Set<ExtensionSpec> implementedExtensionSpecsInFacade = new HashSet<>();

            if (extensionFacade != null) {

                ExtensionFacadeSpec extensionFacadeSpec = new ExtensionFacadeSpec(clz.getName(), extensionFacade.name());
                extensionFacadeSpec.setDescription(extensionFacade.desc());

                Object facadeInstance = beanRepository.getBean(clz.getName());

                // Iterate on all methods from ExtensionFacade, filter method that satisfy Extension
                for (Method method : clz.getMethods()) {
                    //only scan public & non-static & no-param method
                    if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 0) {
                        Class<?> returnType = method.getReturnType();
                        Set<ExtensionSpec> returnTypeImplementedExtensions = new HashSet<>();

                        // if returnType is interface, returnType.getInterfaces() will miss itself
                        List<Class<?>> targets = new ArrayList<>();
                        targets.add(returnType);
                        targets.addAll(Arrays.asList(returnType.getInterfaces()));

                        for (Class<?> itf : targets) {
                            if (clz2ExtensionSpec.containsKey(itf)) {
                                ExtensionSpec implementedExtensionSpec = clz2ExtensionSpec.get(itf);
                                if (!implementedExtensionSpecsInFacade.add(implementedExtensionSpec)) {
                                    String msg = clz + " is " + Annotations.EXTENSION_FACADE + ", and it satisfies " + Annotations.EXTENSION + " " + implementedExtensionSpec.getCode() + " more than once";
                                    LOGGER.error(msg);
                                    throw new EnvironmentException(msg);
                                }

                                returnTypeImplementedExtensions.add(implementedExtensionSpec);
                            }
                        }


                        if (!returnTypeImplementedExtensions.isEmpty()) {
                            Object returnInstance;
                            try {
                                returnInstance = method.invoke(facadeInstance);
                            } catch (Exception e1) {
                                //should not be here, actually
                                throw new EnvironmentException(e1);
                            }

                            if (returnInstance == null) {
                                String msg = method + " should return an implementation of  " + Annotations.EXTENSION + ", but it returns null.";
                                LOGGER.error(msg);
                                throw new EnvironmentException(msg);
                            }

                            ExtensionImplementationSpec extensionImplementationSpec = new ExtensionImplementationSpec(extensionFacadeSpec, method);
                            extensionImplementationSpec.setInstance(returnInstance);
                            extensionImplementationSpec.getExtensionSpecs().addAll(returnTypeImplementedExtensions);

                            returnTypeImplementedExtensions.forEach(extensionSpec ->
                                    extensionFacadeSpec.getExtension2Implementations().put(extensionSpec, extensionImplementationSpec));
                        }
                    }
                }


                if (implementedExtensionSpecsInFacade.isEmpty()) {
                    String msg = clz + " is " + Annotations.EXTENSION_FACADE + " but it does not implements any " + Annotations.EXTENSION;
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                }

                clz2FacadeSpec.put(clz, extensionFacadeSpec);
                LOGGER.debug("find {} on class \"{}\"", Annotations.EXTENSION_FACADE, clz);
            }

        }
        // reset domain.parentDomain
        for (Map.Entry<Class<?>, DomainSpec> e : clz2DomainSpec.entrySet()) {
            Class<?> clz = e.getKey();
            Domain domain = clz2Annotations.get(clz).getDomain();
            Class<?> parentClz = domain.parentDomainClass();
            if (parentClz != void.class) {
                DomainSpec parentSpec = clz2DomainSpec.get(parentClz);
                if (parentSpec == null) {
                    String msg = clz + " is " + Annotations.DOMAIN + ", but its parentDomainClass " + parentClz + "is not " + Annotations.DOMAIN;
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                } else {
                    DomainSpec spec = e.getValue();
                    spec.setParentDomain(parentSpec);
                }
            }

        }

        // reset domainFunctions.domain
        for (Map.Entry<DomainFunctionSpec, Class<?>> e : domainFunctionSpec2Domains.entrySet()) {
            DomainFunctionSpec domainFunctionSpec = e.getKey();
            Class<?> domainClass = e.getValue();

            DomainSpec domainSpec = clz2DomainSpec.get(domainClass);
            if (domainSpec == null) {
                String msg = domainFunctionSpec.getCode() + " is " + Annotations.DOMAIN_FUNCTION + ", but its domain " + domainClass + " is not " + Annotations.DOMAIN;
                LOGGER.error(msg);
                throw new EnvironmentException(msg);
            } else {
                domainFunctionSpec.setDomain(domainSpec);
            }
        }

        // reset domainServices.domain
        for (Map.Entry<DomainServiceSpec, Class<?>> e : domainServiceSpec2Domains.entrySet()) {
            DomainServiceSpec domainServiceSpec = e.getKey();
            Class<?> domainClass = e.getValue();

            DomainSpec domainSpec = clz2DomainSpec.get(domainClass);
            if (domainSpec == null) {
                String msg = domainServiceSpec.getCode() + " is " + Annotations.DOMAIN_SERVICE + ", but its domain " + domainClass + " is not " + Annotations.DOMAIN;
                LOGGER.error(msg);
                throw new EnvironmentException(msg);
            } else {
                domainServiceSpec.setDomain(domainSpec);
            }
        }


        for (Map.Entry<Class<?>, ExtensionFacadeSpec> e : clz2FacadeSpec.entrySet()) {
            Class<?> clz = e.getKey();
            ExtensionFacade extensionFacade = clz2Annotations.get(clz).getExtensionFacade();
            ExtensionFacadeSpec extensionFacadeSpec = e.getValue();

            // reset extension.implementation
            for (Map.Entry<ExtensionSpec, ExtensionImplementationSpec> me : extensionFacadeSpec.getExtension2Implementations().entrySet()) {
                ExtensionSpec extensionSpec = me.getKey();
                ExtensionImplementationSpec implementationSpec = me.getValue();
                extensionSpec.getImplementations().put(extensionFacadeSpec, implementationSpec);
            }
        }

        //construct Environment
        Environment env = new EnvironmentImpl();
        env.getDomains().addAll(clz2DomainSpec.values());
        env.getDomainFunctions().addAll(domainFunctionSpec2Domains.keySet());
        env.getDomainServices().addAll(domainServiceSpec2Domains.keySet());
        env.getExtensions().addAll(clz2ExtensionSpec.values());

        for (TemplateSpec templateSpec : clz2TemplateSpec.values()) {
            // reset template.
            for (Class<?> facadeClz:templateSpec2FacadeClz.get(templateSpec)){
                ExtensionFacadeSpec extensionFacadeSpec= clz2FacadeSpec.get(facadeClz);
                if (extensionFacadeSpec==null){
                    String msg = templateSpec.getCode() + " use "+ facadeClz + " as facade, but it it not " + Annotations.EXTENSION_FACADE;
                    LOGGER.error(msg);
                    throw new EnvironmentException(msg);
                }

                extensionFacadeSpec.getExtension2Implementations().forEach((extension,implementation)->{
                    templateSpec.getExtensions().computeIfAbsent(extension,k->new HashSet<>()).add(extensionFacadeSpec);
                });

            }

            if (templateSpec instanceof BusinessSpec) {
                BusinessSpec businessSpec = (BusinessSpec) templateSpec;
                env.getBusinesses().add(businessSpec);
            } else if (templateSpec instanceof ProductSpec) {
                ProductSpec productSpec = (ProductSpec) templateSpec;
                env.getProducts().add(productSpec);
            } else {
                throw new EnvironmentException("should never be here");
            }

        }


        return env;
    }


}
