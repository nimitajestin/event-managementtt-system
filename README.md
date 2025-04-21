# Event Management System

A comprehensive Java Swing application for managing club events, members, and venues using an MVC architecture with JDBC database connectivity.

## Features

- **Club Management**: Create, update, view, and delete clubs
- **Member Management**: Manage club members with validation for emails and phone numbers
- **Event Planning**: Schedule and track events with venue selection
- **Advanced Filtering**: Filter events by date range and clubs
- **Statistics Dashboard**: Real-time display of system statistics
- **Data Validation**: Input validation for all forms
- **Search Functionality**: Search across clubs, members, and events

## Technical Implementation

### Architecture

- **Model-View-Controller (MVC)** design pattern
- **Service Layer** for business logic separation
- **DAO Pattern** for database operations

### Object-Oriented Concepts Demonstrated

- **Inheritance**:
  - BaseModel → Club/Member/Event classes
  - BaseDashboardPanel → StatsDashboardPanel
- **Interfaces**:
  - CrudService interface for database operations
  - Displayable interface for UI components
- **Polymorphism**:
  - Common treatment of different entity types
  - Type-specific behavior through interface implementations
- **Encapsulation**:
  - Private fields with getters/setters
  - Implementation details hidden behind service interfaces

### Database Connectivity

- JDBC for database operations
- SQL query execution:
  - DML operations (INSERT, UPDATE, DELETE)
  - DRL operations (SELECT with various filtering)

### Error Handling

- Custom exception hierarchy
- Specific exception types for different error categories
- Consistent UI presentation of errors

## Package Structure

```
EventManagementSystem/
├── config/
│   └── db.properties           # Database configuration
├── database/
│   └── CLUB_NORMALISED_TableCreation.sql   # Database schema
├── logs/
│   └── ems.log                 # Application logs
├── src/
│   ├── App.java                # Application entry point
│   ├── db/
│   │   └── DBConnection.java   # Database connection management
│   ├── exception/              # Custom exceptions
│   │   ├── DatabaseException.java
│   │   ├── ValidationException.java
│   │   └── ...
│   ├── model/                  # Data models
│   │   ├── BaseModel.java      # Base abstract class
│   │   ├── Club.java
│   │   ├── Member.java
│   │   ├── Event.java
│   │   └── Venue.java
│   ├── service/                # Business logic services
│   │   ├── CrudService.java    # Generic CRUD interface
│   │   ├── ClubService.java
│   │   ├── MemberService.java
│   │   ├── EventService.java
│   │   └── VenueService.java
│   ├── ui/                     # User interface components
│   │   ├── MainFrame.java      # Main application window
│   │   ├── SplashScreen.java   # Application loading screen
│   │   └── components/         # Reusable UI components
│   │       ├── BaseDashboardPanel.java  # Base panel (inheritance)
│   │       └── StatsDashboardPanel.java # Statistics dashboard
│   └── utils/                  # Utility classes
│       ├── ConfigLoader.java   # Configuration loading
│       ├── ValidationUtils.java # Input validation
│       └── Displayable.java    # UI display interface
```

## Database Schema

The application uses a normalized database schema with the following main tables:

- Clubs - Store club information
- ClubMembers - Store member details
- Events - Store event information
- Venues - Store venue information
- Plus various relationship tables for many-to-many associations

## Getting Started

### Prerequisites

- Java 11 or higher
- MySQL 5.7 or higher
- JDBC MySQL Connector

### Setup

1. Clone this repository
2. Create and configure the database:
   ```
   mysql -u root -p < database/CLUB_NORMALISED_TableCreation.sql
   ```
3. Configure database connection in `config/db.properties`:
   ```
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/CLUB_NORMALISED
   db.username=your_username
   db.password=your_password
   ```
4. Compile and run:
   ```
   javac -d bin src/App.java
   java -cp bin:lib/* App
   ```

## Key Components

### Models

Models represent data entities and inherit from BaseModel. Each model implements validation logic.

### Services

Services implement the CrudService interface and provide database operations for models.

### UI Components

The UI is built using Java Swing and follows a consistent design language.

### Custom Interfaces

- **CrudService**: Defines standard database operations
- **Displayable**: Provides display formatting for UI components

## Validation

Input validation is implemented at multiple levels:

- Client-side validation in UI forms
- Model-level validation through isValid() method
- Database constraints
- Custom ValidationUtils for type-specific validations

## License

This project is for educational purposes only.
