# Notes App – Spring Boot, MongoDB & Spring Security

## General Description

### This is a RESTful Notes Application built with Spring Boot, MongoDB, and Spring Security. It allows users to securely manage their notes, providing full CRUD functionality, tag-based filtering, pagination, and text statistics.

## Features
### User Authentication & Authorization
- Register new users and login using JWT-based authentication. 
- Secure endpoints so only authenticated users can manage their notes.
### Notes Management
- Create Notes: Notes include a Title, Created Date, Text, and optional Tags (BUSINESS, PERSONAL, IMPORTANT). Notes without a title or text cannot be created. 
- Update & Delete Notes: Users can update or remove existing notes.
### Notes Listing
- Display only Title and Created Date. 
- Filter by tags. 
- Pagination support for large numbers of notes. 
- Notes are sorted newest first.
### Text Statistics
- Retrieve a map of unique words and their counts for each note’s text. 
- Example: "note is just a note" → { "note": 2, "is": 1, "just": 1, "a": 1 }. 
- Note text is retrieved via a separate API endpoint.

## Technologies Used
- Backend: Spring Boot, Spring Web, Spring Data MongoDB 
- Security: Spring Security, JWT 
- Database: MongoDB 
- Containerization: Docker & Docker Compose 
- Testing: Unit and integration tests included

## API Endpoints
### Notes API
- POST /notes – Create a new note 
- PATCH /notes/{id} – Update a note 
- DELETE /notes/{id} – Delete a note 
- GET /notes – List notes with pagination & optional tag filter 
- GET /notes/{id} – Get full note by ID 
- GET /notes/{id}/stats – Get unique word count
### Users API
- POST /register – Register a new user 
- POST /login – Login and obtain JWT token 

## Running the Application
- lone the repository: "git clone https://github.com/kira2505/TheNotesApp.git"
- Start the app using Docker Compose: "docker-compose up --build"
- Access the APIs at http://localhost:8080/swagger-ui/index.html#/

## Seed Data & Test User
### The application comes with a preloaded test user and notes using a database seeder. This allows you to quickly test all endpoints without manually creating accounts or notes.
### Test User
- Username: admin 
- Password: admin123

### How to Test
1. Login with the test user to obtain a JWT token
2. Use the returned JWT token in the Authorization header to authenticate requests to the Notes API
3. After authentication, you can:
   - Create new notes 
   - Update or delete existing notes
   - List notes with pagination and filter by tags 
   - Retrieve full note text and statistics on unique words

### Preloaded Notes
- The test user comes with 15 random notes, each containing random text and one of the allowed tags: BUSINESS, PERSONAL, or IMPORTANT. 
- Notes are already sorted by creation date, so you can test pagination, filtering, and statistics endpoints immediately.