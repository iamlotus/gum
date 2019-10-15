package jinlo.gum.demo.quickstart.domain.delivery.receive.facade;

import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.NormalReceiveExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.ReceiveMethodSelectorExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.VirtualReceiveMethodExtension;


public interface ReceiveMethodFacade {

    NormalReceiveExtension getNormalReceiveExtension();

    VirtualReceiveMethodExtension getVirtualReceiveMethodExtension();

    ReceiveMethodSelectorExtension getReceiveMethodSelectorExtension();


}
