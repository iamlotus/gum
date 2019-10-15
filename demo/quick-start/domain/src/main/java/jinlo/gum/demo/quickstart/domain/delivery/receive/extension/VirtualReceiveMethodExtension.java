package jinlo.gum.demo.quickstart.domain.delivery.receive.extension;

import jinlo.gum.core.annotation.Extension;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomain;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;


@FunctionalInterface
@Extension(name = "自定义虚拟收货手机号码",domain= DeliveryDomain.class)
public interface VirtualReceiveMethodExtension {
	String getCustomReceiveMobileNumber(DeliveryOrderLine orderLine);
}
