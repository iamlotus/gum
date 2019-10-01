package jinlo.gum.core.testapps.app1.business2;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.domain1.Ext12;

@ExtensionFacade
public class B2Facade1 {

    public Ext11 getExt1() {
        return () -> 31;
    }

    public Ext12 getExt2() {
        return ()->32;
    }
}
