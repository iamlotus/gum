package jinlo.gum.core.testapps.app3;

import jinlo.gum.core.annotation.ExtensionFacade;

@ExtensionFacade
public class BFacade {
    public Ext1 getExt1(){
        return ()->100;
    }
}
