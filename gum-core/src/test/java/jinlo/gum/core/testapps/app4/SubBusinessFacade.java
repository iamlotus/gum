package jinlo.gum.core.testapps.app4;

import jinlo.gum.core.annotation.ExtensionFacade;

@ExtensionFacade(name = "子业务产品")
public class SubBusinessFacade {
    public Ext1 getExt1() {
        return () -> 300;
    }
}
