import { useState } from "react";
import { analyzeJob } from "../services/api";
import ResultCard from "./ResultCard";

const FORM_FIELDS = [
  { name: "jobTitle", label: "Job Title", type: "text" },
  { name: "companyEmail", label: "Company Email", type: "email" },
  { name: "salary", label: "Salary", type: "text" },
];

const initialFormState = {
  jobTitle: "",
  companyEmail: "",
  salary: "",
  description: "",
};

export default function JobForm() {
  const [formData, setFormData] = useState(initialFormState);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const validateForm = () => {
    const { jobTitle, companyEmail, salary, description } = formData;

    if (!jobTitle.trim()) return "Job Title is required";
    if (!companyEmail.trim()) return "Company Email is required";
    if (!salary.trim()) return "Salary is required";
    if (!description.trim()) return "Job Description is required";
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(companyEmail))
      return "Invalid email format";

    return null;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setError(null);
  };

  const resetForm = () => {
    setFormData(initialFormState);
    setError(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await analyzeJob(formData);
      setResult(response);
      resetForm();  // now safe
    } catch (err) {
      setError(
        err.message || "Failed to analyze job posting. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        {error && <div className="error-message">{error}</div>}

        {FORM_FIELDS.map((field) => (
          <div key={field.name}>
            <label htmlFor={field.name}>{field.label}</label>
            <input
              id={field.name}
              type={field.type}
              name={field.name}
              value={formData[field.name]}
              onChange={handleInputChange}
              disabled={loading}
              required
            />
          </div>
        ))}

        <div>
          <label htmlFor="description">Job Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            disabled={loading}
            required
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Analyzing..." : "Analyze"}
        </button>
      </form>

      {loading && <div className="spinner"></div>}

      {result && <ResultCard result={result} />}
    </>
  );
}
