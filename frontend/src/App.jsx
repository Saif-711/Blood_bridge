import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import RequestBlood from "./pages/RequestBlood";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/request" element={<RequestBlood />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;