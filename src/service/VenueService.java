package service;

import db.DBConnection;
import exception.DatabaseException;
import exception.ValidationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Venue;

public class VenueService implements CrudService<Venue, Integer> {
    
    @Override
    public Venue findById(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Venues WHERE venue_id = ?")) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Venue venue = new Venue();
                venue.setVenueId(rs.getInt("venue_id"));
                venue.setVenueName(rs.getString("venue_name"));
                venue.setLocation(rs.getString("location"));
                return venue;
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding venue by ID: " + id, e);
        }
    }
    
    @Override
    public List<Venue> findAll() {
        List<Venue> venues = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Venues")) {
            
            while (rs.next()) {
                Venue venue = new Venue();
                venue.setVenueId(rs.getInt("venue_id"));
                venue.setVenueName(rs.getString("venue_name"));
                venue.setLocation(rs.getString("location"));
                venues.add(venue);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all venues", e);
        }
        return venues;
    }
    
    @Override
    public Venue save(Venue venue) {
        if (!venue.isValid()) {
            throw new ValidationException("Invalid venue data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Venues (venue_id, venue_name, location) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, venue.getVenueId());
            stmt.setString(2, venue.getVenueName());
            stmt.setString(3, venue.getLocation());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating venue failed, no rows affected.");
            }
            
            return venue;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving venue", e);
        }
    }
    
    @Override
    public boolean update(Venue venue) {
        if (!venue.isValid()) {
            throw new ValidationException("Invalid venue data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Venues SET venue_name = ?, location = ? WHERE venue_id = ?")) {
            
            stmt.setString(1, venue.getVenueName());
            stmt.setString(2, venue.getLocation());
            stmt.setInt(3, venue.getVenueId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating venue", e);
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Venues WHERE venue_id = ?")) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting venue", e);
        }
    }
}
