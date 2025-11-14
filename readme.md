# Employee Management System REST API

This project is a REST API for an Employee Management System, built with Java and Spring Boot. The goal is to create a system for managing employees and eventually calculating salaries automatically.

## Tech Stack

- **Backend:** Java 17
- **Framework:** Spring Boot
- **Data:** Spring Data JPA (Hibernate)
- **Database:** H2 (In-memory, for development)
- **Build Tool:** Maven

## Project Structure

The project follows a standard Spring Boot application structure:

```
.
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── demo/
                        ├── DemoApplication.java  // Main application entry point
                        ├── model/
                        │   └── Employee.java       // JPA Entity for Employee
                        ├── repository/
                        │   └── EmployeeRepository.java // Spring Data JPA repository
                        └── controller/
                            └── EmployeeController.java // REST controller for employees
```

- **`DemoApplication.java`**: The main class that bootstraps the Spring Boot application.
- **`model`**: Contains the JPA entity classes that map to database tables.
- **`repository`**: Contains Spring Data JPA interfaces for database operations (CRUD).
- **`controller`**: Contains REST controllers that define the API endpoints and handle incoming HTTP requests.

## How to Run

1.  Make sure you have Java 17 and Maven installed.
2.  Open the project in your IDE.
3.  The IDE should automatically resolve the Maven dependencies defined in `pom.xml`.
4.  Run the `main` method in the `DemoApplication.java` class.
5.  The application will start on the default port (usually 8080).

## Available API Endpoints

### Employee API (`/api/employees`)

-   **`GET /api/employees`**
    -   **Description:** Retrieves a list of all employees.
    -   **Response:** `200 OK` with a JSON array of employee objects.

-   **`POST /api/employees`**
    -   **Description:** Creates a new employee.
    -   **Request Body:** A JSON object representing the new employee.
        ```json
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "john.doe@example.com"
        }
        ```
    -   **Response:** `200 OK` with the created employee object, including its new `id`.
