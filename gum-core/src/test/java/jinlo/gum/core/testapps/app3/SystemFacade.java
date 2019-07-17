package jinlo.gum.core.testapps.app3;

import jinlo.gum.core.annotation.ExtensionFacade;

@ExtensionFacade(belongsTo = System.class)
public class SystemFacade {
    public Ext1 getExt1(){
        return ()->200;
    }
}
