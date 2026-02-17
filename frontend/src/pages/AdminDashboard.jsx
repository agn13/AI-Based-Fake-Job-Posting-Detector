export default function AdminDashboard({ history }) {
  return (
    <div className="container">
      <h2>Admin Dashboard</h2>

      {history.length === 0 ? (
        <p>No job analyses yet.</p>
      ) : (
        <table className="dashboard-table">
          <thead>
            <tr>
              <th>Job Title</th>
              <th>Email</th>
              <th>Score</th>
              <th>Risk</th>
            </tr>
          </thead>
          <tbody>
            {history.map((item, index) => (
              <tr key={index}>
                <td>{item.jobTitle}</td>
                <td>{item.companyEmail}</td>
                <td>{item.fraudScore}%</td>
                <td>{item.riskLevel}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
