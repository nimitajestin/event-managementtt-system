package exception;
public class ServiceInitializationException extends RuntimeException {
    //Service fails to start due to unavailable resources or bugs in setup
    public ServiceInitializationException(String message) {
        super(message);
    }
    
    public ServiceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
