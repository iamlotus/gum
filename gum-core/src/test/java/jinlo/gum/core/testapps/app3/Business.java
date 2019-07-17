package jinlo.gum.core.testapps.app3;


import jinlo.gum.core.annotation.BusinessTemplate;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;

@BusinessTemplate(parser = Business.MyCodeParser.class)
public interface Business {

    class MyCodeParser implements BusinessCodeParser{

        @Override
        public boolean knows(Object instance) {
            return instance instanceof Entity;
        }

        @Override
        public BusinessCode parse(Object instance) throws IllegalArgumentException {
            return BusinessCode.of("app3");
        }
    }
}
