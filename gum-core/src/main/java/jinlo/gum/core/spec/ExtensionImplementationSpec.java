package jinlo.gum.core.spec;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ExtensionImplementationSpec extends MethodSpec {

    //ExtensionSpecs to be implemented
    private Set<ExtensionSpec> extensionSpecs = new HashSet<>();

    //instance, which (is returned from ExtensionFacade) can be casted to the types extensionSpecs refer to
    private Object instance;

    // which facade this implementation belongs to
    private ExtensionFacadeSpec facade;

    public ExtensionImplementationSpec(ExtensionFacadeSpec facade,Method method) {
        super(method);
        this.facade=facade;
    }

    public ExtensionImplementationSpec(ExtensionFacadeSpec facade,Method method, String name) {
        super(method, name);
        this.facade=facade;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Set<ExtensionSpec> getExtensionSpecs() {
        return extensionSpecs;
    }

    public ExtensionFacadeSpec getFacade() {
        return facade;
    }
}
