from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import re

# Load trained model
model_path = "../models/scam_model.pkl"
model = joblib.load(model_path)

app = FastAPI(title="HireShield ML API")

# =============================
# Request / Response Schemas
# =============================

class JobRequest(BaseModel):
    jobTitle: str
    description: str
    companyEmail: str | None = None
    salary: str | None = None


class PredictionResponse(BaseModel):
    fraudScore: float
    riskLevel: str
    reasons: list[str]


# =============================
# Rule-Based Risk Signals
# =============================

FREE_EMAIL_DOMAINS = ["gmail.com", "yahoo.com", "outlook.com"]

SUSPICIOUS_KEYWORDS = [
    "registration fee",
    "pay fee",
    "earn money fast",
    "guaranteed income",
    "no experience required",
    "limited slots",
    "urgent hiring",
    "processing fee"
]


def rule_based_risk(job: JobRequest):
    risk_score = 0
    reasons = []

    text = f"{job.jobTitle} {job.description}".lower()

    # Keyword detection
    for keyword in SUSPICIOUS_KEYWORDS:
        if keyword in text:
            risk_score += 15
            reasons.append(f"Suspicious keyword detected: '{keyword}'")

    # Free email detection
    if job.companyEmail:
        domain = job.companyEmail.split("@")[-1].lower()
        if domain in FREE_EMAIL_DOMAINS:
            risk_score += 20
            reasons.append("Public email domain used")

    # Unrealistic salary detection
    if job.salary:
        numbers = re.findall(r"\d+", job.salary.replace(",", ""))
        if numbers:
            salary_value = int(numbers[0])
            if salary_value > 200000:  # unrealistic threshold
                risk_score += 20
                reasons.append("Unrealistic salary detected")

    return risk_score, reasons


def calculate_risk_level(score):
    if score < 40:
        return "LOW"
    elif score < 70:
        return "MEDIUM"
    else:
        return "HIGH"


# =============================
# Prediction Endpoint
# =============================

@app.post("/predict", response_model=PredictionResponse)
def predict_job(job: JobRequest):

    # ML probability
    text = f"{job.jobTitle} {job.description}"
    prob = model.predict_proba([text])[0][1]
    ml_score = prob * 100

    # Rule-based score
    rule_score, reasons = rule_based_risk(job)

    # Combine scores
    final_score = min(ml_score + rule_score, 100)
    final_score = round(final_score, 2)

    risk_level = calculate_risk_level(final_score)

    if not reasons:
        reasons.append("No major suspicious patterns detected")

    return {
        "fraudScore": final_score,
        "riskLevel": risk_level,
        "reasons": reasons
    }
