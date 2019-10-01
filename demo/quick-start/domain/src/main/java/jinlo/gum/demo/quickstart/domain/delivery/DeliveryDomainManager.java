package jinlo.gum.demo.quickstart.domain.delivery;

import com.jinlo.jsf.client.JSF;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.ability.ReceiveMethodAbility;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;

/**
 * @author zhenxin.yzx ( Rocky )
 * @since 2019/2/16
 */
public class DeliveryDomainManager {

	private static DeliveryDomainManager instance=new DeliveryDomainManager();

	private DeliveryDomainManager() {

	}

	public static DeliveryDomainManager getInstance() {
		return instance;
	}

	public void doSomeCustomDeliveryCalc(DeliveryOrderLine orderLine) {

		ReceiveMethodAbility ability = JSF.getAbility(ReceiveMethodAbility.class);
		if (ability==null) {
			throw new RuntimeException("Failed to get effect receive method ability..");
		}
		String customDetail = ability.getCustomReceiveDetail(orderLine);//框架层面不用关心具体有哪些实例
		System.out.println("==> 自定义收货地址详情：" + customDetail);
	}
}
