package jinlo.gum.core.spec;


import java.util.HashMap;
import java.util.Map;

public class ExtensionSpec extends Spec{

    //每个扩展点上只会有一个实现
    private Map<ExtensionFacadeSpec, ExtensionImplementationSpec> implementations=new HashMap<>();

    public ExtensionSpec(String code) {
        super(code);
    }

    public ExtensionSpec(String code, String name) {
        super(code, name);
    }

    public Map<ExtensionFacadeSpec, ExtensionImplementationSpec> getImplementations() {
        return implementations;
    }
}
