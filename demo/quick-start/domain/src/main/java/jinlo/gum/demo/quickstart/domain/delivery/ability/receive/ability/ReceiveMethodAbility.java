package jinlo.gum.demo.quickstart.domain.delivery.ability.receive.ability;

import com.jinlo.jsf.client.JSF;
import com.jinlo.jsf.client.annotation.DomainFunction;
import com.jinlo.jsf.client.reduce.Reducers;
import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomain;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension.NormalReceiveExtension;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.extension.VirtualReceiveMethodExtension;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;

import java.util.Objects;

@DomainFunction(name = "获取收货地址详细信息能力", domain = DeliveryDomain.class)
public class ReceiveMethodAbility  {

	/**
	 * @return 获取当前收货方式能力实例所需要的收货地址详细信息
	 */
	public  String getCustomReceiveDetail(DeliveryOrderLine orderLine){
		ReceiveMethodSelectorAbility receiveMethodSelectorAbility=JSF.getAbility(ReceiveMethodSelectorAbility.class);

		switch (receiveMethodSelectorAbility.getCustomReceiveMethodType(orderLine)){
			case VIRTUAL:
				//虚拟收货方式
				return JSF.reduceExecute(VirtualReceiveMethodExtension.class,
						p->p.getCustomReceiveMobileNumber(orderLine),
						Reducers.firstOf(Objects::nonNull)).value();
			case NORMAL:
				default:
					//未特殊指定则为默认收货方式
					return JSF.reduceExecute(NormalReceiveExtension.class,
							p->p.getCustomDetailReceiveAddress(orderLine),
							Reducers.firstOf(Objects::nonNull)).value();
		}
	}


}
