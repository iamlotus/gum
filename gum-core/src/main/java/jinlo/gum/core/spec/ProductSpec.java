package jinlo.gum.core.spec;

import jinlo.gum.core.model.InstanceRecgonizer;

public class ProductSpec extends TemplateSpec {
    public ProductSpec(String code, InstanceRecgonizer recgonizer) {
        super(code, recgonizer);
    }

    public ProductSpec(String code, String name, InstanceRecgonizer recgonizer) {
        super(code, name, recgonizer);
    }
}
