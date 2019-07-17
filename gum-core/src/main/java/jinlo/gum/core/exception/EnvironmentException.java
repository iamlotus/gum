package jinlo.gum.core.exception;


/**
 * Exception during construct environment
 */
public class EnvironmentException extends RuntimeException {
    public EnvironmentException(Exception e) {
        super(e);
    }

    public EnvironmentException(String message) {
        super(message);
    }

    public EnvironmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnvironmentException(Throwable cause) {
        super(cause);
    }
}
