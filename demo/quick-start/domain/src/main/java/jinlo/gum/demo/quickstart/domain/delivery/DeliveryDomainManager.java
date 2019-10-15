package jinlo.gum.demo.quickstart.domain.delivery;

import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.receive.function.ReceiveMethodFunction;


public class DeliveryDomainManager {

	private static DeliveryDomainManager instance=new DeliveryDomainManager();

	private DeliveryDomainManager() {

	}

	public static DeliveryDomainManager getInstance() {
		return instance;
	}

	public void doSomeCustomDeliveryCalc(DeliveryOrderLine orderLine) {

		//TODO can be replaced with IOC
		ReceiveMethodFunction receiveMethodFunction = new ReceiveMethodFunction();

		String customDetail = receiveMethodFunction.getCustomReceiveDetail(orderLine);//框架层面不用关心具体有哪些实例
		System.out.println("orderLine.id="+orderLine.getOrderLineId()+  " ==> customDetail：" + customDetail);
	}
}
