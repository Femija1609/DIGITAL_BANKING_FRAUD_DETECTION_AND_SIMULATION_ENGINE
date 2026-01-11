import { NavLink, useNavigate } from "react-router-dom";

export default function Sidebar() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <aside className="sidebar">
      <h2 className="logo">FraudShield</h2>

      <NavLink to="/" className="nav-item">Dashboard</NavLink>
      <NavLink to="/transactions" className="nav-item">Transactions</NavLink>
      <NavLink to="/fraud-alerts" className="nav-item">Fraud Alerts</NavLink>
      <NavLink to="/high-risk" className="nav-item">High Risk</NavLink>
      <NavLink to="/manual-test" className="nav-item">Manual Test</NavLink>

      <button className="nav-item" onClick={logout}>
        Logout
      </button>
    </aside>
  );
}
