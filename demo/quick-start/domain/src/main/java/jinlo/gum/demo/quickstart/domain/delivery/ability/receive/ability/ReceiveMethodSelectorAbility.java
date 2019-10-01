package jinlo.gum.demo.quickstart.domain.delivery.ability.receive.ability;

import com.jinlo.jsf.client.JSF;
import com.jinlo.jsf.client.annotation.DomainFunction;
import com.jinlo.jsf.client.reduce.Reducers;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomain;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension.ReceiveMethodSelectorExtension;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;

import java.util.Objects;

@DomainFunction(name = "获取收货类型能力", domain = DeliveryDomain.class)
public class ReceiveMethodSelectorAbility {

    public ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine) {
        return JSF.reduceExecute(ReceiveMethodSelectorExtension.class,
                e -> e.getCustomReceiveMethodType(orderLine),
                Reducers.firstOf(Objects::nonNull)).value();
    }
}
