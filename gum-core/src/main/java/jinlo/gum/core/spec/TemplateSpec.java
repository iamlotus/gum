package jinlo.gum.core.spec;

import jinlo.gum.core.model.TemplateChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class TemplateSpec extends Spec{

    private TemplateChecker checker;

    //此模板包含的扩展点及其实现
    private Map<ExtensionSpec,Set<ExtensionFacadeSpec>> extensions=new HashMap<>();

    public TemplateSpec(String code, TemplateChecker checker) {
        super(code);
        this.checker=checker;
    }

    public TemplateSpec(String code, String name, TemplateChecker checker) {
        super(code, name);
        this.checker=checker;
    }

    public Map<ExtensionSpec, Set<ExtensionFacadeSpec>> getExtensions() {
        return extensions;
    }

    public TemplateChecker getChecker() {
        return checker;
    }
}
