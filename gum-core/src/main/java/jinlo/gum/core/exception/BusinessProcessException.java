package jinlo.gum.core.exception;


/**
 * Exception during Business Process
 */
public class BusinessProcessException extends RuntimeException {
    public BusinessProcessException(Exception e) {
        super(e);
    }

    public BusinessProcessException(String message) {
        super(message);
    }

    public BusinessProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessProcessException(Throwable cause) {
        super(cause);
    }
}
