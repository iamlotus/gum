package jinlo.gum.core.testapps.app1.system1;

import jinlo.gum.core.testapps.app1.domain1.Ext11;

//@ExtensionFacade(belongsTo = System1.class)
public class WrongFacade4 {
    // static function
    public static Ext11 getExt11() {
        return ()->0;
    }
}
