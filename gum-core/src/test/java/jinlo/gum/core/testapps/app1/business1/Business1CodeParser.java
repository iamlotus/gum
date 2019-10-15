package jinlo.gum.core.testapps.app1.business1;

import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.testapps.app1.domain1.Entity11;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Business1CodeParser implements BusinessCodeParser {

    private static final BusinessCode CODE1 = BusinessCode.of("sample.code1");

    private static final BusinessCode CODE2 = BusinessCode.of("sample.code2");

    private static final Set<BusinessCode> RANGE;

    static{
        Set<BusinessCode> range=new HashSet<>();
        range.add(CODE1);
        range.add(CODE2);
        RANGE=Collections.unmodifiableSet(range);
    }

    @Override
    public boolean knows(Object instance) {
        return instance instanceof Entity11;
    }

    @Override
    public BusinessCode parse(Object instance) throws IllegalArgumentException {

        return "T".equals(((Entity11) instance).getTag()) ? CODE1 : CODE2;

    }

    @Override
    public Set<BusinessCode> range() {
        return RANGE;
    }
}
