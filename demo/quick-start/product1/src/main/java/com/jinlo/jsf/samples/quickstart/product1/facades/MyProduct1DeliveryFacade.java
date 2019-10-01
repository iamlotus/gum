package com.jinlo.jsf.samples.quickstart.product1.facades;

import com.jinlo.jsf.client.annotation.ExtensionFacade;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.facade.DeliveryBusinessBlankFacade;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;
import com.jinlo.jsf.samples.quickstart.product1.MyProduct1;


@ExtensionFacade(name = "交付域：自定义产品1实现",belongsTo = MyProduct1.class)
public class MyProduct1DeliveryFacade extends DeliveryBusinessBlankFacade {

    @Override
    public String getCustomDetailReceiveAddress(DeliveryOrderLine orderLine) {
        return "天安门1号";
    }

    @Override
    public ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine) {
       return ReceiveMethodType.NORMAL;
    }

}
