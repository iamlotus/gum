package jinlo.gum.demo.quickstart;

import jinlo.gum.core.runtime.Runtime;
import jinlo.gum.core.runtime.*;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomainManager;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;

import java.util.HashSet;
import java.util.Set;

public class SampleRun {

    public static void main(String[] args) {
        Runtime runtime = initRuntime();

        runtime.createProcess().execute(() -> {
            DeliveryDomainManager.getInstance().doSomeCustomDeliveryCalc(new DeliveryOrderLine(1));
            DeliveryDomainManager.getInstance().doSomeCustomDeliveryCalc(new DeliveryOrderLine(2));
            DeliveryDomainManager.getInstance().doSomeCustomDeliveryCalc(new DeliveryOrderLine(3));
            return null;
        });
    }

    private static Runtime initRuntime() {

        String packageName = SampleRun.class.getPackage().getName();

        BeanRepository beanRepository=new PlainBeanRepository();

        EnvironmentBuilder envBuilder = new PlainEnvironmentBuilder(new String[]{packageName}, beanRepository);
        Environment env = envBuilder.build();

        BusinessConfig config1 = new XmlBusinessConfigBuilder(SampleRun.class.getResourceAsStream("/config/app1.normal.config.xml")).build(env);
        BusinessConfig config2 = new XmlBusinessConfigBuilder(SampleRun.class.getResourceAsStream("/config/app1.virtual.config.xml")).build(env);
        BusinessConfig config3 = new XmlBusinessConfigBuilder(SampleRun.class.getResourceAsStream("/config/app1.nothing.config.xml")).build(env);
        Set<BusinessConfig> configs = new HashSet<>();
        configs.add(config1);
        configs.add(config2);
        configs.add(config3);

        Plugin plugin=new XmlPluginBuilder(SampleRun.class.getResourceAsStream("/config/app1.plugin.xml")).build(beanRepository);
        Set<Plugin> plugins=new HashSet<>();
        plugins.add(plugin);

        Runtime runtime = new Runtime(plugins,configs);

        return runtime;
    }
}
