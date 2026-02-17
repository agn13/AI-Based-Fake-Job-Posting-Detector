export const analyzeJob = async (data) => {
  console.log("Sending data:", data);

  // Simulated delay (fake API call)
  await new Promise((resolve) => setTimeout(resolve, 800));

  return {
    fraudScore: 78,
    riskLevel: "HIGH",
    reasons: [
      "Public email domain detected",
      "Unrealistic salary offered",
      "Payment request keyword found"
    ]
  };
};
