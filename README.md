# EasyStock API

EasyStock is a backend REST API for commercial business management, based on the SmartShop project requirements. It is designed for B2B distributors to manage clients, products, orders, and payments.

## Core Features

-   **Client & Loyalty Management:** Full client lifecycle management with an automatic tiered loyalty system (BASIC, SILVER, GOLD, PLATINUM).
-   **Product & Stock Management:** CRUD operations for products with real-time stock tracking and soft-delete functionality.
-   **Order Processing:** Create multi-product orders with automated calculations for loyalty discounts, promo codes, and TVA (20%).
-   **Multi-Method Payments:** Supports splitting a single invoice across multiple payment types (Cash, Cheque, Bank Transfer) and tracks the remaining balance.
-   **Role-Based Access:** Differentiates between ADMIN and CLIENT roles.
-   **Session-Based Authentication:** Simple login/logout functionality using HTTP Sessions.

## Tech Stack

-   **Framework:** Spring Boot 3.2.5
-   **Language:** Java 17
-   **API:** REST (JSON)
-   **Database:** PostgreSQL
-   **ORM:** Spring Data JPA / Hibernate
-   **Database Migration:** Liquibase
-   **Mapping:** MapStruct
-   **Tooling:** Maven, Lombok

## How to Run

1.  **Prerequisites:**
    -   Java 17+
    -   Maven 3.8+
    -   PostgreSQL

2.  **Database Setup:**
    -   Create a PostgreSQL database named `easystock_db`.
    -   Update the database credentials in `src/main/resources/application.yaml`.

3.  **Run the Application:**
    -   Clone the repository: `git clone <your-repo-url>`
    -   Navigate to the project directory: `cd easystock`
    -   Run the application using Maven:
        ```bash
        mvn spring-boot:run
        ```
    -   The API will be available at `http://localhost:8080`.