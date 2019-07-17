package jinlo.gum.core.testapps.app1.domain1;


import jinlo.gum.core.annotation.DomainFunction;

public interface DomainFunctions12 {
    @DomainFunction(domain = Domain1.class)
    void f1();
}
