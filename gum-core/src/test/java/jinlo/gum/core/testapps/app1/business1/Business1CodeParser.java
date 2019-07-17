package jinlo.gum.core.testapps.app1.business1;

import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import jinlo.gum.core.testapps.app1.domain1.Entity11;

public class Business1CodeParser implements BusinessCodeParser {
    @Override
    public boolean knows(Object instance) {
        return instance instanceof Entity11;
    }

    @Override
    public BusinessCode parse(Object instance) throws IllegalArgumentException {

        return BusinessCode.of("T".equals(((Entity11)instance).getTag()) ? "sample.code1" : "sample.code2");

    }
}
