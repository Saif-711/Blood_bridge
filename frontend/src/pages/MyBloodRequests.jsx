import {useNavigate , Link} from 'react-router-dom';
import {requestAPI} from '../services/api';
import { useEffect , useState } from 'react';
export default function MyBloodRequests() {
    const navigate = useNavigate();
    const [requests, setRequests] = useState([]);
    const [message, setMessage] = useState("");
    useEffect(() => {
        const token = localStorage.getItem("token");

        if (!token || token === "undefined" || token === "null") {
            navigate("/login", { replace: true });
            return;
        }

        const fetchRequests = async () => {
            try {
                const res = await requestAPI.getMyRequests();
                setRequests(res);
            } catch (err) {
                console.error(err);
                setMessage("Error fetching requests: " + err.message);
            }
        };

        fetchRequests();
    }, [navigate]);

   

return (
    <div style={{padding: "20px"}}>
        <p style={{ marginBottom: "12px" }}>
        <Link to="/dashboard">← Dashboard</Link>
        </p>
        <h2>My Blood Requests</h2>
        <table style={{width: "100%", borderCollapse: "collapse", marginTop: "12px"}}>
            <thead>
                <tr>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>Blood Type</th>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>Hospital</th>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>Status</th>
                </tr>
            </thead>
            <tbody>
                {requests.length === 0 ? (
                    <tr>
                        <td colSpan="3" style={{border: "1px solid #ddd", padding: "8px", textAlign: "center"}}>
                            No requests found. 
                        </td>
                    </tr>
                ) : (
                    requests.map((request) => (
                        <tr key={request.id}>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{request.bloodType}</td>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{request.hospital}</td>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{request.status}</td>
                        </tr>
                    ))
                )}
            </tbody>
        </table>
    </div>
);
}

