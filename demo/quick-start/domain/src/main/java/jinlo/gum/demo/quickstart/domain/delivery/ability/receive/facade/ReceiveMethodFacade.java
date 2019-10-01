package jinlo.gum.demo.quickstart.domain.delivery.ability.receive.facade;

import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension.NormalReceiveExtension;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension.ReceiveMethodSelectorExtension;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension.VirtualReceiveMethodExtension;

public interface ReceiveMethodFacade extends NormalReceiveExtension, VirtualReceiveMethodExtension, ReceiveMethodSelectorExtension {

}
