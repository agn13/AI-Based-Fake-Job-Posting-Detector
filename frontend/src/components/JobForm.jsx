import { useState } from "react";
import { analyzeJob } from "../services/api";
import ResultCard from "./ResultCard";

const initialFormState = {
  jobTitle: "",
  companyEmail: "",
  salary: "",
  description: "",
};

export default function JobForm({ history, setHistory }) {
  const [formData, setFormData] = useState(initialFormState);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setResult(null);

    const response = await analyzeJob(formData);

    setResult(response);
    setHistory([...history, { ...formData, ...response }]);
    setFormData(initialFormState);
    setLoading(false);
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <input
          name="jobTitle"
          placeholder="Job Title"
          value={formData.jobTitle}
          onChange={handleInputChange}
          required
        />

        <input
          name="companyEmail"
          placeholder="Company Email"
          value={formData.companyEmail}
          onChange={handleInputChange}
          required
        />

        <input
          name="salary"
          placeholder="Salary"
          value={formData.salary}
          onChange={handleInputChange}
          required
        />

        <textarea
          name="description"
          placeholder="Job Description"
          value={formData.description}
          onChange={handleInputChange}
          required
        />

        <button type="submit">
          {loading ? "Analyzing..." : "Analyze"}
        </button>
      </form>

      {loading && <div className="spinner"></div>}

      {result && <ResultCard result={result} />}
    </>
  );
}
