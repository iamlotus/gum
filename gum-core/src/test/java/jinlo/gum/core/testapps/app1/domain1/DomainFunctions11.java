package jinlo.gum.core.testapps.app1.domain1;


import jinlo.gum.core.annotation.DomainFunction;


public class DomainFunctions11 {

    @DomainFunction(domain = Domain1.class,name = "f1",desc = "f1 desc")
    public void f1(){

    }
    @DomainFunction(domain = Domain1.class)
    public int f2(){
        return 0;
    }

    // static is allowed;
    @DomainFunction(domain = Domain1.class)
    public static int f3(){
        return 0;
    }

    // non public is skipped
    @DomainFunction(domain = Domain1.class)
    int f4(){
        return 0;
    }
}
