import pandas as pd
import numpy as np
import re
import os
import joblib

from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.metrics import accuracy_score, classification_report


# ============================
# 1️⃣ Simple Text Cleaning
# ============================

def clean_text(text):
    text = str(text).lower()
    text = re.sub(r"http\S+", "", text)       # remove urls
    text = re.sub(r"[^a-zA-Z\s]", "", text)   # remove special chars
    text = re.sub(r"\s+", " ", text).strip()  # remove extra spaces
    return text


# ============================
# 2️⃣ Load Dataset
# ============================

print("Loading dataset...")

# 🔴 IMPORTANT:
# Place Kaggle CSV file inside:
# ml-model/data/fake_job_postings.csv

data_path = "../data/fake_job_postings.csv"
df = pd.read_csv(data_path)
print("Dataset shape:", df.shape)
print("\nClass distribution:")
print(df["fraudulent"].value_counts())
print("\nUnique classes:", df["fraudulent"].unique())


# Combine useful columns
df["text"] = (
    df["title"].fillna("") + " " +
    df["description"].fillna("")
)

df["text"] = df["text"].apply(clean_text)

X = df["text"]
y = df["fraudulent"]   # 1 = fake, 0 = real


# ============================
# 3️⃣ Train Test Split
# ============================

X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.2,
    random_state=42,
    stratify=y   # 👈 CRITICAL
)


print("Training model...")

# ============================
# 4️⃣ Build Pipeline
# ============================

model = Pipeline([
    ("tfidf", TfidfVectorizer(max_features=5000)),
    ("clf", LogisticRegression(
        max_iter=1000,
        class_weight="balanced"
    ))
])


model.fit(X_train, y_train)


# ============================
# 5️⃣ Evaluate
# ============================

y_pred = model.predict(X_test)
accuracy = accuracy_score(y_test, y_pred)

print(f"\nModel Accuracy: {accuracy:.4f}")
print("\nClassification Report:")
print(classification_report(y_test, y_pred))


# ============================
# 6️⃣ Save Model
# ============================

os.makedirs("../models", exist_ok=True)

model_path = "../models/scam_model.pkl"
joblib.dump(model, model_path)

print(f"\nModel saved at: {model_path}")
