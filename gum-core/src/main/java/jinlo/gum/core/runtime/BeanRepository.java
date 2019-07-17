package jinlo.gum.core.runtime;

public interface BeanRepository {

    /**
     * Get Bean by name
     * @param className
     * @param <T>
     * @return Bean
     */
    <T> T getBean(String className);
}
