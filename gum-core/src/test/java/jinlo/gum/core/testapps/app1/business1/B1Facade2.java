package jinlo.gum.core.testapps.app1.business1;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.core.testapps.app1.domain1.Ext11;

@ExtensionFacade
public class B1Facade2 {

    public Ext11 getFoo() {
        return () -> 22;
    }

}
