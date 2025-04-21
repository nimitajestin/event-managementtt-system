package service;

import db.DBConnection;
import exception.DatabaseException;
import exception.ValidationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Event;

public class EventService implements CrudService<Event, Integer> {
    
    @Override
    public Event findById(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events WHERE event_id = ?")) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setEventName(rs.getString("event_name"));
                event.setDescription(rs.getString("description"));
                event.setClubId(rs.getInt("club_id"));
                event.setEventDate(rs.getDate("event_date"));
                event.setVenueId(rs.getInt("venue_id"));
                return event;
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding event by ID: " + id, e);
        }
    }
    
    @Override
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Events")) {
            
            while (rs.next()) {
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setEventName(rs.getString("event_name"));
                event.setDescription(rs.getString("description"));
                event.setClubId(rs.getInt("club_id"));
                event.setEventDate(rs.getDate("event_date"));
                event.setVenueId(rs.getInt("venue_id"));
                events.add(event);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all events", e);
        }
        return events;
    }
    
    @Override
    public Event save(Event event) {
        if (!event.isValid()) {
            throw new ValidationException("Invalid event data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Events (event_id, event_name, description, club_id, event_date, venue_id) VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, event.getEventId());
            stmt.setString(2, event.getEventName());
            stmt.setString(3, event.getDescription());
            stmt.setInt(4, event.getClubId());
            stmt.setDate(5, new java.sql.Date(event.getEventDate().getTime()));
            stmt.setInt(6, event.getVenueId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating event failed, no rows affected.");
            }
            
            return event;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving event", e);
        }
    }
    
    @Override
    public boolean update(Event event) {
        if (!event.isValid()) {
            throw new ValidationException("Invalid event data");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Events SET event_name = ?, description = ?, club_id = ?, event_date = ?, venue_id = ? WHERE event_id = ?")) {
            
            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getDescription());
            stmt.setInt(3, event.getClubId());
            stmt.setDate(4, new java.sql.Date(event.getEventDate().getTime()));
            stmt.setInt(5, event.getVenueId());
            stmt.setInt(6, event.getEventId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating event", e);
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Events WHERE event_id = ?")) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting event", e);
        }
    }
    
    // Additional methods specific to events
    public List<Event> findEventsByClub(int clubId) {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events WHERE club_id = ?")) {
            
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setEventName(rs.getString("event_name"));
                event.setDescription(rs.getString("description"));
                event.setClubId(rs.getInt("club_id"));
                event.setEventDate(rs.getDate("event_date"));
                event.setVenueId(rs.getInt("venue_id"));
                events.add(event);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding events by club", e);
        }
        return events;
    }
    
    public List<Event> findUpcomingEvents() {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events WHERE event_date >= CURDATE() ORDER BY event_date ASC")) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setEventName(rs.getString("event_name"));
                event.setDescription(rs.getString("description"));
                event.setClubId(rs.getInt("club_id"));
                event.setEventDate(rs.getDate("event_date"));
                event.setVenueId(rs.getInt("venue_id"));
                events.add(event);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding upcoming events", e);
        }
        return events;
    }
}
