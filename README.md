# YucaTracking

**A Java-Based Cassava Traceability and Shipment Tracking System**

## 1. Project Overview

YucaTracking is a Java-based backend prototype designed to support the traceability of cassava from farmers through transportation to its destination.

The system provides a structured way to register farmers, record harvested cassava batches, create shipments, update shipment statuses, and retrieve tracking history.

The project was developed as part of my Software Engineering and backend development learning experience during my SIWES internship at YucaChain Limited.

## 2. Problem Statement

Cassava moves through different stages between harvesting and delivery. Without proper tracking records, it can be difficult to determine the current status of a shipment, identify its associated farmer or batch, and review the events that occurred during transportation.

YucaTracking addresses this problem by providing a backend foundation for recording cassava batches, associating them with shipments, and maintaining shipment tracking history.

## 3. Project Objectives

The main objectives of YucaTracking are to:

- Register farmers and store their information.
- Create and manage cassava batch records.
- Associate cassava batches with shipments.
- Record shipment tracking events.
- Maintain both the current shipment status and its historical tracking records.
- Provide REST API endpoints for interacting with the backend.
- Store and retrieve application data using PostgreSQL.

## 4. Key Features

- **Farmer Management:** Register and retrieve farmer records.
- **Cassava Batch Management:** Create and retrieve cassava batches associated with farmers.
- **Shipment Management:** Create shipments containing transportation and destination information.
- **Tracking Updates:** Record shipment status changes and tracking information.
- **Tracking History:** Retrieve historical updates for a shipment.
- **Journey Retrieval:** Retrieve related farmer, batch, shipment, and tracking information.
- **Status Validation:** Control permitted shipment status transitions.
- **Database Persistence:** Store application records in PostgreSQL.
- **REST API:** Expose HTTP endpoints that accept and return JSON data.

## 5. Technologies Used

| Technology | Purpose |
|---|---|
| Java | Backend application development |
| Java Servlets | Handle HTTP requests and responses |
| Apache Tomcat 9 | Run the web application |
| PostgreSQL | Relational database management |
| JDBC | Connect Java code to PostgreSQL |
| Maven | Manage dependencies and build the project |
| REST API | Provide endpoints for client interaction |
| JSON | Exchange data between clients and the backend |
| Jackson | Convert Java objects to and from JSON |
| Git and GitHub | Version control and source code hosting |
| IntelliJ IDEA | Development environment |

## 6. System Architecture

The application follows a layered backend structure.

```text
Client / API Testing Tool
          |
          v   
HTTP Requests (JSON)
          |
          v
Apache Tomcat
          |
          v
Java Servlets
          |
          v
Service Layer
          |
          v
JDBC
          |
          v
PostgreSQL Database
```

### How It Works

1. A client sends an HTTP request to an API endpoint.
2. Apache Tomcat receives the request and routes it to the appropriate servlet.
3. The servlet processes the request and calls the relevant service class.
4. The service layer performs business logic and interacts with the database through JDBC.
5. PostgreSQL stores or retrieves the required information.
6. The servlet returns an appropriate HTTP response, usually in JSON format.

Separating request handling from business logic makes the application easier to maintain, test, and extend.

## 7. Main Business Workflow

The core workflow follows this sequence:

```text
Register Farmer
      |
      v
Create Cassava Batch
      |
      v
Create Shipment
      |
      v
Record Tracking Updates
      |
      v
Retrieve Shipment History
      |
      v
View Cassava Journey
```

Each cassava batch is associated with a registered farmer. A shipment is then created for a batch, and tracking events are recorded as its status changes.

## 8. Shipment Status Lifecycle

The current prototype supports the following shipment lifecycle:

```text
CREATED  --->  IN_TRANSIT  --->  ARRIVED
```

- **CREATED:** A shipment has been registered.
- **IN_TRANSIT:** The shipment has begun its journey.
- **ARRIVED:** The shipment has reached its destination.

The tracking service validates status transitions to prevent unsupported transitions.

## 9. Database Design

PostgreSQL is used to store the application's relational data.

The main entities include:

### Farmers

Stores information about farmers registered in the system.

### Cassava Batches

Stores information about harvested cassava batches and associates each batch with a farmer.

### Shipments

Stores transportation details and the current status of each shipment.

### Tracking Updates

Stores historical tracking events associated with shipments.

### Relationships

- One farmer can have multiple cassava batches.
- Each cassava batch is associated with a farmer.
- A cassava batch can be associated with a shipment.
- A shipment can have multiple tracking updates.

The database also uses constraints to help maintain data integrity, including a unique batch code and a restriction against creating multiple shipments for the same batch.

## 10. REST API Endpoints

The following endpoints are available in the current prototype.

**Base URL for local development:**

```text
http://localhost:8080
```

### Farmer Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/farmers` | Register a farmer |
| GET | `/api/farmers` | Retrieve farmer information |

### Cassava Batch Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/batches` | Create a cassava batch |
| GET | `/api/batches?batchId=1` | Retrieve a batch by ID |
| GET | `/api/batches?batchCode=YC-001` | Retrieve a batch by batch code |

### Shipment Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/shipments` | Create a shipment |
| GET | `/api/shipments?shipmentId=1` | Retrieve a shipment by ID |

### Tracking Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/tracking` | Record a tracking update |
| GET | `/api/tracking?shipmentId=1` | Retrieve tracking history |

