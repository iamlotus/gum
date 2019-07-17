package jinlo.gum.core.spec;

import java.lang.reflect.Method;

public class DomainServiceSpec extends MethodSpec{

    //所属业务域
    private DomainSpec domain;

    public DomainServiceSpec(Method method) {
        super(method);
    }

    public DomainServiceSpec(Method method, String name) {
        super(method, name);
    }

    public DomainSpec getDomain() {
        return domain;
    }

    public void setDomain(DomainSpec domain) {
        this.domain = domain;
    }
}
