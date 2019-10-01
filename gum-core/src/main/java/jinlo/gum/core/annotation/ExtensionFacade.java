package jinlo.gum.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ExtensionFacade} provides some implementations of {@link Extension}, it must be concrete class and <b>satisfies</b>
 *  at least one {@link Extension}. An implementation of {@link Extension} can not be found by gum framework unless it resides
 *  in a class annotates {@link ExtensionFacade}.
 *  <p>
 *  An {@link ExtensionFacade} <b>satisfies</b> one {@link Extension} if the Facade class has a public no-arg method which
 *  return type implements the Extension class. for example:
 *  <pre>
 *      @Extension
 *      public interface NumExt{
 *          int getNum();
 *      }
 *
 *      @ExtesnionFacade
 *      public class MyFacade{
 *          NumExt getNumExt(){
 *              return ()->10;
 *          }
 *      }
 *  </pre>
 *  A Facade can satisfy an Extension at most once, so we can use Facade to locate implementation of Extension. Thus below is a incorrect sample
 *  <pre>
 *      @Extension
 *      public interface NumExt{
 *          int getNum();
 *      }
 *
 *      @ExtesnionFacade
 *      public class MyFacade{
 *          NumExt getNumExt1(){
 *              return ()->1;
 *          }
 *
 *          // this will cause exception during runtime since there are two methods return NumExt,
 *          // when we say "use implementation of MyFacade", it makes confusion.
 *          NumExt getNumExt2(){
 *              return ()->2;
 *          }
 *      }
 *  </pre>
 *  Why we choose return implementation of Facade instead of implements Extension in Facade? The answer is,
 *  there may be Extensions with exactly same method signature but different meaning, a Facade can satisfies
 *  these Extensions by providing individual method for each Extension, but a class implements these Extensions
 *  can provide only one implementation to fit them all.
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExtensionFacade {

    /**
     * @return name, default to class name, for document purpose.
     */
    String name() default "";

    /**
     * @return description, for document purpose.
     */
    String desc() default "";


}
