package jinlo.gum.demo.quickstart.app.parser;

import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.demo.quickstart.domain.delivery.model.DeliveryOrderLine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class App1BusinessCodeParser implements BusinessCodeParser {

    private static final BusinessCode NOTHING = BusinessCode.of("app1.nothing");

    private static final BusinessCode NORMAL = BusinessCode.of("app1.normal");

    private static final BusinessCode VIRTUAL = BusinessCode.of("app1.virtual");

    private static final Set<BusinessCode> RANGE;

    static{
        Set<BusinessCode> range=new HashSet<>();
        range.add(NOTHING);
        range.add(NORMAL);
        range.add(VIRTUAL);
        RANGE= Collections.unmodifiableSet(range);
    }

    @Override
    public boolean knows(Object instance) {
        return (instance instanceof DeliveryOrderLine);
    }

    @Override
    public BusinessCode parse(Object instance) throws IllegalArgumentException {
        DeliveryOrderLine line = (DeliveryOrderLine) instance;
        long id = line.getOrderLineId();

        String code;
        switch ((int)id%3){
            case 0:
                return NOTHING;
            case 1:
                return NORMAL;
            default:
                return VIRTUAL;

        }
    }

    @Override
    public Set<BusinessCode> range() {
        return RANGE;
    }
}
