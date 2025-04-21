package ui.components;

import java.awt.*;
import javax.swing.*;

/**
 * Base class for dashboard panels in the application
 */
public abstract class BaseDashboardPanel extends JPanel {
    protected String title;
    protected Color backgroundColor;
    protected Color accentColor;
    
    public BaseDashboardPanel(String title, Color backgroundColor, Color accentColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.accentColor = accentColor;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create and add header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create and add content
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the header panel with title
     */
    protected JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(accentColor);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    /**
     * Creates the content panel (to be implemented by subclasses)
     */
    protected abstract JPanel createContentPanel();
    
    /**
     * Refreshes the panel's data
     */
    public abstract void refreshData();
}
