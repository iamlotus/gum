package jinlo.gum.demo.quickstart.domain.delivery.receive.extension;

import jinlo.gum.core.annotation.Extension;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomain;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;


@FunctionalInterface
@Extension(desc = "自定义普通商品的收货地址", domain = DeliveryDomain.class)
public interface NormalReceiveExtension {
    String getCustomDetailReceiveAddress(DeliveryOrderLine orderLine);
}
