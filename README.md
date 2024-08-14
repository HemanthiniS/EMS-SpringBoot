# EMS-SpringBoot

Overview

The trainee will build a basic Employee Management System using Java Spring Boot. The application will integrate with a MySQL database and expose RESTful services for managing employees, departments, and their relationships.

 Requirements:

1. Spring Boot Application:
   - Create a Spring Boot application with dependencies for Spring Data JPA, Spring Web, and MySQL.

2. Database Integration:
   - Integrate the application with a MySQL database.
   - Use Spring Data JPA to manage database operations.

3. Entities and Tables:
   - The application should have at least three entities corresponding to three database tables:
     1. Employee: 
        - `id` (Primary Key, Auto-increment)
        - `name` (String)
        - `email` (String)
        - `department_id` (Foreign Key to Department)
     2. Department: 
        - `id` (Primary Key, Auto-increment)
        - `name` (String)
     3. Project: 
        - `id` (Primary Key, Auto-increment)
        - `name` (String)
        - `description` (String)
        - `employee_id` (Foreign Key to Employee)

4. RESTful Services:
   - Implement CRUD (Create, Read, Update, Delete) operations for each entity:
     - Employee Service: Should include endpoints for adding, updating, deleting, and listing employees.
     - Department Service: Should include endpoints for managing departments.
     - Project Service: Should include endpoints for managing projects and assigning them to employees.

5. Validation:
   - Add basic validation to ensure data integrity (e.g., email format validation, non-empty fields).

6. Basic Security: (Optional)
   - Implement basic authentication using Spring Security (optional for added complexity).

7. Testing:
   - Write unit tests for the service layer using JUnit.
   - Write integration tests for the RESTful endpoints.
