package jinlo.gum.core.spec;

import java.lang.reflect.Method;

public class DomainFunctionSpec extends MethodSpec{

    //所属业务域
    private DomainSpec domain;


    public DomainFunctionSpec(Method method) {
        super(method);
    }

    public DomainFunctionSpec(Method method, String name) {
        super(method, name);
    }


    public DomainSpec getDomain() {
        return domain;
    }

    public void setDomain(DomainSpec domain) {
        this.domain = domain;
    }
}
