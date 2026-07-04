# Movie Journal API

A RESTful API for keeping a personal movie journal — save movies you've watched, 
rate them from 1 to 5, and write short reviews.

Built with Spring Boot as a hands-on project to learn backend development with Java.

## Tech Stack

- **Java 21**
- **Spring Boot** (Spring Web, Spring Data JDBC, Validation)
- **H2** in-memory database
- **Maven** for build and dependency management

## Features

- Full CRUD operations for movies (Create, Read, Update, Delete)
- Input validation (rating must be 1–5, title cannot be empty)
- Layered architecture (Controller → Repository)
- Proper HTTP status codes (201 Created, 404 Not Found, 400 Bad Request)

## API Endpoints

| Method | Endpoint          | Description              |
|--------|-------------------|--------------------------|
| GET    | /api/movies       | Get all movies           |
| GET    | /api/movies/{id}  | Get a movie by ID        |
| POST   | /api/movies       | Add a new movie          |
| PUT    | /api/movies/{id}  | Update an existing movie  |
| DELETE | /api/movies/{id}  | Delete a movie           |

## Movie Model

```json
{
  "id": 1,
  "title": "Inception",
  "rating": 5,
  "review": "Great movie",
  "releaseYear": 2010
}
```

## Running Locally

1. Clone the repository
2. Run `./mvnw spring-boot:run` (or run the main class from your IDE)
3. The API will be available at `http://localhost:8080`
4. H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:movies`)
