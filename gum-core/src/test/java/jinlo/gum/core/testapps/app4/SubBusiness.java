package jinlo.gum.core.testapps.app4;


import jinlo.gum.core.annotation.Business;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Business(facades = SubBusinessFacade.class)
public interface SubBusiness {

    class MyCodeParser implements BusinessCodeParser {

        private static final BusinessCode SUB = BusinessCode.of("app.sub");

        private static final Set<BusinessCode> RANGE;

        static {
            Set<BusinessCode> range = new HashSet<>();
            range.add(SUB);
            RANGE = Collections.unmodifiableSet(range);
        }


        @Override
        public boolean knows(Object instance) {
            return instance instanceof Entity;
        }

        @Override
        public BusinessCode parse(Object instance) throws IllegalArgumentException {
            return SUB;
        }

        @Override
        public Set<BusinessCode> range() {
            return RANGE;
        }
    }
}
