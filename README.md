Java | Spring Boot | JPA | Hibernate | REST API | Swagger | JUnit | Mockito


📌 Project Description

A Spring Boot REST API that demonstrates a Many-to-Many relationship between Students and Courses using Spring Data JPA and Hibernate.

In this system:

A student can enroll in multiple courses.

A course can contain multiple students.

The project showcases REST API development, JPA entity relationships, and layered architecture using Spring Boot.

________________________________________________________________________________________________________________________________________________________________________________________________________

✨ Key Features

• Create and manage Students

• Create and manage Courses

• Many-to-Many relationship using JPA Join Table

• DTO pattern for request and response objects

• Request validation using Jakarta Validation

• RESTful API design

• Global Exception Handling

• API documentation using Swagger / OpenAPI

• Unit tests for Controller and Service layers

• Layered architecture (Controller → Service → Repository)

________________________________________________________________________________________________________________________________________________________________________________________________________

🏗 Tech Stack

**Backend** : Java 17, Spring Boot, Spring Web, Spring Data JPA, Hibernate ORM


**_Database_** : H2 Database (for development), MySQL (can be configured)


**Build Tool** : Maven


**Libraries** : Lombok, Jakarta Persistence API, Jakarta Validation

_________________________________________________________________________________________________________________________________________________________________________________________________________

Entity Relationship : Student  * ----------- *  Course
         (Many-to-Many)

_________________________________________________________________________________________________________________________________________________________________________________________________________

Future Improvements


• Add Pagination & Sorting

• Implement Authentication using Spring Security + JWT





