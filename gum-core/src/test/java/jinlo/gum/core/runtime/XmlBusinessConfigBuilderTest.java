package jinlo.gum.core.runtime;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jinlo.gum.core.exception.BusinessConfigException;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.spec.Spec;
import jinlo.gum.core.testapps.app1.Demo;
import jinlo.gum.core.testapps.app1.business1.B1Facade1;
import jinlo.gum.core.testapps.app1.business1.B1Facade2;
import jinlo.gum.core.testapps.app1.business1.Business1;
import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.domain1.Ext12;
import jinlo.gum.core.testapps.app1.system1.S1Facade1;
import jinlo.gum.core.testapps.app1.system1.System1;
import jinlo.gum.core.testapps.app1.system2.S2Facade1;
import jinlo.gum.core.testapps.app1.system2.S2Facade2;
import jinlo.gum.core.testapps.app1.system2.System2;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class XmlBusinessConfigBuilderTest {

    @Test
    public void testCreateBean() {
        InputStream is = this.getClass().getResourceAsStream("/app1_business1_config.xml");
        XmlBusinessConfigBuilder builder = new XmlBusinessConfigBuilder(is);
        assertEquals(Sets.newHashSet("sample.code1", "sample.code2"), builder.bean.getBusinessCodes());
        assertEquals(Business1.class.getName(), builder.bean.getBusinessTemplate());
        assertEquals(Sets.newHashSet(System1.class.getName(), System2.class.getName()), builder.bean.getSystemTemplates());
        Map<String, List<String>> orders = Maps.newHashMap();
        orders.put(Ext11.class.getName(), Lists.newArrayList(S1Facade1.class.getName(),
                S2Facade1.class.getName(),
                S2Facade2.class.getName(),
                B1Facade1.class.getName(),
                B1Facade2.class.getName()));
        assertEquals(orders, builder.bean.getOrders());
    }

    private BusinessConfig createConfig(String packageName, String configPath) {
        EnvironmentBuilder envBuilder = new PlainEnvironmentBuilder(new String[]{packageName}, new PlainBeanRepository());
        Environment env = envBuilder.build();
        InputStream is = this.getClass().getResourceAsStream(configPath);
        XmlBusinessConfigBuilder builder = new XmlBusinessConfigBuilder(is);
        BusinessConfig config = builder.build(env);
        return config;
    }

    @Test
    public void testBuild() {
        BusinessConfig config = createConfig(Demo.class.getPackage().getName(), "/app1_business1_config.xml");
        assertEquals(Sets.newHashSet(BusinessCode.of("sample.code1"), BusinessCode.of("sample.code2")), config.getBusinessCodes());
        assertEquals(Business1.class.getName(), config.getBusinessTemplate().getCode());
        assertEquals(Sets.newHashSet(System1.class.getName(), System2.class.getName()),
                config.getSystemTemplates().stream().map(Spec::getCode).collect(Collectors.toSet()));

        assertEquals(Sets.newHashSet(Ext11.class.getName(), Ext12.class.getName()), config.getEffectiveExtensionCodes());

        // ext11 显式指定了，以xml中的顺序为准
        List<String> ext11Impls = config.getImplementationsByCode(Ext11.class.getName()).stream().map(Spec::getCode).collect(Collectors.toList());

        assertEquals(Lists.newArrayList(S1Facade1.class.getName()+"#getExt1",
                S2Facade1.class.getName()+"#getExt1",
                S2Facade2.class.getName()+"#getExt1",
                B1Facade1.class.getName()+"#getExt1",
                B1Facade2.class.getName()+"#getFoo"), ext11Impls);

        // ext12 没有显式指定，只能用set判断
        Set<String> ext12Impls = config.getImplementationsByCode(Ext12.class.getName()).stream().map(Spec::getCode).collect(Collectors.toSet());
        assertEquals(Sets.newHashSet(B1Facade1.class.getName()+"#getExt2", S1Facade1.class.getName()+"#getExt112"), ext12Impls);
    }

    @Test(expected = BusinessConfigException.class)
    public void testBuildFromWrongConfig1() {
        createConfig(Demo.class.getPackage().getName(), "/wrong_config/duplicated_business_code");
    }

    @Test(expected = BusinessConfigException.class)
    public void testBuildFromWrongConfig2() {
        createConfig(Demo.class.getPackage().getName(), "/wrong_config/no_business_code.xml");
    }

    @Test(expected = BusinessConfigException.class)
    public void testBuildFromWrongConfig3() {
        createConfig(Demo.class.getPackage().getName(), "/wrong_config/partial_specified_implementation.xml");
    }
}

