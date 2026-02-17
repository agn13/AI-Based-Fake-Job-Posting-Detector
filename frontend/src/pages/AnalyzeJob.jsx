import JobForm from "../components/JobForm";

export default function AnalyzeJob({ history, setHistory }) {
  return (
    <div className="container">
      <h2>Job Fraud Analyzer</h2>
      <JobForm history={history} setHistory={setHistory} />
    </div>
  );
}
