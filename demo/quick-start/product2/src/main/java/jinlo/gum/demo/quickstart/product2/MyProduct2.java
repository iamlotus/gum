package jinlo.gum.demo.quickstart.product2;

import jinlo.gum.core.annotation.Product;
import jinlo.gum.demo.quickstart.product2.facades.MyProduct2DeliveryFacade;

@Product(name = "自定义产品2", facades = MyProduct2DeliveryFacade.class)
public interface MyProduct2{
}
