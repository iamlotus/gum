package jinlo.gum.core.testapps.app1.system2;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.system1.System1;

@ExtensionFacade(belongsTo = System1.class)
public class S2Facade2 {
    public Ext11 getExt1() {
        return () -> 0;
    }

}
