import { useEffect, useState } from "react";

export default function ResultCard({ result }) {
  const [animatedScore, setAnimatedScore] = useState(0);

  useEffect(() => {
    if (!result) return;

    let start = 0;
    const end = result.fraudScore;
    const duration = 800;
    const increment = end / (duration / 20);

    const counter = setInterval(() => {
      start += increment;
      if (start >= end) {
        start = end;
        clearInterval(counter);
      }
      setAnimatedScore(Math.floor(start));
    }, 20);

    return () => clearInterval(counter);
  }, [result]);

  if (!result) return null;

  const getColor = () => {
    if (result.riskLevel === "LOW") return "#16a34a";     // green
    if (result.riskLevel === "MEDIUM") return "#f59e0b"; // orange
    if (result.riskLevel === "HIGH") return "#dc2626";   // red
    return "#000";
  };

  return (
    <div className="result-card">
      <h3>Fraud Score: {animatedScore}%</h3>

      <h4 style={{ color: getColor() }}>
        Risk Level: {result.riskLevel}
      </h4>

      <ul>
        {result.reasons.map((reason, index) => (
          <li key={index}>{reason}</li>
        ))}
      </ul>
    </div>
  );
}
