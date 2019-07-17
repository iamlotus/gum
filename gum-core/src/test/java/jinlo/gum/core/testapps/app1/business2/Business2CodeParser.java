package jinlo.gum.core.testapps.app1.business2;

import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.testapps.app1.domain2.Entity21;

public class Business2CodeParser implements BusinessCodeParser {
    @Override
    public boolean knows(Object instance) {
        return instance instanceof Entity21;
    }

    @Override
    public BusinessCode parse(Object instance) throws IllegalArgumentException {
        return BusinessCode.of("sample.code3");
    }
}
