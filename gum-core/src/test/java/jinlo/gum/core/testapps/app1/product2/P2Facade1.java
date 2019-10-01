package jinlo.gum.core.testapps.app1.product2;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.product1.Product1;

@ExtensionFacade
public class P2Facade1 {
    public Ext11 getExt1() {
        return () -> 0;
    }

}
