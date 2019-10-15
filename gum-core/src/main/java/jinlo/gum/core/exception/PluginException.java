package jinlo.gum.core.exception;


/**
 * Exception during construct Plugin
 */
public class PluginException extends RuntimeException {
    public PluginException(Exception e) {
        super(e);
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
