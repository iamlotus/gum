package jinlo.gum.core.runtime;

import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.spec.BusinessSpec;
import org.junit.Assert;
import org.junit.Test;

public class BusinessConfigImplTest {

    @Test
    public void tesToString(){
        XmlBusinessConfigBuilder.BusinessConfigImpl b=new XmlBusinessConfigBuilder.BusinessConfigImpl();
        Assert.assertEquals("[BusinessConfig:range=[], business=]",b.toString());

        b.getBusinessCodes().add(BusinessCode.of("code1"));
        b.setBusinessSpec(new BusinessSpec("businessspec",null));
        Assert.assertEquals("[BusinessConfig:range=[code1], business=businessspec]",b.toString());
    }
}
