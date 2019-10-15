package jinlo.gum.demo.quickstart.app;


import jinlo.gum.core.annotation.Business;
import jinlo.gum.demo.quickstart.app.facades.App1DeliveryFacade;


@Business(name = "业务1", desc = "业务1示例", facades = {App1DeliveryFacade.class})
public interface App1Business {

}
