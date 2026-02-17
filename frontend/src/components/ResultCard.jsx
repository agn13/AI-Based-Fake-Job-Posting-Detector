import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer
} from "recharts";

export default function ResultCard({ result }) {
  const COLORS = ["#16a34a", "#f59e0b", "#dc2626"];

  const data = [
    { name: "Safe", value: 100 - result.fraudScore },
    { name: "Risk", value: result.fraudScore }
  ];

  const getRiskColor = () => {
    if (result.riskLevel === "LOW") return "#16a34a";
    if (result.riskLevel === "MEDIUM") return "#f59e0b";
    if (result.riskLevel === "HIGH") return "#dc2626";
  };

  return (
    <div className="result-card">
      <h3 style={{ color: getRiskColor() }}>
        Risk Level: {result.riskLevel}
      </h3>

      <div className="chart-container">
        <ResponsiveContainer width="100%" height={250}>
          <PieChart>
            <Pie
              data={data}
              dataKey="value"
              outerRadius={90}
              label
            >
              {data.map((entry, index) => (
                <Cell key={index} fill={COLORS[index]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        </ResponsiveContainer>
      </div>

      <ul>
        {result.reasons.map((reason, index) => (
          <li key={index}>{reason}</li>
        ))}
      </ul>
    </div>
  );
}
