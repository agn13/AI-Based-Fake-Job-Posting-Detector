import { useState } from "react";
import AnalyzeJob from "./pages/AnalyzeJob";
import AdminDashboard from "./pages/AdminDashboard";
import "./styles/global.css";

function App() {
  const [darkMode, setDarkMode] = useState(false);
  const [page, setPage] = useState("analyze");
  const [history, setHistory] = useState([]);

  return (
    <div className={darkMode ? "app dark" : "app"}>
      <nav className="navbar">
        <h1>HireShield AI</h1>

        <div className="nav-buttons">
          <button onClick={() => setPage("analyze")}>Analyze</button>
          <button onClick={() => setPage("dashboard")}>Dashboard</button>
          <button
            className="toggle-btn"
            onClick={() => setDarkMode(!darkMode)}
          >
            {darkMode ? "Light" : "Dark"}
          </button>
        </div>
      </nav>

      {page === "analyze" && (
        <AnalyzeJob history={history} setHistory={setHistory} />
      )}

      {page === "dashboard" && <AdminDashboard history={history} />}
    </div>
  );
}

export default App;
