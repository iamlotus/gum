package jinlo.gum.core.testapps.app1.business2;

import jinlo.gum.core.annotation.Business;
import jinlo.gum.core.model.InstanceRecgonizer;


@Business(recgonizer = Business2.FalseChecker.class, facades = B2Facade1.class)
public interface Business2 {
    class FalseChecker implements InstanceRecgonizer {
        @Override
        public boolean knows(Object instance) {
            return false;
        }
    }

}
