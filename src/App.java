import javax.swing.*;
import ui.MainFrame;
import ui.SplashScreen;
import exception.DatabaseException;
import exception.ServiceInitializationException;
import exception.ValidationException;

public class App {
    
    public static void main(String[] args) {
        
        // Set up specific exception handling for different exception types
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            // Different handling based on exception type - demonstrates polymorphism
            if (throwable instanceof DatabaseException) {
                JOptionPane.showMessageDialog(null,
                        "Database Error: " + throwable.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (throwable instanceof ValidationException) {
                JOptionPane.showMessageDialog(null,
                        "Validation Error: " + throwable.getMessage(),
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
            else if (throwable instanceof ServiceInitializationException) {
                JOptionPane.showMessageDialog(null,
                        "Service Initialization Error: " + throwable.getMessage(),
                        "Initialization Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "An unexpected error occurred: " + throwable.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            SwingUtilities.invokeAndWait(() -> {
                try {
                    SplashScreen splash = new SplashScreen();
                    splash.setVisible(true);
                    
                    new Thread(() -> {
                        try {
                            // Wait until splash screen loading is complete
                            splash.waitForLoading();
                            
                            // Delay slightly for a smoother transition
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            
                            // Switch directly to MainFrame on EDT
                            SwingUtilities.invokeLater(() -> {
                                try {
                                    splash.dispose();
                                    
                                    // Create and display the main frame directly
                                    MainFrame mainFrame = new MainFrame();
                                    mainFrame.setVisible(true);
                                } catch (Exception e) {
                                    // Remove logger
                                    JOptionPane.showMessageDialog(null,
                                            "Error opening main application: " + e.getMessage(),
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                        } catch (Exception e) {
                            // Remove logger
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null,
                                        "Error loading application: " + e.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            });
                        }
                    }, "SplashMonitorThread").start();
                } catch (Exception e) {
                    // Remove logger
                    JOptionPane.showMessageDialog(null,
                            "Error creating splash screen: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } catch (Exception e) {
            // Remove logger
            JOptionPane.showMessageDialog(null, 
                "Failed to start application: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
