from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import re

# =============================
# Load trained model
# =============================

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
    # Global scams
    "registration fee",
    "processing fee",
    "earn money fast",
    "guaranteed income",
    "no experience required",
    "limited slots",
    "urgent hiring",
    "payment required",
    "training fee",

    # Indian-specific scams
    "aadhaar",
    "pan card",
    "upi",
    "google pay",
    "phonepe",
    "paytm",
    "whatsapp only",
    "call hr immediately",
    "data entry work from home",
    "typing job",
    "form filling job",
    "₹",
    "rs ",
    "rs.",
    "work 2 hours daily",
    "earn 30000 weekly",
    "no interview direct joining"
]


def rule_based_risk(job: JobRequest):
    risk_score = 0
    reasons = []

    text = f"{job.jobTitle} {job.description}".lower()

    # -----------------------------
    # 1️⃣ Keyword detection
    # -----------------------------
    for keyword in SUSPICIOUS_KEYWORDS:
        if keyword in text:
            risk_score += 10
            reasons.append(f"Suspicious keyword detected: '{keyword}'")

    # -----------------------------
    # 2️⃣ Free email detection
    # -----------------------------
    if job.companyEmail:
        domain = job.companyEmail.split("@")[-1].lower()
        if domain in FREE_EMAIL_DOMAINS:
            risk_score += 15
            reasons.append("Public email domain used")

    # -----------------------------
    # 3️⃣ Unrealistic salary detection
    # -----------------------------
    if job.salary:

        salary_text = job.salary.lower().replace(",", "")
        numbers = re.findall(r"\d+", salary_text)

        if numbers:

            salary_value = int(numbers[0])

            is_per_day = "per day" in salary_text
            is_per_week = "per week" in salary_text
            is_per_month = "per month" in salary_text
            is_per_year = "per year" in salary_text or "annual" in salary_text

            # USD checks
            if "$" in salary_text:

                if is_per_day and salary_value > 2000:
                    risk_score += 20
                    reasons.append("Unrealistic daily USD salary detected")

                if is_per_year and salary_value > 300000:
                    risk_score += 20
                    reasons.append("Unrealistic annual USD salary detected")

            # INR checks
            if "₹" in salary_text or "rs" in salary_text:

                if is_per_day and salary_value > 50000:
                    risk_score += 20
                    reasons.append("Unrealistic daily INR salary detected")

                if is_per_week and salary_value > 200000:
                    risk_score += 20
                    reasons.append("Unrealistic weekly INR salary detected")

                if is_per_month and salary_value > 1000000:
                    risk_score += 20
                    reasons.append("Unrealistic monthly INR salary detected")

                if is_per_year and salary_value > 50000000:
                    risk_score += 20
                    reasons.append("Unrealistic annual INR salary detected")

    # Prevent rule engine from dominating ML
    risk_score = min(risk_score, 40)

    return risk_score, reasons

    risk_score = 0
    reasons = []

    text = f"{job.jobTitle} {job.description}".lower()

    # Keyword detection
    for keyword in SUSPICIOUS_KEYWORDS:
        if keyword in text:
            risk_score += 10   # reduced from 15
            reasons.append(f"Suspicious keyword detected: '{keyword}'")

    # Free email detection
    if job.companyEmail:
        domain = job.companyEmail.split("@")[-1].lower()
        if domain in FREE_EMAIL_DOMAINS:
            risk_score += 15   # reduced from 20
            reasons.append("Public email domain used")

    # Unrealistic salary detection
    if job.salary:
        salary_text = job.salary.lower().replace(",", "")

        numbers = re.findall(r"\d+", salary_text)

    if numbers:
        salary_value = int(numbers[0])

        # Detect time unit
        is_per_day = "per day" in salary_text
        is_per_week = "per week" in salary_text
        is_per_month = "per month" in salary_text
        is_per_year = "per year" in salary_text or "annual" in salary_text

        # USD unrealistic checks
        if "$" in salary_text:
            if is_per_day and salary_value > 2000:
                risk_score += 20
                reasons.append("Unrealistic daily USD salary detected")

            if is_per_year and salary_value > 300000:
                risk_score += 20
                reasons.append("Unrealistic annual USD salary detected")

        # INR unrealistic checks
        if "₹" in salary_text or "rs" in salary_text:

            if is_per_day and salary_value > 50000:
                risk_score += 20
                reasons.append("Unrealistic daily INR salary detected")

            if is_per_week and salary_value > 200000:
                risk_score += 20
                reasons.append("Unrealistic weekly INR salary detected")

            if is_per_month and salary_value > 1000000:
                risk_score += 20
                reasons.append("Unrealistic monthly INR salary detected")

            if is_per_year and salary_value > 50000000:
                risk_score += 20
                reasons.append("Unrealistic annual INR salary detected")

    # Cap rule score so it doesn’t dominate
    risk_score = min(risk_score, 40)

    return risk_score, reasons


def calculate_risk_level(score):
    if score >= 75:
        return "HIGH"
    elif score >= 40:
        return "MEDIUM"
    else:
        return "LOW"


# =============================
# Prediction Endpoint
# =============================

@app.post("/predict", response_model=PredictionResponse)
def predict_job(job: JobRequest):

    # 1️⃣ ML Probability
    text = f"{job.jobTitle} {job.description}"
    prob = model.predict_proba([text])[0][1]
    ml_score = prob * 100

    # 2️⃣ Rule-Based Score
    rule_score, reasons = rule_based_risk(job)

    # 3️⃣ Weighted Hybrid Score
    final_score = (0.75 * ml_score) + (0.25 * rule_score)

    final_score = min(100, round(final_score, 2))

    # 4️⃣ Risk Level
    risk_level = calculate_risk_level(final_score)

    if not reasons:
        reasons.append("No major suspicious patterns detected")

    return {
        "fraudScore": final_score,
        "riskLevel": risk_level,
        "reasons": reasons
    }
