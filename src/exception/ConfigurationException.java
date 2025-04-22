package exception;
//method overloading
//configuration files are missing like prop file or read error or invlaid or missing config values
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super(message);
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
