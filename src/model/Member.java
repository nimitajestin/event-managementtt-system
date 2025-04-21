package model;

import java.util.Date;
import utils.Displayable;
import utils.ValidationUtils;

public class Member extends BaseModel implements Displayable {
    private int memberId;
    private String memberName;
    private String email;
    private Date joinDate;
    private int clubId;
    private String phone;  
    
    public Member() {
    }
    
    public Member(int memberId, String memberName, String email, Date joinDate, int clubId, String phone) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.joinDate = joinDate;
        this.clubId = clubId;
        this.phone = phone;
    }
    
    // Getters and setters
    public int getMemberId() {
        return memberId;
    }
    
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    
    public String getMemberName() {
        return memberName;
    }
    
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Date getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
    
    public int getClubId() {
        return clubId;
    }
    
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public boolean isValid() {
        return memberId > 0 && 
               ValidationUtils.isNotEmpty(memberName) && 
               ValidationUtils.isValidEmail(email) &&
               (phone == null || phone.isEmpty() || ValidationUtils.isValidPhone(phone));
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", email='" + email + '\'' +
                ", joinDate=" + joinDate +
                ", clubId=" + clubId +
                ", phone='" + phone + '\'' +
                '}';
    }
    
    @Override
    public String toPrintableString() {
        return String.format("Member: %d - %s (%s)", memberId, memberName, email);
    }
    
    @Override
    public String getDisplayName() {
        return memberName;
    }
    
    @Override
    public String getDisplayDescription() {
        return email;
    }
}
