package com.jinlo.jsf.samples.quickstart.app.facades;

import com.jinlo.jsf.client.annotation.ExtensionFacade;
import com.jinlo.jsf.samples.quickstart.app.App1Business;
import jinlo.gum.demo.quickstart.domain.delivery.ability.receive.facade.DeliveryBusinessBlankFacade;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;
import jinlo.gum.demo.quickstart.domain.delivery.model.ReceiveMethodType;


@ExtensionFacade(name = "交付域：业务1实现",belongsTo = App1Business.class)
public class App1DeliveryFacade extends DeliveryBusinessBlankFacade {
    @Override
    public String getCustomDetailReceiveAddress(DeliveryOrderLine orderLine) {
        return null;
    }

    @Override
    public ReceiveMethodType getCustomReceiveMethodType(DeliveryOrderLine orderLine) {
        return ReceiveMethodType.VIRTUAL;
    }

}
