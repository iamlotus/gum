package jinlo.gum.core.testapps.app1.domain2;


import jinlo.gum.core.annotation.DomainService;

public class DomainService21 {


    @DomainService(domain = Domain2.class,name = "function1",desc = "desc")
    public void f1(){

    };

    @DomainService(domain = Domain2.class)
    public static void f2(){

    };

    // non-public is ignored
    @DomainService(domain = Domain2.class)
    void f3(){

    }
}
