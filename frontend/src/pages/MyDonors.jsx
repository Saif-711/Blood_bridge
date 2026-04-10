import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { donorAPI } from "../services/api";

export default function MyDonors() {
    const navigate = useNavigate();
    const [donors , setDonors] = useState([]);
    const [message, setMessage] = useState("");
    useEffect(()=>{
            const token = localStorage.getItem("token");
            if(!token || token === "undefined" || token === "null"){
                navigate("/login", { replace: true });
                return;
            }
            const fetchDonors = async () => {
                try{
                    const res = await donorAPI.getMyDonors();
                    setDonors(res);
                }catch(err){
                    console.error(err);
                    setMessage("Error fetching donors: "+ err.message);
                }
            };
            fetchDonors();
    },[navigate]);

    return (
    <div style={{padding: "20px"}}>
        <p style={{ marginBottom: "12px" }}>
        <Link to="/dashboard">← Dashboard</Link>
        </p>
        <h2>My Donors</h2>
        <table style={{width: "100%", borderCollapse: "collapse", marginTop: "12px"}}>
            <thead>
                <tr>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>Available</th>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>Blood Type</th>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>Location</th>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>Name</th>
                    <th style={{border: "1px solid #ddd", padding: "8px"}}>User ID</th>
                </tr>
            </thead>
            <tbody>
                {donors.length === 0 ? (
                    <tr>
                        <td colSpan="3" style={{border: "1px solid #ddd", padding: "8px", textAlign: "center"}}>
                            No Donors found. 
                        </td>
                    </tr>
                ) : (
                    donors.map((donor) => (
                        <tr key={donor.id}>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{donor.available ? "Yes" : "No"}</td>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{donor.bloodType}</td>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{donor.location}</td>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{donor.name}</td>
                            <td style={{border: "1px solid #ddd", padding: "8px"}}>{donor.user.id}</td>
                        </tr>
                    ))
                )}
            </tbody>
        </table>
    </div>
);

}