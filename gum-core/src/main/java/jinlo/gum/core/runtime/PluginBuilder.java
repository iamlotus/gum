package jinlo.gum.core.runtime;


/**
 * Build Plugin
 */
public interface PluginBuilder {
    /**
     * build Plugin
     *
     * @return
     */
    Plugin build(BeanRepository repository);
}
