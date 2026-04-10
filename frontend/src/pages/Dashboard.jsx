import { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authAPI } from "../services/api";

export default function Dashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token || token === "undefined" || token === "null") {
      navigate("/login", { replace: true });
    }
  }, [navigate]);

  const handleLogout = () => {
    authAPI.logout();
    navigate("/login", { replace: true });
  };

  const cardStyle = {
    display: "block",
    padding: "20px",
    margin: "12px auto",
    maxWidth: "360px",
    border: "1px solid var(--border, #e5e4e7)",
    borderRadius: "8px",
    textDecoration: "none",
    color: "var(--text-h, #08060d)",
    background: "var(--accent-bg, rgba(170, 59, 255, 0.1))",
    boxShadow: "var(--shadow)",
  };

  return (
    <div style={{ padding: "24px 20px" }}>
      <h1 style={{ marginBottom: "8px" }}>Dashboard</h1>
      <p style={{ marginBottom: "24px", color: "var(--text, #6b6375)" }}>
        Choose where to go next.
      </p>

      <nav
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "8px",
          alignItems: "stretch",
        }}
      >
        <Link to="/request-blood" style={cardStyle}>
          <strong>Request blood</strong>
          <div style={{ fontSize: "14px", marginTop: "6px", opacity: 0.85 }}>
            Submit a hospital blood request
          </div>
        </Link>

        <Link to="/add-donor" style={cardStyle}>
          <strong>Add donor</strong>
          <div style={{ fontSize: "14px", marginTop: "6px", opacity: 0.85 }}>
            Register a donor profile
          </div>
        </Link>

        <Link to="/" style={{ ...cardStyle, background: "transparent" }}>
          <strong>Home</strong>
          <div style={{ fontSize: "14px", marginTop: "6px", opacity: 0.85 }}>
            Back to landing page
          </div>
        </Link>

          <Link to="/my-requests" style={{ ...cardStyle, background: "transparent" }}>
          <strong>My Requests</strong>
          <div style={{ fontSize: "14px", marginTop: "6px", opacity: 0.85 }}>
            View your blood requests
          </div>
        </Link>


      </nav>

      <div style={{ marginTop: "28px" }}>
        <button type="button" onClick={handleLogout}>
          Log out
        </button>
      </div>
    </div>
  );
}
