package jinlo.gum.demo.quickstart.app;


import jinlo.gum.core.annotation.Business;
import jinlo.gum.demo.quickstart.app.facades.App1DeliveryFacade;
import jinlo.gum.demo.quickstart.domain.delivery.receive.facade.DeliveryBusinessBlankFacade;


@Business(name = "业务2", desc = "业务2示例", facades = {DeliveryBusinessBlankFacade.class})
public interface App2Business {

}
