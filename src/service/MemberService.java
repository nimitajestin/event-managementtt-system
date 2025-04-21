package service;

import db.DBConnection;
import exception.DatabaseException;
import exception.ValidationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Member;

public class MemberService implements CrudService<Member, Integer> {
    
    @Override
    public Member findById(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ClubMembers WHERE member_id = ?")) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setEmail(rs.getString("email"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setClubId(rs.getInt("club_id"));
                member.setPhone(rs.getString("phone")); 
                return member;
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding member by ID: " + id, e);
        }
    }
    
    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ClubMembers")) {
            
            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setEmail(rs.getString("email"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setClubId(rs.getInt("club_id"));
                member.setPhone(rs.getString("phone")); // Added phone field
                members.add(member);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all members", e);
        }
        return members;
    }
    
    @Override
    public Member save(Member member) {
        if (!member.isValid()) {
            throw new ValidationException("Invalid member data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO ClubMembers (member_id, member_name, email, join_date, club_id, phone) VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, member.getMemberId());
            stmt.setString(2, member.getMemberName());
            stmt.setString(3, member.getEmail());
            stmt.setDate(4, new java.sql.Date(member.getJoinDate().getTime()));
            stmt.setInt(5, member.getClubId());
            stmt.setString(6, member.getPhone()); // Added phone field
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating member failed, no rows affected.");
            }
            
            return member;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving member", e);
        }
    }
    
    @Override
    public boolean update(Member member) {
        if (!member.isValid()) {
            throw new ValidationException("Invalid member data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE ClubMembers SET member_name = ?, email = ?, join_date = ?, club_id = ?, phone = ? WHERE member_id = ?")) {
            
            stmt.setString(1, member.getMemberName());
            stmt.setString(2, member.getEmail());
            stmt.setDate(3, new java.sql.Date(member.getJoinDate().getTime()));
            stmt.setInt(4, member.getClubId());
            stmt.setString(5, member.getPhone()); // Added phone field
            stmt.setInt(6, member.getMemberId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating member", e);
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM ClubMembers WHERE member_id = ?")) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting member", e);
        }
    }
    
    public List<Member> findMembersByClub(int clubId) {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ClubMembers WHERE club_id = ?")) {
            
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setEmail(rs.getString("email"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setClubId(rs.getInt("club_id"));
                member.setPhone(rs.getString("phone")); // Added phone field
                members.add(member);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding members by club", e);
        }
        return members;
    }
}
