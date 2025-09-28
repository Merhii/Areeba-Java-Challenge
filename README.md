# Areeba Java Challenge

This repository is my **demonstration for the Java Challenge by Areeba**.  

I mimicked a **small financial banking system application** with a **proof of concept for fraud detection** using a **microservice based architecture**.

---

## Architecture

For my implementation, I decided to use a **monorepo microservice architecture**.  
Each microservice runs independently and communicates with others through **REST APIs (via Feign Clients)**.

Microservices:
- **Account Service** 
- **Card Service** 
- **Transaction Service** 
- **Fraud Service** 

---

## Dependencies Used

- **Spring Data JPA (Hibernate)**
- **Spring Cloud OpenFeign**
- **PostgreSQL**
- **Lombok**
- **Swagger / OpenAPI**
- **JUnit 5 + Mockito**
- **Maven**

---

## API Documentation

Once the services are running, Swagger UI is available at:

- Account Service → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- Card Service → [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)  
- Transaction Service → [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)  
- Fraud Service → [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)  

---

## Database Setup

The project uses **PostgreSQL**.  

1. Install PostgreSQL and pgAdmin.  
2. Import the database files:   
   -accountdb
   -carddb
   -frauddb
   -transactiondb
   
## Project Setup 

git clone https://github.com/Merhii/Areeba-Java-Challenge.git
cd areeba-java-challenge
mvn clean install

cd account-service
mvn spring-boot:run

cd card-service
mvn spring-boot:run

cd transaction-service
mvn spring-boot:run

cd fraud-service
mvn spring-boot:run
****

## Testing

mvn test
