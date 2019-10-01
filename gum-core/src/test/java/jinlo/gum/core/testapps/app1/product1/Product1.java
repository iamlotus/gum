package jinlo.gum.core.testapps.app1.product1;

import jinlo.gum.core.annotation.Product;
import jinlo.gum.core.testapps.app1.product2.P2Facade1;
import jinlo.gum.core.testapps.app1.product2.P2Facade2;

@Product(name = "系统1", desc = "product1 desc", facades = {P1Facade1.class, P2Facade1.class, P2Facade2.class})
public interface Product1 {
}
