package com.jinlo.jsf.samples.quickstart;

import jinlo.gum.demo.quickstart.domain.delivery.DeliveryDomainManager;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;

import java.util.Arrays;
import java.util.List;

public class SampleRun {

    public static void main(String[] args){
        init();

        DeliveryOrderLine orderLine=new DeliveryOrderLine(1);
        List<?> bizInstances= Arrays.asList(orderLine);

        new BizSessionScope(bizInstances){

            protected Object execute(){
                DeliveryDomainManager.getInstance().doSomeCustomDeliveryCalc(orderLine);
                return null;
            }
        }.invoke();

    }

    private static void init(){
        String packageName=SampleRun.class.getPackage().getName();
        // scan
        JSFEnv.newInitializer().scanProduct(packageName).scanBusiness(packageName).init();



    }
}
