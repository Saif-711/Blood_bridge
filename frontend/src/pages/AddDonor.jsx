import { useState } from "react";
import { donorAPI } from "../services/api";

export default function AddDonor() {
  const [name, setName] = useState("");
  const [bloodType, setBloodType] = useState("");
  const [location, setLocation] = useState("");
  const [available, setAvailable] = useState(true);
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const res = await donorAPI.addDonor({ name, bloodType, location, available });
      setMessage("Donor added successfully!");
      setName("");
      setBloodType("");
      setLocation("");
      setAvailable(true);
    } catch (err) {
      console.error(err);
      setMessage("Error adding donor: " + err.message);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Add Donor</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name:</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Blood Type:</label>
          <input
            type="text"
            value={bloodType}
            onChange={(e) => setBloodType(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Location:</label>
          <input
            type="text"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            required
          />
        </div>

        <div>
          <label>
            Available:
            <input
              type="checkbox"
              checked={available}
              onChange={(e) => setAvailable(e.target.checked)}
            />
          </label>
        </div>

        <button type="submit">Add Donor</button>
      </form>

      {message && <p>{message}</p>}
    </div>
  );
}