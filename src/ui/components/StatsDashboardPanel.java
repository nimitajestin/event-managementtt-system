package ui.components;

import java.awt.*;
import javax.swing.*;
import service.ClubService;
import service.EventService;
import service.MemberService;
import utils.Displayable;
import java.util.List;
import model.Club;
import model.Event;
import model.Member;

/**
 * Dashboard panel to display system statistics.
 * Demonstrates inheritance by extending BaseDashboardPanel.
 */
public class StatsDashboardPanel extends BaseDashboardPanel {
    private ClubService clubService;
    private MemberService memberService;
    private EventService eventService;
    
    private JLabel clubsLabel;
    private JLabel membersLabel;
    private JLabel eventsLabel;
    private JLabel lastActivityLabel;
    
    private JList<Displayable> recentItemsList;
    private DefaultListModel<Displayable> listModel;
    
    public StatsDashboardPanel(String title, Color backgroundColor, Color accentColor,
                               ClubService clubService, MemberService memberService, EventService eventService) {
        super(title, backgroundColor, accentColor);
        this.clubService = clubService;
        this.memberService = memberService;
        this.eventService = eventService;
        
        refreshData();
    }
    
    @Override
    protected JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        
        // Create stats panel
        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 5, 10));
        statsPanel.setOpaque(false);
        
        clubsLabel = new JLabel("Clubs: 0");
        membersLabel = new JLabel("Members: 0");
        eventsLabel = new JLabel("Events: 0");
        lastActivityLabel = new JLabel("Last updated: Never");
        
        Font statsFont = new Font("Segoe UI", Font.PLAIN, 14);
        clubsLabel.setFont(statsFont);
        membersLabel.setFont(statsFont);
        eventsLabel.setFont(statsFont);
        lastActivityLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        statsPanel.add(clubsLabel);
        statsPanel.add(membersLabel);
        statsPanel.add(eventsLabel);
        statsPanel.add(lastActivityLabel);
        
        // Create recent items panel
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setOpaque(false);
        
        JLabel recentLabel = new JLabel("Recent Items");
        recentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        listModel = new DefaultListModel<>();
        recentItemsList = new JList<>(listModel);
        recentItemsList.setCellRenderer(new DisplayableListCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(recentItemsList);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        
        recentPanel.add(recentLabel, BorderLayout.NORTH);
        recentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add everything to content panel
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(recentPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    @Override
    public void refreshData() {
        // Update stats
        int clubCount = clubService.findAll().size();
        int memberCount = memberService.findAll().size();
        int eventCount = eventService.findAll().size();
        
        clubsLabel.setText("Clubs: " + clubCount);
        membersLabel.setText("Members: " + memberCount);
        eventsLabel.setText("Events: " + eventCount);
        
        // Update recent items
        listModel.clear();
        
        // Add recent clubs (polymorphism: treating different types as Displayable)
        List<Club> clubs = clubService.findAll();
        if (!clubs.isEmpty()) {
            listModel.addElement(clubs.get(clubs.size() - 1));
        }
        
        // Add recent members
        List<Member> members = memberService.findAll();
        if (!members.isEmpty()) {
            listModel.addElement(members.get(members.size() - 1));
        }
        
        // Add recent events
        List<Event> events = eventService.findAll();
        if (!events.isEmpty()) {
            listModel.addElement(events.get(events.size() - 1));
        }
        
        // Update timestamp
        lastActivityLabel.setText("Last updated: " + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));
        
        repaint();
    }
    
    /**
     * Custom cell renderer for Displayable objects - demonstrates polymorphism
     */
    private class DisplayableListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                     int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Displayable) {
                Displayable item = (Displayable) value;
                label.setText(item.getDisplayName());
                label.setToolTipText(item.getDisplayDescription());
                
                // Set different icons based on type - demonstrates polymorphism
                if (value instanceof Club) {
                    label.setText("🏢 " + label.getText());
                } else if (value instanceof Member) {
                    label.setText("👤 " + label.getText());
                } else if (value instanceof Event) {
                    label.setText("📅 " + label.getText());
                }
            }
            
            return label;
        }
    }
}
