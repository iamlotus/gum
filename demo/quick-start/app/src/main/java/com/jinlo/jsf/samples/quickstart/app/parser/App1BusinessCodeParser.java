package com.jinlo.jsf.samples.quickstart.app.parser;

import com.jinlo.jsf.client.model.BusinessCode;
import com.jinlo.jsf.client.model.BusinessCodeParser;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;

public class App1BusinessCodeParser implements BusinessCodeParser {
    @Override
    public boolean knows(Object instance) {
        return (instance instanceof DeliveryOrderLine);
    }

    @Override
    public BusinessCode parse(Object instance) throws IllegalArgumentException {
        DeliveryOrderLine line=(DeliveryOrderLine) instance;
        long id=line.getOrderLineId();
        String bizCode=id%2==0?"app1.even":"app1.odd";
        return BusinessCode.of(bizCode);
    }
}
