# Personal Finance Manager

A comprehensive personal finance management system built with Spring Boot.

## Features
- **User Management**: Registration, Login, Session Management.
- **Transaction Management**: Track Income and Expenses, filter by date and category.
- **Category Management**: Default and Custom categories.
- **Savings Goals**: specific financial targets with progress tracking.
- **Reports**: Monthly and Yearly financial summaries.

## Tech Stack
- Java 17
- Spring Boot 3.4.2
- Spring Data JPA
- H2 Database
- Spring Security

## Setup & Run

1.  **Clone the repository**
2.  **Run the application**
    ```bash
    ./mvnw spring-boot:run
    ```
    (Or open in IDE and run `PersonalFinanceManagerApplication`)

3.  **API Documentation**
    The API runs at `http://localhost:8080/api`.
    - Auth: `/api/auth/register`, `/api/auth/login`
    - Transactions: `/api/transactions`
    - Categories: `/api/categories`
    - Goals: `/api/goals`
    - Reports: `/api/reports/monthly/{year}/{month}`

## Testing
Run unit tests with:
```bash
./mvnw test
```
