package utils;

import exception.ConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String DB_CONFIG_PATH = "config/db.properties";
    
    private ConfigLoader() {
    }
    
    public static Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(DB_CONFIG_PATH)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load database configuration", e);
        }
    }
}
