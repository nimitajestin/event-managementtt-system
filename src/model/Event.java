package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import utils.Displayable;
import utils.ValidationUtils;

public class Event extends BaseModel implements Displayable {
    private int eventId;
    private String eventName;
    private String description;
    private int clubId;
    private Date eventDate;
    private int venueId;
    
    public Event() {
    }
    
    public Event(int eventId, String eventName, String description, int clubId, Date eventDate, int venueId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.clubId = clubId;
        this.eventDate = eventDate;
        this.venueId = venueId;
    }
    
    public int getEventId() {
        return eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getClubId() {
        return clubId;
    }
    
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    
    public Date getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    
    public int getVenueId() {
        return venueId;
    }
    
    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }
    
    @Override
    public boolean isValid() {
        return eventId > 0 && 
               ValidationUtils.isNotEmpty(eventName) && 
               clubId > 0 && 
               eventDate != null;
    }
    
    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", description='" + description + '\'' +
                ", clubId=" + clubId +
                ", eventDate=" + eventDate +
                ", venueId=" + venueId +
                '}';
    }
    
    @Override
    public String toPrintableString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = eventDate != null ? sdf.format(eventDate) : "N/A";
        return String.format("Event: %d - %s (Date: %s)", eventId, eventName, dateStr);
    }
    
    @Override
    public String getDisplayName() {
        return eventName;
    }
    
    @Override
    public String getDisplayDescription() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = eventDate != null ? sdf.format(eventDate) : "No date";
        return description != null ? description + " (" + dateStr + ")" : "Event on " + dateStr;
    }
}
