package jinlo.gum.demo.quickstart.domain.delivery.ability.receive.facade;

import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;

public class DeliveryBusinessBlankFacade implements ReceiveMethodFacade {

    @Override
    public String getCustomDetailReceiveAddress(DeliveryOrderLine orderLine) {
        return null;
    }

    @Override
    public ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine) {
        return null;
    }

    @Override
    public String getCustomReceiveMobileNumber(DeliveryOrderLine orderLine) {
        return null;
    }
}
