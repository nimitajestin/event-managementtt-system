package db;

import exception.DatabaseException;
import utils.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;
    private static final Properties dbProps = ConfigLoader.loadDatabaseProperties();
    
    private DBConnection() {
    }
    
    public static Connection getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                String url = dbProps.getProperty("db.url");
                String username = dbProps.getProperty("db.username");
                String password = dbProps.getProperty("db.password");
                
                // Register JDBC driver
                Class.forName(dbProps.getProperty("db.driver"));
                
                // Open connection
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Database driver not found", e);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }
    }
    
    public static void closeConnection() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close database connection", e);
        }
    }
}
