# File Sharing Service

A minimal file-sharing service built with Spring Boot. The current goal is to implement a reliable and secure way to upload files, store metadata in a database, and prepare for future scalability (like S3 integration).

## 🚀 Current Features
- Upload and store files locally
- Store file metadata (name, timestamps, random ID, expiry) in the database
- Transactional handling: database writes and file storage are decoupled using `TransactionSynchronization` for consistency
- Custom exception handling for empty uploads, duplicate IDs, and storage errors
- Unique random ID generation with collision retries

## 📌 In Progress / Future Goals
- Migrate file storage to cloud (e.g., AWS S3)
- Add download & file preview endpoints
- Add expiration-based deletion (cron job or event-driven)
- Implement access control (public/private sharing, auth if needed)
- Add unit & integration tests for robustness
- Improve metadata (file size, type, etc.)
- UI for upload & download (optional MVP frontend)

## 🛠 Tech Stack
- Java 17+
- Spring Boot
- H2 / PostgreSQL (for metadata storage)
- Lombok
- SLF4J + Logback

---

> ⚠️ This is a work in progress, aimed at learning and demonstrating best practices around transactional integrity, error handling, and clean architecture in Spring Boot.

Feel free to clone, learn, or contribute!
