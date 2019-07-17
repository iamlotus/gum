package jinlo.gum.core.runtime;

import com.google.common.collect.Sets;
import jinlo.gum.core.model.TemplateChecker;
import jinlo.gum.core.spec.*;
import jinlo.gum.core.testapps.app1.Demo;
import jinlo.gum.core.testapps.app1.business1.B1Facade1;
import jinlo.gum.core.testapps.app1.business1.B1Facade2;
import jinlo.gum.core.testapps.app1.business1.Business1;
import jinlo.gum.core.testapps.app1.business1.Business1CodeParser;
import jinlo.gum.core.testapps.app1.business2.Business2;
import jinlo.gum.core.testapps.app1.business2.Business2CodeParser;
import jinlo.gum.core.testapps.app1.domain1.*;
import jinlo.gum.core.testapps.app1.domain2.*;
import jinlo.gum.core.testapps.app1.system1.S1Facade1;
import jinlo.gum.core.testapps.app1.system1.System1;
import jinlo.gum.core.testapps.app1.system2.S2Facade1;
import jinlo.gum.core.testapps.app1.system2.S2Facade2;
import jinlo.gum.core.testapps.app1.system2.System2;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlainEnvironmentBuilderTest {

    @Test
    public void createEnvironment() {
        // scan app1
        String[] packageName = new String[]{Demo.class.getPackage().getName()};
        EnvironmentBuilder creator = new PlainEnvironmentBuilder(packageName, new PlainBeanRepository());
        Environment env = creator.build();

        // domains
        List<DomainSpec> domains = env.getDomains().stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(2, domains.size());

        DomainSpec domain1 = domains.get(0);
        assertEquals(Domain1.class.getName(), domain1.getCode());
        assertEquals("No1", domain1.getName());
        assertEquals("Domain1描述", domain1.getDescription());
        assertEquals(null, domain1.getParentDomain());

        DomainSpec domain2 = domains.get(1);
        assertEquals(Domain2.class.getName(), domain2.getCode());
        assertEquals(domain2.getCode(), domain2.getName());
        assertEquals("", domain2.getDescription());
        assertEquals(domain1, domain2.getParentDomain());

        // domain functions
        List<DomainFunctionSpec> functions = env.getDomainFunctions().stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(5, functions.size());
        DomainFunctionSpec function1 = functions.get(0);
        assertEquals(DomainFunctions11.class.getName() + "#f1", function1.getCode());
        assertEquals("f1", function1.getName());
        assertEquals("f1 desc", function1.getDescription());
        assertEquals(domain1, function1.getDomain());

        DomainFunctionSpec function2 = functions.get(1);
        assertEquals(DomainFunctions11.class.getName() + "#f2", function2.getCode());
        assertEquals(function2.getCode(), function2.getName());
        assertEquals("", function2.getDescription());
        assertEquals(domain1, function2.getDomain());

        DomainFunctionSpec function3 = functions.get(2);
        assertEquals(DomainFunctions11.class.getName() + "#f3", function3.getCode());
        assertEquals(function3.getCode(), function3.getName());
        assertEquals("", function3.getDescription());
        assertEquals(domain1, function3.getDomain());


        DomainFunctionSpec function4 = functions.get(3);
        assertEquals(DomainFunctions12.class.getName() + "#f1", function4.getCode());
        assertEquals(function4.getCode(), function4.getName());
        assertEquals("", function4.getDescription());
        assertEquals(domain1, function4.getDomain());

        DomainFunctionSpec function5 = functions.get(4);
        assertEquals(DomainFunctions21.class.getName() + "#f1", function5.getCode());
        assertEquals(function5.getCode(), function5.getName());
        assertEquals("", function5.getDescription());
        assertEquals(domain2, function5.getDomain());


        // domain services
        List<DomainServiceSpec> services = env.getDomainServices().stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(2, services.size());
        DomainServiceSpec service1 = services.get(0);
        assertEquals(DomainService21.class.getName() + "#f1", service1.getCode());
        assertEquals("function1", service1.getName());
        assertEquals("desc", service1.getDescription());
        assertEquals(domain2, service1.getDomain());

        DomainServiceSpec service2 = services.get(1);
        assertEquals(DomainService21.class.getName() + "#f2", service2.getCode());
        assertEquals(service2.getCode(), service2.getName());
        assertEquals("", service2.getDescription());
        assertEquals(domain2, service2.getDomain());


        // business
        List<BusinessTemplateSpec> businesses = env.getBusinessTemplates().stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(2, businesses.size());
        BusinessTemplateSpec business1 = businesses.get(0);
        assertEquals(Business1.class.getName(), business1.getCode());
        assertEquals("业务1", business1.getName());
        assertEquals("desc1", business1.getDescription());
        assertEquals(Business1CodeParser.class, business1.getParser().getClass());
        assertEquals(TemplateChecker.AlwaysKnowChecker.class, business1.getChecker().getClass());

        BusinessTemplateSpec business2 = businesses.get(1);
        assertEquals(Business2.class.getName(), business2.getCode());
        assertEquals(business2.getCode(), business2.getName());
        assertEquals("", business2.getDescription());
        assertEquals(Business2CodeParser.class, business2.getParser().getClass());
        assertEquals(Business2.FalseChecker.class, business2.getChecker().getClass());

        // business extensions
        List<Map.Entry<ExtensionSpec, Set<ExtensionFacadeSpec>>> business1Exts = business1.getExtensions().entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().getCode())).collect(Collectors.toList());
        assertEquals(2, business1Exts.size());

        ExtensionSpec ext11 = business1Exts.get(0).getKey();
        assertEquals(Ext11.class.getName(), ext11.getCode());
        assertEquals("Ext11", ext11.getName());
        assertEquals("desc11", ext11.getDescription());

        ExtensionSpec ext12 = business1Exts.get(1).getKey();
        assertEquals(Ext12.class.getName(), ext12.getCode());
        assertEquals(ext12.getCode(), ext12.getName());
        assertEquals("", ext12.getDescription());

        List<ExtensionFacadeSpec> extensionSpec1Facades = business1Exts.get(0).getValue().stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(2, extensionSpec1Facades.size());

        ExtensionFacadeSpec b1Facade1 = extensionSpec1Facades.get(0);
        assertEquals(B1Facade1.class.getName(), b1Facade1.getCode());
        assertEquals("B1Facade1", b1Facade1.getName());
        assertEquals("B1Facade1 desc", b1Facade1.getDescription());
        assertEquals(Sets.newHashSet(business1), b1Facade1.getBelongsTo());
        List<Map.Entry<ExtensionSpec, ExtensionImplementationSpec>> b1Facade1Ext2Impls = b1Facade1.getExtension2Implementations().entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().getCode())).collect(Collectors.toList());
        assertEquals(2, b1Facade1Ext2Impls.size());
        assertEquals(ext11, b1Facade1Ext2Impls.get(0).getKey());
        ExtensionImplementationSpec b1Facade1Ext1 = b1Facade1Ext2Impls.get(0).getValue();
        assertEquals(B1Facade1.class.getName() + "#getExt1", b1Facade1Ext1.getCode());
        assertTrue(b1Facade1Ext1.getInstance() instanceof Ext11);
        assertEquals(Sets.newHashSet(ext11), b1Facade1Ext1.getExtensionSpecs());
        assertEquals(b1Facade1, b1Facade1Ext1.getFacade());
        assertEquals(ext12, b1Facade1Ext2Impls.get(1).getKey());

        ExtensionImplementationSpec b1Facade1Ext2 = b1Facade1Ext2Impls.get(1).getValue();
        assertEquals(B1Facade1.class.getName() + "#getExt2", b1Facade1Ext2.getCode());
        assertTrue(b1Facade1Ext2.getInstance() instanceof Ext12);
        assertEquals(b1Facade1, b1Facade1Ext2.getFacade());
        assertEquals(Sets.newHashSet(ext12), b1Facade1Ext2.getExtensionSpecs());

        ExtensionFacadeSpec b1Facade2 = extensionSpec1Facades.get(1);
        assertEquals(B1Facade2.class.getName(), b1Facade2.getCode());
        assertEquals(b1Facade2.getCode(), b1Facade2.getName());
        assertEquals("", b1Facade2.getDescription());
        assertEquals(Sets.newHashSet(business1), b1Facade2.getBelongsTo());
        List<Map.Entry<ExtensionSpec, ExtensionImplementationSpec>> b1Facade2Ext2Impls = b1Facade2.getExtension2Implementations().entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().getCode())).collect(Collectors.toList());
        assertEquals(1, b1Facade2Ext2Impls.size());
        assertEquals(ext11, b1Facade2Ext2Impls.get(0).getKey());
        ExtensionImplementationSpec b1Facade2Ext1 = b1Facade2Ext2Impls.get(0).getValue();
        assertEquals(B1Facade2.class.getName() + "#getFoo", b1Facade2Ext1.getCode());
        assertTrue(b1Facade2Ext1.getInstance() instanceof Ext11);
        assertEquals(Sets.newHashSet(ext11), b1Facade2Ext1.getExtensionSpecs());

        List<Map.Entry<ExtensionSpec, Set<ExtensionFacadeSpec>>> business2Exts = business2.getExtensions().entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().getCode())).collect(Collectors.toList());
        assertEquals(0, business2Exts.size());

        // system
        List<SystemTemplateSpec> systems = env.getSystemTemplates().stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(2, systems.size());

        SystemTemplateSpec system1 = systems.get(0);
        assertEquals(System1.class.getName(), system1.getCode());
        assertEquals("系统1", system1.getName());
        assertEquals("system1 desc", system1.getDescription());
        assertEquals(2, system1.getExtensions().size());
        List<ExtensionFacadeSpec> system1Facades = system1.getExtensions().get(ext11).stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(3, system1Facades.size());
        ExtensionFacadeSpec s1Facade1 = system1Facades.get(0);
        assertEquals(S1Facade1.class.getName(), s1Facade1.getCode());
        assertEquals(s1Facade1.getCode(), s1Facade1.getName());
        assertEquals("", s1Facade1.getDescription());
        ExtensionFacadeSpec s2Facade1 = system1Facades.get(1);
        assertEquals(S2Facade1.class.getName(), s2Facade1.getCode());
        assertEquals(s2Facade1.getCode(), s2Facade1.getName());
        assertEquals("", s2Facade1.getDescription());
        ExtensionFacadeSpec s2Facade2 = system1Facades.get(2);
        assertEquals(S2Facade2.class.getName(), s2Facade2.getCode());
        assertEquals(s2Facade2.getCode(), s2Facade2.getName());
        assertEquals("", s2Facade2.getDescription());
        assertEquals(Sets.newHashSet(s1Facade1), system1.getExtensions().get(ext12));

        SystemTemplateSpec system2 = systems.get(1);
        assertEquals(System2.class.getName(), system2.getCode());
        assertEquals(system2.getCode(), system2.getName());
        assertEquals("", system2.getDescription());
        assertEquals(1, system2.getExtensions().size());
        List<ExtensionFacadeSpec> system2Ext11Impls = system2.getExtensions().get(ext11).stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(1, system2Ext11Impls.size());

        assertEquals(Sets.newHashSet(system1), s1Facade1.getBelongsTo());
        assertEquals(Sets.newHashSet(system1, system2), s2Facade1.getBelongsTo());
        assertEquals(Sets.newHashSet(system1), s2Facade2.getBelongsTo());

        // extensions
        List<ExtensionSpec> extensions = env.getExtensions().stream().sorted(Comparator.comparing(Spec::getCode)).collect(Collectors.toList());
        assertEquals(5, extensions.size());
        ExtensionSpec e11 = extensions.get(0);
        assertEquals(Ext11.class.getName(), e11.getCode());
        assertEquals(5, e11.getImplementations().size());

        ExtensionSpec e12 = extensions.get(1);
        assertEquals(Ext12.class.getName(), e12.getCode());
        assertEquals(2, e12.getImplementations().size());

        ExtensionSpec e13 = extensions.get(2);
        assertEquals(Ext13.class.getName(), e13.getCode());
        assertEquals(0, e13.getImplementations().size());

        ExtensionSpec e21 = extensions.get(3);
        assertEquals(Ext21.class.getName(), e21.getCode());
        assertEquals(0, e21.getImplementations().size());

        ExtensionSpec e22 = extensions.get(4);
        assertEquals(Ext22.class.getName(), e22.getCode());
        assertEquals(0, e22.getImplementations().size());
    }

    @Test
    public void createEnvironment2() {
        // empty
        String[] packageName = new String[]{jinlo.gum.core.testapps.app2.Demo.class.getPackage().getName()};
        EnvironmentBuilder creator = new PlainEnvironmentBuilder(packageName, new PlainBeanRepository());
        Environment env = creator.build();
        assertTrue(env.getBusinessTemplates().isEmpty());
        assertTrue(env.getDomains().isEmpty());
        assertTrue(env.getDomainServices().isEmpty());
        assertTrue(env.getExtensions().isEmpty());
        assertTrue(env.getSystemTemplates().isEmpty());

    }
}