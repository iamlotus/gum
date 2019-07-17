package jinlo.gum.core.testapps.app1.business2;

import jinlo.gum.core.annotation.BusinessTemplate;
import jinlo.gum.core.model.TemplateChecker;


@BusinessTemplate(parser = Business2CodeParser.class,checker = Business2.FalseChecker.class)
public interface Business2 {
    class FalseChecker implements TemplateChecker{
        @Override
        public boolean knows(Object instance) {
            return false;
        }
    }

}
