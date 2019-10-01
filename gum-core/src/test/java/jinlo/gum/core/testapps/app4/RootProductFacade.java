package jinlo.gum.core.testapps.app4;

import jinlo.gum.core.annotation.ExtensionFacade;

@ExtensionFacade
public class RootProductFacade {
    public Ext1 getExt1(){
        return ()->100;
    }
}
