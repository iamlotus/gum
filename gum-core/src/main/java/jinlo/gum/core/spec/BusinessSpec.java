package jinlo.gum.core.spec;

import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.model.InstanceRecgonizer;


public class BusinessSpec extends TemplateSpec {

    private BusinessCodeParser parser;

    public BusinessSpec(String code, InstanceRecgonizer recgonizer) {
        super(code, recgonizer);
    }

    public BusinessSpec(String code, String name, InstanceRecgonizer recgonizer) {
        super(code, name, recgonizer);
    }

    public BusinessCodeParser getParser() {
        return parser;
    }

    public void setParser(BusinessCodeParser parser) {
        this.parser = parser;
    }
}
