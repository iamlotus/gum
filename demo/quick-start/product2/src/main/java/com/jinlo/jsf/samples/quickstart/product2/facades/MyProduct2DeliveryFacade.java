package com.jinlo.jsf.samples.quickstart.product2.facades;

import com.jinlo.jsf.client.annotation.ExtensionFacade;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.facade.DeliveryBusinessBlankFacade;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;
import com.jinlo.jsf.samples.quickstart.product2.MyProduct2;

@ExtensionFacade(name = "交付域：自定义产品2实现",belongsTo = MyProduct2.class)
public class MyProduct2DeliveryFacade extends DeliveryBusinessBlankFacade {

    @Override
    public String getCustomDetailReceiveAddress(DeliveryOrderLine orderLine) {
        return "天安门2号";
    }

    @Override
    public ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine) {
        return ReceiveMethodType.NORMAL;
    }

}