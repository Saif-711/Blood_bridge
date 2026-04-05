import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div style={{ padding: "20px" }}>
      <h1>Welcome to Blood Donation Platform</h1>

      <p>Save lives by donating blood ❤️</p>

      <Link to="/request">
        <button>Request Blood</button>
      </Link>

      <Link to="/login">
        <button>Login</button>
      </Link>
    </div>
  );
}