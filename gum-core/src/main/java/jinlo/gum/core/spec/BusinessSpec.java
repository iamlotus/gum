package jinlo.gum.core.spec;

import jinlo.gum.core.model.InstanceRecgonizer;


public class BusinessSpec extends TemplateSpec {
    public BusinessSpec(String code, InstanceRecgonizer recgonizer) {
        super(code, recgonizer);
    }

    public BusinessSpec(String code, String name, InstanceRecgonizer recgonizer) {
        super(code, name, recgonizer);
    }
}
