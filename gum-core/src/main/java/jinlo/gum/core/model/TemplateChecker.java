package jinlo.gum.core.model;


/**
 * 对一个实例，判断某模板是否生效。这里传进来的实例就是{@link BusinessCodeParser}中用来区别业务身份的特殊业务对象。
 * @see BusinessCodeParser
 */
public interface TemplateChecker {

    class AlwaysKnowChecker implements TemplateChecker{
        public static AlwaysKnowChecker INSTANCE=new AlwaysKnowChecker();

        @Override
        public boolean knows(Object instance) {
            return true;
        }
    }

    /**
     * 模板能否识别业务对象，若不能识别，对应的模板就不生效
     * @param instance
     * @return
     */
    boolean knows(Object instance);
}