### Journey Endpoint

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/journeys?batchId=1` | Retrieve the journey associated with a cassava batch |

**Note:** Replace example IDs and batch codes with values that exist in your local database. Request body fields must match the DTOs and validation rules implemented by the application.

## 11. Example API Requests

The examples below illustrate the general request format. Confirm the exact required fields against the corresponding request DTOs in the source code.

### Create a Shipment

**Endpoint**

```http
POST /api/shipments
Content-Type: application/json
```

**Example request body**

```json
{
  "batchId": 1,
  "driverName": "Ibrahim",
  "vehicleNumber": "ABC-123",
  "destination": "Ilorin",
  "currentLocation": "Starting Point"
}
```

The referenced batch must already exist in the database.

### Retrieve a Shipment

```http
GET /api/shipments?shipmentId=1
```

### Retrieve Tracking History

```http
GET /api/tracking?shipmentId=1
```

### Retrieve a Cassava Journey

```http
GET /api/journeys?batchId=1
```

## 12. API Response Format

The API uses a standard response structure.

A successful response follows this general format:

```json
{
  "success": true,
  "data": {}
}
```

An unsuccessful response follows this general format:

```json
{
  "success": false,
  "error": "An error message"
}
```

The actual contents of the `data` field depend on the endpoint being called.

## 13. Data Consistency

Tracking updates must keep the shipment's current status consistent with its tracking history.

When a tracking event is recorded, the application coordinates the insertion of the tracking record and the corresponding updates to the shipment and batch. These database operations use a transaction so that related changes can be committed together or rolled back if an operation fails.

When a shipment reaches the `ARRIVED` status, the application automatically records its arrival time.

## 14. Installation and Setup

### Prerequisites

Before running the application, ensure that you have:

- Java Development Kit (JDK) installed.
- Apache Maven installed.
- PostgreSQL installed and running.
- Apache Tomcat 9 installed.
- A database and the required tables created.
- The project source code cloned from GitHub.

### Step 1: Clone the Repository

Replace the placeholder below with your actual GitHub repository URL.

```bash
git clone https://github.com/AbdulBasit0277/yuca-tracking-system.git
```

Navigate into the project directory:

```bash
cd yuca-tracking-system
```

### Step 2: Configure the Database

Create the PostgreSQL database and required tables.

Update the database configuration in the project to match your local environment, including the database URL, username, and password.

**Security note:** Do not commit database passwords, API secrets, or other credentials to GitHub. Use environment variables or a local configuration file excluded from version control.

### Step 3: Build the Project

Run the following command from the directory containing `pom.xml`:

```bash
mvn clean package
```

Maven will compile the application, resolve its dependencies, and package the web application.

### Step 4: Deploy to Tomcat

Deploy the generated WAR file from the project's `target` directory to the `webapps` directory of your Tomcat 9 installation.

Start Tomcat using the startup script appropriate for your operating system.

### Step 5: Test the API

Use an API testing tool, such as Postman, to send requests to the local endpoints.

For example:

```text
GET http://localhost:8080/api/shipments?shipmentId=1
```

The application must be running, and the requested record must exist for the request to return the expected data.

## 15. Testing

The prototype has been tested through API requests and database-backed workflows, including:

- Registering and retrieving farmers.
- Creating and retrieving cassava batches.
- Creating shipments for registered batches.
- Recording shipment tracking updates.
- Updating shipment status from `CREATED` to `IN_TRANSIT` and then `ARRIVED`.
- Retrieving tracking history.
- Retrieving a cassava journey by batch ID.
- Checking the JSON responses returned by the API.

Further automated testing would help verify edge cases, invalid requests, database failures, and concurrent operations.

## 16. Challenges and Lessons Learned

Developing YucaTracking provided practical experience with:

- Structuring a Java backend application.
- Using Servlets to handle HTTP requests.
- Connecting Java applications to PostgreSQL through JDBC.
- Designing relationships between database entities.
- Implementing REST API endpoints.
- Converting Java objects to and from JSON.
- Validating business rules and shipment status transitions.
- Maintaining consistency across related database records.
- Testing API endpoints and debugging integration issues.

The project also reinforced the importance of separating responsibilities between servlets, service classes, model classes, and database operations.

## 17. Current Limitations

YucaTracking is a backend prototype and foundation for a broader cassava traceability solution.

The current implementation does not claim to provide:

- Live GPS tracking of vehicles.
- Automatic location updates from physical tracking devices.
- A complete production-ready frontend.
- Confirmed integration with YucaChain's production application or database.
- Production deployment or real-world operational validation.

Tracking locations and status updates are currently supplied through the application's API rather than automatically obtained from live GPS hardware.

## 18. Future Improvements

Potential future improvements include:

- Integrating the backend with an approved YucaChain application or database architecture.
- Developing a web or mobile interface for farmers and logistics personnel.
- Integrating GPS devices for automatic location updates.
- Adding authentication and role-based authorization.
- Expanding validation and automated test coverage.
- Adding monitoring, logging, and production deployment configuration.
- Improving reporting and visibility into cassava shipment journeys.
- Evaluating additional traceability and preservation data requirements.

These improvements would depend on technical requirements, infrastructure, security considerations, and approval from the relevant stakeholders.

## 19. Project Status

YucaTracking currently provides a Java/PostgreSQL backend foundation for farmer registration, cassava batch management, shipment creation, tracking updates, and journey retrieval through REST API endpoints.

The project demonstrates core backend development concepts and provides a basis for future development and potential integration.

## 20. Author

**Name:** Saadudeen Abdulbasit

**Programme:** Computer Engineering

**Institution:** University of Ilorin, Nigeria

**Internship Organisation:** YucaChain Limited

**Project:** YucaTracking — Cassava Traceability and Shipment Tracking Backend

---

*Developed as a backend development project during SIWES at YucaChain Limited.*
