package jinlo.gum.core.runtime;

import com.google.common.collect.Sets;
import jinlo.gum.core.annotation.Business;
import jinlo.gum.core.exception.BusinessProcessException;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.model.InstanceRecgonizer;
import jinlo.gum.core.spec.BusinessSpec;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RuntimeTest {
    class Entity1 {
        public String code;
    }


    class Entity2 {
        public String code;
    }

    static class EntityParser implements BusinessCodeParser {

        private static final BusinessCode BUSINESS1 = BusinessCode.of("BUSINESS1");

        private static final BusinessCode BUSINESS2 = BusinessCode.of("BUSINESS2");

        private static final Set<BusinessCode> RANGE;

        static{
            Set<BusinessCode> range=new HashSet<>();
            range.add(BUSINESS1);
            range.add(BUSINESS2);
            RANGE= Collections.unmodifiableSet(range);
        }

        @Override
        public boolean knows(Object instance) {
            return instance instanceof Entity1 || instance instanceof Entity2;
        }

        @Override
        public BusinessCode parse(Object instance) throws IllegalArgumentException {
            if (instance instanceof Entity1) {
                return BusinessCode.of(((Entity1) instance).code);
            } else {
                return BusinessCode.of(((Entity2) instance).code);
            }
        }

        @Override
        public Set<BusinessCode> range() {
            return RANGE;
        }
    }

    private BusinessSpec business1, business2;

    private BusinessConfig config1, config2;

    private Plugin plugin;

    private void mock() {
        business1 = new BusinessSpec("Business1", new InstanceRecgonizer.PositiveRecgonizer());
        config1 = createMock(BusinessConfig.class);
        expect(config1.getBusiness()).andReturn(business1).anyTimes();
        expect(config1.knows(BusinessCode.of("Business1"))).andReturn(true).anyTimes();
        expect(config1.knows(BusinessCode.of("Business2"))).andReturn(false).anyTimes();

        business2 = new BusinessSpec("Business2", new InstanceRecgonizer.PositiveRecgonizer());
        config2 = createMock(BusinessConfig.class);
        expect(config2.getBusiness()).andReturn(business2).anyTimes();
        expect(config2.knows(BusinessCode.of("Business1"))).andReturn(false).anyTimes();
        expect(config2.knows(BusinessCode.of("Business2"))).andReturn(true).anyTimes();

        replay(config1, config2);

        plugin = createMock(Plugin.class);
        expect(plugin.getBusinessCodeParser()).andReturn(new EntityParser()).anyTimes();
        replay(plugin);
    }


    @Test
    public void testFindBusinessConfigByInstance() {

        mock();
        Runtime runtime = new Runtime(Sets.newHashSet(plugin), Sets.newHashSet(config1, config2));

        Entity1 instance = new Entity1();
        instance.code = "Business1";
        Runtime.Target target = runtime.findBusinessConfigByInstance(instance);

        assertEquals("Business1", target.getCode().code());
        assertEquals(config1, target.getConfig());
    }

    @Test
    public void testCreateProcess() {
        mock();
        Runtime runtime = new Runtime(Sets.newHashSet(plugin), Sets.newHashSet(config1, config2));


        Entity1 instance1 = new Entity1();
        instance1.code = "Business1";

        Entity2 instance2 = new Entity2();
        instance2.code = "Business2";
        // precompiled process, instance2 is not acceptable
        BusinessProcess process1 = runtime.createPrecompiledProcess(instance1);
        assertEquals("Business1", process1.getCode(instance1).code());

        try {
            process1.getCode(instance2);
            fail("should throw exception ");
        } catch (BusinessProcessException e) {
            //ok to be here
        }


        // concurrent
        BusinessProcess process2 = runtime.createConcurrentProcess();
        assertEquals("Business1", process2.getCode(instance1).code());
        assertEquals("Business2", process2.getCode(instance2).code());

        try {
            process2.getCode("unkown object");
            fail("should throw exception ");
        } catch (BusinessProcessException e) {
            //ok to be here
        }

        // normal
        BusinessProcess process3 = runtime.createProcess();
        assertEquals("Business1", process3.getCode(instance1).code());
        assertEquals("Business2", process3.getCode(instance2).code());
        try {
            process3.getCode("unkown object");
            fail("should throw exception ");
        } catch (BusinessProcessException e) {
            //ok to be here
        }
    }

}