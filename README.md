# Wishlist API

Backend is a wish list management application with support for roles, wish reservations, moderation, and access control.

---

## The purpose of the project

Create a REST API for managing user desires, where:
- users can create and post desires;
- other users can book wishes;
- moderators and administrators manage content and users;
- all logic is protected by roles and Spring Security;
- the correct architecture of the Spring Boot application is used.

---

## Technology stack

- Java 17  
- Spring Boot  
- Spring Security  
- Spring Data JPA (Hibernate)  
- Liquibase  
- PostgreSQL 
- MapStruct  
- JUnit 5, Mockito  
- Postman  

---

## Application Architecture

The application is built on a layered architecture:

- **Controller** — accepts HTTP requests, returns DTO  
- **Service** — business logic of the application  
- **Repository** — working with the database  
- **Entity** — JPA entities  
- **DTO (Request/Response)** — data exchange with the client  
- **Mapper (MapStruct)** — transformation of Entity ↔ DTO

Controllers do not contain business logic.  
Entities are not returned directly from the controllers.

---

## Security and Roles

Spring Security is used.

### Roles

**ROLE_USER**
- manage your profile
- create and edit your desires
- publish desires
- book other people's desires
- add your desire to categories (general list)
- add your desire to a group (personal list)

**ROLE_MODERATOR**
- viewing and moderation of wishes
- access to administrative API for wishes

**ROLE_ADMIN**
- full access
- user management
- role reversal
- blocking users

---

## Basic functionality

### Users and Security
- registration
- authorization
- getting the current user
- profile editing
- password change
- blocking users (admin)
- changing user role (admin)

### Wish
- creation of desire
- editing a wish
- removal of desire
- publishing a wish
- view published wishes
- view your own desires
- moderation of desires (admin / moderator)

### The Wish Group
- creation of groups
- linking desires to groups
- manage only your own groups

### Category
- view the list of categories
- using categories in desires
- removal of categories by the administrator

### Booking
- booking a published wish
- cancellation of the booking
- one wish can only be booked once
- storing the booking status

---

## Entities and connections

### User
- has a role (ROLE_USER / ROLE_MODERATOR / ROLE_ADMIN)
- can own desires and groups
- can book the wishes of other users

### Wish
- belongs to the user (owner)
- maybe in the wish group
- belongs to the category
- has the status (DRAFT, PUBLISHED, BOOKED, DELETED)

### WishGroup
- belongs to the user
- contains a wish list

### Category
- used to classify desires

### Booking
- connects the user and the desire
- one wish — one active booking
- stores the booking status

---

## Статусы

### WishStatus
- DRAFT — draft  
- PUBLISHED — published and accessible to all  
- BOOKED — booked  
- DELETED — deleted

### BookingStatus
- ACTIVE — active booking  
- CANCELLED — cancelled

---

## API

The project contains a Postman Collection with the following query groups:
- Auth (registration, login)
- Users (profile, password)
- Admin 
- Wishes
- Wish Groups
- Categories
- Bookings

All queries use DTO and return structured responses.

---

## Database migrations

To manage the database schema, use **Liquibase**:
- creation of tables
- filling in the initial data
- version control of the schema

All migrations are performed automatically when the application is launched.

---

## Testing

- Each service is covered by **unit tests**
- **JUnit 5** and **Mockito are used**
  
- Tested:
  - business logic
  - error handling
  - scenarios for saving and retrieving data

---

## Project Launch

1. Clone a repository:
   
git clone https://github.com/dellya4/sw_final

2. Configure the database in application.properties
3. Launch the app
4. Liquibase migrations will be applied automatically at startup.

---

## Postman

The Postman Collection is located in the folder:

postman/

---

## Author: Abdrakhmanova Adel
