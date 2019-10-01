package jinlo.gum.core.testapps.app3;

import jinlo.gum.core.annotation.ExtensionFacade;

@ExtensionFacade
public class PFacade {
    public Ext1 getExt1(){
        return ()->200;
    }
}
