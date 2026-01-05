export default function Navbar({ user, onLogout, setPage }) {
  return (
    <div style={{
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
      padding: "15px 30px",
      background: "#0D47A1",
      color: "white"
    }}>

      <h3>💳 Digital Banking Fraud Detection</h3>

      <div style={{ display: "flex", gap: "20px", alignItems: "center" }}>
        <button onClick={() => setPage("dashboard")} style={btnStyle}>
          Dashboard
        </button>

        <button onClick={() => setPage("profile")} style={btnStyle}>
          My Profile
        </button>

        <span>{user.username}</span>

        <button onClick={onLogout} style={{ ...btnStyle, background: "#D32F2F" }}>
          Logout
        </button>
      </div>
    </div>
  );
}

const btnStyle = {
  background: "transparent",
  color: "white",
  border: "1px solid white",
  padding: "6px 12px",
  cursor: "pointer",
  borderRadius: "4px"
};
