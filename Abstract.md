# Abstract

## Placement Cell Management System

The **Placement Management System** is a desktop application built using **Java Swing** and **MySQL** that streamlines the campus placement process for educational institutions. It provides a centralized platform for administrators to manage companies, create placement drives, and track student applications, while giving students the ability to register, browse eligible drives, and monitor their application status.

### Problem Statement

Campus placement coordination involves managing large volumes of student data, company information, drive schedules, and application tracking. Manual processes are error-prone, time-consuming, and lack real-time visibility for both administrators and students.

### Solution

This system digitizes the entire placement workflow:

1. **Student Registration & Authentication** — Students self-register with their academic details and authenticate using their roll number and email.
2. **Company & Drive Management** — Administrators maintain a directory of recruiting companies and create placement drives specifying eligibility criteria (minimum GPA), available seats, and compensation (LPA).
3. **Smart Application Processing** — The system enforces GPA eligibility checks automatically, prevents duplicate applications, and provides status tracking through the full lifecycle (Applied → Shortlisted → Accepted / Rejected).
4. **Role-Based Dashboards** — Separate interfaces for Admin (full CRUD on all entities) and Student (profile management, drive browsing, application tracking).

### Technology

- **Frontend**: Java Swing (cross-platform GUI)
- **Backend**: JDBC with MySQL (relational data persistence)
- **Architecture**: Layered design with Model, Database Access, and GUI layers

### Outcome

The system reduces administrative overhead, eliminates manual errors, and provides transparent, real-time placement tracking for all stakeholders.
