import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import RequestBlood from "./pages/RequestBlood";
import AddDonor from "./pages/AddDonor";
import MyBloodRequests from "./pages/MyBloodRequests";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/request-blood" element={<RequestBlood />} />
        <Route path="/add-donor" element={<AddDonor />} />
        <Route path="/my-requests" element={<MyBloodRequests />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;