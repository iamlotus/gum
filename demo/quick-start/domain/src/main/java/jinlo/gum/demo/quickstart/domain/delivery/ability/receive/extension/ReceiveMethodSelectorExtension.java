package jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension;

import com.jinlo.jsf.client.annotation.Extension;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;


@FunctionalInterface
@Extension(name ="自定义收货方式" )
public interface ReceiveMethodSelectorExtension {
	ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine);
}
