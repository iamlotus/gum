package jinlo.gum.core.spec;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExtensionFacadeSpec extends Spec {

    // 此门面实现了哪些扩展点以及扩展点的具体实现，在一个门面中一个扩展点只能有一个实现。这个实现在运行时可以动态转换为扩展点对应的接口
    private Map<ExtensionSpec, ExtensionImplementationSpec> extension2Implementations = new HashMap<>();

    // 所属模板，每个门面可以属于若干模板
    private Set<TemplateSpec> belongsTo = new HashSet<>();

    public ExtensionFacadeSpec(String code) {
        super(code);
    }

    public ExtensionFacadeSpec(String code, String name) {
        super(code, name);
    }

    public Map<ExtensionSpec, ExtensionImplementationSpec> getExtension2Implementations() {
        return extension2Implementations;
    }

    public Set<TemplateSpec> getBelongsTo() {
        return belongsTo;
    }

}
