🛡️ AI-Based Fake Job Posting Detector (HireShield)

An AI-powered fraud detection system that identifies fake and scam job postings using Machine Learning + Rule-Based Intelligence.

Built using:

⚛️ React (Frontend)

☕ Spring Boot (Backend API)

🐍 FastAPI (ML Microservice)

🍃 MongoDB (Database)

🤖 TF-IDF + Logistic Regression (ML Model)

🚀 System Architecture
React (Frontend - 5173)
        ↓
Spring Boot (Backend - 8080)
        ↓
FastAPI ML Service (8000)
        ↓
MongoDB (Database)

📦 Project Structure
AI-Based-Fake-Job-Posting-Detector/
│
├── frontend/          # React (Vite)
├── backend/           # Spring Boot
├── ml-model/
│   ├── training/      # Model training scripts
│   ├── models/        # Trained model (.pkl)
│   └── api/           # FastAPI ML service
└── README.md

🧠 Core Features
✅ ML-Based Scam Detection

TF-IDF vectorization

Logistic Regression classifier

Fraud probability score

✅ Rule-Based Detection

Suspicious keyword detection

Free/public email domain detection

Unrealistic salary detection (INR & USD aware)

Indian-specific scam pattern detection

✅ Hybrid Risk Engine

ML Score (75%)

Rule-Based Score (25%)

Final risk classification:

LOW

MEDIUM

HIGH

✅ Admin Dashboard

View analyzed jobs

Pagination

Risk filtering

Statistics endpoint

⚙️ Setup Instructions
🟢 1️⃣ Start MongoDB

If local:

mongod


Default:

mongodb://localhost:27017

🟢 2️⃣ Start ML Service (FastAPI)

Go to:

ml-model/api


Activate virtual environment:

Windows (PowerShell)
.\venv\Scripts\Activate


Run FastAPI:

uvicorn app:app --reload --port 8000


Verify:

http://localhost:8000/docs

🟢 3️⃣ Start Backend (Spring Boot)

Go to:

backend


Run:

mvn spring-boot:run


Backend runs on:

http://localhost:8080

🟢 4️⃣ Start Frontend (React)

Go to:

frontend


Run:

npm install
npm run dev


Frontend runs on:

http://localhost:5173

🔐 Security

Spring Security enabled

Basic Authentication

CSRF disabled for REST APIs

Public endpoint:

POST /api/jobs/analyze


Protected endpoints:

GET /api/jobs
GET /api/jobs/stats


Default credentials (if using generated password):

Username: user
Password: (shown in backend console)

📡 API Endpoints
🔎 Analyze Job
POST /api/jobs/analyze

Sample Request
{
  "jobTitle": "Software Engineer",
  "description": "Develop backend APIs",
  "companyEmail": "hr@tcs.com",
  "salary": "₹6,00,000 per year"
}

Sample Response
{
  "fraudScore": 23.45,
  "riskLevel": "LOW",
  "reasons": [
    "No major suspicious patterns detected"
  ]
}

📊 Get Jobs (Admin)
GET /api/jobs?page=0&size=5&riskLevel=HIGH

📈 Get Stats
GET /api/jobs/stats


Returns:

{
  "total": 120,
  "highRisk": 30,
  "mediumRisk": 25,
  "lowRisk": 65
}

🧪 Testing Checklist
ML Layer

Model trains successfully

Handles empty salary safely

Handles no suspicious keywords

No crash on malformed input

Backend Layer

Calls ML service successfully

Saves to MongoDB

Returns correct fraudScore

Pagination works

Stats endpoint works

Frontend Layer

Dynamic fraud score animation

Real backend integration (no mock)

Proper error handling

Admin dashboard displays data

🧮 Risk Scoring Logic

Final Score:

Final Score = (0.75 × ML Score) + (0.25 × Rule Score)


Rule score capped at 40 to prevent dominance.

Risk Levels:

Score	Level
< 40	LOW
40–74	MEDIUM
≥ 75	HIGH
🧠 Dataset

17,880 job postings

Fraudulent vs Real

Class imbalance handled using:

class_weight='balanced'

📌 Future Improvements

JWT Authentication

Role-based Admin access

Salary normalization engine

ML Explainability (feature importance)

Dockerized deployment

CI/CD integration

Redis caching

Rate limiting

👨‍💻 Contributors

Frontend + ML: Your Name

Backend + Database: Friend Name

🏁 Final Notes

This project demonstrates:

Microservice architecture

AI + Rule hybrid fraud detection

Full-stack integration

Security implementation

Real-world scam detection use case