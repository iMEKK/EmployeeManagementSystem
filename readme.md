# Employee Management System

A full-stack web application for managing employees, payroll, and leave requests, built with a Spring Boot backend and a vanilla JavaScript frontend.

## ✨ Features

This application implements role-based access control with two main roles: **Admin** and **Employee**.

### 👨‍💼 Admin Features
- **Employee Management**: Full CRUD (Create, Read, Update, Delete) functionality for all employee records.
- **Leave Approval**: View all submitted leave requests and approve or reject them.
- **Payroll Calculation**: Trigger monthly payroll calculations for all employees based on their work hours, overtime, and approved leave.

### 👷 Employee Features
- **Time Tracking**: View personal timesheet history.
- **Leave Requests**: Submit new leave requests for approval.
- **View Payslips**: View personal payroll history (payslips).

## 🛠️ Tech Stack

- **Backend**:
  - Java 17
  - Spring Boot 3
  - Spring Security (for Role-Based Authentication)
  - Spring Data JPA (Hibernate)
  - Maven
- **Database**:
  - H2 In-Memory Database
- **Frontend**:
  - HTML5
  - CSS3
  - Vanilla JavaScript (ES6)

## 📂 Project Structure

The project is organized into two main parts within the standard Maven structure:

```
.
└── src/
    ├── main/
    │   ├── java/com/example/demo/  // Spring Boot Backend Source Code
    │   │   ├── config/             // SecurityConfig, DataSeeder
    │   │   ├── controller/         // REST API Controllers
    │   │   ├── dto/                // Data Transfer Objects (ErrorResponse)
    │   │   ├── exception/          // Global Exception Handling
    │   │   ├── model/              // JPA Entities (Employee, Payroll, etc.)
    │   │   ├── repository/         // Spring Data JPA Repositories
    │   │   └── service/            // Business Logic Services
    │   │
    │   └── resources/
    │       ├── static/             // Frontend Source Code
    │       │   ├── css/style.css
    │       │   ├── js/             // All JavaScript files for the UI
    │       │   └── *.html          // All HTML pages
    │       └── application.properties
    │
    └── test/                       // Test code
```

## 🚀 How to Run and Test

### Prerequisites
- Java 17 (or higher)
- Apache Maven

### Running the Application
1.  Open the project in your favorite IDE (like IntelliJ IDEA or VS Code).
2.  Locate and run the `main` method in `DemoApplication.java`.
3.  The application will start on the default port `8080`.

### Testing the Application
1.  Once the application is running, open your web browser and go to the login page:
    - **URL:** `http://localhost:8080/login.html`

2.  Use the default credentials created by the `DataSeeder` to log in and test the features for each role.

## 🔑 Default Credentials

The system automatically creates the following users for testing purposes when it first starts up:

| Username | Password | Role | Description |
| :--- | :--- | :--- | :--- |
| `admin` | `adminpass` | **Admin** | Has access to all administrative features. |
| `employee1` | `pass1` | Employee | A diligent employee with overtime records. |
| `employee2` | `pass2` | Employee | A normal employee with an approved leave record. |
