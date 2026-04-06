# OmniAnalytic - Java Spring Boot Demo Application

This project is a Java Spring Boot application built with JDK 17 and Maven, demonstrating several core features and modern development practices.

## 🚀 Key Features

*   **RESTful API**: Implementation of various HTTP methods (GET, POST, PUT, DELETE).
*   **Search & Pagination**: Search functionality for employees with built-in Spring Data JPA pagination.
*   **AspectJ Logging**: Automatic logging of all requests and responses using Aspect-Oriented Programming (AOP).
*   **In-Memory Database**: Uses H2 database for rapid development and testing (accessible at `/h2-console`).
*   **Complex JPA Queries**: Demonstrates joining two tables (`Employee` and `Department`) using JPQL.
*   **External API Integration**: A service method that calls an external API (e.g., www.google.com) using `RestTemplate`.
*   **Unit Testing**: Comprehensive unit tests using JUnit 5 and Mockito.

## 🛠 Tech Stack

*   **Java 17**
*   **Spring Boot 3.2.1**
*   **Spring Data JPA**
*   **Spring AOP (AspectJ)**
*   **H2 Database**
*   **Lombok**
*   **Maven**

## 🏗 Project Structure

- `com.example.OmniAnalytic.controller`: REST Controllers for handling HTTP requests.
- `com.example.OmniAnalytic.service`: Business logic layer, including external API calls.
- `com.example.OmniAnalytic.repository`: Data access layer using Spring Data JPA.
- `com.example.OmniAnalytic.model`: JPA Entities (Employee, Department).
- `com.example.OmniAnalytic.config`: Configuration classes (RestTemplate, Logging Aspect).

## 🏃‍♂️ How to Run

1.  **Build the Application**
    ```bash
    mvn clean install
    ```

2.  **Run the Application**
    ```bash
    mvn spring-boot:run
    ```

3.  **Access H2 Console**
    - URL: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: `password`

## 🧪 Testing the API

### 1. Create an Employee (POST)
```bash
curl -X POST http://localhost:8080/api/employees \
-H "Content-Type: application/json" \
-d '{"firstName": "John", "lastName": "Doe"}'
```

### 2. Search with Pagination (GET)
```bash
curl "http://localhost:8080/api/employees/search?query=John&page=0&size=10"
```

### 3. Call External API (GET)
```bash
curl http://localhost:8080/api/employees/external
```

## 🧪 Running Tests
```bash
mvn test
```
