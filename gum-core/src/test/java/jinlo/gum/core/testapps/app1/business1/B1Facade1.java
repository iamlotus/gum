package jinlo.gum.core.testapps.app1.business1;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.domain1.Ext12;

@ExtensionFacade(belongsTo = Business1.class,name = "B1Facade1",desc = "B1Facade1 desc")
public class B1Facade1 {

    public Ext11 getExt1() {
        return () -> 11;
    }

    public Ext12 getExt2() {
        return ()->12;
    }
}
