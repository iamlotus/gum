package jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension;

import jinlo.gum.core.annotation.Extension;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomain;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;


@FunctionalInterface
@Extension(name = "自定义详细的收货地址", domain = DeliveryDomain.class)
public interface NormalReceiveExtension {
    String getCustomDetailReceiveAddress(DeliveryOrderLine orderLine);
}
