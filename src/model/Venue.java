package model;

import utils.ValidationUtils;

public class Venue extends BaseModel {
    private int venueId;
    private String venueName;
    private String location;
    
    public Venue() {
    }
    
    public Venue(int venueId, String venueName, String location) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.location = location;
    }
    
    // Getters and setters
    public int getVenueId() {
        return venueId;
    }
    
    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }
    
    public String getVenueName() {
        return venueName;
    }
    
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    @Override
    public boolean isValid() {
        return venueId > 0 && 
               ValidationUtils.isNotEmpty(venueName);
    }
    
    @Override
    public String toString() {
        return "Venue{" +
                "venueId=" + venueId +
                ", venueName='" + venueName + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
