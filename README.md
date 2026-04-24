# 💼 HireDesk — Backend

> Interview Pipeline Management System — Spring Boot REST API

## 🚀 Tech Stack
- Java 17 + Spring Boot 3
- Spring Security + JWT Authentication
- Spring Data JPA + Hibernate
- PostgreSQL Database
- Maven Build Tool
- Lombok, Swagger (OpenAPI)
- Apache POI (Excel), BCrypt

## ✨ Features
- JWT based authentication with role based access (Recruiter / Interviewer)
- Job opening management with interview rounds
- Candidate pipeline tracking with Kanban stages
- Token based feedback collection (no login required for interviewers)
- Analytics — hiring funnel, source breakdown, interviewer stats
- Bulk candidate upload via Excel
- Automated email notifications (Spring Mail)

## 📡 API Endpoints
| Module | Endpoints |
|--------|-----------|
| Auth | POST /api/auth/register, /api/auth/login |
| Jobs | GET/POST/PUT/DELETE /api/jobs |
| Candidates | GET/POST/PATCH /api/candidates |
| Pipeline | GET /api/candidates/pipeline/{jobId} |
| Feedback | POST /api/feedback/request, /api/feedback/submit/{token} |
| Analytics | GET /api/analytics/overview, /funnel, /sources |

## 🗄️ Database Schema
- users, job_openings, interview_rounds
- candidates, interview_slots, feedback

## ⚙️ Setup & Run
```bash
# 1. Clone the repo
git clone https://github.com/krrishchopra7/hiredesk-backend.git

# 2. Create PostgreSQL database
createdb hiredesk_db

# 3. Update application.properties
spring.datasource.password=YOUR_PASSWORD

# 4. Run
mvn spring-boot:run
```

## 🔗 Frontend
https://github.com/krrishchopra7/hiredesk-frontend
