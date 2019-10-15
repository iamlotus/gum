package jinlo.gum.core.testapps.app3;


import jinlo.gum.core.annotation.Business;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Business(facades = BFacade.class)
public interface B {

    class MyCodeParser implements BusinessCodeParser{

        private static final BusinessCode APP3 = BusinessCode.of("app3");

        private static final Set<BusinessCode> RANGE;

        static{
            Set<BusinessCode> range=new HashSet<>();
            range.add(APP3);
            RANGE= Collections.unmodifiableSet(range);
        }

        @Override
        public boolean knows(Object instance) {
            return instance instanceof Entity;
        }

        @Override
        public BusinessCode parse(Object instance) throws IllegalArgumentException {
            return APP3;
        }

        @Override
        public Set<BusinessCode> range() {
            return RANGE;
        }
    }
}
