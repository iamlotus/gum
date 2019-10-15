package jinlo.gum.demo.quickstart.domain.delivery.receive.function;

import jinlo.gum.core.annotation.DomainFunction;
import jinlo.gum.core.reduce.Reducers;
import jinlo.gum.core.runtime.BusinessProcesses;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomain;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.NormalReceiveExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.ReceiveMethodSelectorExtension;
import jinlo.gum.demo.quickstart.domain.delivery.receive.extension.VirtualReceiveMethodExtension;

import java.util.Objects;

/**
 * 收货相关功能
 */
public class ReceiveMethodFunction {

    @DomainFunction(name = "获取收货地址详细信息能力", domain = DeliveryDomain.class)
    public String getCustomReceiveDetail(DeliveryOrderLine orderLine) {
        ReceiveMethodType type = getCustomReceiveMethodType(orderLine);
        if (type == null) {
            type = ReceiveMethodType.NORMAL;
        }
        switch (type) {
            case VIRTUAL:
                //虚拟收货方式
                return BusinessProcesses.executeExtension(orderLine,
                        VirtualReceiveMethodExtension.class,
                        Reducers.firstOf(Objects::nonNull),
                        p -> p.getCustomReceiveMobileNumber(orderLine));
            case NORMAL:
            default:
                //未特殊指定则为默认收货方式
                return BusinessProcesses.executeExtension(orderLine,
                        NormalReceiveExtension.class,
                        Reducers.firstOf(Objects::nonNull),
                        p -> p.getCustomDetailReceiveAddress(orderLine));
        }
    }

    @DomainFunction(name = "获取收货类型能力", domain = DeliveryDomain.class)
    public ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine) {
        return BusinessProcesses.executeExtension(orderLine,
                ReceiveMethodSelectorExtension.class,
                Reducers.firstOf(Objects::nonNull),
                e -> e.getCustomReceiveMethodType(orderLine));
    }


}
