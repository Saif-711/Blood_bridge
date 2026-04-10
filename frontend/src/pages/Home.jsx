import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div style={{ padding: "20px" }}>
      <h1>Welcome to Blood Donation Platform</h1>

      <p>Save lives by donating blood ❤️</p>

      <Link to="/login">
        <button>Login</button>
      </Link>

      <Link to="/register">
        <button>Register</button>
      </Link>

      <Link to="/dashboard">
        <button>Dashboard</button>
      </Link>
    </div>
  );
}