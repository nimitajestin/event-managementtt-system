package ui.components;

import java.awt.*;
import javax.swing.*;
import model.Club;
import model.Event;
import model.Member;
import service.ClubService;
import service.EventService;
import service.MemberService;
import utils.Printable;

/**
 * Component that displays statistics about clubs, members, and events.
 */
public class StatsComponent extends BaseComponent {
    
    private ClubService clubService;
    private MemberService memberService;
    private EventService eventService;
    
    private JLabel clubsLabel;
    private JLabel membersLabel;
    private JLabel eventsLabel;
    
    public StatsComponent(String title, Color backgroundColor, 
                          ClubService clubService, 
                          MemberService memberService, 
                          EventService eventService) {
        super(title, backgroundColor);
        this.clubService = clubService;
        this.memberService = memberService;
        this.eventService = eventService;
        
        initializeUI();
        updateStats();
    }
    
    private void initializeUI() {
        setLayout(new GridLayout(4, 1, 5, 5));
        setBorder(BorderFactory.createTitledBorder(getTitle()));
        
        clubsLabel = new JLabel("Clubs: 0");
        membersLabel = new JLabel("Members: 0");
        eventsLabel = new JLabel("Events: 0");
        
        add(clubsLabel);
        add(membersLabel);
        add(eventsLabel);
    }
    
    public void updateStats() {
        int clubCount = clubService.findAll().size();
        int memberCount = memberService.findAll().size();
        int eventCount = eventService.findAll().size();
        
        clubsLabel.setText("Clubs: " + clubCount);
        membersLabel.setText("Members: " + memberCount);
        eventsLabel.setText("Events: " + eventCount);
        
        repaint();
    }
    
    @Override
    public String toPrintableString() {
        return String.format("Statistics - Clubs: %d, Members: %d, Events: %d", 
                clubService.findAll().size(),
                memberService.findAll().size(),
                eventService.findAll().size());
    }
}
