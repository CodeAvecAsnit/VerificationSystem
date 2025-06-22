# Email Verification Microservice (Spring Boot)

This project implements an email verification microservice using Spring Boot. It handles user registration with email and password, sends a verification code, and only stores verified users into the database. It is designed to be stateless for unverified users, reducing unnecessary database usage.

## Features

### 1. Email Verification Flow
- Accepts a registration request with user email and password.
- Hashes the password using SHA-256.
- Generates a random 6-digit verification code.
- Sends the verification code to the user's email using SMTP.
- Waits for user to enter the code.
- If the entered code is correct and within timeout and allowed attempts, user is stored in the database.

### 2. No Pre-Verification Persistence
- Unlike Entity Framework Core in .NET, where foreign key constraints often require early data persistence, this Spring Boot microservice does not store unverified data.
- Unverified users are held in memory using in-memory storage until verification.

### 3. In-Memory Storage for Verification Process
- Uses `ConcurrentHashMap<String, CodeEntry>` to map user emails to their verification codes.
- Uses `ConcurrentHashMap<String, FinalUser>` to temporarily store the user object before they are verified.
- Each `CodeEntry` tracks when the code was generated and how many times the user has attempted to verify.

### 4. Expiry and Rate Limiting
- Each verification code expires after a fixed timeout (e.g., 5 minutes).
- Maximum number of allowed attempts to enter the verification code is limited (e.g., 5 tries).
- If either the timeout is exceeded or the attempt limit is reached, both the verification code and the user data are removed from memory.

### 5. Password Hashing
- Passwords are hashed using Java's built-in SHA-256 implementation (`MessageDigest`).
- Hashing happens before storing any data, including in-memory data.

### 6. Email Sending
- Sends email using JavaMailSender or HTTP call to another microservice (e.g., .NET-based email service).
- Email body contains the verification code.

### 7. Component-Based Design
- The `Manager` class acts as a central service for handling code verification, user insertion, attempt tracking, and memory cleanup.
- It is annotated with `@Component` and used through dependency injection.
- Spring Boot ensures a single shared instance (`singleton`) of this service across all requests.

### 8. Integration with Database
- After verification succeeds, the user is saved to the database using a `UserRepository` with `save(user)`.
- This is the only point at which data is persisted to the database, making the flow efficient and clean.

## Design Considerations
- The entire logic avoids overloading the database by using memory for transient data.
- Controllers are stateless and do not store data themselves. All logic and memory state are handled by the shared service (`Manager`).
- This approach avoids complications related to entity state, foreign key constraints, and object tracking which are common in frameworks like Entity Framework in .NET.

## Code Quality Notes
- Timeout and attempt limit are configurable via constants in the service class.
- Clean separation of responsibilities: code generation, email sending, verification, and user saving are handled separately.
- Exceptions are used to control flow when timeouts or rate limits are hit.

## Advantages Over Traditional Persistence
- Avoids storing incomplete or unverified user data.
- Better control over user verification lifecycle.
- More suitable for production systems with heavy signup traffic.

## Comparison to .NET (Entity Framework)
- In .NET, database-first designs often require early persistence for entities like `User` before `VerificationCode`, leading to unnecessary records.
- Spring Boot, in this case, avoids that by holding unverified users in memory.
- This design is more efficient in terms of storage and database cleanliness.

## Notes
- No user is saved until they are successfully verified.
- Multiple verification attempts and expiration are enforced to prevent abuse.
- The system is designed to scale by adjusting timeout and attempt limits as needed.
