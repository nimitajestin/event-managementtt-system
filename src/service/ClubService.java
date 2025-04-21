package service;

import db.DBConnection;
import exception.DatabaseException;
import exception.ValidationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Club;

public class ClubService implements CrudService<Club, Integer> {
    
    @Override
    public Club findById(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Clubs WHERE club_id = ?")) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Club club = new Club();
                club.setClubId(rs.getInt("club_id"));
                club.setClubName(rs.getString("club_name"));
                club.setClubDescription(rs.getString("club_description"));
                return club;
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding club by ID: " + id, e);
        }
    }
    
    @Override
    public List<Club> findAll() {
        List<Club> clubs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Clubs")) {
            
            while (rs.next()) {
                Club club = new Club();
                club.setClubId(rs.getInt("club_id"));
                club.setClubName(rs.getString("club_name"));
                club.setClubDescription(rs.getString("club_description"));
                clubs.add(club);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all clubs", e);
        }
        return clubs;
    }
    
    @Override
    public Club save(Club club) {
        if (!club.isValid()) {
            throw new ValidationException("Invalid club data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Clubs (club_id, club_name, club_description) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, club.getClubId());
            stmt.setString(2, club.getClubName());
            stmt.setString(3, club.getClubDescription());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating club failed, no rows affected.");
            }
            
            return club;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving club", e);
        }
    }
    
    @Override
    public boolean update(Club club) {
        if (!club.isValid()) {
            throw new ValidationException("Invalid club data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Clubs SET club_name = ?, club_description = ? WHERE club_id = ?")) {
            
            stmt.setString(1, club.getClubName());
            stmt.setString(2, club.getClubDescription());
            stmt.setInt(3, club.getClubId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating club", e);
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Clubs WHERE club_id = ?")) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting club", e);
        }
    }
}
