package jinlo.gum.core.exception;


/**
 * Exception during construct BusinessConfig
 */
public class BusinessConfigException extends RuntimeException {
    public BusinessConfigException(Exception e) {
        super(e);
    }

    public BusinessConfigException(String message) {
        super(message);
    }

    public BusinessConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessConfigException(Throwable cause) {
        super(cause);
    }
}
