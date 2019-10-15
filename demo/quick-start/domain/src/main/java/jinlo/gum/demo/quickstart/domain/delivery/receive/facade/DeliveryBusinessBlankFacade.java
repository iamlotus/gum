package jinlo.gum.demo.quickstart.domain.delivery.receive.facade;

import jinlo.gum.core.annotation.ExtensionFacade;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.NormalReceiveExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.ReceiveMethodSelectorExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.VirtualReceiveMethodExtension;


@ExtensionFacade
public class DeliveryBusinessBlankFacade implements ReceiveMethodFacade {

    @Override
    public NormalReceiveExtension getNormalReceiveExtension() {
        return orderLine -> null;
    }

    @Override
    public VirtualReceiveMethodExtension getVirtualReceiveMethodExtension() {
        return orderLine -> null;
    }

    @Override
    public ReceiveMethodSelectorExtension getReceiveMethodSelectorExtension() {
        return orderLine -> null;
    }
}
