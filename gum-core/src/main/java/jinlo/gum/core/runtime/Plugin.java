package jinlo.gum.core.runtime;

import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;

import java.util.Set;

/**
 * {@link BusinessCodeParser} is not set in {@link BusinessConfig} because BusinessCodeParser can be shared with
 * some BusinessConfigs. Although Plugin only contains BusinessCodeParser by now, it is designed to be able to
 * contain other components in future.
 */
public interface Plugin {

    /**
     * Name of the plugin, must be unique.
     *
     * @return
     */
    String getName();

    /**
     * Description of the plugin.
     *
     * @return
     */
    String getDescription();

    /**
     * All {@link BusinessCode} the plugin will handle. It equals to invoke {@link BusinessCodeParser#range()}
     * on the result from {@link #getBusinessCodeParser()}
     *
     * @return
     */
    Set<BusinessCode> range();

    /**
     * A {@link BusinessCodeParser} that is used to parse code
     *
     * @return
     */
    BusinessCodeParser getBusinessCodeParser();
}
