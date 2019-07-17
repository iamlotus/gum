package jinlo.gum.core.testapps.app3;

import jinlo.gum.core.annotation.ExtensionFacade;

@ExtensionFacade(belongsTo = Business.class)
public class BusinessFacade {
    public Ext1 getExt1(){
        return ()->100;
    }
}
