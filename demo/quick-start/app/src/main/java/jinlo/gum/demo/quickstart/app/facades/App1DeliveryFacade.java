package jinlo.gum.demo.quickstart.app.facades;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.NormalReceiveExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.ReceiveMethodSelectorExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.VirtualReceiveMethodExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.facade.DeliveryBusinessBlankFacade;


@ExtensionFacade(name = "交付域：电影票业务实现")
public class App1DeliveryFacade extends DeliveryBusinessBlankFacade {

    //电影票业务是虚拟收货地址

    @Override
    public ReceiveMethodSelectorExtension getReceiveMethodSelectorExtension() {
        return orderLine -> ReceiveMethodType.VIRTUAL;
    }

    @Override
    public VirtualReceiveMethodExtension getVirtualReceiveMethodExtension() {
        return orderLine -> "13845678900";
    }

}
