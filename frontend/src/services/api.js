
const BASE_URL = "http://localhost:8081";
 
function getHeaders() {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
}
 
async function handleResponse(res) {
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || `Error ${res.status}`);
  }
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}
 
export const authAPI = {
  register: (data) =>
    fetch(`${BASE_URL}/auth/register`, {
      method: "POST",
      headers: getHeaders(),
      body: JSON.stringify(data),
    }).then(handleResponse),

  login: (data) =>
    fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: getHeaders(),
      body: JSON.stringify(data),
    }).then(handleResponse),
};

// Donor API
export const donorAPI = {
  addDonor: (data) =>
    fetch(`${BASE_URL}/donors`, {
      method: "POST",
      headers: getHeaders(),
      body: JSON.stringify(data),
    }).then(handleResponse),

  getAllDonors: () =>
    fetch(`${BASE_URL}/donors`, {
      headers: getHeaders(),
    }).then(handleResponse),

  getAvailableDonors: () =>
    fetch(`${BASE_URL}/donors/available`, {
      headers: getHeaders(),
    }).then(handleResponse),
};

// Blood Request API
export const requestAPI = {
  createRequest: (data) =>
    fetch(`${BASE_URL}/requests`, {
      method: "POST",
      headers: getHeaders(),
      body: JSON.stringify(data),
    }).then(handleResponse),

  getAllRequests: () =>
    fetch(`${BASE_URL}/requests`, {
      headers: getHeaders(),
    }).then(handleResponse),

  getRequestsByStatus: (status) =>
    fetch(`${BASE_URL}/requests/status/${status}`, {
      headers: getHeaders(),
    }).then(handleResponse),
};