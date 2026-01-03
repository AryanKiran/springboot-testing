
---

````md
# Spring Boot Testing & Code Coverage (JaCoCo)

A hands-on Spring Boot project focused on unit testing, repository testing, and code coverage analysis using industry-standard tools.  
This repository demonstrates practical backend testing skills commonly used in production-grade Java applications.

---

## 🔍 Key Skills Demonstrated

- Writing unit tests using JUnit 5  
- Mocking dependencies with Mockito  
- Testing Spring Data JPA repositories  
- Validating exception handling and edge cases  
- Measuring and analyzing code coverage using JaCoCo  
- Structuring tests using the Arrange–Act–Assert (AAA) pattern  

---

## 🛠️ Technology Stack

- Java  
- Spring Boot  
- JUnit 5  
- Mockito  
- Spring Data JPA  
- H2 In-Memory Database  
- JaCoCo  

---

## 🧪 Testing Strategy

### Service Layer Testing
- Business logic tested in isolation  
- External dependencies mocked using Mockito  

### Repository Layer Testing
- Uses `@DataJpaTest`  
- Verifies JPA queries and persistence behavior  

---

## 📊 JaCoCo Code Coverage

JaCoCo is used to ensure test quality by tracking:
- Line coverage  
- Method coverage  
- Branch coverage  

### Generate Coverage Report
```bash
./mvnw clean test
````

### View Coverage Report

After running tests, open the following file in a browser:

```
target/site/jacoco/index.html
```

---

## 📁 Project Structure

```
src
 ├── main
 │   └── java
 │       └── application source code
 └── test
     └── java
         ├── service tests
         └── repository tests
```

---

## 🚀 Getting Started

```bash
git clone <repository-url>
cd <project-directory>
./mvnw test
```
---


