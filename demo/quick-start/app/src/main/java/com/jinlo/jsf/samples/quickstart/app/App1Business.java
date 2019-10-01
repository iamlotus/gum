package com.jinlo.jsf.samples.quickstart.app;


import com.jinlo.jsf.client.annotation.BusinessTemplate;
import com.jinlo.jsf.samples.quickstart.app.parser.App1BusinessCodeParser;

@BusinessTemplate(name = "业务1",desc = "业务1示例",parser = App1BusinessCodeParser.class)
public interface App1Business {

}
