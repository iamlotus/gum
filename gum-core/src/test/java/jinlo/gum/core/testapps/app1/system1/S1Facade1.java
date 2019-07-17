package jinlo.gum.core.testapps.app1.system1;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.domain1.Ext12;

@ExtensionFacade(belongsTo = System1.class)
public class S1Facade1 {


    public Ext11 getExt1() {
        return () -> 1;
    }


    public Ext12 getExt112() {
        return ()->2;
    }
}
