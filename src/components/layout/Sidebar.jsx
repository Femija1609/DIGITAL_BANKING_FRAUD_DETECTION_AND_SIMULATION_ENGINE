import { NavLink, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "../../services/api";

export default function Sidebar() {
  const navigate = useNavigate();

  const [counts, setCounts] = useState({
    transactions: 0,
    fraud: 0,
    highRisk: 0,
  });

  // 🔔 AUTO REFRESH NOTIFICATIONS
  useEffect(() => {
    const loadCounts = () => {
      api.get("/notifications/count")
        .then(res => {
          setCounts({
            transactions: res.data.transactions || 0,
            fraud: res.data.fraud || 0,
            highRisk: res.data.highRisk || 0,
          });
        })
        .catch(() => {});
    };

    loadCounts();
    const interval = setInterval(loadCounts, 3000);
    return () => clearInterval(interval);
  }, []);

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <aside className="sidebar">
      <h2 className="logo">PAY-WATCH 🔰</h2>

      <NavLink to="/" className="nav-item">DASHBOARD</NavLink>

      <NavLink to="/transactions" className="nav-item">
        TRANSACTIONS
        {counts.transactions > 0 && (
          <span className="badge badge-warning">{counts.transactions}</span>
        )}
      </NavLink>

      <NavLink to="/fraud-alerts" className="nav-item">
        FRAUD ALERTS
        {counts.fraud > 0 && (
          <span className="badge badge-danger">{counts.fraud}</span>
        )}
      </NavLink>

      <NavLink to="/high-risk" className="nav-item">
        HIGH RISK
        {counts.highRisk > 0 && (
          <span className="badge badge-warning">{counts.highRisk}</span>
        )}
      </NavLink>

      <NavLink to="/manual-test" className="nav-item">MANUAL TEST</NavLink>

      <button className="logout-btn" onClick={logout}>
        LOGOUT
      </button>
    </aside>
  );
}
