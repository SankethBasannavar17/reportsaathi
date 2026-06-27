# ReportSaathi — Backend API

> Understand your health reports in your language — instantly.

## What This Is
Java Spring Boot 3.x REST API powering the ReportSaathi Android app.
Handles OCR (Google Cloud Vision), AI explanation (GPT-4o), JWT auth (Firebase), and PostgreSQL storage.

## Tech Stack
- **Java 21** + **Spring Boot 3.2.5**
- **PostgreSQL** (Supabase in prod)
- **Google Cloud Vision** for OCR
- **OpenAI GPT-4o** for plain-language explanations
- **Firebase Admin** for Google Sign-In verification
- **JWT** for stateless auth

## Session Progress
- [x] Session 1: Project setup, DB connection, `/api/health` endpoint
- [ ] Session 2: Auth (Firebase + JWT)
- [ ] Session 3: Report upload + OCR
- [ ] Session 4: OCR parsing
- [ ] Session 5: AI explanation service
- [ ] Session 6: Full pipeline
- [ ] Session 7: History API

## Run Locally

### Prerequisites
1. Java 21 installed
2. PostgreSQL running locally
3. Database created: `createdb reportsaathi`

### Steps
```bash
# Clone
git clone https://github.com/SankethBasannavar17/reportsaathi.git
cd reportsaathi/reportsaathi-backend

# Run
mvn spring-boot:run

# Test health endpoint
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "status": "UP",
  "app": "ReportSaathi Backend",
  "version": "0.0.1"
}
```

## Environment Variables (Production)
Set these in Railway/Render:
```
DATABASE_URL=jdbc:postgresql://...
DATABASE_USER=...
DATABASE_PASSWORD=...
JWT_SECRET=...
OPENAI_API_KEY=sk-...
```
