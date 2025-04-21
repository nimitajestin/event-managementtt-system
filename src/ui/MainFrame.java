package ui;

import exception.ServiceInitializationException;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import model.Club;
import model.Event;
import model.Member;
import model.Venue;
import service.ClubService;
import service.EventService;
import service.MemberService;
import service.VenueService;
import ui.components.StatsComponent;
import utils.ValidationUtils;
import ui.components.StatsDashboardPanel;
import ui.components.BaseDashboardPanel;

public class MainFrame extends JFrame {
    
    private ClubService clubService;
    private MemberService memberService;
    private EventService eventService;
    private VenueService venueService;
    
    private JTabbedPane tabbedPane;
    private JPanel clubsPanel;
    private JPanel membersPanel;
    private JPanel eventsPanel;
    private JLabel statusLabel;
    
    private final Color PRIMARY_COLOR = new Color(0, 121, 107);
    private final Color ACCENT_COLOR = new Color(0, 150, 136);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private final Color PANEL_COLOR = Color.WHITE;
    private final Color BUTTON_TEXT_COLOR = Color.BLACK;
    private final Color STATS_COLOR = new Color(240, 240, 250);
    
    public MainFrame() {
        try {
            initializeServices();
            setTitle("Event Management System");
            setSize(1100, 700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.put("TabbedPane.selected", ACCENT_COLOR);
                UIManager.put("TabbedPane.background", BACKGROUND_COLOR);
                UIManager.put("TabbedPane.foreground", Color.DARK_GRAY);
                UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
                UIManager.put("Panel.background", BACKGROUND_COLOR);
                UIManager.put("Button.background", PRIMARY_COLOR);
                UIManager.put("Button.foreground", BUTTON_TEXT_COLOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(BACKGROUND_COLOR);
            JPanel headerPanel = createHeaderPanel();
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
            contentPanel.setBackground(BACKGROUND_COLOR);
            tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
            tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
            tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            clubsPanel = createClubsPanel();
            membersPanel = createMembersPanel();
            eventsPanel = createEventsPanel();
            tabbedPane.addTab("Clubs", null, createScrollPane(clubsPanel), "Manage Clubs");
            tabbedPane.addTab("Members", null, createScrollPane(membersPanel), "Manage Members");
            tabbedPane.addTab("Events", null, createScrollPane(eventsPanel), "Manage Events");
            BaseDashboardPanel statsDashboard = new StatsDashboardPanel(
                    "System Statistics", 
                    BACKGROUND_COLOR,
                    PRIMARY_COLOR,
                    clubService, 
                    memberService, 
                    eventService);
            statsDashboard.setPreferredSize(new Dimension(220, 0));
            contentPanel.add(tabbedPane, BorderLayout.CENTER);
            contentPanel.add(statsDashboard, BorderLayout.EAST);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            JPanel statusBar = new JPanel(new BorderLayout());
            statusBar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            statusBar.setBackground(PANEL_COLOR);
            statusLabel = new JLabel("Ready");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusBar.add(statusLabel, BorderLayout.WEST);
            JLabel versionLabel = new JLabel("v1.0");
            versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            versionLabel.setForeground(Color.GRAY);
            statusBar.add(versionLabel, BorderLayout.EAST);
            mainPanel.add(statusBar, BorderLayout.SOUTH);
            setContentPane(mainPanel);
            tabbedPane.addChangeListener(e -> {
                if (statsDashboard instanceof StatsDashboardPanel) {
                    ((StatsDashboardPanel) statsDashboard).refreshData();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error initializing application: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ServiceInitializationException("Failed to initialize MainFrame", e);
        }
    }
    
    private void initializeServices() {
        try {
            clubService = new ClubService();
            memberService = new MemberService();
            eventService = new EventService();
            venueService = new VenueService();
        } catch (Exception e) {
            throw new ServiceInitializationException("Failed to initialize services: " + e.getMessage(), e);
        }
    }
    
    private JScrollPane createScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel titleLabel = new JLabel("Event Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(15);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(new Color(0, 0, 0, 0));
        searchButton.setForeground(BUTTON_TEXT_COLOR);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim().toLowerCase();
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term", "Search", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int currentTab = tabbedPane.getSelectedIndex();
            if (currentTab == 0) {
                searchClubs(searchTerm);
            } else if (currentTab == 1) {
                searchMembers(searchTerm);
            } else if (currentTab == 2) {
                searchEvents(searchTerm);
            }
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        return headerPanel;
    }
    
    private void searchClubs(String searchTerm) {
        List<Club> allClubs = clubService.findAll();
        List<Club> filteredClubs = new ArrayList<>();
        for (Club club : allClubs) {
            if (club.getClubName().toLowerCase().contains(searchTerm) || 
                (club.getClubDescription() != null && club.getClubDescription().toLowerCase().contains(searchTerm))) {
                filteredClubs.add(club);
            }
        }
        if (filteredClubs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No clubs found matching: " + searchTerm, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        displaySearchResults("Clubs matching '" + searchTerm + "'", convertClubsToData(filteredClubs), 
                             new String[]{"ID", "Club Name", "Description", "Members"});
    }
    
    private Object[][] convertClubsToData(List<Club> clubs) {
        Object[][] data = new Object[clubs.size()][4];
        for (int i = 0; i < clubs.size(); i++) {
            Club club = clubs.get(i);
            data[i][0] = club.getClubId();
            data[i][1] = club.getClubName();
            data[i][2] = club.getClubDescription();
            data[i][3] = memberService.findMembersByClub(club.getClubId()).size();
        }
        return data;
    }
    
    private void searchMembers(String searchTerm) {
        List<Member> allMembers = memberService.findAll();
        List<Member> filteredMembers = new ArrayList<>();
        for (Member member : allMembers) {
            if (member.getMemberName().toLowerCase().contains(searchTerm) || 
                (member.getEmail() != null && member.getEmail().toLowerCase().contains(searchTerm)) ||
                (member.getPhone() != null && member.getPhone().contains(searchTerm))) {
                filteredMembers.add(member);
            }
        }
        if (filteredMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No members found matching: " + searchTerm, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Object[][] data = new Object[filteredMembers.size()][6];
        for (int i = 0; i < filteredMembers.size(); i++) {
            Member member = filteredMembers.get(i);
            data[i][0] = member.getMemberId();
            data[i][1] = member.getMemberName();
            data[i][2] = member.getEmail();
            data[i][3] = member.getPhone();
            data[i][4] = member.getJoinDate() != null ? dateFormat.format(member.getJoinDate()) : "";
            Club club = clubService.findById(member.getClubId());
            data[i][5] = club != null ? club.getClubName() : "";
        }
        displaySearchResults("Members matching '" + searchTerm + "'", data, 
                             new String[]{"ID", "Name", "Email", "Phone", "Join Date", "Club"});
    }
    
    private void searchEvents(String searchTerm) {
        List<Event> allEvents = eventService.findAll();
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (event.getEventName().toLowerCase().contains(searchTerm) || 
                (event.getDescription() != null && event.getDescription().toLowerCase().contains(searchTerm))) {
                filteredEvents.add(event);
            }
        }
        if (filteredEvents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No events found matching: " + searchTerm, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Object[][] data = new Object[filteredEvents.size()][6];
        for (int i = 0; i < filteredEvents.size(); i++) {
            Event event = filteredEvents.get(i);
            data[i][0] = event.getEventId();
            data[i][1] = event.getEventName();
            data[i][2] = event.getDescription();
            Club club = clubService.findById(event.getClubId());
            data[i][3] = club != null ? club.getClubName() : "";
            data[i][4] = event.getEventDate() != null ? dateFormat.format(event.getEventDate()) : "";
            data[i][5] = event.getVenueId();
        }
        displaySearchResults("Events matching '" + searchTerm + "'", data, 
                             new String[]{"ID", "Event Name", "Description", "Club", "Date", "Venue"});
    }
    
    private void displaySearchResults(String title, Object[][] data, String[] columnNames) {
        JDialog resultsDialog = new JDialog(this, "Search Results", true);
        resultsDialog.setSize(800, 500);
        resultsDialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(PANEL_COLOR);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(240, 240, 255));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 245, 250));
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JButton closeButton = createStyledButton("Close", null);
        closeButton.addActionListener(e -> resultsDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        resultsDialog.setContentPane(panel);
        resultsDialog.setVisible(true);
    }
    
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "About", true);
        aboutDialog.setSize(400, 300);
        aboutDialog.setLocationRelativeTo(this);
        JPanel aboutPanel = new JPanel(new BorderLayout(20, 20));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        aboutPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Event Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        versionLabel.setHorizontalAlignment(JLabel.CENTER);
        versionLabel.setForeground(Color.GRAY);
        JTextArea descArea = new JTextArea(
                "This application provides a comprehensive solution for managing club events, " +
                "members, and activities. It was developed as part of a Java MVC architecture project."
        );
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        JButton closeButton = createStyledButton("Close", null);
        closeButton.addActionListener(e -> aboutDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(versionLabel, BorderLayout.CENTER);
        aboutPanel.add(headerPanel, BorderLayout.NORTH);
        aboutPanel.add(descArea, BorderLayout.CENTER);
        aboutPanel.add(buttonPanel, BorderLayout.SOUTH);
        aboutDialog.setContentPane(aboutPanel);
        aboutDialog.setVisible(true);
    }
    
    private class ComboItem {
        private int value;
        private String label;
        
        public ComboItem(int value, String label) {
            this.value = value;
            this.label = label;
        }
        
        public int getValue() {
            return value;
        }
        
        public String getLabel() {
            return label;
        }
        
        @Override
        public String toString() {
            return label;
        }
    }
    
    private JPanel createClubsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Club Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        JLabel subtitleLabel = new JLabel("Create and manage clubs");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setOpaque(false);
        JButton addButton = createStyledButton("Add Club", null);
        JButton editButton = createStyledButton("Edit Club", null);
        JButton deleteButton = createStyledButton("Delete Club", null);
        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(titlePanel, BorderLayout.WEST);
        northPanel.add(actionPanel, BorderLayout.EAST);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(PANEL_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        String[] columnNames = {"ID", "Club Name", "Description", "Members"};
        Object[][] data = getClubData();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(240, 240, 255));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 245, 250));
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        addButton.addActionListener(e -> openClubDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int clubId = (Integer) table.getValueAt(selectedRow, 0);
                Club club = clubService.findById(clubId);
                openClubDialog(club);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Please select a club to edit", 
                        "No Selection", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int clubId = (Integer) table.getValueAt(selectedRow, 0);
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this club?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        clubService.delete(clubId);
                        refreshClubsPanel();
                        statusLabel.setText("Club deleted successfully");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Error deleting club: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Please select a club to delete", 
                        "No Selection", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return panel;
    }
    
    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        if (iconPath != null && !iconPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            if (icon.getIconWidth() != -1) {
                button.setIcon(icon);
            }
        }
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 105, 92), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        return button;
    }
    
    private Object[][] getClubData() {
        List<Club> clubs = clubService.findAll();
        Object[][] data = new Object[clubs.size()][4];
        for (int i = 0; i < clubs.size(); i++) {
            Club club = clubs.get(i);
            data[i][0] = club.getClubId();
            data[i][1] = club.getClubName();
            data[i][2] = club.getClubDescription();
            data[i][3] = memberService.findMembersByClub(club.getClubId()).size();
        }
        return data;
    }
    
    private void openClubDialog(Club club) {
        String title = (club == null) ? "Add New Club" : "Edit Club";
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        JPanel formPanel = new JPanel(new BorderLayout(20, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        fieldsPanel.setOpaque(false);
        JLabel idLabel = new JLabel("Club ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (club != null) {
            idField.setText(String.valueOf(club.getClubId()));
            idField.setEditable(false);
        }
        JLabel nameLabel = new JLabel("Club Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (club != null) {
            nameField.setText(club.getClubName());
        }
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextArea descArea = new JTextArea();
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        if (club != null) {
            descArea.setText(club.getClubDescription());
        }
        fieldsPanel.add(idLabel);
        fieldsPanel.add(idField);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameField);
        fieldsPanel.add(descLabel);
        fieldsPanel.add(descScroll);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String description = descArea.getText();
                Club clubObj = (club == null) ? new Club() : club;
                clubObj.setClubId(id);
                clubObj.setClubName(name);
                clubObj.setClubDescription(description);
                if (!clubObj.isValid()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Please provide valid club information",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (club == null) {
                    clubService.save(clubObj);
                    statusLabel.setText("Club created successfully");
                } else {
                    clubService.update(clubObj);
                    statusLabel.setText("Club updated successfully");
                }
                refreshClubsPanel();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Please enter a valid Club ID (numeric value)",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error saving club: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setContentPane(formPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Member Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        JLabel subtitleLabel = new JLabel("Manage club members");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setOpaque(false);
        JButton addButton = createStyledButton("Add Member", null);
        JButton editButton = createStyledButton("Edit Member", null);
        JButton deleteButton = createStyledButton("Delete Member", null);
        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(titlePanel, BorderLayout.WEST);
        northPanel.add(actionPanel, BorderLayout.EAST);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(PANEL_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        String[] columnNames = {"ID", "Name", "Email", "Phone", "Join Date", "Club"};
        Object[][] data = getMemberData();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(240, 240, 255));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 245, 250));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        addButton.addActionListener(e -> openMemberDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int memberId = (Integer) table.getValueAt(selectedRow, 0);
                Member member = memberService.findById(memberId);
                openMemberDialog(member);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Please select a member to edit", 
                        "No Selection", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int memberId = (Integer) table.getValueAt(selectedRow, 0);
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this member?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        memberService.delete(memberId);
                        refreshMembersPanel();
                        refreshClubsPanel();
                        statusLabel.setText("Member deleted successfully");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Error deleting member: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Please select a member to delete", 
                        "No Selection", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return panel;
    }
    
    private Object[][] getMemberData() {
        List<Member> members = memberService.findAll();
        Object[][] data = new Object[members.size()][6];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            data[i][0] = member.getMemberId();
            data[i][1] = member.getMemberName();
            data[i][2] = member.getEmail();
            data[i][3] = member.getPhone();
            data[i][4] = member.getJoinDate() != null ? dateFormat.format(member.getJoinDate()) : "";
            Club club = clubService.findById(member.getClubId());
            data[i][5] = club != null ? club.getClubName() : "";
        }
        return data;
    }
    
    private void openMemberDialog(Member member) {
        String title = (member == null) ? "Add New Member" : "Edit Member";
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        JPanel formPanel = new JPanel(new BorderLayout(20, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        fieldsPanel.setOpaque(false);
        JLabel idLabel = new JLabel("Member ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (member != null) {
            idField.setText(String.valueOf(member.getMemberId()));
            idField.setEditable(false);
        }
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (member != null) {
            nameField.setText(member.getMemberName());
        }
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (member != null) {
            emailField.setText(member.getEmail());
        }
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (member != null && member.getPhone() != null) {
            phoneField.setText(member.getPhone());
        }
        JLabel joinDateLabel = new JLabel("Join Date:");
        joinDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField joinDateField = new JTextField();
        joinDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (member != null && member.getJoinDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            joinDateField.setText(sdf.format(member.getJoinDate()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            joinDateField.setText(sdf.format(new Date()));
        }
        JLabel clubLabel = new JLabel("Club:");
        clubLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<ComboItem> clubComboBox = new JComboBox<>();
        List<Club> clubs = clubService.findAll();
        for (Club club : clubs) {
            clubComboBox.addItem(new ComboItem(club.getClubId(), club.getClubName()));
        }
        if (member != null) {
            for (int i = 0; i < clubComboBox.getItemCount(); i++) {
                ComboItem item = clubComboBox.getItemAt(i);
                if (item.getValue() == member.getClubId()) {
                    clubComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        fieldsPanel.add(idLabel);
        fieldsPanel.add(idField);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameField);
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(emailField);
        fieldsPanel.add(phoneLabel);
        fieldsPanel.add(phoneField);
        fieldsPanel.add(joinDateLabel);
        fieldsPanel.add(joinDateField);
        fieldsPanel.add(clubLabel);
        fieldsPanel.add(clubComboBox);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton saveButton = createStyledButton("Save", null);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String joinDateStr = joinDateField.getText();
                if (!ValidationUtils.isValidDate(joinDateStr)) {
                    JOptionPane.showMessageDialog(dialog,
                            "Please enter a valid date in format YYYY-MM-DD",
                            "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Date joinDate = ValidationUtils.parseDate(joinDateStr);
                ComboItem selectedClub = (ComboItem) clubComboBox.getSelectedItem();
                int clubId = selectedClub.getValue();
                Member memberObj = (member == null) ? new Member() : member;
                memberObj.setMemberId(id);
                memberObj.setMemberName(name);
                memberObj.setEmail(email);
                memberObj.setPhone(phone);
                memberObj.setJoinDate(joinDate);
                memberObj.setClubId(clubId);
                if (!memberObj.isValid()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Please provide valid member information",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (member == null) {
                    memberService.save(memberObj);
                    statusLabel.setText("Member created successfully");
                } else {
                    memberService.update(memberObj);
                    statusLabel.setText("Member updated successfully");
                }
                refreshMembersPanel();
                refreshClubsPanel();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error saving member: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setContentPane(formPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Event Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        JLabel subtitleLabel = new JLabel("Schedule and manage events");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setOpaque(false);
        JButton addButton = createStyledButton("Add Event", null);
        JButton editButton = createStyledButton("Edit Event", null);
        JButton deleteButton = createStyledButton("Delete Event", null);
        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(titlePanel, BorderLayout.WEST);
        northPanel.add(actionPanel, BorderLayout.EAST);
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtersPanel.setOpaque(false);
        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"All Events", "Upcoming Events", "Past Events"});
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel clubFilterLabel = new JLabel("Club:");
        clubFilterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<ComboItem> clubFilterComboBox = new JComboBox<>();
        clubFilterComboBox.addItem(new ComboItem(0, "All Clubs"));
        List<Club> clubs = clubService.findAll();
        for (Club club : clubs) {
            clubFilterComboBox.addItem(new ComboItem(club.getClubId(), club.getClubName()));
        }
        JButton applyFilterButton = createStyledButton("Apply", null);
        applyFilterButton.setPreferredSize(new Dimension(80, 30));
        applyFilterButton.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            ComboItem selectedClub = (ComboItem) clubFilterComboBox.getSelectedItem();
            List<Event> filteredEvents = new ArrayList<>();
            if ("Upcoming Events".equals(selectedFilter)) {
                filteredEvents = eventService.findUpcomingEvents();
            } else if ("Past Events".equals(selectedFilter)) {
                List<Event> allEvents = eventService.findAll();
                Date currentDate = new Date();
                for (Event event : allEvents) {
                    if (event.getEventDate() != null && event.getEventDate().before(currentDate)) {
                        filteredEvents.add(event);
                    }
                }
            } else {
                filteredEvents = eventService.findAll();
            }
            if (selectedClub.getValue() != 0) {
                List<Event> clubFilteredEvents = new ArrayList<>();
                for (Event event : filteredEvents) {
                    if (event.getClubId() == selectedClub.getValue()) {
                        clubFilteredEvents.add(event);
                    }
                }
                filteredEvents = clubFilteredEvents;
            }
            updateEventsTable(panel, filteredEvents);
            statusLabel.setText("Events filtered: " + filteredEvents.size() + " events found");
        });
        filtersPanel.add(filterLabel);
        filtersPanel.add(filterComboBox);
        filtersPanel.add(clubFilterLabel);
        filtersPanel.add(clubFilterComboBox);
        filtersPanel.add(applyFilterButton);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(PANEL_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        tablePanel.add(filtersPanel, BorderLayout.NORTH);
        String[] columnNames = {"ID", "Event Name", "Description", "Club", "Date", "Venue"};
        Object[][] data = getEventData();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(240, 240, 255));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 245, 250));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        addButton.addActionListener(e -> openEventDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int eventId = (Integer) table.getValueAt(selectedRow, 0);
                Event event = eventService.findById(eventId);
                openEventDialog(event);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Please select an event to edit", 
                        "No Selection", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int eventId = (Integer) table.getValueAt(selectedRow, 0);
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this event?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        eventService.delete(eventId);
                        refreshEventsPanel();
                        statusLabel.setText("Event deleted successfully");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Error deleting event: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Please select an event to delete", 
                        "No Selection", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return panel;
    }
    
    private void updateEventsTable(JPanel panel, List<Event> filteredEvents) {
        Component[] components = panel.getComponents();
        for (Component c : components) {
            if (c instanceof JPanel && ((JPanel)c).getComponentCount() > 0) {
                Component[] subComponents = ((JPanel)c).getComponents();
                for (Component sc : subComponents) {
                    if (sc instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) sc;
                        if (scrollPane.getViewport().getView() instanceof JTable) {
                            JTable table = (JTable) scrollPane.getViewport().getView();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Object[][] data = new Object[filteredEvents.size()][6];
                            for (int i = 0; i < filteredEvents.size(); i++) {
                                Event event = filteredEvents.get(i);
                                data[i][0] = event.getEventId();
                                data[i][1] = event.getEventName();
                                data[i][2] = event.getDescription();
                                Club club = clubService.findById(event.getClubId());
                                data[i][3] = club != null ? club.getClubName() : "";
                                data[i][4] = event.getEventDate() != null ? dateFormat.format(event.getEventDate()) : "";
                                Venue venue = venueService.findById(event.getVenueId());
                                data[i][5] = venue != null ? venue.getVenueName() : String.valueOf(event.getVenueId());
                            }
                            DefaultTableModel model = (DefaultTableModel) table.getModel();
                            model.setRowCount(0);
                            for (Object[] row : data) {
                                model.addRow(row);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void openEventDialog(Event event) {
        String title = (event == null) ? "Add New Event" : "Edit Event";
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        JPanel formPanel = new JPanel(new BorderLayout(20, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        fieldsPanel.setOpaque(false);
        JLabel idLabel = new JLabel("Event ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (event != null) {
            idField.setText(String.valueOf(event.getEventId()));
            idField.setEditable(false);
        }
        JLabel nameLabel = new JLabel("Event Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (event != null) {
            nameField.setText(event.getEventName());
        }
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextArea descArea = new JTextArea();
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        if (event != null && event.getDescription() != null) {
            descArea.setText(event.getDescription());
        }
        JLabel dateLabel = new JLabel("Event Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (event != null && event.getEventDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateField.setText(sdf.format(event.getEventDate()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateField.setText(sdf.format(new Date()));
        }
        JLabel clubLabel = new JLabel("Club:");
        clubLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<ComboItem> clubComboBox = new JComboBox<>();
        for (Club club : clubService.findAll()) {
            clubComboBox.addItem(new ComboItem(club.getClubId(), club.getClubName()));
        }
        if (event != null) {
            for (int i = 0; i < clubComboBox.getItemCount(); i++) {
                ComboItem item = clubComboBox.getItemAt(i);
                if (item.getValue() == event.getClubId()) {
                    clubComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        JLabel venueLabel = new JLabel("Venue:");
        venueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<ComboItem> venueComboBox = new JComboBox<>();
        List<Venue> venues = venueService.findAll();
        for (Venue venue : venues) {
            venueComboBox.addItem(new ComboItem(venue.getVenueId(), venue.getVenueName()));
        }
        if (event != null) {
            for (int i = 0; i < venueComboBox.getItemCount(); i++) {
                ComboItem item = venueComboBox.getItemAt(i);
                if (item.getValue() == event.getVenueId()) {
                    venueComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        fieldsPanel.add(idLabel);
        fieldsPanel.add(idField);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameField);
        fieldsPanel.add(clubLabel);
        fieldsPanel.add(clubComboBox);
        fieldsPanel.add(dateLabel);
        fieldsPanel.add(dateField);
        fieldsPanel.add(venueLabel);
        fieldsPanel.add(venueComboBox);
        fieldsPanel.add(descLabel);
        fieldsPanel.add(descScroll);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton saveButton = createStyledButton("Save", null);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        formPanel.add(titleLabel, BorderLayout.NORTH);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String description = descArea.getText();
                String eventDateStr = dateField.getText();
                if (!ValidationUtils.isValidDate(eventDateStr)) {
                    JOptionPane.showMessageDialog(dialog,
                            "Please enter a valid date in format YYYY-MM-DD",
                            "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Date eventDate = ValidationUtils.parseDate(eventDateStr);
                ComboItem selectedClub = (ComboItem) clubComboBox.getSelectedItem();
                int clubId = selectedClub.getValue();
                ComboItem selectedVenue = (ComboItem) venueComboBox.getSelectedItem();
                int venueId = selectedVenue.getValue();
                Event eventObj = (event == null) ? new Event() : event;
                eventObj.setEventId(id);
                eventObj.setEventName(name);
                eventObj.setDescription(description);
                eventObj.setEventDate(eventDate);
                eventObj.setVenueId(venueId);
                eventObj.setClubId(clubId);
                if (!eventObj.isValid()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Please provide valid event information",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (event == null) {
                    eventService.save(eventObj);
                    statusLabel.setText("Event created successfully");
                } else {
                    eventService.update(eventObj);
                    statusLabel.setText("Event updated successfully");
                }
                refreshEventsPanel();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error saving event: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setContentPane(formPanel);
        dialog.setVisible(true);
    }
    
    private Object[][] getEventData() {
        List<Event> events = eventService.findAll();
        Object[][] data = new Object[events.size()][6];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            data[i][0] = event.getEventId();
            data[i][1] = event.getEventName();
            data[i][2] = event.getDescription();
            Club club = clubService.findById(event.getClubId());
            data[i][3] = club != null ? club.getClubName() : "";
            data[i][4] = event.getEventDate() != null ? dateFormat.format(event.getEventDate()) : "";
            Venue venue = venueService.findById(event.getVenueId());
            data[i][5] = venue != null ? venue.getVenueName() : String.valueOf(event.getVenueId());
        }
        return data;
    }
    
    private void refreshAllPanels() {
        refreshClubsPanel();
        refreshMembersPanel();
        refreshEventsPanel();
        statusLabel.setText("All panels refreshed");
    }
    
    private void refreshClubsPanel() {
        int index = tabbedPane.indexOfComponent(tabbedPane.getComponentAt(0));
        if (index != -1) {
            clubsPanel = createClubsPanel();
            tabbedPane.setComponentAt(index, createScrollPane(clubsPanel));
            refreshAllDashboards();
        }
    }
    
    private void refreshMembersPanel() {
        int index = tabbedPane.indexOfComponent(tabbedPane.getComponentAt(1));
        if (index != -1) {
            membersPanel = createMembersPanel();
            tabbedPane.setComponentAt(index, createScrollPane(membersPanel));
            refreshAllDashboards();
        }
    }
    
    private void refreshEventsPanel() {
        int index = tabbedPane.indexOfComponent(tabbedPane.getComponentAt(2));
        if (index != -1) {
            eventsPanel = createEventsPanel();
            tabbedPane.setComponentAt(index, createScrollPane(eventsPanel));
            refreshAllDashboards();
        }
    }
    
    private void refreshAllDashboards() {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof Container) {
                findAndRefreshDashboards((Container) comp);
            }
        }
    }
    
    private void findAndRefreshDashboards(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof BaseDashboardPanel) {
                ((BaseDashboardPanel) comp).refreshData();
            } else if (comp instanceof Container) {
                findAndRefreshDashboards((Container) comp);
            }
        }
    }
}
