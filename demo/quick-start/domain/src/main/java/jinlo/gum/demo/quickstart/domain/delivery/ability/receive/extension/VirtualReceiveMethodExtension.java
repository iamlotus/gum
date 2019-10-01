package jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension;

import com.jinlo.jsf.client.annotation.Extension;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;


@FunctionalInterface
@Extension(name = "自定义虚拟收货手机号码")
public interface VirtualReceiveMethodExtension {
	String getCustomReceiveMobileNumber(DeliveryOrderLine orderLine);
}
