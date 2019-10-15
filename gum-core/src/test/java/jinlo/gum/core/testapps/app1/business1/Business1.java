package jinlo.gum.core.testapps.app1.business1;

import jinlo.gum.core.annotation.Business;

@Business(name = "业务1", desc = "desc1", facades = {B1Facade1.class, B1Facade2.class})
public class Business1 {
}
