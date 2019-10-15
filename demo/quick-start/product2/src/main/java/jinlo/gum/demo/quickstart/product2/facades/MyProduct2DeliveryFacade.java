package jinlo.gum.demo.quickstart.product2.facades;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.NormalReceiveExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.ReceiveMethodSelectorExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.facade.DeliveryBusinessBlankFacade;

@ExtensionFacade(name = "交付域：自定义产品2实现")
public class MyProduct2DeliveryFacade extends DeliveryBusinessBlankFacade {

    @Override
    public ReceiveMethodSelectorExtension getReceiveMethodSelectorExtension() {
        return orderLine -> ReceiveMethodType.NORMAL;
    }

    @Override
    public NormalReceiveExtension getNormalReceiveExtension() {
        return orderLine -> "天安门2号";
    }

}