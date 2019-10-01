package jinlo.gum.core.testapps.app4;


import jinlo.gum.core.annotation.Business;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;

@Business(parser = SubBusiness.MyCodeParser.class, facades = SubBusinessFacade.class)
public interface SubBusiness {

    class MyCodeParser implements BusinessCodeParser {

        @Override
        public boolean knows(Object instance) {
            return instance instanceof Entity;
        }

        @Override
        public BusinessCode parse(Object instance) throws IllegalArgumentException {
            return BusinessCode.of("app.sub");
        }
    }
}
