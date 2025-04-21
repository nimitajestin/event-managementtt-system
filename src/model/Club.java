package model;

import utils.Displayable;
import utils.ValidationUtils;

public class Club extends BaseModel implements Displayable {
    private int clubId;
    private String clubName;
    private String clubDescription;
    
    public Club() {
    }
    
    public Club(int clubId, String clubName, String clubDescription) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubDescription = clubDescription;
    }
    
    public int getClubId() {
        return clubId;
    }
    
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    
    public String getClubName() {
        return clubName;
    }
    
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
    
    public String getClubDescription() {
        return clubDescription;
    }
    
    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }
    
    @Override
    public boolean isValid() {
        return clubId > 0 && 
               ValidationUtils.isNotEmpty(clubName);
    }
    
    @Override
    public String toString() {
        return "Club{" +
                "clubId=" + clubId +
                ", clubName='" + clubName + '\'' +
                ", clubDescription='" + clubDescription + '\'' +
                '}';
    }
    
    @Override
    public String toPrintableString() {
        return String.format("Club: %d - %s", clubId, clubName);
    }
    
    @Override
    public String getDisplayName() {
        return clubName;
    }
    
    @Override
    public String getDisplayDescription() {
        return clubDescription != null ? clubDescription : "No description available";
    }
}
