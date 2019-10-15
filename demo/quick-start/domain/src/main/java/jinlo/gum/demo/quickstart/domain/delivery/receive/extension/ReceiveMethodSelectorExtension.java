package jinlo.gum.demo.quickstart.domain.delivery.receive.extension;

import jinlo.gum.core.annotation.Extension;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomain;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;


@FunctionalInterface
@Extension(name ="自定义收货方式",domain= DeliveryDomain.class)
public interface ReceiveMethodSelectorExtension {
	ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine);
}
