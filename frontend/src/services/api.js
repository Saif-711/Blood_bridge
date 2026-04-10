const BASE_URL = "http://localhost:8081";

function getHeaders(auth = true) {
  const headers = {
    "Content-Type": "application/json",
  };

  if (auth) {
    const token = localStorage.getItem("token");

    if (token && token !== "undefined" && token !== "null") {
      headers.Authorization = `Bearer ${token}`; // ✅ FIXED
    }
  }
  return headers;
}

// ✅ safer response handler
async function handleResponse(res) {
  const text = await res.text();

  if (!res.ok) {
    throw new Error(text || `Error ${res.status}`);
  }

  try {
    return text ? JSON.parse(text) : null;
  } catch {
    return text;
  }
}

// ================= AUTH =================
export const authAPI = {
  // ✅ DON'T send token in register
  register: (data) =>
    fetch(`${BASE_URL}/auth/register`, {
      method: "POST",
      headers: getHeaders(false),
      body: JSON.stringify(data),
    }).then(handleResponse),

  // ✅ DON'T send token in login
  login: async (data) => {
    const res = await fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: getHeaders(false),
      body: JSON.stringify(data),
    });

    const result = await handleResponse(res);

    // Backend returns JWT as plain text (not JSON); handleResponse gives a string in that case
    const token =
      typeof result === "string"
        ? result.trim()
        : result?.token || result?.accessToken;

    if (token) {
      localStorage.setItem("token", token);
      console.log("Saved token:", token);
    } else {
      console.error("No token received from backend:", result);
    }

    return result;
  },

  // ✅ optional logout helper
  logout: () => {
    localStorage.removeItem("token");
  },
};

// ================= DONOR =================
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

  getMyDonors: () =>
    fetch(`${BASE_URL}/donors/my-donors`, {
      headers: getHeaders(),
    }).then(handleResponse),
};

// ================= REQUEST =================
export const requestAPI = {
  
  createRequest: (data) => {
  console.log("TOKEN:", localStorage.getItem("token"));

  return fetch(`${BASE_URL}/requests`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(data),
  }).then(handleResponse);
},  

  getAllRequests: () =>
    fetch(`${BASE_URL}/requests`, {
      headers: getHeaders(),
    }).then(handleResponse),

  getRequestsByStatus: (status) =>
    fetch(`${BASE_URL}/requests/status/${status}`, {
      headers: getHeaders(),
    }).then(handleResponse),

  getMyRequests: () =>
    fetch (`${BASE_URL}/requests/my-requests`,{
      headers: getHeaders(),
    }).then(handleResponse),    
};