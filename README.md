# Spring Boot Application

A Spring Boot REST API application with endpoints for managing users, documents, and permissions.

## Features

- User management with in-memory ConcurrentHashMap storage
- Document management per user
- Permission management per user
- RESTful API endpoints

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Quick start

Please consult [QUICK_START.md](QUICK_START.md) for instructions on how to run the application with Docker. Here is TL;DR

```bash
docker-compose down && docker-compose up -d --build
```

## API Endpoints

### 1. Users Endpoint

**Get all users:**
```
GET /api/users
```

**Get user by ID:**
```
GET /api/users/{userId}
```

Response example:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "username": "jdoe",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com"
}
```

### 2. Documents Endpoint

**Get documents for a user:**
```
GET /api/documents/user/{userId}
```

Response example:
```json
[
  "Document 1: Annual Report 2023",
  "Document 2: Project Proposal",
  "Document 3: Meeting Notes"
]
```

**Add a document for a user:**
```
POST /api/documents/user/{userId}
Content-Type: text/plain

Document content here
```

### 3. Permissions Endpoint

**Get permissions for a user:**
```
GET /api/permissions/user/{userId}
```

Response example:
```json
[
  "READ",
  "WRITE",
  "DELETE",
  "ADMIN"
]
```

**Add a permission for a user:**
```
POST /api/permissions/user/{userId}
Content-Type: text/plain

EXECUTE
```

## Project Structure

```
grafana-example/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── Main.java                    # Spring Boot application entry point
│       │           ├── controller/
│       │           │   ├── UserController.java      # User REST endpoints
│       │           │   ├── DocumentController.java  # Document REST endpoints
│       │           │   └── PermissionController.java # Permission REST endpoints
│       │           ├── model/
│       │           │   └── User.java                # User model
│       │           └── service/
│       │               ├── UserService.java         # User business logic
│       │               ├── DocumentService.java     # Document business logic
│       │               └── PermissionService.java   # Permission business logic
│       └── resources/
│           └── application.properties               # Application configuration
└── pom.xml                                          # Maven configuration
```

## Data Storage

All data is stored in-memory using `ConcurrentHashMap`:
- **Users**: `ConcurrentHashMap<UUID, User>`
- **Documents**: `ConcurrentHashMap<UUID, List<String>>`
- **Permissions**: `ConcurrentHashMap<UUID, List<String>>`

Sample data is initialized on application startup.

## Building the Application

```bash
mvn clean package
```

The JAR file will be created in the `target/` directory.

## Running the JAR

```bash
java -jar target/grafana-example-1.0-SNAPSHOT.jar
```

