package jinlo.gum.demo.quickstart.domain.delivery.model;

import lombok.Getter;
import lombok.Setter;

public class DeliveryOrderLine{

    @Getter
    @Setter
    private long orderLineId;

    public DeliveryOrderLine( long orderLineId) {
        this.orderLineId = orderLineId;
    }

}
