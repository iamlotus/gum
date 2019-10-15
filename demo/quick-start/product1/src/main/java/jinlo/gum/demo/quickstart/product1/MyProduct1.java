package jinlo.gum.demo.quickstart.product1;


import jinlo.gum.core.annotation.Product;
import jinlo.gum.demo.quickstart.product1.facades.MyProduct1DeliveryFacade;

@Product(name = "自定义产品1", facades = MyProduct1DeliveryFacade.class)
public interface MyProduct1 {

}
