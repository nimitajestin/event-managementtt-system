package exception;
public class ServiceInitializationException extends RuntimeException {
    
    public ServiceInitializationException(String message) {
        super(message);
    }
    
    public ServiceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
