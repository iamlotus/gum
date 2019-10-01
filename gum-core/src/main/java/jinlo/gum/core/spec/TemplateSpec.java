package jinlo.gum.core.spec;

import jinlo.gum.core.model.InstanceRecgonizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class TemplateSpec extends Spec{

    // is the template effective?
    private InstanceRecgonizer recgonizer;

    // 此模板包含的扩展点及其实现
    private Map<ExtensionSpec,Set<ExtensionFacadeSpec>> extensions=new HashMap<>();

    public TemplateSpec(String code, InstanceRecgonizer recgonizer) {
        super(code);
        this.recgonizer = recgonizer;
    }

    public TemplateSpec(String code, String name, InstanceRecgonizer recgonizer) {
        super(code, name);
        this.recgonizer = recgonizer;
    }

    public Map<ExtensionSpec, Set<ExtensionFacadeSpec>> getExtensions() {
        return extensions;
    }

    public InstanceRecgonizer getRecgonizer() {
        return recgonizer;
    }
}
