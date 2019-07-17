package jinlo.gum.core.testapps.app1.domain2;


import jinlo.gum.core.annotation.Domain;
import jinlo.gum.core.testapps.app1.domain1.Domain1;

@Domain(parentDomainClass = Domain1.class)
public interface Domain2 {
}
