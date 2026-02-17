import joblib
import os

# Load model
model_path = "../models/scam_model.pkl"
model = joblib.load(model_path)

def predict_job(title, description):
    text = f"{title} {description}"
    prob = model.predict_proba([text])[0][1]  # probability of class 1 (fake)
    return round(prob * 100, 2)


if __name__ == "__main__":
    title = input("Enter Job Title: ")
    description = input("Enter Job Description: ")

    score = predict_job(title, description)

    print(f"\nFraud Probability: {score}%")
