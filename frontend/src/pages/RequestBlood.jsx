import { useState } from "react";
import { requestAPI } from "../services/api";

export default function RequestBlood() {
  const [bloodType, setBloodType] = useState("");
  const [hospital, setHospital] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const res = await requestAPI.createRequest({ bloodType, hospital });
      setMessage("Request submitted successfully!");
      setBloodType("");
      setHospital("");
    } catch (err) {
      console.error(err);
      setMessage("Error submitting request: " + err.message);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Request Blood</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Blood Type:</label>
          <input
            type="text"
            value={bloodType}
            onChange={(e) => setBloodType(e.target.value)}
            placeholder="A+, O-, etc."
            required
          />
        </div>

        <div>
          <label>Hospital:</label>
          <input
            type="text"
            value={hospital}
            onChange={(e) => setHospital(e.target.value)}
            placeholder="Hospital name"
            required
          />
        </div>

        <button type="submit">Submit Request</button>
      </form>

      {message && <p>{message}</p>}
    </div>
  );
}