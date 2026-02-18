export const analyzeJob = async (data) => {
  console.log("Sending to backend:", data);

  const response = await fetch(
    "http://localhost:8080/api/jobs/analyze",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    console.error("Backend error:", errorText);
    throw new Error("Failed to analyze job");
  }

  const result = await response.json();
  console.log("Backend response:", result);

  return result;
};
