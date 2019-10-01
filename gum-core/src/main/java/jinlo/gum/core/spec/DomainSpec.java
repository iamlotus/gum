package jinlo.gum.core.spec;

public class DomainSpec extends Spec {

    private DomainSpec parentDomain;

    public DomainSpec(String code) {
        super(code);
    }

    public DomainSpec(String code, String name) {
        super(code, name);
    }

    public DomainSpec getParentDomain() {
        return parentDomain;
    }

    public void setParentDomain(DomainSpec parentDomain) {
        this.parentDomain = parentDomain;
    }
}
