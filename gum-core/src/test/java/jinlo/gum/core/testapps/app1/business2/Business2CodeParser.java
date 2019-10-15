package jinlo.gum.core.testapps.app1.business2;

import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.testapps.app1.domain2.Entity21;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Business2CodeParser implements BusinessCodeParser {

    private static final BusinessCode CODE3 = BusinessCode.of("sample.code3");

    private static final Set<BusinessCode> RANGE;

    static{
        Set<BusinessCode> range=new HashSet<>();
        range.add(CODE3);
        RANGE= Collections.unmodifiableSet(range);
    }

    @Override
    public boolean knows(Object instance) {
        return instance instanceof Entity21;
    }

    @Override
    public BusinessCode parse(Object instance) throws IllegalArgumentException {
        return CODE3;
    }

    @Override
    public Set<BusinessCode> range() {
        return RANGE;
    }
}
