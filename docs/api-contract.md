POST /api/jobs/analyze

Request:
- jobTitle: string
- jobDescription: string
- companyEmail: string
- salary: string

Response:
- fraudScore: number (0–100)
- riskLevel: LOW | MEDIUM | HIGH
- reasons: string[]