package jinlo.gum.core.spec;

import jinlo.gum.core.model.TemplateChecker;

public class SystemTemplateSpec extends TemplateSpec {
    public SystemTemplateSpec(String code, TemplateChecker checker) {
        super(code, checker);
    }

    public SystemTemplateSpec(String code, String name, TemplateChecker checker) {
        super(code, name, checker);
    }
}
