package jinlo.gum.core.spec;

import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.model.TemplateChecker;


public class BusinessTemplateSpec extends TemplateSpec {

    private BusinessCodeParser parser;

    public BusinessTemplateSpec(String code, TemplateChecker checker) {
        super(code, checker);
    }

    public BusinessTemplateSpec(String code, String name, TemplateChecker checker) {
        super(code, name, checker);
    }

    public BusinessCodeParser getParser() {
        return parser;
    }

    public void setParser(BusinessCodeParser parser) {
        this.parser = parser;
    }
}
