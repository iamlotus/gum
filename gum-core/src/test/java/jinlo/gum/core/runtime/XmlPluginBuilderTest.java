package jinlo.gum.core.runtime;

import com.google.common.collect.Sets;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.testapps.app1.business1.Business1CodeParser;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class XmlPluginBuilderTest {

    @Test
    public void testCreateBean() {
        InputStream is = this.getClass().getResourceAsStream("/app1_plugin.xml");
        XmlPluginBuilder builder = new XmlPluginBuilder(is);
        assertEquals("app1 plugin name", builder.bean.getName());
        assertEquals("app1 plugin description", builder.bean.getDescription());
        assertEquals(Business1CodeParser.class.getName(), builder.bean.getBusinessCodeParser());
    }

    private Plugin createPlugin(String path) {
        InputStream is = this.getClass().getResourceAsStream(path);
        XmlPluginBuilder builder = new XmlPluginBuilder(is);
        return builder.build(new PlainBeanRepository());

    }

    @Test
    public void testBuild() {
        Plugin plugin=createPlugin("/app1_plugin.xml");
        assertEquals(Sets.newHashSet(BusinessCode.of("sample.code1"),BusinessCode.of("sample.code2")), plugin.range());
        assertEquals("app1 plugin name", plugin.getName());
        assertEquals("app1 plugin description", plugin.getDescription());
        assertEquals(Business1CodeParser.class, plugin.getBusinessCodeParser().getClass());

    }


}

