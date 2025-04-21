package ui.components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import utils.Printable;

public class BaseComponent extends JPanel implements Printable {
    
    protected String title;
    protected Color backgroundColor;
    protected Font titleFont;
    
    public BaseComponent(String title, Color backgroundColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.titleFont = new Font("Segoe UI", Font.BOLD, 16);
        setBackground(backgroundColor);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String toPrintableString() {
        return "Component: " + title;
    }
}
