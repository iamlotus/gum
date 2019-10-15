package jinlo.gum.core.runtime;

import com.google.common.collect.Sets;
import jinlo.gum.core.testapps.app3.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BusinessProcessesTest {

    @Test
    public void executeExtension() {
        //业务扩展点先执行
        int result;
        result = createProcessFromDemo3("/app3_plugin.xml","/app3_business_first_config.xml").execute(() -> {
            Function f = new Function();
            return f.getNumber();
        });
        assertEquals(101, result);

        //系统扩展点先执行
        result = createProcessFromDemo3("/app3_plugin.xml","/app3_system_first_config.xml").execute(() -> {
            Function f = new Function();
            return f.getNumber();
        });
        assertEquals(201, result);
    }

    @Test
    public void executeExtensionFirstOf() {
        //FirstOf 会把业务扩展点过滤掉
        int result;
        result = createProcessFromDemo3("/app3_plugin.xml","/app3_business_first_config.xml").execute(() -> {
            Function f = new Function();
            return f.getNumberGreaterThan150();
        });
        assertEquals(200, result);

        //FirstOf 会把业务扩展点过滤掉
        result = createProcessFromDemo3("/app3_plugin.xml","/app3_system_first_config.xml").execute(() -> {
            Function f = new Function();
            return f.getNumberGreaterThan150();
        });
        assertEquals(200, result);

        //FirstOf 会把所有扩展点过滤掉
        Integer i = createProcessFromDemo3("/app3_plugin.xml","/app3_system_first_config.xml").execute(() -> {
            Function f = new Function();
            return f.getNumberGreaterThan300();
        });
        assertNull(i);
    }

    @Test
    public void executeExtensionWithDetail() {
        //返回Detail
        ExtensionExecuteDetail<Integer,Integer> details;
        details = createProcessFromDemo3("/app3_plugin.xml","/app3_business_first_config.xml").execute(() -> {
            Function f = new Function();
            return f.getNumberGreaterThan150WithDetail();
        });
        assertEquals(Ext1.class.getName(), details.getExtensionCode());
        assertEquals(200, details.getResult().intValue());
        assertEquals(2, details.getDetails().size());

        ExtensionExecuteDetail.FacadeDetail<Integer> detail1=details.getDetails().get(0);
        assertEquals(BFacade.class.getName(), detail1.getFacadeCode());
        assertEquals(100, detail1.getResult().intValue());

        ExtensionExecuteDetail.FacadeDetail<Integer> detail2=details.getDetails().get(1);
        assertEquals(PFacade.class.getName(), detail2.getFacadeCode());
        assertEquals(200, detail2.getResult().intValue());

    }

    private BusinessProcess createProcessFromDemo3(String pluginName,String configName) {
        String[] packageName = new String[]{Demo3.class.getPackage().getName()};
        EnvironmentBuilder envBuilder = new PlainEnvironmentBuilder(packageName, new PlainBeanRepository());
        Environment env = envBuilder.build();

        XmlBusinessConfigBuilder configBuilder = new XmlBusinessConfigBuilder(this.getClass().getResourceAsStream(configName));
        BusinessConfig config = configBuilder.build(env);

        XmlPluginBuilder pluginBuilder = new XmlPluginBuilder(this.getClass().getResourceAsStream(pluginName));
        Plugin plugin = pluginBuilder.build(new PlainBeanRepository());

        Runtime runtime = new Runtime(Sets.newHashSet(plugin), Sets.newHashSet(config));
        BusinessProcess process = runtime.createProcess();
        return process;
    }
}