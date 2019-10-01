package jinlo.gum.core.testapps.app1.product1;

import jinlo.gum.core.testapps.app1.domain1.Ext11;
import jinlo.gum.core.testapps.app1.domain1.Ext12;

// one facade should not satisfies more than one extension
//@ExtensionFacade(belongsTo = Product1.class)
public class WrongFacade6 {
    public interface Ext112 extends Ext11, Ext12 {

    }


    public Ext11 getExt1() {
        return () -> 0;
    }


    public Ext112 getExt112() {
        return new Ext112() {
            @Override
            public int f1() {
                return 1;
            }

            @Override
            public int f2() {
                return 2;
            }
        };
    }
}
