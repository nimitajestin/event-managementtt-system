CREATE DATABASE CLUB_NORMALISED;
USE CLUB_NORMALISED;

CREATE TABLE Clubs (
    club_id INT PRIMARY KEY,
    club_name VARCHAR(100) NOT NULL,
    club_description TEXT
);

CREATE TABLE ClubMembers (
	member_id INT PRIMARY KEY,
	member_name VARCHAR(100) NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	join_date DATE,
    phone numeric(10),
    club_id INT,
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id) ON DELETE CASCADE);

CREATE TABLE ClubMembership (
    member_id INT,
    club_id INT,
    PRIMARY KEY (member_id, club_id),
    FOREIGN KEY (member_id) REFERENCES ClubMembers(member_id) ON DELETE CASCADE,
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id) ON DELETE CASCADE
);

CREATE TABLE ClubPositions (
    position_id INT PRIMARY KEY,
    position_name VARCHAR(50) NOT NULL
);

CREATE TABLE MemberPositions (
    member_id INT,
    position_id INT,
    club_id INT,
    PRIMARY KEY (member_id, position_id, club_id),
    FOREIGN KEY (member_id) REFERENCES ClubMembers(member_id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES ClubPositions(position_id),
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id) ON DELETE CASCADE
);

CREATE TABLE Venues (
    venue_id INT PRIMARY KEY,
    venue_name VARCHAR(100),
    location VARCHAR(255)
);

CREATE TABLE Events (
    event_id INT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    description TEXT,
    club_id INT,
    event_date DATE,
    venue_id INT,
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id),
    FOREIGN KEY (venue_id) REFERENCES Venues(venue_id)
);

CREATE TABLE EventRoles (
    role_id INT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE EventAssignments (
    member_id INT,
    event_id INT,
    role_id INT,
    PRIMARY KEY (member_id, event_id, role_id),
    FOREIGN KEY (member_id) REFERENCES ClubMembers(member_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id),
    FOREIGN KEY (role_id) REFERENCES EventRoles(role_id)
);

CREATE TABLE EventRegistrations (
    registration_id INT PRIMARY KEY,
    member_id INT,
    event_id INT,
    registration_date DATE,
    FOREIGN KEY (member_id) REFERENCES ClubMembers(member_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

CREATE TABLE EventAttendance (
    attendance_id INT PRIMARY KEY,
    member_id INT,
    event_id INT,
    attendance_status VARCHAR(20),
    FOREIGN KEY (member_id) REFERENCES ClubMembers(member_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

CREATE TABLE EventFeedback (
    feedback_id INT PRIMARY KEY,
    member_id INT,
    event_id INT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    FOREIGN KEY (member_id) REFERENCES ClubMembers(member_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

CREATE TABLE BudgetRequests (
    request_id INT PRIMARY KEY,
    club_id INT,
    event_id INT,
    amount_requested DECIMAL(10,2),
    reason TEXT,
    status VARCHAR(20),
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

CREATE TABLE BudgetApprovals (
    approval_id INT PRIMARY KEY,
    request_id INT,
    approved_amount DECIMAL(10,2),
    approval_date DATE,
    FOREIGN KEY (request_id) REFERENCES BudgetRequests(request_id)
);

CREATE TABLE Sponsorships (
    sponsor_id INT PRIMARY KEY,
    sponsor_name VARCHAR(100) NOT NULL,
    event_id INT,
    amount DECIMAL(10,2),
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

CREATE TABLE Announcements (
    announcement_id INT PRIMARY KEY,
    club_id INT,
    title VARCHAR(100),
    content TEXT,
    announcement_date DATE,
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id)
);

CREATE TABLE Documents (
    document_id INT PRIMARY KEY,
    club_id INT,
    title VARCHAR(100),
    file_path VARCHAR(255),
    upload_date DATE,
    event_id INT,
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE CASCADE
);

CREATE TABLE FacultyCoordinators (
    coordinator_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    club_id INT,
    FOREIGN KEY (club_id) REFERENCES Clubs(club_id),
    phone VARCHAR(15)
);

ALTER TABLE ClubMembers ADD phone numeric(10);

UPDATE clubmembers SET club_id = 1 WHERE member_id BETWEEN 101 AND 106;
UPDATE clubmembers SET club_id = 2 WHERE member_id BETWEEN 201 AND 206;
UPDATE clubmembers SET club_id = 3 WHERE member_id BETWEEN 301 AND 307;
UPDATE clubmembers SET club_id = 4 WHERE member_id BETWEEN 401 AND 415;