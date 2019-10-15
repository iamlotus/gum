package jinlo.gum.core.testapps.app4;


import jinlo.gum.core.annotation.Business;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Business(facades = ParentBusiness.class)
public interface ParentBusiness {

    class MyCodeParser implements BusinessCodeParser{
        private static final BusinessCode PARENT = BusinessCode.of("app.parent");

        private static final Set<BusinessCode> RANGE;

        static{
            Set<BusinessCode> range=new HashSet<>();
            range.add(PARENT);
            RANGE= Collections.unmodifiableSet(range);
        }


        @Override
        public boolean knows(Object instance) {
            return instance instanceof Entity;
        }

        @Override
        public BusinessCode parse(Object instance) throws IllegalArgumentException {
            return PARENT;
        }

        @Override
        public Set<BusinessCode> range() {
            return RANGE;
        }
    }
}
