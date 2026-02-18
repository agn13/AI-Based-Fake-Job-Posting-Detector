import { useEffect, useState } from "react";

export default function AdminDashboard() {

  const [jobs, setJobs] = useState([]);
  const [stats, setStats] = useState({
    total: 0,
    highRisk: 0,
    mediumRisk: 0,
    lowRisk: 0
  });

  const fetchJobs = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/jobs");
      const data = await res.json();
      setJobs(data.content || []);
    } catch (err) {
      console.error("Failed to fetch jobs", err);
    }
  };

  const fetchStats = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/jobs/stats");
      const data = await res.json();
      setStats(data);
    } catch (err) {
      console.error("Failed to fetch stats", err);
    }
  };

  const deleteJob = async (id) => {
    if (!window.confirm("Are you sure you want to delete this job?")) return;

    try {
      await fetch(`http://localhost:8080/api/jobs/${id}`, {
        method: "DELETE"
      });

      fetchJobs();
      fetchStats();
    } catch (err) {
      console.error("Delete failed", err);
    }
  };

  useEffect(() => {
    fetchJobs();
    fetchStats();
  }, []);

  return (
    <div className="container">
      <h2>Admin Dashboard</h2>

      {/* Stats Section */}
      <div style={{ marginBottom: "20px" }}>
        <p><strong>Total Jobs:</strong> {stats.total}</p>
        <p style={{ color: "red" }}><strong>High Risk:</strong> {stats.highRisk}</p>
        <p style={{ color: "orange" }}><strong>Medium Risk:</strong> {stats.mediumRisk}</p>
        <p style={{ color: "green" }}><strong>Low Risk:</strong> {stats.lowRisk}</p>
      </div>

      {jobs.length === 0 ? (
        <p>No job analyses yet.</p>
      ) : (
        <table className="dashboard-table" border="1" cellPadding="8">
          <thead>
            <tr>
              <th>Job Title</th>
              <th>Email</th>
              <th>Score</th>
              <th>Risk</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {jobs.map((item) => (
              <tr key={item.id}>
                <td>{item.jobTitle}</td>
                <td>{item.companyEmail}</td>
                <td>{item.fraudScore}%</td>
                <td style={{
                  color:
                    item.riskLevel === "HIGH"
                      ? "red"
                      : item.riskLevel === "MEDIUM"
                      ? "orange"
                      : "green"
                }}>
                  {item.riskLevel}
                </td>
                <td>
                  <button
                    style={{
                      backgroundColor: "red",
                      color: "white",
                      padding: "5px 10px",
                      border: "none",
                      cursor: "pointer"
                    }}
                    onClick={() => deleteJob(item.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
