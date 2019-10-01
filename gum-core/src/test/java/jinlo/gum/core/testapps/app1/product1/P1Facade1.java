package jinlo.gum.core.testapps.app1.product1;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.domain1.Ext12;

@ExtensionFacade
public class P1Facade1 {


    public Ext11 getExt1() {
        return () -> 1;
    }


    public Ext12 getExt112() {
        return ()->2;
    }
}
