package jinlo.gum.core.annotation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSFService {
    Class<?>[] target();
}
