package jinlo.gum.core.testapps.app1.product1;

import jinlo.gum.core.testapps.app1.domain1.Ext11;

//@ExtensionFacade(belongsTo = Product1.class)
public class WrongFacade1 {
    // not no-param function
    Ext11 getExt11(int i) {
        return null;
    }
}
