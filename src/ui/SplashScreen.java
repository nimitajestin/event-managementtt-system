package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class SplashScreen extends JFrame {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private Timer timer;
    private int progress = 0;
    private boolean loadingComplete = false;
    
    private final Color PRIMARY_COLOR = new Color(0, 121, 107);
    private final Color ACCENT_COLOR = new Color(0, 150, 136);
    private final Color BUTTON_TEXT_COLOR = Color.BLACK;
    
    public SplashScreen() {
        initializeUI();
        startProgressSimulation();
    }
    
    private void initializeUI() {
        // Basic frame setup
        setUndecorated(true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with custom rounded corners and gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background with new colors
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, 
                        getWidth(), getHeight(), ACCENT_COLOR);
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Logo panel (dummy logo if resource not available)
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw a stylized "EMS" text as logo
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                g2d.drawString("EMS", getWidth()/2 - 50, getHeight()/2 + 15);
                
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(getWidth()/2 - 70, getHeight()/2 - 40, 140, 80, 20, 20);
                
                g2d.dispose();
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(200, 120));
        
        // Title and subtitle panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Event Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(Box.createVerticalGlue());
        
        // Progress panel
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setOpaque(false);
        
        statusLabel = new JLabel("Initializing application...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Progress bar - update colors
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(255, 255, 255, 180));
        progressBar.setBackground(new Color(0, 0, 0, 80));
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setPreferredSize(new Dimension(400, 15));
        progressBar.setMaximumSize(new Dimension(500, 15));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        progressPanel.add(statusLabel);
        progressPanel.add(Box.createVerticalStrut(15));
        progressPanel.add(progressBar);
        
        // Version label
        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(255, 255, 255, 150));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Add components to main panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(logoPanel, BorderLayout.NORTH);
        centerPanel.add(textPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(progressPanel, BorderLayout.SOUTH);
        mainPanel.add(versionLabel, BorderLayout.NORTH);
        
        // Set content pane and shape
        setContentPane(mainPanel);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
    }
    
    private void startProgressSimulation() {
        timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 1;
                progressBar.setValue(progress);
                
                // Update status text based on progress
                if (progress < 30) {
                    statusLabel.setText("Loading application components...");
                } else if (progress < 60) {
                    statusLabel.setText("Initializing database connection...");
                } else if (progress < 80) {
                    statusLabel.setText("Preparing user interface...");
                } else {
                    statusLabel.setText("Almost ready...");
                }
                
                if (progress >= 100) {
                    timer.stop();
                    statusLabel.setText("Ready to launch!");
                    loadingComplete = true;
                }
            }
        });
        timer.start();
    }
    
    @Override
    public void dispose() {
        try {
            // Clean up resources before disposing
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            super.dispose();
        } catch (Exception e) {
            // Handle exception
        }
    }
    
    // Method to check if loading is complete
    public boolean isLoadingComplete() {
        return loadingComplete;
    }
    
    // Method to wait for loading to complete
    public void waitForLoading() {
        int maxWaitTimeMs = 10000; // 10 seconds timeout
        int waitInterval = 100;
        int totalWaitTime = 0;
        
        while (!isLoadingComplete() && totalWaitTime < maxWaitTimeMs) {
            try {
                Thread.sleep(waitInterval);
                totalWaitTime += waitInterval;
            } catch (InterruptedException e) {
                // Handle exception
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        if (totalWaitTime >= maxWaitTimeMs) {
            loadingComplete = true; // Force completion to prevent hanging
        }
    }
}
